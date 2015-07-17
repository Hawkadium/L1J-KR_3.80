package l1j.server.server.model.Instance;


import javolution.util.FastTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CalcExp;
import java.util.Random;

public class L1ScarecrowInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	private static Random _random = new Random(System.nanoTime());

	
	public L1ScarecrowInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		if (player.isInParty()) { // �߰� ����ƺ���׹���

		} else {
			if (attack.calcHit()) {
				if (this.getNpcId() == 4500060){
				    if (player.getInventory().findItemId(40308).getCount() < 1000000){ 
				     player.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
				       "100���� �̻� ������ ���Ӱ����մϴ�."));
				     return; 
				     }
				    this.setNpoint(this.getNpoint()+25000);
				    player.getInventory().consumeItem(40308, 50000);//5��
				    int chance = _random.nextInt(15000)+1;
				    if (chance <= 100){ 
				     player.getInventory().storeItem(40308, 700000);
				     
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 70������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 101 && chance <= 150){
				     player.getInventory().storeItem(40308, 1200000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� 120������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >=151 && chance <= 175){
				     player.getInventory().storeItem(40308, 2000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 200������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >=176 && chance <= 189){
				     player.getInventory().storeItem(40308, 3500000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 350������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 190 && chance <= 192){
				     player.getInventory().storeItem(40308, 10000000);
				     String chatId = " 3���÷:"+player.getName()+"\\fY�� �Ƶ��� 1000������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 193 && chance <= 194){
				     player.getInventory().storeItem(40308, 30000000);
				     String chatId = " 2���÷:"+player.getName()+"\\fY�� �Ƶ��� 3000������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 194 && chance < 196){
				     player.getInventory().storeItem(40308, 70000000);
				        Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " 1���÷:"+player.getName()+"��1���÷ ��� 7õ������÷"));
				       } else if  (chance >= 196 && chance <= 200){
				        int k2 = (int) (this.getNpoint()*0.6);
				        int k3 = this.setNpoint(this.getNpoint() - k2);
				        player.getInventory().storeItem(40308, k3);
				     Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " ����:"+player.getName()+"�� ��� ["+k3+"]�� ȹ��"));
				     this.setNpoint(k2);
				       } else if (chance >= 201 && chance <= 15000){
				        player.sendPackets(new S_SystemMessage("\\fY���� : [25000]�� ����  , �� : ["+this.getNpoint()+"]�� ����"));
				    }
				    }
				   if (this.getNpcId() == 4500061){
				    if (player.getInventory().findItemId(40308).getCount() < 10000000){ 
				     player.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
				       "1000���� �̻� ������ ���Ӱ����մϴ�."));
				     return; 
				     }
				    this.setSpoint(this.getSpoint()+50000);
				    player.getInventory().consumeItem(40308, 100000);//10��
				    int chance = _random.nextInt(15000)+1;
				    if (chance <= 100){ 
				     player.getInventory().storeItem(40308, 1000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 100������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 101 && chance <= 150){
				     player.getInventory().storeItem(40308, 3000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 300������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 151 && chance <= 175){
				     player.getInventory().storeItem(40308, 4000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 400������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 176 && chance <= 189){
				     player.getInventory().storeItem(40308, 10000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 1000������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 190 && chance <= 192){
				     player.getInventory().storeItem(40308, 30000000);
				     String chatId = " 3���÷:"+player.getName()+"\\fY�� �Ƶ��� 3000������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 193 && chance <= 194){
				     player.getInventory().storeItem(40308, 40000000);
				     String chatId = " 2���÷:"+player.getName()+"\\f�� �Ƶ��� 4000������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 194 && chance < 196){
				     player.getInventory().storeItem(40308, 130000000);
				     Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " 1���÷:"+player.getName()+"�� 1���÷��1��3õȹ��"));
				       } else if  (chance >= 196 && chance <= 200){
				        int k2 = (int) (this.getSpoint()*0.6);
				        int k3 = this.setSpoint(this.getSpoint() - k2);
				        player.getInventory().storeItem(40308, k3);
				     Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " ����:"+player.getName()+"�� ��� ["+k3+"]�� ȹ��"));
				     this.setSpoint(k2);
				       } else if (chance >= 201 && chance <= 15000){
				        player.sendPackets(new S_SystemMessage("\\fY���� : [50000]�� ����  , �� : ["+this.getSpoint()+
				       "]�� ����"));
				    }
				    }
				   if (this.getNpcId() == 4500062){
				    if (player.getInventory().findItemId(40308).getCount() < 300000){ 
				     player.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
				       "30���� �̻� ������ ���Ӱ����մϴ�."));
				     return; 
				     }
				    this.setKpoint(this.getKpoint() + 5000);
				    player.getInventory().consumeItem(40308, 10000);//1����
				    int chance = _random.nextInt(15000)+1;
				    if (chance <= 100){ 
				     player.getInventory().storeItem(40308, 200000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 20������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 101 && chance <= 150){
				     player.getInventory().storeItem(40308, 400000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 40������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 151 && chance <= 175){
				     player.getInventory().storeItem(40308, 500000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 50������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 176 && chance <= 189){
				     player.getInventory().storeItem(40308, 1000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 100������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 190 && chance <= 192){
				     player.getInventory().storeItem(40308, 3000000);
				     String chatId = " 3���÷:"+player.getName()+"\\fY�� �Ƶ��� 300������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 193 && chance <= 194){
				     player.getInventory().storeItem(40308, 5000000);
				     String chatId = " 2���÷:"+player.getName()+"\\fY�� �Ƶ��� 500������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 194 && chance < 196){
				     player.getInventory().storeItem(40308, 30000000);
				     Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " 1���÷:"+player.getName()+"�� 1���÷��� 3000����ȹ��"));
				       } else if  (chance >= 196 && chance <= 200){
				        int k2 = (int) (this.getKpoint()*0.6);
				        int k3 = this.setKpoint(this.getKpoint() - k2);
				        player.getInventory().storeItem(40308, k3);
				     Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " ����:"+player.getName()+"�� ��� ["+k3+"]�� ȹ��"));
				     this.setKpoint(k2);
				       } else if (chance >= 201 && chance <= 15000){
				        player.sendPackets(new S_SystemMessage("\\fY���� : [5000]�� ����  , �� : ["+this.getKpoint()+
				       "]�� ����"));
				    }
				    }
				   if (this.getNpcId() == 4500063){
				    if (player.getInventory().findItemId(40308).getCount() < 500000){ 
				     player.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"50���� �̻� ������ ���Ӱ����մϴ�."));
				     return; 
				     }
				    this.setPpoint(this.getPpoint()+15000);
				    player.getInventory().consumeItem(40308, 30000);//3��
				    int chance = _random.nextInt(15000)+1;
				    if (chance <= 100){ 
				     player.getInventory().storeItem(40308, 300000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 30������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 101 && chance <= 150){
				     player.getInventory().storeItem(40308, 500000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 50������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 151 && chance <= 175){
				     player.getInventory().storeItem(40308, 1000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 100������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 176 && chance <= 189){
				     player.getInventory().storeItem(40308, 3000000);
				     String chatId = " ��÷:"+player.getName()+"\\fY�� �Ƶ��� 300������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 190 && chance <= 192){
				     player.getInventory().storeItem(40308, 5000000);
				     String chatId = " 3���÷:"+player.getName()+"\\fY�� �Ƶ��� 500������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 193 && chance <= 194){
				     player.getInventory().storeItem(40308, 20000000);
				     String chatId = " 2���÷:"+player.getName()+"\\fY�� �Ƶ��� 2000������÷!";
				     Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, chatId, 0));
				       } else if (chance >= 194 && chance < 196){
				     player.getInventory().storeItem(40308, 60000000);
				     Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " 1���÷:"+player.getName()+"�� 1���÷��� 6000����ȹ��"));
				       } else if  (chance >= 196 && chance <= 200){
				        int k2 = (int) (this.getPpoint()*0.6);
				        int k3 = this.setPpoint(this.getPpoint() - k2);
				        player.getInventory().storeItem(40308, k3);
				     Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, " ����:"+player.getName()+"�� ��� ["+k3+"]�� ȹ��"));
				     this.setPpoint(k2);
				       } else if (chance >= 201 && chance <= 15000){
				        player.sendPackets(new S_SystemMessage("\\fY���� : [15000]�� ����  , �� : ["+this.getPpoint()+ "]�� ����"));
				    }
				    }

				if (player.getLevel() < 99) {//����ƺ���
					FastTable<L1PcInstance> targetList = new FastTable<L1PcInstance>();
					targetList.add(player);
					FastTable<Integer> hateList = new FastTable<Integer>();
					hateList.add(1);
					CalcExp.calcExp(player, getId(), targetList, hateList,
							getExp());
				}
				/**����ΰ�� */
				if (player.isGm()) {
				if (this.getNpcId() == 45001|| this.getNpcId() == 45002){
				int dmg = attack.calcDamage();//�����������κ�
				player.sendPackets(new S_SystemMessage("\\fY����������:[" + dmg + "]�Դϴ�."));
			}
				}
				int heading = getMoveState().getHeading();
				if (heading < 7)
					heading++;
				else
					heading = 0;
				getMoveState().setHeading(heading);
				Broadcaster.broadcastPacket(this, new S_ChangeHeading(this));
			}
			attack.action();
		}
	}

	@Override
	public void onTalkAction(L1PcInstance l1pcinstance) {
	}

	public void onFinalAction() {
	}

	public void doFinalAction() {
	}
}
