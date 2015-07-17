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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.Warehouse.ClanWarehouse;
import l1j.server.Warehouse.ElfWarehouse;
import l1j.server.Warehouse.PackageWarehouse;
import l1j.server.Warehouse.PrivateWarehouse;
import l1j.server.Warehouse.SpecialWarehouse;
import l1j.server.Warehouse.Warehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.BugKick;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ClanWarehouseList;
import l1j.server.server.datatables.NpcCashShopTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.shop.L1ShopBuyOrderList;
import l1j.server.server.model.shop.L1ShopSellOrderList;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.utils.SQLUtil;
import server.CodeLogger;
import server.LineageClient;
import server.manager.eva;
import server.system.autoshop.AutoShopManager;

public class C_ShopAndWarehouse extends ClientBasePacket {
	private final int TYPE_BUY_SHP = 0; // ���� or ���� ���� ���

	private final int TYPE_SEL_SHP = 1; // ���� or ���� ���� �ȱ�

	private final int TYPE_PUT_PWH = 2; // ���� â�� �ñ��

	private final int TYPE_GET_PWH = 3; // ���� â�� ã��

	private final int TYPE_PUT_CWH = 4; // ���� â�� �ñ��

	private final int TYPE_GET_CWH = 5; // ���� â�� ã��

	private final int TYPE_PUT_EWH = 8; // ���� â�� �ñ��

	private final int TYPE_GET_EWH = 9; // ���� â�� ã��

	private final int TYPE_GET_MWH = 10; // ��Ű�� â�� ã��
	
	private final int TYPE_PUT_SWH = 17; // Ư��â�� �ñ��
	
	private final int TYPE_GET_SWH = 19; // Ư��â�� ã��

	public C_ShopAndWarehouse(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);
		int npcObjectId = readD();
		int resultType = readC();
		int size = readC();
		@SuppressWarnings("unused")
		int unknown = readC();

		if (size < 0)
			return;
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		// �����������Ŵ
		pc.saveInventory();
		// �����������Ŵ
		int level = pc.getLevel();
	//	if (isTwoLogin(pc))
	//		return;
		if (size < 0) {
			pc.sendPackets(new S_Disconnect());

			return;
		}
		if (size > 150) {
			System.out.println("��Ŷ���� ["+pc.getName()+"] IP "+clientthread.getIp());
			 clientthread.kick();
			 clientthread.close();
			 return;
			}
		if (pc.getOnlineStatus() == 0 || isTwoLogin(pc)) {
			clientthread.kick();
			clientthread.close();
			return;
		}

		int npcId = 0;
		String npcImpl = "";
		boolean isPrivateShop = false;
		boolean tradable = true;
		L1Object findObject = L1World.getInstance().findObject(npcObjectId);
		if (findObject == null) {
			   clientthread.kick();
			   clientthread.close();
			   return;
			  }
		if (findObject != null) { // 3��
			int diffLocX = Math.abs(pc.getX() - findObject.getX());
			int diffLocY = Math.abs(pc.getY() - findObject.getY());
			if (diffLocX > 3 || diffLocY > 3) {
				return;
			}
			if (findObject instanceof L1NpcInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) findObject;
				npcId = targetNpc.getNpcTemplate().get_npcId();
				npcImpl = targetNpc.getNpcTemplate().getImpl();
			} else if (findObject instanceof L1PcInstance) {
				isPrivateShop = true;
			}
		}
		// //�ߺ� ���� ���׹���
		if (pc.getOnlineStatus() == 0) {
			clientthread.kick();
			return;
		}
		// //�ߺ� ���� ���׹���

		/** ����, â�� ���� ����Ʈ * */
/*		if (findObject != null) {
			NpcEffect(pc, findObject, npcId, resultType);
		}*/

		switch (resultType) {
		case TYPE_BUY_SHP: // ���� or ���� ���� ���
			if (npcId == 70035 || npcId == 70041 || npcId == 70042) {
				int status = L1BugBearRace.getInstance().getBugRaceStatus();
				boolean chk = L1BugBearRace.getInstance().buyTickets;
				if (status != L1BugBearRace.STATUS_READY || chk == false) {
					return;
				}
			}
			if (size != 0 && npcImpl.equalsIgnoreCase("L1NpcCashShop")){
				buyItemFromNpcCashShop(pc, npcId, size);
				break;
			}
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Merchant")) {
				buyItemFromShop(pc, npcId, size);
				break;
			}
			if (size != 0 && npcImpl.equalsIgnoreCase("L1NpcShop")) {
				buyItemFromNpcShop(pc, npcId, size);
				break;
			}
			
			if (size != 0 && isPrivateShop) {
				buyItemFromPrivateShop(pc, findObject, size);
				break;
			}
		case TYPE_SEL_SHP: // ���� or ���� ���� �ȱ�

	      	if(size != 0 && npcImpl.equalsIgnoreCase("L1NpcCashShop")){
	      		sellItemToNpcShop(pc, npcId, size);
				break;
			}
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Merchant"))
				sellItemToShop(pc, npcId, size);
			if (size != 0 && isPrivateShop)
				sellItemToPrivateShop(pc, findObject, size);
			break;
		case TYPE_PUT_PWH: // ���� â�� �ñ��
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) {// �ڽ���
																					// â��
																					// �ݳ�
				putItemToPrivateWarehouse(pc, size);
				break;
			}
		case TYPE_GET_PWH: // ���� â�� ã��
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) {// �ڽ���
																					// â��
																					// ã��
				getItemToPrivateWarehouse(pc, size);
				break;
			}
		case TYPE_PUT_CWH: // ���� â�� �ñ��
			if (npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) {

				putItemToClanWarehouse(pc, size);
				break;
			}

		case TYPE_GET_CWH: // ���� â�� ã��
			if (npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) {

				getItemToClanWarehouse(pc, size);
				break;
			}
		case TYPE_PUT_EWH: // ���� â�� �ñ��
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 99) {
				pc.sendPackets(new S_SystemMessage("���� â��� ��� �Ұ��� �մϴ�."));
				putItemToElfWarehouse(pc, size);
				break;
			}
		case TYPE_GET_EWH: // ���� â�� ã��
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 99) {
				pc.sendPackets(new S_SystemMessage("���� â��� ��� �Ұ��� �մϴ�."));
				getItemToElfWarehouse(pc, size);
				break;
			}
		case TYPE_GET_MWH: // ��Ű�� â�� ã��
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf"))
				getItemToPackageWarehouse(pc, size);
			break;
		case TYPE_PUT_SWH: // Ư�� â�� �ñ�� 
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")
					&& pc.get_SpecialSize() == 0) {
				pc.sendPackets(new S_ServerMessage(1238));
				break;
			}
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
				putItemToSpecialWarehouse(pc, size);
				break;
			}
		case TYPE_GET_SWH: // Ư�� â�� ã�� 
			if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
				getItemToSpecialWarehouse(pc, size);
				break;
			}
		default:
		}
	}

	private void doNothingClanWarehouse(L1PcInstance pc) {
		if (pc == null)
			return;

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan == null)
			return;

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance()
				.getClanWarehouse(clan.getClanName());
		if (clanWarehouse == null)
			return;

		clanWarehouse.unlock(pc.getId());
	}

	private void getItemToPackageWarehouse(L1PcInstance pc, int size) {
		int objectId, count;
		L1ItemInstance item = null;
		PackageWarehouse w = WarehouseManager.getInstance().getPackageWarehouse(pc.getAccountName());
		if (w == null)
			return;

		for (int i = 0; i < size; i++) {
			objectId = readD();
			count = readD();
			item = w.getItem(objectId);

			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (count > item.getCount())
				count = item.getCount();
			if (!isAvailablePcWeight(pc, item, count))
				break;
			/*���׹���*/
			int itemType = item.getItem().getType2();
			if(count <= 0){
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (item == null || item.getCount() < count) {
		    	 pc.sendPackets(new S_Disconnect());
		         return;
		    }
		    if ((itemType == 1 && item.getCount() != 1) ||	(itemType == 2 && item.getCount() != 1)){
				pc.sendPackets(new S_Disconnect());
				return;
			}
		    if (count <= 0 || count < 1 || item.getCount() <= 0) {
		    	pc.sendPackets(new S_Disconnect());
		        return;
		    }			
		    if (!pc.isGm() && item.getItem().getItemId() == 40308)  {
	        	if (count > 10000000) {
	        		 pc.sendPackets(new S_SystemMessage("�Ƶ����� 1000�� ������ â���̿��� �����մϴ�."));
	        		 return;
	        	}
		    }
		    if(item.getItem().getItemId() == 40445 || item.getItem().getItemId() == 41251 ||
		    		item.getItem().getItemId() == 41254){
		    	if (count > 1) {
	        		 pc.sendPackets(new S_SystemMessage("�ش� �������� 1���� â���̿��� �����մϴ�."));
	        		 return;
	        	}
		    }
			if(item.getItem().getItemId() >= 76767 && item.getItem().getItemId() <= 76784 ){
				pc.sendPackets(new S_SystemMessage("������Ǯ�� ��,������ â���̿��� �Ұ��� �մϴ�."));
			return;
			}
	        if (item.getItem().getItemId() == 41159 || item.getItem().getItemId() == 41246){ 
				  pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				  return;
			}
			if (item.getAcByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHolyDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHitByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(6524)) { 
				pc.sendPackets(new S_SystemMessage("\\fY������ 30�ʰ��� â�� �̿��� �ϽǼ� �����ϴ�."));
				return;
		    }
	        if (item.getCount() > 2000000000) {
			    return;
			}
		    if (count > 2000000000) {
			    return;
			}
			 //** �߰�� ������� ���� ���� **//
			  long nowtime = System.currentTimeMillis();
			  if(item.getItemdelay3() >=  nowtime ){
				  break;
			  }  
			  //** �߰�� ������� ���� ���� **//
				 //�ż��ѿ��� ���������׹���
			   if (!item.getItem().isToBeSavedAtOnce()) {
			   pc.getInventory().saveItem(item, L1PcInventory.COL_COUNT);
			   }	 
		      //�ż��ѿ��� ���������׹���
			w.tradeItem(item, count, pc.getInventory());
			eva.LogWareHouseAppend("��Ű��:", pc.getName(), "", item, count, objectId);
			CodeLogger.getInstance().warehouselog("ã	��Ű��", pc.getName()+"["+pc.getAccountName()+"]",item,count);

		}
	}

	private void getItemToElfWarehouse(L1PcInstance pc, int size) {
		if (pc.getLevel() < 5 || !pc.isElf())
			return;

		L1ItemInstance item;
		ElfWarehouse elfwarehouse = WarehouseManager.getInstance()
				.getElfWarehouse(pc.getAccountName());
		if (elfwarehouse == null)
			return;

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();
			item = elfwarehouse.getItem(objectId);

			/* ���׹��� */
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			/* ���׹��� */

			if (item == null || item.getCount() < count || count <= 0
					|| item.getCount() <= 0) {
				BugKick.getInstance().KickPlayer(pc);
				break;
			}
			if (item.getCount() > 10000000) {
				break;
			}
			if (count > 10000000) {
				break;
			}
			/* ���׹��� */
			if (item.getEnchantLevel() >= 1) { // 1�̻� ��þ�� ��������
																// ��ӺҰ�
				pc.sendPackets(new S_SystemMessage("��þ�� �������� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (item.getItem().getItemId() == 40308) {
				if (count > 10000000) {
					pc.sendPackets(new S_SystemMessage("�Ƶ��� (10,000,000) ���ϸ� â�� �̿��� �����մϴ�."));
					return;
				}
			}
			if (item.getItem().getItemId() == 41159 
					|| item.getItem().getItemId() == 520000
					|| item.getItem().getItemId() == 520001
					|| item.getItem().getItemId() == 520002
					|| item.getItem().getItemId() == 41246) { // ����(438005) â�� �ñ��� ���ϰ� by ����ž�� 2012.02.06 
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			
			/** ���� ��øȿ�� ���� ���� �ҽ� by ����ž�� 2012.02.23 */
			if (item.getAcByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHolyDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHitByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(6524)) { 
				pc.sendPackets(new S_SystemMessage("\\fY������ 30�ʰ��� â�� �̿��� �ϽǼ� �����ϴ�."));
				return;
		    }
			if (item.getCount() > 2000000000) {
				return;
			}
			if (count > 2000000000) {
				return;
			}
			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (count > item.getCount())
				count = item.getCount();
			if (!isAvailablePcWeight(pc, item, count))
				break;

			if (pc.getInventory().consumeItem(40494, 2)) {
				elfwarehouse.tradeItem(item, count, pc.getInventory());
				eva.LogWareHouseAppend("����:ã", pc.getName(), "", item, count, objectId);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$767"));
				break;
			}
		}
	}

	private void putItemToElfWarehouse(L1PcInstance pc, int size) {
		if (pc.getLevel() < 5 || !pc.isElf())
			return;

		L1Object object = null;
		L1ItemInstance item = null;
		ElfWarehouse elfwarehouse = WarehouseManager.getInstance()
				.getElfWarehouse(pc.getAccountName());
		if (elfwarehouse == null)
			return;

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();

			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;
			if (item.getItem().getItemId() == 76778
					|| item.getItem().getItemId() == 555563
					|| item.getItem().getItemId() == 76779
					|| item.getItem().getItemId() == 76780
					|| item.getItem().getItemId() == 76781
					|| item.getItem().getItemId() == 76782
					|| item.getItem().getItemId() == 76783
					|| item.getItem().getItemId() == 76784
					|| item.getItem().getItemId() == 76773
					|| item.getItem().getItemId() == 76772
					|| item.getItem().getItemId() == 76771
					|| item.getItem().getItemId() == 76770
					|| item.getItem().getItemId() == 76769
					|| item.getItem().getItemId() == 76768
					|| item.getItem().getItemId() == 76767
					|| item.getItem().getItemId() == 76777
					|| item.getItem().getItemId() == 76776
					|| item.getItem().getItemId() == 76775
					|| item.getItem().getItemId() == 76774
							|| item.getItem().getItemId() == 767740
					||item.getItem().getItemId() == 41159
					|| item.getItem().getItemId() == 41246
					|| item.getItem().getItemId() == 40308
					|| item.getItem().getItemId() == 720
					|| item.getItem().getItemId() == 721
					|| item.getItem().getItemId() == 722
					
					) {
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				break;
			}
			if (item.getItem().getItemId() == 40074
					|| item.getItem().getItemId() == 40087
					|| item.getItem().getItemId() == 140074
					|| item.getItem().getItemId() == 140087
					|| item.getItem().getItemId() == 240074
					|| item.getItem().getItemId() == 240087) {
				pc.sendPackets(new S_SystemMessage(
						"������ ��ȭ�ֹ����� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			
			if (item.getItem().getItemId() == 40308) {
				if (count > 10000000) {
					pc.sendPackets(new S_SystemMessage("�Ƶ��� (10,000,000) ���ϸ� â�� �̿��� �����մϴ�."));
					return;
				}
			}
			if (item.getAcByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHolyDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHitByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			
			if(item.getItem().getItemId() >= 76767 && item.getItem().getItemId() <= 76784 ){
				pc.sendPackets(new S_SystemMessage("������Ǯ�� ��,������ â���̿��� �Ұ��� �մϴ�."));
			return;
			}
			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (count > item.getCount())
				count = item.getCount();
			/* ���׹��� */
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			/* ���׹��� */

			if (item == null || item.getCount() < count || count <= 0
					|| item.getCount() <= 0) {
				break;
			}
			if (item.getCount() > 10000000) {
				break;
			}
			if (count > 10000000) {
				break;
			}

			if (!item.getItem().isTradable()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem()
						.getName())); // \f1%0�� �����ų� �Ǵ� Ÿ�ο��� ������ �� �� �����ϴ�.
				break;
			}
			/* ���׹��� */
			if (item.getEnchantLevel() >= 1) { // 1�̻� ��þ�� ��������
																// ��ӺҰ�
				pc.sendPackets(new S_SystemMessage("��þ�� �������� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (!checkPetList(pc, item))
				break;
			if (!isAvailableWhCount(elfwarehouse, pc, item, count))
				break;

			pc.getInventory().tradeItem(objectId, count, elfwarehouse);
			pc.getLight().turnOnOffLight();
			eva.LogWareHouseAppend("����:��", pc.getName(), "", item, count, objectId);
		}
	}

	private void getItemToClanWarehouse(L1PcInstance pc, int size) {
		if (pc.getLevel() < 5)
			return;

		if (size == 0) {
			doNothingClanWarehouse(pc);
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());

		if (!isAvailableClan(pc, clan))
			return;

		L1ItemInstance item;
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance()
				.getClanWarehouse(clan.getClanName());
		if (clanWarehouse == null)
			return;
		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();
			item = clanWarehouse.getItem(objectId);

			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (!hasAdena(pc))
				break;
			if (count >= item.getCount())
				count = item.getCount();
			if (!isAvailablePcWeight(pc, item, count))
				break;
			/* ���׹��� */
			  // int itemType = item.getItem().getType2();
			   if (count <= 0) {
			    pc.sendPackets(new S_Disconnect());
			    return;
			   }
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			/* ���׹��� */
			if (item == null || item.getCount() < count) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count <= 0 || count < 1 || item.getCount() <= 0) {
				BugKick.getInstance().KickPlayer(pc);
				break;
			}
			if(item.getBless() >= 128){ 
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (item.getItem().getItemId() == 40308) {
				if (count > 10000000) {
					pc.sendPackets(new S_SystemMessage("�Ƶ��� (10,000,000) ���ϸ� â�� �̿��� �����մϴ�."));
					return;
				}
			}
			if (item.getItem().getItemId() == 41159 
					|| item.getItem().getItemId() == 520000
					|| item.getItem().getItemId() == 520001
					|| item.getItem().getItemId() == 520002
					|| item.getItem().getItemId() == 41246) { // ����(438005) â�� �ñ��� ���ϰ� by ����ž�� 2012.02.06 
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (item.getAcByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHolyDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHitByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
						if (pc.getSkillEffectTimerSet().hasSkillEffect(6524)) { 
				pc.sendPackets(new S_SystemMessage("������ 30�ʰ� â�� �̿��� �ϽǼ� �����ϴ�."));
				return;
		    }
			/* ���׹��� */
			if (item.getEnchantLevel() >= 1) { // 1�̻� ��þ�� ��������
																// ��ӺҰ�
				pc.sendPackets(new S_SystemMessage("��þ�� �������� �̿��� �� �� �����ϴ�."));
				return;
			}
			 //** �߰�� ������� ���� ���� **//
			  long nowtime = System.currentTimeMillis();
			  if(item.getItemdelay3() >=  nowtime ){
				  break;
			  }  
			  //** �߰�� ������� ���� ���� **//
				 //�ż��ѿ��� ���������׹���
			   if (!item.getItem().isToBeSavedAtOnce()) {
			   pc.getInventory().saveItem(item, L1PcInventory.COL_COUNT);
			   }	 
		      //�ż��ѿ��� ���������׹���
				if (item.getCount() > 2000000000) {
					return;
				}
				if (count > 2000000000) {
					return;
				}
			clanWarehouse.tradeItem(item, count, pc.getInventory());
			ClanWarehouseList.getInstance().addList(pc.getClanid(), pc.getName()+" ��(��) �������� ã�ҽ��ϴ�.. \n"
					+"["+(item.getItem().getType2() == 1 || item.getItem().getType2() == 2 ? "+"+item.getEnchantLevel()+" "+item.getName() : item.getName())+"] x "+count+"��\n", currentTime());
			CodeLogger.getInstance().warehouselog("ã	����", pc.getName()+"["+pc.getClanname()+"]",item,count);
			eva.LogWareHouseAppend("����:ã", pc.getName(), pc.getClanname(), item, count, objectId);
			UpdateLog(pc.getName(), pc.getClanname(), item.getName(), count, 1);
	

		}
		clanWarehouse.unlock(pc.getId());
	}

	private void putItemToClanWarehouse(L1PcInstance pc, int size) {
		if (size == 0) {
			doNothingClanWarehouse(pc);
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());

		if (!isAvailableClan(pc, clan))
			return;

		L1Object object = null;
		L1ItemInstance item = null;
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance()
				.getClanWarehouse(clan.getClanName());
		if (clanWarehouse == null)
			return;

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();

			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;

			if (item == null)
				break;
			if (count > item.getCount())
				count = item.getCount();
			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (item.getItem().getItemId() == 40074
					|| item.getItem().getItemId() == 40087
					|| item.getItem().getItemId() == 140074
					|| item.getItem().getItemId() == 140087
					|| item.getItem().getItemId() == 240074
					|| item.getItem().getItemId() == 240087) {
				pc.sendPackets(new S_SystemMessage(
						"������ ��ȭ�ֹ����� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			if(item.getItem().getItemId() >= 76767 && item.getItem().getItemId() <= 76784 ){
				pc.sendPackets(new S_SystemMessage("������Ǯ�� ��,������ â���̿��� �Ұ��� �մϴ�."));
			return;
			}
			if (item.getItem().getItemId() == 76778
					|| item.getItem().getItemId() == 76779
					|| item.getItem().getItemId() == 555563
					|| item.getItem().getItemId() == 76780
					|| item.getItem().getItemId() == 76781
					|| item.getItem().getItemId() == 76782
					|| item.getItem().getItemId() == 76783
					|| item.getItem().getItemId() == 76784
					|| item.getItem().getItemId() == 76773
					|| item.getItem().getItemId() == 76772
					|| item.getItem().getItemId() == 76771
					|| item.getItem().getItemId() == 76770
					|| item.getItem().getItemId() == 76769
					|| item.getItem().getItemId() == 76768
					|| item.getItem().getItemId() == 76767
					|| item.getItem().getItemId() == 76777
					|| item.getItem().getItemId() == 76776
					|| item.getItem().getItemId() == 76775
					|| item.getItem().getItemId() == 76774
							|| item.getItem().getItemId() == 767740
					||item.getItem().getItemId() == 41159
					|| item.getItem().getItemId() == 41246
					|| item.getItem().getItemId() == 40308
					|| item.getItem().getItemId() ==500144
					|| item.getItem().getItemId() ==500145
					|| item.getItem().getItemId() ==500146
					|| item.getItem().getItemId() ==490018
					|| item.getItem().getItemId() == 720
					|| item.getItem().getItemId() == 721
					|| item.getItem().getItemId() == 722
					
					) {
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (item.getItem().getItemId() == 40308) {
				if (count > 10000000) {
					pc.sendPackets(new S_SystemMessage("�Ƶ��� (10,000,000) ���ϸ� â�� �̿��� �����մϴ�."));
					return;
				}
			}
			if (item.getAcByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHolyDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHitByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getCount() > 2000000000) {
				return;
			}
			if (count > 2000000000) {
				return;
			}
			if (item.getItem().getItemId() == 423012
					|| item.getItem().getItemId() == 423013) { // 10�ֳ�Ƽ
				pc.sendPackets(new S_ServerMessage(210, item.getItem()
						.getName())); // \f1%0�� �����ų� �Ǵ� Ÿ�ο��� ������ �� �� �����ϴ�.
				return;
			}
			if(item.getBless() >= 128){ 
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (item.getBless() >= 128 || !item.getItem().isTradable()) {
				// \f1%0�� �����ų� �Ǵ� Ÿ�ο��� �絵 �� �� �����ϴ�.
				pc.sendPackets(new S_ServerMessage(210, item.getItem()
						.getName()));
				break;
			}

			/* ���׹��� */
			if (item.getEnchantLevel() >= 1) { // 1�̻� ��þ�� ��������
																// ��ӺҰ�
				pc.sendPackets(new S_SystemMessage("��þ�� �������� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (!checkPetList(pc, item))
				break;
			if (!isAvailableWhCount(clanWarehouse, pc, item, count))
				break;
			 //** �߰�� ������� ���� ���� **//
			  long nowtime = System.currentTimeMillis();
			  if(item.getItemdelay3() >=  nowtime ){
				  break;
			  }  
			  //** �߰�� ������� ���� ���� **//
				 //�ż��ѿ��� ���������׹���
			   if (!item.getItem().isToBeSavedAtOnce()) {
			   pc.getInventory().saveItem(item, L1PcInventory.COL_COUNT);
			   }	 
		      //�ż��ѿ��� ���������׹���
			pc.getInventory().tradeItem(objectId, count, clanWarehouse);
			pc.getLight().turnOnOffLight();
			ClanWarehouseList.getInstance().addList(pc.getClanid(), pc.getName()+" ��(��) �������� �ð���ϴ�. \n"
					+"["+(item.getItem().getType2() == 1 || item.getItem().getType2() == 2 ? "+"+item.getEnchantLevel()+" "+item.getName() : item.getName())+"] x "+count+"��\n", currentTime());
			CodeLogger.getInstance().warehouselog("��	����", pc.getName()+"["+pc.getClanname()+"]",item,count);
			eva.LogWareHouseAppend("����:��", pc.getName(), pc.getClanname(), item, count, objectId);
			UpdateLog(pc.getName(), pc.getClanname(), item.getName(), count, 0);

		}
		clanWarehouse.unlock(pc.getId());
	}

	private void getItemToPrivateWarehouse(L1PcInstance pc, int size) {
		L1ItemInstance item = null;
		PrivateWarehouse warehouse = WarehouseManager.getInstance()
				.getPrivateWarehouse(pc.getAccountName());
		if (warehouse == null)
			return;

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();
			item = warehouse.getItem(objectId);

			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			/* ���׹��� */
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			/* ���׹��� */
			if (item == null || item.getCount() < count) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count <= 0 || count < 1 || item.getCount() <= 0) {
				BugKick.getInstance().KickPlayer(pc);
				break;
			}
		    if (!pc.isGm() && item.getItem().getItemId() == 40308)  {
	        	if (count > 10000000) {
	        		 pc.sendPackets(new S_SystemMessage("�Ƶ����� 1000�� ������ â���̿��� �����մϴ�."));
	        		 return;
	        	}
		    }
			/* ���׹��� */
			if (item.getItem().getItemId() == 41159
					|| item.getItem().getItemId() == 76778
					|| item.getItem().getItemId() == 76779
					|| item.getItem().getItemId() == 76780
					|| item.getItem().getItemId() == 76781
					|| item.getItem().getItemId() == 76782
					|| item.getItem().getItemId() == 76783
					|| item.getItem().getItemId() == 76784
					|| item.getItem().getItemId() == 76773
					|| item.getItem().getItemId() == 76772
					|| item.getItem().getItemId() == 76771
					|| item.getItem().getItemId() == 76770
					|| item.getItem().getItemId() == 76769
					|| item.getItem().getItemId() == 76768
					|| item.getItem().getItemId() == 76767
					|| item.getItem().getItemId() == 76777
					|| item.getItem().getItemId() == 555563
					|| item.getItem().getItemId() == 76776
					|| item.getItem().getItemId() == 76775
					|| item.getItem().getItemId() == 76774
							|| item.getItem().getItemId() == 767740
					|| item.getItem().getItemId() ==500144
					|| item.getItem().getItemId() ==500145
					|| item.getItem().getItemId() ==500146
					|| item.getItem().getItemId() ==490018
					||item.getItem().getItemId() == 41246) {
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
						if (pc.getSkillEffectTimerSet().hasSkillEffect(6524)) { 
				pc.sendPackets(new S_SystemMessage("������ 30�ʰ� â�� �̿��� �ϽǼ� �����ϴ�."));
				return;
		    }
						if (item.getAcByMagic() > 0) {
							pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
							return;
						}
						if (item.getDmgByMagic() > 0) {
							pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
							return;
						}
						if (item.getHolyDmgByMagic() > 0) {
							pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
							return;
						}
						if (item.getHitByMagic() > 0) {
							pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
							return;
						}
						if (item.getCount() > 2000000000) {
							return;
						}
						if (count > 2000000000) {
							return;
						}
			if (count > item.getCount())
				count = item.getCount();
			if (!isAvailablePcWeight(pc, item, count))
				break;
			if (!hasAdena(pc))
				break;
			 //** �߰�� ������� ���� ���� **//
			  long nowtime = System.currentTimeMillis();
			  if(item.getItemdelay3() >=  nowtime ){
				  break;
			  }  
			  //** �߰�� ������� ���� ���� **//
				 //�ż��ѿ��� ���������׹���
			   if (!item.getItem().isToBeSavedAtOnce()) {
			   pc.getInventory().saveItem(item, L1PcInventory.COL_COUNT);
			   }	 
		      //�ż��ѿ��� ���������׹���
			warehouse.tradeItem(item, count, pc.getInventory());
			CodeLogger.getInstance().warehouselog("ã	�Ϲ�", pc.getName()+"["+pc.getAccountName()+"]",item,count);
			eva.LogWareHouseAppend("�Ϲ�:ã", pc.getName(), "", item, count, objectId);

		}
	}
	// Ư��â�� ã�� 
	private void getItemToSpecialWarehouse(L1PcInstance pc, int size) {
		L1ItemInstance item = null;
		SpecialWarehouse warehouse = WarehouseManager.getInstance()
				.getSpecialWarehouse(pc.getName());
		if (warehouse == null)
			return;
		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();
			item = warehouse.getItem(objectId);
			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (count > item.getCount())
				count = item.getCount();
			if (!isAvailablePcWeight(pc, item, count))
				break;
			if (!hasAdena(pc))
				break;
			if (pc.getLevel() < 5) {
				pc.sendPackets(new S_SystemMessage("â��� 5���� �̻� ��� ���� �մϴ�."));
				return;
			}
		    if (!pc.isGm() && item.getItem().getItemId() == 40308)  {
	        	if (count > 20000000) {
	        		 pc.sendPackets(new S_SystemMessage("�Ƶ����� 2000�� ������ â���̿��� �����մϴ�."));
	        		 return;
	        	}
		    }
			/* ���׹��� */
			if (item.getItem().getItemId() == 41159
					|| item.getItem().getItemId() == 76778
					|| item.getItem().getItemId() == 76779
					|| item.getItem().getItemId() == 76780
					|| item.getItem().getItemId() == 76781
					|| item.getItem().getItemId() == 76782
					|| item.getItem().getItemId() == 76783
					|| item.getItem().getItemId() == 76784
					|| item.getItem().getItemId() == 76773
					|| item.getItem().getItemId() == 76772
					|| item.getItem().getItemId() == 76771
					|| item.getItem().getItemId() == 76770
					|| item.getItem().getItemId() == 76769
					|| item.getItem().getItemId() == 76768
					|| item.getItem().getItemId() == 76767
					|| item.getItem().getItemId() == 76777
					|| item.getItem().getItemId() == 555563
					|| item.getItem().getItemId() == 76776
					|| item.getItem().getItemId() == 76775
					|| item.getItem().getItemId() == 76774
							|| item.getItem().getItemId() == 767740
					|| item.getItem().getItemId() ==500144
					|| item.getItem().getItemId() ==500145
					|| item.getItem().getItemId() ==500146
					|| item.getItem().getItemId() ==490018
					||item.getItem().getItemId() == 41246) {
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
						if (pc.getSkillEffectTimerSet().hasSkillEffect(6524)) { 
				pc.sendPackets(new S_SystemMessage("������ 30�ʰ� â�� �̿��� �ϽǼ� �����ϴ�."));
				return;
		    }
			warehouse.tradeItem(item, count, pc.getInventory());
			CodeLogger.getInstance()
					.warehouselog("Ư�� ã��	�Ϲ�",
							pc.getName() + "[" + pc.getAccountName() + "]",
							item, count);
		}
	}

	// Ư��â�� �ñ�� 
	private void putItemToSpecialWarehouse(L1PcInstance pc, int size) {
		L1Object object = null;
		L1ItemInstance item = null;
		SpecialWarehouse warehouse = WarehouseManager.getInstance()
				.getSpecialWarehouse(pc.getName());
		if (warehouse == null)
			return;
		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();
			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;
			if (pc.getLevel() < 5) {
				pc.sendPackets(new S_SystemMessage("â��� 5���� �̻� ��� ���� �մϴ�."));
				return;
			}
			/* ���׹��� */
			// ** ������� **// By �����
			if (pc.getInventory().findItemId(40308).getCount() < 31) {
				S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
				pc.sendPackets(s_chatpacket);
				return;
			}
			// ����ʻ� �� ������ ���� ���� �� ������ ���� ĳ���� �������̶��
			if (isTwoLogin(pc))
				return;
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			/* ���׹��� */
			if (item == null || item.getCount() < count || count <= 0
					|| item.getCount() <= 0) {
				break;
			}
		    if (!pc.isGm() && item.getItem().getItemId() == 40308)  {
	        	if (count > 20000000) {
	        		 pc.sendPackets(new S_SystemMessage("�Ƶ����� 2000�� ������ â���̿��� �����մϴ�."));
	        		 return;
	        	}
		    }
			if(item.getItem().getItemId() >= 76767 && item.getItem().getItemId() <= 76784 ){
				pc.sendPackets(new S_SystemMessage("������Ǯ�� ��,������ â���̿��� �Ұ��� �մϴ�."));
			return;
			}
			if (item.getItem().getItemId() == 41159
					|| item.getItem().getItemId() == 76778
					|| item.getItem().getItemId() == 76779
					|| item.getItem().getItemId() == 76780
					|| item.getItem().getItemId() == 76781
					|| item.getItem().getItemId() == 76782
					|| item.getItem().getItemId() == 76783
					|| item.getItem().getItemId() == 76784
					|| item.getItem().getItemId() == 555563
					|| item.getItem().getItemId() == 76773
					|| item.getItem().getItemId() == 76772
					|| item.getItem().getItemId() == 76771
					|| item.getItem().getItemId() == 76770
					|| item.getItem().getItemId() == 76769
					|| item.getItem().getItemId() == 76768
					|| item.getItem().getItemId() == 76767
					|| item.getItem().getItemId() == 76777
					|| item.getItem().getItemId() == 76776
					|| item.getItem().getItemId() == 76775
					|| item.getItem().getItemId() == 76774
							|| item.getItem().getItemId() == 767740
					|| item.getItem().getItemId() ==500144
					|| item.getItem().getItemId() ==500145
					|| item.getItem().getItemId() ==500146
					|| item.getItem().getItemId() ==490018
					||item.getItem().getItemId() == 41246
					|| item.getItem().getItemId() == 720
					|| item.getItem().getItemId() == 721
					|| item.getItem().getItemId() == 722
					) {
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (item.getCount() > 10000000) {
				break;
			}
			if (count > 10000000) {
				break;
			}
			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (!checkPetList(pc, item))
				break;
			if (!isAvailableWhCount(warehouse, pc, item, count))
				break;
			if (count > item.getCount())
				count = item.getCount();

			pc.getInventory().tradeItem(objectId, count, warehouse);
			pc.getLight().turnOnOffLight();

			CodeLogger.getInstance()
					.warehouselog("Ư�� �ñ�	�Ϲ�",
							pc.getName() + "[" + pc.getAccountName() + "]",
							item, count);
		}
	}
	// Ư��â�� �ñ��
	

	private static String currentTime() {
		//TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		//Calendar cal = Calendar.getInstance(tz);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
		int year = cal.get(Calendar.YEAR) - 2000;
		String year2;
		if (year < 10) {
			year2 = "0" + year;
		} else {
			year2 = Integer.toString(year);
		}
		int Month = cal.get(Calendar.MONTH) + 1;
		String Month2 = null;
		if (Month < 10) {
			Month2 = "0" + Month;
		} else {
			Month2 = Integer.toString(Month);
		}
		int date = cal.get(Calendar.DATE);
		String date2 = null;
		if (date < 10) {
			date2 = "0" + date;
		} else {
			date2 = Integer.toString(date);
		}
		int hour = cal.get(Calendar.HOUR);
		String hour2 = null;
		if(hour < 10)
			hour2 = "0" + hour;
		else
			hour2 = Integer.toString(hour);
		int min = cal.get(Calendar.MINUTE);
		String min2 = null;
		if(min < 10)
			min2 = "0" + min;
		else
			min2 = Integer.toString(min);
		return year2 + "/" + Month2 + "/" + date2 + " " + hour2+":"+min2;
	}

	private void putItemToPrivateWarehouse(L1PcInstance pc, int size) {
		L1Object object = null;
		L1ItemInstance item = null;
		PrivateWarehouse warehouse = WarehouseManager.getInstance()
				.getPrivateWarehouse(pc.getAccountName());
		if (warehouse == null)
			return;

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();

			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;

			if (!item.getItem().isTradable()) {
				// \f1%0�� �����ų� �Ǵ� Ÿ�ο��� �絵 �� �� �����ϴ�.
				pc.sendPackets(new S_ServerMessage(210, item.getItem()
						.getName()));
				break;
			}
		    if (!pc.isGm() && item.getItem().getItemId() == 40308)  {
	        	if (count > 10000000) {
	        		 pc.sendPackets(new S_SystemMessage("�Ƶ����� 1000�� ������ â���̿��� �����մϴ�."));
	        		 return;
	        	}
		    }
			if(item.getItem().getItemId() >= 76767 && item.getItem().getItemId() <= 76784 ){
				pc.sendPackets(new S_SystemMessage("������Ǯ�� ��,������ â���̿��� �Ұ��� �մϴ�."));
			return;
			}
			if (item.getItem().getItemId() == 41159
					|| item.getItem().getItemId() == 76778
					|| item.getItem().getItemId() == 76779
					|| item.getItem().getItemId() == 76780
					|| item.getItem().getItemId() == 76781
					|| item.getItem().getItemId() == 76782
					|| item.getItem().getItemId() == 76783
					|| item.getItem().getItemId() == 76784
					|| item.getItem().getItemId() == 555563
					|| item.getItem().getItemId() == 76773
					|| item.getItem().getItemId() == 76772
					|| item.getItem().getItemId() == 76771
					|| item.getItem().getItemId() == 76770
					|| item.getItem().getItemId() == 76769
					|| item.getItem().getItemId() == 76768
					|| item.getItem().getItemId() == 76767
					|| item.getItem().getItemId() == 76777
					|| item.getItem().getItemId() == 76776
					|| item.getItem().getItemId() == 76775
					|| item.getItem().getItemId() == 76774
							|| item.getItem().getItemId() == 767740
					|| item.getItem().getItemId() ==500144
					|| item.getItem().getItemId() ==500145
					|| item.getItem().getItemId() ==500146
					|| item.getItem().getItemId() ==490018
					||item.getItem().getItemId() == 41246
					|| item.getItem().getItemId() == 720
					|| item.getItem().getItemId() == 721
					|| item.getItem().getItemId() == 722
					) {
				pc.sendPackets(new S_SystemMessage("�ش� �������� â�� �̿��� �� �� �����ϴ�."));
				return;
			}
			if (item.getItem().getItemId() == 40308) {
				if (count > 10000000) {
					pc.sendPackets(new S_SystemMessage("�Ƶ��� (10,000,000) ���ϸ� â�� �̿��� �����մϴ�."));
					return;
				}
			}
			if (item.getAcByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHolyDmgByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getHitByMagic() > 0) {
				pc.sendPackets(new S_SystemMessage("���� ��� ����ȿ���� �����ֽ��ϴ�."));
				return;
			}
			if (item.getCount() > 2000000000) {
				return;
			}
			if (count > 2000000000) {
				return;
			}
			if (!isAvailableTrade(pc, objectId, item, count))
				break;
			if (!checkPetList(pc, item))
				break;
			if (!isAvailableWhCount(warehouse, pc, item, count))
				break;
			 //** �߰�� ������� ���� ���� **//
			  long nowtime = System.currentTimeMillis();
			  if(item.getItemdelay3() >=  nowtime ){
				  break;
			  }  
			  //** �߰�� ������� ���� ���� **//
				 //�ż��ѿ��� ���������׹���
			   if (!item.getItem().isToBeSavedAtOnce()) {
			   pc.getInventory().saveItem(item, L1PcInventory.COL_COUNT);
			   }	 
		      //�ż��ѿ��� ���������׹���
			if (count > item.getCount())
				count = item.getCount();

			pc.getInventory().tradeItem(objectId, count, warehouse);
			pc.getLight().turnOnOffLight();
			eva.LogWareHouseAppend("�Ϲ�:��", pc.getName(), "", item, count, objectId);
			CodeLogger.getInstance().warehouselog("��	�Ϲ�", pc.getName()+"["+pc.getAccountName()+"]",item,count);

		}
	}

	private void sellItemToPrivateShop(L1PcInstance pc, L1Object findObject,
			int size) {
		L1PcInstance targetPc = null;

		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		if (targetPc.isTradingInPrivateShop())
			return;

		targetPc.setTradingInPrivateShop(true);

		L1PrivateShopBuyList psbl;
		L1ItemInstance item = null;
		boolean[] isRemoveFromList = new boolean[8];
		FastTable<L1PrivateShopBuyList> buyList = targetPc.getBuyList();

		synchronized (buyList) {
			int order, itemObjectId, count, buyPrice, buyTotalCount, buyCount;
			for (int i = 0; i < size; i++) {
				itemObjectId = readD();
				count = readCH();
				order = readC();

				item = pc.getInventory().getItem(itemObjectId);

				if (!isAvailableTrade(pc, itemObjectId, item, count))
					break;
				if (item.getBless() >= 128) {
					// \f1%0�� �����ų� �Ǵ� Ÿ�ο��� �絵 �� �� �����ϴ�.
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName()));
					break;
				}

				psbl = (L1PrivateShopBuyList) buyList.get(order);
				buyPrice = psbl.getBuyPrice();
				buyTotalCount = psbl.getBuyTotalCount(); // �� ������ ����
				buyCount = psbl.getBuyCount(); // �� ����

				if (count > buyTotalCount - buyCount)
					count = buyTotalCount - buyCount;

				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905)); // ��� �ϰ� �ִ� ��������
																// �Ǹ��� �� �����ϴ�.
					break;
				}

				if (!isAvailablePcWeight(pc, item, count))
					break;
				if (isOverMaxAdena(targetPc, buyPrice, count))
					return;

				// ���λ��� �κ� ��Ž� ��� //
				int itemType = item.getItem().getType2();
				/* ���׹��� */
				if (itemObjectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					targetPc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					targetPc.sendPackets(new S_Disconnect());
					return;
				}
				if (count >= item.getCount()) {
					count = item.getCount();
				}
				/* ���׹��� */
				if ((itemType == 1 && count != 1)
						|| (itemType == 2 && count != 1)) {
					return;
				}
				if (item.getCount() <= 0 || count <= 0
						|| item.getCount() < count) {
					pc.sendPackets(new S_Disconnect());
					targetPc.sendPackets(new S_Disconnect());
					return;
				}
				if (buyPrice * count <= 0 || buyPrice * count > 2000000000) {
					return;
				}
		        
				if (item.getCount() > 2000000000) {
					return;
				}
				if (count > 2000000000) {
					return;
				}
				// ** ���λ��� �κ� ��Ž� ��� **// by �����
				if (count >= item.getCount())
					count = item.getCount();

				if (!targetPc.getInventory().checkItem(L1ItemId.ADENA, count * buyPrice)) {
					targetPc.sendPackets(new S_ServerMessage(189)); // \f1�Ƶ�����/ �����մϴ�.
					break;
				}

				L1ItemInstance adena = targetPc.getInventory().findItemId(
						L1ItemId.ADENA);
				if (adena == null)
					break;

				targetPc.getInventory().tradeItem(adena, count * buyPrice,
						pc.getInventory());
				pc.getInventory().tradeItem(item, count,
						targetPc.getInventory());
				psbl.setBuyCount(count + buyCount);
				buyList.set(order, psbl);

				if (psbl.getBuyCount() == psbl.getBuyTotalCount()) { // �� ������
																		// ������
																		// ���
					isRemoveFromList[order] = true;
				}

				try {
					pc.saveInventory();
					targetPc.saveInventory();
				} catch (Exception e) {
					// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
			// ������ �������� ����Ʈ�� ���̷κ��� ����
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					buyList.remove(i);
				}
			}
			targetPc.setTradingInPrivateShop(false);
		}
	}

	private void sellItemToShop(L1PcInstance pc, int npcId, int size) {
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
		int itemNumber;
		long itemcount;

		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			if (npcId >= 9000001 && npcId <= 9000036 && !pc.getInventory().getItem(itemNumber).isPackage()) {
				pc.sendPackets(new S_SystemMessage("\\fY��Ű���������� �������� ���� �������� ���ԵǾ� �ֽ��ϴ�."));
				return;
			}
			orderList.add(itemNumber, (int) itemcount, pc);
			if (orderList.BugOk() != 0) {
			    /*pc.sendPackets(new S_Disconnect());
			    pc.getNetConnection().kick();
			    pc.getNetConnection().close();*/
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					if (player.isGm() || pc == player) {
						player.sendPackets(new S_SystemMessage(pc.getName()+ "�� ���� �ִ뱸�� �����ʰ� ("+itemcount+")"));
					}
				}
			  }
		}
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.buyItems(orderList);
			// �����������Ŵ
			pc.saveInventory();
			// �����������Ŵ
		}
	}
	private void sellItemToNpcShop(L1PcInstance pc, int npcId, int size) {
		L1Shop shop = NpcCashShopTable.getInstance().get(npcId);
		L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
		int itemNumber; long itemcount;

		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			//itemcount = readD();
			itemcount = 1;
			//System.out.println(itemNumber + " " + itemcount);
			if(itemcount <= 0){
				return;
			}			
			orderList.add(itemNumber, (int)itemcount , pc);	
		}
		int bugok = orderList.BugOk();
		//System.out.println("ok");
		if (bugok == 0){
			shop.buyItemsToNpcShop(orderList, npcId);
		}		
	}
	/** ���� ���� ���Ǿ� ������ ���� */
	private void buyItemFromNpcShop(L1PcInstance pc, int npcId, int size) {
		L1Shop shop = NpcShopTable.getInstance().get(npcId);
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber;
		long itemcount;

		if (shop.getSellingItems().size() < size) {
			System.out.println("������ �Ǹ��ϴ� ������ ��(" + shop.getSellingItems().size() + ")���� �� ���� ����� ��.("+size+")��");
			pc.sendPackets(new S_Disconnect());
			pc.getNetConnection().kick();
			pc.getNetConnection().close();
			return;
		}
		
		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			if(size >= 2){
				pc.sendPackets(new S_SystemMessage("�ѹ��� ���� �ٸ��������� �����Ҽ������ϴ�."));
				return;
			}
			//if(pc.getMapId() == 360){			
				if(itemcount > 15) {
					pc.sendPackets(new S_SystemMessage("�ִ뱸�ż��� : ���۷�(15����) / ����(1����)"));
					return;
				//}
			}
			orderList.add(itemNumber, (int) itemcount, pc);
		}
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.sellItems(pc, orderList);
			// �鼷���� ���� ���������׹���
			pc.saveInventory();
			// �鼷���� ���� ���������׹���
		}
	}
	/** ĳ�� ������ ���� */
	private void buyItemFromNpcCashShop(L1PcInstance pc, int npcId, int size){
		L1Shop shop = NpcCashShopTable.getInstance().get(npcId);
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber; long itemcount;
		
		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if(itemcount <= 0) {
				return;
			}
			if(size >= 2){ //���ÿ� �ٸ������� ������� 2���� ���õȴٸ�,
				pc.sendPackets(new S_SystemMessage("\\fY�ѹ��� ���� �ٸ��������� �����Ҽ������ϴ�."));
				return;
			}
			//if(pc.getMapId() == 631){//���������� ���۸� �����ϰ��߱⶧����, �������忡�� 15���� ���� �̻� �Ȼ�����			
				if(itemcount > 1) {
					pc.sendPackets(new S_SystemMessage("\\fY�ִ뱸�ż��� : ����(1����)"));
					return;
				//}
			}	
			orderList.add(itemNumber, (int)itemcount , pc);	
		}
		int bugok = orderList.BugOk();
		if (bugok == 0){
			shop.sellItems(pc, orderList);
		    //�鼷���� ���� ���������׹���
		    pc.saveInventory();
		    //�鼷���� ���� ���������׹���
		}
	}
	

	private void buyItemFromPrivateShop(L1PcInstance pc, L1Object findObject,
			int size) {
		L1PcInstance targetPc = null;
		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		if (targetPc.isTradingInPrivateShop())
			return;

		FastTable<L1PrivateShopSellList> sellList = targetPc.getSellList();

		synchronized (sellList) {
			// ǰ���� �߻���, �������� �����ۼ��� ����Ʈ���� �ٸ���
			if (pc.getPartnersPrivateShopItemCount() != sellList.size())
				return;
			if (pc.getPartnersPrivateShopItemCount() < sellList.size())
				return;

			targetPc.setTradingInPrivateShop(true);

			L1ItemInstance item;
			L1PrivateShopSellList pssl;
			boolean[] isRemoveFromList = new boolean[8];
			int order, count, price, sellCount, sellPrice, itemObjectId, sellTotalCount;
			for (int i = 0; i < size; i++) { // ���� ������ ��ǰ
				order = readD();
				count = readD();

				pssl = (L1PrivateShopSellList) sellList.get(order);
				itemObjectId = pssl.getItemObjectId();
				sellPrice = pssl.getSellPrice();
				sellTotalCount = pssl.getSellTotalCount(); // �� ������ ����
				sellCount = pssl.getSellCount(); // �� ����
				item = targetPc.getInventory().getItem(itemObjectId);

				if (item == null)
					break;
				//** �߰�� ������� ���� ���� **//
				  long nowtime = System.currentTimeMillis();
				  if(item.getItemdelay3() >=  nowtime ){
					  break;
				  }  
				  //** �߰�� ������� ���� ���� **//
				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905, "")); // ��� �ϰ� �ִ�
																	// ������ ����
																	// ���ϰ�.
					break;
				}
				if (count > sellTotalCount - sellCount)
					count = sellTotalCount - sellCount;
				if (count == 0)
					break;
				/*���׹���*/
				int itemType = item.getItem().getType2();
				if(size >= 2){
					pc.sendPackets(new S_SystemMessage("�ѹ��� ���� �ٸ��������� �����Ҽ������ϴ�."));
					break;
				}
				//if(targetPc.getMapId() == 360){
					if(count > 15) {
						pc.sendPackets(new S_SystemMessage("�ִ뱸�ż��� : ���۷�(15����) / ����(1����)"));
						break;
					//}
				}
	            if(count <= 0){
					pc.sendPackets(new S_Disconnect());
					return;
				}
				
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (item == null || item.getCount() < count) {
			    	 pc.sendPackets(new S_Disconnect());
			         return;
			    }
			    if ((itemType == 1 && item.getCount() != 1) ||	(itemType == 2 && item.getCount() != 1)){
					pc.sendPackets(new S_Disconnect());
					return;
				}
			    if (count <= 0 || count < 1 || item.getCount() <= 0) {
			    	pc.sendPackets(new S_Disconnect());
			        return;
			    }			
		        if (item.getCount() > 2000000000) {
				    return;
				}
			    if (count > 2000000000) {
				    return;
				}
				if (!isAvailablePcWeight(pc, item, count))
					break;
				if (isOverMaxAdena(pc, sellPrice, count))
					break;

				price = count * sellPrice;
				if (price <= 0 || price > 2000000000)
					break;

				if (!isAvailableTrade(pc, targetPc, itemObjectId, item, count))
					break;

				if (count >= item.getCount())
					count = item.getCount();
				if (item.getCount() > 9999)
					break;

				if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ�ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
					pc.sendPackets(s_chatpacket);
					break;
				}

				L1ItemInstance adena = pc.getInventory().findItemId(
						L1ItemId.ADENA);

				if (targetPc == null || adena == null)
					break;
				if (targetPc.getInventory().tradeItem(item, count,
						pc.getInventory()) == null)
					break;

				pc.getInventory().tradeItem(adena, price,
						targetPc.getInventory());

				// %1%o %0�� �Ǹ��߽��ϴ�.
				String message = item.getItem().getName() + " ("
						+ String.valueOf(count) + ")";
				targetPc.sendPackets(new S_ServerMessage(877, pc.getName(),
						message));

				pssl.setSellCount(count + sellCount);
				sellList.set(order, pssl);

				writeLogbuyPrivateShop(pc, targetPc, item, count, price);

				if (pssl.getSellCount() == pssl.getSellTotalCount()) // �ش� ����
																		// �� �ȾҴ�
					isRemoveFromList[order] = true;
				try {
					pc.saveInventory();
					targetPc.saveInventory();
				} catch (Exception e) {
				}
			}

			// ǰ���� �������� ����Ʈ�� ���̷κ��� ����
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					sellList.remove(i);
				}
			}
			targetPc.setTradingInPrivateShop(false);
		}
	}

	private void buyItemFromShop(L1PcInstance pc, int npcId, int size) {
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber;
		long itemcount;
		if (shop.getSellingItems().size() < size) {
			   System.out.println("������ �Ǹ��ϴ� ������ ��(" + shop.getSellingItems().size() + ")���� �� ���� ����� ��.("+size+")��");
			   pc.getNetConnection().kick();
			   pc.getNetConnection().close();
			   return;
			  }
		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			
			if (npcId >= 9000001 && npcId <= 9000036) {
				if (itemcount > 1) {
					pc.sendPackets(new S_SystemMessage("\\fY���� �������� 1���� ���԰����մϴ�."));
					return;
				}
			}
			
			
			orderList.add(itemNumber, (int) itemcount, pc);
			if (orderList.BugOk() != 0) {
			  /*  pc.sendPackets(new S_Disconnect());
			    pc.getNetConnection().kick();
			    pc.getNetConnection().close();
			    return;*/
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					if (player.isGm() || pc == player) {
						player.sendPackets(new S_SystemMessage(pc.getName()+ "�� ���� �ִ뱸�� �����ʰ� ("+itemcount+")"));
					}
				}
			  }
		}
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.sellItems(pc, orderList);
			// �����������Ŵ
			pc.saveInventory();
			// �����������Ŵ
		}
	}
	
	private static void UpdateLog(String name, String clanname, String itemname, int count, int type) {//������ �߰�
		  Connection con = null;
		  PreparedStatement pstm = null;
		  Timestamp time = new Timestamp(System.currentTimeMillis());
		  try {
		   con = L1DatabaseFactory.getInstance().getConnection();
		   pstm = con.prepareStatement("INSERT INTO clan_warehouse_log SET name=?, clan_name=?, item_name=?, item_count=?, type=?, time=?");
		   pstm.setString(1, name);
		   pstm.setString(2, clanname);
		   pstm.setString(3, itemname);
		   pstm.setInt(4, count);
		   pstm.setInt(5, type);
		   pstm.setTimestamp(6, time);
		   pstm.execute();
		  } catch (SQLException e) {
		  } finally {
		   SQLUtil.close(pstm);
		   SQLUtil.close(con);
		  }
		 }


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
	

	private void writeLogbuyPrivateShop(L1PcInstance pc, L1PcInstance targetPc,
			L1ItemInstance item, int count, int price) {
		String itemadena = item.getName() + "(" + price + ")";
		eva.LogShopAppend("����", pc.getName(), targetPc.getName(), item.getEnchantLevel(), itemadena, item.getBless(), count, item.getId());
	}

	private boolean isOverMaxAdena(L1PcInstance pc, int sellPrice, int count) {
		if (sellPrice * count > 2000000000
				||sellPrice * count < 0) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"));
			return true;
		}
		if(count < 0){
			return true;
		}
		if(sellPrice < 0){
			return true;
		}
		return false;
	}

	private boolean checkPetList(L1PcInstance pc, L1ItemInstance item) {
		L1DollInstance doll = null;
		Object[] dollList = pc.getDollList().values().toArray();
		for (Object dollObject : dollList) {
			doll = (L1DollInstance) dollObject;
			if (item.getId() == doll.getItemObjId()) {
				pc.sendPackets(new S_ServerMessage(1181)); // 
				return false;
			}
		}
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					// \f1%0�� �����ų� �Ǵ� Ÿ�ο��� �絵 �� �� �����ϴ�.
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName()));
					return false;
				}
			}
		}
		return true;
	}

	private boolean isAvailableWhCount(Warehouse warehouse, L1PcInstance pc,
			L1ItemInstance item, int count) {
		if (warehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
			// \f1��밡 ������ �ʹ� ������ �־� �ŷ��� �� �����ϴ�.
			pc.sendPackets(new S_ServerMessage(75));
			return false;
		}
		return true;
	}

	private boolean isAvailableClan(L1PcInstance pc, L1Clan clan) {
		if (pc.getClanid() == 0 || clan == null) {
			// \f1���� â�� ����Ϸ��� ���Ϳ� �������� ������ �ȵ˴ϴ�.
			pc.sendPackets(new S_ServerMessage(208));
			return false;
		}
		return true;
	}

	private boolean isAvailablePcWeight(L1PcInstance pc, L1ItemInstance item,
			int count) {
		if (pc.getInventory().checkAddItem(item, count) != L1Inventory.OK) {
			// \f1 ������ �ִ� ���� ���ſ��� �ŷ��� �� �����ϴ�.
			pc.sendPackets(new S_ServerMessage(270));
			return false;
		}
		return true;
	}

	private boolean hasAdena(L1PcInstance pc) {
		if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
			// \f1�Ƶ����� �����մϴ�.
			S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�Ƶ����� ���ġ �ʽ��ϴ�.", Opcodes.S_OPCODE_MSG, 20); 
			pc.sendPackets(s_chatpacket);
			return false;
		}
		return true;
	}

	private boolean isAvailableTrade(L1PcInstance pc, int objectId,
			L1ItemInstance item, int count) {
		boolean result = true;

		if (item == null)
			result = false;
		if (objectId != item.getId())
			result = false;
		if (!item.isStackable() && count != 1)
			result = false;
		if (item.getCount() <= 0 || item.getCount() > 2000000000)
			result = false;
		if (count <= 0 || count > 2000000000)
			result = false;

		if (!result) {
			pc.sendPackets(new S_Disconnect());
		}

		return result;
	}

	private boolean isAvailableTrade(L1PcInstance pc, L1PcInstance targetPc,
			int itemObjectId, L1ItemInstance item, int count) {
		boolean result = true;

		if (item == null)
			result = false;
		if ((itemObjectId != item.getId())
				|| (!item.isStackable() && count != 1))
			result = false;
		if (count <= 0 || item.getCount() <= 0 || item.getCount() < count)
			result = false;
		if (count > 2000000000 || item.getCount() > 2000000000)
			result = false;

		if (!result) {
			pc.sendPackets(new S_Disconnect());
			targetPc.sendPackets(new S_Disconnect());
		}

		return result;
	}

	/*private void NpcEffect(L1PcInstance pc, L1Object target, int npcId,
			int resultType) {
		try {
			if (pc == null || target == null) {
				return;
			}

			if (resultType == TYPE_BUY_SHP || resultType == TYPE_SEL_SHP) {
				int castgfx = 0;
				if (npcId  >= 9000001 &&  npcId <= 9000014) {
					castgfx = 763;
				} else {
					castgfx = 0;
				}
				if (castgfx != 0) {
					pc.sendPackets(new S_SkillSound(target.getId(), castgfx));
					Broadcaster.broadcastPacket(pc, new S_SkillSound(target
							.getId(), castgfx));
				}
			} else {
			}
		} catch (Exception e) {
		}
	}*/

	@Override
	public String getType() {
		return "[C] C_Result";
	}
}