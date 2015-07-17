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
package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.BuffSystem;
import l1j.server.Warehouse.PrivateWarehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TradeAddItem;
import l1j.server.server.serverpackets.S_TradeStatus;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.utils.SQLUtil;
import server.CodeLogger;
import server.manager.eva;

// Referenced classes of package l1j.server.server.model:
// L1Trade

public class L1Trade {
	/** ��¥ �� �ð� ��� **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int sec = rightNow.get(Calendar.SECOND);
	int year = rightNow.get(Calendar.YEAR);
	int month =  rightNow.get(Calendar.MONTH)+1;
	String totime = "[" + year + ":" + month + ":" + day + "]";
	String totime1 = "[" + hour + ":" + min + ":" + sec + "]";
	String date = +year+"_"+month+"_"+day;
	
	private static L1Trade _instance;

	public L1Trade() {
	}

	public static L1Trade getInstance() {
		if (_instance == null) {
			_instance = new L1Trade();
		}
		return _instance;
	}

	// ////////////////////////////ĳ���ͱ�ȯ//////////////////////////////////////////
	private boolean chaTradeChk(L1PcInstance pc, L1PcInstance tradingPartner,
			int itemid) {
		if (tradingPartner.getNetConnection().getAccount().countCharacters() >= tradingPartner
				.getNetConnection().getAccount().getCharSlot()) {
			tradingPartner.setChaTradeSlot(true);
		}
		if (itemid == 2100904) { // �ϱ�
			if (pc.getLevel() > 69) {
				pc.sendPackets(new S_SystemMessage("��� ĳ���ͱ�ȯ��ǥ�� �̿��ϼ���."));
				return false;
			}
		} else { // ���
			if (pc.getLevel() < 70) {
				pc.sendPackets(new S_SystemMessage("�ϱ� ĳ���ͱ�ȯ��ǥ�� �̿��ϼ���."));
				return false;
			}
		}
		return true;
	}

	private boolean chaTrade(L1PcInstance pc, L1PcInstance tradingPartner,
			L1ItemInstance item) {
		if (!chaTradeChk(pc, tradingPartner, item.getItemId()))
			return false;

		String title = null;
		switch (pc.getType()) {
		case 0:
			title = "����";
			break;
		case 1:
			title = "���";
			break;
		case 2:
			title = "����";
			break;
		case 3:
			title = "������";
			break;
		case 4:
			title = "��ũ����";
			break;
		case 5:
			title = "����";
			break;
		case 6:
			title = "ȯ����";
			break;
		}
		String chatText = "�ŷ� ��� ĳ������ ������ [" + title + "] ������ ["
				+ pc.getLevel() + "] ������ ��� ������ ["
				+ pc.getAbility().getElixirCount()
				+ "]�Դϴ�. ĳ���� �ŷ��� �ŷ� �Ϸ� �� �ڵ����� ������ ����˴ϴ�.";

		S_ChatPacket s_chatpacket = new S_ChatPacket(tradingPartner, chatText,
				Opcodes.S_OPCODE_NORMALCHAT, 2);
		if (!tradingPartner.getExcludingList().contains(
				tradingPartner.getName())) {
			tradingPartner.sendPackets(s_chatpacket);
		}

		chatText = "���� ĳ���ʹ� ������ �������� �Ѿ�� �ŷ� �� �ڵ����� ������ ����Ǹ� ������ �ŷ��Ƶ����� ���� â�� ����˴ϴ�.";

		S_ChatPacket s_chatpacket1 = new S_ChatPacket(pc, chatText,
				Opcodes.S_OPCODE_NORMALCHAT, 2);
		if (!pc.getExcludingList().contains(pc.getName())) {
			pc.sendPackets(s_chatpacket1);
		}

		return true;
	}

	// ////////////////////////////ĳ���ͱ�ȯ//////////////////////////////////////////
	public void TradeAddItem(L1PcInstance player, int itemid, int itemcount) {
		L1Object trading_partner = L1World.getInstance().findObject(
				player.getTradeID());
		L1ItemInstance l1iteminstance = player.getInventory().getItem(itemid);
		if (l1iteminstance != null && trading_partner != null) {
			if (trading_partner instanceof L1PcInstance) {
				L1PcInstance tradepc = (L1PcInstance) trading_partner;
				if (!l1iteminstance.isEquipped()) {
					if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) { // ������
						// ����
						// �߰�
						player.sendPackets(new S_TradeStatus(1));
						tradepc.sendPackets(new S_TradeStatus(1));
						player.setTradeOk(false);
						tradepc.setTradeOk(false);
						player.setTradeID(0);
						tradepc.setTradeID(0);
						return;
					}
					// ////////////////////////////ĳ���ͱ�ȯ//////////////////////////////////////////
					if (l1iteminstance.getItemId() == 2100903
							|| l1iteminstance.getItemId() == 2100904) {
						if (!chaTrade(player, tradepc, l1iteminstance)) {
							return;
						}

						List<?> player_tradelist = player
								.getTradeWindowInventory().getItems();
						int player_tradecount = player
								.getTradeWindowInventory().getSize();
						List<?> trading_partner_tradelist = tradepc
								.getTradeWindowInventory().getItems();
						int trading_partner_tradecount = tradepc
								.getTradeWindowInventory().getSize();
						L1ItemInstance l1iteminstance1 = null;
						for (int cnt = 0; cnt < player_tradecount; cnt++) {
							l1iteminstance1 = (L1ItemInstance) player_tradelist
									.get(cnt);
							if (l1iteminstance1.getItemId() == 2100903
									|| l1iteminstance1.getItemId() == 2100904) {
								player.sendPackets(new S_SystemMessage(
										"ĳ����ȯ��ǥ�� �ϳ��� �ø��� �ֽ��ϴ�."));
								return;
							}
						}
						String title = null;
						switch (player.getType()) {
						case 0:
							title = "����";
							break;
						case 1:
							title = "���";
							break;
						case 2:
							title = "����";
							break;
						case 3:
							title = "������";
							break;
						case 4:
							title = "��ũ����";
							break;
						case 5:
							title = "����";
							break;
						case 6:
							title = "ȯ����";
							break;
						}
						player.getInventory().tradeItem(l1iteminstance,
								itemcount, player.getTradeWindowInventory());
						player.sendPackets(new S_TradeAddItem(l1iteminstance,
								itemcount, 0, title, player.getLevel()));
						tradepc.sendPackets(new S_TradeAddItem(l1iteminstance,
								itemcount, 1, title, player.getLevel()));
					} else {

						// ////////////////////////////ĳ���ͱ�ȯ//////////////////////////////////////////
						player.getInventory().tradeItem(l1iteminstance,
								itemcount, player.getTradeWindowInventory());
						player.sendPackets(new S_TradeAddItem(l1iteminstance,
								itemcount, 0));
						tradepc.sendPackets(new S_TradeAddItem(l1iteminstance,
								itemcount, 1));
					}
				}
			} else if (trading_partner instanceof L1BuffNpcInstance) {
				L1BuffNpcInstance tradenpc = (L1BuffNpcInstance) trading_partner;
				if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) { // ������
					// ����
					// �߰�
					player.sendPackets(new S_TradeStatus(1));
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					return;
				}
				if (l1iteminstance.getItemId() != 40308) {
					player.sendPackets(new S_TradeStatus(1));
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(
							tradenpc, "�Ƶ����� �÷��ּ���.", 0));
					return;
				}

				if (l1iteminstance.getItemId() == 40308 && itemcount != 100000
						&& tradenpc.getNpcTemplate().get_npcId() == 7000067) {
					player.sendPackets(new S_TradeStatus(1));
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(
							tradenpc, "����� 10���Ƶ� �Դϴ�.", 0));
				} else if (l1iteminstance.getItemId() == 40308
						&& itemcount != 200000
						&& tradenpc.getNpcTemplate().get_npcId() == 7000070) {
					player.sendPackets(new S_TradeStatus(1));
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(
							tradenpc, "����� 20���Ƶ� �Դϴ�.", 0));
				} else {
					player.getInventory().tradeItem(l1iteminstance, itemcount,
							player.getTradeWindowInventory());
					player.sendPackets(new S_TradeAddItem(l1iteminstance,
							itemcount, 0));
				}
			}
		}
	}

	private void chaTradeAccount(String accountName, String chaName) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE characters SET account_name=? WHERE char_name=?");
			pstm.setString(1, accountName);
			pstm.setString(2, chaName);
			pstm.execute();
		} catch (Exception e) {

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void chaTradeOk(L1PcInstance player, L1PcInstance trading_partner,
			boolean chaChk1, boolean chaChk2) {
		if (chaChk1 || chaChk2) {
			player.sendPackets(new S_Disconnect());
			trading_partner.sendPackets(new S_Disconnect());
		}
		if (chaChk1) {
			chaTradeAccount(trading_partner.getAccountName(), player.getName());
		}
		if (chaChk2) {

			chaTradeAccount(player.getAccountName(), trading_partner.getName());
		}
	}

	private boolean chaTradeItemChk(List<?> pc_tradelist, int listcount) {
		L1ItemInstance l1iteminstance = null;
		for (int cnt = 0; cnt < listcount; cnt++) {
			l1iteminstance = (L1ItemInstance) pc_tradelist.get(cnt);
			if (l1iteminstance.getItemId() == 2100903
					|| l1iteminstance.getItemId() == 2100904) {
				return true;
			}
		}
		return false;
	}
	/***** ���� �ð� �������� *****/
	private static String getLogTime(){
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm"); 
		String time = dateFormat.format(currentDate.getTime());
		return time;
	}
	public void TradeOK(L1PcInstance pc) {
		try	{
			int cnt;
			boolean chaChk1 = false; // ĳ���ͱ�ȯ�ֹ���
			boolean chaChk2 = false; // ĳ���ͱ�ȯ�ֹ���
			L1Object trading_partner = L1World.getInstance().findObject(
					pc.getTradeID());
			if (trading_partner != null) {
				if (trading_partner instanceof L1PcInstance) {
					L1PcInstance tradepc = (L1PcInstance) trading_partner;
					List<?> player_tradelist = pc.getTradeWindowInventory()
							.getItems();
					int player_tradecount = pc.getTradeWindowInventory()
							.getSize();

					List<?> trading_partner_tradelist = tradepc
							.getTradeWindowInventory().getItems();
					int trading_partner_tradecount = tradepc
							.getTradeWindowInventory().getSize();

					chaChk1 = chaTradeItemChk(player_tradelist,
							player_tradecount);
					chaChk2 = chaTradeItemChk(trading_partner_tradelist,
							trading_partner_tradecount);
					L1ItemInstance pcitem = null;
					for (cnt = 0; cnt < player_tradecount; cnt++) {
						pcitem = (L1ItemInstance) player_tradelist.get(0);

						if (chaChk2
								&& !(pcitem.getItemId() == 2100903 || pcitem
										.getItemId() == 2100904)) {
							PrivateWarehouse warehouse = WarehouseManager
									.getInstance().getPrivateWarehouse(
											tradepc.getAccountName());

							if (warehouse == null)
								return;

							if (warehouse.checkAddItemToWarehouse(pcitem,
									player_tradecount) == L1Inventory.SIZE_OVER) {
								tradepc.sendPackets(new S_ServerMessage(75)); // \f1��밡
																				// ������
																				// �ʹ�
																				// ������
																				// �־�
																				// �ŷ���
																				// ��
																				// �����ϴ�.
								break;
							}

							pc.getTradeWindowInventory().tradeItem(pcitem,pcitem.getCount(), warehouse);
							eva.LogTradeAppend("�ɸ��ͱ�ȯ", pc.getName(), tradepc.getName(), pcitem.getEnchantLevel(), pcitem.getName(), pcitem.getBless(), pcitem.getCount(), pcitem.getId());

						} else {
							pc.getTradeWindowInventory().tradeItem(pcitem,
									pcitem.getCount(), tradepc.getInventory());
							CodeLogger.getInstance().tradelog("��ȯ", pc.getName()+")))"+tradepc.getName() + ":  +"+ pcitem.getEnchantLevel() +" " +pcitem.getName() + " "+ pcitem.getCount()+ "��");	
						//	eva.writeMessage(17,getLogTime()+"��" + pc.getName()+">>>"+tradepc.getName() +":  +"+ pcitem.getEnchantLevel() +" " +pcitem.getName() + " "+ pcitem.getCount()+ "��");  
							eva.LogTradeAppend("��ȯ", pc.getName(), tradepc.getName(), pcitem.getEnchantLevel(), pcitem.getName(), 	pcitem.getBless(), pcitem.getCount(), pcitem.getId());
						}
					}
					L1ItemInstance tradepcitem = null;
					for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
						tradepcitem = (L1ItemInstance) trading_partner_tradelist
								.get(0);

						if (chaChk1
								&& !(tradepcitem.getItemId() == 2100903 || tradepcitem
										.getItemId() == 2100904)) {
							PrivateWarehouse warehouse = WarehouseManager
									.getInstance().getPrivateWarehouse(
											pc.getAccountName());
							if (warehouse == null)
								return;

							if (warehouse.checkAddItemToWarehouse(tradepcitem,
									trading_partner_tradecount) == L1Inventory.SIZE_OVER) {
								pc.sendPackets(new S_ServerMessage(75)); // \f1��밡
																			// ������
																			// �ʹ�
																			// ������
																			// �־�
																			// �ŷ���
																			// ��
																			// �����ϴ�.
								break;
							}


							tradepc.getTradeWindowInventory().tradeItem(tradepcitem, tradepcitem.getCount(), warehouse);
							eva.LogTradeAppend("�ɸ��ͱ�ȯ", tradepc.getName(), pc.getName(), tradepcitem.getEnchantLevel(), tradepcitem.getName(), tradepcitem.getBless(), tradepcitem.getCount(), tradepcitem.getId());

					} else {

							tradepc.getTradeWindowInventory().tradeItem(
									tradepcitem, tradepcitem.getCount(),
									pc.getInventory());
							 CodeLogger.getInstance().tradelog("��ȯ", tradepc.getName()+">>>"+pc.getName() + ":  +"+ tradepcitem.getEnchantLevel()+" " + tradepcitem.getName()+" " + tradepcitem.getCount()+ "��");	
							 eva.LogTradeAppend("��ȯ", tradepc.getName(), pc.getName(), tradepcitem.getEnchantLevel(), tradepcitem.getName(), tradepcitem.getBless(), tradepcitem.getCount(), tradepcitem.getId());
							 //   eva.writeMessage(17,getLogTime()+"��" + tradepc.getName()+"<<<"+pc.getName() +":  +"+ tradepcitem.getEnchantLevel()+" " + tradepcitem.getName()+" " + tradepcitem.getCount()+ "��"); 
}
						}
					/**���Ի��� */
					if (tradepc.isAutoshop()) {							
						for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
							if (player.isGm() && !player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_MENT)) {
								if (pcitem.getEnchantLevel() > 0) {
									player.sendPackets(new S_SystemMessage("\\fT" + pc.getName()+ "���� +" + pcitem.getEnchantLevel() + " " + pcitem.getName() + "�� ���Խ��׽��ϴ�."));
								} else {
									player.sendPackets(new S_SystemMessage("\\fT" + pc.getName()+ "���� " + pcitem.getName() + "�� " + pcitem.getCount() + "�� ���Խ��׽��ϴ�."));
								}
							}
						}
					}
						/**���Ի��� */
					pc.sendPackets(new S_TradeStatus(0));
					tradepc.sendPackets(new S_TradeStatus(0));
					pc.setTradeOk(false);
					tradepc.setTradeOk(false);
					pc.setTradeID(0);
					tradepc.setTradeID(0);
					pc.getLight().turnOnOffLight();
					tradepc.getLight().turnOnOffLight();
					if (chaChk1) {
						if (tradepc.getInventory().checkItem(2100903, 1)) {
							tradepc.getInventory().consumeItem(2100903, 1);
						}
						if (tradepc.getInventory().checkItem(2100904, 1)) {
							tradepc.getInventory().consumeItem(2100904, 1);
						}
					}
					if (chaChk2) {
						if (pc.getInventory().checkItem(2100903, 1)) {
							pc.getInventory().consumeItem(2100903, 1);
						}
						if (pc.getInventory().checkItem(2100904, 1)) {
							pc.getInventory().consumeItem(2100904, 1);
						}
					}
					chaTradeOk(pc, tradepc, chaChk1, chaChk2);

				} else if (trading_partner instanceof L1BuffNpcInstance) {
					L1BuffNpcInstance tradenpc = (L1BuffNpcInstance) trading_partner;
					List<?> player_tradelist = pc.getTradeWindowInventory()
							.getItems();
					int player_tradecount = pc.getTradeWindowInventory()
							.getSize();

					L1ItemInstance pcitem = null;
					for (cnt = 0; cnt < player_tradecount; cnt++) {
						pcitem = (L1ItemInstance) player_tradelist.get(0);
						L1Castle castle = CastleTable.getInstance()
								.getCastleTable(4);
						L1Castle castle2 = CastleTable.getInstance()
								.getCastleTable(6);
						int money = castle.getPublicReadyMoney()
								+ pcitem.getCount() / 3;
						int money2 = castle2.getPublicReadyMoney()
								+ pcitem.getCount() / 10;
						castle.setPublicReadyMoney(money);
						castle2.setPublicReadyMoney(money2);
						pc.getTradeWindowInventory().consumeItem(
								pcitem.getItemId(), pcitem.getCount());
					}
					pc.sendPackets(new S_TradeStatus(0));
					pc.setTradeOk(false);
					tradenpc.setTradeOk(false);
					pc.setTradeID(0);
					tradenpc.setTradeID(0);
					pc.getLight().turnOnOffLight();
					tradenpc.getLight().turnOnOffLight();
					new BuffSystem(tradenpc, pc);
				}
			}
		} catch (Exception e) { 
			System.out.println("����");
			}
}
			
	public void TradeCancel(L1PcInstance pc) {
		int cnt;

		L1Object trading_partner = L1World.getInstance().findObject(
				pc.getTradeID());
		if (trading_partner != null) {
			if (trading_partner instanceof L1PcInstance) {
				L1PcInstance tradepc = (L1PcInstance) trading_partner;
				List<?> player_tradelist = pc.getTradeWindowInventory()
						.getItems();
				int player_tradecount = pc.getTradeWindowInventory().getSize();

				List<?> trading_partner_tradelist = tradepc
						.getTradeWindowInventory().getItems();
				int trading_partner_tradecount = tradepc
						.getTradeWindowInventory().getSize();
				L1ItemInstance pcitem = null;
				for (cnt = 0; cnt < player_tradecount; cnt++) {
					pcitem = (L1ItemInstance) player_tradelist.get(0);
					pc.getTradeWindowInventory().tradeItem(pcitem,
							pcitem.getCount(), pc.getInventory());
				}
				L1ItemInstance tradepcitem = null;

				for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
					tradepcitem = (L1ItemInstance) trading_partner_tradelist
							.get(0);
					tradepc.getTradeWindowInventory().tradeItem(tradepcitem,
							tradepcitem.getCount(), tradepc.getInventory());
				}

				pc.sendPackets(new S_TradeStatus(1));
				tradepc.sendPackets(new S_TradeStatus(1));
				pc.setTradeOk(false);
				tradepc.setTradeOk(false);
				pc.setTradeID(0);
				tradepc.setTradeID(0);
			} else if (trading_partner instanceof L1BuffNpcInstance) {
				L1BuffNpcInstance tradenpc = (L1BuffNpcInstance) trading_partner;
				List<?> player_tradelist = pc.getTradeWindowInventory()
						.getItems();
				int player_tradecount = pc.getTradeWindowInventory().getSize();

				L1ItemInstance pcitem = null;
				for (cnt = 0; cnt < player_tradecount; cnt++) {
					pcitem = (L1ItemInstance) player_tradelist.get(0);
					pc.getTradeWindowInventory().tradeItem(pcitem,
							pcitem.getCount(), pc.getInventory());
				}
				pc.sendPackets(new S_TradeStatus(1));
				pc.setTradeOk(false);
				tradenpc.setTradeOk(false);
				pc.setTradeID(0);
				tradenpc.setTradeID(0);
			}
		}
	}
}