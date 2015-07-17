/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
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
import java.util.Calendar;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.server.TimeController.HellDevilController;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;



public class C_NPCAction1 extends ClientBasePacket {

	private static final String C_NPC_ACTION1 = "[C] C_NPCAction1";	
	private static Random _random = new Random(System.nanoTime());


	public C_NPCAction1(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		int objid = readD();
		String s = readS();
		L1Object obj = L1World.getInstance().findObject(objid);
		
		if(s.equalsIgnoreCase("deadTrans") || s.equalsIgnoreCase("pvpSet") || s.equalsIgnoreCase("ShowHPMPRecovery") ||
				s.equalsIgnoreCase("showDisableEffectIcon") || s.equalsIgnoreCase("showDungeonTimeLimit") ){
			return;
		}


		int[] materials = null;
		int[] counts = null;
		int[] createitem = null;
		int[] createcount = null;

		String htmlid = null;
		String success_htmlid = null;
		String failure_htmlid = null;
		String[] htmldata = null;
		

		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}

		
	// �̴� ���� �ڸ�
		
	 if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4206003) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (pc.isInvisble()) {
			pc.sendPackets(new S_NpcChatPacket(npc,"�ڸ� ������ �ޱ� ���ؼ��� ������°� �ƴϾ�� �մϴ�.", 0));
			return;
		}
		if (s.equalsIgnoreCase("1")) {
			comaCheck(pc, 3, objid);
		} else if (s.equalsIgnoreCase("2")) {
			comaCheck(pc, 5, objid);
		} else if (s.equalsIgnoreCase("a")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 1);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("b")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 2);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("c")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 3);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("d")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 4);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("e")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 5);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("f")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 1);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("g")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 2);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("h")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 3);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("i")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 4);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("j")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 5);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("k")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 1);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("l")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 2);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("m")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 3);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("n")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 4);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("o")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 5);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("p")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 1);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("q")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 2);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("s")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 3);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("t")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 4);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("u")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 5);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("v")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 1);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("w")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 2);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("x")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 3);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("y")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 4);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("z")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 5);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("3")) {
			resetPiece(pc);
			selectComa(pc, objid);
		} else if (s.equalsIgnoreCase("4")) {
			giveComaBuff(pc, objid);
		}
		
		
	 } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50025  
		      || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50032 
		      || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50048 
		      || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50058 ){
		             if (s.equalsIgnoreCase("EnterSeller")) { // ����: ������ ã�´�
		          String s2 = readS(); 
		       L1PcInstance targetShop = L1World.getInstance().getPlayer(s2);
		       if (targetShop == null) {
		        pc.sendPackets(new S_ServerMessage(73, s2));
		       }
		        if (!targetShop.isPrivateShop()) {
		        pc.sendPackets(new S_SystemMessage(targetShop.getName()+"���� ������ ���� ���� �ʽ��ϴ�."));
		        }
		          if (pc.getMapId() != targetShop.getMapId()) {
		        pc.sendPackets(new S_SystemMessage(targetShop.getName()+"���� �� ����ȿ� ���� �����Դϴ�."));
		       } else{ 
		       L1Teleport.teleportToTargetFront(pc, targetShop, 1);
		      }
		     }
	

		  	} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 94250) {// ������
		    	   if (s.equals("A")){ // 76����
		    	    if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT76)){
		    	     pc.sendPackets(new S_ServerMessage(3255)); // �ش� ������ �̹� Ȯ�� �Ǿ����ϴ�.
		    	    } else {
		    	     if (pc.getInventory().checkItem(40308, 10000000) && pc.getLevel() >=76){
		    	      pc.getInventory().consumeItem(40308, 10000000);
		    	      pc.getQuest().set_end(L1Quest.QUEST_SLOT76);
		    	      pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		    	     } else {
		    	      pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot6"));
		    	     }
		    	    }
		    	   } else if (s.equals("B")){ // 81����
		    	    if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT81)){
		    	     pc.sendPackets(new S_ServerMessage(3255)); // �ش� ������ �̹� Ȯ�� �Ǿ����ϴ�.
		    	    } else {
		    	     if (pc.getInventory().checkItem(40308, 30000000) && pc.getLevel() >= 81){
		    	      pc.getInventory().consumeItem(40308, 30000000);
		    	      pc.getQuest().set_end(L1Quest.QUEST_SLOT81);
		    	      pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		    	     } else {
		    	      pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot6"));
		    	     }
		    	    }
		    	   }
		    
            
		             
		             
		
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71126) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("0")) { // ��Ÿ�ٵ�� �����ּ���
				RealTime time = RealTimeClock.getInstance().getRealTime();
				int entertime = pc.getLdungeonTime() % 1000;
				int enterday = pc.getLdungeonTime() / 1000;
				int dayofyear = time.get(Calendar.DAY_OF_YEAR);
				if (entertime > 180 && enterday == dayofyear) {
					pc.sendPackets(new S_NpcChatPacket(npc,	"��Ÿ�ٵ� ��� �ð��� ���� ���� �ʽ��ϴ�.", 0));
					htmlid = "";
					return;
				} else {
					if (enterday < dayofyear)
						pc.setLdungeonTime(dayofyear * 1000);
					L1Teleport.teleport(pc, 32728, 32848, (short) 451, 5, true); // ��Ÿ�ٵ� ���� 1����ȸ������ �ڷ���Ʈ, ��ġ ����
					int a = entertime % 60;
					if (a == 0) {
						int b = (180 - entertime) / 60;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + b + "�ð� ���ҽ��ϴ�."));// �ð�
					} else if ((180 - entertime) < 60) {
						int c = 180 - entertime;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + c + "�� ���ҽ��ϴ�."));// ��
					}
				}
			} else if (s.equalsIgnoreCase("1")) { // ��Ÿ�ٵ�� �����ּ���
					RealTime time = RealTimeClock.getInstance().getRealTime();
					int entertime = pc.getLdungeonTime() % 1000;
					int enterday = pc.getLdungeonTime() / 1000;
					int dayofyear = time.get(Calendar.DAY_OF_YEAR);
					if (entertime > 180 && enterday == dayofyear) {
						pc.sendPackets(new S_NpcChatPacket(npc,	"��Ÿ�ٵ� ��� �ð��� ���� ���� �ʽ��ϴ�.", 0));
						htmlid = "";
						return;
					} else {
						if (enterday < dayofyear)
							pc.setLdungeonTime(dayofyear * 1000);
						L1Teleport.teleport(pc, 32675, 32836, (short) 475, 5, true); // ��Ÿ�ٵ� ���� 1����ȸ������ �ڷ���Ʈ, ��ġ ����
						int a = entertime % 60;
						if (a == 0) {
							int b = (180 - entertime) / 60;
							pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + b + "�ð� ���ҽ��ϴ�."));// �ð�
						} else if ((180 - entertime) < 60) {
							int c = 180 - entertime;
							pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + c + "�� ���ҽ��ϴ�."));// ��
						}
					}
			} else if (s.equalsIgnoreCase("2")) { // ��Ÿ�ٵ�� �����ּ���
				RealTime time = RealTimeClock.getInstance().getRealTime();
				int entertime = pc.getLdungeonTime() % 1000;
				int enterday = pc.getLdungeonTime() / 1000;
				int dayofyear = time.get(Calendar.DAY_OF_YEAR);
				if (entertime > 180 && enterday == dayofyear) {
					pc.sendPackets(new S_NpcChatPacket(npc,	"��Ÿ�ٵ� ��� �ð��� ���� ���� �ʽ��ϴ�.", 0));
					htmlid = "";
					return;
				} else {
					if (enterday < dayofyear)
						pc.setLdungeonTime(dayofyear * 1000);
					L1Teleport.teleport(pc, 32684, 32833, (short) 462, 5, true); // ��Ÿ�ٵ� ���� 1����ȸ������ �ڷ���Ʈ, ��ġ ����
					int a = entertime % 60;
					if (a == 0) {
						int b = (180 - entertime) / 60;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + b + "�ð� ���ҽ��ϴ�."));// �ð�
					} else if ((180 - entertime) < 60) {
						int c = 180 - entertime;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + c + "�� ���ҽ��ϴ�."));// ��
					}
				}
			} else if (s.equalsIgnoreCase("3")) { // ��Ÿ�ٵ�� �����ּ���
				RealTime time = RealTimeClock.getInstance().getRealTime();
				int entertime = pc.getLdungeonTime() % 1000;
				int enterday = pc.getLdungeonTime() / 1000;
				int dayofyear = time.get(Calendar.DAY_OF_YEAR);
				if (entertime > 180 && enterday == dayofyear) {
					pc.sendPackets(new S_NpcChatPacket(npc,	"��Ÿ�ٵ� ��� �ð��� ���� ���� �ʽ��ϴ�.", 0));
					htmlid = "";
					return;
				} else {
					if (enterday < dayofyear)
						pc.setLdungeonTime(dayofyear * 1000);
					L1Teleport.teleport(pc, 32725, 32727, (short) 453, 5, true); // ��Ÿ�ٵ� ���� 1����ȸ������ �ڷ���Ʈ, ��ġ ����
					int a = entertime % 60;
					if (a == 0) {
						int b = (180 - entertime) / 60;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + b + "�ð� ���ҽ��ϴ�."));// �ð�
					} else if ((180 - entertime) < 60) {
						int c = 180 - entertime;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + c + "�� ���ҽ��ϴ�."));// ��
					}
				}
			} else if (s.equalsIgnoreCase("4")) { // ��Ÿ�ٵ�� �����ּ���
				RealTime time = RealTimeClock.getInstance().getRealTime();
				int entertime = pc.getLdungeonTime() % 1000;
				int enterday = pc.getLdungeonTime() / 1000;
				int dayofyear = time.get(Calendar.DAY_OF_YEAR);
				if (entertime > 180 && enterday == dayofyear) {
					pc.sendPackets(new S_NpcChatPacket(npc,	"��Ÿ�ٵ� ��� �ð��� ���� ���� �ʽ��ϴ�.", 0));
					htmlid = "";
					return;
				} else {
					if (enterday < dayofyear)
						pc.setLdungeonTime(dayofyear * 1000);
	
					L1Teleport.teleport(pc, 32786, 32814, (short) 492, 5, true); // ��Ÿ�ٵ� ���� 1����ȸ������ �ڷ���Ʈ, ��ġ ����
					int a = entertime % 60;
					if (a == 0) {
						int b = (180 - entertime) / 60;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + b + "�ð� ���ҽ��ϴ�."));// �ð�
					} else if ((180 - entertime) < 60) {
						int c = 180 - entertime;
						pc.sendPackets(new S_SystemMessage("��Ÿ�ٵ� ��� �ð��� " + c + "�� ���ҽ��ϴ�."));// ��
					}
				}
				
				
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(41007, 1)) {
					htmlid = "eris10";
				} else {
					L1ItemInstance item = pc.getInventory().storeItem(41007, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "eris6";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(41009, 1)) {
					htmlid = "eris10";
				} else {
					L1ItemInstance item = pc.getInventory().storeItem(41009, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "eris8";
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(41007, 1)) {
					if (pc.getInventory().checkItem(40969, 20)) {
						htmlid = "eris18";
						materials = new int[] { 40969, 41007 };
						counts = new int[] { 20, 1 };
						createitem = new int[] { 41008 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "eris5";
					}
				} else {
					htmlid = "eris2";
				}
			} else if (s.equalsIgnoreCase("E")) {
				if (pc.getInventory().checkItem(41010, 1)) {
					htmlid = "eris19";
				} else {
					htmlid = "eris7";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(41010, 1)) {
					htmlid = "eris19";
				} else {
					if (pc.getInventory().checkItem(41009, 1)) {
						if (pc.getInventory().checkItem(40959, 1)) {
							htmlid = "eris17";
							materials = new int[] { 40959, 41009 };
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40960, 1)) {
							htmlid = "eris16";
							materials = new int[] { 40960, 41009 };
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40961, 1)) {
							htmlid = "eris15";
							materials = new int[] { 40961, 41009 };
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40962, 1)) {
							htmlid = "eris14";
							materials = new int[] { 40962, 41009 };
							counts = new int[] { 1, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40635, 10)) {
							htmlid = "eris12";
							materials = new int[] { 40635, 41009 };
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40638, 10)) {
							htmlid = "eris11";
							materials = new int[] { 40638, 41009 };
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40642, 10)) {
							htmlid = "eris13";
							materials = new int[] { 40642, 41009 };
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else if (pc.getInventory().checkItem(40667, 10)) {
							htmlid = "eris13";
							materials = new int[] { 40667, 41009 };
							counts = new int[] { 10, 1 };
							createitem = new int[] { 41010 };
							createcount = new int[] { 1 };
						} else {
							htmlid = "eris8";
						}
					} else {
						htmlid = "eris7";
					}
				}
			}
			
			
			//���������� ī�ʽ�
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9425) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32850, 32854, (short)537, 5, true);
				return;
			} else if (s.equalsIgnoreCase("b")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32813, 32870, (short)537, 5, true);
				return;
			}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9422) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32617, 33213, (short)4, 5, true);
				return;
			}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9423) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32621, 33201, (short)4, 5, true);
				return;
			}
			
			
			
			
			
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9421) {/// ���ھ�
			pc.sendPackets(new S_SystemMessage("\\fY+8����� �Ƶ���(2õ) �ʿ��մϴ�."));
			pc.sendPackets(new S_SystemMessage("\\fY+9����� �Ƶ���(3õ) �ʿ��մϴ�."));
		      if (s.equals("A")){ // +7 ������ Ű��ũ   
		      if (pc.getInventory().MakeCheckEnchant(410003, 8)//������üũ
		        && pc.getInventory().checkItem(40308, 20000000)) {
		       pc.getInventory().MakeDeleteEnchant(410003, 8);
		       pc.getInventory().consumeItem(40308, 20000000);
		       createNewItem2(pc, 423, 1, 7);
		       htmlid = "vjaya05";
		      } else {
		       htmlid = "vjaya04";
		      }
		     }
		      if (s.equals("B")){ // +8 ������ Ű��ũ
		       if (pc.getInventory().MakeCheckEnchant(410003, 9)
		         && pc.getInventory().checkItem(40308, 30000000)) {
		        pc.getInventory().MakeDeleteEnchant(410003, 9);
		        pc.getInventory().consumeItem(40308, 30000000);
		        createNewItem2(pc, 423, 1, 8);
		        htmlid = "vjaya05";
		       } else {
		        htmlid = "vjaya04";
		       }
		      }
		      if (s.equals("C")){ // +7 ������ Ű��ũ
		      if (pc.getInventory().MakeCheckEnchant(410004, 8)
		        && pc.getInventory().checkItem(40308, 20000000)) {
		       pc.getInventory().MakeDeleteEnchant(410004, 8);
		       pc.getInventory().consumeItem(40308, 20000000);
		       createNewItem2(pc, 423, 1, 7);
		       htmlid = "vjaya05";
		      } else {
		       htmlid = "vjaya04";
		      }
		     }
		      if (s.equals("D")){ // +8 ������ Ű��ũ
		       if (pc.getInventory().MakeCheckEnchant(410004, 9)
		         && pc.getInventory().checkItem(40308, 30000000)) {
		        pc.getInventory().MakeDeleteEnchant(410004, 9);
		        pc.getInventory().consumeItem(40308, 30000000);
		        createNewItem2(pc, 423, 1, 8);
		        htmlid = "vjaya05";
		       } else {
		        htmlid = "vjaya04";
		       }
		      }


		
		
	
	 } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 42120113) {//��� ������
	        if (s.equalsIgnoreCase("1") && pc.getLevel() > 45 && pc.getLevel() < 90) { //66����
				RealTime time = RealTimeClock.getInstance().getRealTime();
				int entertime = pc.getDdungeonTime() % 1000;
				int enterday = pc.getDdungeonTime() / 1000;
				int dayofyear = time.get(Calendar.DAY_OF_YEAR);
				if(entertime > 120 && enterday == dayofyear){
					pc.sendPackets(new S_ServerMessage(1522, "2"));// 3�ð� ��� ����ߴ�.
					htmlid ="";
					return;
				} else {
					if(enterday < dayofyear)pc.setDdungeonTime(dayofyear * 1000);
					L1Teleport.teleport(pc, 32740, 32777, (short) 30, 6, true);
					 pc.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIMER, (120 - entertime)*60));
						//pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "ǥ����� �Ĺ������γ������� ������ ���ϰų�(����)�װų�(�۵��,��ġ���)�븻������ �����"));
						//  pc.sendPackets(new S_SystemMessage("ǥ����� �Ĺ������γ������� ������ ���ϰų�(����)�װų�(�۵��,��ġ���)�븻������ �����"));
					int a = entertime % 60;
					if(a == 0){
						int b = (120 - entertime) / 60;
						pc.sendPackets(new S_ServerMessage(1526, ""+b+""));// b �ð� ���Ҵ�.
					} else if ((120 - entertime) < 60){
						int c = 120 - entertime;
						pc.sendPackets(new S_ServerMessage(1527, ""+c+""));// �� ���Ҵ�.
					}
				}
			} else {
				htmlid="dvdgate2";
			}
	

	// �ų� ������ (����)
	} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4213001) {
	if (s.equalsIgnoreCase("0")) { // 1��
		Yuris(pc, objid, 1, 3000);
	} else if (s.equalsIgnoreCase("1")) { // 3��
		Yuris(pc, objid, 3, 9000);
	} else if (s.equalsIgnoreCase("2")) { // 5��
		Yuris(pc, objid, 5, 15000);
	} else if (s.equalsIgnoreCase("3")) { // 10��
		Yuris(pc, objid, 10, 30000);
	}
	}
	
	}
		
	
	
	
		// �ų� ������
			private void Yuris(L1PcInstance pc, int objid, int count, int lawful) {
				if (pc.getInventory().checkItem(L1ItemId.REDEMPTION_BIBLE, count)) {
					pc.getInventory().consumeItem(L1ItemId.REDEMPTION_BIBLE, count);
					pc.addLawful(lawful);
					pc.sendPackets(new S_Lawful(pc.getId(), pc.getLawful()));
					pc.sendPackets(new S_NPCTalkReturn(objid, "yuris2"));
				} else {
					pc.sendPackets(new S_NPCTalkReturn(objid, "yuris3"));
				}
			}
			
			private void selectComa(L1PcInstance pc, int objid) {
				String[] htmldata = new String[] {
						String.valueOf(pc.getDeathMatchPiece()),
						String.valueOf(pc.getGhostHousePiece()),
						String.valueOf(pc.getPetRacePiece()),
						String.valueOf(pc.getPetMatchPiece()),
						String.valueOf(pc.getUltimateBattlePiece()) };
				pc.sendPackets(new S_NPCTalkReturn(objid, "coma5", htmldata));
			}
			
			private void comaCheck(L1PcInstance pc, int type, int objid) {
				FastTable<Integer> list = new FastTable<Integer>();
				if (pc.getInventory().checkItem(435010, 1)) {
					list.add(435010);
				}
				if (pc.getInventory().checkItem(435009, 1)) {
					list.add(435009);
				}
				if (pc.getInventory().checkItem(435011, 1)) {
					list.add(435011);
				}
				if (pc.getInventory().checkItem(435012, 1)) {
					list.add(435012);
				}
				if (pc.getInventory().checkItem(435013, 1)) {
					list.add(435013);
				}
				if (list.size() >= type) {
					for (int i = 0; i < type; i++) {
						pc.getInventory().consumeItem(list.get(i), 1);
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.STATUS_COMA_3)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.STATUS_COMA_5)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(
								L1SkillId.STATUS_COMA_3);
						pc.getSkillEffectTimerSet().removeSkillEffect(
								L1SkillId.STATUS_COMA_5);
					}
					if (type == 3) {
						new L1SkillUse().handleCommands(pc, L1SkillId.STATUS_COMA_3, pc
								.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_SPELLSC);
					} else if (type == 5) {
						new L1SkillUse().handleCommands(pc, L1SkillId.STATUS_COMA_5, pc
								.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_SPELLSC);
					}
					pc.sendPackets(new S_NPCTalkReturn(objid, ""));
				} else {
					pc.sendPackets(new S_NPCTalkReturn(objid, "coma3"));
				}
				list.clear();
			}
				
				private void resetPiece(L1PcInstance pc) {
					pc.setDeathMatchPiece(0);
					pc.setGhostHousePiece(0);
					pc.setPetRacePiece(0);
					pc.setPetMatchPiece(0);
					pc.setUltimateBattlePiece(0);
				}
				
				private void giveComaBuff(L1PcInstance pc, int objid) {
					int amount = pc.getUltimateBattlePiece() + pc.getDeathMatchPiece()
							+ pc.getGhostHousePiece() + pc.getPetRacePiece()
							+ pc.getPetMatchPiece();
					if (amount < 3 || amount == 4) {
						pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_3"));
					} else if (amount == 3) {
						if (isComaBuff(pc)) {
							consumePiece(pc);
							new L1SkillUse().handleCommands(pc, L1SkillId.STATUS_COMA_3, pc
									.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							pc.sendPackets(new S_NPCTalkReturn(objid, ""));
						} else {
							pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_2"));
						}
					} else if (amount == 5) {
						if (isComaBuff(pc)) {
							consumePiece(pc);
							new L1SkillUse().handleCommands(pc, L1SkillId.STATUS_COMA_5, pc
									.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							pc.sendPackets(new S_NPCTalkReturn(objid, ""));
						} else {
							pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_2"));
						}
					} else if (amount > 5) {
						pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_1"));
					}
					resetPiece(pc);
				}
				 private boolean createNewItem2(L1PcInstance pc, int item_id, int count,
						   int EnchantLevel) {
						  L1ItemInstance item = ItemTable.getInstance().createItem(item_id);

						  item.setCount(count);
						  item.setEnchantLevel(EnchantLevel);
						  item.setIdentified(true);
						  if (item != null) {
						   if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						    pc.getInventory().storeItem(item);
						   } else { // ���� �� ���� ���� ���鿡 ����߸��� ó���� ĵ���� ���� �ʴ´�(���� ����)
						    L1World.getInstance().getInventory(pc.getX(), pc.getY(),
						      pc.getMapId()).storeItem(item);
						   }
						   pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0��
						                   // �տ�
						                   // �־����ϴ�.
						   return true;
						  } else {
						   return false;
						  }
						 }



				
				
				private boolean isComaBuff(L1PcInstance pc) {
					if (pc.getInventory().checkItem(435010, pc.getUltimateBattlePiece())
							&& pc.getInventory().checkItem(435009, pc.getDeathMatchPiece())
							&& pc.getInventory().checkItem(435011, pc.getGhostHousePiece())
							&& pc.getInventory().checkItem(435012, pc.getPetRacePiece())
							&& pc.getInventory().checkItem(435013, pc.getPetMatchPiece())) {
						return true;
					}
					return false;
				}
				
				private void consumePiece(L1PcInstance pc) {
					pc.getInventory().consumeItem(435010, pc.getUltimateBattlePiece());// ����
					pc.getInventory().consumeItem(435009, pc.getDeathMatchPiece()); // ������ġ
					pc.getInventory().consumeItem(435011, pc.getGhostHousePiece());// ������
					pc.getInventory().consumeItem(435012, pc.getPetRacePiece());// �극�̽�
					pc.getInventory().consumeItem(435013, pc.getPetMatchPiece());// ���ġ
				}

				
		
		@Override
		public String getType() {
			return C_NPC_ACTION1;
		}

	}
