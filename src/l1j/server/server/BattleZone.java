/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;


public class BattleZone implements Runnable {
	private static Logger _log = Logger.getLogger(BattleZone.class.getName());
	protected final Random _random = new Random();

	private static BattleZone _instance;

	int ����2 = 3*3600;
	int ����5 = 6*3600;
	int ����8 = 9*3600;
	int ����11 = 12*3600;
	int ����14 = 15*3600;
	int ����17 = 18*3600;
	int ����20 = 21*3600;
	int ����23 = 24*3600;

	//��� ���ۿ���
	private boolean _DuelStart;
	public boolean getDuelStart() {
		return _DuelStart;
	}

	public void setDuelStart(boolean duel) {
		_DuelStart = duel;
	}
	//��� ���忩��
	private boolean _DuelOpen;
	public boolean getDuelOpen() {
		return _DuelOpen;
	}

	public void setDuelOpen(boolean duel) {
		_DuelOpen = duel;
	}
	//��1 ���忩�� //���� �ο� ���߱� sunny
	private boolean _Team1Open;
	public boolean getTeam1Open() {
		return _Team1Open;
	}

	public void setTeam1Open(boolean Team1) {
		_Team1Open = Team1;
	}
	//��2 ���忩�� //���� �ο� ���߱� sunny
	private boolean _Team2Open;
	public boolean getTeam2Open() {
		return _Team2Open;
	}

	public void setTeam2Open(boolean Team2) {
		_Team2Open = Team2;
	}
	//��� ���ۿ���
	private boolean _����;

	public boolean ��Ʋ������() {
		return _����;
	}

	public void set��Ʋ������(boolean flag) {
		_���� = flag;
	}

	private boolean _����;
	public boolean ��Ʋ������() {
		return _����;
	}

	public void set��Ʋ������(boolean flag) {
		_���� = flag;
	}
	public int DuelCount;
	
	public int MemberCount1;
	
	public int MemberCount2;

	private int enddueltime;

	private boolean Close;
	protected ArrayList<L1PcInstance> ����1 = new ArrayList<L1PcInstance>();
	public void add����1(L1PcInstance pc) 	{ ����1.add(pc); }
	public void remove����1(L1PcInstance pc) 	{ 
		deleteMiniHp(pc);
		pc.set_DuelLine(0);
		MemberCount1 -= 1;
		����1.remove(pc); 
		BattleZone.getInstance().DuelCount -= 1;
		}
	public void clear����1() 					{ ����1.clear();	  }
	public int get����1Count() 				{ return ����1.size();	}
	
	protected ArrayList<L1PcInstance> ����2 = new ArrayList<L1PcInstance>();
	public void add����2(L1PcInstance pc) 	{ ����2.add(pc); }
	public void remove����2(L1PcInstance pc) 	{ 
		deleteMiniHp(pc);
		pc.set_DuelLine(0);
		MemberCount2 -= 1;
		����2.remove(pc); 
		BattleZone.getInstance().DuelCount -= 1;
		}
	public void clear����2() 					{ ����2.clear();	  }
	public int get����2Count() 				{ return ����2.size();	}
	
	protected ArrayList<L1PcInstance> ��Ʋ������ = new ArrayList<L1PcInstance>();
	public void add��Ʋ������(L1PcInstance pc) 	{ ��Ʋ������.add(pc); }
	public void remove��Ʋ������(L1PcInstance pc) 	{ 
		deleteMiniHp(pc);
		pc.set_DuelLine(0);
		��Ʋ������.remove(pc);
		}
	public void clear��Ʋ������() 					{ ��Ʋ������.clear();	  }
	public boolean is��Ʋ������(L1PcInstance pc) 	{ return ��Ʋ������.contains(pc); 	}
	public int get��Ʋ������Count() 				{ return ��Ʋ������.size();	}

	private boolean GmStart = false;
	public void setGmStart(boolean ck){	GmStart = ck; }
	public boolean getGmStart(){	return GmStart;	}
	
public L1PcInstance[] toArray��Ʋ������() {
		return ��Ʋ������.toArray(new L1PcInstance[��Ʋ������.size()]);
	}
	public static BattleZone getInstance() {
		if (_instance == null) {
			_instance = new BattleZone();
		}
		return _instance;
	}


	@Override
	public void run() {
		System.out.println(BattleZone.class.getName()  + " ��Ʋ�����������");
		try {
			while (true) {
				if(��Ʋ������()==true){
					Thread.sleep(1000*60*60*2); //2�ð�/1000=1��/���������� ������ ���̴°� �Ű�Ƚᵵ��
					set��Ʋ������(false);
				}else
				checkDuelTime(); // ��� ���ɽð��� üũ
				Thread.sleep(1000*10);
				����üũ();
			}
		} catch (Exception e1) {
		}
	}

	private void ����üũ() {

		L1PcInstance[] pc = toArray��Ʋ������();
		for (int i = 0; i < pc.length; i++) {
			if(pc[i].getMapId() == 5001 || pc[i].getMapId() == 5153 || !pc[i].isDead()){
                            return;
                        }else if (pc[i].getMapId() != 5001 || pc[i].getMapId() != 5153 || pc[i].isDead()){
				remove��Ʋ������(pc[i]);
				remove����1(pc[i]);
				remove����2(pc[i]);
				pc[i].set_DuelLine(0);
			}
			}
	}

	//���ð�üũ
	private void checkDuelTime() {
		//���ӽð��� �޾ƿ´�.
		int servertime = RealTimeClock.getInstance().getRealTime().getSeconds();
		//�����ð�
		int nowdueltime =servertime % 86400;
		int count1 = 0;
		int count2 = 0;
		int winLine = 0;
		//���帶���ð�  ����
		 if (getDuelStart() == false ){
			//���۽ð�����
				//���۽ð�����
				//���۽ð�����
			  if (  	    nowdueltime >= ����2-31 && nowdueltime <= ����2+31 // /2��
					     || nowdueltime >= ����5-31 && nowdueltime <= ����5+31 ///5��
					     || nowdueltime >= ����8-31 && nowdueltime <= ����8+31 ////8��
					     || nowdueltime >= ����11-31 && nowdueltime <= ����11+31 //11��
					     || nowdueltime >= ����14-31 && nowdueltime <= ����14+31//14��
					     || nowdueltime >= ����17-31 && nowdueltime <= ����17+31//17��
					     || nowdueltime >= ����20-31 && nowdueltime <= ����20+31//20��
					     || nowdueltime >= ����23-31 && nowdueltime <= ����23+31//23��
					     || getGmStart())
			{ //12��
				setDuelOpen(true);
				setDuelStart(true);
				����3�д��();
			}
			if (��Ʋ������() == true)	{
				L1PcInstance[] c = toArray��Ʋ������();
				for (int i = 0; i < c.length; i++) {
						if(c[i].getMapId() == 5001){ 
							if(!c[i].isDead()){
								try{
								��Ʋ������1(c[i]);
								��Ʋ������2(c[i]);
								set��Ʋ������(false);
								} catch (Exception e1) {
								}
							}
						}
				}
					setDuelStart(true);
					//������ �ð�����
					enddueltime = nowdueltime + 600; //20������������ð� ���ϴ°�
			}
		}else{
			//����ð��̰ų� ����������
			if(nowdueltime >= enddueltime || Close == true){
				L1PcInstance[] c1 = toArray��Ʋ������();
				for (int i = 0; i < c1.length; i++) {
						if(c1[i].getMapId() == 5153){
							if(!c1[i].isDead()){
								if(c1[i].get_DuelLine() == 1){
									count1 += 1;
								}else{
									count2 += 1;
								}
							}
						}
				}
				//���üũ
				if(count1 > count2){
					//1������ ��� ���Ͼ���Ʈ�װǾ���
					winLine = 1;
					L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ�� ����! ��ũ ���� �¸� *");
				}else if(count1 < count2){
					//2������ ���
					winLine = 2;
					L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ�� ����! ��ũ ���� �¸� *");
				}else{
					winLine = 3;
					L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ�� ����! 1�� ���ΰ� 2�������� �����Դϴ� *");
				}
				L1PcInstance[] c2 = toArray��Ʋ������();
				for (int i = 0; i < c2.length; i++) {
					//�̱� ���ο��� ����������
				     if(c2[i].get_DuelLine() == winLine){
				      c2[i].getInventory().storeItem(41159, 3000);
				      c2[i].getInventory().storeItem(40308, 30000000);
				      c2[i].getInventory().storeItem(127000, 5);
				      c2[i].sendPackets(new S_SystemMessage("\\fU* ���� - �¸������� ���� 3000���� ���޵Ǿ����ϴ� *"));
				      c2[i].sendPackets(new S_SystemMessage("\\fU* ���� - �¸������� �Ƶ� 3000�� ���޵Ǿ����ϴ� *"));
				      c2[i].sendPackets(new S_SystemMessage("\\fU* ���� - �¸������� ���̵��ǹ���(5) ���޵Ǿ����ϴ� *"));

				     }
					//��Ʋ���̶��
					if(c2[i].getMapId() == 5153){
						if(!c2[i].isDead()){
							c2[i].set_DuelLine(0);
							L1Teleport.teleport(c2[i], 33090, 33402, (short) 4, 0, true);// ���� �� �� �Ǵ� ��ǥ
						remove��Ʋ������(c2[i]);    }
					}else if(c2[i].get_DuelLine() != 0){
						c2[i].set_DuelLine(0);
					}
					deleteMiniHp(c2[i]);
				}

				L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ���� 3�ð� �������� �����ϴ� *");
				��Ʋ������.clear();
				����1.clear();
				����2.clear();
				DuelCount = 0;
				MemberCount1 = 0; //�ο��� ���� sunny //���� �ο� ���߱� sunny
				MemberCount2 = 0; //�ο��� ���� sunny //���� �ο� ���߱� sunny
				set��Ʋ������(true);
				set��Ʋ������(false);
				setDuelStart(false);
				Close = false;
				setGmStart(false);

			}else{
				
				//������ �����Ǿ��ٸ�
				if(!getDuelOpen()){
					int count3 = 0;
					int count4 = 0;
					L1PcInstance[] c3 = toArray��Ʋ������();
					for (int i = 0; i < c3.length; i++) {
						//��Ʋ���̶��
						if(c3[i].getMapId() == 5153){
							if(!c3[i].isDead()){//�������� ���� üũ
								if(c3[i].get_DuelLine() == 1){
									count3 += 1;
								}else{
									count4 += 1;
								}
							}
						}
					}

					//1���̵� 2���̵� ���������� 0���϶� �����������<<
					if(count3 == 0 || count4 == 0){
						Close = true;
					}
				}

			}
		}
		
	}
	private void ��Ʋ������1(L1PcInstance ��Ʋ�ǽ�){
		try{
			if (��Ʋ�ǽ�.get_DuelLine()==1){
				int ranx  = 32628 + _random.nextInt(4);
	    		int rany  = 32896 + _random.nextInt(5);
	    		L1Teleport.teleport(��Ʋ�ǽ�, ranx, rany, (short) 5153, 1, true);
	            createMiniHp(��Ʋ�ǽ�);
			}	
			} catch (Exception e1) {
		}
	}
	private void ��Ʋ������2(L1PcInstance ��Ʋ�ǽ�){
		try{
			if (��Ʋ�ǽ�.get_DuelLine()==2){
				int ranx2  = 32650 - _random.nextInt(4);
	    		int rany2  = 32893 + _random.nextInt(5);
	    		L1Teleport.teleport(��Ʋ�ǽ�, ranx2, rany2, (short) 5153, 5, true);
	            createMiniHp(��Ʋ�ǽ�);
			}	
			} catch (Exception e1) {
		}
	}
	private void ����3�д��(){
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "���� ��Ʋ�� ���Ƿ� ������ �����մϴ�."));
		L1World.getInstance().broadcastServerMessage("\\fW* ����� ��Ʋ���� �����ϴ�. ���Ƿ� �������ּ��� *");
		L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ���� �����Ͽ� ������ ��Ʋ�� ��̸� �������� *");;
		L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ�� ������ ������ �Ͻð� �����Ͻñ� �ٶ��ϴ� *");;
		setTeam1Open(true); //���� �ο� ���߱� sunny
		setTeam2Open(true); //���� �ο� ���߱� sunny
		try{
			Thread.sleep(1000*120);
		}catch(Exception e){
		}
		L1World.getInstance().broadcastServerMessage("\\fW* �� ��Ʋ���� �����ϴ�. ���ѷ� �������ּ��� *");
		try{
			Thread.sleep(1000*50);
		}catch(Exception e){
		}
		L1World.getInstance().broadcastServerMessage("\\fW* 10�� �� ��Ʋ�� ������ �����˴ϴ� * ");
		
		try{
			Thread.sleep(1000*10);
		}catch(Exception e){
		}

		
		if(MemberCount1 != 30){
			L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ�� �ο��� ���� �ʾ� ��ũ���������� 30�� ����Ǿ����ϴ� *");
			setTeam1Open(true);
			setTeam2Open(false);
			try{
				Thread.sleep(1000*30);
			}catch(Exception e){
			}
		}
		if(MemberCount2 != 30){
			L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ�� �ο��� ���� �ʾ� ��ũ���������� 30�� ����Ǿ����ϴ� *");
			setTeam1Open(false);
			setTeam2Open(true);
			try{
				Thread.sleep(1000*30);
			}catch(Exception e){
			}
		}
		if(getDuelOpen()){
				setDuelOpen(false);
		}
		setTeam1Open(false);
		setTeam2Open(false);
		L1World.getInstance().broadcastServerMessage("\\fW* ��Ʋ�� ������ �����Ǿ����ϴ� *");
		L1World.getInstance().broadcastServerMessage("\\fW* 5���� ��Ʋ�� ���۵˴ϴ�. �غ��ϼ��� *");
		try{
			Thread.sleep(1000*5);
		}catch(Exception e){
		}
		set��Ʋ������(true);
	}
	
 private void createMiniHp(L1PcInstance pc) {
  // ��Ʋ��, ���� HP�� ǥ�ý�Ų��
  List<L1PcInstance> li = null;
	li = pc.getNearObjects().getKnownPlayers();
	for( int i = 0 ; i < li.size() ; i++){
		L1PcInstance member = li.get(i);
 // for (L1PcInstance member :pc.getNearObjects().getKnownPlayers()){
   //�������ο��� hpǥ��
   if(member != null){
    if(pc.get_DuelLine() == member.get_DuelLine()){
     member.sendPackets(new S_HPMeter(pc.getId(), 100
      * pc.getCurrentHp() / pc.getMaxHp()));
     pc.sendPackets(new S_HPMeter(member.getId(), 100
      * member.getCurrentHp() / member.getMaxHp()));
    }
   }
  }


 }
	private void deleteMiniHp(L1PcInstance pc) {
		// ��Ʋ�����, HP�ٸ� �����Ѵ�.
  List<L1PcInstance> li = null;
	li = pc.getNearObjects().getKnownPlayers();
	for( int i = 0 ; i < li.size() ; i++){
		L1PcInstance member = li.get(i);
 // for (L1PcInstance member :pc.getNearObjects().getKnownPlayers()){
			//�������ο��� hpǥ��
			if(member != null){
				if(pc.get_DuelLine() == member.get_DuelLine()){
					pc.sendPackets(new S_HPMeter(member.getId(), 0xff));
					member.sendPackets(new S_HPMeter(pc.getId(), 0xff));
				}
			}
		}
	}

	 private void Clearline() {
		 for(L1PcInstance c : L1World.getInstance().getAllPlayers()) {
			 switch(c.get_DuelLine()) {
				 case 1:
					 c.set_DuelLine(0);
					 c.sendPackets(new S_SystemMessage("\\fW[ ��Ʋ�� ������ ���� ó�� �Ǿ����ϴ� ]"));
				 case 2:
					 c.set_DuelLine(0);
					 c.sendPackets(new S_SystemMessage("\\fW[ ��Ʋ�� ������ ���� ó�� �Ǿ����ϴ� ]"));
				 break;
				 default:
				 break;
			 }
		 }
	 }
}
