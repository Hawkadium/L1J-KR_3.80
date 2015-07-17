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

package l1j.server.server.clientpackets;

import java.util.Random;
import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.AutoCheckTimer;
import l1j.server.server.GMCommands;
import l1j.server.server.Opcodes;
import l1j.server.server.UserCommands;
import l1j.server.server.TimeController.WitsTimeController;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.CodeLogger;
import server.LineageClient;
import server.manager.eva;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

// chat opecode type
// ��� 0x44 0x00
// ����(! ) 0x44 0x00
// �ӻ���(") 0x56 charname
// ��ü(&) 0x72 0x03
// Ʈ���̵�($) 0x44 0x00
// PT(#) 0x44 0x0b
// ����(@) 0x44 0x04
// ����(%) 0x44 0x0d
// CPT(*) 0x44 0x0e
// �������� (+)
public class C_Chat extends ClientBasePacket {

	private static final String C_CHAT = "[C] C_Chat";

	public C_Chat(byte abyte0[], LineageClient clientthread) {
		super(abyte0);
try{
		L1PcInstance pc = clientthread.getActiveChar();
		
		int chatType = readC();
		String chatText = readS();
		
		if(pc == null || clientthread.getActiveChar() == null){ // Null ó��
			   clientthread.kick();
			   return;
			  }
		 if(L1World.getInstance().getPlayer(clientthread.getActiveChar().getName()) == null){ // ���� ������Ʈ���� ã�� �� ���ٸ�
			   try {
			    System.out.println("����Null : " + clientthread.getActiveChar().getName() + " / " + clientthread.getIp());
			   } catch (Exception e) {
			   }
			   clientthread.kick();
			   return;
			  }
			if (pc.getMapId() == 631 && !pc.isGm()) {
				pc.sendPackets(new S_ServerMessage(912)); // ä���� �� �� �����ϴ�.
				return;
			}
		 if(pc.getMapId() == 5153){//��Ʋ�� ���� ���γ��� ä��
				if(!pc.isGm()){
			   for (L1PcInstance battlemember : L1World.getInstance().getRecognizePlayer(pc)) {
						S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_OPCODE_NORMALCHAT, 0);
						if(pc.get_DuelLine() == battlemember.get_DuelLine()){
							if (!battlemember.getExcludingList().contains(pc.getName())) {
								pc.sendPackets(s_chatpacket);
								battlemember.sendPackets(s_chatpacket);
								return;
						}
					}
				}
		     }
		 }

		int a_code = AutoCheckTimer.aCode;

		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SILENCE)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.AREA_OF_SILENCE)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.STATUS_POISON_SILENCE)) {
			return;
		}
		if (pc.getSkillEffectTimerSet().hasSkillEffect(
				L1SkillId.STATUS_CHAT_PROHIBITED)) { // ä��
			// ������
			pc.sendPackets(new S_ServerMessage(242)); // ���� ä�� �������Դϴ�.
			return;
		}

		if (pc.isDeathMatch() && !pc.isGhost()) {
			pc.sendPackets(new S_ServerMessage(912)); // ä���� �� �� �����ϴ�.
			return;
		}

		switch (chatType) {
		case 0: 
			if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
				return;
			}

			// �ڵ����
			if (pc.����Ȯ�����ʿ��ѻ��������Լ�()) {// pc�� L1PcInstance�� ȣ���ϴ� ��ü, �� �׳�
				// L1PcInstanceŬ������
				int CompareCode = Integer.parseInt(chatText);
				if (a_code == CompareCode) {
					pc.����Ȯ�κ��ʿ���·ιٲٱ�();
					pc
							.sendPackets(new S_SystemMessage(
									"���� ��������� �ƴѰ� �����Ǿ����ϴ�."));
				} else if (a_code != CompareCode) {// pc�� L1PcInstance�� ȣ���ϴ�
													// ��ü, ��
					// �׳� L1PcInstanceŬ������
					pc.sendPackets(new S_SystemMessage("���� �ڵ� �� �߸� �Է� �Ͽ����ϴ�."));// (2)
					pc.sendPackets(new S_SystemMessage("���� �ڵ�� : " + a_code
							+ " �Դϴ�."));// (3)
				}
			}
			// / �ڵ����

			// GMĿ���
			if (chatText.startsWith(".")
					&& (pc.getAccessLevel() == Config.GMCODE || pc
							.getAccessLevel() == 1)) {
				String cmd = chatText.substring(1);
				GMCommands.getInstance().handleCommands(pc, cmd);
				return;
			}
			if (chatText.startsWith("$")) {
				String text = chatText.substring(1);
				chatWorld(pc, text, 12);
				if (!pc.isGm()) {
					pc.checkChatInterval();
				}
				return;
			}
			if (chatText.startsWith(".")) { // �����ڸ�Ʈ
				String cmd = chatText.substring(1);

				UserCommands.getInstance().handleCommands(pc, cmd);
				return;
			}

			if (chatText.startsWith("$")) { // ����ä��
				String text = chatText.substring(1);

				chatWorld(pc, text, 12);
				if (!pc.isGm()) {
					pc.checkChatInterval();
				}
				return;
			}


			S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
					Opcodes.S_OPCODE_NORMALCHAT, 0);
			if (!pc.getExcludingList().contains(pc.getName())) {
				pc.sendPackets(s_chatpacket);
			}
			for (L1PcInstance listner : L1World.getInstance()
					.getRecognizePlayer(pc)) {
				if (!listner.getExcludingList().contains(pc.getName())) {
					listner.sendPackets(s_chatpacket);
				}
			}
			

			// ���� ó��
			L1MonsterInstance mob = null;
			for (L1Object obj : pc.getNearObjects().getKnownObjects()) {
				if (obj instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) obj;
					if (mob.getNpcTemplate().is_doppel()
							&& mob.getName().equals(pc.getName())) {
						Broadcaster.broadcastPacket(mob, new S_NpcChatPacket(
								mob, chatText, 0));
					}
				}
			}
			eva.LogChatNormalAppend("[�Ϲ�]", pc.getName(), chatText);
			break;
		case 2: 
			if (pc.isGhost()) {
				return;
			}
			S_ChatPacket s_chatpacket2 = new S_ChatPacket(pc, chatText,
					Opcodes.S_OPCODE_NORMALCHAT, 2);
			if (!pc.getExcludingList().contains(pc.getName())) {
				pc.sendPackets(s_chatpacket2);
			}
			for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(
					pc, 50)) {
				if (!listner.getExcludingList().contains(pc.getName())) {
					listner.sendPackets(s_chatpacket2);
				}
			}
			eva.LogChatNormalAppend("[�Ϲ�]", pc.getName(), chatText);
			// ���� ó��
			for (L1Object obj : pc.getNearObjects().getKnownObjects()) {
				if (obj instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) obj;
					if (mob.getNpcTemplate().is_doppel()
							&& mob.getName().equals(pc.getName())) {
						for (L1PcInstance listner : L1World.getInstance()
								.getVisiblePlayer(mob, 50)) {
							listner.sendPackets(new S_NpcChatPacket(mob,
									chatText, 2));
						}
					}
				}
			}
		
			break;
		case 3: 
			chatWorld(pc, chatText, chatType);
			CodeLogger.getInstance().chatlog("&&",pc.getName() + "	" + chatText);
		//	eva.writeMessage(2, pc.getName() + ":" + chatText);
			String temp = "";
			StringTokenizer values = new StringTokenizer(chatText, " ");
			while (values.hasMoreElements()) {// ��������
				temp += values.nextToken();
			}
			String f[] = { "�ù�", "����", "����", "���߳�", "â��", "����", "�ֹ�", "�ֺ�",
					"��.��", "�ư���", "���", "���", "����", "sex", "��s", "����", "����",
					"����", "����", "����", "����", "fuck", "��.ģ", "��.��", "�Ϲ�", "��.��",
					"��.��", "����", "��.��", "����", "����", "���ŷ�", "��Ƽ��", "��.��", "�ú�",
					"��", "��.��", "����", "��.��", "����", "����", "����", "��ġ", "�ù�",
					"����" };
			for (int i = 0; i < f.length; i++) {
				if (temp.indexOf(f[i]) > -1 && !pc.isGm()
						&& pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
					// ����̰ų� ��üä�÷� �ȵǸ� ����
					int time = 10; // ä�ݽð����� �д���
					pc.getSkillEffectTimerSet().setSkillEffect(1005,
							time * 60 * 1000);
					pc.sendPackets(new S_SkillIconGFX(36, time * 60));
					pc.sendPackets(new S_ServerMessage(242)); // ���� ä�� �������Դϴ�.
					pc.sendPackets(new S_SystemMessage("��Ģ�� ������� " + time
							+ "�� ä�ñ����Դϴ�"));

				}
			}
			if(pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL){
				if (WitsTimeController.witsGameStarted) {
					try {
						 if (WitsTimeController._witsWinPlayers.containsKey(pc.getName())) {
							 pc.sendPackets(new S_SystemMessage("\\fV" + pc.getName() + "���� ���� �����ǿ� �����ϼ̽��ϴ�."));
							 return;
						 }					 
						 
						 int count = 0;						
						 try {
							 count = Integer.parseInt(temp);
						 } catch (Exception e) {
							pc.sendPackets(new S_SystemMessage("\\fV���ڸ� �Է��ϼ���."));
							return;
						 }
						 int witsCount = ++WitsTimeController.witsCount;
						 
						 for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
							 if (player.isGm()) {
								 player.sendPackets(new S_SystemMessage("\\fU" + pc.getName() + "���� �Է� : " + count));
									player.sendPackets(new S_SystemMessage("\\fU���� ī���� : " + witsCount));
							 }
						 }
						 
						 if (count >= witsCount && witsCount <= 5) {
							 WitsTimeController._witsWinPlayers.put(pc.getName(), pc);
						 } else {
							 WitsTimeController.witsGameStarted = false;
							 WitsTimeController.witsCount = 0;
							 // ��ǰ�� ����
							 String[] itemId = Config.ALT_WITS_PRESENT.split(",");
							 // ��ǰ�� ����
							 String[] itemCount = Config.ALT_WITS_PRESENT_COUNT.split(",");
							 					   
							 for (L1PcInstance player : WitsTimeController.getInstance().getWitsWinPlayers()) {
								 try {
									 if (player == null || player.getNetConnection() == null) {
											continue;
									 }
									 Random _random = new Random();
									 int rnd = _random.nextInt(5);//0~5
									 pc.getInventory().storeItem(Integer.parseInt(itemId[rnd]), Integer.parseInt(itemCount[rnd])); // ��ǰ�� ������ ����

									 L1ItemInstance item = ItemTable.getInstance().createItem(Integer.parseInt(itemId[rnd]));
								   
									 player.sendPackets(new S_SystemMessage("\\fV" + item.getName() + "�� " + itemCount[rnd] + "�� ������ϴ�."));	
									 
									  for (L1PcInstance gm : L1World.getInstance().getAllPlayers()) {
											if (gm.isGm()) {
											 gm.sendPackets(new S_SystemMessage(player.getName() + "�Բ� ��ǰ : " + item.getName() + "��(��) " + itemCount[rnd] + "�� �����Ͽ����ϴ�."));
										 }
									 }
									 
									 WitsTimeController._witsWinPlayers.remove(player.getName());
								 } catch (Exception e) { }
							 }
							 
							 L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fY��ġ ������ ����Ǿ����ϴ�." ));
							 
							 if (WitsTimeController.witsGameCount == 0) {
								 WitsTimeController.getInstance().stopcheckChatTime();
							 }
						 }
					} catch (Exception e) { }				 
				}  
			}
			break;
		case 4: 
			if (pc.getClanid() != 0) { // ũ�� �Ҽ���
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				int rank = pc.getClanRank();
				if (clan != null&& (rank == L1Clan.CLAN_RANK_PROBATION || rank == L1Clan.CLAN_RANK_PUBLIC|| rank == L1Clan.CLAN_RANK_GUARDIAN 
								|| rank == L1Clan.CLAN_RANK_SUB_PRINCE || rank == L1Clan.CLAN_RANK_PRINCE )) {

					S_ChatPacket s_chatpacket4 = new S_ChatPacket(pc, chatText,
							Opcodes.S_OPCODE_MSG, 4);

						CodeLogger.getInstance().chatlog("@@", pc.getName()+"["+pc.getClanname()+"]: "+chatText);

						eva.LogChatClanAppend("[����]", pc.getName(), pc.getClanname(), chatText);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket4);
						}
					}
				}
			}
		
			break;
		case 11: 
			if (pc.isInParty()) { // ��Ƽ��

				S_ChatPacket s_chatpacket11 = new S_ChatPacket(pc, chatText,
						Opcodes.S_OPCODE_MSG, 11);
				for (L1PcInstance listner : pc.getParty().getMembers()) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						listner.sendPackets(s_chatpacket11);
					}
				}

			}
			CodeLogger.getInstance().chatlog("##",
					pc.getName() + "	" + chatText);
			eva.LogChatPartyAppend("[��Ƽ]", pc.getName(), chatText);
		
			break;
		case 12: 
			chatWorld(pc, chatText, chatType);
		
			break;
		case 13:  // ���� ä��
			if (pc.getClanid() != 0) { // ���� �Ҽ���
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				int rank = pc.getClanRank();
				if (clan != null&& (rank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_PRINCE)) {
					S_ChatPacket s_chatpacket13 = new S_ChatPacket(pc, chatText,Opcodes.S_OPCODE_MSG, 13);// 13
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						int listnerRank = listner.getClanRank();
						if (!listner.getExcludingList().contains(pc.getName())
								&& (listnerRank == L1Clan.CLAN_RANK_GUARDIAN || listnerRank == L1Clan.CLAN_RANK_PRINCE)) {
							listner.sendPackets(s_chatpacket13);
						}
					}
				}
			}
			CodeLogger.getInstance().chatlog("%%",
					pc.getName() + "	" + chatText);
		
			eva.LogChatPartyAppend("[����]", pc.getName(), chatText);
		
			break;
		case 14:  // ä�� ��Ƽ
			if (pc.isInChatParty()) { // ä�� ��Ƽ��

				S_ChatPacket s_chatpacket14 = new S_ChatPacket(pc, chatText,
						Opcodes.S_OPCODE_NORMALCHAT, 14);
				for (L1PcInstance listner : pc.getChatParty().getMembers()) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						listner.sendPackets(s_chatpacket14);
					}
				}
			}
			CodeLogger.getInstance().chatlog("**",
					pc.getName() + "	" + chatText);
			eva.LogChatPartyAppend("[��Ƽ]", pc.getName(), chatText);
		
			break;
		case 15: 
			if (pc.getClanid() != 0) { // ���� �Ҽ���
				L1PcInstance allianceLeader = (L1PcInstance) L1World
						.getInstance().findObject(pc.getTempID());
				String TargetClanName = allianceLeader.getClanname();
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				L1Clan TargetClan = L1World.getInstance().getClan(
						TargetClanName);
				if (clan != null) {

					S_ChatPacket s_chatpacket15 = new S_ChatPacket(pc, chatText,
							Opcodes.S_OPCODE_NORMALCHAT, 15);
					// ������ �¶������� �ڱ��� ������ �¶������� ������ �������� ����־����. (����� ��ó��)
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						int AllianceClan = listner.getClanid();
						if (pc.getClanid() == AllianceClan) {
							listner.sendPackets(s_chatpacket15);
						}
					} // �ڱ����� ���ۿ�
					for (L1PcInstance alliancelistner : TargetClan.getOnlineClanMember()) {

						int AllianceClan = alliancelistner.getClanid();
						if (pc.getClanid() == AllianceClan) {
							alliancelistner.sendPackets(s_chatpacket15);
						}
					} // �������� ���ۿ�
				
				}
			}
			break;
		case 17:
			if (pc.getClanid() != 0) { // ���� �Ҽ���
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null
						&& (pc.isCrown() && pc.getId() == clan.getLeaderId())) {
					S_ChatPacket s_chatpacket17 = new S_ChatPacket(pc, chatText,Opcodes.S_OPCODE_MSG, 17);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket17);
							
							
							
							
	
							
							
						}
					}
				}
			}
			break;
		default:
			break;
		}
		if (!pc.isGm()) {
			pc.checkChatInterval();
		}
     }catch(Exception e){}
	}
	
	
	private void chatWorld(L1PcInstance pc, String chatText, int chatType) {
		if (pc.isGm() || pc.getAccessLevel() == 1) {			
			if (chatType == 3) {
				L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(pc, chatText, Opcodes.S_OPCODE_MSG, chatType));
				eva.LogChatAppend("[��ü]", pc.getName(), chatText);
			} else if (chatType == 12) {
				L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(pc, chatText, Opcodes.S_OPCODE_MSG, 3));
				eva.LogChatTradeAppend("[���]", pc.getName(), chatText);

			}			
		} else if (pc.isCrown() && pc.getClanid() != 0) { // ���ͱ��ִ� �����Ѿ��� ��â����
			if (L1World.getInstance().isWorldChatElabled()) {
				if (pc.get_food() >= 12) { // 5%
					pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
					for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							if (listner.isShowTradeChat() && chatType == 12) {
								listner.sendPackets(new S_ChatPacket(pc,chatText, Opcodes.S_OPCODE_MSG,chatType));
							} else if (listner.isShowWorldChat()&& chatType == 3) {
								listner.sendPackets(new S_ChatPacket(pc,chatText, Opcodes.S_OPCODE_MSG,chatType));
							}
						}
					}
				} else {
					pc.sendPackets(new S_ServerMessage(462));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(510));
			}
		} else if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
			if (L1World.getInstance().isWorldChatElabled()) {
				if (pc.get_food() >= 12) { // 5%
					if (chatType == 3) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));					
						eva.LogChatAppend("[��ü]", pc.getName(), chatText);
					} else if (chatType == 12) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						eva.LogChatTradeAppend("[���]", pc.getName(), chatText);
					}
					pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
					for (L1PcInstance listner : L1World.getInstance()
							.getAllPlayers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							if (listner.isShowTradeChat() && chatType == 12) {
								listner.sendPackets(new S_ChatPacket(pc,
										chatText, Opcodes.S_OPCODE_MSG,
										chatType));
							} else if (listner.isShowWorldChat()
									&& chatType == 3) {
								listner.sendPackets(new S_ChatPacket(pc,
										chatText, Opcodes.S_OPCODE_MSG,
										chatType));
							}
						}
					}
				} else {
					pc.sendPackets(new S_ServerMessage(462));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(510));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(195, String
					.valueOf(Config.GLOBAL_CHAT_LEVEL)));
		}
	}

	@Override
	public String getType() {
		return C_CHAT;
	}
}
