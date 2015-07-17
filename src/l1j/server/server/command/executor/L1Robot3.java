package l1j.server.server.command.executor;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.GameSystem.RobotThread;
import l1j.server.GameSystem.bean.RobotFishing;
import l1j.server.server.ActionCodes;
import l1j.server.server.BadNamesList;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import server.controller.Robot.RobotControler;

public class L1Robot3 implements L1CommandExecutor {

	private static Random _random =  new Random(System.nanoTime());


	private static final int[] MALE_LIST = new int[] { 61, 138, 2786, 48, 37, 2796};
	private static final int[] FEMALE_LIST = new int[] { 61, 138, 2786, 48, 37, 2796 };
	//private static final int[] MALE_LIST = new int[] { 61, 138, 734, 2786, 6658, 48, 37, 1186, 2796, 6661, 6650 };
	//private static final int[] FEMALE_LIST = new int[] { 48, 37, 1186, 2796, 6661, 6650 };
	//private static final int[] WEAPON_LIST = new int[] { 41, 172, 125, 80, 52, 410003 };

	private L1Robot3() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Robot3();
	}

	
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);	
			//CodeLogger.getInstance().gmlog(
					//"GMCOMMAND",
				//	"ĳ��=" + pc.getName() + "[" + pc.getAccountName() + "]"
					//		+ "	��ɾ�="  +"  �κ�  " + arg + "	IP="
						//	+ pc.getNetConnection().getHostname());
			int robot = Integer.parseInt(tok.nextToken());
			int count = Integer.parseInt(tok.nextToken());
			int isteleport = 0;
			
			try {
				isteleport = Integer.parseInt(tok.nextToken());
			} catch (Exception e) {
				isteleport = 0;
			}
			
			int SearchCount = 0;
			
			L1Map map = pc.getMap();
			
			int x = 0;
			int y = 0;

			int[] loc = { -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8 };
			
			pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
			while(count-- > 0){
				String name = RobotThread.getName();
				if(name == null){
					pc.sendPackets(new S_SystemMessage( "���̻� ������ �̸��� ���������ʽ��ϴ�." ));
					RobotThread.list_name_idx = 0;
					return;
				}
				
				L1PcInstance player = L1World.getInstance().getPlayer(name);
				
				if (player != null) {
					continue;
				}
				
				L1PcInstance newPc = new L1PcInstance();
				newPc.setAccountName("");
				newPc.setId(ObjectIdFactory.getInstance().nextId());
				newPc.setName(name);

				if (robot == 0) { // �ΰ�����ON ���ڸ�������� ����
					newPc.setHighLevel(10);//��������
					newPc.setLevel(10);
					newPc.setExp(0);
					newPc.setLawful(0);
					newPc.setClanid(0);
					newPc.setClanname("");
//					���� ȣĪ
					int rnd = _random.nextInt(8);
					switch(rnd){
					case 0:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 1:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 2:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 3:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 4:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 5:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 6:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 7:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
                    break;
					}
//			���� ȣĪ
				} else if (robot == 1){ //�ΰ�����ON ����.��ų.����.������� ���½ð��帣������ֵ� ����
					int rnd = _random.nextInt(8);
					newPc.setHighLevel(45);//��������
					newPc.setLevel(45);
					newPc.setExp(28877490);
					newPc.setClanid(0);
					newPc.setClanname("");
					switch(rnd){
					case 0:
					newPc.setLawful(32767);
					break;
					case 1:
					newPc.setLawful(2000);
					break;
					case 2:
					newPc.setLawful(3000);
					break;
					case 3:
					newPc.setLawful(4000);
					break;
					case 4:
					newPc.setLawful(5000);
					break;
					case 5:
					newPc.setLawful(6000);
					break;
					case 6:
					newPc.setLawful(7000);
					break;
					case 7:
					newPc.setLawful(7500);
					break;	
					}
					switch(rnd){
					case 0:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 1:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 2:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 3:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 4:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 5:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 6:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 7:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						}
				} else if (robot == 2){ //�ΰ�����ON  ����.��ų.����.���������½ð��帣������ֵ� ����
					newPc.setHighLevel(45);//��������
					newPc.setLevel(45);
					newPc.setExp(28877490);
					newPc.setLawful(-20000);
					newPc.setClanid(0);
					newPc.setClanname("");
					int rnd = _random.nextInt(8);
					switch(rnd){
					case 0:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 1:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 2:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 3:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 4:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 5:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 6:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 7:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						}
				} else if (robot == 3) { //�ΰ�����ON  ����.������� �ʹݿ��¿� ����
					newPc.setHighLevel(25);//��������
					newPc.setLevel(25);
					newPc.setExp(0);
					newPc.setLawful(0);
					newPc.setClanid(0);
					newPc.setClanname("");
//					���� ȣĪ
					int rnd = _random.nextInt(8);
					switch(rnd){
					case 0:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 1:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 2:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 3:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 4:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 5:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 6:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						case 7:
							newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
						break;
						}
			} else if (robot == 4) { //�ΰ�����ON  ����.����  â�����Ѱ�
				int rnd = _random.nextInt(8);
				newPc.setHighLevel(52);
				newPc.setLevel(52);
				newPc.setExp(0);
				newPc.setClanid(0);
				newPc.setClanname("");
				switch(rnd){
				case 0:
				newPc.setLawful(32767);
				break;
				case 1:
				newPc.setLawful(2000);
				break;
				case 2:
				newPc.setLawful(3000);
				break;
				case 3:
				newPc.setLawful(4000);
				break;
				case 4:
				newPc.setLawful(5000);
				break;
				case 5:
				newPc.setLawful(6000);
				break;
				case 6:
				newPc.setLawful(7000);
				break;
				case 7:
				newPc.setLawful(7500);
				break;	
				}
//				���� ȣĪ
				switch(rnd){
				case 0:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 1:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 2:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 3:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 4:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 5:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 6:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 7:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					}
			} else if (robot == 5) { //�ΰ�����ON  ���.����.����.����
				int rnd = _random.nextInt(8);
				newPc.setHighLevel(56);
				newPc.setLevel(56);
				newPc.setExp(0);
				newPc.setClanid(0);
				newPc.setClanname("");
				switch(rnd){
				case 0:
				newPc.setLawful(32767);
				break;
				case 1:
				newPc.setLawful(2000);
				break;
				case 2:
				newPc.setLawful(3000);
				break;
				case 3:
				newPc.setLawful(4000);
				break;
				case 4:
				newPc.setLawful(5000);
				break;
				case 5:
				newPc.setLawful(6000);
				break;
				case 6:
				newPc.setLawful(7000);
				break;
				case 7:
				newPc.setLawful(7500);
				break;	
				}
//				���� ȣĪ
				switch(rnd){
				case 0:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 1:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 2:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 3:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 4:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 5:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 6:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					case 7:
						newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
					break;
					}
				////////////////////���ͷκ�/////////////////////////
			} else if (robot == 6) { //�ΰ�����ON  ���.����.����.����
				int rnd = _random.nextInt(8);
				newPc.setHighLevel(56);
				newPc.setLevel(56);
				newPc.setExp(0);
				newPc.setClanid(298400658);
				newPc.setClanname("�⸣Ÿ��");
				switch(rnd){
				case 0:
				newPc.setLawful(32767);
				break;
				case 1:
				newPc.setLawful(2000);
				break;
				case 2:
				newPc.setLawful(3000);
				break;
				case 3:
				newPc.setLawful(-4000);
				break;
				case 4:
				newPc.setLawful(5000);
				break;
				case 5:
				newPc.setLawful(6000);
				break;
				case 6:
				newPc.setLawful(7000);
				break;
				case 7:
				newPc.setLawful(7500);
				break;	
				}
//				���� ȣĪ
				switch(rnd){
				case 0:
				newPc.setTitle ("\\f<  �⸣����Ÿ�� ");
				break;
				case 1:
				newPc.setTitle ("\\f<  �⸣Ÿ��");
				break;
				case 2:
				newPc.setTitle ("\\f<  �⸣�ý�Ÿ��");
				break;
				case 3:
				newPc.setTitle ("\\f<  ȣĪ�������٤�");
				break;
				case 4:
				newPc.setTitle ("\\f<  �⸣Ŀ��Ÿ��");
				break;
				case 5:
				newPc.setTitle ("\\f<  �⸣����Ÿ��");
				break;
				case 6:
				newPc.setTitle ("\\f<  �⸣�뵷Ÿ��");
				break;
				case 7:
				newPc.setTitle ("\\f<  �⸣���Ÿ��");
				break;
				}
			} else if (robot == 7) { //�ΰ�����ON  ���.����.����.����
				int rnd = _random.nextInt(8);
				newPc.setHighLevel(52);
				newPc.setLevel(52);
				newPc.setExp(0);
				newPc.setClanid(298400658);
				newPc.setClanname("����");
				switch(rnd){
				case 0:
				newPc.setLawful(32767);
				break;
				case 1:
				newPc.setLawful(2000);
				break;
				case 2:
				newPc.setLawful(3000);
				break;
				case 3:
				newPc.setLawful(-4000);
				break;
				case 4:
				newPc.setLawful(5000);
				break;
				case 5:
				newPc.setLawful(6000);
				break;
				case 6:
				newPc.setLawful(7000);
				break;
				case 7:
				newPc.setLawful(7500);
				break;	
				}
//				���� ȣĪ
				switch(rnd){
				case 0:
				newPc.setTitle ("\\f<  �� �� �� ��");
				break;
				case 1:
				newPc.setTitle ("");
				break;
				case 2:
				newPc.setTitle ("\\f<  �� �� �� ��");
				break;
				case 3:
				newPc.setTitle ("\\f<  ������");
				break;
				case 4:
				newPc.setTitle ("\\f<  �� �� �� ��");
				break;
				case 5:
				newPc.setTitle ("\\f<  �� �� �� ��");
				break;
				case 6:
				newPc.setTitle ("\\f=  ��������");
				break;
				case 7:
				newPc.setTitle ("\\f<  �� �� �� ��");
				break;
				}
				////////////////////���ͷκ�/////////////////////////
			} else if (robot == 8) { //�ΰ�����ON  ���.����.����.����
				int rnd = _random.nextInt(8);
				newPc.setHighLevel(56);
				newPc.setLevel(56);
				newPc.setExp(0);
				newPc.setClanid(298400760);
				newPc.setClanname("�Ǹ�");
				switch(rnd){
				case 0:
				newPc.setLawful(32767);
				break;
				case 1:
				newPc.setLawful(2000);
				break;
				case 2:
				newPc.setLawful(3000);
				break;
				case 3:
				newPc.setLawful(-4000);
				break;
				case 4:
				newPc.setLawful(5000);
				break;
				case 5:
				newPc.setLawful(6000);
				break;
				case 6:
				newPc.setLawful(7000);
				break;
				case 7:
				newPc.setLawful(7500);
				break;	
				}
//				���� ȣĪ
				switch(rnd){
				case 0:
				newPc.setTitle ("\\f3  �� �� �� ��");
				break;
				case 1:
				newPc.setTitle ("");
				break;
				case 2:
				newPc.setTitle ("\\f3  �� �� û ��");
				break;
				case 3:
				newPc.setTitle ("");
				break;
				case 4:
				newPc.setTitle ("\\f3  �� �� ȫ ��");
				break;
				case 5:
				newPc.setTitle ("\\f3  �� �� �� ��");
				break;
				case 6:
				newPc.setTitle ("");
				break;
				case 7:
				newPc.setTitle ("\\f3  �� �� �� ��");
				break;
				}
			} else if (robot == 9) { //�ΰ�����ON  ���.����.����.����
				int rnd = _random.nextInt(8);
				newPc.setHighLevel(52);
				newPc.setLevel(52);
				newPc.setExp(0);
				newPc.setClanid(298400760);
				newPc.setClanname("�Ǹ�");
				switch(rnd){
				case 0:
				newPc.setLawful(32767);
				break;
				case 1:
				newPc.setLawful(2000);
				break;
				case 2:
				newPc.setLawful(3000);
				break;
				case 3:
				newPc.setLawful(-4000);
				break;
				case 4:
				newPc.setLawful(5000);
				break;
				case 5:
				newPc.setLawful(6000);
				break;
				case 6:
				newPc.setLawful(7000);
				break;
				case 7:
				newPc.setLawful(7500);
				break;	
				}
//				���� ȣĪ
				switch(rnd){
				case 0:
				newPc.setTitle ("\\f3  �� �� �� ��");
				break;
				case 1:
				newPc.setTitle ("");
				break;
				case 2:
				newPc.setTitle ("\\f3  �� �� û ��");
				break;
				case 3:
				newPc.setTitle ("");
				break;
				case 4:
				newPc.setTitle ("\\f3  �� �� ȫ ��");
				break;
				case 5:
				newPc.setTitle ("\\f3  �� �� �� ��");
				break;
				case 6:
				newPc.setTitle ("");
				break;
				case 7:
				newPc.setTitle ("\\f3  �� �� �� ��");
				break;
				}
		} else if (robot == 10) { //�ΰ�����ON  ���.����.����.����
			int rnd = _random.nextInt(8);
			newPc.setHighLevel(56);
			newPc.setLevel(56);
			newPc.setExp(0);
			newPc.setClanid(298400834);
			newPc.setClanname("�ݺ�");
			switch(rnd){
			case 0:
			newPc.setLawful(32767);
			break;
			case 1:
			newPc.setLawful(2000);
			break;
			case 2:
			newPc.setLawful(3000);
			break;
			case 3:
			newPc.setLawful(-4000);
			break;
			case 4:
			newPc.setLawful(5000);
			break;
			case 5:
			newPc.setLawful(6000);
			break;
			case 6:
			newPc.setLawful(7000);
			break;
			case 7:
			newPc.setLawful(7500);
			break;	
			}
//			���� ȣĪ
			switch(rnd){
			case 0:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 1:
			newPc.setTitle ("");
			break;
			case 2:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 3:
			newPc.setTitle ("");
			break;
			case 4:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 5:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 6:
			newPc.setTitle ("");
			break;
			case 7:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			}
		} else if (robot == 11) { //�ΰ�����ON  ���.����.����.����
			int rnd = _random.nextInt(8);
			newPc.setHighLevel(52);
			newPc.setLevel(52);
			newPc.setExp(0);
			newPc.setClanid(298400834);
			newPc.setClanname("�ݺ�");
			switch(rnd){
			case 0:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 1:
			newPc.setTitle ("");
			break;
			case 2:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 3:
			newPc.setTitle ("");
			break;
			case 4:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 5:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 6:
			newPc.setTitle ("");
			break;
			case 7:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			}
//			���� ȣĪ
			switch(rnd){
			case 0:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 1:
			newPc.setTitle ("");
			break;
			case 2:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 3:
			newPc.setTitle ("");
			break;
			case 4:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 5:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			case 6:
			newPc.setTitle ("");
			break;
			case 7:
			newPc.setTitle ("\\fe  �� �� �� ��");
			break;
			}
		} else if (robot == 12) { //�ΰ�����ON  ���.����.����.����
			int rnd = _random.nextInt(8);
			newPc.setHighLevel(56);
			newPc.setLevel(56);
			newPc.setExp(0);
			newPc.setClanid(299090949);
			newPc.setClanname("����ܽ�");
			switch(rnd){
			case 0:
			newPc.setLawful(32767);
			break;
			case 1:
			newPc.setLawful(2000);
			break;
			case 2:
			newPc.setLawful(3000);
			break;
			case 3:
			newPc.setLawful(-4000);
			break;
			case 4:
			newPc.setLawful(5000);
			break;
			case 5:
			newPc.setLawful(6000);
			break;
			case 6:
			newPc.setLawful(7000);
			break;
			case 7:
			newPc.setLawful(7500);
			break;	
			}
//			���� ȣĪ
			switch(rnd){
			case 0:
			newPc.setTitle ("���ų�������");
			break;
			case 1:
			newPc.setTitle ("\\fY �� �� �� ��");
			break;
			case 2:
			newPc.setTitle ("\\fY �� �� �� ��");
			break;
			case 3:
			newPc.setTitle ("�츮��ȣĪ�̹���");
			break;
			case 4:
			newPc.setTitle ("\\fY �� �� Ÿ ��");
			break;
			case 5:
			newPc.setTitle ("\\fY �� �� �� ��");
			break;
			case 6:
			newPc.setTitle ("\\fY �� �� �� ��");
			break;
			case 7:
			newPc.setTitle ("\\fY 1���³�����");
			break;
			}
		} else if (robot == 13) { //�ΰ�����ON  ���.����.����.����
			int rnd = _random.nextInt(8);
			newPc.setHighLevel(52);
			newPc.setLevel(52);
			newPc.setExp(0);
			newPc.setClanid(299090949);
			newPc.setClanname("�ʵ�");
			switch(rnd){
			case 0:
			newPc.setLawful(32767);
			break;
			case 1:
			newPc.setLawful(2000);
			break;
			case 2:
			newPc.setLawful(3000);
			break;
			case 3:
			newPc.setLawful(-4000);
			break;
			case 4:
			newPc.setLawful(5000);
			break;
			case 5:
			newPc.setLawful(6000);
			break;
			case 6:
			newPc.setLawful(7000);
			break;
			case 7:
			newPc.setLawful(7500);
			break;	
			}
//			���� ȣĪ
			switch(rnd){
			case 1:
			newPc.setTitle ("�ų��ʵ������");
			break;
			case 2:
			newPc.setTitle ("�ų��ʵ���ִ�");
			break;
			case 3:
			newPc.setTitle ("��ĳ����������");
			break;
			case 4:
			newPc.setTitle ("������������");
			break;
			case 5:
			newPc.setTitle ("�߸����׾�");
			break;
			case 6:
			newPc.setTitle ("�ǵ����̸��̻ڱ���");
			break;
			case 7:
			newPc.setTitle ("�ǵ���~");
			break;
			}
		} else if (robot == 14) { //����
			newPc.setHighLevel(5);
			newPc.setLevel(5);
			newPc.setExp(0);
			newPc.setLawful(0);
			newPc.setClanid(0);
			newPc.setClanname("");
			newPc.setTitle ("\\fC " + Config.SERVER_NAME + " ����");
			int typeCount = 0;
			for(L1PcInstance tempPc : L1World.getInstance().getAllPlayers()){
				if(tempPc.noPlayerCK && tempPc.getLevel() == 5){
					typeCount++;
				}
			}
			RobotFishing rf = null;
			try{
				rf = RobotThread.getRobotFish().get(typeCount);
			}catch(Exception e){
				continue;
			}
			if(rf == null)continue;
			newPc.setX(rf.x);
			newPc.setY(rf.y);
			newPc.setMap((short)rf.map);
			newPc.getMoveState().setHeading(rf.heading);
			int sex = _random.nextInt(1);
			int type = _random.nextInt(MALE_LIST.length);
			int klass = 0;
					
			switch (sex) {
			case 0:
				klass = MALE_LIST[type];
				break;
			case 1:
				klass = FEMALE_LIST[type];
				break;
			}
			
			newPc.noPlayerCK = true;
			newPc.setClassId(klass);
			newPc.getGfxId().setTempCharGfx(klass);
			newPc.getGfxId().setGfxId(klass);
			newPc.set_sex(sex);
			newPc.setType(type);
			newPc.setFishing(true);
			newPc._fishingX = rf.fishX;
			newPc._fishingY = rf.fishY;
			Broadcaster.broadcastPacket(newPc, new S_Fishing(newPc.getId(),ActionCodes.ACTION_Fishing, rf.fishX, rf.fishY));
			
			L1World.getInstance().storeObject(newPc);
			L1World.getInstance().addVisibleObject(newPc);
			
			newPc.setNetConnection(null);

			SearchCount++;
			continue;
			/*int rnd = _random.nextInt(8);
			newPc.setHighLevel(5);
			newPc.setLevel(5);
			newPc.setExp(0);
			newPc.setLawful(0);
			newPc.setClanid(0);
			newPc.setClanname("");
			switch(rnd){
			case 0:
				newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 1:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 2:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 3:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 4:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 5:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 6:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 7:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				}
			int typeCount = 0;
			for(L1PcInstance tempPc : L1World.getInstance().getAllPlayers()){
				if(tempPc.noPlayerCK && tempPc.getLevel() == 5){
					typeCount++;
				}
			}
			RobotFishing rf = null;
			try{
				rf = RobotThread.getRobotFish().get(typeCount);
			}catch(Exception e){
				continue;
			}
			if(rf == null)continue;
			newPc.setX(rf.x);
			newPc.setY(rf.y);
			newPc.setMap((short)rf.map);
		//	newPc.getMoveState().setHeading(rf.heading);
			int sex = _random.nextInt(1);
			int type = _random.nextInt(MALE_LIST.length);
			int klass = 0;
					
			switch (sex) {
			case 0:
				klass = MALE_LIST[type];
				break;
			case 1:
				klass = FEMALE_LIST[type];
				break;
			}
			
			newPc.noPlayerCK = true;
			newPc.setClassId(klass);
			newPc.getGfxId().setTempCharGfx(klass);
			newPc.getGfxId().setGfxId(klass);
			newPc.set_sex(sex);
			newPc.setType(type);
			newPc.setFishing(true);
			newPc._fishingX = rf.fishX;
			newPc._fishingY = rf.fishY;
			Broadcaster.broadcastPacket(newPc, new S_Fishing(newPc.getId(),ActionCodes.ACTION_Fishing, rf.fishX, rf.fishY));
			
			L1World.getInstance().storeObject(newPc);
			L1World.getInstance().addVisibleObject(newPc);
			
			newPc.setNetConnection(null);

			SearchCount++;
			continue;*/
		} else if (robot == 15) { //�丮
			int rnd = _random.nextInt(8);
			newPc.setHighLevel(1);
			newPc.setLevel(1);
			newPc.setExp(0);
			newPc.setLawful(0);
			newPc.setClanid(0);
			newPc.setClanname("");
			switch(rnd){
			case 0:
				newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 1:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 2:
				newPc.setTitle ("���Ƴ���");
				break;
				case 3:
				newPc.setTitle ("��Ű��������");
				break;
				case 4:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 5:
				newPc.setTitle ("�� �丮�Ѵ�");
				break;
				case 6:
				newPc.setTitle ("�ǵ�ǵ�");
				break;
				case 7:
				newPc.setTitle ("������");
				break;
				}
		} else if(robot == 16) { // �ΰ�����ON ���ڸ� ����
				newPc.setHighLevel(7);//��������
				newPc.setLevel(7);
				newPc.setExp(0);
				newPc.setLawful(0);
				newPc.setClanid(0);
				newPc.setClanname("");
//				���� ȣĪ
				int rnd = _random.nextInt(8);
				switch(rnd){
			case 0:
				newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 1:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 2:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 3:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 4:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 5:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 6:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				case 7:
					newPc.setTitle("\\fC " + Config.SERVER_NAME + " ����");
				break;
				}

		}
				newPc.addBaseMaxHp((short)2300);
				newPc.setCurrentHp(2300);
				newPc.setDead(false);
				newPc.addBaseMaxMp((short)500);
				newPc.setCurrentMp(500);
				newPc.getResistance().addMr(100);
				newPc.getAbility().setBaseStr(35);
				newPc.getAbility().setStr(35);
				newPc.getAbility().setBaseCon(35);
				newPc.getAbility().setCon(35);
				newPc.getAbility().setBaseDex(35);
				newPc.getAbility().setDex(35);
				newPc.getAbility().setBaseCha(35);
				newPc.getAbility().setCha(35);
				newPc.getAbility().setBaseInt(35);
				newPc.getAbility().setInt(35);
				newPc.getAbility().setBaseWis(35);
				newPc.getAbility().setWis(35);
								
				int sex = _random.nextInt(1);
				int type = _random.nextInt(MALE_LIST.length);
				int klass = 0;
								
				switch (sex) {
				case 0:
					klass = MALE_LIST[type];
					break;
				case 1:
					klass = FEMALE_LIST[type];
					break;
				}
				
				newPc.setClassId(klass);
				newPc.getGfxId().setTempCharGfx(klass);
				newPc.getGfxId().setGfxId(klass);
				newPc.set_sex(sex);
				newPc.setType(type);
				
				for(int i=0; i < 17; i++){
					x = loc[_random.nextInt(17)];
					y = loc[_random.nextInt(17)];
					newPc.setX(pc.getX() + x);
					newPc.setY(pc.getY() + y);
					newPc.setMap(pc.getMapId());
					if (map.isPassable(newPc.getX(), newPc.getY())) {
						break;
					}
				}	

				newPc.getMoveState().setHeading(random(0, 7));

				newPc.set_food(39);
				//newPc.setClanid(0);
				//newPc.setClanname("");
				newPc.setClanRank(0);
				newPc.setElfAttr(0);
				newPc.set_PKcount(0);
				newPc.setExpRes(0);
				newPc.setPartnerId(0);
				newPc.setAccessLevel((short)0);
				newPc.setGm(false);
				newPc.setMonitor(false);
				newPc.setHomeTownId(0);
				newPc.setContribution(0);
				newPc.setHellTime(0);
				newPc.setBanned(false);
				newPc.setKarma(0);
				newPc.setReturnStat(0);				
				newPc.setGmInvis(false);
				
				if (robot == 0) {
					newPc.noPlayerCK = true; // �ڷ���Ʈ ���ϴ� �κ�Ʈ�� ����, �ڷ���Ʈ ���ϴ� �ű� �κ�Ʈ ����(��Ʈ��, ȣĪ �ֽ�)
				} else if (robot == 1 || robot == 2 || robot == 3|| robot == 4|| robot == 5|| robot == 6|| robot == 7
						|| robot == 8|| robot == 9|| robot == 10|| robot == 11|| robot == 12|| robot == 13|| robot == 14|| robot == 15|| robot == 16) {
					newPc.noPlayerCK = true; // �ڷ���Ʈ �ϴ� �κ�Ʈ�� ����, �ڷ���Ʈ �ϴ� �ű� �κ�Ʈ ����(���Ǯ, ȣĪ ����), �ڷ���Ʈ �ϴ� �ű� �κ�Ʈ ����(ī��, ȣĪ ����)					
				} else {
					newPc.noPlayerCK = true; // �ڷ���Ʈ ���ϴ� �κ�Ʈ�� ����, �ڷ���Ʈ ���ϴ� �ű� �κ�Ʈ ����(��Ʈ��, ȣĪ �ֽ�)
				}
				
				if (newPc.isKnight()) {
					newPc.setCurrentWeapon(61);//����
					newPc.getEquipSlot().set(ItemTable.getInstance().createItem(61));
				} else if (newPc.isCrown()) {
					newPc.setCurrentWeapon(294);//����
					newPc.getEquipSlot().set(ItemTable.getInstance().createItem(294));
				} else if (newPc.isElf()) {
					newPc.setCurrentWeapon(189);//�ձ�
					newPc.getEquipSlot().set(ItemTable.getInstance().createItem(189));
					L1Item temp = ItemTable.getInstance().getTemplate(40748);
					if (temp != null) {
							L1ItemInstance item = ItemTable.getInstance().createItem(40748);
							item.setItemOwner(newPc);
							item.setEnchantLevel(0);
							item.setCount(count);
							newPc.getInventory().storeItem(item);
					}
					newPc.getInventory().setArrow(40748);
				} else if (newPc.isWizard()) {
					newPc.setCurrentWeapon(134);//����
					newPc.getEquipSlot().set(ItemTable.getInstance().createItem(134));
				} else if (newPc.isDarkelf()) {
					newPc.setCurrentWeapon(86);//����
					newPc.getEquipSlot().set(ItemTable.getInstance().createItem(86));
				} else if (newPc.isIllusionist()) {
					newPc.setCurrentWeapon(410004);//��Ű
					newPc.getEquipSlot().set(ItemTable.getInstance().createItem(410004));
				} else if (newPc.isDragonknight()) {
					newPc.setCurrentWeapon(410001);//��ü
					newPc.getEquipSlot().set(ItemTable.getInstance().createItem(410001));
				}	
					
				
				if (isteleport == 1) {
					int rnd1 = CommonUtil.random(20, 60);
					newPc.setTeleportTime(rnd1);
					
					int rnd2 = CommonUtil.random(5, 60);
					
					if (rnd1 == rnd2) {
						rnd2++;
					}
					newPc.setSkillTime(rnd2);
				}
				
				newPc.setActionStatus(0);				
				L1World.getInstance().storeObject(newPc);
				L1World.getInstance().addVisibleObject(newPc);
				
				newPc.setNetConnection(null);

				SearchCount++;
				newPc.RobotStartCK = true;
				int timernd = _random.nextInt(6) * 1000;
				newPc.RobotDeadDelay = System.currentTimeMillis() + timernd;
				RobotControler.addList(newPc);
			}
			pc.sendPackets(new S_SystemMessage(SearchCount + "���� �κ� ĳ���Ͱ� ��ġ �Ǿ����ϴ�."));
			pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
			
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("-------------------Robot Commands.------------------"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 0:��Ʈ�� [���ڸ��������]�����"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 1:���Ǯ [����.����.��ų.����.�������]���¸���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 2:ī��ƽ [����.����.��ų.����.�������]���¸���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 3:��Ʈ�� [����.����.����.�������]ó�����¸���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 4:���Ǯ [����.��ų.���Ż��]â���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 5:���Ǯ [���.��ų.���Ż��]��ɿ�"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 6:���Ǯ [�⸣Ÿ�����ͻ�ɿ�]��ɿ�"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 7:���Ǯ [�⸣Ÿ������â���]â���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 8:���Ǯ [�Ǹ����ͻ�ɿ�]��ɿ�"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 9:���Ǯ [�Ǹ�����â���]â���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 10:���Ǯ[�ݺ����ͻ�ɿ�]��ɿ�"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 11:���Ǯ[�ݺ�����â���]â���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 12:���Ǯ[�ʵ����ͻ�ɿ�]��ɿ�"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 13:���Ǯ[�ʵ�����â���]â���"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 14:���Ǯ[�����ͷκ�����]]25���Է�"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 15:���Ǯ[�丮�κ�����]������"));
			pc.sendPackets(new S_SystemMessage(" Ÿ�� - 16:���Ǯ[���ڸ�����]�����"));
			pc.sendPackets(new S_SystemMessage(" �ൿ - 0 :��, 1:�� [�ൿ�����������������ּ���]"));
			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(".�κ� (Ÿ��) (��) (�ൿ)").toString()));
			pc.sendPackets(new S_SystemMessage("-------------------Robot Commands.------------------"));
		}
	}
	
	private static boolean isAlphaNumeric(String s) {
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		return flag;
	}
	
	private static boolean isInvalidName(String name) {
		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("EUC-KR").length;
		} catch (UnsupportedEncodingException e) {
			return false;
		}

		if (isAlphaNumeric(name)) {
			return false;
		}

		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * ���� �Լ�
	 * @param lbound
	 * @param ubound
	 * @return
	 */
	static public int random(int lbound, int ubound) {
		return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}
}