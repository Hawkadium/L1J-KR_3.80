package l1j.server.server.model.npc.action.function;

import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.AQUA_PROTECTER;
import static l1j.server.server.model.skill.L1SkillId.CONCENTRATION;
import static l1j.server.server.model.skill.L1SkillId.EARTH_SKIN;
import static l1j.server.server.model.skill.L1SkillId.FIRE_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.INSIGHT;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.SHINING_AURA;

import java.util.Random;

import l1j.server.server.Opcodes;
import l1j.server.server.TimeController.InDunController;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1NpcActionHelper {
	private Random _random = new Random(System.nanoTime());
	
	private static L1NpcActionHelper _instance;
	
	public static L1NpcActionHelper getInstance() {
		if (_instance == null) {
			_instance = new L1NpcActionHelper();
		}
		return _instance;
	}
	
	public String NpcAction(L1PcInstance pc,  L1Object obj, String s, String htmlid) {
		try {	
				/** ���׸��� ���� �������� */
			 if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000196) { // ���׸���
				if (s.equalsIgnoreCase("7")) { // 1. ���� 30������ �Ǿ����ϴ�
					if (pc.getLevel() >= 30) { // pc�� ������ 30 �̻��̶��
						if (pc.getQuest().get_step(L1Quest.QUEST_VETERAN) == 0) { // ó�� �޴°Ŷ��
							pc.getInventory().storeItem(5000287, 1); // ���ñ�� �ݷ� ��Ű�� ����
							pc.sendPackets(new S_SystemMessage("���ñ�� �ݷ� ��Ű���� ������ϴ�."));
							pc.getQuest().set_step(L1Quest.QUEST_VETERAN, 1);
							htmlid = "veteranR1"; // ���ñ�� �ݷ� ��Ű���� ������ ��Ƚ��ϴ�.
						} else { // �ѹ� �޾Ҵٸ�
							htmlid = "veteranR4"; // ���ñ���� ������ �̹� ���޹����̽��ϴ�
						}
					} else { // pc�� ������ 30 �̸��̶��
						htmlid = "veteranR5"; // ������ ���ñ���� ������ �帱 �� �����ϴ�.
					}
				}

				if (s.equalsIgnoreCase("8")) { // 1. ���� 40������ �Ǿ����ϴ�
					if (pc.getLevel() >= 40) { // pc�� ������ 40 �̻��̶��
						if (pc.getQuest().get_step(L1Quest.QUEST_VETERAN) == 1) { // ó���޴°Ŷ��
							pc.getInventory().storeItem(5000288, 1); // ���ñ�� ���� ��Ű�� ����
							pc.sendPackets(new S_SystemMessage("���ñ�� ���� ��Ű���� ������ϴ�."));
							pc.getQuest().set_step(L1Quest.QUEST_VETERAN, 2);
							htmlid = "veteranR2"; // ���ñ�� ���� ��Ű���� ������ ��Ƚ��ϴ�.
						} else { // �ѹ� �޾Ҵٸ�
							htmlid = "veteranR4"; // ���ñ���� ������ �̹� ���޹����̽��ϴ�
						}
					} else { // pc�� ������ 40 �̸��̶��
						htmlid = "veteranR5"; // ������ ���ñ���� ������ �帱 �� �����ϴ�.
					}
				}

				if (s.equalsIgnoreCase("9")) { // 1. ���� 45������ �Ǿ����ϴ�
					if (pc.getLevel() >= 45) { // pc�� ������ 40 �̻��̶��
						if (pc.getQuest().get_step(L1Quest.QUEST_VETERAN) == 2) { // ó�� �޴°Ŷ��
							pc.getInventory().storeItem(5000289, 1); // ���ñ�� ���� ��������
							pc.sendPackets(new S_SystemMessage("���ñ�� ���� ������ ������ϴ�."));
							pc.getQuest().set_step(L1Quest.QUEST_VETERAN,L1Quest.QUEST_END);
							htmlid = "veteranR3"; // ���ñ�� ���� ������ ������ ��Ƚ��ϴ�.
						} else { // �ѹ� �޾Ҵٸ�
							htmlid = "veteranR6"; // ���ñ���� ������ ��� ���޹����̽��ϴ�.
						}
					} else { // pc�� ������ 40 �̸��̶��
						htmlid = "veteranR5"; // ������ ���ñ���� ������ �帱 �� �����ϴ�.
					}
				}

				// ���⸦ ����������
				if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("2")
						|| s.equalsIgnoreCase("3")) {
					int[] veteranWeapon = { 303, 304, 305, 306, 307, 308, 309,
							310 };
					for (int i = 0; i < veteranWeapon.length; i++) {
						if (pc.getInventory().checkItemNotEquipped(
								veteranWeapon[i], 1)) {
							pc.getInventory().consumeItem(veteranWeapon[i], 1);
							if (s.equalsIgnoreCase("1")) {
								pc.getInventory().storeItem(5000081, 200); // �����ֽ�20������
								pc.sendPackets(new S_ServerMessage(403,	"������ ���� �ֽ� (200)"));
							} else if (s.equalsIgnoreCase("2")) {
								pc.getInventory().storeItem(40087, 1); // ���� �����ֹ���
								pc.sendPackets(new S_ServerMessage(403,	"���� ���� �ֹ���"));
							} else if (s.equalsIgnoreCase("3")) {
								pc.getInventory().storeItem(40074, 1); // ���� ���� �ֹ���
								pc.sendPackets(new S_ServerMessage(403,	"���� ���� �ֹ���"));
							}
							htmlid = "veteranE3"; // ���� �Ǵ� ��� ��ȭ ���� �ֹ����α�ȯ�����̽��ϴ�.
						} else {
							// htmlid = "veteranE4"; // ��ȯ�Ͻ� ��� �����ϴ�. Ȥ â�� �ִ�
							// ����
							// �ƴ�����?
						}
					}
				}

				// ���� ����������
				if (s.equalsIgnoreCase("4") || s.equalsIgnoreCase("5")
						|| s.equalsIgnoreCase("6")) {
					int[] veteranArmor = { 500031, 500032, 500033, 500034,
							500035, 500036, 500037, 500038 };
					for (int i = 0; i < veteranArmor.length; i++) {
						if (pc.getInventory().checkItemNotEquipped(
								veteranArmor[i], 1)) {
							pc.getInventory().consumeItem(veteranArmor[i], 1);
							if (s.equalsIgnoreCase("4")) {
								pc.getInventory().storeItem(5000081, 200); // �����ֽ�20������
								pc.sendPackets(new S_ServerMessage(403,	"������ ���� �ֽ� (200)"));
							} else if (s.equalsIgnoreCase("5")) {
								pc.getInventory().storeItem(40087, 1); // ���� �����ֹ���
								pc.sendPackets(new S_ServerMessage(403,	"���� ���� �ֹ���"));
							} else if (s.equalsIgnoreCase("6")) {
								pc.getInventory().storeItem(40074, 1); // ���� �����ֹ���
								pc.sendPackets(new S_ServerMessage(403,	"���� ���� �ֹ���"));
							}
							htmlid = "veteranE3"; // ���� �Ǵ� ��� ��ȭ ���� �ֹ����� ��ȯ�����̽��ϴ�.
						} else {
							// htmlid = "veteranE4"; // ��ȯ�Ͻ� ��� �����ϴ�. Ȥ â�� �ִ�
							// ����
							// �ƴ�����?
						}
					}
				}
				
				
			
			
					
				
				
				
				
			   	//��������Ʈ ������ 
			  } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 401060) {

				   if (s.equalsIgnoreCase("A")){
				    if (!pc.getInventory().checkItem(5438)){ //������ �ָӴ� ������~
				    pc.getInventory().storeItem(5438, 1);//������ �ָӴ� 
				    htmlid = "highdaily4"; 
				    pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "������ �� ã�� ���ø� ����Ʈ�� ���� �Ͻ� �� �ֽ��ϴ�."));
				    } else {
				    htmlid = "highdaily20";	
				    }
			    	} else if (s.equalsIgnoreCase("B")){
				    if(pc.getInventory().checkItem(5437)) {//������ ����
				     htmlid ="highdaily5";
				    	} else {
				     htmlid ="highdaily10";
				    }
			    	} else if (s.equalsIgnoreCase("C")){
			    		
			    		
			    		if (pc.getInventory().checkItem(5439, 100)){//����� ��ǥ
				     while(pc.getInventory().consumeItem(5439, 1)); //����� ��ǥ
				     while(pc.getInventory().consumeItem(5437, 1)); //������ ����
				        
				     Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
					     pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
				     int[] allBuffSkill = { HASTE, ADVANCE_SPIRIT, EARTH_SKIN, AQUA_PROTECTER, CONCENTRATION, PATIENCE, INSIGHT, SHINING_AURA, FIRE_WEAPON };
				     pc.setBuffnoch(1);
				     L1SkillUse l1skilluse = new L1SkillUse();
				     for (int i = 0; i < allBuffSkill.length; i++) {
				     l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
				     }
				     pc.getInventory().storeItem(437011, 3); //����
				     pc.getInventory().storeItem(40308, 5000000); //����
				     pc.sendPackets(new S_SystemMessage("����:�巡������(3)�� ������ϴ�."));
				     pc.sendPackets(new S_SystemMessage("����:�Ƶ���(5�鸸)�� ������ϴ�."));
				     
				     
				     pc.save();
				     htmlid = "highdaily7";
				    } else {
				     htmlid = "highdaily31";
				    }
			    	} else if (s.equalsIgnoreCase("D")){
				    if (pc.getInventory().checkItem(5438, 1)){//���������ָӴ�
				     pc.getInventory().consumeItem(5438, 1);
				     htmlid ="highdaily8";
				     }
				    }	
				   
				    /** �巡�� �� ������ */
					  } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 401240) {
						   int oldLevel = pc.getLevel();
						   int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
						   int exp = 0;
						   if (s.equalsIgnoreCase("A")) {
						   if (!pc.getInventory().checkItem(5455)) {//�巡��� �������� �ָӴ�
						     pc.getInventory().storeItem(5455, 1);
						     htmlid = "highdailyb4";
						     pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "�巡��� ������ �� ã�� ���ø� ����Ʈ�� ���� �Ͻ� �� �ֽ��ϴ�."));
					 } else {
							 htmlid = "highdailyb20";	
							 }
				     } else if (s.equalsIgnoreCase("B")) {
						    if (pc.getInventory().checkItem(5456)) { // ������ ����
						        htmlid = "highdailyb5";
					    } else {
						        htmlid = "highdailyb10";
						    }
					 } else if (s.equalsIgnoreCase("C")) {
						    if (pc.getInventory().checkItem(5451, 100)) { // �巡�� ��
						     while (pc.getInventory().consumeItem(5451, 1));
						     while (pc.getInventory().consumeItem(5456, 1)); // ������ ����
						     exp = (int) (needExp * 0.90);
						     pc.addExp(exp);
						     Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
						     pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
						     int[] allBuffSkill = { HASTE, ADVANCE_SPIRIT, EARTH_SKIN, AQUA_PROTECTER, CONCENTRATION, PATIENCE, INSIGHT, SHINING_AURA, FIRE_WEAPON };
						     pc.setBuffnoch(1);
						     L1SkillUse l1skilluse = new L1SkillUse();
						     for (int i = 0; i < allBuffSkill.length; i++) {
						      l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						      }
						      pc.save();
						      htmlid = "highdailyb7";
					  } else {
						      htmlid = "highdailyb31";
						    }
				   } else if (s.equalsIgnoreCase("D")) {
						    if (pc.getInventory().checkItem(5455, 1)) {
						          pc.getInventory().consumeItem(5455, 1);
						          htmlid = "highdailyb8";
						          }
						     }
						 //�ǵ��� ( ������ �ϴ����� )
					  } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40199) {
								// �Ҳ��� ������ �޴´� ( ó���κ� )
								if (s.equalsIgnoreCase("0")) { // �ڷ�?
									htmlid = "";
								} else if (s.equalsIgnoreCase("a")) {
									if (pc.getInventory().checkItem(41159) == true) {
										if (pc.getLevel() >= 5) {
											pc.getInventory().consumeItem(41159, 10);
											int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211, 115, 107, 148 };
											pc.setBuffnoch(1);
											L1SkillUse l1skilluse = new L1SkillUse();
											for (int i = 0; i < allBuffSkill.length; i++) {
											l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
											}
											htmlid = "";
										} else {
											pc.sendPackets(new S_SystemMessage("5���� �̻���� ��� �����մϴ�."));
										}
										//�ٶ��� ������ �޴´� (ó���κ�)
									} else if (pc.getInventory().checkItem(41159) == true) {
										if (pc.getLevel() >= 5) {
											pc.getInventory().consumeItem(41159, 10);
											int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211, 115, 148 };
											pc.setBuffnoch(1);
											L1SkillUse l1skilluse = new L1SkillUse();
											for (int i = 0; i < allBuffSkill.length; i++) {
												l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
											}
											htmlid = "";
										} else {
											pc.sendPackets(new S_SystemMessage("5���� �̻���� ��� �����մϴ�."));
										}
										//��
									} else {
										pc.sendPackets(new S_SystemMessage("�Ƚ��Ǳ����� �����մϴ�."));
									}
								}
								//��ȥ�� ������ �޴´� ( ó���κ�)
						//	} else if (pc.getInventory().checkItem(41159) == true) {
								if (s.equalsIgnoreCase("b")) {
									if (pc.getInventory().checkItem(41159, 10)) {
										if (pc.getLevel() >= 5) {
											pc.getInventory().consumeItem(41159, 10);
											int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211, 115, 149 };
											pc.setBuffnoch(1);
											L1SkillUse l1skilluse = new L1SkillUse();
											for (int i = 0; i < allBuffSkill.length; i++) {
												l1skilluse.handleCommands(pc, allBuffSkill[i],
														pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
											}
											htmlid = "";
										} else {
											pc.sendPackets(new S_SystemMessage("5���� �̻���� ��� �����մϴ�."));
										}
									} else {
										pc.sendPackets(new S_SystemMessage("�Ƚ��Ǳ����� �����մϴ�."));
									}
								}
								
								/*** �ϴ����� ������ ***/
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 402030) {
							L1NpcInstance npc = (L1NpcInstance) obj;
								if (pc.getInventory().checkItem(41159, 50) && pc.getInventory().checkItem(5565, 100)) {
									pc.getInventory().consumeItem(41159, 50);//���л���
									pc.getInventory().consumeItem(5565, 100);//��ȥ����
									pc.getInventory().storeItem(46446, 1);//�����ֱ�
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"������ �Ƚ� ���ڸ� ������ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
								} else {
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƚ��Ǳ��� �Ǵ� ��ȥ�� �����մϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									pc.sendPackets(new S_NpcChatPacket(npc,"���� �غ����̿���! ���߿����ø� �ΰԵ帱����!", 0));
								}
			 
								/*** �ϴ����� ������ �湮�� ***/
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200017) {
							L1NpcInstance npc = (L1NpcInstance) obj;
							if (s.equalsIgnoreCase("0")) { //�縻 1���ͱ�ȯ
							 	if (pc.getInventory().checkItem(5565, 100)) {
									pc.getInventory().consumeItem(5565, 100);
									pc.getInventory().storeItem(65481, 1);
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� �Ƶ����� �Һ��ϰ�, ������ �޾ҽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									htmlid = "sustranger4"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "sustranger5"; // ��ᰡ ������ �׼�
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"��ſ��Դ� '�Ƚ��� ��ȥ'�� �����մϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									pc.sendPackets(new S_NpcChatPacket(npc,"���� �غ����̿���! ���߿����ø� �ΰԵ帱����!", 0));
								}
							}
								
								/*** �Է� ������ ***/
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 76000) {
							if (s.equalsIgnoreCase("0")) { // �Է�������� ���� 1�� ��ȯ
								if (pc.getInventory().checkItem(5000061, 1)	&& pc.getInventory().checkItem(437017, 2)) {
									pc.getInventory().consumeItem(5000061, 1);
									pc.getInventory().consumeItem(437017, 2);
									pc.getInventory().storeItem(5000059, 1);
									pc.sendPackets(new S_SystemMessage("�Է��� ������ ������ ������ϴ�."));
									htmlid = "gerengfoll2"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "gerengfoll3"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("1")) { // �Է��� ����� ���� 10�� ��ȯ
								if (pc.getInventory().checkItem(5000061, 10) && pc.getInventory().checkItem(437017, 20)) {
									pc.getInventory().consumeItem(5000061, 10);
									pc.getInventory().consumeItem(437017, 20);
									pc.getInventory().storeItem(5000059, 10);
									pc.sendPackets(new S_SystemMessage("�Է��� ������ ���� (10)���� ������ϴ�."));
									htmlid = "gerengfoll2"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "gerengfoll3"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("2")) { // �Է�������� ���� 1�� ��ȯ
								if (pc.getInventory().checkItem(5000062, 1)	&& pc.getInventory().checkItem(437017, 2)) {
									pc.getInventory().consumeItem(5000062, 1);
									pc.getInventory().consumeItem(437017, 2);
									pc.getInventory().storeItem(5000060, 1);
									pc.sendPackets(new S_SystemMessage("�Է��� ����� ������ ������ϴ�."));
									htmlid = "gerengfoll2"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "gerengfoll3"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("3")) { // �Է��� ����� ���� 10�� ��ȯ
								if (pc.getInventory().checkItem(5000062, 10) && pc.getInventory().checkItem(437017, 20)) {
									pc.getInventory().consumeItem(5000062, 10);
									pc.getInventory().consumeItem(437017, 20);
									pc.getInventory().storeItem(5000060, 10);
									pc.sendPackets(new S_SystemMessage("�Է��� ����� ���� (10)���� ������ϴ�."));
									htmlid = "gerengfoll2"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "gerengfoll3"; // ��ᰡ ������ �׼�
								}
							}
							
								/** ��ũ ���ʱ��� �ڷ����� **/
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 170823) {
							if (s.equalsIgnoreCase("a")) {
								if (pc.getLevel() >= 45) {//45�̻�
									L1Teleport.teleport(pc, 32750, 32892, (short) 261, 5, true,
											false);
									htmlid = "";
								} else {
									htmlid = "islandev2";
								}
							
						} else if (s.equalsIgnoreCase("b")) {
							if (pc.getLevel() >= 45) {//45�̻�
								L1Teleport.teleport(pc, 32750, 32892, (short) 263, 5, true,
										false);
								htmlid = "";
							} else {
								htmlid = "islandev2";
							}
						} else if (s.equalsIgnoreCase("c")) {
							if (pc.getLevel() >= 45) {//45�̻�
								L1Teleport.teleport(pc, 32750, 32892, (short) 264, 5, true,
										false);
								htmlid = "";
							} else {
								htmlid = "islandev2";
							}
						} else if (s.equalsIgnoreCase("d")) {
							if (pc.getLevel() >= 45) {//45�̻�
								L1Teleport.teleport(pc, 32750, 32892, (short) 265, 5, true,
										false);
								htmlid = "";
							} else {
								htmlid = "islandev2";
							}
						} else if (s.equalsIgnoreCase("e")) {
							if (pc.getLevel() >= 45) {//45�̻�
								L1Teleport.teleport(pc, 32750, 32892, (short) 266, 5, true,
										false);
								htmlid = "";
							} else {
								htmlid = "islandev2";
							}
						} else if (s.equalsIgnoreCase("f")) {
							if (pc.getLevel() >= 45) {//45�̻�
								L1Teleport.teleport(pc, 32750, 32892, (short) 268, 5, true,
										false);
								htmlid = "";
							} else {
								htmlid = "islandev2";
							}
						} else if (s.equalsIgnoreCase("g")) {
							if (pc.getLevel() >= 45) {//45�̻�
								L1Teleport.teleport(pc, 32750, 32892, (short) 269, 5, true,
										false);
								htmlid = "";
							} else {
								htmlid = "islandev2";
							}
						} else if (s.equalsIgnoreCase("h")) {
							if (pc.getLevel() >= 45) {//45�̻�
								L1Teleport.teleport(pc, 32750, 32892, (short) 279, 5, true,
										false);
								htmlid = "";
							} else {
								htmlid = "islandev2";
							}
						}/** �������! **/

							// ����ġ ���޴�
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200008) {
							if (s.equalsIgnoreCase("0")) {
								if (pc.getLevel() < 51) {
									pc.addExp((ExpTable.getExpByLevel(52) - 1) - pc.getExp()
											- ((ExpTable.getExpByLevel(52) - 1) / 100));
								} else if (pc.getLevel() >= 51 && pc.getLevel() < 70) {
									pc.addExp((ExpTable.getExpByLevel(pc.getLevel() + 2) - 1)
											- pc.getExp() - 100);
									pc.setCurrentHp(pc.getMaxHp());
									pc.setCurrentMp(pc.getMaxMp());
								}
								if (ExpTable.getLevelByExp(pc.getExp()) >= 70) {
									htmlid = "expgive3";
								} else {
									htmlid = "expgive1";
								}
							}
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200088) {
							if (s.equalsIgnoreCase("0")) {
								if (pc.getLevel() < 51) {
									pc.addExp((ExpTable.getExpByLevel(52) - 1) - pc.getExp()
											- ((ExpTable.getExpByLevel(52) - 1) / 100));
								} else if (pc.getLevel() >= 51 && pc.getLevel() < 85) {
									pc.addExp((ExpTable.getExpByLevel(pc.getLevel() + 2) - 1)
											- pc.getExp() - 100);
									pc.setCurrentHp(pc.getMaxHp());
									pc.setCurrentMp(pc.getMaxMp());
								}
								if (ExpTable.getLevelByExp(pc.getExp()) >= 85) {
									htmlid = "expgive3";
								} else {
									htmlid = "expgive1";
								}
							}
							// ��簡 ����
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212001) {
							L1ItemInstance item = null;
							if (s.equalsIgnoreCase("0")) {
								if (pc.getInventory().checkItem(L1ItemId.DRUGA_POKET)) { // �̹�
									// �ָӴϰ�
									// �ִ�.
									htmlid = "veil3";
								} else if (!pc.getInventory().checkItem(L1ItemId.ADENA, 100000)) { // 100��
									// �Ƶ�����
									// ����.
									htmlid = "veil4";
								} else { // �ָӴϰ� ���� 100�� �Ƶ����� ������ (������ �ִ� ������ �ȴ�)
									pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
									item = pc.getInventory().storeItem(L1ItemId.DRUGA_POKET, 1);
									pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
									htmlid = "veil7";
								}
							}
							
							// ũ����
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212012) {
							if (s.equalsIgnoreCase("a")) {
								new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_CRAY, pc
										.getId(), pc.getX(), pc.getY(), null, 0,
										L1SkillUse.TYPE_SPELLSC);
								htmlid = "grayknight2";
							}
							
							/*** ��ũ ��Ÿ ***/
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 57002) {
							if (s.equalsIgnoreCase("1")) { //�縻1000���ͱ�ȯ
								if (pc.getInventory().checkItem(65487, 1000)) {//�縻üũ
									pc.getInventory().consumeItem(65487, 1000);//�縻����
									pc.getInventory().storeItem(6548, 10);//�����ֱ�
								//	pc.sendPackets(new S_SystemMessage("�縻(1000��)�� ��ȯ �Ͽ����ϴ�."));
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"ũ�������� �縻 (1000��)�� ��ȯ�Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									htmlid = "santaorc01"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "santaorc03"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("2")) { //�縻 500���� ��ȯ
								if (pc.getInventory().checkItem(65487, 500)) {
									pc.getInventory().consumeItem(65487, 500);
									pc.getInventory().storeItem(6548, 7);
								//	pc.sendPackets(new S_SystemMessage("�縻(500��)�� ��ȯ �Ͽ����ϴ�."));
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"ũ�������� �縻 (500��)�� ��ȯ�Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									htmlid = "santaorc01"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "santaorc03"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("3")) { //�縻 100���� ��ȯ
								if (pc.getInventory().checkItem(65487, 100)) {
									pc.getInventory().consumeItem(65487, 100);
									pc.getInventory().storeItem(6548, 5);
								//	pc.sendPackets(new S_SystemMessage("�縻(100��)�� ��ȯ �Ͽ����ϴ�."));
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"ũ�������� �縻 (100��)�� ��ȯ�Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									htmlid = "santaorc01"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "santaorc03"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("4")) { //�縻 50���ͱ�ȯ
								if (pc.getInventory().checkItem(65487, 50)) {
									pc.getInventory().consumeItem(65487, 50);
									pc.getInventory().storeItem(6548, 3);
							//		pc.sendPackets(new S_SystemMessage("�縻(50��)�� ��ȯ �Ͽ����ϴ�."));
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"ũ�������� �縻 (50��)�� ��ȯ�Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									htmlid = "santaorc01"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "santaorc03"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("5")) { //�縻 10���� ��ȯ
								if (pc.getInventory().checkItem(65487, 10)) {
									pc.getInventory().consumeItem(65487, 10);
									pc.getInventory().storeItem(6548, 3);
								//	pc.sendPackets(new S_SystemMessage("�縻(10��)�� ��ȯ �Ͽ����ϴ�."));
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"ũ�������� �縻 (10��)�� ��ȯ�Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									htmlid = "santaorc01"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "santaorc03"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("6")) { //�縻 1���ͱ�ȯ
								if (pc.getInventory().checkItem(65487, 1)) {
									pc.getInventory().consumeItem(65487, 1);
									pc.getInventory().storeItem(6548, 1);
								//	pc.sendPackets(new S_SystemMessage("�縻(50��)�� ��ȯ �Ͽ����ϴ�."));
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"ũ�������� �縻 (1��)�� ��ȯ�Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
									htmlid = "santaorc01"; // �ŷ� �Ϸ� �׼�
								} else {
									htmlid = "santaorc03"; // ��ᰡ ������ �׼�
								}
							}
							if (s.equalsIgnoreCase("b")) { //��ũ��Ÿ ��������
										if (pc.getInventory().checkItem(40308, 0)) {
										if (pc.getLevel() >= 5) {
											pc.getInventory().consumeItem(40308, 0);
											int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211, 115, 107, 148 };//�̰ŵ� ���̾���� �ߺ��̳׿�..
											pc.setBuffnoch(1);
											L1SkillUse l1skilluse = new L1SkillUse();
											for (int i = 0; i < allBuffSkill.length; i++) {
												l1skilluse.handleCommands(pc, allBuffSkill[i],
														pc.getId(), pc.getX(), pc.getY(), null, 0,
														L1SkillUse.TYPE_GMBUFF);
											}
											S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"��ſ��� '��ũ��Ÿ'�� ������ �����մϴ�.", Opcodes.S_OPCODE_MSG, 20); 
											pc.sendPackets(s_chatpacket);
											htmlid = "santaorc01"; // �ŷ� �Ϸ� �׼�
										} else {			
										S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� Lv.5 ���� �Դϴ�.", Opcodes.S_OPCODE_MSG, 20); 
										pc.sendPackets(s_chatpacket);
										}
								} else {
									htmlid = "santaorc03"; // ��ᰡ ������ �׼�
								}
							}
							
							/** ���������� **/
						} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000065) {
								if (s.equalsIgnoreCase("a")) { //�ٰŸ�����
									if (pc.getInventory().checkItem(40308, 0)) {
									if (pc.getLevel() >= 5) {
										pc.getInventory().consumeItem(40308, 0);
										int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211,115, 107, 148 };
										pc.setBuffnoch(1);
										L1SkillUse l1skilluse = new L1SkillUse();
										for (int i = 0; i < allBuffSkill.length; i++) {
										l1skilluse.handleCommands(pc, allBuffSkill[i],pc.getId(), pc.getX(), pc.getY(), null, 0,
													L1SkillUse.TYPE_GMBUFF);
										}
										htmlid = "";
									} else {
										S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� Lv.5 ���� �Դϴ�.", Opcodes.S_OPCODE_MSG, 20); 
										pc.sendPackets(s_chatpacket);
									}					
									} else {
										//pc.sendPackets(new S_ServerMessage(189));
										S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ �ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
										pc.sendPackets(s_chatpacket);
										
									}
							}
							if (s.equalsIgnoreCase("b")) { //���Ÿ�����
								if (pc.getInventory().checkItem(40308, 0)) {
									if (pc.getLevel() >= 5) {
										pc.getInventory().consumeItem(40308, 0);
										int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211, 115, 149 };
										pc.setBuffnoch(1);
										L1SkillUse l1skilluse = new L1SkillUse();
										for (int i = 0; i < allBuffSkill.length; i++) {
											l1skilluse.handleCommands(pc, allBuffSkill[i],
													pc.getId(), pc.getX(), pc.getY(), null, 0,
													L1SkillUse.TYPE_GMBUFF);
										}
										htmlid = "";
									} else {
										S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� Lv.5 ���� �Դϴ�.", Opcodes.S_OPCODE_MSG, 20); 
										pc.sendPackets(s_chatpacket);
									}
								} else {
									//pc.sendPackets(new S_ServerMessage(189));
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ �ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
								}
							}
															
				/** �ʺ��� ����� �߰� - SRC�� ����ȭ **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 705141) {
					if (s.equalsIgnoreCase("D")) {
						if (pc.getLevel() == 2) {
							htmlid = "tutorm1_bs";
						} else if (pc.isWizard()) {
							if (pc.getLevel() <= 3) {
								htmlid = "tutorm1_bs";
							} else if (pc.getLevel() <= 5) {
								htmlid = "tutorm2_bs";
							} else if (pc.getLevel() <= 7) {
								htmlid = "tutorm3_bs";
							} else if (pc.getLevel() <= 11) {
								htmlid = "tutorm4_bs";
							} else if (pc.getLevel() <= 12) {
								htmlid = "tutorm5_bs";
							}
						} else if (pc.isCrown()) {
							if (pc.getLevel() <= 4) {
								htmlid = "tutorp1_bs";
							} else if (pc.getLevel() <= 7) {
								htmlid = "tutorp2_bs";
							} else if (pc.getLevel() <= 9) {
								htmlid = "tutorp3_bs";
							} else if (pc.getLevel() <= 11) {
								htmlid = "tutorp4_bs";
							} else if (pc.getLevel() <= 12) {
								htmlid = "tutorp5_bs";
							}
						} else if (pc.isElf()) {
							if (pc.getLevel() <= 4) {
								htmlid = "tutore1_bs";
							} else if (pc.getLevel() <= 7) {
								htmlid = "tutore2_bs";
							} else if (pc.getLevel() == 8) {
								htmlid = "tutore3_bs";
							} else if (pc.getLevel() == 9) {
								htmlid = "tutore4_bs";
							} else if (pc.getLevel() <= 11) {
								htmlid = "tutore5_bs";
							} else if (pc.getLevel() == 12) {
								htmlid = "tutore6_bs";
							}
						} else if (pc.isDarkelf()) {
							if (pc.getLevel() <= 4) {
								htmlid = "tutord1_bs";
							} else if (pc.getLevel() <= 6) {
								htmlid = "tutord2_bs";
							} else if (pc.getLevel() <= 8) {
								htmlid = "tutord3_bs";
							} else if (pc.getLevel() <= 10) {
								htmlid = "tutord4_bs";
							} else if (pc.getLevel() == 12) {
								htmlid = "tutord5_bs";
							}
						} else if (pc.isIllusionist()) {
							if (pc.getLevel() <= 4) {
								htmlid = "tutori1_bs";
							} else if (pc.getLevel() <= 6) {
								htmlid = "tutori2_bs";
							} else if (pc.getLevel() <= 9) {
								htmlid = "tutori3_bs";
							} else if (pc.getLevel() <= 11) {
								htmlid = "tutori4_bs";
							} else if (pc.getLevel() == 12) {
								htmlid = "tutori5_bs";
							}
						} else {
							if (pc.getLevel() <= 4) {
								htmlid = "tutordk1_bs";
							} else if (pc.getLevel() <= 6) {
								htmlid = "tutordk2_bs";
							} else if (pc.getLevel() <= 8) {
								htmlid = "tutordk3_bs";
							} else if (pc.getLevel() <= 10) {
								htmlid = "tutordk4_bs";
							} else if (pc.getLevel() <= 12) {
								htmlid = "tutordk5_bs";
							}
						}
					} else if (s.equalsIgnoreCase("O")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32605, 32837, (short) 2005, 4, true); // �������ʱٱ�
					} else if (s.equalsIgnoreCase("P")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32733, 32902, (short) 2005, 4, true); // �������ʱٱ�
					} else if (s.equalsIgnoreCase("Q")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32559, 32843, (short) 2005, 4, true); // ���������ʻ����
					} else if (s.equalsIgnoreCase("R")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32677, 32982, (short) 2005, 4, true); // ���������� �����
					} else if (s.equalsIgnoreCase("S")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32781, 32854, (short) 2005, 4, true); // ���� �ϵ��� �����
					} else if (s.equalsIgnoreCase("T")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32674, 32739, (short) 2005, 4, true); // �����ϼ��� �����
					} else if (s.equalsIgnoreCase("U")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32578, 32737, (short) 2005, 4, true); // �������� �����
					} else if (s.equalsIgnoreCase("V")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32542, 32996, (short) 2005, 4, true); // ���� ���ʻ����
					} else if (s.equalsIgnoreCase("W")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32794, 32973, (short) 2005, 4, true); // ���� ���� �����
					} else if (s.equalsIgnoreCase("X")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32803, 32789, (short) 2005, 4, true); // ���� ����

					} else if (s.equalsIgnoreCase("L")) { // ���ž ���� ����
						if (pc.getLevel() < 3) {
							htmlid = "";
						} else if (pc.getLevel() > 9 && pc.isElf()) {
							htmlid = "";
							pc.getInventory().storeItem(40101, 1);
							pc.sendPackets(new S_SystemMessage("�ʺ��� ���� �ʺ��� ����̰� ��ſ��� ������ ��� ��ȯ �ֹ����� �־����ϴ�."));
							L1Teleport.teleport(pc, 34041, 32155, (short) 4, 4,	true);
						}
					} else if (s.equalsIgnoreCase("M")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32878, 32905, (short) 304, 4, true);
						pc.getInventory().storeItem(40101, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� �ʺ��� ����̰� ��ſ��� ������ ��� ��ȯ �ֹ����� �־����ϴ�."));
					} else if (s.equalsIgnoreCase("I")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32635, 33182, (short) 4, 4, true); // ����
						pc.getInventory().storeItem(40101, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� �ʺ��� ����̰� ��ſ��� ������ ��� ��ȯ �ֹ����� �־����ϴ�."));
					} else if (s.equalsIgnoreCase("N")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32760, 32885, (short) 1000, 4, true);
						pc.getInventory().storeItem(40101, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� �ʺ��� ����̰� ��ſ��� ������ ��� ��ȯ �ֹ����� �־����ϴ�."));
					} else if (s.equalsIgnoreCase("H")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32575, 32944, (short) 0, 4, true); // ���ϴ¼�â��
						pc.getInventory().storeItem(40101, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� �ʺ��� ����̰� ��ſ��� ������ ��� ��ȯ �ֹ����� �־����ϴ�."));
					} else if (s.equalsIgnoreCase("K")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32585, 32956, (short) 0, 4, true); // �Է�
						pc.getInventory().storeItem(40101, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� �ʺ��� ����̰� ��ſ��� ������ ��� ��ȯ �ֹ����� �־����ϴ�."));
					} else if (s.equalsIgnoreCase("J")) {
						htmlid = "";
						L1Teleport.teleport(pc, 32675, 32813, (short) 2005, 4, true); // �������
						pc.getInventory().storeItem(40101, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� �ʺ��� ����̰� ��ſ��� ������ ��� ��ȯ �ֹ����� �־����ϴ�."));
					}
					
					/** ������ ������ �߰� - SRC�� ����ȭ **/
				} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 710141) {
					if (s.equalsIgnoreCase("A")) {
						pc.setExp(pc.getExp() + 546);
						/** [����ȭ�� ���� �ҽ� ���� ����] ��Ʈ�� �޶� �̷��� ���� **/
						pc.getInventory().storeItem(20028, 1); // ���۾� ����?
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� ������ �������� ��ſ��� ���ž�� ���� ������ �־����ϴ�."));
						pc.getInventory().storeItem(20126, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� ������ �������� ��ſ��� ���ž�� ���� ������ �־����ϴ�."));
						pc.getInventory().storeItem(20206, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� ������ �������� ��ſ��� ���ž�� ���� ������ �־����ϴ�."));
						pc.getInventory().storeItem(20173, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� ������ �������� ��ſ��� ���ž�� ���� �尩�� �־����ϴ�."));
						pc.getInventory().storeItem(20232, 1);
						pc.sendPackets(new S_SystemMessage("�ʺ��� ���� ������ �������� ��ſ��� ���ž�� ���� ���и� �־����ϴ�."));
						int[] item_ids = { 40099, 40101, 40098, 40029, 40030, }; // �������۾�
						int[] item_amounts = { 30, 5, 20, 50, 5, };
						for (int i = 0; i < item_ids.length; i++) {
							L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
							pc.sendPackets(new S_ServerMessage(143, "$8447", item.getItem().getName()+ "("+ item_amounts[i] + ")"));
						}
						htmlid = "";
					}			
						
				/** �ػ��� ������� */
				}else if(((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 460000040) {
				if(s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(40308, 5000000)){ //������ õ���Ƶ���
					if (pc.getLevel() >= 60) { //���� ���� ����
						if (pc.isInParty() && pc.getParty().isLeader(pc)){
							if (pc.getParty().getNumOfMembers() >= 8){ //��Ƽ �����
								pc.getInventory().consumeItem(40308, 5000000); //������ �Ƶ���
								pc.getParty().getLeader().getName();
								L1Party party = pc.getParty();
								L1PcInstance[] players = party.getMembers();
								for(L1PcInstance pc1:players){
									InDunController.getInstance().addPlayMember(pc1);
									Random random = new Random(); 
									int i13 = 32799 + random.nextInt(3); 
									int k19 = 32808 + random.nextInt(3);
									L1Teleport.teleport(pc1, i13, k19, (short) 9101, 5, true);
								}
								htmlid = "";
							} else {
								pc.sendPackets(new S_SystemMessage("�ּ� �ο��� 8�� �Ӵϴ�."));
							}
						} else {
							pc.sendPackets(new S_SystemMessage("��Ƽ�� �������� �ȳ��� �Ҽ� �ֽ��ϴ�."));
						}
					} else {
						pc.sendPackets(new S_SystemMessage("��Ƽ�� ������ 60�̻��� �ƴմϴ�."));
					}
				} else {
					pc.sendPackets(new S_SystemMessage("����ᰡ �����մϴ�(���鸸�Ƶ���)"));
				}
				}
									/** ���ɼ� ������� */
				}else if(((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 460000041) {
				if(s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(40308, 5000000)){ //������ õ���Ƶ���
					if (pc.getLevel() >= 60) { //���� ���� ����
						if (pc.isInParty() && pc.getParty().isLeader(pc)){
							if (pc.getParty().getNumOfMembers() >= 8){ //��Ƽ �����
								pc.getInventory().consumeItem(40308, 5000000); //������ �Ƶ���
								pc.getParty().getLeader().getName();
								L1Party party = pc.getParty();
								L1PcInstance[] players = party.getMembers();
								for(L1PcInstance pc1:players){
									InDunController.getInstance().addPlayMember(pc1);
									Random random = new Random(); 
									int i13 = 32796 + random.nextInt(3); 
									int k19 = 32802 + random.nextInt(3);
									L1Teleport.teleport(pc1, i13, k19, (short) 9102, 5, true);
								}
								htmlid = "";
							} else {
								pc.sendPackets(new S_SystemMessage("�ּ� �ο��� 8�� �Ӵϴ�."));
							}
						} else {
							pc.sendPackets(new S_SystemMessage("��Ƽ�� �������� �ȳ��� �Ҽ� �ֽ��ϴ�."));
						}
					} else {
						pc.sendPackets(new S_SystemMessage("��Ƽ�� ������ 60�̻��� �ƴմϴ�."));
					}
				} else {
					pc.sendPackets(new S_SystemMessage("����ᰡ �����մϴ�(5�鸸�Ƶ���)"));
				}
				}
				
				} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039031) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (s.equalsIgnoreCase("a")) {
						if (pc.getInventory().checkItem(5000136, 1)) {
							htmlid = "birthday2";
						} else {
							pc.getInventory().storeItem(5000136, 1);
							htmlid = "birthday3";
						}
					}

					if (s.equalsIgnoreCase("b")) {
						if (pc.getInventory().checkItem(5000141, 1)) {
							pc.getInventory().consumeItem(5000141, 1);
							new L1SkillUse().handleCommands(pc,
								L1SkillId.STATUS_COMA_5, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							htmlid = "birthday4";
						} else { // ��ᰡ ������ ���
							pc.sendPackets(new S_NpcChatPacket(npc,"���� ���� �ָӴϰ� �ʿ��մϴ�.", 0));
						}
					}
				
				//�����Ʒñ���
			 } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40090) { 
	          	   if (s.equalsIgnoreCase("0")) { 
	          		   if (pc.getsub() == 0) {
	          			    pc.getInventory().storeItem(5445, 1); //������ ���� �ָӴ�
	          			    pc.getInventory().storeItem(427131, 1); //�������� ��Ʈ
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� $14171 ��  �־����ϴ�."));
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� $14187 ��  �־����ϴ�."));
	          	          if (pc.isCrown()) {
	          	        	 pc.getInventory().storeItem(40226, 1); //������ (Ʈ�� Ÿ��)
	 						pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ (Ʈ�� Ÿ��) ��  �־����ϴ�."));
	                      } else if (pc.isElf()) {
	          	          pc.getInventory().storeItem(40233, 1); //������ ���� (�ٵ� �� ���ε�)
	          	          pc.getInventory().storeItem(40234, 1);//������ ���� (�ڷ���Ʈ �� ����)
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ ���� (�ٵ� �� ���ε�) ��  �־����ϴ�."));
	 						pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ ���� (�ڷ���Ʈ �� ����) ��  �־����ϴ�."));
	                      } else if (pc.isWizard()) {
	          	          pc.getInventory().storeItem(40188, 1); //������ (���̽�Ʈ)
	          	          pc.getInventory().storeItem(40176, 1); //������ (�޵����̼�)
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ (���̽�Ʈ) ��  �־����ϴ�."));
	 						pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ (�޵����̼�) ��  �־����ϴ�."));
	                      } else if (pc.isDarkelf()) {
	          	          pc.getInventory().storeItem(40268, 1);//�������� ���� (�긵 ����)
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� �������� ���� (�긵 ����) ��  �־����ϴ�."));
	                      } else if (pc.isDragonknight()) {
	          	          pc.getInventory().storeItem(439100, 1); //������ ����(�巡�� ��Ų)
	          	          pc.getInventory().storeItem(439106, 1); //������ ����(���� �����̾�)
	          	          pc.getInventory().storeItem(439101, 1);//������ ����(���� ������)
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ ����(�巡�� ��Ų)) ��  �־����ϴ�."));
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ ����(���� �����̾�) ��  �־����ϴ�."));
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ������ ����(���� ������) ��  �־����ϴ�."));
	                      } else if (pc.isIllusionist()) {
	          	          pc.getInventory().storeItem(439004, 1); //����� ����(ť��:�̱״ϼ�)
	          	          pc.getInventory().storeItem(439000, 1); //����� ����(�̷� �̹���)
	          	          pc.getInventory().storeItem(439001, 1); //����� ����(��ǻ��)
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ����� ����(ť��:�̱״ϼ�) ��  �־����ϴ�.."));
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ����� ����(�̷� �̹���) ��  �־����ϴ�."));
							pc.sendPackets(new S_SystemMessage("���� �Ʒ� ���� �� ����� ����(��ǻ��) ��  �־����ϴ�."));
	          	          }
	          	          pc.setsub(pc.getsub() + 1);  
	          	          pc.save();
	          	        htmlid = "hpass20";
	            } else {
	          	        htmlid = "";
	          	      }
	           } else if (s.equalsIgnoreCase("1")) {// LV:20
	        	    if (pc.getsub() == 1) {
	          	    if (pc.getInventory().checkItem(5440, 20)) { //������ ���� check
	          	        while (pc.getInventory().consumeItem(5440, 1)); //������ ���� delete
	          	      pc.getInventory().storeItem(5445, 1);//������ ���� �ָӴ�
	          	      pc.setsub(pc.getsub() + 1);
	          	      pc.save();
	          	       htmlid = "hpass9";
	           } else {
	          	       htmlid = "hpass20";
	          	      }
	           } else {
	          	       htmlid = "hpass20";
	          	      }
	           } else if (s.equalsIgnoreCase("2")) {// LV:25
	        	   if (pc.getsub() == 2) {
	          	    if (pc.getInventory().checkItem(5441, 20)) {// ������ �̻� check
	          	        while (pc.getInventory().consumeItem(5441, 1));//������ �̻� delete
	          	      pc.getInventory().storeItem(5445, 1); //������ ���� �ָӴ�
	          	      pc.setsub(pc.getsub() + 1);
	          	      pc.save();
	          	        htmlid = "hpass9";
	            } else {
	          	        htmlid = "hpass21";
	          	       }
	            } else {
	          	        htmlid = "hpass21";
	          	       }
	            } else if (s.equalsIgnoreCase("3")) {// LV:30
	            	 if (pc.getsub() == 3) {
	          	     if (pc.getInventory().checkItem(5442, 20)) { //�콼���� check
	          	         while (pc.getInventory().consumeItem(5442, 1));//�콼���� delete
	          	                pc.getInventory().storeItem(5445, 1); //������ ���� �ָӴ�
	          	          if (pc.isCrown()) { //����
	          	              //  pc.getInventory().storeItem(40228, 1); //������(�� Ŭ��)
	        					final int[] item_ids = { 40228 };
	        					final int[] item_amounts = { 1 };					
	        					L1ItemInstance item = null;
	        					for (int i = 0; i < item_ids.length; i++) {
	        						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
	        						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
	        				     }
	                      } else if (pc.isElf()) { //����
	          	              //  pc.getInventory().storeItem(40243, 1); //������ ���� (���� ���� ������Ż)
	        					final int[] item_ids = { 40243 };
	        					final int[] item_amounts = { 1 };					
	        					L1ItemInstance item = null;
	        					for (int i = 0; i < item_ids.length; i++) {
	        						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
	        						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
	        				     }
	                      } else if (pc.isDarkelf()) {
	          	             //   pc.getInventory().storeItem(40270, 1);//�������� ���� (���� �Ǽ����̼�)
	        					final int[] item_ids = { 40270 };
	        					final int[] item_amounts = { 1 };					
	        					L1ItemInstance item = null;
	        					for (int i = 0; i < item_ids.length; i++) {
	        						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
	        						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
	        				     }
	                      } else if (pc.isDragonknight()) {
	          	               // pc.getInventory().storeItem(439105, 1);//������ ����(���巯��Ʈ)
	        					final int[] item_ids = { 439105 };
	        					final int[] item_amounts = { 1 };					
	        					L1ItemInstance item = null;
	        					for (int i = 0; i < item_ids.length; i++) {
	        						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
	        						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
	        				  }
	                      } else if (pc.isIllusionist()) {
	          	              //  pc.getInventory().storeItem(439014, 1);//����� ����(ť��:��ũ)
	        					final int[] item_ids = { 439014 };
	        					final int[] item_amounts = { 1 };					
	        					L1ItemInstance item = null;
	        					for (int i = 0; i < item_ids.length; i++) {
	        						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
	        						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
	        				  }
	          	          }
	          	      pc.setsub(pc.getsub() + 1);
	          	      pc.save();
	          	      htmlid = "hpass9";
	            } else {
	          	      htmlid = "hpass22";
	          	     }
	            } else {
	          	      htmlid = "hpass22";
	          	     }
	            } else if (s.equalsIgnoreCase("4")) {// LV:35
	            	 if (pc.getsub() == 4) {
	          	     if (pc.getInventory().checkItem(5443, 20)) { //�콼 �尩 
	          	         while (pc.getInventory().consumeItem(5443, 1));//�콼 �尩 
	          	    //  pc.getInventory().storeItem(15445, 1);//������ ���� �ָӴ�
	          	  //    pc.getInventory().storeItem(520301, 1); //�������� ����� 
						final int[] item_ids = { 5445, 427129 };
						final int[] item_amounts = { 1, 1 };					
						L1ItemInstance item = null;
						for (int i = 0; i < item_ids.length; i++) {
							item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
							pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					  }
	          	      pc.setsub(pc.getsub() + 1);
	          	      pc.save();
	          	        htmlid = "hpass9";
	            } else {
	          	        htmlid = "hpass23";
	          	        }
	            } else {
	          	        htmlid = "hpass23";
	          	       }
	            } else if (s.equalsIgnoreCase("5")) {// LV:45
	            	 if (pc.getsub() == 6) {
	          	     if (pc.getInventory().checkItem(5444, 20)) { //�콼 ��ȭ 
	          	         while (pc.getInventory().consumeItem(5444, 1)); //�콼 ��ȭ 
	          	   //   pc.getInventory().storeItem(15446, 1);//���� ���� ��ǥ
	          	    //  pc.getInventory().storeItem(420010, 1); //���ž�� �Ͱ��� 
						final int[] item_ids = { 5446, 420010 };
						final int[] item_amounts = { 1, 1 };					
						L1ItemInstance item = null;
						for (int i = 0; i < item_ids.length; i++) {
							item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
							pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					  }
	          	      pc.setsub(pc.getsub() + 1);
	          	      pc.save();
	          	      htmlid = "hpass9";
	            } else {
	          	      htmlid = "hpass24";
	          	     }
	            } else {
	          	     htmlid = "hpass24";
	          	     }
	            } else if (s.equalsIgnoreCase("u")) {// Lv:40
	          	     if (pc.getLevel() >= 40 && pc.getsub() == 5) {
	          	    	//  pc.getInventory().storeItem(15445, 1);//������ ���� �ָӴ�
	  					final int[] item_ids = { 5445 };
	  					final int[] item_amounts = { 1 };					
	  					L1ItemInstance item = null;
	  					for (int i = 0; i < item_ids.length; i++) {
	  						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
	  						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
	  					  }
	          	          pc.setsub(pc.getsub() + 1);
	          	          pc.save();
	          	                htmlid = "hpass7";
	          	        } else {
	          	                htmlid = "hpass12";
	          	               }  
	                    } 
	              }				 
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return htmlid;
	}
	
}