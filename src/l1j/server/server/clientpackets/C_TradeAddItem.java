/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be trading_partnerful,
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

import server.LineageClient;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AutoShopBuyTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.AutoShopBuyTable.ItemInfo;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_TradeAddItem;
// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_TradeAddItem extends ClientBasePacket {
	private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";

	public C_TradeAddItem(byte abyte0[], LineageClient client) throws Exception {

		super(abyte0);

		int itemid = readD();
		int itemcount = readD();
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		L1Trade trade = new L1Trade();
		L1ItemInstance item = pc.getInventory().getItem(itemid);

		if (itemid != item.getId()) {
			client.kick();
			return;
		}
		if (!item.isStackable() && itemcount != 1) {
			client.kick();
			return;
		}
		if (itemcount <= 0 || item.getCount() <= 0) {
			return;
		}
		if (itemcount > item.getCount()) {
			itemcount = item.getCount();
		}
		if (itemcount > 2000000000) {
			return;
		}
		if (item.getItem().getItemId() == 423012
				|| item.getItem().getItemId() == 423013) { // 10�ֳ�Ƽ
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0��
			// �����ų�
			// �Ǵ�
			// Ÿ�ο���
			// ������
			// �� ��
			// �����ϴ�.
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
		if (!item.getItem().isTradable()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0��
			// �����ų�
			// �Ǵ�
			// Ÿ�ο���
			// ������
			// �� ��
			// �����ϴ�.
			return;
		}
				if (pc.getTradeID() == 0) { // Ʈ���̵����� �ƴѰ��
			return;
		}
		if (item.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0��
			// �����ų�
			// �Ǵ�
			// Ÿ�ο���
			// ������
			// �� ��
			// �����ϴ�.
			return;
		}
		if (item.isEquipped()) {
			pc.sendPackets(new S_ServerMessage(906));
			return;
		}
		L1DollInstance doll = null;
		Object[] dollList = pc.getDollList().values().toArray();
		for (Object dollObject : dollList) {
			doll = (L1DollInstance) dollObject;
			if (item.getId() == doll.getItemObjId()) {
				pc.sendPackets(new S_ServerMessage(1181)); // 
				return;
			}
		}
		Object[] petlist = pc.getPetList().values().toArray();
		L1PetInstance pet = null;
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					// \f1%0�� �����ų� �Ǵ� Ÿ�ο��� ������ �� �� �����ϴ�.
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName()));
					return;
				}
			}
		}
		L1Object tradingPartner = L1World.getInstance().findObject(pc.getTradeID());
		if (tradingPartner != null) {
			if(tradingPartner instanceof L1PcInstance){
		L1PcInstance tradepc = (L1PcInstance)tradingPartner;
		if (tradepc.getInventory(). checkAddItem(item, itemcount)
		!= L1Inventory.OK) { // �뷮 �߷� Ȯ�� �� �޼��� �۽�
			tradepc.sendPackets(new S_ServerMessage(270)); // \f1 ������ �ִ� ���� ���ſ��� �ŷ��� �� �����ϴ�.
		pc.sendPackets(new S_ServerMessage(271)); // \f1��밡 ������ �ʹ� ������ �־� �ŷ��� �� �����ϴ�.
		return;

		} 
		
		if (pc.getTradeOk() || tradepc.getTradeOk()) { // �ϷḦ �������¶�� ������ ���̻� �� �ø�
			 pc.sendPackets(new S_SystemMessage("�Ϸ��� ���¿����� �߰��� �������� �ø� �� �����ϴ�."));
			 tradepc.sendPackets(new S_SystemMessage("�Ϸ��� ���¿����� �߰��� �������� �ø� �� �����ϴ�."));
		      return;
		   }    // �̺κ��� �߰����ֽø� �˴ϴ�.
		    pc.setTradeOk(false);
		}else if(tradingPartner instanceof L1BuffNpcInstance){
			L1BuffNpcInstance target = (L1BuffNpcInstance)tradingPartner;
			if (pc.getTradeOk() || target.getTradeOk()) { return; }
		}	 
		}
		trade.TradeAddItem(pc, itemid, itemcount);
		/**���Ի���*/
		if (((L1PcInstance)tradingPartner).isAutoshop()) {	
			ItemInfo iteminfo = AutoShopBuyTable.getInstance().getBuyItemInfo(item);
			
			if (iteminfo == null) {
				Thread.sleep(1500);
				S_ChatPacket s_chatpacket = new S_ChatPacket((L1PcInstance)tradingPartner, "�˼������� �������� �ʴ� ǰ���Դϴ�.", Opcodes.S_OPCODE_NORMALCHAT, 0);			
				for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer((L1PcInstance)tradingPartner)) {
					if (!listner.getExcludingList().contains(((L1PcInstance)tradingPartner).getName())) {
						listner.sendPackets(s_chatpacket);
					}
				}
				trade.TradeCancel(pc);
				return;
			}	
			Thread.sleep(800);
			L1ItemInstance adena = ItemTable.getInstance().createItem(40308);
			adena.setCount(iteminfo.price * itemcount);
			((L1PcInstance)tradingPartner).getInventory().storeItem(adena);
			((L1PcInstance)tradingPartner).getInventory().tradeItem(adena, adena.getCount(), ((L1PcInstance)tradingPartner).getTradeWindowInventory());
			pc.sendPackets(new S_TradeAddItem(adena, adena.getCount(), 1));
		}
		/**���Ի���*/
	}
	@Override
	public String getType() {
		return C_TRADE_ADD_ITEM;
	}
}
