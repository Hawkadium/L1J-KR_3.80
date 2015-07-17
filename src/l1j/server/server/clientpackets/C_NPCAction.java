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

import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.GhostHouse;
import l1j.server.GameSystem.PetMatch;
import l1j.server.GameSystem.PetRacing;
import l1j.server.GameSystem.IceInstance.IceDemonController;
import l1j.server.GameSystem.IceInstance.IceQueenController;
import l1j.server.GameSystem.MiniGame.DeathMatch;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.TimeController.DevilController;
import l1j.server.server.TimeController.InDunController;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.CharSoldierTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.gametime.RealTime;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.npc.action.function.L1NpcActionHelper;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_AGShopSellList;
import l1j.server.server.serverpackets.S_ApplyAuction;
import l1j.server.server.serverpackets.S_AuctionBoardRead;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_DelSkill;
import l1j.server.server.serverpackets.S_Deposit;
import l1j.server.server.serverpackets.S_Drawal;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_HouseMap;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NoTaxShopSellList;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetList;
import l1j.server.server.serverpackets.S_PremiumShopSellList;
import l1j.server.server.serverpackets.S_RetrieveElfList;
import l1j.server.server.serverpackets.S_RetrieveList;
import l1j.server.server.serverpackets.S_RetrievePackageList;
import l1j.server.server.serverpackets.S_RetrievePledgeList;
import l1j.server.server.serverpackets.S_RetrieveSpecialList;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SelectTarget;
import l1j.server.server.serverpackets.S_SellHouse;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShopBuyList;
import l1j.server.server.serverpackets.S_ShopSellList;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SoldierBuyList;
import l1j.server.server.serverpackets.S_SoldierGiveList;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TaxRate;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1CharSoldier;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.templates.L1Town;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;
import server.system.autoshop.AutoShopManager;

public class C_NPCAction extends ClientBasePacket {

	private static final String C_NPC_ACTION = "[C] C_NPCAction";

	private static Logger _log = Logger.getLogger(C_NPCAction.class.getName());

	private static Random _random = new Random(System.nanoTime());

	public C_NPCAction(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		int objid = readD();
		String s = readS();
		if(s.equalsIgnoreCase("deadTrans") || s.equalsIgnoreCase("pvpSet") || s.equalsIgnoreCase("ShowHPMPRecovery") ||
			    s.equalsIgnoreCase("showDisableEffectIcon") || s.equalsIgnoreCase("showDungeonTimeLimit") ){
			   return;
			  }
		String s2 = null;
		if (s.equalsIgnoreCase("select") || s.equalsIgnoreCase("map")
				|| s.equalsIgnoreCase("apply")) {
			s2 = readS();
		} else if (s.equalsIgnoreCase("ent")) {
			L1Object obj = L1World.getInstance().findObject(objid);
			if (obj != null && obj instanceof L1NpcInstance) {
				final int PET_MATCH_MANAGER = 80088;
				if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == PET_MATCH_MANAGER) {
					s2 = readS();
				}
			}
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
		L1PcInstance target;
		L1Object obj = L1World.getInstance().findObject(objid);
		if (obj != null) {
			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				int difflocx = Math.abs(pc.getX() - npc.getX());
				int difflocy = Math.abs(pc.getY() - npc.getY());

				if (difflocx > 3 || difflocy > 3) {
					return;
				}
				npc.onFinalAction(pc, s);
			} else if (obj instanceof L1PcInstance) {
				target = (L1PcInstance) obj;
				if (s.matches("[0-9]+")) {
					if (target.isSummonMonster()) {
						summonMonster(target, s);
						target.setSummonMonster(false);
					}
				} else {
					if (target.isShapeChange()) {
						L1PolyMorph.handleCommands(target, s);
						target.setShapeChange(false);
					} else if (target.isArchShapeChange()) {
						int time;
						if (target.isArchPolyType() == true) {
							time = 1200;
						} else {
							time = -1;
						}
						L1PolyMorph.ArchPoly(target, s, time);
						target.setArchShapeChange(false);
					} else {
						L1PolyMorph poly = PolyTable.getInstance().getTemplate(
								s);
						if (poly != null || s.equals("none")) {
							if (target.getInventory().checkItem(40088)
									&& usePolyScroll(target, 40088, s)) {
							}
							if (target.getInventory().checkItem(40096)
									&& usePolyScroll(target, 40096, s)) {
							}
							if (target.getInventory().checkItem(140088)
									&& usePolyScroll(target, 140088, s)) {
							}
						}
					}
				}
				return;
			}
		} else {
		}
		L1NpcAction action = NpcActionTable.getInstance().get(s, pc, obj);
		if (action != null) {
			L1NpcHtml result = action.execute(s, pc, obj, readByte());
			if (result != null) {
				pc.sendPackets(new S_NPCTalkReturn(obj.getId(), result));
			}
			return;
		}

		if (s.equalsIgnoreCase("buy")) {
			if (pc.getInventory().getWeight240() == 240) {
				// \f1��� �ִ� ������ ���ſ� �ŷ��� �� �� �����ϴ�.
				pc.sendPackets(new S_ServerMessage(270));
				return;
			}
			int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (isNpcSellOnly(npc)) {
				return;
			}//������ ������ ����������.
			if (npcid == 4220000 || npcid == 4220001 || npcid == 4220002 || npcid == 97905 || npcid == 97904 || npcid == 97902
					|| npcid == 4220003 || npcid == 4220700 || npcid == 4200104 || npcid == 402031 || npcid == 880587 || npcid == 880595
					|| npcid == 5000401) {
				pc.sendPackets(new S_PremiumShopSellList(objid));
				return;
			}
			if (npcid == 70068 || npcid == 70020 || npcid == 70056 || npcid == 880587 || npcid == 880595
					|| npcid == 70051 || npcid == 70055 || npcid == 4213002
					|| npcid == 70017 || npcid == 4200105) {
				pc.sendPackets(new S_NoTaxShopSellList(objid));
				return;
			}
			if (npcid == 4208001) {
				pc.sendPackets(new S_AGShopSellList(objid));
				return;
			}
			pc.sendPackets(new S_ShopSellList(objid));
		} else if (s.equalsIgnoreCase("sell")) {
			int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
			if (npcid == 70523 || npcid == 70805) {
				htmlid = "ladar2";
			} else if (npcid == 70537 || npcid == 70807) {
				htmlid = "farlin2";
			} else if (npcid == 70525 || npcid == 70804) {
				htmlid = "lien2";
			} else if (npcid == 50527 || npcid == 50505 || npcid == 50519 || npcid == 50545 || npcid == 50531 || npcid == 50529
					|| npcid == 50516 || npcid == 50538 || npcid == 50518 || npcid == 50509 || npcid == 50536 || npcid == 50520
					|| npcid == 50543 || npcid == 50526 || npcid == 50512 || npcid == 50510 || npcid == 50504 || npcid == 50525
					|| npcid == 50534 || npcid == 50540 || npcid == 50515 || npcid == 50513 || npcid == 50528 || npcid == 50533
					|| npcid == 50542 || npcid == 50511 || npcid == 50501 || npcid == 50503 || npcid == 50508 || npcid == 50514
					|| npcid == 50532 || npcid == 50544 || npcid == 50524 || npcid == 50535 || npcid == 50521 || npcid == 50517
					|| npcid == 50537 || npcid == 50539 || npcid == 50507 || npcid == 50530 || npcid == 50502 || npcid == 50506
					|| npcid == 50522 || npcid == 50541 || npcid == 50523 || npcid == 50620 || npcid == 50623 || npcid == 50619
					|| npcid == 50621 || npcid == 50622 || npcid == 50624 || npcid == 50617 || npcid == 50614 || npcid == 50618
					|| npcid == 50616 || npcid == 50615 || npcid == 50626 || npcid == 50627 || npcid == 50628 || npcid == 50629
					|| npcid == 50630 || npcid == 50631) {
				String sellHouseMessage = sellHouse(pc, objid, npcid);
				if (sellHouseMessage != null) {
					htmlid = sellHouseMessage;
				}
			} else {
				pc.sendPackets(new S_ShopBuyList(objid, pc));
			}
		} else if (s.equalsIgnoreCase("history")) {
			if (pc.isCrown() && pc.getClanid() > 0) {
				if (isTwoLogin(pc))
					return;
				  if (pc.getClan() != null) {
					     if (!(pc.getClanRank() == L1Clan.CLAN_RANK_PROBATION || pc.getClanRank() == L1Clan.CLAN_RANK_PUBLIC)) {
					      pc.sendPackets(new S_PacketBox(pc, S_PacketBox.CLAN_WAREHOUSE_LIST));
					     } else
					         pc.sendPackets(new S_ServerMessage(728));
					       } else
					        pc.sendPackets(new S_ServerMessage(208));
}
		} else if (s.equalsIgnoreCase("retrieve")) {
			if (pc.getLevel() >= 5) {
				if (isTwoLogin(pc))
					return;
				pc.sendPackets(new S_RetrieveList(objid, pc));
			}
		} else if (s.equalsIgnoreCase("retrieve-elven")) {
			if (pc.getLevel() >= 5 && pc.isElf()) {
				if (isTwoLogin(pc))
					return;
				pc.sendPackets(new S_RetrieveElfList(objid, pc));
			}
		} else if (s.equalsIgnoreCase("retrieve-aib")) {// ��Ű��â��
			if (pc.getLevel() >= 5) {
				if (isTwoLogin(pc))
					return;
				pc.sendPackets(new S_RetrievePackageList(objid, pc));
			}
		} else if (s.equalsIgnoreCase("retrieve-char")) { // Ư��â��
			if (isTwoLogin(pc))
				return;
			if (pc.get_SpecialSize() == 0) {
				pc.sendPackets(new S_ServerMessage(1623));
				return;
			}
			pc.sendPackets(new S_RetrieveSpecialList(objid, pc));
		} else if (s.equalsIgnoreCase("retrieve-pledge")) {
			if (pc.getLevel() >= 5) {
				if (isTwoLogin(pc))
					return;
				if (pc.getClan() != null) {
				     if (!(pc.getClanRank() == L1Clan.CLAN_RANK_PROBATION || pc.getClanRank() == L1Clan.CLAN_RANK_PUBLIC)) {
				      if (client.getAccount().getGamePassword() != 0)
				       pc.sendPackets(new S_ServerMessage(834));// �������
				      else
				       pc.sendPackets(new S_RetrievePledgeList(objid, pc));
				     } else
				      pc.sendPackets(new S_ServerMessage(728));
				    } else
				     pc.sendPackets(new S_ServerMessage(208));
			}
			
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3200015) {
			L1Teleport.teleport(pc, 32827, 32774, (short) 68, 5, true);
			htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3200016) {
			L1Teleport.teleport(pc, 32704, 32787, (short) 2005, 5, true);
			htmlid = "";




			
			
			
	
	
			
			
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039004) { // ���ֹ�������
			// �翤
			// (��������)
			if (s.equalsIgnoreCase("A")) {
				Collection<L1PcInstance> players = null;
				players = new FastTable<L1PcInstance>();
				players.add(pc);
				for (L1PcInstance tg : players) {
					new L1SkillUse().handleCommands(pc, 10501, tg.getId(), tg
							.getX(), tg.getY(), null, 60,
							L1SkillUse.TYPE_SPELLSC);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039005) { // ���ֹ�������
			// �翤2
			// (�ź��ѹ���)
			if (s.equalsIgnoreCase("A")) {
				Collection<L1PcInstance> players = null;
				players = new FastTable<L1PcInstance>();
				players.add(pc);
				for (L1PcInstance tg : players) {
					new L1SkillUse().handleCommands(pc, 10500, tg.getId(), tg
							.getX(), tg.getY(), null, 60,
							L1SkillUse.TYPE_SPELLSC);
				}
			}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039009) { // ���ֹ�������
			// �翤(�Ա�)
			if (s.equalsIgnoreCase("A")) {
				Collection<L1PcInstance> players = null;
				players = new FastTable<L1PcInstance>();
				players.add(pc);
				for (L1PcInstance tg : players) {
					new L1SkillUse().handleCommands(pc, 10499, tg.getId(), tg
							.getX(), tg.getY(), null, 60,
							L1SkillUse.TYPE_SPELLSC);
					new L1SkillUse().handleCommands(pc, 10501, tg.getId(), tg
							.getX(), tg.getY(), null, 60,
							L1SkillUse.TYPE_SPELLSC);
				}
			}
			/** ���� �ý��� * */
		} else if (s.equalsIgnoreCase("get")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			int npcId = npc.getNpcTemplate().get_npcId();
			if (npcId == 70099 || npcId == 70796) {
				L1ItemInstance item = pc.getInventory().storeItem(20081, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				pc.getQuest().set_end(L1Quest.QUEST_OILSKINMANT);
				htmlid = "";
			} else if (npcId == 70528 || npcId == 70546 || npcId == 70567
					|| npcId == 70594 || npcId == 70654 || npcId == 70748
					|| npcId == 70774 || npcId == 70799 || npcId == 70815
					|| npcId == 70860) {
				if (pc.getHomeTownId() > 0) {
				} else {
				}
			}
		} else if (s.equalsIgnoreCase("room")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			pc.sendPackets(new S_NpcChatPacket(npc, "������ ��¥�� �����Ͻø� �˴ϴ�.", 0));
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50635) {
			if (s.equalsIgnoreCase("enter")) {
				if (IceQueenController.getInstance().Start == false) {
					
					L1Teleport.teleport(pc, 32728, 32819, (short) 2101, 0, true);
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+ pc.getName()+ "���� ���̽� ���� �������� �������ϴ�."));
					L1World.getInstance().broadcastServerMessage("\\fW"+ pc.getName()+ "���� ���̽� ���� �������� �������ϴ�.");
					GeneralThreadPool.getInstance().execute(IceQueenController.getInstance());
				} else {
					pc.sendPackets(new S_SystemMessage("���̽� ���� ������ �̹� �������Դϴ�."));
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50636) {
			if (s.equalsIgnoreCase("enter")) {
				if (IceDemonController.getInstance().Start == false) {
					L1Teleport.teleport(pc, 32728, 32819, (short) 2151, 0, true);
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+ pc.getName()+ "���� ���̽� ���� �������� �������ϴ�."));
					L1World.getInstance().broadcastServerMessage("\\fW"+ pc.getName()+ "���� ���̽� ���� �������� �������ϴ�.");
					GeneralThreadPool.getInstance().execute(IceDemonController.getInstance());
				} else {
					pc.sendPackets(new S_SystemMessage("���̽� ���� ������ �̹� �������Դϴ�."));
				}
			}
			
		} else if (s.equalsIgnoreCase("hall")
				&& obj instanceof L1MerchantInstance) {

		} else if (s.equalsIgnoreCase("return")) {

		} else if (s.equalsIgnoreCase("enter")) {// �δ� �����ȯ
			if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039026) { // ��Ű
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (InDunController.getInstance().getInDunOpen() == false) {//pc�� �غ��ž��ؿ�..�� ��Ʈ������ 
					if (pc.isInParty() && pc.getParty().isLeader(pc)) {
						if (pc.getParty().getNumOfMembers() >= 2) {// ��Ƽ�ο�
							pc.getParty().getLeader().getName();
							InDunController Indun = InDunController.getInstance();
							Indun.start();
							L1Party party = pc.getParty();
							L1PcInstance[] players = party.getMembers();
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+ pc.getParty().getLeader().getName()+ "���� ������ �Բ� ���ŷ� �������ϴ�."));
							L1World.getInstance().broadcastServerMessage("\\fW"+ pc.getParty().getLeader().getName()+ "���� ������ �Բ� ���ŷ� �������ϴ�.");
							
							for (L1PcInstance pc1 : players) {
								Indun.addPlayMember(pc1);
								L1Teleport.teleport(pc1, 32726, 32725, (short) 9000, 5, true);
							}
						} else {
							htmlid = "id0_1"; // ��Ƽ���� ���ڶ�
						}
					} else {
						htmlid = "id0_2"; // ������ �;���
					}
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"�̹� ���ŷ� ���ߴ밡 ����ߴٳ�. ����� �ٽÿ���.", 0));
					htmlid = "";
				  }
			  }
		} else if (s.equalsIgnoreCase("enter")) {
			
		} else if (s.equalsIgnoreCase("openigate")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			openCloseGate(pc, npc.getNpcTemplate().get_npcId(), true);
			htmlid = "";
		} else if (s.equalsIgnoreCase("closeigate")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			openCloseGate(pc, npc.getNpcTemplate().get_npcId(), false);
			htmlid = "";
		} else if (s.equalsIgnoreCase("askwartime")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (npc.getNpcTemplate().get_npcId() == 60514) {
				htmldata = makeWarTimeStrings(L1CastleLocation.KENT_CASTLE_ID);
				htmlid = "ktguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60560) {
				htmldata = makeWarTimeStrings(L1CastleLocation.OT_CASTLE_ID);
				htmlid = "orcguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60552) {
				htmldata = makeWarTimeStrings(L1CastleLocation.WW_CASTLE_ID);
				htmlid = "wdguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60524
					|| npc.getNpcTemplate().get_npcId() == 60525
					|| npc.getNpcTemplate().get_npcId() == 60529) {
				htmldata = makeWarTimeStrings(L1CastleLocation.GIRAN_CASTLE_ID);
				htmlid = "grguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 70857) {
				htmldata = makeWarTimeStrings(L1CastleLocation.HEINE_CASTLE_ID);
				htmlid = "heguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60530
					|| npc.getNpcTemplate().get_npcId() == 60531) {
				htmldata = makeWarTimeStrings(L1CastleLocation.DOWA_CASTLE_ID);
				htmlid = "dcguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 60533
					|| npc.getNpcTemplate().get_npcId() == 60534) {
				htmldata = makeWarTimeStrings(L1CastleLocation.ADEN_CASTLE_ID);
				htmlid = "adguard7";
			} else if (npc.getNpcTemplate().get_npcId() == 81156) {
				htmldata = makeWarTimeStrings(L1CastleLocation.DIAD_CASTLE_ID);
				htmlid = "dfguard3";
			}
		} else if (s.equalsIgnoreCase("inex")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) {
					if (castle_id == 4)
						htmlid = "orville2";
					else if (castle_id == 6)
						htmlid = "potempin2";
					L1Castle l1castle = CastleTable.getInstance()
							.getCastleTable(castle_id);
					int money = l1castle.getShowMoney();// ���
					int a = money / 2 * 3;// �Һ��
					int b = money + a; // �Ѿ�
					int pm = l1castle.getPublicMoney();
					htmldata = new String[] { "" + b + "", "" + a + "",
							"" + money + "", "" + pm + "" };
				}
			}
		} else if (s.equalsIgnoreCase("stdex")) { // �⺻����
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) {
					if (castle_id == 4)
						htmlid = "orville3";
					else if (castle_id == 6)
						htmlid = "potempin3";
					L1Castle l1castle = CastleTable.getInstance()
							.getCastleTable(castle_id);
					int i = l1castle.getShowMoney();// ��� �ݾ�
					int totalmoney = i + i / 2 * 3;
					int money = totalmoney;
					int a = money / 100 * 25;// 25%
					int b = money / 100 * 10;// 10%
					int c = money / 100 * 5;// 5%
					htmldata = new String[] { "" + a + "", "" + b + "",
							"" + c + "", "" + c + "", "" + b + "", "" + c + "" };
				}
			}
		} else if (s.equalsIgnoreCase("tax")) {
			if (pc.getId() != pc.getClan().getLeaderId()
					|| pc.getClanRank() != 10 || !pc.isCrown()) {
				return;
			}
			pc.sendPackets(new S_TaxRate(pc.getId()));
		} else if (s.equalsIgnoreCase("withdrawal")) {
			if (pc.getId() != pc.getClan().getLeaderId()
					|| pc.getClanRank() != 10 || !pc.isCrown()) {
				return;
			}
/*			

			
		//3.63���ּ����ݼ���
		} else if (s.equalsIgnoreCase("tax")) {
			if (pc.getId() != pc.getClan().getLeaderId() &&!pc.isCrown()){
			return;
			} 
			pc.sendPackets(new S_TaxRate(pc.getId())); 
			} else if (s.equalsIgnoreCase("withdrawal")) {
			if (pc.getId() != pc.getClan().getLeaderId() &&!pc.isCrown()){
			return;
			}*/

			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) {
					L1Castle l1castle = CastleTable.getInstance()
							.getCastleTable(castle_id);
					if (l1castle.getPublicMoney() <= 0)
						return;
					pc.sendPackets(new S_Drawal(pc.getId(), l1castle
							.getPublicMoney()));
				}
			}
		} else if (s.equalsIgnoreCase("cdeposit")) {// �ڱ��Ա�
			pc.sendPackets(new S_Deposit(pc.getId()));
		} else if (s.equalsIgnoreCase("employ")) {// �뺴���
			int castle_id = pc.getClan().getCastleId();
			pc.sendPackets(new S_SoldierBuyList(objid, castle_id));
		} else if (s.equalsIgnoreCase("arrange")) {// �뺴��ġ
			int castle_id = pc.getClan().getCastleId();
			pc.sendPackets(new S_SoldierGiveList(objid, castle_id));
		} else if (s.equalsIgnoreCase("castlegate")) { // ����
			castleGateStatus(pc, objid);
		} else if (s.equalsIgnoreCase("demand")) {
			GiveSoldier(pc, objid);
		} else if (s.equalsIgnoreCase("healegate_giran outer gatef")) {// �ܼ� ����
			repairGate(pc, 2031, 4);
		} else if (s.equalsIgnoreCase("healegate_giran outer gatel")) {// �ܼ� ����
			repairGate(pc, 2032, 4);
		} else if (s.equalsIgnoreCase("healegate_giran inner gatef")) {// ���� ����
			repairGate(pc, 2033, 4);
		} else if (s.equalsIgnoreCase("healegate_giran inner gatel")) {// ���� ����
			repairGate(pc, 2034, 4);
		} else if (s.equalsIgnoreCase("healegate_giran inner gater")) {// ���� ����
			repairGate(pc, 2035, 4);
		} else if (s.equalsIgnoreCase("healigate_giran castle house door")) {// ������
			repairGate(pc, 2030, 4);
		} else if (s.equalsIgnoreCase("hhealegate_iron door a")) {// ���� �ܼ� ����
			repairGate(pc, 2051, 4);
		} else if (s.equalsIgnoreCase("hhealegate_iron door b")) {// ���� �ܼ� ������
			repairGate(pc, 2052, 4);
		} else if (s.equalsIgnoreCase("autorepairon")) {// �ڵ����� On
			repairAutoGate(pc, 1);
		} else if (s.equalsIgnoreCase("autorepairoff")) {// �ڵ����� Off
			repairAutoGate(pc, 0);
		} else if (s.equalsIgnoreCase("encw")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			L1NpcInstance npc = (L1NpcInstance) obj;
			int npcid = npc.getNpcTemplate().get_npcId();

			if (npcid == 70508) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					//pc.sendPackets(new S_ServerMessage(189));
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ �ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
					return;
				}
			} else if (npcid == 70547) { // ��Ʈ��
				if (clan != null && clan.getCastleId() != 1)
					return;
			} else if (npcid == 70816) { // ��ũ��
				if (clan != null && clan.getCastleId() != 2)
					return;
			} else if (npcid == 70777) { // ���ٿ��
				if (clan != null && clan.getCastleId() != 3)
					return;
			} else if (npcid == 70599) { // ���
				if (clan != null && clan.getCastleId() != 4)
					return;
			} else if (npcid == 70861) { // ���̳�
				if (clan != null && clan.getCastleId() != 5)
					return;
			} else if (npcid == 70655) { // ����
				if (clan != null && clan.getCastleId() != 6)
					return;
			} else if (npcid == 70686) { // �Ƶ�
				if (clan != null && clan.getCastleId() != 7)
					return;
			}
			if (pc.getWeapon() == null) {
				pc.sendPackets(new S_ServerMessage(79));
			} else {
				L1SkillUse l1skilluse = null;
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if (pc.getWeapon().equals(item)) {
						l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(pc, L1SkillId.ENCHANT_WEAPON,
								item.getId(), 0, 0, null, 0,
								L1SkillUse.TYPE_SPELLSC);
						break;
					}
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("enca")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			L1NpcInstance npc = (L1NpcInstance) obj;
			int npcid = npc.getNpcTemplate().get_npcId();

			if (npcid == 70509) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
					return;
				}
			} else if (npcid == 70550) { // ��Ʈ��
				if (clan != null && clan.getCastleId() != 1)
					return;
			} else if (npcid == 70820) { // ��ũ��
				if (clan != null && clan.getCastleId() != 2)
					return;
			} else if (npcid == 70780) { // ���ٿ��
				if (clan != null && clan.getCastleId() != 3)
					return;
			} else if (npcid == 70601) { // ���
				if (clan != null && clan.getCastleId() != 4)
					return;
			} else if (npcid == 70865) { // ���̳�
				if (clan != null && clan.getCastleId() != 5)
					return;
			} else if (npcid == 70657) { // ����
				if (clan != null && clan.getCastleId() != 6)
					return;
			} else if (npcid == 70692) { // �Ƶ�
				if (clan != null && clan.getCastleId() != 7)
					return;
			}
			L1ItemInstance item = pc.getInventory().getItemEquipped(2, 2);
			if (item != null) {
				L1SkillUse l1skilluse = new L1SkillUse();
				l1skilluse.handleCommands(pc, L1SkillId.BLESSED_ARMOR, item
						.getId(), 0, 0, null, 0, L1SkillUse.TYPE_SPELLSC);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("depositnpc")) { // �� �ñ��
			Object[] petList = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			@SuppressWarnings("unused")
			int value = 0;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					if (pet.getArmor() != null) {
						pet.removePetArmor(pet.getArmor());
					}
					if (pet.getWeapon() != null) {
						pet.removePetWeapon(pet.getWeapon());
					}
					pet.collect();
					int time = pet.getSkillEffectTimerSet()
							.getSkillEffectTimeSec(L1SkillId.STATUS_PET_FOOD);
					PetTable.getInstance().storePetFoodTime(pet.getId(),
							pet.getFood(), time);
					pet.getSkillEffectTimerSet().clearSkillEffectTimer();
					pet.deleteMe();
					pc.getPetList().remove(pet.getId());
				}
				value++;
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("withdrawnpc")) {
			pc.sendPackets(new S_PetList(objid, pc));
		} else if (s.equalsIgnoreCase("changename")) {
			pc.setTempID(objid);
			pc.sendPackets(new S_Message_YN(325, ""));
		} else if (s.equalsIgnoreCase("attackchr")) {
			if (obj instanceof L1Character) {
				L1Character cha = (L1Character) obj;
				pc.sendPackets(new S_SelectTarget(cha.getId()));
			}
		} else if (s.equalsIgnoreCase("select")) {
			pc.sendPackets(new S_AuctionBoardRead(objid, s2));
		} else if (s.equalsIgnoreCase("map")) {
			pc.sendPackets(new S_HouseMap(objid, s2));
		} else if (s.equalsIgnoreCase("apply")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
					if (pc.getLevel() >= 15) {
						if (clan.getHouseId() == 0) {
							pc.sendPackets(new S_ApplyAuction(objid, s2));
						} else {
							pc.sendPackets(new S_ServerMessage(521));
							htmlid = "";
						}
					} else {
						pc.sendPackets(new S_ServerMessage(519));
						htmlid = "";
					}
				} else {
					pc.sendPackets(new S_ServerMessage(518));
					htmlid = "";
				}
			} else {
				pc.sendPackets(new S_ServerMessage(518));
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("open") || s.equalsIgnoreCase("close")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			openCloseDoor(pc, npc, s);
			htmlid = "";
		} else if (s.equalsIgnoreCase("expel")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			expelOtherClan(pc, npc.getNpcTemplate().get_npcId());
			htmlid = "";
		} else if (s.equalsIgnoreCase("pay")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			htmldata = makeHouseTaxStrings(pc, npc);
			htmlid = "agpay";
		} else if (s.equalsIgnoreCase("payfee")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			payFee(pc, npc);
			htmlid = "";
		} else if (s.equalsIgnoreCase("name")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					if (!pc.isCrown() || pc.getId() != clan.getLeaderId()) {
						pc.sendPackets(new S_ServerMessage(518));
						return;
					}
					L1House house = HouseTable.getInstance().getHouseTable(
							houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						pc.setTempID(houseId);
						pc.sendPackets(new S_Message_YN(512, ""));
					}
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("rem")) {
		} else if (s.equalsIgnoreCase("tel0") || s.equalsIgnoreCase("tel1")
				|| s.equalsIgnoreCase("tel2") || s.equalsIgnoreCase("tel3")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(
							houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						int[] loc = new int[3];
						if (s.equalsIgnoreCase("tel0")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId,
									0);
						} else if (s.equalsIgnoreCase("tel1")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId,
									1);
						} else if (s.equalsIgnoreCase("tel2")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId,
									2);
						} else if (s.equalsIgnoreCase("tel3")) {
							loc = L1HouseLocation.getHouseTeleportLoc(houseId,
									3);
						}
						L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2],
								5, true);
					}
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("upgrade")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(
							houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
							if (house.isPurchaseBasement()) {
								pc.sendPackets(new S_ServerMessage(1135));
							} else {
								if (pc.getInventory().consumeItem(
										L1ItemId.ADENA, 5000000)) {
									house.setPurchaseBasement(true);
									HouseTable.getInstance().updateHouse(house);
									pc.sendPackets(new S_ServerMessage(1099));
								} else {
									S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
									pc.sendPackets(s_chatpacket);
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(518));
						}
					}
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("hall")
				&& obj instanceof L1HousekeeperInstance) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(
							houseId);
					int keeperId = house.getKeeperId();
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcTemplate().get_npcId() == keeperId) {
						if (house.isPurchaseBasement()) {
							int[] loc = new int[3];
							loc = L1HouseLocation.getBasementLoc(houseId);
							L1Teleport.teleport(pc, loc[0], loc[1],
									(short) (loc[2]), 5, true);
						} else {
							pc.sendPackets(new S_ServerMessage(1098));
						}
					}
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("fire")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(2);
				pc.save();
				pc.sendPackets(new S_SkillIconGFX(15, 1));
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("water")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(4);
				pc.save();
				pc.sendPackets(new S_SkillIconGFX(15, 2));
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("air")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(8);
				pc.save();
				pc.sendPackets(new S_SkillIconGFX(15, 3));
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("earth")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return;
				}
				pc.setElfAttr(1);
				pc.save();
				pc.sendPackets(new S_SkillIconGFX(15, 4));
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("init")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() == 0) {
					return;
				}
				L1Skills l1skills1 = null;
				for (int cnt = 129; cnt <= 176; cnt++) {
					l1skills1 = SkillsTable.getInstance().getTemplate(cnt);
					int skill_attr = l1skills1.getAttr();
					if (skill_attr != 0) {
						SkillsTable.getInstance().spellLost(pc.getId(),
								l1skills1.getSkillId());
					}
				}
				if (pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.ELEMENTAL_PROTECTION)) {
					pc.getSkillEffectTimerSet().removeSkillEffect(
							L1SkillId.ELEMENTAL_PROTECTION);
				}
				pc.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 248, 252, 252, 255, 0, 0, 0, 0, 0,
						0));
				pc.setElfAttr(0);
				pc.save();
				pc.sendPackets(new S_ServerMessage(678));
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("exp")) {
			if (pc.getExpRes() == 1) {
				int cost = 0;
				int level = pc.getLevel();
				int lawful = pc.getLawful();
				if (level < 45) {
					cost = level * level * 100;
				} else {
					cost = level * level * 200;
				}
				if (lawful >= 0) {
					cost = (cost / 2);
				}
				cost *= 2;
				pc.sendPackets(new S_Message_YN(738, String.valueOf(cost)));
			} else {
				pc.sendPackets(new S_ServerMessage(739));
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("material")) { // ��ȣ ����
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (pc.getInventory().checkItem(701235, 1)) {
				if (pc.getExpRes() == 1) {
					pc.sendPackets(new S_Message_YN(2551, ""));
				} else {
					pc.sendPackets(new S_ServerMessage(739));
					htmlid = "";
				}
			} else {
				pc.sendPackets(new S_NpcChatPacket(npc, "��ȣ ������ �����մϴ�.", 0));
			}
		} else if (s.equalsIgnoreCase("pk")) {
			if (pc.getLawful() < 30000) {
				pc.sendPackets(new S_ServerMessage(559));
			} else if (pc.get_PKcount() < 5) {
				pc.sendPackets(new S_ServerMessage(560));
			} else {
				if (pc.getInventory().consumeItem(L1ItemId.ADENA, 700000)) {
					pc.set_PKcount(pc.get_PKcount() - 5);
					pc.sendPackets(new S_ServerMessage(561, String.valueOf(pc
							.get_PKcount())));
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("ent")) {
			int npcId = ((L1NpcInstance) obj).getNpcId();
			if (npcId == 80085) {
				htmlid = enterHauntedHouse(pc);
/*			} else if (npcId == 80086 || npcId == 80087) {
				htmlid = enterDeathMatch(pc, npcId);*/
			} else if (npcId == 80088) {
				htmlid = enterPetMatch(pc, Integer.valueOf(s2));
			} else if (npcId == 4206002) { // �� ���̽�
				htmlid = enterPetRacing(pc);
			} else if (npcId == 4206000) {// ���ʿ��ǽ�
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (pc.getLevel() != pc.getHighLevel()) {
					pc.sendPackets(new S_NpcChatPacket(npc,	"������ ����� �ɸ��� �Դϴ�. ������ �̿����ּ���.", 0));
				} else if (!pc.getInventory().checkItem(
						L1ItemId.REMINISCING_CANDLE)) {
					pc.sendPackets(new S_NpcChatPacket(npc,	"�������ͽ� �ʱ�ȭ�� �ʿ��� �������� �����ϴ�.", 0));
				} else {

					if (pc.getLevel() > 50)
						if (pc.getInventory().checkItem(
								L1ItemId.REMINISCING_CANDLE)) {
							pc.getInventory().consumeItem(
									L1ItemId.REMINISCING_CANDLE, 1);
							L1Teleport.teleport(pc,
									32723 + _random.nextInt(10),
									32851 + _random.nextInt(10), (short) 5166,
									5, true);
							StatInitialize(pc);
							htmlid = "";
						} else {
							pc.sendPackets(new S_NpcChatPacket(npc,	"�������ͽ� �ʱ�ȭ�� �ʿ��� �������� �����ϴ�.", 0));
						}
					else {
						pc.sendPackets(new S_NpcChatPacket(npc,	"ȸ���� �к��� ����51���� ����ϽǼ� �ֽ��ϴ�.", 0));

					}
				}
			} else if (npcId == 50038 || npcId == 50042 || npcId == 50029
					|| npcId == 50019 || npcId == 50062) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				pc.sendPackets(new S_NpcChatPacket(npc,	"���� ������� ������ ���Դϴ�.", 0));
			} else {
				htmlid = enterUb(pc, npcId);
			}
		} else if (s.equalsIgnoreCase("par")) {
			htmlid = enterUb(pc, ((L1NpcInstance) obj).getNpcId());
		} else if (s.equalsIgnoreCase("info")) {
			int npcId = ((L1NpcInstance) obj).getNpcId();
			if (npcId == 80085 || npcId == 80086 || npcId == 80087) {
			} else {
				htmlid = "colos2";
			}
		} else if (s.equalsIgnoreCase("sco")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			UbRank(pc, npc);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 190009) {
			if (s.equalsIgnoreCase("D")) {// �ʺ��� �����
				if (pc.getLevel() < 13) {
					
					pc.sendPackets(new S_SkillHaste(pc.getId(), 1, 1800));
					Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 1, 0));
					pc.sendPackets(new S_SkillSound(pc.getId(), 755));
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 755));
					pc.getMoveState().setMoveSpeed(1);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.STATUS_HASTE, 1800 * 1000);
					pc.setCurrentHp(pc.getMaxHp());
					pc.setCurrentMp(pc.getMaxMp());
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				}
				htmlid = "tutorm4";
			} else if (s.equalsIgnoreCase("Q")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32605, 32837, (short) 2005, 4, true);
			} else if (s.equalsIgnoreCase("R")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32733, 32902, (short) 2005, 4, true);
			} else if (s.equalsIgnoreCase("S")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32802, 32803, (short) 2005, 4, true);
			} else if (s.equalsIgnoreCase("T")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32642, 32763, (short) 2005, 4, true);
			} else if (s.equalsIgnoreCase("H")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32572, 32945, (short) 0, 4, true);
			} else if (s.equalsIgnoreCase("K")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32587, 32955, (short) 0, 4, true);
			} else if (s.equalsIgnoreCase("J")) {
				htmlid = "";
				L1Teleport.teleport(pc, 32872, 32871, (short) 86, 4, true);
			}
		} else if (s.equalsIgnoreCase("skeleton nbmorph")) {
			if (pc.getLevel() < 13) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 2374);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("lycanthrope nbmorph")) {
			if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
				poly(client, 3874);
				pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
			} else {
				S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
				pc.sendPackets(s_chatpacket);
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("shelob nbmorph")) {
			if (pc.getLevel() < 13) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 95);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket); 
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("ghoul nbmorph")) {
			if (pc.getLevel() < 13) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 3873);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket); 
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("ghast nbmorph")) {
			if (pc.getLevel() < 13) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 3875);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket); 
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("atuba orc nbmorph")) {
			if (pc.getLevel() < 13) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 3868);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket); 
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("skeleton axeman nbmorph")) {
			if (pc.getLevel() < 13) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 2376);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket); 
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("troll nbmorph")) {
			if (pc.getLevel() < 13) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 3878);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket); 
				}
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("status")) {
			htmlid = "maeno4";
			htmldata = L1BugBearRace.getInstance().makeStatusString();
		} else if (s.equalsIgnoreCase("contract1")) {
			pc.getQuest().set_step(L1Quest.QUEST_LYRA, 1);
			htmlid = "lyraev2";
		} else if (s.equalsIgnoreCase("contract1yes")
				|| s.equalsIgnoreCase("contract1no")) {

			if (s.equalsIgnoreCase("contract1yes")) {
				htmlid = "lyraev5";
			} else if (s.equalsIgnoreCase("contract1no")) {
				pc.getQuest().set_step(L1Quest.QUEST_LYRA, 0);
				htmlid = "lyraev4";
			}
			int totem = 0;
			if (pc.getInventory().checkItem(40131)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40132)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40133)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40134)) {
				totem++;
			}
			if (pc.getInventory().checkItem(40135)) {
				totem++;
			}
			if (totem != 0) {
				materials = new int[totem];
				counts = new int[totem];
				createitem = new int[totem];
				createcount = new int[totem];

				totem = 0;
				if (pc.getInventory().checkItem(40131)) {
					L1ItemInstance l1iteminstance = pc.getInventory()
							.findItemId(40131);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40131;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 50;
					totem++;
				}
				if (pc.getInventory().checkItem(40132)) {
					L1ItemInstance l1iteminstance = pc.getInventory()
							.findItemId(40132);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40132;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 100;
					totem++;
				}
				if (pc.getInventory().checkItem(40133)) {
					L1ItemInstance l1iteminstance = pc.getInventory()
							.findItemId(40133);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40133;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 50;
					totem++;
				}
				if (pc.getInventory().checkItem(40134)) {
					L1ItemInstance l1iteminstance = pc.getInventory()
							.findItemId(40134);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40134;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 30;
					totem++;
				}
				if (pc.getInventory().checkItem(40135)) {
					L1ItemInstance l1iteminstance = pc.getInventory()
							.findItemId(40135);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40135;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 200;
					totem++;
				}
			}
		} else if (s.equalsIgnoreCase("pandora6")
				|| s.equalsIgnoreCase("cold6") || s.equalsIgnoreCase("balsim3")
				|| s.equalsIgnoreCase("arieh6") || s.equalsIgnoreCase("andyn3")
				|| s.equalsIgnoreCase("ysorya3") || s.equalsIgnoreCase("luth3")
				|| s.equalsIgnoreCase("catty3") || s.equalsIgnoreCase("mayer3")
				|| s.equalsIgnoreCase("vergil3") || s.equalsIgnoreCase("stella6")
				|| s.equalsIgnoreCase("ralf6")	|| s.equalsIgnoreCase("berry6")
				|| s.equalsIgnoreCase("jin6") || s.equalsIgnoreCase("defman3")
				|| s.equalsIgnoreCase("mellisa3") || s.equalsIgnoreCase("mandra3") || s.equalsIgnoreCase("bius3")
				|| s.equalsIgnoreCase("momo6") || s.equalsIgnoreCase("ashurEv7")
				|| s.equalsIgnoreCase("elmina3") || s.equalsIgnoreCase("glen3")
				|| s.equalsIgnoreCase("mellin3") || s.equalsIgnoreCase("orcm6")
				|| s.equalsIgnoreCase("jackson3") || s.equalsIgnoreCase("britt3") || s.equalsIgnoreCase("old6")
				|| s.equalsIgnoreCase("shivan3")) {
			htmlid = s;
			int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
			int taxRatesCastle = L1CastleLocation
					.getCastleTaxRateByNpcId(npcid);
			htmldata = new String[] { String.valueOf(taxRatesCastle) };
		} else if (s.equalsIgnoreCase("set")) {
			if (obj instanceof L1NpcInstance) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int town_id = L1TownLocation.getTownIdByNpcid(npcid);

				if (town_id >= 1 && town_id <= 10) {
					if (pc.getHomeTownId() == -1) {
						pc.sendPackets(new S_ServerMessage(759));
						htmlid = "";
					} else if (pc.getHomeTownId() > 0) {
						if (pc.getHomeTownId() != town_id) {
							L1Town town = TownTable.getInstance().getTownTable(
									pc.getHomeTownId());
							if (town != null) {
								pc.sendPackets(new S_ServerMessage(758, town
										.get_name()));
							}
							htmlid = "";
						} else {
							htmlid = "";
						}
					} else if (pc.getHomeTownId() == 0) {
						if (pc.getLevel() < 10) {
							pc.sendPackets(new S_ServerMessage(757));
							htmlid = "";
						} else {
							int level = pc.getLevel();
							int cost = level * level * 10;
							if (pc.getInventory().consumeItem(L1ItemId.ADENA,
									cost)) {
								pc.setHomeTownId(town_id);
								pc.setContribution(0);
								pc.save();
							} else {
								pc.sendPackets(new S_ServerMessage(337, "$4"));
							}
							htmlid = "";
						}
					}
				}
			}
		} else if (s.equalsIgnoreCase("clear")) {
			if (obj instanceof L1NpcInstance) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int town_id = L1TownLocation.getTownIdByNpcid(npcid);
				if (town_id > 0) {
					if (pc.getHomeTownId() > 0) {
						if (pc.getHomeTownId() == town_id) {
							pc.setHomeTownId(-1);
							pc.setContribution(0);
							pc.save();
						} else {
							pc.sendPackets(new S_ServerMessage(756));
						}
					}
					htmlid = "";
				}
			}
		} else if (s.equalsIgnoreCase("ask")) {
			if (obj instanceof L1NpcInstance) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int town_id = L1TownLocation.getTownIdByNpcid(npcid);

				if (town_id >= 1 && town_id <= 10) {
					L1Town town = TownTable.getInstance().getTownTable(town_id);
					String leader = town.get_leader_name();
					if (leader != null && leader.length() != 0) {
						htmlid = "owner";
						htmldata = new String[] { leader };
					} else {
						htmlid = "noowner";
					}
				}
			}
			/** �����޶��� �̷����� */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50007) { // �����޶���
			if (s.equalsIgnoreCase("journey")) {
				pc.sendPackets(new S_SystemMessage("��� �Ҽ� �����ϴ�."));
			}
			/** �����޶��� �̷����� */
			//������� npc
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50637) { // ���ž ø����
			if (s.equalsIgnoreCase("a")) {
				if (!pc.getInventory().checkItem(46091)) {//üũ
				pc.getInventory().storeItem(46091, 10);//�������� �ش�.
				htmlid = "icqwand2";//�����ص�Ƚ��ϴ�.
			} else {
				htmlid = "icqwand4";//�̹̹޾ҽ��ϴ�.
			}
			}
				if (s.equalsIgnoreCase("b")) {
				if (!pc.getInventory().checkItem(46113)) {//üũ
					pc.getInventory().storeItem(46113, 100);//�������� �ش�.
					htmlid = "icqwand3";//�����ص�Ƚ��ϴ�.
				} else {//�ѹ��޾Ҵٸ�.
					htmlid = "icqwand4";//�̹̹޾ҽ��ϴ�.
				}
			}
				/** ��� ������ **/
		/*} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 42120113) {//��� ������
	        if (s.equalsIgnoreCase("1") && pc.getLevel() > 45 && pc.getLevel() < 90) { //66����
				RealTime time = RealTimeClock.getInstance().getRealTime();
				int entertime = pc.getDdungeonTime() % 1000;
				int enterday = pc.getDdungeonTime() / 1000;
				int dayofyear = time.get(Calendar.DAY_OF_YEAR);
				if(entertime > 180 && enterday == dayofyear){
					pc.sendPackets(new S_ServerMessage(1522, "2"));// 3�ð� ��� ����ߴ�.
					htmlid ="";
					return;
				} else {
					if(enterday < dayofyear)pc.setDdungeonTime(dayofyear * 1000);
					L1Teleport.teleport(pc, 32740, 32777, (short) 30, 6, true);
					 pc.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIMER, (120 - entertime)*60));
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
			}*/
			
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70758) { // ����
			if (s.equalsIgnoreCase("a")) {
				pc.getInventory().storeItem(49031, 1);// �ֱ�
				pc.getInventory().storeItem(21081, 1);// �ֱ�
				pc.sendPackets(new S_SystemMessage("������ ��ſ��� ���� ����ü�� �־����ϴ�."));
				pc.sendPackets(new S_SystemMessage("������ ��ſ��� ���� ������ �Ͱ��� �� �־����ϴ�."));
				L1Teleport.teleport(pc, 34056, 32279, (short) 4, 5, true);
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70700) { // ����46116
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(46111, 100)
						&& pc.getInventory().checkItem(46116, 1)
						&& pc.getInventory().checkItem(46117, 1)) {
					pc.getInventory().consumeItem(46111, 100);
					pc.getInventory().consumeItem(46116, 1);
					pc.getInventory().consumeItem(46117, 1);
					pc.getInventory().storeItem(46112, 5);
					int needExp = ExpTable.getNeedExpNextLevel(pc.getLevel());
					double PobyExp = needExp * 0.00; 
					pc.addExp((int) PobyExp);
					pc.setExpRes(0);
					pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
					htmlid = "marbinquest4";
				} else {
					htmlid = "marbinquest5";
				}
			}
			if (s.equalsIgnoreCase("b")) {
				if (pc.getLevel() >= 51) {
					pc.getInventory().storeItem(46115, 1);
					htmlid = "marbinquest2";
				} else {
					htmlid = "marbinquest8";
				}
			}
			if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().checkItem(46111, 100)
						&& pc.getInventory().checkItem(46118, 1)
						&& pc.getInventory().checkItem(46117, 1)) {
					pc.getInventory().consumeItem(46111, 100);
					pc.getInventory().consumeItem(46118, 1);
					pc.getInventory().consumeItem(46117, 1);
					pc.getInventory().storeItem(46112, 5);
					int needExp = ExpTable.getNeedExpNextLevel(pc.getLevel());
					double PobyExp = needExp * 0.00; 
					pc.addExp((int) PobyExp);
					pc.setExpRes(0);
					pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
					htmlid = "marbinquest6";
				} else {
					htmlid = "marbinquest7";
				}
			}
			// �극��
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70751) {
			if (s.equalsIgnoreCase("a")) {
				int x = 0, y = 0, mapid = 0;
				// ����
				if (pc.isCrown()) {
					x = 32734;
					y = 32852;
					mapid = 274;
					// ���
				} else if (pc.isKnight()) {
					x = 32737;
					y = 32810;
					mapid = 276;
					// ����
				} else if (pc.isElf()) {
					x = 32735;
					y = 32867;
					mapid = 275;
					// ��ũ ����
				} else if (pc.isDarkelf()) {
					x = 32736;
					y = 32809;
					mapid = 273;
					// ����
				} else if (pc.isDragonknight()) {
					x = 32734;
					y = 32852;
					mapid = 277;
					// ������
				} else if (pc.isWizard()) {
					x = 32739;
					y = 32856;
					mapid = 271;
					// ȯ����
				} else if (pc.isIllusionist()) {
					x = 32809;
					y = 32830;
					mapid = 272;
				}
				L1Teleport.teleport(pc, x, y, (short) mapid, pc.getMoveState().getHeading(), true);
				htmlid = "";
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(46110, 1)) { // ȭ���� ���
					pc.getInventory().consumeItem(46110, 1);
					L1Teleport.teleport(pc, 32788, 32799, (short) 2100, pc.getMoveState().getHeading(), true);
					htmlid = "";
				} else {
					htmlid = "newbrad3";
				}
			}
			/** �ʸ��� */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80102) { // �ʸ���
			if (s.equalsIgnoreCase("1")) { // �����Ƿڸ� �׸��д�
				pc.getInventory().consumeItem(41329, 1); // �Ƿڼ� ȸ��
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4040007) { // �����ڵ����
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("0")) { // �ڷ�?
				htmlid = "";
			} else if (s.equalsIgnoreCase("1")) { // �ٸ�����? �̰� �³�?!��
				htmlid = "lowlv15";
			} else if (s.equalsIgnoreCase("2")) { // ���ž��� ����
				if (!pc.getInventory().checkItem(20126)
						&& !pc.getInventory().checkItem(20206) && !pc.getInventory().checkItem(20232)
						&& !pc.getInventory().checkItem(20173) && !pc.getInventory().checkItem(20028)
						&& !pc.getInventory().checkItem(7) && !pc.getInventory().checkItem(174)
						&& !pc.getInventory().checkItem(73) && !pc.getInventory().checkItem(175)
						&& !pc.getInventory().checkItem(224) && !pc.getInventory().checkItem(120)
						&& !pc.getInventory().checkItem(105) && !pc.getInventory().checkItem(156)
						&& !pc.getInventory().checkItem(35)	&& !pc.getInventory().checkItem(48)) { // �� �� ��ξ�����
						pc.getInventory().storeItem(350209, 1); // �����۹��� ����
					// (�ʺ������ۻ���)
					pc.sendPackets(new S_SystemMessage("�ʺ� ������ ���ڸ� ������ϴ�."));
					htmlid = "lowlv16";
				} else if (!pc.getInventory().checkItem(7)
						&& !pc.getInventory().checkItem(174)
						&& !pc.getInventory().checkItem(73)
						&& !pc.getInventory().checkItem(175)
						&& !pc.getInventory().checkItem(224)
						&& !pc.getInventory().checkItem(120)
						&& !pc.getInventory().checkItem(105)
						&& !pc.getInventory().checkItem(156)
						&& !pc.getInventory().checkItem(35)
						&& !pc.getInventory().checkItem(48)) {
					if (pc.isKnight()) { // ����̸�?
						pc.getInventory().storeItem(35, 1); // ���ž ��
						pc.getInventory().storeItem(48, 1); // ���ž ��հ�
						pc.getInventory().storeItem(40014, 10); // ����� ����
						htmlid = "lowlv19";
					}
					if (pc.isCrown()) { // ���ָ�
						pc.getInventory().storeItem(35, 1); // ���ž ��
						pc.getInventory().storeItem(48, 1); // ���ž ��հ�
						pc.getInventory().storeItem(40031, 10); // �Ǹ��� ��
						htmlid = "lowlv19";
					}
					if (pc.isWizard()) { // �����
						pc.getInventory().storeItem(120, 1); // ���ž ������
						pc.getInventory().storeItem(40016, 10); // ������ ����
						htmlid = "lowlv19";
					}
					if (pc.isElf()) { // �����̸�
						pc.getInventory().storeItem(40068, 10); // ���� ����
						pc.getInventory().storeItem(35, 1); // ���ž ��
						pc.getInventory().storeItem(174, 1); // ���ž Ȱ
						pc.getInventory().storeItem(175, 1); // ���ž Ȱ
						pc.getInventory().storeItem(40744, 2000); // ��ȭ��
						htmlid = "lowlv19";
					}
					if (pc.isDarkelf()) { // �ٿ��̸�
						pc.getInventory().storeItem(73, 1); // ���ž �̵���
						pc.getInventory().storeItem(156, 1); // ���ž ũ�ο�
						htmlid = "lowlv19";
					}
					if (pc.isDragonknight()) { // �����
						pc.getInventory().storeItem(430006, 10); // ���׵�� ����
						pc.getInventory().storeItem(35, 1); // ���ž ��
						pc.getInventory().storeItem(147, 1); // ���ž ����
						pc.getInventory().storeItem(48, 1); // ���ž ��հ�
						htmlid = "lowlv19";
					}
					if (pc.isIllusionist()) { // ȯ�����
						pc.getInventory().storeItem(430006, 10); // ���׵�� ����
						pc.getInventory().storeItem(120, 1); // ���ž ������
						htmlid = "lowlv19";
					}// ���ž�� ���Ⱑ ������
				} else if (!pc.getInventory().checkItem(20126)
						&& !pc.getInventory().checkItem(20206)
						&& !pc.getInventory().checkItem(20232)
						&& !pc.getInventory().checkItem(20173)
						&& !pc.getInventory().checkItem(20028)) { // �� ���
					// ������
					pc.getInventory().storeItem(20126, 1);
					pc.getInventory().storeItem(20206, 1);
					pc.getInventory().storeItem(20232, 1);
					pc.getInventory().storeItem(20173, 1);
					pc.getInventory().storeItem(20028, 1);
					htmlid = "lowlv18";
				} else {
					htmlid = "lowlv17"; // �� ���� ��� ������
				}
			} else if (s.equalsIgnoreCase("3")) { // �� �ֹ��� 1500���� ��ȯ
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 1500)) { // 1500
					// �Ƶ�����
					// �ȵɶ�
					htmlid = "lowlv20"; // �Ƶ��� x �� �ֹ��� X
				} else { // �Ƶ����� ������
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1500); // 1500��
					// ����
					pc.getInventory().storeItem(540341, 1); // �� �ֹ��� ȹ��
					pc.sendPackets(new S_SystemMessage("������ �� �ֹ����� ������ϴ�."));
					htmlid = "lowlv21"; // �� �ֹ��� , �߾���?!��
				}
			} else if (s.equalsIgnoreCase("4")) { // ���� �ֹ��� 1000���� ��ȯ
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) { // 1000
					// �Ƶ�����
					// �ȵɶ�
					htmlid = "lowlv20"; // �Ƶ��� x ���� �ֹ��� X
				} else { // �Ƶ����� ������
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000); // 1000��
					// ����
					pc.getInventory().storeItem(540342, 1); // ���� �ֹ��� ȹ��
					pc.sendPackets(new S_SystemMessage("������ ���� �ֹ����� ������ϴ�."));
					htmlid = "lowlv21"; // ���� �ֹ��� , �߾���?!��
				}
			} else if (s.equalsIgnoreCase("5")) { // ���� 250���� ��ȯ
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 1)) { // 250��
					// �Ƶ�����
					// �ȵɶ�
					htmlid = "lowlv14"; // �Ƶ��� x ���� ����
				} else { // �Ƶ����� ������
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1); // 250��
					// ����
					pc.getInventory().storeItem(400251, 1); // ���ž�� ����
					pc.sendPackets(new S_SystemMessage("���ž�� ������ ������ϴ�."));
					//pc.sendPackets(new S_NpcChatPacket(npc,"���ž�� ������ ǰ�� �����Դϴ�.", 0));
					htmlid = "lowlv21"; // �����ٶ� , �߾���?!��
				}
			} else if (s.equalsIgnoreCase("6")) { // ���ž �����ָӴ� 2000���� ��ȯ
				if (pc.getInventory().checkItem(420010, 1)
						&& pc.getInventory().checkItem(20282, 1)) { // �����ָӴ�,�����ֹ���
					// ������(�����۹��ʺ���)
					htmlid = "lowlv23"; // �ָӴ�O ,X
				} else if (!pc.getInventory().checkItem(L1ItemId.ADENA, 2000)) { // 2000��
					// �Ƶ�����
					// �ȵɶ�
					htmlid = "lowlv20"; // ������
				} else { // �ָӴ�,�����ֹ���X , �������� (������ �ִ� ���� O)
					pc.getInventory().consumeItem(L1ItemId.ADENA, 2000); // 2000��
					// ����
					pc.getInventory().storeItem(540344, 1); // ���� �ָӴ�
					// ȹ��(�����۹��ʺ���)
					pc.sendPackets(new S_SystemMessage("���ž�� �����ָӴϸ� ������ϴ�."));
					htmlid = "lowlv22"; // �ָӴ��ٶ� , ����
				}
			}
		/*	} else if (s.equalsIgnoreCase("7")) { // ���ž�� ��ű�( �Ͱ��� ?? )
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 1)) { // 250�� �Ƶ������ȵɶ�
					htmlid = "lowlv24"; // �Ƶ��� x ���� ����
				} else { // �Ƶ����� ������
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1); // 250������
					pc.getInventory().storeItem(420010, 1); // ���ž�� ����
					pc.sendPackets(new S_SystemMessage("���ž�ǱͰ��̸� ������ϴ�."));
					//pc.sendPackets(new S_NpcChatPacket(npc,"���ž�� ������ ǰ�� �����Դϴ�.", 0));
					htmlid = "lowlv23"; // ���ž�Ͱ��� �ٶ� ��Ʈ
				}
				}*/
			/** �ʸ��� */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71038) {
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41060, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				htmlid = "orcfnoname9";
			} else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41060, 1)) {
					htmlid = "orcfnoname11";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71039) {
			if (s.equalsIgnoreCase("teleportURL")) {
				htmlid = "orcfbuwoo2";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71040) {
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41065, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				htmlid = "orcfnoa4";
			} else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41065, 1)) {
					htmlid = "orcfnoa7";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71041) {
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41064, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				htmlid = "orcfhuwoomo4";
			} else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41064, 1)) {
					htmlid = "orcfhuwoomo6";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71042) {
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41062, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				htmlid = "orcfbakumo4";
			} else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41062, 1)) {
					htmlid = "orcfbakumo6";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71043) {
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41063, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				htmlid = "orcfbuka4";
			} else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41063, 1)) {
					htmlid = "orcfbuka6";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71044) {
			if (s.equalsIgnoreCase("A")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(41061, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				htmlid = "orcfkame4";
			} else if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().consumeItem(41061, 1)) {
					htmlid = "orcfkame6";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71078) {
			if (s.equalsIgnoreCase("teleportURL")) {
				htmlid = "usender2";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71080) {
			if (s.equalsIgnoreCase("teleportURL")) {
				htmlid = "amisoo2";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71180) { // ������
			// 49026 ����� ��ȭ
			if (s.equalsIgnoreCase("A")) { // �޲ٴ� ������
				if (pc.getInventory().checkItem(49026, 1000)) {
					pc.getInventory().consumeItem(49026, 1000);
					pc.getInventory().storeItem(41093, 1);
					htmlid = "jp6";
				} else {
					htmlid = "jp5";
				}
			} else if (s.equalsIgnoreCase("B")) { // ���
				if (pc.getInventory().checkItem(49026, 5000)) {
					pc.getInventory().consumeItem(49026, 5000);
					pc.getInventory().storeItem(41094, 1);
					htmlid = "jp6";
				} else {
					htmlid = "jp5";
				}
			} else if (s.equalsIgnoreCase("C")) { // �巹��
				if (pc.getInventory().checkItem(49026, 10000)) {
					pc.getInventory().consumeItem(49026, 10000);
					pc.getInventory().storeItem(41095, 1);
					htmlid = "jp6";
				} else {
					htmlid = "jp5";
				}
			} else if (s.equalsIgnoreCase("D")) { // ����
				if (pc.getInventory().checkItem(49026, 100000)) {
					pc.getInventory().consumeItem(49026, 100000);
					pc.getInventory().storeItem(41095, 1);
					htmlid = "jp6";
				} else {
					htmlid = "jp5";
				}
			} else if (s.equalsIgnoreCase("E")) { // ������
				if (pc.getInventory().checkItem(49026, 1000)) {
					pc.getInventory().consumeItem(49026, 1000);
					pc.getInventory().storeItem(41098, 1);
					htmlid = "jp8";
				} else {
					htmlid = "jp5";
				}
			} else if (s.equalsIgnoreCase("F")) { // ���õ� ����
				if (pc.getInventory().checkItem(49026, 5000)) {
					pc.getInventory().consumeItem(49026, 5000);
					pc.getInventory().storeItem(41099, 1);
					htmlid = "jp8";
				} else {
					htmlid = "jp5";
				}
			} else if (s.equalsIgnoreCase("G")) { // �ְ�� ����
				if (pc.getInventory().checkItem(49026, 10000)) {
					pc.getInventory().consumeItem(49026, 10000);
					pc.getInventory().storeItem(41100, 1);
					htmlid = "jp8";
				} else {
					htmlid = "jp5";
				}
			} else if (s.equalsIgnoreCase("H")) { // �� �� ���� ����
				if (pc.getInventory().checkItem(49026, 100000)) {
					pc.getInventory().consumeItem(49026, 100000);
					pc.getInventory().storeItem(41101, 1);
					htmlid = "jp8";
				} else {
					htmlid = "jp5";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71181) { // ������
			if (s.equalsIgnoreCase("A")) { // ������
				if (pc.getInventory().checkItem(41093)) {
					pc.getInventory().consumeItem(41093, 1);
					pc.getInventory().storeItem(41097, 1);
					htmlid = "my5";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("B")) { // ���
				if (pc.getInventory().checkItem(41094)) {
					pc.getInventory().consumeItem(41094, 1);
					pc.getInventory().storeItem(41097, 1);
					htmlid = "my6";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("C")) { // �巹��
				if (pc.getInventory().checkItem(41095)) {
					pc.getInventory().consumeItem(41095, 1);
					pc.getInventory().storeItem(41097, 1);
					htmlid = "my7";
				} else {
					htmlid = "my4";
				}
			} else if (s.equalsIgnoreCase("D")) { // ����
				if (pc.getInventory().checkItem(41093)) {
					pc.getInventory().consumeItem(41093, 1);
					pc.getInventory().storeItem(41097, 1);
					htmlid = "my8";
				} else {
					htmlid = "my4";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71182) { // ����
			if (s.equalsIgnoreCase("A")) { // ������ ������
				if (pc.getInventory().checkItem(41098)) {
					pc.getInventory().consumeItem(41098, 1);
					pc.getInventory().storeItem(41102, 1);
					htmlid = "sm5";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("B")) { // ���õ� ����
				if (pc.getInventory().checkItem(41099)) {
					pc.getInventory().consumeItem(41099, 1);
					pc.getInventory().storeItem(41102, 1);
					htmlid = "sm6";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("C")) { // �ְ�� ����
				if (pc.getInventory().checkItem(41100)) {
					pc.getInventory().consumeItem(41100, 1);
					pc.getInventory().storeItem(41102, 1);
					htmlid = "sm7";
				} else {
					htmlid = "sm4";
				}
			} else if (s.equalsIgnoreCase("D")) { // �� �� ���� ����
				if (pc.getInventory().checkItem(41101)) {
					pc.getInventory().consumeItem(41101, 1);
					pc.getInventory().storeItem(41102, 1);
					htmlid = "sm8";
				} else {
					htmlid = "sm4";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80048) {
			if (s.equalsIgnoreCase("2")) {
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80049) {
			if (s.equalsIgnoreCase("1")) {
				if (pc.getKarma() <= -10000000) {
					pc.setKarma(1000000);
					pc.sendPackets(new S_ServerMessage(1078));
					htmlid = "betray13";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80050) {
			if (s.equalsIgnoreCase("1")) {
				htmlid = "meet105";
			} else if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(40718)) {
					htmlid = "meet106";
				} else {
					htmlid = "meet110";
				}
			} else if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().consumeItem(40718, 1)) {
					pc.addKarma((int) (-100 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(1079));
					htmlid = "meet107";
				} else {
					htmlid = "meet104";
				}
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().consumeItem(40718, 10)) {
					pc.addKarma((int) (-1000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(1079));
					htmlid = "meet108";
				} else {
					htmlid = "meet104";
				}
			} else if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().consumeItem(40718, 100)) {
					pc.addKarma((int) (-10000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(1079));
					htmlid = "meet109";
				} else {
					htmlid = "meet104";
				}
			} else if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(40615)
						|| pc.getInventory().checkItem(40616)) {
					htmlid = "";
				} else {
					if (pc.getKarmaLevel() <= -1) {
						L1Teleport.teleport(pc, 32683, 32895, (short) 608, 5,
								true);
					}
				}

			}
			pc.sendUhodo();// ��ȣ��ǥ��
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80052) {
			if (s.equalsIgnoreCase("a")) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(
						STATUS_CURSE_YAHEE)) {
					pc.sendPackets(new S_ServerMessage(79));
				} else {
					pc.getSkillEffectTimerSet().setSkillEffect(
							STATUS_CURSE_BARLOG, 1020 * 1000); // 1020
					pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 1,
							1020));
					pc.sendPackets(new S_SkillSound(pc.getId(), 750));
					Broadcaster.broadcastPacket(pc, new S_SkillSound(
							pc.getId(), 750));
					pc.sendPackets(new S_ServerMessage(1127));
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80053) {

			if (s.equalsIgnoreCase("a")) {
				int aliceMaterialId = 0;
				int karmaLevel = 0;
				int[] material = null;
				int[] count = null;
				int createItem = 0;
				String successHtmlId = null;
				String htmlId = null;
				int[] aliceMaterialIdList = { 40991, 196, 197, 198, 199, 200,
						201, 202 };
				int[] karmaLevelList = { -1, -2, -3, -4, -5, -6, -7, -8 };
				int[][] materialsList = { { 40995, 40718, 40991 },
						{ 40997, 40718, 196 }, { 40990, 40718, 197 },
						{ 40994, 40718, 198 }, { 40993, 40718, 199 },
						{ 40998, 40718, 200 }, { 40996, 40718, 201 },
						{ 40992, 40718, 202 } };
				int[][] countList = { { 100, 100, 1 }, { 100, 100, 1 },
						{ 100, 100, 1 }, { 50, 100, 1 }, { 50, 100, 1 },
						{ 50, 100, 1 }, { 10, 100, 1 }, { 10, 100, 1 } };
				int[] createItemList = { 196, 197, 198, 199, 200, 201, 202, 203 };
				String[] successHtmlIdList = { "alice_1", "alice_2", "alice_3",
						"alice_4", "alice_5", "alice_6", "alice_7", "alice_8" };
				String[] htmlIdList = { "aliceyet", "alice_1", "alice_2",
						"alice_3", "alice_4", "alice_5", "alice_5", "alice_7" };

				for (int i = 0; i < aliceMaterialIdList.length; i++) {
					if (pc.getInventory().checkItem(aliceMaterialIdList[i])) {
						aliceMaterialId = aliceMaterialIdList[i];
						karmaLevel = karmaLevelList[i];
						material = materialsList[i];
						count = countList[i];
						createItem = createItemList[i];
						successHtmlId = successHtmlIdList[i];
						htmlId = htmlIdList[i];
						break;
					}
				}
				if (aliceMaterialId == 0) {
					htmlid = "alice_no";

				} else if (aliceMaterialId != 203) {
					if (pc.getKarmaLevel() <= karmaLevel) {
						materials = material;
						counts = count;
						createitem = new int[] { createItem };
						createcount = new int[] { 1 };
						success_htmlid = successHtmlId;
						failure_htmlid = "alice_no";
					} else {
						htmlid = htmlId;
					}
				} else if (aliceMaterialId == 203) {
					htmlid = "alice_8";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71168) {
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(41028)) {
					L1Teleport.teleport(pc, 32648, 32921, (short) 535, 5, true);
					pc.getInventory().consumeItem(41028, 1);
				}
			} else {
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80055) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			htmlid = getYaheeAmulet(pc, npc, s);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80056) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (pc.getKarma() <= -10000000) {
				getBloodCrystalByKarma(pc, npc, s);
			}
			htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80063) {
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(40921)) {
					L1Teleport.teleport(pc, 32674, 32832, (short) 603, 2, true);
				} else {
					htmlid = "gpass02";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80064) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("1")) {
				htmlid = "meet005";
			} else if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(40678)) {
					htmlid = "meet006";
				} else {
					htmlid = "meet010";
				}
			} else if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().consumeItem(40678, 1)) {
					pc.addKarma((int) (100 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(1078));
					htmlid = "meet007";
				} else {
					htmlid = "meet004";
				}
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().consumeItem(40678, 10)) {
					pc.addKarma((int) (1000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(1078));
					htmlid = "meet008";
				} else {
					htmlid = "meet004";
				}
			} else if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().consumeItem(40678, 100)) {
					pc.addKarma((int) (10000 * Config.RATE_KARMA));
					pc.sendPackets(new S_ServerMessage(1078));
					htmlid = "meet009";
				} else {
					htmlid = "meet004";
				}
			} else if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(40921)) {// �������������������߷ϱͰ��̾�Ÿ��
					htmlid = "";
					pc.sendPackets(new S_NpcChatPacket(npc,	"�̳� �������Ⱑ ����� �κ��丮�� ������ �����ڰ� ����������!!", 0));
				} else {
					if (pc.getKarmaLevel() >= 1) {
						L1Teleport.teleport(pc, 32674, 32832, (short) 602, 2, true);
					}
				}
			}
			pc.sendUhodo();// ��ȣ��ǥ��
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80066) {
			if (s.equalsIgnoreCase("1")) {
				if (pc.getKarma() >= 10000000) {
					pc.setKarma(-1000000);
					pc.sendPackets(new S_ServerMessage(1079));
					htmlid = "betray03";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80071) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			htmlid = getBarlogEarring(pc, npc, s);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80073) {

			if (s.equalsIgnoreCase("a")) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(	STATUS_CURSE_BARLOG)) {
					pc.sendPackets(new S_ServerMessage(79));
				} else {
					pc.getSkillEffectTimerSet().setSkillEffect(	STATUS_CURSE_YAHEE, 1020 * 1000);
					pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 2, 1020));
					pc.sendPackets(new S_SkillSound(pc.getId(), 750));
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 750));
					pc.sendPackets(new S_ServerMessage(1127));
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80072) {
			String sEquals = null;
			int karmaLevel = 0;
			int[] material = null;
			int[] count = null;
			int createItem = 0;
			String failureHtmlId = null;
			String htmlId = null;

			String[] sEqualsList = { "0", "1", "2", "3", "4", "5", "6", "7",
					"8", "a", "b", "c", "d", "e", "f", "g", "h" };
			String[] htmlIdList = { "lsmitha", "lsmithb", "lsmithc", "lsmithd",
					"lsmithe", "", "lsmithf", "lsmithg", "lsmithh" };
			int[] karmaLevelList = { 1, 2, 3, 4, 5, 6, 7, 8 };
			int[][] materialsList = { { 20158, 40669, 40678 },
					{ 20144, 40672, 40678 }, { 20075, 40671, 40678 },
					{ 20183, 40674, 40678 }, { 20190, 40674, 40678 },
					{ 20078, 40674, 40678 }, { 20078, 40670, 40678 },
					{ 40719, 40673, 40678 } };
			int[][] countList = { { 1, 50, 100 }, { 1, 50, 100 },
					{ 1, 50, 100 }, { 1, 20, 100 }, { 1, 40, 100 },
					{ 1, 5, 100 }, { 1, 1, 100 }, { 1, 1, 100 } };
			int[] createItemList = { 20083, 20131, 20069, 20179, 20209, 20290,
					20261, 20031 };
			String[] failureHtmlIdList = { "lsmithaa", "lsmithbb", "lsmithcc",
					"lsmithdd", "lsmithee", "lsmithff", "lsmithgg", "lsmithhh" };
			for (int i = 0; i < sEqualsList.length; i++) {
				if (s.equalsIgnoreCase(sEqualsList[i])) {
					sEquals = sEqualsList[i];
					if (i <= 8) {
						htmlId = htmlIdList[i];
					} else if (i > 8) {
						karmaLevel = karmaLevelList[i - 9];
						material = materialsList[i - 9];
						count = countList[i - 9];
						createItem = createItemList[i - 9];
						failureHtmlId = failureHtmlIdList[i - 9];
					}
					break;
				}
			}
			if (s.equalsIgnoreCase(sEquals)) {
				if (karmaLevel != 0 && (pc.getKarmaLevel() >= karmaLevel)) {
					materials = material;
					counts = count;
					createitem = new int[] { createItem };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = failureHtmlId;
				} else {
					htmlid = htmlId;
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80074) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (pc.getKarma() >= 10000000) {
				getSoulCrystalByKarma(pc, npc, s);
			}
			htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80057) {
			htmlid = karmaLevelToHtmlId(pc.getKarmaLevel());
			htmldata = new String[] { String.valueOf(pc.getKarmaPercent()) };
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80059
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80060
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80061
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80062) {
			htmlid = talkToDimensionDoor(pc, (L1NpcInstance) obj, s);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4205000) {
			if (s.equalsIgnoreCase("entertestdg")) {
				L1Teleport.teleport(pc, 32769, 32768, (short) 22, 5, false);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81124) {
			if (s.equalsIgnoreCase("1")) {
				poly(client, 4002);
				htmlid = "";
			} else if (s.equalsIgnoreCase("2")) {
				poly(client, 4004);
				htmlid = "";
			} else if (s.equalsIgnoreCase("3")) {
				poly(client, 4950);
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70534// ����������
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70556
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70572
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70631//���
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70663
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70761
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70788
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70806
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70830
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70876) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("NUL")) {
				if (pc.getInventory().checkItem(40014, 3) && pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
					pc.getInventory().consumeItem(40014, 3);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
					pc.getInventory().storeItem(50014, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"��� �Ǵ� �Ƶ����� ���� �մϴ�.", 0));
				}
			}
			if (s.equalsIgnoreCase("SI")) {
				if (pc.getInventory().checkItem(40068, 3) && pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
					pc.getInventory().consumeItem(40068, 3);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
					pc.getInventory().storeItem(50015, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"��� �Ǵ� �Ƶ����� ���� �մϴ�.", 0));
				}
			}
			if (s.equalsIgnoreCase("RS")) {
				if (pc.getInventory().checkItem(40016, 3)
						&& pc.getInventory().checkItem(L1ItemId.ADENA, 500)) {
					pc.getInventory().consumeItem(40016, 3);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 500);
					pc.getInventory().storeItem(50016, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"��� �Ǵ� �Ƶ����� ���� �մϴ�.", 0));
				}
			}
			if (s.equalsIgnoreCase("-")) {
				if (pc.getInventory().checkItem(40015, 3)
						&& pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
					pc.getInventory().consumeItem(40015, 3);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
					pc.getInventory().storeItem(50017, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"��� �Ǵ� �Ƶ����� ���� �մϴ�.", 0));
				}
			}
			if (s.equalsIgnoreCase("<")) {
				if (pc.getInventory().checkItem(40013, 3)
						&& pc.getInventory().checkItem(L1ItemId.ADENA, 500)) {
					pc.getInventory().consumeItem(40013, 3);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 500);
					pc.getInventory().storeItem(50018, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"��� �Ǵ� �Ƶ����� ���� �մϴ�.", 0));
				}
			}
			if (s.equalsIgnoreCase("K")) {
				if (pc.getInventory().checkItem(40032, 3)
						&& pc.getInventory().checkItem(L1ItemId.ADENA, 500)) {
					pc.getInventory().consumeItem(40032, 3);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 500);
					pc.getInventory().storeItem(50019, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"��� �Ǵ� �Ƶ����� ���� �մϴ�.", 0));
				}
			}
			if (s.equalsIgnoreCase("Z")) {
				if (pc.getInventory().checkItem(40088, 3)
						&& pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
					pc.getInventory().consumeItem(40088, 3);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
					pc.getInventory().storeItem(50022, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,"��� �Ǵ� �Ƶ����� ���� �մϴ�.", 0));
				}
			}
			if (s.equalsIgnoreCase("i")) {//������ �����ȯ�ܼ�
				if (pc.getInventory().checkItem(40308, 400)) {
					pc.getInventory().consumeItem(40308, 400);
					pc.getInventory().storeItem(40081, 1);
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ������� �ŷ��Ϸ� �Ǿ����ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ��� �Ǵ� �Ƶ����� ���� �մϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70512
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71037) {
			if (pc.getLevel() >= 13)
				return;
			if (s.equalsIgnoreCase("fullheal")) {
				pc.setCurrentHp(pc.getMaxHp());
				pc.setCurrentMp(pc.getMaxMp());
				pc.sendPackets(new S_ServerMessage(77));
				pc.sendPackets(new S_SkillSound(pc.getId(), 830));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71030) {
			if (s.equalsIgnoreCase("fullheal")) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 5)) { // check
					pc.getInventory().consumeItem(L1ItemId.ADENA, 5); // del
					pc.setCurrentHp(pc.getMaxHp());
					pc.setCurrentMp(pc.getMaxMp());
					pc.sendPackets(new S_ServerMessage(77));
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					if (pc.isInParty()) { pc.getParty().updateMiniHP(pc);
					}
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$4"));
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71002) {
			if (s.equalsIgnoreCase("0")) {
				if (pc.getLevel() <= 13) {
					L1SkillUse skillUse = new L1SkillUse();
					skillUse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0,
					L1SkillUse.TYPE_NPCBUFF, (L1NpcInstance) obj);
					htmlid = "";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71055) {
			if (s.equalsIgnoreCase("0")) {
				L1ItemInstance item = pc.getInventory().storeItem(40701, 1);
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 1);
				htmlid = "lukein8";
			}
			if (s.equalsIgnoreCase("1")) {
				pc.getQuest().set_end(L1Quest.QUEST_TBOX3);
				materials = new int[] { 40716 }; // �Ҿƹ����� ����
				counts = new int[] { 1 };
				createitem = new int[] { 20269 }; // �ذ�����
				createcount = new int[] { 1 };
				htmlid = "lukein0";
			} else if (s.equalsIgnoreCase("2")) {
				htmlid = "lukein12";
				pc.getQuest().set_step(L1Quest.QUEST_RESTA, 3);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71063) {
			if (s.equalsIgnoreCase("0")) {
				materials = new int[] { 40701 };
				counts = new int[] { 1 };
				createitem = new int[] { 40702 };
				createcount = new int[] { 1 };
				htmlid = "maptbox1";
				pc.getQuest().set_end(L1Quest.QUEST_TBOX1);
				int[] nextbox = { 1, 2, 3 };
				int pid = _random.nextInt(nextbox.length);
				int nb = nextbox[pid];
				if (nb == 1) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 2);
				} else if (nb == 2) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 3);
				} else if (nb == 3) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 4);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71064
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71065
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71066) {
			if (s.equalsIgnoreCase("0")) {
				materials = new int[] { 40701 };
				counts = new int[] { 1 };
				createitem = new int[] { 40702 };
				createcount = new int[] { 1 };
				htmlid = "maptbox1";
				pc.getQuest().set_end(L1Quest.QUEST_TBOX2);
				int[] nextbox2 = { 1, 2, 3, 4, 5, 6 };
				int pid = _random.nextInt(nextbox2.length);
				int nb2 = nextbox2[pid];
				if (nb2 == 1) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 5);
				} else if (nb2 == 2) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 6);
				} else if (nb2 == 3) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 7);
				} else if (nb2 == 4) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 8);
				} else if (nb2 == 5) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 9);
				} else if (nb2 == 6) {
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 10);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71067
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71068
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71069
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71070
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71071
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71072) { // ����
			// ����-3��°
			if (s.equalsIgnoreCase("0")) {
				htmlid = "maptboxi";
				materials = new int[] { 40701 }; // ���� ������ ����
				counts = new int[] { 1 };
				createitem = new int[] { 40716 }; // �Ҿƹ����� ����
				createcount = new int[] { 1 };
				pc.getQuest().set_end(L1Quest.QUEST_TBOX3);
				pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 11);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71056) { // �ù���(������)
			// �Ƶ��� ã�´�
			if (s.equalsIgnoreCase("a")) {
				pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 1);
				htmlid = "simizz7";
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(40661)
						&& pc.getInventory().checkItem(40662)
						&& pc.getInventory().checkItem(40663)) {
					htmlid = "simizz8";
					pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 2);
					materials = new int[] { 40661, 40662, 40663 };
					counts = new int[] { 1, 1, 1 };
					createitem = new int[] { 20044 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "simizz9";
				}
			} else if (s.equalsIgnoreCase("d")) {
				htmlid = "simizz12";
				pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, L1Quest.QUEST_END);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71057) { // ����(������)
			// ������ ���� ��´�
			if (s.equalsIgnoreCase("3")) {
				htmlid = "doil4";
			} else if (s.equalsIgnoreCase("6")) {
				htmlid = "doil6";
			} else if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(40714)) {
					htmlid = "doil8";
					materials = new int[] { 40714 };
					counts = new int[] { 1 };
					createitem = new int[] { 40647 };
					createcount = new int[] { 1 };
					pc.getQuest().set_step(L1Quest.QUEST_DOIL,
							L1Quest.QUEST_END);
				} else {
					htmlid = "doil7";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71059) { // ���
			// ��(������)
			// ��� ���� ��Ź�� �޾Ƶ��δ�
			if (s.equalsIgnoreCase("A")) {
				htmlid = "rudian6";
				L1ItemInstance item = pc.getInventory().storeItem(40700, 1);
				pc.sendPackets(new S_ServerMessage(143,	((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				pc.getQuest().set_step(L1Quest.QUEST_RUDIAN, 1);
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40710)) {
					htmlid = "rudian8";
					materials = new int[] { 40700, 40710 };
					counts = new int[] { 1, 1 };
					createitem = new int[] { 40647 };
					createcount = new int[] { 1 };
					pc.getQuest().set_step(L1Quest.QUEST_RUDIAN,
							L1Quest.QUEST_END);
				} else {
					htmlid = "rudian9";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71060) { // ����Ÿ(������)
			// ����鿡 ����
			if (s.equalsIgnoreCase("A")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_RUDIAN) == L1Quest.QUEST_END) {
					htmlid = "resta6";
				} else {
					htmlid = "resta4";
				}
			} else if (s.equalsIgnoreCase("B")) {
				htmlid = "resta10";
				pc.getQuest().set_step(L1Quest.QUEST_RESTA, 2);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71061) { // ī����(������)
			// ������ ������ �ּ���
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40647, 3)) {
					htmlid = "cadmus6";
					pc.getInventory().consumeItem(40647, 3);
					pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 2);
				} else {
					htmlid = "cadmus5";
					pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 1);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71062) { // ī��Ʈ(������)
			// �Ҿƹ����� ��ٸ��� ������ �Բ� ������
			if (s.equalsIgnoreCase("start")) {
				htmlid = "kamit2";
				final int[] item_ids = { 40711 };
				final int[] item_amounts = { 1 };
				for (int i = 0; i < item_ids.length; i++) {
					L1ItemInstance item = pc.getInventory().storeItem(
							item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143,
							((L1NpcInstance) obj).getNpcTemplate().get_name(),
							item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 3);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71036) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "kamyla7";
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 1);
			} else if (s.equalsIgnoreCase("c")) {
				htmlid = "kamyla10";
				pc.getInventory().consumeItem(40644, 1);
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 3);
			} else if (s.equalsIgnoreCase("e")) {
				htmlid = "kamyla13";
				pc.getInventory().consumeItem(40630, 1);
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 4);
			} else if (s.equalsIgnoreCase("i")) {
				htmlid = "kamyla25";
			} else if (s.equalsIgnoreCase("b")) { // ī �̶�(������� �̱�)
				if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 1) {
					L1Teleport.teleport(pc, 32679, 32742, (short) 482, 5, true);
				}
			} else if (s.equalsIgnoreCase("d")) { // ī �̶�(�𿡰� ���� ��)
				if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 3) {
					L1Teleport.teleport(pc, 32736, 32800, (short) 483, 5, true);
				}
			} else if (s.equalsIgnoreCase("f")) { // ī �̶�(ȣ�� ���ϼұ�)
				if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 4) {
					L1Teleport.teleport(pc, 32746, 32807, (short) 484, 5, true);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71089) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "francu10";
				L1ItemInstance item = pc.getInventory().storeItem(40644, 1);
				pc.sendPackets(new S_ServerMessage(143,	((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 2);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71090) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "";
				final int[] item_ids = { 246, 247, 248, 249, 40660 };
				final int[] item_amounts = { 1, 1, 1, 1, 5 };
				L1ItemInstance item = null;
				for (int i = 0; i < item_ids.length; i++) {
					item = pc.getInventory().storeItem(item_ids[i],
							item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143,
							((L1NpcInstance) obj).getNpcTemplate().get_name(),
							item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 1);
				}
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkEquipped(246)
						|| pc.getInventory().checkEquipped(247)
						|| pc.getInventory().checkEquipped(248)
						|| pc.getInventory().checkEquipped(249)) {
					htmlid = "jcrystal5";
				} else if (pc.getInventory().checkItem(40660)) {
					htmlid = "jcrystal4";
				} else {
					pc.getInventory().consumeItem(246, 1);
					pc.getInventory().consumeItem(247, 1);
					pc.getInventory().consumeItem(248, 1);
					pc.getInventory().consumeItem(249, 1);
					pc.getInventory().consumeItem(40620, 1);
					pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 2);
					L1Teleport.teleport(pc, 32801, 32895, (short) 483, 4, true);
				}
			} else if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().checkEquipped(246)
						|| pc.getInventory().checkEquipped(247)
						|| pc.getInventory().checkEquipped(248)
						|| pc.getInventory().checkEquipped(249)) {
					htmlid = "jcrystal5";
				} else {
					pc.getInventory().checkItem(40660);
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40660);
					int sc = l1iteminstance.getCount();
					if (sc > 0) {
						pc.getInventory().consumeItem(40660, sc);
					}
					pc.getInventory().consumeItem(246, 1);
					pc.getInventory().consumeItem(247, 1);
					pc.getInventory().consumeItem(248, 1);
					pc.getInventory().consumeItem(249, 1);
					pc.getInventory().consumeItem(40620, 1);
					pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 0);
					L1Teleport.teleport(pc, 32736, 32800, (short) 483, 4, true);
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71091) {
			if (s.equalsIgnoreCase("a")) {
				htmlid = "";
				pc.getInventory().consumeItem(40654, 1);
				pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, L1Quest.QUEST_END);
				L1Teleport.teleport(pc, 32744, 32927, (short) 483, 4, true);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71074) {
			if (s.equalsIgnoreCase("A")) {
				htmlid = "lelder5";
				pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 1);
			} else if (s.equalsIgnoreCase("B")) {
				htmlid = "lelder10";
				pc.getInventory().consumeItem(40633, 1);
				pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 3);
			} else if (s.equalsIgnoreCase("C")) {
				htmlid = "lelder13";
				if (pc.getQuest().get_step(L1Quest.QUEST_LIZARD) == L1Quest.QUEST_END) {
				}
				materials = new int[] { 40634 };
				counts = new int[] { 1 };
				createitem = new int[] { 20167 }; // ���ڵ���κ�
				createcount = new int[] { 1 };
				pc.getQuest().set_step(L1Quest.QUEST_LIZARD, L1Quest.QUEST_END);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71198) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getQuest().get_step(71198) != 0
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41339, 5)) {
					L1ItemInstance item = ItemTable.getInstance().createItem(
							41340);
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(143,
									((L1NpcInstance) obj).getNpcTemplate()
											.get_name(), item.getItem()
											.getName()));
						}
					}
					pc.getQuest().set_step(71198, 1);
					htmlid = "tion4";
				} else {
					htmlid = "tion9";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getQuest().get_step(71198) != 1
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41341, 1)) {
					pc.getQuest().set_step(71198, 2);
					htmlid = "tion5";
				} else {
					htmlid = "tion10";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getQuest().get_step(71198) != 2
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41343, 1)) {
					L1ItemInstance item = ItemTable.getInstance().createItem(
							21057);
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(143,
									((L1NpcInstance) obj).getNpcTemplate()
											.get_name(), item.getItem()
											.getName()));
						}
					}
					pc.getQuest().set_step(71198, 3);
					htmlid = "tion6";
				} else {
					htmlid = "tion12";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getQuest().get_step(71198) != 3
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41344, 1)) {
					L1ItemInstance item = ItemTable.getInstance().createItem(
							21058);
					if (item != null) {
						pc.getInventory().consumeItem(21057, 1);
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(143,
									((L1NpcInstance) obj).getNpcTemplate()
											.get_name(), item.getItem()
											.getName()));
						}
					}
					pc.getQuest().set_step(71198, 4);
					htmlid = "tion7";
				} else {
					htmlid = "tion13";
				}
			} else if (s.equalsIgnoreCase("E")) {
				if (pc.getQuest().get_step(71198) != 4
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41345, 1)) {
					L1ItemInstance item = ItemTable.getInstance().createItem(
							21059);
					if (item != null) {
						pc.getInventory().consumeItem(21058, 1);
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(143,
									((L1NpcInstance) obj).getNpcTemplate()
											.get_name(), item.getItem()
											.getName()));
						}
					}
					pc.getQuest().set_step(71198, 0);
					pc.getQuest().set_step(71199, 0);
					htmlid = "tion8";
				} else {
					htmlid = "tion15";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71199) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getQuest().get_step(71199) != 0
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().checkItem(41340, 1)) {
					pc.getQuest().set_step(71199, 1);
					htmlid = "jeron2";
				} else {
					htmlid = "jeron10";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getQuest().get_step(71199) != 1
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(L1ItemId.ADENA, 1000000)) {
					L1ItemInstance item = ItemTable.getInstance().createItem(
							41341);
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(143,
									((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
					}
					pc.getInventory().consumeItem(41340, 1);
					pc.getQuest().set_step(71199, 255);
					htmlid = "jeron6";
				} else {
					htmlid = "jeron8";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getQuest().get_step(71199) != 1
						|| pc.getInventory().checkItem(21059, 1)) {
					return;
				}
				if (pc.getInventory().consumeItem(41342, 1)) {
					L1ItemInstance item = ItemTable.getInstance().createItem(
							41341);
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, 1) == 0) {
							pc.getInventory().storeItem(item);
							pc.sendPackets(new S_ServerMessage(143,
									((L1NpcInstance) obj).getNpcTemplate()
											.get_name(), item.getItem()
											.getName()));
						}
					}
					pc.getInventory().consumeItem(41340, 1);
					pc.getQuest().set_step(71199, 255);
					htmlid = "jeron5";
				} else {
					htmlid = "jeron9";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80079) {
			if (s.equalsIgnoreCase("0")) {
				if (!pc.getInventory().checkItem(41312)) {
					L1ItemInstance item = pc.getInventory().storeItem(41312, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(143,
								((L1NpcInstance) obj).getNpcTemplate()
										.get_name(), item.getItem().getName()));
						pc.getQuest().set_step(L1Quest.QUEST_KEPLISHA,
								L1Quest.QUEST_END);
					}
					htmlid = "keplisha7";
				}
			} else if (s.equalsIgnoreCase("1")) {
				if (!pc.getInventory().checkItem(41314)) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
						materials = new int[] { L1ItemId.ADENA, 41313 };
						counts = new int[] { 1000, 1 };
						createitem = new int[] { 41314 };
						createcount = new int[] { 1 };
						int htmlA = _random.nextInt(3) + 1;
						int htmlB = _random.nextInt(100) + 1;
						if (htmlA == 1)
							htmlid = "horosa" + htmlB; // horosa1 ~
						else if (htmlA == 2)
							htmlid = "horosb" + htmlB; // horosb1
	
						else if (htmlA == 3)
							htmlid = "horosc" + htmlB; // horosc1
					} else {
						htmlid = "keplisha8";
					}
				}
			} else if (s.equalsIgnoreCase("2")) {
				if (pc.getGfxId().getTempCharGfx() != pc.getClassId()) {
					htmlid = "keplisha9";
				} else {
					if (pc.getInventory().checkItem(41314)) {
						pc.getInventory().consumeItem(41314, 1);
						int html = _random.nextInt(9) + 1;
						int PolyId = 6180 + _random.nextInt(64);
						polyByKeplisha(client, PolyId);
						switch (html) {
						case 1:
							htmlid = "horomon11";
							break;
						case 2:
							htmlid = "horomon12";
							break;
						case 3:
							htmlid = "horomon13";
							break;
						case 4:
							htmlid = "horomon21";
							break;
						case 5:
							htmlid = "horomon22";
							break;
						case 6:
							htmlid = "horomon23";
							break;
						case 7:
							htmlid = "horomon31";
							break;
						case 8:
							htmlid = "horomon32";
							break;
						case 9:
							htmlid = "horomon33";
							break;
						default:
							break;
						}
					}
				}
			} else if (s.equalsIgnoreCase("3")) {
				if (pc.getInventory().checkItem(41312)) {
					pc.getInventory().consumeItem(41312, 1);
					htmlid = "";
				}
				if (pc.getInventory().checkItem(41313)) {
					pc.getInventory().consumeItem(41313, 1);
					htmlid = "";
				}
				if (pc.getInventory().checkItem(41314)) {
					pc.getInventory().consumeItem(41314, 1);
					htmlid = "";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80082) { // ���ò���(IN)
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(41293, 1)) {
					L1PolyMorph.undoPoly(pc);
					L1Teleport.teleport(pc, 32806, 32811, (short) 5302, 6, true);
					htmlid = "fk_in_3";// ���� ���ÿ� ���� �������ٷ�?
				} else {
					pc.sendPackets(new S_NpcChatPacket(npc,	"���ø� �Ϸ��� ���� ���ô밡 �ʿ��մϴ�.", 0));

				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80083) { // ���ò���(OUT)
			if (s.equalsIgnoreCase("teleportURL")) {// �׸��ΰ� ������
				int mapId = pc.getMapId();
				switch (mapId) {
				case 5302:
					break;
				}
				L1Teleport.teleport(pc, 32613, 32781, (short) 4, 4, true);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80084) {
			if (s.equalsIgnoreCase("q")) {
				if (pc.getInventory().checkItem(41356, 1)) {
					htmlid = "rparum4";
				} else {
					L1ItemInstance item = pc.getInventory().storeItem(41356, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(143,
								((L1NpcInstance) obj).getNpcTemplate()
										.get_name(), item.getItem().getName()));
					}
					htmlid = "rparum3";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80105) {
			if (s.equalsIgnoreCase("c")) {
				if (pc.isCrown()) {
					if (pc.getInventory().checkItem(20383, 1)) {
						if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000)) {
							L1ItemInstance item = pc.getInventory().findItemId(20383);
							if (item != null && item.getChargeCount() != 50) {
								item.setChargeCount(50);
								pc.getInventory().updateItem(item,
										L1PcInventory.COL_CHARGE_COUNT);
								pc.getInventory().consumeItem(L1ItemId.ADENA,
										100000);
								htmlid = "";
							}
						} else {
							pc.sendPackets(new S_ServerMessage(337, "$4"));
						}
					}
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70702) {
			if (s.equalsIgnoreCase("chg")) {
				if (pc.getPartnerId() != 0) {
					for (L1PcInstance partner : L1World.getInstance()
							.getVisiblePlayer(pc, 3)) {
						if (partner.getId() == pc.getPartnerId()) {
							break;
						}
						return;
					}
					if (pc.getInventory().checkItem(40903)
							|| pc.getInventory().checkItem(40904)
							|| pc.getInventory().checkItem(40905)
							|| pc.getInventory().checkItem(40906)
							|| pc.getInventory().checkItem(40907)
							|| pc.getInventory().checkItem(40908)) {
						if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
							int chargeCount = 0;
							for (int itemId = 40903; itemId <= 40908; itemId++) {
								L1ItemInstance item = pc.getInventory()
										.findItemId(itemId);
								if (itemId == 40903 || itemId == 40904
										|| itemId == 40905) {
									chargeCount = itemId - 40902;
								}
								if (itemId == 40906) {
									chargeCount = 5;
								}
								if (itemId == 40907 || itemId == 40908) {
									chargeCount = 20;
								}
								if (item != null
										&& item.getChargeCount() != chargeCount) {
									item.setChargeCount(chargeCount);
									pc.getInventory().updateItem(item,
											L1PcInventory.COL_CHARGE_COUNT);
									pc.getInventory().consumeItem(
											L1ItemId.ADENA, 1000);
									htmlid = "";
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(337, "$4"));
						}
					}
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4202000) { // ����
			// �ǿ���
			if (s.equalsIgnoreCase("teleportURL") && pc.isDragonknight()) {
				htmlid = "feaena3";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4201000) { // ȯ����
			// �ƻ�
			if (s.equalsIgnoreCase("teleportURL") && pc.isIllusionist()) {
				htmlid = "asha3";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4206001) {
			if (s.equalsIgnoreCase("0")) {
				if (pc.getInventory().checkItem(L1ItemId.REMINISCING_CANDLE)) {
					htmlid = "candleg3";
				} else {
					pc.getInventory().storeItem(L1ItemId.REMINISCING_CANDLE, 1);
					htmlid = "candleg2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200003
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200007) {
			if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(L1ItemId.TIMECRACK_BROKENPIECE)) {
					pc.getInventory().consumeItem(
							L1ItemId.TIMECRACK_BROKENPIECE, 1);
					L1Teleport.teleport(pc, 33970, 33246, (short) 4, 4, true);
				} else {
					htmlid = "joegolem20";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4218003) { // ���
			// ������(������)
			if (s.equalsIgnoreCase("a")) {
				htmlid = "prokel3";
				pc.getInventory().storeItem(210087, 1);
				pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1);
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(210088)
						|| pc.getInventory().checkItem(210089)
						|| pc.getInventory().checkItem(210090)) {
					htmlid = "prokel5";
					pc.getInventory().consumeItem(210088, 1);
					pc.getInventory().consumeItem(210089, 1);
					pc.getInventory().consumeItem(210090, 1);
					pc.getInventory().storeItem(410002, 1);
					pc.getInventory().storeItem(
							L1ItemId.DRAGONKNIGHTTABLET_DRAGONSKIN, 1);
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 255);
				} else {
					htmlid = "prokel6";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4219004) { // �Ǻ�����
			// �Ƿ���(����Ʈ)
			if (s.equalsIgnoreCase("a")) {
				htmlid = "silrein4";
				pc.getInventory().storeItem(210092, 5);
				pc.getInventory().storeItem(210093, 1);
				pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1);
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(210091, 10)
						|| pc.getInventory().checkItem(40510)
						|| pc.getInventory().checkItem(40511)
						|| pc.getInventory().checkItem(40512)
						|| pc.getInventory().checkItem(41080)) {
					htmlid = "silrein7";
					pc.getInventory().consumeItem(210091, 10);
					pc.getInventory().consumeItem(40510, 1);
					pc.getInventory().consumeItem(40511, 1);
					pc.getInventory().consumeItem(40512, 1);
					pc.getInventory().consumeItem(41080, 1);
					pc.getInventory().storeItem(410005, 1);
					pc.getInventory().storeItem(
							L1ItemId.MEMORIALCRYSTAL_CUBE_IGNITION, 1);
					pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 255);
				} else {
					htmlid = "silrein8";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777849) { // ų��
			// (ȣ����)
			if (s.equalsIgnoreCase("0")) { // ų���� ��༭
				if (pc.getInventory().checkItem(40308, 500000)) {
					pc.getInventory().consumeItem(40308, 500000);
					pc.getInventory().storeItem(87050, 1);
					pc.sendPackets(new S_SystemMessage("ų���� ��ſ��� ų���� ��༭�� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "killton3"; // �Ƶ��� ������
				}
			}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777848) { // �޸�
			// (������)
			if (s.equalsIgnoreCase("0")) { // �޸��� ��༭
				if (pc.getInventory().checkItem(40308, 500000)) {
					pc.getInventory().consumeItem(40308, 500000);
					pc.getInventory().storeItem(87051, 1);
					pc.sendPackets(new S_SystemMessage("�޸��� ��ſ��� �޸��� ��༭�� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "merin3"; // �Ƶ��� ������
				}
			}

			/** ���� ��� �ý��� * */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777846) {
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(40308, 3000)) {
					pc.getInventory().consumeItem(40308, 3000);
					pc.getInventory().storeItem(L1ItemId.MOPO_JUiCE_CONTRACT1,
							1);
					pc.sendPackets(new S_SystemMessage("Ư�깰 ������ ������ ��ſ��� ������ ��༭:�ֽ� 100���� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "mopo5";
				}
			}
			if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(40308, 6000)) {
					pc.getInventory().consumeItem(40308, 6000);
					pc.getInventory().storeItem(L1ItemId.MOPO_JUiCE_CONTRACT2,
							1);
					pc.sendPackets(new S_SystemMessage("Ư�깰 ������ ������ ��ſ��� ������ ��༭:�ֽ� 200���� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "mopo5";
				}
			}
			if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().checkItem(40308, 9000)) {
					pc.getInventory().consumeItem(40308, 9000);
					pc.getInventory().storeItem(L1ItemId.MOPO_JUiCE_CONTRACT3,
							1);
					pc.sendPackets(new S_SystemMessage("Ư�깰 ������ ������ ��ſ��� ������ ��༭:�ֽ� 300���� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "mopo5";
				}
			}
			if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(40308, 3000)) {
					pc.getInventory().consumeItem(40308, 3000);
					pc.getInventory().storeItem(L1ItemId.MOPO_JUiCE_CONTRACT4,
							1);
					pc.sendPackets(new S_SystemMessage("Ư�깰 ������ ������ ��ſ��� ������ ��༭:���� 100���� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "mopo5";
				}
			}
			if (s.equalsIgnoreCase("e")) {
				if (pc.getInventory().checkItem(40308, 6000)) {
					pc.getInventory().consumeItem(40308, 6000);
					pc.getInventory().storeItem(L1ItemId.MOPO_JUiCE_CONTRACT5,
							1);
					pc.sendPackets(new S_SystemMessage("Ư�깰 ������ ������ ��ſ��� ������ ��༭:���� 200���� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "mopo5";
				}
			}
			if (s.equalsIgnoreCase("f")) {
				if (pc.getInventory().checkItem(40308, 9000)) {
					pc.getInventory().consumeItem(40308, 9000);
					pc.getInventory().storeItem(L1ItemId.MOPO_JUiCE_CONTRACT6,
							1);
					pc.sendPackets(new S_SystemMessage("Ư�깰 ������ ������ ��ſ��� ������ ��༭:���� 300���� �־����ϴ�."));
					htmlid = "";
				} else {
					htmlid = "mopo5";
				}
			}			

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70077) {// �ε��
			if (s.equalsIgnoreCase("buy 1")) {
				petbuy(client, 45042, L1ItemId.ADENA, 50000);
			} else if (s.equalsIgnoreCase("buy 2")) {
				petbuy(client, 45034, L1ItemId.ADENA, 50000);
			} else if (s.equalsIgnoreCase("buy 3")) {
				petbuy(client, 45046, L1ItemId.ADENA, 50000);
			} else if (s.equalsIgnoreCase("buy 4")) {
				petbuy(client, 45047, L1ItemId.ADENA, 50000);
			}
			htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4220011) {
			if (s.equalsIgnoreCase("buy 5")) {
				petbuy(client, 46044, 41159, 1000);
				htmlid = "subsusp3";
			} else if (s.equalsIgnoreCase("buy 6")) {
				petbuy(client, 46042, 41159, 1000);
				htmlid = "subsusp4";
			}
			/** ���� ������  * */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40150) {
			if (s.equalsIgnoreCase("J")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 2400);
					L1Teleport.teleport(pc, 33769, 32861, (short) 106, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40104, 2)) {
					pc.getInventory().consumeItem(40104, 2);
					L1Teleport.teleport(pc, 32744, 32861, (short) 116, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40105, 2)) {
					pc.getInventory().consumeItem(40105, 2);
					L1Teleport.teleport(pc, 32744, 32861, (short) 126, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(40106, 2)) {
					pc.getInventory().consumeItem(40106, 2);
					L1Teleport.teleport(pc, 32744, 32861, (short) 136, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(40107, 2)) {
					pc.getInventory().consumeItem(40107, 2);
					L1Teleport.teleport(pc, 32744, 32861, (short) 146, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("E")) {
				if (pc.getInventory().checkItem(40108, 2)) {
					pc.getInventory().consumeItem(40108, 2);
					L1Teleport.teleport(pc, 32735, 32800, (short) 156, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("F")) {
				if (pc.getInventory().checkItem(40109, 2)) {
					pc.getInventory().consumeItem(40109, 2);
					L1Teleport.teleport(pc, 32738, 32796, (short) 166, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob1";
				}
			} else if (s.equalsIgnoreCase("G")) {
				if (pc.getInventory().checkItem(40110, 2)) {
					pc.getInventory().consumeItem(40110, 2);
					L1Teleport.teleport(pc, 32738, 32796, (short) 176, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("H")) {
				if (pc.getInventory().checkItem(40111, 2)) {
					pc.getInventory().consumeItem(40111, 2);
					L1Teleport.teleport(pc, 32738, 32796, (short) 186, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			} else if (s.equalsIgnoreCase("I")) {
				if (pc.getInventory().checkItem(40112, 2)) {
					pc.getInventory().consumeItem(40112, 2);
					L1Teleport.teleport(pc, 32738, 32796, (short) 196, 5, true);
					htmlid = "";
				} else {
					htmlid = "maetnob2";
				}
			}
			/** ���� ������  * */
			/** ��������� �Ҳ� * */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40151) {// 1��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 2400);
					L1Teleport.teleport(pc, 32800, 32800, (short) 110, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32796, 32798, (short) 101, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40152) {// 10��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40104, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40104, 2);
					L1Teleport.teleport(pc, 32800, 32800, (short) 120, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32631, 32935, (short) 111, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40153) {// 20��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40105, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40105, 2);
					L1Teleport.teleport(pc, 32800, 32800, (short) 130, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32631, 32935, (short) 121, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40154) {// 30��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40106, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40106, 2);
					L1Teleport.teleport(pc, 32800, 32800, (short) 140, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32631, 32935, (short) 131, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40155) {// 40��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40107, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40107, 2);
					L1Teleport.teleport(pc, 32796, 32796, (short) 150, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32631, 32935, (short) 141, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40160) {// 50��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40108, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40108, 2);
					L1Teleport.teleport(pc, 32720, 32821, (short) 160, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32631, 32935, (short) 151, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40156) {// 60��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40109, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40109, 2);
					L1Teleport.teleport(pc, 32720, 32821, (short) 170, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32669, 32814, (short) 161, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40157) {// 70��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40110, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40110, 2);
					L1Teleport.teleport(pc, 32724, 32822, (short) 180, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32669, 32814, (short) 171, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40158) {// 80��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40111, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40111, 2);
					L1Teleport.teleport(pc, 32722, 32827, (short) 190, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32669, 32814, (short) 181, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40159) {// 90��
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308)
						&& pc.getInventory().checkItem(40112, 2)) {
					pc.getInventory().consumeItem(40308, 300);
					pc.getInventory().consumeItem(40112, 2);
					L1Teleport.teleport(pc, 32731, 32856, (short) 200, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308)) {
					pc.getInventory().consumeItem(40308, 300);
					L1Teleport.teleport(pc, 32669, 32814, (short) 191, 5, true);
					htmlid = "";
				} else {
					htmlid = "omantel2";
				}
			}
			/** ��������� �Ҳ� * */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4309003) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (pc.getLevel() < 30) {
				htmlid = "sharna4";
			} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 2500)) {
				int itemid = 0;
				if (pc.getLevel() >= 30 && pc.getLevel() < 40) {
					itemid = L1ItemId.SHARNA_POLYSCROLL_LV30;
				} else if (pc.getLevel() >= 40 && pc.getLevel() < 52) {
					itemid = L1ItemId.SHARNA_POLYSCROLL_LV40;
				} else if (pc.getLevel() >= 52 && pc.getLevel() < 55) {
					itemid = L1ItemId.SHARNA_POLYSCROLL_LV52;
				} else if (pc.getLevel() >= 55 && pc.getLevel() < 60) {
					itemid = L1ItemId.SHARNA_POLYSCROLL_LV55;
				} else if (pc.getLevel() >= 60 && pc.getLevel() < 65) {
					itemid = L1ItemId.SHARNA_POLYSCROLL_LV60;
				} else if (pc.getLevel() >= 65 && pc.getLevel() < 70) {
					itemid = L1ItemId.SHARNA_POLYSCROLL_LV65;
				} else if (pc.getLevel() >= 70) {
					itemid = L1ItemId.SHARNA_POLYSCROLL_LV70;
				}
				pc.getInventory().consumeItem(L1ItemId.ADENA, 2500);
				L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
				String npcName = npc.getNpcTemplate().get_name();
				String itemName = item.getItem().getName();
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				htmlid = "sharna3";
			} else {
				htmlid = "sharna5";
			}
	
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80076) { // �Ѿ���
			// ���ػ�
			if (s.equalsIgnoreCase("A")) {
				int[] diaryno = { 49082, 49083 };
				int pid = _random.nextInt(diaryno.length);
				int di = diaryno[pid];
				if (di == 49082) { // Ȧ�� ������ �̾ƶ�
					htmlid = "voyager6a";
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(di, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				} else if (di == 49083) { // ¦�� ������ �̾ƶ�
					htmlid = "voyager6b";
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(di, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71128) { // ����
			// ����
			// �丮Ÿ
			if (s.equals("A")) {
				if (pc.getInventory().checkItem(41010, 1)) { // �̸����� ��õ��
					htmlid = "perita2";
				} else {
					htmlid = "perita3";
				}
			} else if (s.equals("p")) {
				// �������� �� �� �� �Ǻ�
				if (pc.getInventory().checkItem(40987, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(40988, 1) // ����Ʈ Ŭ����
						&& pc.getInventory().checkItem(40989, 1)) { // ������ũ��
					htmlid = "perita43";
				} else if (pc.getInventory().checkItem(40987, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(40989, 1)) { // ������ũ��
					htmlid = "perita44";
				} else if (pc.getInventory().checkItem(40987, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(40988, 1)) { // ����Ʈ Ŭ����
					htmlid = "perita45";
				} else if (pc.getInventory().checkItem(40988, 1) // ����Ʈ Ŭ����
						&& pc.getInventory().checkItem(40989, 1)) { // ������ũ��
					htmlid = "perita47";
				} else if (pc.getInventory().checkItem(40987, 1)) { // ������ Ŭ����
					htmlid = "perita46";
				} else if (pc.getInventory().checkItem(40988, 1)) { // ����Ʈ Ŭ����
					htmlid = "perita49";
				} else if (pc.getInventory().checkItem(40987, 1)) { // ������ũ��
					htmlid = "perita48";
				} else {
					htmlid = "perita50";
				}
			} else if (s.equals("q")) {
				// �� �� �� �Ǻ�
				if (pc.getInventory().checkItem(41173, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(41174, 1) // ����Ʈ Ŭ����
						&& pc.getInventory().checkItem(41175, 1)) { // ������ũ��
					htmlid = "perita54";
				} else if (pc.getInventory().checkItem(41173, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(41175, 1)) { // ������ũ��
					htmlid = "perita55";
				} else if (pc.getInventory().checkItem(41173, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(41174, 1)) { // ����Ʈ Ŭ����
					htmlid = "perita56";
				} else if (pc.getInventory().checkItem(41174, 1) // ����Ʈ Ŭ����
						&& pc.getInventory().checkItem(41175, 1)) { // ������ũ��
					htmlid = "perita58";
				} else if (pc.getInventory().checkItem(41174, 1)) { // ������ Ŭ����
					htmlid = "perita57";
				} else if (pc.getInventory().checkItem(41175, 1)) { // ����Ʈ Ŭ����
					htmlid = "perita60";
				} else if (pc.getInventory().checkItem(41176, 1)) { // ������ũ��
					htmlid = "perita59";
				} else {
					htmlid = "perita61";
				}
			} else if (s.equals("s")) {
				// �ź����� �� �� �� �Ǻ�
				if (pc.getInventory().checkItem(41161, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(41162, 1) // ����Ʈ Ŭ����
						&& pc.getInventory().checkItem(41163, 1)) { // ������ũ��
					htmlid = "perita62";
				} else if (pc.getInventory().checkItem(41161, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(41163, 1)) { // ������ũ��
					htmlid = "perita63";
				} else if (pc.getInventory().checkItem(41161, 1) // ������ Ŭ����
						&& pc.getInventory().checkItem(41162, 1)) { // ����Ʈ Ŭ����
					htmlid = "perita64";
				} else if (pc.getInventory().checkItem(41162, 1) // ����Ʈ Ŭ����
						&& pc.getInventory().checkItem(41163, 1)) { // ������ũ��
					htmlid = "perita66";
				} else if (pc.getInventory().checkItem(41161, 1)) { // ������ Ŭ����
					htmlid = "perita65";
				} else if (pc.getInventory().checkItem(41162, 1)) { // ����Ʈ Ŭ����
					htmlid = "perita68";
				} else if (pc.getInventory().checkItem(41163, 1)) { // ������ũ��
					htmlid = "perita67";
				} else {
					htmlid = "perita69";
				}
			} else if (s.equals("B")) {
				// ��ȭ�� �Ϻ�
				if (pc.getInventory().checkItem(40651, 10) // ���� ����
						&& pc.getInventory().checkItem(40643, 10) // ���� ����
						&& pc.getInventory().checkItem(40618, 10) // ������ ����
						&& pc.getInventory().checkItem(40645, 10) // ��ǳ�� ���� ��
						&& pc.getInventory().checkItem(40676, 10) // ����� ����
						&& pc.getInventory().checkItem(40442, 5) // ���Ӻ��� ����
						&& pc.getInventory().checkItem(40051, 1)) { // ��� ���޶���
					htmlid = "perita7";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40442, 40051 };
					counts = new int[] { 10, 10, 10, 10, 20, 5, 1 };
					createitem = new int[] { 40925 }; // ��ȭ�� �Ϻ�
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita8";
				}
			} else if (s.equals("G") || s.equals("h") || s.equals("i")) {
				// �ź����� �ϺΣ�1 �ܰ�
				if (pc.getInventory().checkItem(40651, 5) // ���� ����
						&& pc.getInventory().checkItem(40643, 5) // ���� ����
						&& pc.getInventory().checkItem(40618, 5) // ������ ����
						&& pc.getInventory().checkItem(40645, 5) // ��ǳ�� ���� ��
						&& pc.getInventory().checkItem(40676, 5) // ����� ����
						&& pc.getInventory().checkItem(40675, 5) // ����� ����
						&& pc.getInventory().checkItem(40049, 3) // ��� ���
						&& pc.getInventory().checkItem(40051, 1)) { // ��� ���޶���
					htmlid = "perita27";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40049, 40051 };
					counts = new int[] { 5, 5, 5, 5, 10, 10, 3, 1 };
					createitem = new int[] { 40926 }; // �ź����� �ϺΣ�1 �ܰ�
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita28";
				}
			} else if (s.equals("H") || s.equals("j") || s.equals("k")) {
				// �ź����� �ϺΣ�2 �ܰ�
				if (pc.getInventory().checkItem(40651, 10) // ���� ����
						&& pc.getInventory().checkItem(40643, 10) // ���� ����
						&& pc.getInventory().checkItem(40618, 10) // ������ ����
						&& pc.getInventory().checkItem(40645, 10) // ��ǳ�� ���� ��
						&& pc.getInventory().checkItem(40676, 20) // ����� ����
						&& pc.getInventory().checkItem(40675, 10) // ����� ����
						&& pc.getInventory().checkItem(40048, 3) // ��� ���̾Ƹ��
						&& pc.getInventory().checkItem(40051, 1)) { // ��� ���޶���
					htmlid = "perita29";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40048, 40051 };
					counts = new int[] { 10, 10, 10, 10, 20, 10, 3, 1 };
					createitem = new int[] { 40927 }; // �ź����� �ϺΣ�2 �ܰ�
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita30";
				}
			} else if (s.equals("I") || s.equals("l") || s.equals("m")) {
				// �ź����� �ϺΣ�3 �ܰ�
				if (pc.getInventory().checkItem(40651, 20) // ���� ����
						&& pc.getInventory().checkItem(40643, 20) // ���� ����
						&& pc.getInventory().checkItem(40618, 20) // ������ ����
						&& pc.getInventory().checkItem(40645, 20) // ��ǳ�� ���� ��
						&& pc.getInventory().checkItem(40676, 30) // ����� ����
						&& pc.getInventory().checkItem(40675, 10) // ����� ����
						&& pc.getInventory().checkItem(40050, 3) // ��� �����̾�
						&& pc.getInventory().checkItem(40051, 1)) { // ��� ���޶���
					htmlid = "perita31";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40050, 40051 };
					counts = new int[] { 20, 20, 20, 20, 30, 10, 3, 1 };
					createitem = new int[] { 40928 }; // �ź����� �ϺΣ�3 �ܰ�
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita32";
				}
			} else if (s.equals("J") || s.equals("n") || s.equals("o")) {
				// �ź����� �ϺΣ�4 �ܰ�
				if (pc.getInventory().checkItem(40651, 30) // ���� ����
						&& pc.getInventory().checkItem(40643, 30) // ���� ����
						&& pc.getInventory().checkItem(40618, 30) // ������ ����
						&& pc.getInventory().checkItem(40645, 30) // ��ǳ�� ���� ��
						&& pc.getInventory().checkItem(40676, 30) // ����� ����
						&& pc.getInventory().checkItem(40675, 20) // ����� ����
						&& pc.getInventory().checkItem(40052, 1) // �ְ�� ���̾Ƹ��
						&& pc.getInventory().checkItem(40051, 1)) { // ��� ���޶���
					htmlid = "perita33";
					materials = new int[] { 40651, 40643, 40618, 40645, 40676,
							40675, 40052, 40051 };
					counts = new int[] { 30, 30, 30, 30, 30, 20, 1, 1 };
					createitem = new int[] { 40928 }; // �ź����� �ϺΣ�4 �ܰ�
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita34";
				}
			} else if (s.equals("K")) { // 1 �ܰ� �� ��(��ȥ�� �� ��)
				int earinga = 0;
				int earingb = 0;
				if (pc.getInventory().checkEquipped(21014)
						|| pc.getInventory().checkEquipped(21006)
						|| pc.getInventory().checkEquipped(21007)) {
					htmlid = "perita36";
				} else if (pc.getInventory().checkItem(21014, 1)) { // ������ Ŭ����
					earinga = 21014;
					earingb = 41176;
				} else if (pc.getInventory().checkItem(21006, 1)) { // ����Ʈ Ŭ����
					earinga = 21006;
					earingb = 41177;
				} else if (pc.getInventory().checkItem(21007, 1)) { // ������ũ��
					earinga = 21007;
					earingb = 41178;
				} else {
					htmlid = "perita36";
				}
				if (earinga > 0) {
					materials = new int[] { earinga };
					counts = new int[] { 1 };
					createitem = new int[] { earingb };
					createcount = new int[] { 1 };
				}
			} else if (s.equals("L")) { // 2 �ܰ� �� ��(������ �� ��)
				if (pc.getInventory().checkEquipped(21015)) {
					htmlid = "perita22";
				} else if (pc.getInventory().checkItem(21015, 1)) {
					materials = new int[] { 21015 };
					counts = new int[] { 1 };
					createitem = new int[] { 41179 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita22";
				}
			} else if (s.equals("M")) { // 3 �ܰ� �� ��(������ �� ��)
				if (pc.getInventory().checkEquipped(21016)) {
					htmlid = "perita26";
				} else if (pc.getInventory().checkItem(21016, 1)) {
					materials = new int[] { 21016 };
					counts = new int[] { 1 };
					createitem = new int[] { 41182 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita26";
				}
			} else if (s.equals("b")) { // 2 �ܰ� �� ��(������ �� ��)
				if (pc.getInventory().checkEquipped(21009)) {
					htmlid = "perita39";
				} else if (pc.getInventory().checkItem(21009, 1)) {
					materials = new int[] { 21009 };
					counts = new int[] { 1 };
					createitem = new int[] { 41180 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita39";
				}
			} else if (s.equals("d")) { // 3 �ܰ� �� ��(���� �� ��)
				if (pc.getInventory().checkEquipped(21012)) {
					htmlid = "perita41";
				} else if (pc.getInventory().checkItem(21012, 1)) {
					materials = new int[] { 21012 };
					counts = new int[] { 1 };
					createitem = new int[] { 41183 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita41";
				}
			} else if (s.equals("a")) { // 2 �ܰ� �� ��(�г��� �� ��)
				if (pc.getInventory().checkEquipped(21008)) {
					htmlid = "perita38";
				} else if (pc.getInventory().checkItem(21008, 1)) {
					materials = new int[] { 21008 };
					counts = new int[] { 1 };
					createitem = new int[] { 41181 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita38";
				}
			} else if (s.equals("c")) { // 3 �ܰ� �� ��(����� �� ��)
				if (pc.getInventory().checkEquipped(21010)) {
					htmlid = "perita40";
				} else if (pc.getInventory().checkItem(21010, 1)) {
					materials = new int[] { 21010 };
					counts = new int[] { 1 };
					createitem = new int[] { 41184 };
					createcount = new int[] { 1 };
				} else {
					htmlid = "perita40";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71129) { // ����
			// ������
			// �뽺
			if (s.equals("Z")) {
				htmlid = "rumtis2";
			} else if (s.equals("Y")) {
				if (pc.getInventory().checkItem(41010, 1)) { // �̸����� ��õ��
					htmlid = "rumtis3";
				} else {
					htmlid = "rumtis4";
				}
			} else if (s.equals("q")) {
				htmlid = "rumtis92";
			} else if (s.equals("A")) {
				if (pc.getInventory().checkItem(41161, 1)) {
					// �ź����� �� �� ��
					htmlid = "rumtis6";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("B")) {
				if (pc.getInventory().checkItem(41164, 1)) {
					// �ź����� ������ �� ��
					htmlid = "rumtis7";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("C")) {
				if (pc.getInventory().checkItem(41167, 1)) {
					// �ź����� ȸ�� ������ �� ��
					htmlid = "rumtis8";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("T")) {
				if (pc.getInventory().checkItem(41167, 1)) {
					// �ź����� ȭ��Ʈ ������ �� ��
					htmlid = "rumtis9";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("w")) {
				if (pc.getInventory().checkItem(41162, 1)) {
					// �ź����� �� �� ��
					htmlid = "rumtis14";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("x")) {
				if (pc.getInventory().checkItem(41165, 1)) {
					// �ź����� ����Ʈ �� ��
					htmlid = "rumtis15";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("y")) {
				if (pc.getInventory().checkItem(41168, 1)) {
					// �ź����� ȸ�� ����Ʈ �� ��
					htmlid = "rumtis16";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("z")) {
				if (pc.getInventory().checkItem(41171, 1)) {
					// �ź����� ȭ��Ʈ ����Ʈ �� ��
					htmlid = "rumtis17";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("U")) {
				if (pc.getInventory().checkItem(41163, 1)) {
					// �ź����� �� �� ��
					htmlid = "rumtis10";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("V")) {
				if (pc.getInventory().checkItem(41166, 1)) {
					// �̽��׸��ƽ��������̾Ƹ�
					htmlid = "rumtis11";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("W")) {
				if (pc.getInventory().checkItem(41169, 1)) {
					// �̽��׸��ƽ��׷��̿������̾Ƹ�
					htmlid = "rumtis12";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("X")) {
				if (pc.getInventory().checkItem(41172, 1)) {
					// �̽��׸��ƽ�ȭ�̿������̾Ƹ�
					htmlid = "rumtis13";
				} else {
					htmlid = "rumtis101";
				}
			} else if (s.equals("D") || s.equals("E") || s.equals("F")
					|| s.equals("G")) {
				int insn = 0;
				int bacn = 0;
				int me = 0;
				int mr = 0;
				int mj = 0;
				int an = 0;
				int men = 0;
				int mrn = 0;
				int mjn = 0;
				int ann = 0;
				if (pc.getInventory().checkItem(40959, 1) // ��������� ����
						&& pc.getInventory().checkItem(40960, 1) // ���ɱ����� ����
						&& pc.getInventory().checkItem(40961, 1) // ���������� ����
						&& pc.getInventory().checkItem(40962, 1)) { // �ϻ챺���� ����
					insn = 1;
					me = 40959;
					mr = 40960;
					mj = 40961;
					an = 40962;
					men = 1;
					mrn = 1;
					mjn = 1;
					ann = 1;
				} else if (pc.getInventory().checkItem(40642, 10) // ������� ����
						&& pc.getInventory().checkItem(40635, 10) // ���ɱ��� ����
						&& pc.getInventory().checkItem(40638, 10) // �������� ����
						&& pc.getInventory().checkItem(40667, 10)) { // �ϻ챺��
					// ����
					bacn = 1;
					me = 40642;
					mr = 40635;
					mj = 40638;
					an = 40667;
					men = 10;
					mrn = 10;
					mjn = 10;
					ann = 10;
				}
				if (pc.getInventory().checkItem(40046, 1) // �����̾�
						&& pc.getInventory().checkItem(40618, 5) // ������ ����
						&& pc.getInventory().checkItem(40643, 5) // ���� ����
						&& pc.getInventory().checkItem(40645, 5) // ��ǳ�� ���� ��
						&& pc.getInventory().checkItem(40651, 5) // ���� ����
						&& pc.getInventory().checkItem(40676, 5)) { // ����� ����
					if (insn == 1 || bacn == 1) {
						htmlid = "rumtis60";
						materials = new int[] { me, mr, mj, an, 40046, 40618,
								40643, 40651, 40676 };
						counts = new int[] { men, mrn, mjn, ann, 1, 5, 5, 5, 5,
								5 };
						createitem = new int[] { 40926 }; // ������ �����̾1 �ܰ�
						createcount = new int[] { 1 };
					} else {
						htmlid = "rumtis18";
					}
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71119) {
			if (s.equalsIgnoreCase("request las history book")) {
				materials = new int[] { 41019, 41020, 41021, 41022, 41023,
						41024, 41025, 41026 };
				counts = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
				createitem = new int[] { 41027 };
				createcount = new int[] { 1 };
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71170) {
			if (s.equalsIgnoreCase("request las weapon manual")) {
				materials = new int[] { 41027 };
				counts = new int[] { 1 };
				createitem = new int[] { 40965 };
				createcount = new int[] { 1 };
				htmlid = "";
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6000015) {
			if (s.equalsIgnoreCase("1")) {
				L1Teleport.teleport(pc, 33966, 33253, (short) 4, 5, true);
			} else if (s.equalsIgnoreCase("a")
					&& pc.getInventory().checkItem(41158, 10)) {
				L1Teleport.teleport(pc, 32800, 32800, (short) 110, 5, true);
				pc.getInventory().consumeItem(41158, 10);
			} else if (s.equalsIgnoreCase("b")
					&& pc.getInventory().checkItem(41158, 20)) {
				L1Teleport.teleport(pc, 32800, 32800, (short) 120, 5, true);
				pc.getInventory().consumeItem(41158, 20);
			} else if (s.equalsIgnoreCase("c")
					&& pc.getInventory().checkItem(41158, 30)) {
				L1Teleport.teleport(pc, 32800, 32800, (short) 130, 5, true);
				pc.getInventory().consumeItem(41158, 30);
			} else if (s.equalsIgnoreCase("d")
					&& pc.getInventory().checkItem(41158, 40)) {
				L1Teleport.teleport(pc, 32800, 32800, (short) 140, 5, true);
				pc.getInventory().consumeItem(41158, 40);
			} else if (s.equalsIgnoreCase("e")
					&& pc.getInventory().checkItem(41158, 50)) {
				L1Teleport.teleport(pc, 32796, 32796, (short) 150, 5, true);
				pc.getInventory().consumeItem(41158, 50);
			} else if (s.equalsIgnoreCase("f")
					&& pc.getInventory().checkItem(41158, 60)) {
				L1Teleport.teleport(pc, 32720, 32821, (short) 160, 5, true);
				pc.getInventory().consumeItem(41158, 60);
			} else if (s.equalsIgnoreCase("g")
					&& pc.getInventory().checkItem(41158, 70)) {
				L1Teleport.teleport(pc, 32720, 32821, (short) 170, 5, true);
				pc.getInventory().consumeItem(41158, 70);
			} else if (s.equalsIgnoreCase("h")
					&& pc.getInventory().checkItem(41158, 80)) {
				L1Teleport.teleport(pc, 32724, 32822, (short) 180, 5, true);
				pc.getInventory().consumeItem(41158, 80);
			} else if (s.equalsIgnoreCase("i")
					&& pc.getInventory().checkItem(41158, 90)) {
				L1Teleport.teleport(pc, 32722, 32827, (short) 190, 5, true);
				pc.getInventory().consumeItem(41158, 90);
			} else if (s.equalsIgnoreCase("j")
					&& pc.getInventory().checkItem(41158, 100)) {
				L1Teleport.teleport(pc, 32731, 32856, (short) 200, 5, true);
				pc.getInventory().consumeItem(41158, 100);
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80067) { // ø����(�����
			// ������)
			// �������ϸ鼭�� �³��Ѵ١�
			if (s.equalsIgnoreCase("n")) {
				htmlid = "";
				poly(client, 6034);
				final int[] item_ids = { 41132, 41133, 41134 };
				final int[] item_amounts = { 1, 1, 1 };
				L1ItemInstance item = null;
				for (int i = 0; i < item_ids.length; i++) {
					item = pc.getInventory().storeItem(item_ids[i],
							item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143,
							((L1NpcInstance) obj).getNpcTemplate().get_name(),
							item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 1);
				}
				// ���׷� �ӹ��� �׸��д١�
			} else if (s.equalsIgnoreCase("d")) {
				htmlid = "minicod09";
				pc.getInventory().consumeItem(41130, 1);
				pc.getInventory().consumeItem(41131, 1);
				// ���ʱ�ȭ�Ѵ١�
			} else if (s.equalsIgnoreCase("k")) {
				htmlid = "";
				pc.getInventory().consumeItem(41132, 1); // ���ڱ��� Ÿ�� �� ����
				pc.getInventory().consumeItem(41133, 1); // ���ڱ��� ���� �� ����
				pc.getInventory().consumeItem(41134, 1); // ���ڱ��� ���� �� ����
				pc.getInventory().consumeItem(41135, 1); // ī���� Ÿ�� �� ����
				pc.getInventory().consumeItem(41136, 1); // ī���� ���� �� ����
				pc.getInventory().consumeItem(41137, 1); // ī���� ���� �� ����
				pc.getInventory().consumeItem(41138, 1); // ī���� ����
				pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 0);
				// ������ �ǳ��ش�
			} else if (s.equalsIgnoreCase("e")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END
						|| pc.getKarmaLevel() >= 1) {
					htmlid = "";
				} else {
					if (pc.getInventory().checkItem(41138)) {
						htmlid = "";
						pc.addKarma((int) (1600 * Config.RATE_KARMA));
						pc.getInventory().consumeItem(41130, 1); // ���ڱ��� ��༭
						pc.getInventory().consumeItem(41131, 1); // ���ڱ��� ���ɼ�
						pc.getInventory().consumeItem(41138, 1); // ī���� ����
						pc.getQuest().set_step(L1Quest.QUEST_DESIRE,
								L1Quest.QUEST_END);
					} else {
						htmlid = "minicod04";
					}
				}
				// ������ �޴´�
			} else if (s.equalsIgnoreCase("g")) {
				L1ItemInstance item = pc.getInventory().storeItem(41130, 1);
				pc
						.sendPackets(new S_ServerMessage(143,
								((L1NpcInstance) obj).getNpcTemplate()
										.get_name(), item.getItem().getName()));
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4204000) { // ��������Ʈ
			// �κ��ĵ�
			if (s.equals("A")) { /* robinhood1~7 */
				if (pc.getInventory().checkItem(40028)) { /* ����ֽ� üũ */
					pc.getInventory().consumeItem(40028, 1); /* ����ֽ� �Һ� */
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 1); /* 1�ܰ� �Ϸ� */
					htmlid = "robinhood4";
				} else {
					htmlid = "robinhood19";
				}
			} else if (s.equals("B")) { /* robinhood8 */
				final int[] item_ids = { 41346, 41348 };
				final int[] item_amounts = { 1, 1, };
				L1ItemInstance item = null;
				for (int i = 0; i < item_ids.length; i++) {
					item = pc.getInventory().storeItem(item_ids[i],
							item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 2);
					htmlid = "robinhood13";
				}
			} else if (s.equals("C")) { /* robinhood9 */
				if (pc.getInventory().checkItem(41346)
						&& pc.getInventory().checkItem(41351)
						&& pc.getInventory().checkItem(41352, 4)
						&& pc.getInventory().checkItem(40618, 30)
						&& pc.getInventory().checkItem(40643, 30)
						&& pc.getInventory().checkItem(40645, 30)
						&& pc.getInventory().checkItem(40651, 30)
						&& pc.getInventory().checkItem(40676, 30)) {
					pc.getInventory().consumeItem(41346, 1); // �޸���, ����, ����, ��, ��, �ٶ�, ���� ��Ҽ���
					pc.getInventory().consumeItem(41351, 1);
					pc.getInventory().consumeItem(41352, 4);
					pc.getInventory().consumeItem(40651, 30);
					pc.getInventory().consumeItem(40643, 30);
					pc.getInventory().consumeItem(40645, 30);
					pc.getInventory().consumeItem(40618, 30);
					pc.getInventory().consumeItem(40676, 30);
					final int[] item_ids = { 41350, 41347 };
					final int[] item_amounts = { 1, 1, };
					L1ItemInstance item = null;
					for (int i = 0; i < item_ids.length; i++) {
						item = pc.getInventory().storeItem(item_ids[i],	item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
					}
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 7); // 7�ܰ� �Ϸ�
					htmlid = "robinhood10"; /* ������ ��Ḧ ã�ƿ���.. */
				} else {
					htmlid = "robinhood15"; /* �޺�����, ���� �����Դ°� */
				}
			} else if (s.equals("E")) { /* robinhood11 */
				if (pc.getInventory().checkItem(41350)
						&& pc.getInventory().checkItem(41347)
						&& pc.getInventory().checkItem(40491, 30)
						&& pc.getInventory().checkItem(40495, 40)
						&& pc.getInventory().checkItem(100)
						&& pc.getInventory().checkItem(40509, 12)
						&& pc.getInventory().checkItem(40052)
						&& pc.getInventory().checkItem(40053)
						&& pc.getInventory().checkItem(40054)
						&& pc.getInventory().checkItem(40055)) {
					pc.getInventory().consumeItem(41350, 1); // ����, �޸���, �׸�������, �̽�����, ������, ����, �ְ�޺���1����
					pc.getInventory().consumeItem(41347, 1);
					pc.getInventory().consumeItem(40491, 30);
					pc.getInventory().consumeItem(40495, 40);
					pc.getInventory().consumeItem(100, 1);
					pc.getInventory().consumeItem(40509, 12);
					pc.getInventory().consumeItem(40052, 1);
					pc.getInventory().consumeItem(40053, 1);
					pc.getInventory().consumeItem(40054, 1);
					pc.getInventory().consumeItem(40055, 1);
					final int[] item_ids = { 205 };
					final int[] item_amounts = { 1 };
					L1ItemInstance item = null;
					for (int i = 0; i < item_ids.length; i++) {
						item = pc.getInventory().storeItem(item_ids[i],
								item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(143,
								((L1NpcInstance) obj).getNpcTemplate()
										.get_name(), item.getItem().getName()));
					}
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 0); // ����Ʈ ����
					htmlid = "robinhood12"; /* �ϼ��̾� */
				} else {
					htmlid = "robinhood17"; /* ��ᰡ �����Ѱ� */
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4210000) {
			if (s.equals("A")) { /* zybril1 ~ zybril6 */
				if (pc.getInventory().checkItem(41348)) { /* �Ұ��� */
					pc.getInventory().consumeItem(41348, 1);
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 3); //3�ܰ�Ϸ�
					htmlid = "zybril13"; /* �� �� Ȱ���� */
				} else {
					htmlid = "zybril11"; /* ������ ���? */
				}
			} else if (s.equals("B")) { /* zybril7 */
				if (pc.getInventory().checkItem(40048, 10)
						&& pc.getInventory().checkItem(40049, 10)
						&& pc.getInventory().checkItem(40050, 10)
						&& pc.getInventory().checkItem(40051, 10)) {
					pc.getInventory().consumeItem(40048, 10); // ���, ���, ���,��
					pc.getInventory().consumeItem(40049, 10);
					pc.getInventory().consumeItem(40050, 10);
					pc.getInventory().consumeItem(40051, 10);
					final int[] item_ids = { 41353 };
					final int[] item_amounts = { 1 };
					for (int i = 0; i < item_ids.length; i++) {
						pc.getInventory().storeItem(item_ids[i],
								item_amounts[i]);
						pc.sendPackets(new S_SystemMessage("������ �ܰ��� ������ϴ�."));
					}
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 4); // 4�ܰ� �Ϸ�
					htmlid = "zybril12"; /* ��α��� �����Կ� */
				} else {
					htmlid = "";
				}
			} else if (s.equals("C")) { /* zybril8 */
				if (pc.getInventory().checkItem(40514, 10)
						&& pc.getInventory().checkItem(41353, 1)) {
					pc.getInventory().consumeItem(40514, 10); //������ ����, ������ �ܰ�
					pc.getInventory().consumeItem(41353, 1);
					final int[] item_ids = { 41354 };
					final int[] item_amounts = { 1 };
					for (int i = 0; i < item_ids.length; i++) {
						 pc.getInventory().storeItem(item_ids[i],
								item_amounts[i]);
						pc
								.sendPackets(new S_SystemMessage("�ż��� ������ ���� ������ϴ�."));
					}
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 5); // 5�ܰ� �Ϸ�
					htmlid = "zybril9"; /* ������ �Ѱ�����Ź�� */
				} else {
					htmlid = "zybril13"; /* �����Ǵ����� �ʿ��մϴ�.. */
				}
			} else if (s.equals("D")) { /* zybril18 */
				if (pc.getInventory().checkItem(41349)) { /* �翤�� ���� */
					pc.getInventory().consumeItem(41349, 1);
					final int[] item_ids = { 41351 };
					final int[] item_amounts = { 1 };
					for (int i = 0; i < item_ids.length; i++) {
					pc.getInventory().storeItem(item_ids[i],
								item_amounts[i]);
						pc.sendPackets(new S_SystemMessage("�޺��� ���⸦ ������ϴ�."));
					}
					pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 6); // �Ϸ�
					htmlid = "zybril10"; /* �޺��� ���⸦ �������� */
				} else {
					htmlid = "zybril14"; /* ��� ����? */
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71179) {// ����(�����
			// ����)
			if (s.equalsIgnoreCase("A")) {// ������ ����� �����
				Random random = new Random();
				if (pc.getInventory().checkItem(49028, 1)
						&& pc.getInventory().checkItem(49029, 1)
						&& pc.getInventory().checkItem(49030, 1)
						&& pc.getInventory().checkItem(41139, 1)) { 
					if (random.nextInt(10) > 6) {
						materials = new int[] { 49028, 49029, 49030, 41139 };
						counts = new int[] { 1, 1, 1, 1 };
						createitem = new int[] { 41140 }; // ������ ����� �����
						createcount = new int[] { 1 };
						htmlid = "dh8";
					} else { // ������ ��� �����۸� �����
						materials = new int[] { 49028, 49029, 49030, 41139 };
						counts = new int[] { 1, 1, 1, 1 };
						createitem = new int[] { L1ItemId.GEMSTONE_POWDER }; // ����
						// ����
						createcount = new int[] { 5 };
						htmlid = "dh7";
					}
				} else { // ��ᰡ ������ ���
					htmlid = "dh6";
				}
			} else if (s.equalsIgnoreCase("B")) {// ������ ����� ����� ������ ��Ź�Ѵ�.
				Random random = new Random();
				if (pc.getInventory().checkItem(49027, 1)
						&& pc.getInventory().checkItem(41140, 1)) { 
					if (random.nextInt(10) > 7) {
						materials = new int[] { 49027, 41140 };
						counts = new int[] { 1, 1 };
						createitem = new int[] { 20422 }; // ������ ��� �����
						createcount = new int[] { 1 };
						htmlid = "dh9";
					} else {
						materials = new int[] { 49027, 41140 };
						counts = new int[] { 1, 1 };
						createitem = new int[] { L1ItemId.GEMSTONE_POWDER }; // ��������
						createcount = new int[] { 5 };
						htmlid = "dh7";
					}
				} else { // ��ᰡ ������ ���
					htmlid = "dh6";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81202) { // ø����(�׸�����
			// ������)
			// ��ȭ�� ������ �³��Ѵ١�
			if (s.equalsIgnoreCase("n")) {
				htmlid = "";
				poly(client, 6035);
				final int[] item_ids = { 41123, 41124, 41125 };
				final int[] item_amounts = { 1, 1, 1 };
				L1ItemInstance item = null;
				for (int i = 0; i < item_ids.length; i++) {
					item = pc.getInventory().storeItem(item_ids[i],
							item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143,
							((L1NpcInstance) obj).getNpcTemplate().get_name(),
							item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 1);
				}
				// ���׷� �ӹ��� �׸��д١�
			} else if (s.equalsIgnoreCase("d")) {
				htmlid = "minitos09";
				pc.getInventory().consumeItem(41121, 1);
				pc.getInventory().consumeItem(41122, 1);
				// ���ʱ�ȭ�Ѵ١�
			} else if (s.equalsIgnoreCase("k")) {
				htmlid = "";
				pc.getInventory().consumeItem(41123, 1); // ī���� Ÿ�� �� ����
				pc.getInventory().consumeItem(41124, 1); // ī���� ���� �� ����
				pc.getInventory().consumeItem(41125, 1); // ī���� ���� �� ����
				pc.getInventory().consumeItem(41126, 1); // ���ڱ��� Ÿ�� �� ����
				pc.getInventory().consumeItem(41127, 1); // ���ڱ��� ���� �� ����
				pc.getInventory().consumeItem(41128, 1); // ���ڱ��� ���� �� ����
				pc.getInventory().consumeItem(41129, 1); // ���ڱ��� ����
				pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 0);
				// ������ �ǳ��ش�
			} else if (s.equalsIgnoreCase("e")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END
						|| pc.getKarmaLevel() >= 1) {
					htmlid = "";
				} else {
					if (pc.getInventory().checkItem(41129)) {
						htmlid = "";
						pc.addKarma((int) (-1600 * Config.RATE_KARMA));
						pc.getInventory().consumeItem(41121, 1); // ī���� ��༭
						pc.getInventory().consumeItem(41122, 1); // ī���� ���ɼ�
						pc.getInventory().consumeItem(41129, 1); // ���ڱ��� ����
						pc.getQuest().set_step(L1Quest.QUEST_SHADOWS,
								L1Quest.QUEST_END);
					} else {
						htmlid = "minitos04";
					}
				}
				// ������� �޴´�
			} else if (s.equalsIgnoreCase("g")) {
				L1ItemInstance item = pc.getInventory().storeItem(41121, 1);
				pc
						.sendPackets(new S_ServerMessage(143,
								((L1NpcInstance) obj).getNpcTemplate()
										.get_name(), item.getItem().getName()));
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70842) { // ������
			if (pc.getInventory().checkItem(40665)) {
				htmlid = "marba17";
				if (s.equalsIgnoreCase("B")) {
					htmlid = "marba7";
					if (pc.getInventory().checkItem(214)
							&& pc.getInventory().checkItem(20389)
							&& pc.getInventory().checkItem(20393)
							&& pc.getInventory().checkItem(20401)
							&& pc.getInventory().checkItem(20406)
							&& pc.getInventory().checkItem(20409)) {
						htmlid = "marba15";
					}
				}
			} else if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40637)) {
					htmlid = "marba20";
				} else {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(40637, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "marba6";
				}
			}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777017) {
			if (s.equalsIgnoreCase("b")) {
				htmlid = "";
				if (DevilController.getInstance().getDevilStart() == true) {
					Random random = new Random();
					int i13 = 32723 + random.nextInt(10);
					int k19 = 32800 + random.nextInt(10);
					L1Teleport.teleport(pc, i13, k19, (short) 5167, 6, true);
                    pc.sendPackets(new S_SystemMessage("���䰡 �����ð����κ��� 1�ð����� ���尡���մϴ�."));
					return;
				} else {
					//pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\f<[�Ǹ����ǿ���:����ð��̾ƴմϴ�]"));
					pc.sendPackets(new S_SystemMessage("�Ǹ����ǿ���:����ð��̾ƴմϴ�"));
					return;
				}
			}
		
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70845) { // �ƶ�
			if (pc.getInventory().checkItem(40665)) {
				htmlid = "aras8";
			} else if (pc.getInventory().checkItem(40637)) {
				htmlid = "aras1";
				if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().checkItem(40664)) {
						htmlid = "aras6";
						if (pc.getInventory().checkItem(40679)
								|| pc.getInventory().checkItem(40680)
								|| pc.getInventory().checkItem(40681)
								|| pc.getInventory().checkItem(40682)
								|| pc.getInventory().checkItem(40683)
								|| pc.getInventory().checkItem(40684)
								|| pc.getInventory().checkItem(40693)
								|| pc.getInventory().checkItem(40694)
								|| pc.getInventory().checkItem(40695)
								|| pc.getInventory().checkItem(40697)
								|| pc.getInventory().checkItem(40698)
								|| pc.getInventory().checkItem(40699)) {
							htmlid = "aras3";
						} else {
							htmlid = "aras6";
						}
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(
								40664, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName,
								itemName));
						htmlid = "aras6";
					}
				} else if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkItem(40664)) {
						pc.getInventory().consumeItem(40664, 1);
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(
								40665, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName,
								itemName));
						htmlid = "aras13";
					} else {
						htmlid = "aras14";
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(
								40665, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName,
								itemName));
					}
				} else {
					if (s.equalsIgnoreCase("7")) {
						if (pc.getInventory().checkItem(40693)
								&& pc.getInventory().checkItem(40694)
								&& pc.getInventory().checkItem(40695)
								&& pc.getInventory().checkItem(40697)
								&& pc.getInventory().checkItem(40698)
								&& pc.getInventory().checkItem(40699)) {
							htmlid = "aras10";
						} else {
							htmlid = "aras9";
						}
					}
				}
			} else {
				htmlid = "aras7";
			}

			
	
			
			
			
			
			/** ������ * */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777835
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877831
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877832
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877833
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877834
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877835
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877836
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877837
				|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 877838) {
			if (s.equalsIgnoreCase("z")) { //����Ǳ����� �߰�
			    if (pc.getInventory().checkItem(40308, 300000)) {
			     if (pc.getLevel() >= 5) {
			      pc.getInventory().consumeItem(40308, 300000);
			      int[] allBuffSkill = { 88 , 89 , 68 , 43, 79, 151, 160, 206, 216, 211, 107, 148, 211, 206 };
			      pc.setBuffnoch(1);
			      L1SkillUse l1skilluse = new L1SkillUse();
			      for (int i = 0; i < allBuffSkill.length; i++) {
			       l1skilluse.handleCommands(pc, allBuffSkill[i],
			       pc.getId(), pc.getX(), pc.getY(), null, 0,L1SkillUse.TYPE_GMBUFF);
			      }
			      htmlid = "bs_done";//�����׼ǰ� 
			     }
			    } else {
			     htmlid = "bs_done3";//�����׼ǰ�
			     pc.sendPackets(new S_SystemMessage("30�� �Ƶ����� �����մϴ�."));//������Ʈ
			    }
			}
				if (s.equalsIgnoreCase("a")) { //�ٰŸ�����
					if (pc.getInventory().checkItem(40308, 3000)) {//�����ּ��� ;;
					if (pc.getLevel() >= 5) {
						pc.getInventory().consumeItem(40308, 3000);
						int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211, 107, 148 };//�̰ŵ� ���̾���� �ߺ��̳׿�..
						pc.setBuffnoch(1);
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i],
									pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
						}
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("5���� �̻���� ��� �����մϴ�."));
					}					
					} else {
						S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
						pc.sendPackets(s_chatpacket);
					}
			}
			if (s.equalsIgnoreCase("b")) { //���Ÿ�����
				if (pc.getInventory().checkItem(40308, 3000)) {
					if (pc.getLevel() >= 5) {
						pc.getInventory().consumeItem(40308, 3000);
						int[] allBuffSkill = { 43, 79, 151, 160, 206, 216, 211, 149 };
						pc.setBuffnoch(1);
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i],pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
						}
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("5���� �̻���� ��� �����մϴ�."));
					}
				} else {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
				}
			}
			if (s.equalsIgnoreCase("c")) {
			     if (pc.getInventory().checkItem(40308, 350000)) {	
		        		if (pc.isGhost()) { return; }
		        		if (pc.isInvisble()) { return; }// �κ��� ����		
		        		if (pc.isInvisDelay()) { return; }// �κ������� ����
		        		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) { return; }
			            pc.getInventory().consumeItem(40308, 350000);
			            int[] allBuffSkill = { 43, 79, 151, 160,  211, 216 ,206};
			            pc.setBuffnoch(1);
			            L1SkillUse l1skilluse = new L1SkillUse();
			            for (int i = 0; i < allBuffSkill.length; i++) {
			            l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			            }
			            htmlid = "bs_done";
			    } else {
			            htmlid = "bs_done3";
			           }
			    }
				  				/** �� �ֹ��� ���� */
			       if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("2")|| s.equalsIgnoreCase("3")
			              || s.equalsIgnoreCase("4")   || s.equalsIgnoreCase("5")){
			          htmlid = "bs_m4";
			                  /** 1�ܰ� ���� */
			               if (s.equalsIgnoreCase("A")){
			            if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("B")){
			            if (obj instanceof L1NpcInstance) {}               
			                 } else if (s.equalsIgnoreCase("C")){
			            if (obj instanceof L1NpcInstance) {}         
			                  } else if (s.equalsIgnoreCase("D")){
			            if (obj instanceof L1NpcInstance) {}          
			               } else if (s.equalsIgnoreCase("E")){
			              if (obj instanceof L1NpcInstance) {}
			               /** 2�ܰ� ���� */
			               } else if (s.equalsIgnoreCase("F")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("G")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("H")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("I")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("J")){
			              if (obj instanceof L1NpcInstance) {}
			              /** 3�ܰ� ���� */
			               } else if (s.equalsIgnoreCase("K")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("L")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("M")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("N")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("O")){
			              if (obj instanceof L1NpcInstance) {}
			              /** 4�ܰ� ���� */
			               } else if (s.equalsIgnoreCase("P")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("Q")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("R")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("S")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("T")){
			              if (obj instanceof L1NpcInstance) {}
			              /** 5�ܰ� ���� */
			               } else if (s.equalsIgnoreCase("U")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("V")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("W")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("X")){
			              if (obj instanceof L1NpcInstance) {}
			               } else if (s.equalsIgnoreCase("Y")){
			              if (obj instanceof L1NpcInstance) {}
			               }
	               }
			       
			       				/** �ǵ��� ���� �ý��� �߰� �κ�( ���� ) **/
						if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40199){
							   int type = 0;
							   if (s.equalsIgnoreCase("a")) { // �Ҳ��� ������ �޴´�.
							    type = 0;
							   } else if (s.equalsIgnoreCase("b")) { // �ٶ��� ������ �޴´�.
							    type = 1;
							   } else if (s.equalsIgnoreCase("c")) { // ��ȥ�� ������ �޴´�.
							    type = 2;
							   }
							   if (pc.getInventory().checkItem(41159, 10)) {
							    pc.getInventory().consumeItem(41159, 10);
							    Zendor(pc, objid, type);
							   } else {
							    htmlid = "pbs_03";
							   }
							  }
						/** �ǵ��� ���� �ý��� �߰� �κ�( ���� ) **/
						
						
			//�Ǿ ����
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70908) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			String npcName = npc.getNpcTemplate().get_name();
			   if (s.equals("a")){ // ����� ���� ���մϴ�.
			    int[] list = new int[2];
			    for (L1ItemInstance item : pc.getInventory().getItems()) {
			     switch(item.getItemId()){
			     case 13:
			     case 81:
			     case 162:
			     case 177:
			     case 194:
			      if (item.getEnchantLevel() == 8 && pc.getInventory().checkItem(40308, 20000000)){
			       list[0] = 1;
			      } else if (item.getEnchantLevel() == 9 && pc.getInventory().checkItem(40308, 30000000)){
			       list[1] = 2;
			      }
			      break;
			     default:
			      break;
			     }
			    }
			    int count = list[0] +  list[1];
			    switch(count){
			    case 1:
			     htmlid = "piers03"; 
			     break;
			    case 2:
			     htmlid = "piers02";
			     break;
			    case 3:
			     htmlid = "piers01";
			     break;
			    default:
			     htmlid = "piers04"; 
					pc.sendPackets(new S_SystemMessage("\\fY+8����� �Ƶ���(2õ) �ʿ��մϴ�."));
					pc.sendPackets(new S_SystemMessage("\\fY+9����� �Ƶ���(3õ) �ʿ��մϴ�."));
			     break;
			    }
			   } else if (s.equals("0")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[0], pc.PiersEnchant[0], pc.PiersItemId[18]);
			   } else if (s.equals("1")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[1], pc.PiersEnchant[1], pc.PiersItemId[18]);
			   } else if (s.equals("2")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[2], pc.PiersEnchant[2], pc.PiersItemId[18]);
			   } else if (s.equals("3")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[3], pc.PiersEnchant[3], pc.PiersItemId[18]);
			   } else if (s.equals("4")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[4], pc.PiersEnchant[4], pc.PiersItemId[18]);
			   } else if (s.equals("5")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[5], pc.PiersEnchant[5], pc.PiersItemId[18]);
			   } else if (s.equals("6")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[6], pc.PiersEnchant[6], pc.PiersItemId[18]);
			   } else if (s.equals("7")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[7], pc.PiersEnchant[7], pc.PiersItemId[18]);
			   } else if (s.equals("8")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[8], pc.PiersEnchant[8], pc.PiersItemId[18]);
			   } else if (s.equals("9")){
			    PiersItem(pc, objid, npcName, pc.PiersItemId[9], pc.PiersEnchant[9], pc.PiersItemId[18]);
			   } else if (s.equals("A")){ // +7 �ı��� ũ�ο�    
			    PiersCheckItem(pc, objid, 8, 20000000, 46172, 7);
			   } else if (s.equals("B")){ // +7 �ı��� �̵���
			    PiersCheckItem(pc, objid, 8, 20000000, 46173, 7);
			   } else if (s.equals("C")){ // +8 �ı��� ũ�ο�
			    PiersCheckItem(pc, objid, 9, 30000000, 46172, 8);
			   } else if (s.equals("D")){ // +8 �ı��� �̵���
			    PiersCheckItem(pc, objid, 9, 30000000, 46173, 8);
			   }                             
			
			// �ƿ��Ű���� ����
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4218005) {
			if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)
						&& !pc.getInventory().checkItem(423014)) {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
					pc.getInventory().storeItem(423014, 1);
					htmlid = "evdcs3";
				} else if (pc.getInventory().checkItem(423014)) {
					htmlid = "evdcs4";
				} else {
					htmlid = "evdcs5";
				}
			}
			if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(L1ItemId.MIRACLE_FRAGMENT, 6)) {
					pc.getInventory().consumeItem(L1ItemId.MIRACLE_FRAGMENT, 6);
					pc.getInventory().storeItem(L1ItemId.AURAKIA_PRESENT, 1);
					htmlid = "evdcs6";
				} else if (pc.getInventory().checkItem(
						L1ItemId.DISTURBING_PROOF, 1)) {
					pc.getInventory().consumeItem(L1ItemId.DISTURBING_PROOF, 1);
					pc.getInventory().storeItem(L1ItemId.AURAKIA_PRESENT, 1);
					htmlid = "evdcs6";
				} else {
					htmlid = "evdcs7";
				}
			}
			// �Ƿ��� ����
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4219005) {
			if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)
						&& !pc.getInventory().checkItem(423015)) {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
					pc.getInventory().storeItem(423015, 1);
					htmlid = "evics3";
				} else if (pc.getInventory().checkItem(423015)) {
					htmlid = "evics4";
				} else {
					htmlid = "evics5";
				}
			}
			if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(L1ItemId.SHINY_LEAF, 2)) {
					pc.getInventory().consumeItem(L1ItemId.SHINY_LEAF, 2);
					pc.getInventory().storeItem(L1ItemId.SILEN_PRESENT, 1);
					htmlid = "evics6";
				} else if (pc.getInventory().checkItem(
						L1ItemId.DISTURBING_PROOF, 1)) {
					pc.getInventory().consumeItem(L1ItemId.DISTURBING_PROOF, 1);
					pc.getInventory().storeItem(L1ItemId.SILEN_PRESENT, 1);
					htmlid = "evics6";
				} else {
					htmlid = "evics7";
				}
			}
			// �̴� ���� �ڸ�
	
			// ���ž ���谡
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200015) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_TIKAL_BOSSDIE)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(
						L1SkillId.STATUS_TIKAL_BOSSDIE);
			}
			int[] allBuffSkill = { L1SkillId.STATUS_TIKAL_BOSSJOIN };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc
						.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}
			htmlid = "aitexplorer1";
			// ���ž ���谡

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 40105) {// �ɷ��Ͻ��̺�Ʈ
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(40006, 1)) {
					pc.getInventory().consumeItem(40006, 1);
					pc.getInventory().storeItem(30239, 1);
					htmlid = "kelenisA2";
				} else {
					htmlid = "kelenisE";
				}
			}

			if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(40008, 1)) {
					pc.getInventory().consumeItem(40008, 1);
					pc.getInventory().storeItem(30239, 1);
					htmlid = "kelenisA2";
				} else {
					htmlid = "kelenisE";
				}
			}

			if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().checkItem(40007, 1)) {
					pc.getInventory().consumeItem(40007, 1);
					pc.getInventory().storeItem(30239, 1);
					htmlid = "kelenisA2";
				} else {
					htmlid = "kelenisE";
				}
			}
			if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(30239, 10)) {
					pc.getInventory().consumeItem(30239, 10);
					pc.getInventory().storeItem(30238, 1);
					htmlid = "kelenisA2";
				} else {
					htmlid = "kelenisE2";
				}
			}
			/** 2011�� �ҷ��� �̺�Ʈ 1�� */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200001) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("z")) {
				if (pc.getInventory().checkItem(40726, 4)) {
					pc.getInventory().consumeItem(40726, 4);
					pc.getInventory().storeItem(87880, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("y")) {
				if (pc.getInventory().checkItem(426312, 1)) {
					pc.getInventory().consumeItem(426312, 1);
					pc.getInventory().storeItem(87880, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(30231, 1)) {
					pc.getInventory().consumeItem(30231, 1);
					pc.getInventory().storeItem(87881, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(30231, 3)) {
					pc.getInventory().consumeItem(30231, 3);
					pc.getInventory().storeItem(87883, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().checkItem(30231, 3)) {
					pc.getInventory().consumeItem(30231, 3);
					pc.getInventory().storeItem(87882, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(30231, 0)) {
					pc.getInventory().consumeItem(30231, 0);
					pc.sendPackets(new S_NpcChatPacket(npc,"������ ǰ�� �����Դϴ�.", 0));
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}

			/** 2011�� �ҷ��� �̺�Ʈ 2�� */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 47431) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("z")) {
				if (pc.getInventory().checkItem(40726, 4)) {
					pc.getInventory().consumeItem(40726, 4);
					pc.getInventory().storeItem(87880, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(30231, 1)) {
					pc.getInventory().consumeItem(30231, 1);
					pc.getInventory().storeItem(87881, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(30231, 3)) {
					pc.getInventory().consumeItem(30231, 3);
					pc.getInventory().storeItem(87883, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("c")) {
				if (pc.getInventory().checkItem(30231, 3)) {
					pc.getInventory().consumeItem(30231, 3);
					pc.getInventory().storeItem(87882, 1);
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(30231, 0)) {
					pc.getInventory().consumeItem(30231, 0);
					pc
							.sendPackets(new S_NpcChatPacket(npc,
									"������ ǰ�� �����Դϴ�.", 0));
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("e")) {
				if (pc.getInventory().checkItem(30231, 30)) {
					pc.getInventory().consumeItem(30231, 30);
					createNewItem(pc, 426310, 1, 7); // �ҷ��� ȣ�� �尩
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("f")) {
				if (pc.getInventory().checkItem(30231, 30)) {
					pc.getInventory().consumeItem(30231, 30);
					createNewItem(pc, 342, 1, 7); // �ҷ��� ȣ�� ����
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("g")) {
				if (pc.getInventory().checkItem(30231, 30)) {
					pc.getInventory().consumeItem(30231, 30);
					createNewItem(pc, 340, 1, 7); // �ҷ��� ȣ�� ��հ�
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("h")) {
				if (pc.getInventory().checkItem(30231, 30)) {
					pc.getInventory().consumeItem(30231, 30);
					createNewItem(pc, 341, 1, 7); // �ҷ��� ȣ�� ������
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			if (s.equalsIgnoreCase("i")) {
				if (pc.getInventory().checkItem(30231, 30)) {
					pc.getInventory().consumeItem(30231, 30);
					createNewItem(pc, 426311, 1, 7); // �ҷ��� ȣ�� ����
					htmlid = "sslip4";
				} else {
					htmlid = "sslip3";
				}
			}
			// ������ �丮�� (�����)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200016) { // ������
			L1NpcInstance npc = (L1NpcInstance) obj;
			// �丮��
			if (s.equalsIgnoreCase("0")) {
				if (pc.getInventory().checkItem(41159, 45)) { // �ź���
					// ���� ����
					pc.getInventory().consumeItem(41159, 45); // �����Ǵ�
					// ���м���
					pc.getInventory().storeItem(437019, 1); // ������Ű
					pc.sendPackets(new S_SystemMessage("������Ű�� ������ϴ�."));
					// htmlid="suschef2";
				} else { // ��ᰡ ������ ���
					pc.sendPackets(new S_NpcChatPacket(npc,"�Ƚ��� ������ �����մϴ�.", 0));
					htmlid = "suschef5";
				}
			}
			if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(437020, 1)) { // �����
					pc.getInventory().consumeItem(437020, 1); // �����Ǵ�
					// �����
					Random random = new Random();
					int gn4 = random.nextInt(4) + 1;
					if (gn4 == 1) {
						int[] allBuffSkill = { L1SkillId.STATUS_LUCK_A };
						pc.setBuffnoch(1);
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc
									.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
						}
						htmlid = "";
						pc.sendPackets(new S_NpcChatPacket(npc,"�ڳ״� ���� ���� ����.... �ְ��� ���̾�!! ���ð��� ���� �ζǶ� �̾ƺ���~",0));
					} else if (gn4 == 2) {
						int[] allBuffSkill = { L1SkillId.STATUS_LUCK_B };
						pc.setBuffnoch(1);
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,L1SkillUse.TYPE_GMBUFF);
						}
						htmlid = "";
						pc.sendPackets(new S_NpcChatPacket(npc,"������ ��� �������̾�.... �׷��� ���� �� �����϶�~", 0));
					} else if (gn4 == 3) {
						int[] allBuffSkill = { L1SkillId.STATUS_LUCK_C };
						pc.setBuffnoch(1);
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,L1SkillUse.TYPE_GMBUFF);
						}
						htmlid = "";
						pc.sendPackets(new S_NpcChatPacket(npc,"����� �����.... ���� ȫ�� ���� ���ٰ���?? �����شٸ� ������ ���� ��������!!",0));
					} else if (gn4 == 4) {
						int[] allBuffSkill = { L1SkillId.STATUS_LUCK_D };
						pc.setBuffnoch(1);
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,L1SkillUse.TYPE_GMBUFF);
						}
						htmlid = "";
						pc.sendPackets(new S_NpcChatPacket(npc,
								"������ ���ð������� ������ �ȳ����°� ������ ����....", 0));
					}

					htmlid = "suschef2";
				} else { // ��ᰡ ������ ���
					pc.sendPackets(new S_SystemMessage("� ������ �����ϴ�.."));
					htmlid = "suschef4";
				}
			}

							/** ������ �ڷ����� * */
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4220013) {//��ü��
			   if (s.equalsIgnoreCase("b")) {
			    if (pc.getInventory().checkItem(41159, 1)){
			     pc.getInventory().consumeItem(41159, 1);
			     L1Teleport.teleport(pc, 33440, 32803, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("c")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 34053, 32281, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("d")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 33709, 32500, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("e")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 33607, 33257, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }  
			   } else if (s.equalsIgnoreCase("f")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 33051, 32790, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("g")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32606, 32733, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("h")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 33073, 33391, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("i")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32644, 33207, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("j")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32741, 32450, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("k")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32583, 32924, (short)0, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("l")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 33950, 33364, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("m")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32595, 32721, (short)4, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("n")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32779, 32848, (short)340, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }
			   } else if (s.equalsIgnoreCase("o")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32702, 32842, (short)350, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    } 
			   } else if (s.equalsIgnoreCase("p")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32733, 32792, (short)360, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    } 
			   } else if (s.equalsIgnoreCase("q")) {
			    if (pc.getInventory().checkItem(41159, 2)){
			     pc.getInventory().consumeItem(41159, 2);
			     L1Teleport.teleport(pc, 32733, 32792, (short)370, 5, true);
			     htmlid = "";
			    } else {
			     htmlid = "pctel2";
			    }   
			   } 

			   						/** �ð��� �տ� **/
			// �׺� ������
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200005) {
			if (s.equalsIgnoreCase("e")) {
				 if (pc.getMapId() != 0) {
						Collection<L1PcInstance> templist = L1World.getInstance()
								.getAllPlayers();
						L1PcInstance[] list = templist
								.toArray(new L1PcInstance[templist.size()]);
						int count = 0;
						for (L1PcInstance player : list) {
							if (player == null)
								continue;
							if (player.getMapId() == 782) {
								count += 1;
							}
						}
						if (count >= 20) {
							pc.sendPackets(new S_ServerMessage(1536));// �ο��� �������� �� �̻� ������ �� �����ϴ�.
							return;
			}else if (pc.getInventory().checkItem(L1ItemId.TEBEOSIRIS_KEY, 1)) { // Ű
					pc.getInventory().consumeItem(L1ItemId.TEBEOSIRIS_KEY, 1); // �����Ǵ�Ű
					htmlid = "";
					L1Teleport.teleport(pc, 32735, 32831, (short) 782, 5, true);
					pc.sendPackets(new S_ServerMessage(1471));
					// ���ø��� : ����� �͵�..�̰��� �����!! �ƴ���! ȣ�罺! ���͵��� ���������!!
				} else {
					htmlid = "tebegate3";
				}
					}
			}
			// ƼĮ ������
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200006) {
			if (s.equalsIgnoreCase("e")) {
			 if (pc.getMapId() != 0) {
				Collection<L1PcInstance> templist = L1World.getInstance()
						.getAllPlayers();
				L1PcInstance[] list = templist
						.toArray(new L1PcInstance[templist.size()]);
				int count = 0;
				for (L1PcInstance player : list) {
					if (player == null)
						continue;
					if (player.getMapId() == 784) {
						count += 1;
					}
				}
				if (count >= 20) {
					pc.sendPackets(new S_ServerMessage(1536));// �ο��� �������� �� �̻� ������ �� �����ϴ�.
					return;
				} else if (pc.getInventory().checkItem(L1ItemId.TIKAL_KEY, 1)) { // Ű
					pc.getInventory().consumeItem(L1ItemId.TIKAL_KEY, 1); // �����Ǵ�Ű
					htmlid = "";
					L1Teleport.teleport(pc, 32731, 32863, (short) 784, 5, true);
					new L1SkillUse().handleCommands(pc,
							L1SkillId.STATUS_TIKAL_BOSSJOIN, pc.getId(), pc
									.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_SPELLSC);
					pc.sendPackets(new S_ServerMessage(1485));
					// ����ĭ : ���� �̰��� �����ٴ�!! ���� ����!! ����Ŷ�!!
				}else{
					htmlid = "tikalgate3";
				}
			}
			}
			
			
		
			
			
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9213) {// �����ǺҰ�
			L1NpcInstance npc = (L1NpcInstance) obj;
			String npcName = npc.getNpcTemplate().get_name();
			if (s.equals("a")){ // ������� �ϰڽ��ϴ�.
				int[] list = new int[2];
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					switch(item.getItemId()){
					case 37:
					case 100037:
					case 42:
					case 100042:
					case 41:
					case 100041:
					case 200041:
					case 52:
					case 100052:
					case 200052:
					case 180:
					case 100180:
					case 181:
					case 100181:
					case 131:
						if (item.getEnchantLevel() == 8 && pc.getInventory().checkItem(40308, 20000000)){
							list[0] = 1;
						} else if (item.getEnchantLevel() == 9 && pc.getInventory().checkItem(40308, 30000000)){
							list[1] = 2;
						}
						break;
					default:
						break;
					}
				}
				int count = list[0] +  list[1];
				switch(count){
				case 1:
					htmlid = "rushi03"; 
					break;
				case 2:
					htmlid = "rushi02";
					break;
				case 3:
					htmlid = "rushi01";
					break;
				default:
					htmlid = "rushi04"; 
					pc.sendPackets(new S_SystemMessage("\\fY+8����� �Ƶ���(2õ) �ʿ��մϴ�."));
					pc.sendPackets(new S_SystemMessage("\\fY+9����� �Ƶ���(3õ) �ʿ��մϴ�."));
					break;
				}
			} else if (s.equals("0")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[0], pc.FireEnchant[0], pc.FireGolem[17]);
			} else if (s.equals("1")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[1], pc.FireEnchant[1], pc.FireGolem[17]);
			} else if (s.equals("2")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[2], pc.FireEnchant[2], pc.FireGolem[17]);
			} else if (s.equals("3")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[3], pc.FireEnchant[3], pc.FireGolem[17]);
			} else if (s.equals("4")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[4], pc.FireEnchant[4], pc.FireGolem[17]);
			} else if (s.equals("5")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[5], pc.FireEnchant[5], pc.FireGolem[17]);
			} else if (s.equals("6")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[6], pc.FireEnchant[6], pc.FireGolem[17]);
			} else if (s.equals("7")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[7], pc.FireEnchant[7], pc.FireGolem[17]);
			} else if (s.equals("8")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[8], pc.FireEnchant[8], pc.FireGolem[17]);
			} else if (s.equals("9")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[9], pc.FireEnchant[9], pc.FireGolem[17]);
			} else if (s.equals(":")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[10], pc.FireEnchant[10], pc.FireGolem[17]);
			} else if (s.equals(";")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[11], pc.FireEnchant[11], pc.FireGolem[17]);
			} else if (s.equals("<")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[12], pc.FireEnchant[12], pc.FireGolem[17]);
			} else if (s.equals("=")){
				FireGolemItem(pc, objid, npcName, pc.FireGolem[13], pc.FireEnchant[13], pc.FireGolem[17]);
			} else if (s.equals("A")){ // +7 ������ �ܰ�				
				FireGolemCheckItem(pc, objid, 8, 20000000, 46135, 7);
			} else if (s.equals("B")){ // +7 ��ǳ�� ����
				FireGolemCheckItem(pc, objid, 8, 20000000, 46136, 7);
			} else if (s.equals("C")){ // +7 �ĸ��� ���
				FireGolemCheckItem(pc, objid, 8, 20000000, 46137, 7);
			} else if (s.equals("D")){ // +7 õ���� ������
				FireGolemCheckItem(pc, objid, 8, 20000000, 46138, 7);
			} else if (s.equals("E")){ // +7 Ȥ���� â
				FireGolemCheckItem(pc, objid, 8, 20000000, 46139, 7);
			} else if (s.equals("F")){ // +7 ���Ű�
				FireGolemCheckItem(pc, objid, 8, 20000000, 46140, 7);
			} else if (s.equals("G")){ // +7 ��õ�� Ȱ
				FireGolemCheckItem(pc, objid, 8, 20000000, 46141, 7);
			} else if (s.equals("H")){ // +8 ������ �ܰ�
				FireGolemCheckItem(pc, objid, 9, 30000000, 46142, 8);
			} else if (s.equals("I")){ // +8 ��ǳ�� ����
				FireGolemCheckItem(pc, objid, 9, 30000000, 46143, 8);
			} else if (s.equals("J")){ // +8 �ĸ��� ���
				FireGolemCheckItem(pc, objid, 9, 30000000, 46144, 8);
			} else if (s.equals("K")){ // +8 õ���� ������
				FireGolemCheckItem(pc, objid, 9, 30000000, 46145, 8);
			} else if (s.equals("L")){ // +8 Ȥ���� â
				FireGolemCheckItem(pc, objid, 9, 30000000, 46146, 8);
			} else if (s.equals("M")){ // +8 ���Ű�
				FireGolemCheckItem(pc, objid, 9, 30000000, 46147, 8);
			} else if (s.equals("N")){ // +8 ��õȰ
				FireGolemCheckItem(pc, objid, 9, 30000000, 46148, 8);
			}
			
			
			// ������ ����
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212000) { // npcid
			// ������ �ܰ�
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().MakeCheckEnchant(5, 7)
						&& pc.getInventory().MakeCheckEnchant(6, 7)
						&& pc.getInventory().checkItem(41246, 1000)
						&& pc.getInventory().checkItem(L1ItemId.BRAVE_CRYSTAL,10)) {
					pc.getInventory().MakeDeleteEnchant(5, 7);
					pc.getInventory().MakeDeleteEnchant(6, 7);
					pc.getInventory().consumeItem(41246, 1000);
					pc.getInventory().consumeItem(L1ItemId.BRAVE_CRYSTAL, 10);
					pc.getInventory().storeItem(412002, 1);
					htmlid = "joegolem9";
				} else {
					htmlid = "joegolem15";
				}
			}
			// ��ǳ�� ����
			if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().MakeCheckEnchant(145, 7)
						&& pc.getInventory().MakeCheckEnchant(148, 7)
						&& pc.getInventory().checkItem(41246, 1000)
						&& pc.getInventory().checkItem(L1ItemId.BRAVE_CRYSTAL,
								10)) {
					pc.getInventory().MakeDeleteEnchant(145, 7);
					pc.getInventory().MakeDeleteEnchant(148, 7);
					pc.getInventory().consumeItem(41246, 1000);
					pc.getInventory().consumeItem(L1ItemId.BRAVE_CRYSTAL, 10);
					pc.getInventory().storeItem(412005, 1);
					htmlid = "joegolem10";
				} else {
					htmlid = "joegolem15";
				}
			}
			// �ĸ��� ���
			if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().MakeCheckEnchant(52, 7)
						&& pc.getInventory().MakeCheckEnchant(64, 7)
						&& pc.getInventory().checkItem(41246, 1000)
						&& pc.getInventory().checkItem(L1ItemId.BRAVE_CRYSTAL,
								10)) {
					pc.getInventory().MakeDeleteEnchant(52, 7);
					pc.getInventory().MakeDeleteEnchant(64, 7);
					pc.getInventory().consumeItem(41246, 1000);
					pc.getInventory().consumeItem(L1ItemId.BRAVE_CRYSTAL, 10);
					pc.getInventory().storeItem(412001, 1);
					htmlid = "joegolem11";
				} else {
					htmlid = "joegolem15";
				}
			}
			// ��ũ�������� ������
			if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().MakeCheckEnchant(125, 7)
						&& pc.getInventory().MakeCheckEnchant(129, 7)
						&& pc.getInventory().checkItem(41246, 1000)
						&& pc.getInventory().checkItem(L1ItemId.BRAVE_CRYSTAL,
								10)) {
					pc.getInventory().MakeDeleteEnchant(125, 7);
					pc.getInventory().MakeDeleteEnchant(129, 7);
					pc.getInventory().consumeItem(41246, 1000);
					pc.getInventory().consumeItem(L1ItemId.BRAVE_CRYSTAL, 10);
					pc.getInventory().storeItem(412003, 1);
					htmlid = "joegolem12";
				} else {
					htmlid = "joegolem15";
				}
			}
			// Ȥ���� â
			if (s.equalsIgnoreCase("E")) {
				if (pc.getInventory().MakeCheckEnchant(99, 7)
						&& pc.getInventory().MakeCheckEnchant(104, 7)
						&& pc.getInventory().checkItem(41246, 1000)
						&& pc.getInventory().checkItem(L1ItemId.BRAVE_CRYSTAL,
								10)) {
					pc.getInventory().MakeDeleteEnchant(99, 7);
					pc.getInventory().MakeDeleteEnchant(104, 7);
					pc.getInventory().consumeItem(41246, 1000);
					pc.getInventory().consumeItem(L1ItemId.BRAVE_CRYSTAL, 10);
					pc.getInventory().storeItem(412004, 1);
					htmlid = "joegolem13";
				} else {
					htmlid = "joegolem15";
				}
			}
			// ���Ű�
			if (s.equalsIgnoreCase("F")) {
				if (pc.getInventory().MakeCheckEnchant(32, 7)
						&& pc.getInventory().MakeCheckEnchant(42, 7)
						&& pc.getInventory().checkItem(41246, 1000)
						&& pc.getInventory().checkItem(L1ItemId.BRAVE_CRYSTAL,
								10)) {
					pc.getInventory().MakeDeleteEnchant(32, 7);
					pc.getInventory().MakeDeleteEnchant(42, 7);
					pc.getInventory().consumeItem(41246, 1000);
					pc.getInventory().consumeItem(L1ItemId.BRAVE_CRYSTAL, 10);
					pc.getInventory().storeItem(412000, 1);
					htmlid = "joegolem14";
				} else {
					htmlid = "joegolem15";
				}
			}
		
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212002) { // ��������
			L1NpcInstance npc = (L1NpcInstance) obj;
			if (s.equalsIgnoreCase("H")) { // ������ ���� ǥ��
				if (pc.getInventory().checkItem(408986, 32)
						&& pc.getInventory().checkItem(40308, 100000)) {
					pc.getInventory().consumeItem(408986, 32); // ǥ�İ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.getInventory().storeItem(430106, 1);
					pc.sendPackets(new S_SystemMessage("������ ������ ������ϴ�."));
				} else {
					htmlid = "sherme10";
				}
			} else if (s.equalsIgnoreCase("I")) { // ������ ���� ǥ��
				if (pc.getInventory().checkItem(408987, 32)
						&& pc.getInventory().checkItem(40308, 100000)) {
					pc.getInventory().consumeItem(408987, 32); // ǥ�İ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.getInventory().storeItem(430104, 1);
					pc.sendPackets(new S_SystemMessage("������ ������ ������ϴ�."));
				} else {
					htmlid = "sherme10";
				}
			} else if (s.equalsIgnoreCase("J")) { // ȭ���� ���� ǥ��
				if (pc.getInventory().checkItem(408988, 32)
						&& pc.getInventory().checkItem(40308, 100000)) {
					pc.getInventory().consumeItem(408988, 32); // ǥ�İ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.getInventory().storeItem(430107, 1);
					pc.sendPackets(new S_SystemMessage("ȭ���� ������ ������ϴ�."));
				} else {
					htmlid = "sherme10";
				}
			} else if (s.equalsIgnoreCase("K")) { // ǳ���� ���� ǥ��
				if (pc.getInventory().checkItem(408989, 32)
						&& pc.getInventory().checkItem(40308, 100000)) {
					pc.getInventory().consumeItem(408989, 32); // ǥ�İ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.getInventory().storeItem(430105, 1);
					pc.sendPackets(new S_SystemMessage("ǳ���� ������ ������ϴ�."));
				} else {
					htmlid = "sherme10";
				}
			} else if (s.equalsIgnoreCase("A")) { // ������ ����
				if (pc.getInventory().checkItem(430102, 1)
						&& pc.getInventory().checkItem(40308, 100000)) {

					pc.getInventory().storeItem(430106, 1);
					pc.getInventory().consumeItem(430102, 1); // ���εȸ��Ȱ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.sendPackets(new S_SystemMessage("������ ������ ������ϴ�."));
				} else {
					htmlid = "sherme1";
				}
			} else if (s.equalsIgnoreCase("B")) { // ������ ����
				if (pc.getInventory().checkItem(430100, 1)
						&& pc.getInventory().checkItem(40308, 100000)) {

					pc.getInventory().storeItem(430104, 1);
					pc.getInventory().consumeItem(430100, 1); // ���εȸ��Ȱ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.sendPackets(new S_SystemMessage("������ ������ ������ϴ�."));
				} else {
					htmlid = "sherme1";
				}
			} else if (s.equalsIgnoreCase("C")) { // ȭ���� ����
				if (pc.getInventory().checkItem(430103, 1)
						&& pc.getInventory().checkItem(40308, 100000)) {
					pc.getInventory().storeItem(430107, 1);
					pc.getInventory().consumeItem(430103, 1); // ���εȸ��Ȱ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.sendPackets(new S_SystemMessage("ȭ���� ������ ������ϴ�."));
				} else {
					htmlid = "sherme1";
				}
			} else if (s.equalsIgnoreCase("D")) { // ǳ���� ����
				if (pc.getInventory().checkItem(430101, 1)
						&& pc.getInventory().checkItem(40308, 100000)) {
					pc.getInventory().storeItem(430105, 1);
					pc.getInventory().consumeItem(430101, 1); // ���εȸ��Ȱ� �Ƶ����Һ�
					pc.getInventory().consumeItem(40308, 100000);
					pc.sendPackets(new S_SystemMessage("ǳ���� ������ ������ϴ�."));
				} else {
					htmlid = "sherme1";
				}
			} else if (s.equalsIgnoreCase("E")) { // ��������
				Random random = new Random();
				if (pc.getInventory().checkItem(430106, 1)
						&& pc.getInventory().checkItem(430104, 1)
						&& pc.getInventory().checkItem(40308, 200000)) {
					if (random.nextInt(10) > 6) { // 30%�� Ȯ���� ����
						pc.getInventory().consumeItem(430106, 1);
						pc.getInventory().consumeItem(430104, 1);
						pc.getInventory().consumeItem(40308, 200000);
						pc.getInventory().storeItem(430108, 1);
						pc.sendPackets(new S_SystemMessage("ź���� ������ ������ϴ�."));
					} else { // ������ ��� �����۸� �����
						pc.getInventory().consumeItem(430106, 1);
						pc.getInventory().consumeItem(430104, 1);
						pc.getInventory().consumeItem(40308, 200000);
						pc.sendPackets(new S_NpcChatPacket(npc,
								"���տ� ���� �Ͽ����ϴ�.", 0));
						htmlid = "sherme5";
					}
				} else { // ��ᰡ ������ ���
					htmlid = "sherme1";
				}
			} else if (s.equalsIgnoreCase("F")) { // ��������
				Random random = new Random();
				if (pc.getInventory().checkItem(430108, 1)
						&& pc.getInventory().checkItem(430105, 1)
						&& pc.getInventory().checkItem(40308, 200000)) {
					if (random.nextInt(10) > 6) { // 30%�� Ȯ���� ����
						pc.getInventory().consumeItem(430108, 1);
						pc.getInventory().consumeItem(430105, 1);
						pc.getInventory().consumeItem(40308, 200000);
						pc.getInventory().storeItem(430109, 1);
						pc.sendPackets(new S_SystemMessage("������ ������ ������ϴ�."));
					} else { // ������ ��� �����۸� �����
						pc.getInventory().consumeItem(430108, 1);
						pc.getInventory().consumeItem(430105, 1);
						pc.getInventory().consumeItem(40308, 200000);
						pc.sendPackets(new S_NpcChatPacket(npc,
								"���տ� ���� �Ͽ����ϴ�.", 0));
						htmlid = "sherme5";
					}
				} else { // ��ᰡ ������ ���
					htmlid = "sherme1";
				}
			} else if (s.equalsIgnoreCase("G")) { // ��������
				Random random = new Random();
				if (pc.getInventory().checkItem(430109, 1)
						&& pc.getInventory().checkItem(430107, 1)
						&& pc.getInventory().checkItem(40308, 200000)) {
					if (random.nextInt(10) > 6) { // 30%�� Ȯ���� ����
						pc.getInventory().consumeItem(430109, 1);
						pc.getInventory().consumeItem(430107, 1);
						pc.getInventory().consumeItem(40308, 200000);
						pc.getInventory().storeItem(430110, 1);
						pc.sendPackets(new S_SystemMessage("������ ������ ������ϴ�."));
					} else { // ������ ��� �����۸� �����
						pc.getInventory().consumeItem(430109, 1);
						pc.getInventory().consumeItem(430107, 1);
						pc.getInventory().consumeItem(40308, 200000);
						pc.sendPackets(new S_NpcChatPacket(npc,
								"���տ� ���� �Ͽ����ϴ�.", 0));
						htmlid = "sherme5";
					}
				} else { // ��ᰡ ������ ���
					htmlid = "sherme1";
				}
			}
			// ����
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212008) {
			// 7 ��� ������ 8 Ȳ�� ������
			if (s.equalsIgnoreCase("buy 7")) {
				petbuy(client, 4000000, 430111, 1);
			} else if (s.equalsIgnoreCase("buy 8")) {
				petbuy(client, 4000001, 430112, 1);
			}
			htmlid = "";
	
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039027) {// ������
			if (s.equalsIgnoreCase("a")) {
				if (pc.getInventory().checkItem(40308, 10000)) {
					if (pc.getInventory().checkItem(21132)) {
						pc.getInventory().consumeItem(40308, 10000);
						pc.getInventory().consumeItem(21132, 1);
						L1PolyMorph.undoPoly(pc);
						L1Teleport.teleport(pc, 32743, 32854, (short) 9100, 5, true);
					} else {
						htmlid = "j_html02";
					}
				} else {
					htmlid = "j_html02";
				}
			}
			if (s.equalsIgnoreCase("d")) {
				if (pc.getInventory().checkItem(500088, 1)
						&& pc.getInventory().checkItem(500089, 1)
						&& pc.getInventory().checkItem(500090, 1)
						&& pc.getInventory().checkItem(500091, 1)
						&& pc.getInventory().checkItem(500092, 1)
						&& pc.getInventory().checkItem(500093, 1)
						&& pc.getInventory().checkItem(500094, 1)
						&& pc.getInventory().checkItem(500095, 1)
						&& pc.getInventory().checkItem(500096, 1)
						&& pc.getInventory().checkItem(500097, 1)) {
					pc.getInventory().consumeItem(500088, 1);
					pc.getInventory().consumeItem(500089, 1);
					pc.getInventory().consumeItem(500090, 1);
					pc.getInventory().consumeItem(500091, 1);
					pc.getInventory().consumeItem(500092, 1);
					pc.getInventory().consumeItem(500093, 1);
					pc.getInventory().consumeItem(500094, 1);
					pc.getInventory().consumeItem(500095, 1);
					pc.getInventory().consumeItem(500096, 1);
					pc.getInventory().consumeItem(500097, 1);
					pc.getInventory().storeItem(500098, 1);// ������ �ϱ���.
				} else {
					htmlid = "j_html06"; // �ϱ��忡 ���� ������ ����.
				}
			}
			if (s.equalsIgnoreCase("c")) {
				pc.sendPackets(new S_SystemMessage("�ð��� �׾Ƹ��� ������ϴ�."));
				pc.getInventory().storeItem(21131, 1);
				htmlid = "";
			}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039030) {// �ޱ׸�Ʈ
			if (s.equalsIgnoreCase("0")) {
				if (pc.getInventory().checkItem(40308, 1000)) {
					pc.getInventory().consumeItem(40308, 1000);
					pc.getInventory().storeItem(500066, 1);
					htmlid = "hugrint2";
				} else {
					htmlid = "hugrint3";
				}
			}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4100038) { // ����
			// ������
			L1Teleport.teleport(pc, 32596, 32916, (short) 0, 5, true);
		} else {
			htmlid = L1NpcActionHelper.getInstance().NpcAction(pc, obj, s,
					htmlid);// ���׸���

		}
		if (htmlid != null && htmlid.equalsIgnoreCase("colos2")) {
			htmldata = makeUbInfoStrings(((L1NpcInstance) obj).getNpcTemplate()
					.get_npcId());
		}
		if (createitem != null) {
			boolean isCreate = true;
			for (int j = 0; j < materials.length; j++) {
				if (!pc.getInventory().checkItemNotEquipped(materials[j],
						counts[j])) {
					L1Item temp = ItemTable.getInstance().getTemplate(
							materials[j]);
					pc.sendPackets(new S_ServerMessage(337, temp.getName()));
					isCreate = false;
				}
			}
			if (isCreate) {
				int create_count = 0;
				int create_weight = 0;
				L1Item temp = null;
				for (int k = 0; k < createitem.length; k++) {
					temp = ItemTable.getInstance().getTemplate(createitem[k]);
					if (temp.isStackable()) {
						if (!pc.getInventory().checkItem(createitem[k])) {
							create_count += 1;
						}
					} else {
						create_count += createcount[k];
					}
					create_weight += temp.getWeight() * createcount[k] / 1000;
				}
				if (pc.getInventory().getSize() + create_count > 180) {
					pc.sendPackets(new S_ServerMessage(263));
					return;
				}
				if (pc.getMaxWeight() < pc.getInventory().getWeight()
						+ create_weight) {
					pc.sendPackets(new S_ServerMessage(82));
					return;
				}

				for (int j = 0; j < materials.length; j++) {
					pc.getInventory().consumeItem(materials[j], counts[j]);
				}
				L1ItemInstance item = null;
				for (int k = 0; k < createitem.length; k++) {
					item = pc.getInventory().storeItem(createitem[k],
							createcount[k]);
					if (item != null) {
						String itemName = ItemTable.getInstance().getTemplate(
								createitem[k]).getName();
						String createrName = "";
						if (obj instanceof L1NpcInstance) {
							createrName = ((L1NpcInstance) obj)
									.getNpcTemplate().get_name();
						}
						if (createcount[k] > 1) {
							pc.sendPackets(new S_ServerMessage(143,
									createrName, itemName + " ("
											+ createcount[k] + ")"));
						} else {
							pc.sendPackets(new S_ServerMessage(143,
									createrName, itemName));
						}
					}
				}
				if (success_htmlid != null) {
					pc.sendPackets(new S_NPCTalkReturn(objid, success_htmlid,
							htmldata));
				}
			} else {
				if (failure_htmlid != null) {
					pc.sendPackets(new S_NPCTalkReturn(objid, failure_htmlid,
							htmldata));
				}
			}
		}
		if (htmlid != null) {
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
		}
	}
	

	// �Ǿ
		 private void PiersItem(L1PcInstance pc, int objid, String npcName, int checkItem, int checkEnchant, int adena){
		  if (pc.getInventory().MakeCheckEnchant(checkItem, checkEnchant) && pc.getInventory().checkItem(40308, adena)){
		   pc.getInventory().MakeDeleteEnchant(checkItem, checkEnchant);
		   pc.getInventory().consumeItem(40308, adena);
		   createNewItem(pc, npcName, pc.PiersItemId[16], 1, pc.PiersItemId[17]);
		   pc.sendPackets(new S_NPCTalkReturn(objid, ""));
		   for (int i = 0; i < 20; i++){
		    pc.PiersItemId[i] = 0;
		    pc.PiersEnchant[i] = 0;
		   }
		  } else {
		   pc.sendPackets(new S_NPCTalkReturn(objid, "piers04"));
		  }
		 }
		 
		 // �Ǿ ������ üũ 
		 private void PiersCheckItem(L1PcInstance pc, int objid, int enchant, int adena, int newItem, int newItemEnchant){
		  int listcount = 0;
		  String[] list = new String[10];
		  for (int i = 0; i < 10; i++){
		     list[i] = " ";
		  }
		  for (L1ItemInstance item : pc.getInventory().getItems()) {
		   switch(item.getItemId()){
		   case 13:
		   case 81:
		   case 162:
		   case 177:
		   case 194:
		    if (item.getEnchantLevel() == enchant && pc.getInventory().checkItem(40308, adena)){
		     list[listcount] = "+" + item.getEnchantLevel() + " " + item.getName();
		     pc.PiersItemId[listcount] = item.getItemId();
		     pc.PiersEnchant[listcount] = enchant;
		     listcount++;
		    }
		    break;
		   default:
		    break;
		   }
		  }
		  if (listcount == 0 || !pc.getInventory().checkItem(40308, adena)){
		   pc.sendPackets(new S_NPCTalkReturn(objid, "piers04"));
		  } else {
		   pc.PiersItemId[16] = newItem;
		   pc.PiersItemId[17] = newItemEnchant;
		   pc.PiersItemId[18] = adena;
		   pc.sendPackets(new S_NPCTalkReturn(objid, "piers00", list));
		  }
		 }

	private void petbuy(LineageClient client, int npcid, int paytype,
			int paycount) {
		L1PcInstance pc = client.getActiveChar();
		L1PcInventory inv = pc.getInventory();
		int charisma = pc.getAbility().getTotalCha();
		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		if (pc.isCrown()) { // CROWN
			charisma += 6;
		} else if (pc.isElf()) { // ELF
			charisma += 12;
		} else if (pc.isWizard()) { // WIZ
			charisma += 6;
		} else if (pc.isDarkelf()) { // DE
			charisma += 6;
		} else if (pc.isDragonknight()) { // ����
			charisma += 6;
		} else if (pc.isIllusionist()) { // ȯ����
			charisma += 6;
		}
		charisma -= petcost;
		int petCount = charisma / 6;
		if (petCount <= 0) {
			pc.sendPackets(new S_ServerMessage(489)); // ���������� �ϴ� �ֿϵ����� �ʹ�
			// �����ϴ�.
			return;
		}
		if (pc.getInventory().checkItem(paytype, paycount)) {
			pc.getInventory().consumeItem(paytype, paycount);
			L1SpawnUtil.spawn(pc, npcid, 0, 0, false);
			L1MonsterInstance targetpet = null;
			L1ItemInstance petamu = null;
			L1PetType petType = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(pc,
					3)) {
				if (object instanceof L1MonsterInstance) {
					targetpet = (L1MonsterInstance) object;
					petType = PetTypeTable.getInstance().get(
							targetpet.getNpcTemplate().get_npcId());
					if (petType == null || targetpet.isDead()) {
						return;
					}

					if (charisma >= 6 && inv.getSize() < 180) {
						petamu = inv.storeItem(40314, 1); // ���� �ƹ·�Ʈ
						if (petamu != null) {
							new L1PetInstance(targetpet, pc, petamu.getId());
							pc.sendPackets(new S_ItemName(petamu));
						}
					}
				}
			}
		}
	}

	private String karmaLevelToHtmlId(int level) {
		if (level == 0 || level < -7 || 7 < level) {
			return "";
		}
		String htmlid = "";
		if (0 < level) {
			htmlid = "vbk" + level;
		} else if (level < 0) {
			htmlid = "vyk" + Math.abs(level);
		}
		return htmlid;
	}

	private String enterUb(L1PcInstance pc, int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		if (!ub.isActive() || !ub.canPcEnter(pc)) {
			return "colos2";
		}
		if (ub.isNowUb()) {
			return "colos1";
		}
		if (ub.getMembersCount() >= ub.getMaxPlayer()) {
			return "colos4";
		}

		ub.addMember(pc);
		L1Location loc = ub.getLocation().randomLocation(10, false);
		L1Teleport.teleport(pc, loc.getX(), loc.getY(), ub.getMapId(), 5, true);
		return "";
	}

	private String enterHauntedHouse(L1PcInstance pc) {
		// ��, �÷�����, �ʰ�, �̹��ֳ�
		if (pc.getLevel() < 30) {
			pc.sendPackets(new S_ServerMessage(1273, "30", "99"));
			return "";
		}
		if (GhostHouse.getInstance().isPlayingNow()) {
			pc.sendPackets(new S_ServerMessage(1182));
			return "";
		}
		if (GhostHouse.getInstance().getEnterMemberCount() >= 10) {
			pc.sendPackets(new S_ServerMessage(1184));
			return "";
		}
		if (GhostHouse.getInstance().isEnterMember(pc)) {
			pc.sendPackets(new S_ServerMessage(1254));
			return "";
		}
		if (DeathMatch.getInstance().isEnterMember(pc)) {
			DeathMatch.getInstance().removeEnterMember(pc);
		}
		if (PetRacing.getInstance().isEnterMember(pc)) {
			PetRacing.getInstance().removeEnterMember(pc);
		}
		GhostHouse.getInstance().addEnterMember(pc);
		return "";
	}

	/*private String enterDeathMatch(L1PcInstance pc, int npcId) {
		if (!pc.isGm()) {
			pc.sendPackets(new S_SystemMessage("������ġ�� ��Ʋ������ ����Ǿ����ϴ�."));
			return "";
		}
		if (DeathMatch.getInstance().getMiniGameStatus() == DeathMatch.getInstance().MiniGameStatus.PLAY) {
			pc.sendPackets(new S_ServerMessage(1182));
			return "";
		}
		if (DeathMatch.getInstance().getPlayerMemberCount() >= 20) {
			pc.sendPackets(new S_SystemMessage("�̹� ������ġ�� ��ȭ ���¶��."));
			return "";
		}
		if (npcId == 80087) {
			if (pc.getLevel() < 52) {
				pc.sendPackets(new S_ServerMessage(1273, "52", "99"));
				return "";
			}
			if (DeathMatch.DEATH_MATCH_PLAY_LEVEL == 1) {
				pc.sendPackets(new S_ServerMessage(1386));
				return "";
			}
		} else if (npcId == 80086) {
			if (pc.getLevel() < 30 || pc.getLevel() > 51) {
				pc.sendPackets(new S_ServerMessage(1273, "30", "51"));
				return "";
			}
			if (DeathMatch.DEATH_MATCH_PLAY_LEVEL == -1) {
				pc.sendPackets(new S_ServerMessage(1386));
				return "";
			}
		}

		if (GhostHouse.getInstance().isEnterMember(pc)) {
			GhostHouse.getInstance().removeEnterMember(pc);
		}
		if (PetRacing.getInstance().isEnterMember(pc)) {
			PetRacing.getInstance().removeEnterMember(pc);
		}
		DeathMatch.getInstance().addWaitListMember(pc);
		return "";
	}
*/
	private String enterPetMatch(L1PcInstance pc, int objid2) {
		Object[] petlist = pc.getPetList().values().toArray();
		if (petlist.length > 0) {
			pc.sendPackets(new S_ServerMessage(1187)); // ���� �ƹ·�Ʈ�� ������Դϴ�.
			return "";
		}
		if (!PetMatch.getInstance().enterPetMatch(pc, objid2)) {
			pc.sendPackets(new S_ServerMessage(1182));
		}
		return "";
	}

	private String enterPetRacing(L1PcInstance pc) {
		if (pc.getLevel() < 30) {
			pc.sendPackets(new S_ServerMessage(1273, "30", "99"));
			return "";
		}
		if (PetRacing.getInstance().getEnterMemberCount() >= 10) {
			pc.sendPackets(new S_SystemMessage("�̹� �극�̽��� ��ȭ ���¶��."));
			return "";
		}
		if (PetRacing.getInstance().isEnterMember(pc)) {
			pc.sendPackets(new S_ServerMessage(1254));
			return "";
		}
		if (GhostHouse.getInstance().isEnterMember(pc)) {
			GhostHouse.getInstance().removeEnterMember(pc);
		}
		if (DeathMatch.getInstance().isEnterMember(pc)) {
			DeathMatch.getInstance().removeEnterMember(pc);
		}
		PetRacing.getInstance().addMember(pc);
		return "";
	}

	private void summonMonster(L1PcInstance pc, String s) { // ���ո���
		String[] summonstr_list;
		int[] summonid_list;
		int[] summonlvl_list;
		int[] summoncha_list;
		int summonid = 0;
		int levelrange = 0;
		int summoncost = 0;
		summonstr_list = new String[] { "7", "263", "519", "8", "264", "520",
				"9", "265", "521", "10", "266", "522", "11", "267", "523",
				"12", "268", "524", "13", "269", "525", "14", "270", "526",
				"15", "271", "527", "16", "17", "18", "274" };
		summonid_list = new int[] { 81210, 81211, 81212, 81213, 81214, 81215,
				81216, 81217, 81218, 81219, 81220, 81221, 81222, 81223, 81224,
				81225, 81226, 81227, 81228, 81229, 81230, 81231, 81232, 81233,
				81234, 81235, 81236, 81237, 81238, 81239, 81240 };
		summonlvl_list = new int[] { 28, 28, 28, 32, 32, 32, 36, 36, 36, 40,
				40, 40, 44, 44, 44, 48, 48, 48, 52, 52, 52, 56, 56, 56, 60, 60,
				60, 64, 68, 72, 72 };
		summoncha_list = new int[] { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
				8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 14, 42, 42, 50 };
		for (int loop = 0; loop < summonstr_list.length; loop++) {
			if (s.equalsIgnoreCase(summonstr_list[loop])) {
				summonid = summonid_list[loop];
				levelrange = summonlvl_list[loop];
				summoncost = summoncha_list[loop];
				break;
			}
		}
		if (pc.getLevel() < levelrange) {
			pc.sendPackets(new S_ServerMessage(743));
			return;
		}

		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		if ((summonid == 81238 || summonid == 81239 || summonid == 81240)
				&& petcost != 0) {
			pc.sendPackets(new S_CloseList(pc.getId()));
			return;
		}
		int charisma = pc.getAbility().getTotalCha() + 6 - petcost;
		int summoncount = 0;
		if (levelrange <= 52) {
			summoncount = charisma / summoncost;
		} else if (levelrange == 56) {
			summoncount = charisma / (summoncost + 2);
		} else if (levelrange == 60) {
			summoncount = charisma / (summoncost + 4);
		} else if (levelrange == 64) {
			summoncount = charisma / (summoncost + 6);
		} else {
			summoncount = charisma / summoncost;
		}

		if (levelrange <= 52 && summoncount > 5) {
			summoncount = 5;
		} else if (levelrange == 56 && summoncount > 4) {
			summoncount = 4;
		} else if (levelrange == 60 && summoncount > 3) {
			summoncount = 3;
		} else if (levelrange == 64 && summoncount > 2) {
			summoncount = 2;
		}

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
		L1SummonInstance summon = null;
		for (int cnt = 0; cnt < summoncount; cnt++) {
			summon = new L1SummonInstance(npcTemp, pc);
			if (summonid == 81238 || summonid == 81239 || summonid == 81240) {
				summon.setPetcost(pc.getAbility().getTotalCha() + 7);
			} else {
				if (levelrange <= 52)
					summon.setPetcost(summoncost);
				else if (levelrange == 56)
					summon.setPetcost(summoncost + 2);
				else if (levelrange == 60)
					summon.setPetcost(summoncost + 4);
				else if (levelrange == 64)
					summon.setPetcost(summoncost + 6);
				else
					summoncount = charisma / summoncost;
			}
		}
		pc.sendPackets(new S_CloseList(pc.getId()));
	}

	private void poly(LineageClient clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();
		
		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
			pc.getInventory().consumeItem(L1ItemId.ADENA, 100);

			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);
		} else {
			pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
	}

	private void polyByKeplisha(LineageClient clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();
		
		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
			pc.getInventory().consumeItem(L1ItemId.ADENA, 100);

			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_KEPLISHA);
		} else {
			pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
	}

	private String sellHouse(L1PcInstance pc, int objectId, int npcId) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan == null) {
			return "";
		}
		int houseId = clan.getHouseId();
		if (houseId == 0) {
			return "";
		}
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		if (npcId != keeperId) {
			return "";
		}
		if (!pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(518));
			System.out.print("1111");
			return "";
		}
		if (pc.getId() != clan.getLeaderId()) {
			pc.sendPackets(new S_ServerMessage(518));
			System.out.print("2222");
			return "";
		}
		if (house.isOnSale()) {
			System.out.print("3333");
			return "agonsale";
		}

		pc.sendPackets(new S_SellHouse(objectId, String.valueOf(houseId)));
		return null;
	}

	private void openCloseDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		// int doorId = 0;
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					L1DoorInstance door1 = null;
					L1DoorInstance door2 = null;
					L1DoorInstance door3 = null;
					L1DoorInstance door4 = null;
					for (L1DoorInstance door : DoorSpawnTable.getInstance()
							.getDoorList()) {
						if (door.getKeeperId() == keeperId) {
							if (door1 == null) {
								door1 = door;
								continue;
							}
							if (door2 == null) {
								door2 = door;
								continue;
							}
							if (door3 == null) {
								door3 = door;
								continue;
							}
							if (door4 == null) {
								door4 = door;
								break;
							}
						}
					}
					if (door1 != null) {
						if (s.equalsIgnoreCase("open")) {
							door1.open();
						} else if (s.equalsIgnoreCase("close")) {
							door1.close();
						}
					}
					if (door2 != null) {
						if (s.equalsIgnoreCase("open")) {
							door2.open();
						} else if (s.equalsIgnoreCase("close")) {
							door2.close();
						}
					}
					if (door3 != null) {
						if (s.equalsIgnoreCase("open")) {
							door3.open();
						} else if (s.equalsIgnoreCase("close")) {
							door3.close();
						}
					}
					if (door4 != null) {
						if (s.equalsIgnoreCase("open")) {
							door4.open();
						} else if (s.equalsIgnoreCase("close")) {
							door4.close();
						}
					}
				}
			}
		}
	}

	/** ����������Ʈ* */
	private void openCloseGate(L1PcInstance pc, int keeperId, boolean isOpen) {
		boolean isNowWar = false;
		int pcCastleId = 0;
		if (pc.getClanid() != 0) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				pcCastleId = clan.getCastleId();
			}
		}
		int rank = pc.getClanRank();
		if (keeperId == 70656 || keeperId == 70549 || keeperId == 70985) {
			if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
					return;
				}
				if (rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"��ȣ��� ��� �̻� ������ ������ �� �ֽ��ϴ�."));
					return;
				}
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"NPC�� �����ϰ� �ִ� �� ���� �� �� �����ϴ�."));
				return;
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.KENT_CASTLE_ID);
		} else if (keeperId == 70600) { // OT
			if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
					return;
				}
				if (rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"��ȣ��� ��� �̻� ������ ������ �� �ֽ��ϴ�."));
					return;
				}
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"NPC�� �����ϰ� �ִ� �� ���� �� �� �����ϴ�."));
				return;
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.OT_CASTLE_ID);
		} else if (keeperId == 70778 || keeperId == 70987 || keeperId == 70687) {
			if (isExistDefenseClan(L1CastleLocation.WW_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.WW_CASTLE_ID) {
					return;
				}
				if (rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"��ȣ��� ��� �̻� ������ ������ �� �ֽ��ϴ�."));
					return;
				}
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"NPC�� �����ϰ� �ִ� �� ���� �� �� �����ϴ�."));
				return;
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.WW_CASTLE_ID);
		} else if (keeperId == 70817 || keeperId == 70800 || keeperId == 70988
				|| keeperId == 70990 || keeperId == 70989 || keeperId == 70991) {
			if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
					return;
				}
				if (rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"��ȣ��� ��� �̻� ������ ������ �� �ֽ��ϴ�."));
					return;
				}
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"NPC�� �����ϰ� �ִ� �� ���� �� �� �����ϴ�."));
				return;
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.GIRAN_CASTLE_ID);
		} else if (keeperId == 70863 || keeperId == 70992 || keeperId == 70862) {
			if (isExistDefenseClan(L1CastleLocation.HEINE_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.HEINE_CASTLE_ID) {
					return;
				}
				if (rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"��ȣ��� ��� �̻� ������ ������ �� �ֽ��ϴ�."));
					return;
				}
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"NPC�� �����ϰ� �ִ� �� ���� �� �� �����ϴ�."));
				return;
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.HEINE_CASTLE_ID);
		} else if (keeperId == 70995 || keeperId == 70994 || keeperId == 70993) {
			if (isExistDefenseClan(L1CastleLocation.DOWA_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.DOWA_CASTLE_ID) {
					return;
				}
				if (rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"��ȣ��� ��� �̻� ������ ������ �� �ֽ��ϴ�."));
					return;
				}
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"NPC�� �����ϰ� �ִ� �� ���� �� �� �����ϴ�."));
				return;
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.DOWA_CASTLE_ID);
		} else if (keeperId == 70996) {
			if (isExistDefenseClan(L1CastleLocation.ADEN_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.ADEN_CASTLE_ID) {
					return;
				}
				if (rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_PRINCE) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"��ȣ��� ��� �̻� ������ ������ �� �ֽ��ϴ�."));
					return;
				}
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"NPC�� �����ϰ� �ִ� �� ���� �� �� �����ϴ�."));
				return;
			}
			isNowWar = WarTimeController.getInstance().isNowWar(
					L1CastleLocation.ADEN_CASTLE_ID);
		}
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (door.getKeeperId() == keeperId) {
				if (isNowWar && door.getMaxHp() > 1) {
				} else {
					if (isOpen) {
						door.open();
					} else {
						door.close();
					}
				}
			}
		}
	}

	/** ����������Ʈ* */
	private boolean isExistDefenseClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		return isExistDefenseClan;
	}

	private void expelOtherClan(L1PcInstance clanPc, int keeperId) {
		int houseId = 0;
		for (L1House house : HouseTable.getInstance().getHouseTableList()) {
			if (house.getKeeperId() == keeperId) {
				houseId = house.getHouseId();
			}
		}
		if (houseId == 0) {
			return;
		}

		int[] loc = new int[3];
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (L1HouseLocation.isInHouseLoc(houseId, pc.getX(), pc.getY(), pc
					.getMapId())
					&& clanPc.getClanid() != pc.getClanid() && !pc.isGm()) {
				loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
				if (pc != null) {
					L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5,
							true);
				}
			}
		}
	}
	private void payFee(L1PcInstance pc, L1NpcInstance npc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 2000)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 2000);
						TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
						Calendar cal = Calendar.getInstance(tz);
						cal.add(Calendar.DATE, Config.HOUSE_TAX_INTERVAL);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						house.setTaxDeadline(cal);
						HouseTable.getInstance().updateHouse(house);
					} else {
						S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
						pc.sendPackets(s_chatpacket);
					}
				}
			}
		}
	}
	
	 private void Zendor(L1PcInstance pc, int objid, int Number){ // ������ by��
		  int[][] Buff = null;
		  Buff = new int[][] {
		    { 43, 79, 3, 160, 211, 216, 148 }, // �ٰŸ�
		    { 43, 79, 3, 160, 211, 216, 149 }, // ���Ÿ�
		    { 43, 79, 3, 160, 211, 216, 206 }, // ��ȥ����
		    };
		  L1SkillUse l1skilluse = new L1SkillUse();
		  pc.setBuffnoch(1);
		  for (int i = 0; i < Buff[Number].length; i++) {
		   l1skilluse.handleCommands(pc, Buff[Number][i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		  }
		  pc.setBuffnoch(0);
		  pc.sendPackets(new S_NPCTalkReturn(objid, "bs_done"));
		 }

	private String[] makeHouseTaxStrings(L1PcInstance pc, L1NpcInstance npc) {
		String name = npc.getNpcTemplate().get_name();
		String[] result;
		result = new String[] { name, "2000", "1", "1", "00" };
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					Calendar cal = house.getTaxDeadline();
					int month = cal.get(Calendar.MONTH) + 1;
					int day = cal.get(Calendar.DATE);
					int hour = cal.get(Calendar.HOUR_OF_DAY);
					result = new String[] { name, "2000",
							String.valueOf(month), String.valueOf(day),
							String.valueOf(hour) };
				}
			}
		}
		return result;
	}

	private String[] makeWarTimeStrings(int castleId) {
		L1Castle castle = CastleTable.getInstance().getCastleTable(castleId);
		if (castle == null) {
			return null;
		}
		Calendar warTime = castle.getWarTime();
		int year = warTime.get(Calendar.YEAR);
		int month = warTime.get(Calendar.MONTH) + 1;
		int day = warTime.get(Calendar.DATE);
		int hour = warTime.get(Calendar.HOUR_OF_DAY);
		int minute = warTime.get(Calendar.MINUTE);
		String[] result;
		if (castleId == L1CastleLocation.OT_CASTLE_ID) {
			result = new String[] { String.valueOf(year),
					String.valueOf(month), String.valueOf(day),
					String.valueOf(hour), String.valueOf(minute) };
		} else {
			result = new String[] { "", String.valueOf(year),
					String.valueOf(month), String.valueOf(day),
					String.valueOf(hour), String.valueOf(minute) };
		}
		return result;
	}

	private String getYaheeAmulet(L1PcInstance pc, L1NpcInstance npc, String s) {
		int[] amuletIdList = { 20358, 20359, 20360, 20361, 20362, 20363, 20364,
				20365 };
		int amuletId = 0;
		int karmalevel = pc.getKarmaLevel();
		int bug = 0;

		L1ItemInstance item = null;
		String htmlid = null;
		if (s.equalsIgnoreCase("1")) {
			bug = -1;
			amuletId = amuletIdList[0];
		} else if (s.equalsIgnoreCase("2")) {
			bug = -2;
			amuletId = amuletIdList[1];
		} else if (s.equalsIgnoreCase("3")) {
			bug = -3;
			amuletId = amuletIdList[2];
		} else if (s.equalsIgnoreCase("4")) {
			bug = -4;
			amuletId = amuletIdList[3];
		} else if (s.equalsIgnoreCase("5")) {
			bug = -5;
			amuletId = amuletIdList[4];
		} else if (s.equalsIgnoreCase("6")) {
			bug = -6;
			amuletId = amuletIdList[5];
		} else if (s.equalsIgnoreCase("7")) {
			bug = -7;
			amuletId = amuletIdList[6];
		} else if (s.equalsIgnoreCase("8")) {
			bug = -8;
			amuletId = amuletIdList[7];
		}
		if (amuletId != 0) {
			if (karmalevel == bug) {
				item = pc.getInventory().storeItem(amuletId, 1);
				if (item != null) {
					pc.sendPackets(new S_ServerMessage(143, npc
							.getNpcTemplate().get_name(), item.getLogName())); // \f1%0��%1��
					// �־����ϴ�.
				}
			} else {
				pc.sendPackets(new S_SystemMessage("���� ��ȣ���� �����մϴ�."));
			}
			for (int id : amuletIdList) {
				if (id == amuletId) {
					break;
				}
				if (pc.getInventory().checkItem(id)) {
					pc.getInventory().consumeItem(id, 1);
				}
			}
			htmlid = "";
		}
		return htmlid;
	}

	private String getBarlogEarring(L1PcInstance pc, L1NpcInstance npc, String s) {
		int[] earringIdList = { 21020, 21021, 21022, 21023, 21024, 21025,
				21026, 21027 };
		int earringId = 0;
		int karmalevel = pc.getKarmaLevel();
		int bug = 0;
		L1ItemInstance item = null;
		String htmlid = null;
		if (s.equalsIgnoreCase("1")) {
			earringId = earringIdList[0];
			bug = 1;
		} else if (s.equalsIgnoreCase("2")) {
			earringId = earringIdList[1];
			bug = 2;
		} else if (s.equalsIgnoreCase("3")) {
			earringId = earringIdList[2];
			bug = 3;
		} else if (s.equalsIgnoreCase("4")) {
			earringId = earringIdList[3];
			bug = 4;
		} else if (s.equalsIgnoreCase("5")) {
			earringId = earringIdList[4];
			bug = 5;
		} else if (s.equalsIgnoreCase("6")) {
			earringId = earringIdList[5];
			bug = 6;
		} else if (s.equalsIgnoreCase("7")) {
			earringId = earringIdList[6];
			bug = 7;
		} else if (s.equalsIgnoreCase("8")) {
			earringId = earringIdList[7];
			bug = 8;
		}
		if (earringId != 0) {
			if (karmalevel == bug) {
				item = pc.getInventory().storeItem(earringId, 1);
				if (item != null) {
					pc.sendPackets(new S_ServerMessage(143, npc
							.getNpcTemplate().get_name(), item.getLogName()));
				}
			} else {
				pc.sendPackets(new S_SystemMessage("���� ��ȣ���� �����մϴ�."));
			}

			for (int id : earringIdList) {
				if (id == earringId) {
					break;
				}
				if (pc.getInventory().checkItem(id)) {
					pc.getInventory().consumeItem(id, 1);
				}
			}
			htmlid = "";
		}
		return htmlid;
	}

	private String[] makeUbInfoStrings(int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		return ub.makeUbInfoStrings();
	}

	private String talkToDimensionDoor(L1PcInstance pc, L1NpcInstance npc,
			String s) {
		String htmlid = "";
		int protectionId = 0;
		int sealId = 0;
		int locX = 0;
		int locY = 0;
		short mapId = 0;
		if (npc.getNpcTemplate().get_npcId() == 80059) {
			protectionId = 40909;
			sealId = 40913;
			locX = 32773;
			locY = 32835;
			mapId = 607;
		} else if (npc.getNpcTemplate().get_npcId() == 80060) {
			protectionId = 40912;
			sealId = 40916;
			locX = 32757;
			locY = 32842;
			mapId = 606;
		} else if (npc.getNpcTemplate().get_npcId() == 80061) {
			protectionId = 40910;
			sealId = 40914;
			locX = 32830;
			locY = 32822;
			mapId = 604;
		} else if (npc.getNpcTemplate().get_npcId() == 80062) {
			protectionId = 40911;
			sealId = 40915;
			locX = 32835;
			locY = 32822;
			mapId = 605;
		}

		if (s.equalsIgnoreCase("a")) {
			L1Teleport.teleport(pc, locX, locY, mapId, 5, true);
			htmlid = "";
		} else if (s.equalsIgnoreCase("b")) {
			L1ItemInstance item = pc.getInventory().storeItem(protectionId, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate()
						.get_name(), item.getLogName()));
			}
			htmlid = "";
		} else if (s.equalsIgnoreCase("c")) {
			htmlid = "wpass07";
		} else if (s.equalsIgnoreCase("d")) {
			if (pc.getInventory().checkItem(sealId)) {
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
		} else if (s.equalsIgnoreCase("e")) {
			htmlid = "";
		} else if (s.equalsIgnoreCase("f")) {
			if (pc.getInventory().checkItem(protectionId)) {
				pc.getInventory().consumeItem(protectionId, 1);
			}
			if (pc.getInventory().checkItem(sealId)) {
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
			htmlid = "";
		}
		return htmlid;
	}

	private boolean isNpcSellOnly(L1NpcInstance npc) {
		int npcId = npc.getNpcTemplate().get_npcId();
		String npcName = npc.getNpcTemplate().get_name();
		if (npcId == 70027 || "�Ƶ����".equals(npcName)) {
			return true;
		}
		return false;
	}

	private void getBloodCrystalByKarma(L1PcInstance pc, L1NpcInstance npc,
			String s) {
		L1ItemInstance item = null;

		if (s.equalsIgnoreCase("1")) {
			pc.addKarma((int) (500 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate()
						.get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(1081));
		} else if (s.equalsIgnoreCase("2")) {
			pc.addKarma((int) (5000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 10);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate()
						.get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(1081));
		} else if (s.equalsIgnoreCase("3")) {
			pc.addKarma((int) (50000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 100);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate()
						.get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(1081));
		}
	}

	private void getSoulCrystalByKarma(L1PcInstance pc, L1NpcInstance npc,
			String s) {
		L1ItemInstance item = null;

		if (s.equalsIgnoreCase("1")) {
			pc.addKarma((int) (-500 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate()
						.get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(1080));
		} else if (s.equalsIgnoreCase("2")) {
			pc.addKarma((int) (-5000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 10);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate()
						.get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(1080));
		} else if (s.equalsIgnoreCase("3")) {
			pc.addKarma((int) (-50000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 100);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate()
						.get_name(), item.getLogName()));
			}
			pc.sendPackets(new S_ServerMessage(1080));
		}
	}

	private void StatInitialize(L1PcInstance pc) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(ADVANCE_SPIRIT)) {
			pc.getSkillEffectTimerSet().removeSkillEffect(ADVANCE_SPIRIT);
		}
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc
				.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);
						if (pc.getWeapon() != null)
			pc.getInventory().setEquipped(pc.getWeapon(), false, false, false);
		pc.getInventory().takeoffEquip(945);
		pc.sendPackets(new S_CharVisualUpdate(pc));
		for (L1ItemInstance armor : pc.getInventory().getItems()) {
			for (int type = 0; type <= 12; type++) {
				if (armor != null) {
					pc.getInventory().setEquipped(armor, false, false, false);
				}
			}
		}
		pc.setReturnStat(pc.getExp());
		pc.sendPackets(new S_SPMR(pc));
		pc.sendPackets(new S_OwnCharAttrDef(pc));
		pc.sendPackets(new S_OwnCharStatus2(pc));
		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
        pc.StatReturnCK = true;
		try {
			pc.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "C_NPCAction[�����ʱ�ȭ]����", e);
		}
	}

	private void GiveSoldier(L1PcInstance pc, int objid) {
		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		if (petcost != 0) {
			pc.sendPackets(new S_CloseList(pc.getId()));
			return;
		}
		RealTime r = new RealTime();
		int time = r.getSeconds();

		FastTable<L1CharSoldier> list = CharSoldierTable.getInstance()
				.getCharSoldier(pc.getId(), time);

		L1CharSoldier t;

		int d = CharSoldierTable.getInstance().SoldierCalculate(pc.getId());
		if (d > 0 && list.size() == 0) {
			pc.sendPackets(new S_NPCTalkReturn(objid, "colbert2"));
			return;
		} else if (d == 0) {
			pc.sendPackets(new S_NPCTalkReturn(objid, "colbert3"));
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			t = list.get(i);
			int a = t.getSoldierNpc();
			int b = t.getSoldierCount();
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(a);
			@SuppressWarnings("unused")
			L1SummonInstance summon = null;
			for (int c = 0; c < b; c++) {
				summon = new L1SummonInstance(npcTemp, pc);
			}
		}
		t = null;
		pc.sendPackets(new S_CloseList(pc.getId()));
	}

	private void castleGateStatus(L1PcInstance pc, int objid) {
		String htmlid = null;
		String doorStatus = null;
		String[] htmldata = null;
		String[] doorName = null;
		String doorCrack = null;
		int[] doornpc = null;

		switch (pc.getClan().getCastleId()) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			htmlid = "orville5";
			doornpc = new int[] { 2031, 2032, 2033, 2034, 2035, 2030 };
			doorName = new String[] { "$1399", "$1400", "$1401", "$1402",
					"$1403", "$1386" };
			htmldata = new String[12];
			break;
		case 5:
		case 6:
			htmlid = "potempin5";
			doornpc = new int[] { 2051, 2052, 2050 }; // ����, ����, ������
			doorName = new String[] { "$1399", "$1603", "$1386" };
			htmldata = new String[4];
			break;
		}

		for (int i = 0; i < doornpc.length; i++) {
			L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(
					doornpc[i]);
			if (door.getOpenStatus() == ActionCodes.ACTION_Close)
				doorStatus = "$442"; // ����
			else if (door.getOpenStatus() == ActionCodes.ACTION_Open)
				doorStatus = "$443"; // ����
			htmldata[i] = "" + doorName[i] + "" + doorStatus + "";
			// System.out.println("������� " + door.getCrackStatus());
			switch (door.getCrackStatus()) {
			case 0:
				doorCrack = "$439";
				break;
			case 1:
				doorCrack = "$438";
				break;
			case 2:
				doorCrack = "$437";
				break;
			case 3:
				doorCrack = "$436";
				break;
			case 4:
				doorCrack = "$435";
				break;
			default:
				doorCrack = "$434";
				break;
			}
			htmldata[i + doornpc.length] = "" + doorName[i] + "" + doorCrack
					+ "";
		}
		pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
	}

	private void repairGate(L1PcInstance pc, int npcId, int castleId) {
		if (pc.getClan().getCastleId() != castleId)
			return;
		if (WarTimeController.getInstance().isNowWar(castleId))
			return;
		L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(npcId);
		door.repairGate();
	}

	private void repairAutoGate(L1PcInstance pc, int order) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int castleId = clan.getCastleId();
			if (castleId != 0) {
				if (!WarTimeController.getInstance().isNowWar(castleId)) {
					for (L1DoorInstance door : DoorSpawnTable.getInstance()
							.getDoorList()) {
						if (L1CastleLocation.checkInWarArea(castleId, door)) {
							door.setAutoStatus(order);
						}
					}
					pc.sendPackets(new S_ServerMessage(990));
				} else {
					pc.sendPackets(new S_ServerMessage(991));
				}
			}
		}
	}

	private boolean usePolyScroll(L1PcInstance pc, int itemId, String s) {
		int time = 0;
		if (itemId == 40088 || itemId == 40096) {
			time = 1800;
		} else if (itemId == 140088) {
			time = 2100;
		}

		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		L1ItemInstance item = pc.getInventory().findItemId(itemId);
		boolean isUseItem = false;
		if (poly != null || s.equals("none")) {
			if (s.equals("none")) {
				if (pc.getGfxId().getTempCharGfx() == 6034
						|| pc.getGfxId().getTempCharGfx() == 6035) {
					isUseItem = true;
				} else {
					pc.getSkillEffectTimerSet().removeSkillEffect(
							L1SkillId.SHAPE_CHANGE);
					isUseItem = true;
				}
			} else if (poly.getMinLevel() == 100) {
				isUseItem = true;
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time,
						L1PolyMorph.MORPH_BY_ITEMMAGIC);
				isUseItem = true;
			}
		}
		if (isUseItem) {
			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(new S_ServerMessage(181));
		}
		return isUseItem;
	}

	// /////////////////////////////�ҷ����̺�Ʈ
	@SuppressWarnings("unused")
	private boolean createNewItem(L1PcInstance pc, int item_id, int count,
			int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		item.setCount(count);

		item.setEnchantLevel(enchant);
		if (item != null) {
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(),
						pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			return true;
		} else {
			return false;
		}
	}
	
	


	// /////////////////////////////�ҷ����̺�Ʈ
	private boolean isTwoLogin(L1PcInstance c) {
		for(L1PcInstance target : L1World.getInstance().getAllPlayersToArray()){
			if(target.noPlayerCK) continue;
			int count = 0;
			if(c.getId() == target.getId()){
				count++;
				if(count > 1){
					c.getNetConnection().kick();
					c.getNetConnection().close();
					target.getNetConnection().kick();
					target.getNetConnection().close();
					return true;
				}
			}
			else if(c.getId() != target.getId()){
				if(c.getAccountName().equalsIgnoreCase(target.getAccountName())) {
					if(!AutoShopManager.getInstance().isAutoShop(target.getId())){
						c.getNetConnection().kick();
						c.getNetConnection().close();
						target.getNetConnection().kick();
						target.getNetConnection().close();
						return true;
					}			
				}
			}
		}
		return false;
	}
	

	// ������ �Ұ�	
	private void FireGolemItem(L1PcInstance pc, int objid, String npcName, int checkItem, int checkEnchant, int adena){
		  if (pc.getInventory().MakeCheckEnchant(checkItem, checkEnchant) && pc.getInventory().checkItem(40308, adena)){
		   pc.getInventory().MakeDeleteEnchant(checkItem, checkEnchant);
		   pc.getInventory().consumeItem(40308, adena);
		   createNewItem(pc, npcName, pc.FireGolem[15], 1, pc.FireGolem[16]);
		   pc.sendPackets(new S_NPCTalkReturn(objid, ""));
		   for (int i = 0; i < 18; i++){
		    pc.FireGolem[i] = 0;
		    pc.FireEnchant[i] = 0;
		   }
		  } else {
		   pc.sendPackets(new S_NPCTalkReturn(objid, "rushi04"));
		  }
		 }
	// ��þƮ üũ
	private void FireGolemCheckItem(L1PcInstance pc, int objid, int enchant, int adena, int newItem, int newItemEnchant){
		int listcount = 0;
		String[] list = new String[14];
		for (int i = 0; i < 14; i++){
		   list[i] = " ";
		}
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			switch(item.getItemId()){
			case 37:
			case 100037:
			case 42:
			case 100042:
			case 41:
			case 100041:
			case 200041:
			case 52:
			case 100052:
			case 200052:
			case 180:
			case 100180:
			case 181:
			case 100181:
			case 131:
				if (item.getEnchantLevel() == enchant){
					list[listcount] = "+" + item.getEnchantLevel() + " " + item.getName();
					pc.FireGolem[listcount] = item.getItemId();
					pc.FireEnchant[listcount] = item.getEnchantLevel();
					pc.FireGolem[15] = newItem;
					pc.FireGolem[16] = newItemEnchant;
					pc.FireGolem[17] = adena;
					listcount++;
					pc.sendPackets(new S_NPCTalkReturn(objid, "rushi00", list));
				}
				break;
			default:
				break;
			}
		}
		if (listcount == 0 || !pc.getInventory().checkItem(40308, adena)){
			pc.sendPackets(new S_NPCTalkReturn(objid, "rushi04"));
		}
	}

	@SuppressWarnings("unused")
	private boolean createNewItem(L1PcInstance pc, String npcName, int item_id,
			int count, int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		item.setCount(count);
		item.setEnchantLevel(enchant);
		if (item != null) {
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(),
						pc.getMapId()).storeItem(item);
			}
			pc
					.sendPackets(new S_ServerMessage(143, npcName, item
							.getLogName()));
			return true;
		} else {
			return false;
		}
	}

	private void UbRank(L1PcInstance pc, L1NpcInstance npc) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(
				npc.getNpcTemplate().get_npcId());
		String[] htmldata = null;
		htmldata = new String[11];
		htmldata[0] = npc.getNpcTemplate().get_name();
		String htmlid = "colos3";
		int i = 1;

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM ub_rank WHERE ub_id=? order by score desc limit 10");
			pstm.setInt(1, ub.getUbId());
			rs = pstm.executeQuery();
			while (rs.next()) {
				htmldata[i] = rs.getString(2) + " : "
						+ String.valueOf(rs.getInt(3));
				i++;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "C_NPCAction[UbRank]Error", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), htmlid, htmldata));
	}

	@Override
	public String getType() {
		return C_NPC_ACTION;
	}

}
