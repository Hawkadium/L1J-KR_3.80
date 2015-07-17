package server.controller.Robot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.bean.RobotLocation;
import l1j.server.GameSystem.bean.RobotMent;
import l1j.server.GameSystem.bean.RobotName;
import l1j.server.server.command.executor.L1Robot;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class RobotAIThread implements Runnable {	
	
	
	static private final int Scarecrow = 1;  // ����ƺ�
	static private final int Hunt = 2;       // ���
	
	// �ΰ����� �����带 ó���ص��Ǵ��� Ȯ�ο�.
	static private boolean running;
	// �����尡 ������ ��� �޽��� �ð���.
	static private long sleep;
	// ������ �κ��鰴ü
	static private FastTable<L1PcInstance> huntList;
	// ������ �κ��鰴ü
	static private FastTable<L1PcInstance> scarecrowList;
	// �ڷ���Ʈ�� ��ǥ���
	static private List<RobotLocation> list_location;
	// �ڷ���Ʈ�� ��ǥ���
	static private List<RobotMent> list_ment;
	// ������ �ɸ��� ���
	static private List<RobotName> list_name;
	static public int list_name_idx;
	
	/**
	 * �ʱ�ȭ ó�� �Լ�.
	 */
	static public void init(){
		// ���� �ʱ�ȭ.
		sleep = 10;
		list_name_idx = 0;
		huntList = new FastTable<L1PcInstance>();
		scarecrowList = new FastTable<L1PcInstance>();
		list_location = new ArrayList<RobotLocation>();
		list_ment = new ArrayList<RobotMent>();
		list_name = new ArrayList<RobotName>();
		// �κ� �ΰ����ɿ� ������ Ȱ��ȭ.
		new Thread(new RobotAIThread()).start();
		new Thread(new RobotScarecrow()).start();
		// ���κ��� ���� ����.
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			pstm = con.prepareStatement("SELECT * FROM robot_location where count = '1'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotLocation rl = new RobotLocation();
				rl.uid = rs.getInt("uid");
				rl.x = rs.getInt("x");
				rl.y = rs.getInt("y");
				rl.map = rs.getInt("map");
				rl.etc = rs.getString("etc");
				
				list_location.add(rl);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("SELECT * FROM robot_message");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotMent rm = new RobotMent();
				rm.uid = rs.getInt("uid");
				rm.type = rs.getString("type");
				rm.ment = rs.getString("ment");
				
				list_ment.add(rm);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("SELECT * FROM robot_name");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotName rn = new RobotName();
				rn.uid = rs.getInt("uid");
				rn.name = rs.getString("name");
				list_name.add(rn);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static boolean doesCharNameExist(String name) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT name FROM robot_name WHERE name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
	
	/**
	 * ���� ó�� �Լ�.
	 */
	static public void close(){
		running = false;
	}
	
	/**
	 * ���� ��Ͽ� �߰���û ó�� �Լ�.
	 * @param robot
	 */
	static public void append(L1PcInstance robot, int type){
		switch (type) {
		case Scarecrow:
			synchronized (scarecrowList) {
				scarecrowList.add(robot);
			}
			break;
		case Hunt:
			synchronized (huntList) {
				huntList.add(robot);
			}
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * ������Ͽ��� ���ſ�û ó�� �Լ�.
	 * @param robot
	 */
	static public void remove(L1PcInstance robot, int type){
		switch (type) {
		case Scarecrow:
			synchronized (scarecrowList) {
				scarecrowList.remove(robot);
			}
			break;
		case Hunt:
			synchronized (huntList) {
				huntList.remove(robot);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * ���� ��� ĳ�� ����Ʈ
	 */
	private static Collection<L1PcInstance> huntValues;
	static public Collection<L1PcInstance> getHunt() {
		Collection<L1PcInstance> vs = huntValues;
		return (vs != null) ? vs : (huntValues = Collections.unmodifiableCollection(huntList));
	}
	
	@Override
	public void run(){
		try {
			long time = System.currentTimeMillis();
			while (true) {
				try {					
					time = System.currentTimeMillis();
					// �κ��� �ΰ����� Ȱ��ȭ.
					for(L1PcInstance robot : huntList) {
						if (robot == null) {
							continue;
						}
						robot.getRobotAi().toAI(time);
					}
				} catch (Exception e) {
					System.out.println("�κ� �����尡 ������������ ���� �Ǿ� AI ������� �ߺ����� �߻�!");
					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(sleep);
					} catch (Exception e) {	}
				}
			}
		} catch (Exception e) {			
		} finally {
			try {
				Thread.sleep(sleep);
			} catch (Exception e) {	}
		}
	}
	
	
	static class RobotScarecrow implements Runnable {
		@Override
		public void run() {
			try {
				long time = System.currentTimeMillis();
				while (true) {
					try {						
						time = System.currentTimeMillis();
						// �κ��� �ΰ����� Ȱ��ȭ.				
						for(L1PcInstance robot : scarecrowList) {
							if (robot == null) {
								continue;
							}
							robot.getRobotAi().toAI(time);
						}
					} catch (Exception e) {
						System.out.println("�κ� �����尡 ������������ ���� �Ǿ� AI ������� �ߺ����� �߻�!");
						e.printStackTrace();
					} finally {
						try {
							Thread.sleep(sleep);
						} catch (Exception e) {	}
					}
				}
			} catch (Exception e) {	
			} finally {
				try {
					Thread.sleep(sleep);
				} catch (Exception e) {	}
			}
		}
	}
	
	/**
	 * ����� �ڷ���Ʈ ��ǥ����.
	 * @param type
	 * @return
	 */
	static public RobotLocation getLocation() {
		if(list_location.size() == 0)
			return null;
		
		return list_location.get( L1Robot.random(0, list_location.size()-1));
	}
	
	static public List<RobotMent> getRobotMent(){
		return list_ment;
	}
	
	static public List<RobotName> getRobotName(){
		return list_name;
	}
	
	static public String getName(){
		try {
			// �̸���� ��ȸ.
			for( ; list_name_idx < list_name.size() ; ){
				String name = list_name.get(list_name_idx++).name;
				Connection con = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				System.out.println(name);
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
					pstm.setString(1, name);
					rs = pstm.executeQuery();
					if(!rs.next())
						return name;
				} catch (SQLException e) {
				} finally {
					SQLUtil.close(rs);
					SQLUtil.close(pstm);
					SQLUtil.close(con);
				}
			}
		} catch (Exception e) { }
		// ��� ������ �̸� ���� Ȯ��.
		// �������� �̵������� �����Ұ�� ����.
		return null;
	}
	
	static public void reload() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		list_location.clear();
		list_ment.clear();
		list_name.clear();
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			pstm = con.prepareStatement("SELECT * FROM robot_location where count = '1'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotLocation rl = new RobotLocation();
				rl.uid = rs.getInt("uid");
				rl.x = rs.getInt("x");
				rl.y = rs.getInt("y");
				rl.map = rs.getInt("map");
				rl.etc = rs.getString("etc");
				
				list_location.add(rl);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("SELECT * FROM robot_message");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotMent rm = new RobotMent();
				rm.uid = rs.getInt("uid");
				rm.type = rs.getString("type");
				rm.ment = rs.getString("ment");
				
				list_ment.add(rm);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("SELECT * FROM robot_name");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotName rn = new RobotName();
				rn.uid = rs.getInt("uid");
				rn.name = rs.getString("name");
				list_name.add(rn);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
		} catch (SQLException e) {
			
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
