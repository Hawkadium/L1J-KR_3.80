package l1j.server.server;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import l1j.server.Config;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EventTowerInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class MiniWarGame extends Thread {
	
		private static MiniWarGame _instance;
		
		public int winLine = 0;
		
		public boolean _BattleEntry = false;

		private boolean _BattleStart;
		public boolean getBattleStart() {
			return _BattleStart;
		}
		public void setBattleStart(boolean Battle) {
			_BattleStart = Battle;
		}
		
		/*
		 * ��ȣž�� ���õȺκ�1
		 */
		private  L1NpcInstance _tower = null;
		public void setTower(L1NpcInstance A){
			_tower = A;
		}
		public L1NpcInstance getTower(){
			return _tower;
		}
		private boolean _towerDead = false;
		
		public void setTowerDead(boolean A){
			_towerDead = A;
		}
		public boolean getTowerDead(){
			return _towerDead;
		}
		
		/*
		 * ��ȣž�� ���õȺκ�2
		 */
		private  L1NpcInstance _tower2 = null;
		public void setTower2(L1NpcInstance B){
			_tower = B;
		}
		public L1NpcInstance getTower2(){
			return _tower2;
		}
		private boolean _towerDead2 = false;
		
		public void setTowerDead2(boolean B){
			_towerDead = B;
		}
		public boolean getTowerDead2(){
			return _towerDead2;
		}
		
		private static long sTime = 0;
		
		private String NowTime = "";
		//�ð� ����
		private static final int LOOP = Config.MINIWAR_LOOPTIME;

		private static final SimpleDateFormat s = new SimpleDateFormat("HH", Locale.KOREA);

		private static final SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);

		public static MiniWarGame getInstance() {
			if(_instance == null) {
				_instance = new MiniWarGame();
			}
			return _instance;
		}
		
		public final ArrayList<L1PcInstance> _Members = new ArrayList<L1PcInstance>();
		
		public void AddMember(L1PcInstance pc) {
			if (!_Members.contains(pc)) {
				_Members.add(pc);
				pc.sendPackets(new S_SystemMessage("\\fT�����û �Ǿ����ϴ�. ����� �̴ϰ������� ���۵˴ϴ�."));
			}
		}
		
		public void removeMember(L1PcInstance pc) {
			_Members.remove(pc);
		}

		public void clearMembers() {
			_Members.clear();
		}

		public boolean isMember(L1PcInstance pc) {
			return _Members.contains(pc);
		}

		public L1PcInstance[] getMemberArray() {
			return _Members.toArray(new L1PcInstance[_Members.size()]);
		}

		public int getMemberCount() {
			return _Members.size();
		}
		
		
		public final ArrayList<L1PcInstance> _MembersLine1 = new ArrayList<L1PcInstance>();
		
		public void AddMemberLine1(L1PcInstance pc) {
			if (!_MembersLine1.contains(pc)) {
				_MembersLine1.add(pc);
			}
		}
		
		public void removeMemberLine1(L1PcInstance pc) {
			_MembersLine1.remove(pc);
		}

		public void clearMembersLine1() {
			_MembersLine1.clear();
		}

		public boolean isMemberLine1(L1PcInstance pc) {
			return _MembersLine1.contains(pc);
		}

		public L1PcInstance[] getMemberArrayLine1() {
			return _MembersLine1.toArray(new L1PcInstance[_MembersLine1.size()]);
		}

		public int getMemberCountLine1() {
			return _MembersLine1.size();
		}
		
		public final ArrayList<L1PcInstance> _MembersLineReal1 = new ArrayList<L1PcInstance>();
		
		public void AddMemberLineReal1(L1PcInstance pc) {
			if (!_MembersLineReal1.contains(pc)) {
				_MembersLineReal1.add(pc);
			}
		}
		
		public void removeMemberLineReal1(L1PcInstance pc) {
			_MembersLineReal1.remove(pc);
		}

		public void clearMembersLineReal1() {
			_MembersLineReal1.clear();
		}

		public boolean isMemberLineReal1(L1PcInstance pc) {
			return _MembersLineReal1.contains(pc);
		}

		public L1PcInstance[] getMemberArrayLineReal1() {
			return _MembersLineReal1.toArray(new L1PcInstance[_MembersLineReal1.size()]);
		}

		public int getMemberCountLineReal1() {
			return _MembersLineReal1.size();
		}
		
		public final ArrayList<L1PcInstance> _MembersLine2 = new ArrayList<L1PcInstance>();
		
		public void AddMemberLine2(L1PcInstance pc) {
			if (!_MembersLine2.contains(pc)) {
				_MembersLine2.add(pc);
			}
		}
		
		public void removeMemberLine2(L1PcInstance pc) {
			_MembersLine2.remove(pc);
		}

		public void clearMembersLine2() {
			_MembersLine2.clear();
		}

		public boolean isMemberLine2(L1PcInstance pc) {
			return _MembersLine2.contains(pc);
		}

		public L1PcInstance[] getMemberArrayLine2() {
			return _MembersLine2.toArray(new L1PcInstance[_MembersLine2.size()]);
		}

		public int getMemberCountLine2() {
			return _MembersLine2.size();
		}
		
		public final ArrayList<L1PcInstance> _MembersLineReal2 = new ArrayList<L1PcInstance>();
		
		public void AddMemberLineReal2(L1PcInstance pc) {
			if (!_MembersLineReal2.contains(pc)) {
				_MembersLineReal2.add(pc);
			}
		}
		
		public void removeMemberLineReal2(L1PcInstance pc) {
			_MembersLineReal2.remove(pc);
		}

		public void clearMembersLineReal2() {
			_MembersLineReal2.clear();
		}

		public boolean isMemberLineReal2(L1PcInstance pc) {
			return _MembersLineReal2.contains(pc);
		}

		public L1PcInstance[] getMemberArrayLineReal2() {
			return _MembersLineReal2.toArray(new L1PcInstance[_MembersLineReal2.size()]);
		}

		public int getMemberCountLineReal2() {
			return _MembersLineReal2.size();
		}

		
		@Override
			public void run() {
			System.out.println("MiniWarGame.getInstance ... �̴ϰ����� ����!");
			try	{
					while (true) {
						try{
						       Thread.sleep(1000); 
						   }catch(Exception e){
						   }
						/** ���� **/
						if(!isOpen())
							continue;
						if(L1World.getInstance().getAllPlayers().size() <= 0)
							continue;
						
						Thread.sleep(60000L);
						
						/** ���� �޼��� **/
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "NEW�̴ϰ����� ���Ƚ��ϴ�."));
						L1World.getInstance().broadcastServerMessage("\\fWNEW�̴ϰ����� ���ѷ� �����ϼ���!");
					
						/** NEW�̴ϰ��� ����**/
						setBattleStart(true);
						_BattleEntry = true;
						
						Thread.sleep(60000L);
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "NEW�̴ϰ��� ���� ���� 3���� �Դϴ�."));
						L1World.getInstance().broadcastServerMessage("\\fWNEW�̴ϰ��� ���� ���� 3���� �Դϴ�.");
					
						Thread.sleep(60000L);
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "NEW�̴ϰ��� ���� ���� 2���� �Դϴ�."));
						L1World.getInstance().broadcastServerMessage("\\fWNEW�̴ϰ��� ���� ���� 2���� �Դϴ�.");
					
						Thread.sleep(60000L);
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "NEW�̴ϰ��� ���� ���� 1���� �Դϴ�."));
						L1World.getInstance().broadcastServerMessage("\\fWNEW�̴ϰ��� ���� ���� 1���� �Դϴ�.");

						Thread.sleep(60000L);
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "NEW�̴ϰ��� ���� �����Ǿ����ϴ�."));
						L1World.getInstance().broadcastServerMessage("\\fWNEW�̴ϰ��� ���� �����Ǿ����ϴ�.");
					
						_BattleEntry = false;
						
						InBattle();
						
						L1WarSpawn warspawn = new L1WarSpawn();
						L1Npc l1npc = NpcTable.getInstance().getTemplate(6100008);	
						warspawn.SpawnWarObject(l1npc, Config.MINIWAR_ATEAM_X, Config.MINIWAR_ATEAM_Y, (short) (2006));
						
						L1WarSpawn warspawn2 = new L1WarSpawn();
						L1Npc l1npc2 = NpcTable.getInstance().getTemplate(6100009);	
						warspawn2.SpawnWarObject(l1npc2, Config.MINIWAR_BTEAM_X, Config.MINIWAR_BTEAM_Y, (short) (2006));

						Thread.sleep(3600000L); //1�ð�

						TelePort();
						
						/** ���� **/
						End(); 
	
					}

				} catch(Exception e){
					e.printStackTrace();
				}
			}

			/**
			 *���� �ð��� �����´�
			 *
			 *@return (Strind) ���� �ð�(MM-dd HH:mm)
			 */
			 public String OpenTime() {
				 Calendar c = Calendar.getInstance();
				 c.setTimeInMillis(sTime);
				 return ss.format(c.getTime());
			 }

			 /**
			 *�̴ϰ����� �����ִ��� Ȯ��
			 *
			 *@return (boolean) �����ִٸ� true �����ִٸ� false
			 */
			 private boolean isOpen() {
				 NowTime = getTime();
				 if((Integer.parseInt(NowTime) % LOOP) == 0) return true;
				 return false;
			 }
			 /**
			 *���� ����ð��� �����´�
			 *
			 *@return (String) ���� �ð�(HH:mm)
			 */
			 private String getTime() {
				 return s.format(Calendar.getInstance().getTime());
			 }
			 
			 /**�̺�Ʈ���� ������ ��ȯ**/
			 public void TelePort() {
				 for(L1PcInstance c : L1World.getInstance().getAllPlayers()) {
					 switch(c.getMap().getId()) {
						 case 2006:
						 c.stopHpRegenerationByDoll();
						 c.stopMpRegenerationByDoll();
						 L1Teleport.teleport(c, 33970, 33246, (short) 4, 4, true);
						 c.set_MiniDuelLine(0);
						 c.sendPackets(new S_SystemMessage("�׻� ����ϴ� "+ Config.SERVER_NAME +" �����Դϴ�! ���� �ϼ̽��ϴ�."));
						 break;
						 default:
						 break;
					 }
				 }
			 }
			 

			 /** ���� **/
			 public void End() {
				 L1World.getInstance().broadcastServerMessage("\\fW[ NEW�̴ϰ����� "+LOOP+ "�ð� �������� ���۵˴ϴ� ]");
				 clearMembers();
				 clearMembersLine1();
				 clearMembersLineReal1();
				 clearMembersLine2();
				 clearMembersLineReal2();
				 setBattleStart(false);
				 TelePort();
				 /**Ÿ�� ���� **/
				 setTowerDead(false);
				 setTowerDead2(false);
				 DeletedMon1();
				 DeletedMon2();
				 DeletedMon3();
				 winLine = 0;
				 System.out.println("MiniWarGame.getInstance ... �̴ϰ����� ���� ����!");
			 }
			 
			 private void DeletedMon1() {		
					for (Object obj : L1World.getInstance().getVisibleObjects(2006).values()) {			
						if (obj instanceof L1EventTowerInstance){			
							L1EventTowerInstance tower = (L1EventTowerInstance) obj;				
							if(tower.getNpcTemplate().get_npcId() == 6100008){					
								tower.deleteMe();	
								}
							}
						}
					}
			 
			 private void DeletedMon2() {		
					for (Object obj : L1World.getInstance().getVisibleObjects(2006).values()) {			
						if (obj instanceof L1EventTowerInstance){			
							L1EventTowerInstance tower = (L1EventTowerInstance) obj;				
							if(tower.getNpcTemplate().get_npcId() == 6100009){					
								tower.deleteMe();	
								}
							}
						}
					}
			 
			 private void DeletedMon3() {		
					for (Object obj : L1World.getInstance().getVisibleObjects(2006).values()) {			
						if (obj instanceof L1EventTowerInstance){			
							L1EventTowerInstance tower = (L1EventTowerInstance) obj;				
							if(tower.getNpcTemplate().get_npcId() == 6100002){					
								tower.deleteMe();	
								}
							}
						}
					}
			 
			 private void InBattle(){
				 if(_Members.size() <= 1){//��������9
					 L1World.getInstance().broadcastServerMessage("\\fW[ �̴ϰ��� �ο��� �ּ� 10���̻��̿��� ���۵˴ϴ� ]");
					 End();
				 }
				 for (int i = 0; i < _Members.size(); i++) {
				  L1PcInstance pc = _Members.get(i);//����Ʈ���� ����� �����´�.
				  if(pc != null){
				     if(i % 2 == 0){//¦���� ���
				      pc.set_MiniDuelLine(1);
				      AddMemberLine1(pc);
				      AddMemberLineReal1(pc);
				      L1Teleport.teleport(pc, Config.MINIWAR_ATEAM_X, Config.MINIWAR_BTEAM_Y, (short) 2006, 1, true);
				      pc.sendPackets(new S_SystemMessage("\\fT�̴ϰ����� [A] �������� �����ϼ̽��ϴ�."));
				      createHp(pc);
				      pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "������ ��ȣž�� ���� �μŹ��������� ����մϴ�."));
				     }else{//Ȧ���ϰ��
				      pc.set_MiniDuelLine(2);
				      AddMemberLine2(pc);
				      AddMemberLineReal2(pc);
				      L1Teleport.teleport(pc, Config.MINIWAR_BTEAM_X, Config.MINIWAR_BTEAM_Y, (short) 2006, 5, true);
				      pc.sendPackets(new S_SystemMessage("\\fT�̴ϰ����� [B] �������� �����ϼ̽��ϴ�."));
				      createHp(pc);
				      pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "������ ��ȣž�� ���� �μŹ��������� ����մϴ�."));
				     }
				    }else{
				    	removeMember(pc);
				    }
				 }
				}

			 // �̴� HP�ٸ� �����Ѵ�.
			 private void createHp(L1PcInstance pc) {
				 for (L1PcInstance member : getMemberArray()) {
					 if(member != null){
							 if(isMemberLine1(pc) == isMemberLine1(member)){//���� ������ ������� HP�ٸ� ����
								 member.sendPackets(new S_HPMeter(pc.getId(), 100 * pc.getCurrentHp() / pc.getMaxHp()));
								 pc.sendPackets(new S_HPMeter(member.getId(), 100 * member.getCurrentHp() / member.getMaxHp()));
							 }else if(isMemberLine2(pc) == isMemberLine2(member)){//���� ������ ������� HP�ٸ� ����
								 member.sendPackets(new S_HPMeter(pc.getId(), 100 * pc.getCurrentHp() / pc.getMaxHp()));
								 pc.sendPackets(new S_HPMeter(member.getId(), 100 * member.getCurrentHp() / member.getMaxHp()));
						}
					 }
				 }
			 }
			 //HP�ٸ� �����Ѵ�.
			 public void deleteMiniHp(L1PcInstance pc) {
				 for (L1PcInstance member : getMemberArray()) {
					 if(member != null){
							 if(isMemberLineReal1(pc) == isMemberLineReal1(member)){
								 pc.sendPackets(new S_HPMeter(member.getId(), 0xff));
								 member.sendPackets(new S_HPMeter(pc.getId(), 0xff));
							 }else if(isMemberLineReal2(pc) == isMemberLineReal2(member)){
								 pc.sendPackets(new S_HPMeter(member.getId(), 0xff));
								 member.sendPackets(new S_HPMeter(pc.getId(), 0xff));
						  }
					 }
				 }
			 }
			 
		}
