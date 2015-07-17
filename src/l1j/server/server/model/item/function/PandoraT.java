package l1j.server.server.model.item.function;


import java.sql.Timestamp;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class PandoraT extends L1ItemInstance{
	
	public PandoraT(L1Item item){
		super(item);
	}
	
	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet){
		try{
			if(cha instanceof L1PcInstance){
				L1PcInstance pc = (L1PcInstance)cha;
				L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
				L1ItemInstance targetItem = pc.getInventory().getItem(packet.readD());
				int targetItemId = targetItem.getItemId();
				int itemId = this.getItemId();
				int EndTime = 360000 * 1000; // 5��
				switch(itemId){
				/** 
				 * ����Ƽ ��ȯ
				 * 
				 * **/
				case 89531:  // �ǵ����� �Ϸ²� ���:Ƽ[30��]
					switch (targetItemId) {
					case 20085: // Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22440, EndTime); // �ǵ����� �Ϸ²��� Ƽ
						break;						
					case 20084: // ������ Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22450, EndTime); // �ǵ����� �Ϸ²��� ������ Ƽ
						break;
					default:
						pc.sendPackets(new S_ServerMessage(328));
					}
					break;
				case 89532: // �ǵ����� ��ø�� ���:Ƽ[30��]
					switch (targetItemId) {
					case 20085: // Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22441, EndTime); // �ǵ����� ��ø���� Ƽ
						break;						
					case 20084: // ������ Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22451, EndTime); // �ǵ����� ��ø���� ������ Ƽ
						break;
					default:
						pc.sendPackets(new S_ServerMessage(328));
					}
					break;
				case 89533: // �ǵ����� ���Ĳ� ���:Ƽ[30��]
					switch (targetItemId) {
					case 20085: // Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22442, EndTime); // �ǵ����� ���Ĳ��� Ƽ
						break;						
					case 20084: // ������ Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22452, EndTime); // �ǵ����� ���Ĳ��� ������ Ƽ
						break;
					default:
						pc.sendPackets(new S_ServerMessage(328));
					}
					break;
				case 89534: // �ǵ����� ������ ���:Ƽ[30��]
					switch (targetItemId) {
					case 20085: // Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22443, EndTime); // �ǵ����� �������� Ƽ
						break;						
					case 20084: // ������ Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22453, EndTime); // �ǵ����� �������� ������ Ƽ
						break;
					default:
						pc.sendPackets(new S_ServerMessage(328));
					}
					break;
				case 89535: // �ǵ����� ü�²� ���:Ƽ[30��]
					switch (targetItemId) {
					case 20085: // Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22444, EndTime); // �ǵ����� ü�²��� Ƽ
						break;						
					case 20084: // ������ Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22454, EndTime); // �ǵ����� ü�²��� ������ Ƽ
						break;
					default:
						pc.sendPackets(new S_ServerMessage(328));
					}
					break;
				case 89536: // �ǵ����� �ŷ²� ���:Ƽ[30��]
					switch (targetItemId) {
					case 20085: // Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22445, EndTime); // �ǵ����� �ŷ²��� Ƽ
						break;						
					case 20084: // ������ Ƽ����
						PandoraTransform(pc, useItem, targetItem , 22455, EndTime); // �ǵ����� �ŷ²��� ������ Ƽ
						break;
					default:
						pc.sendPackets(new S_ServerMessage(328));
					}
					break;
				/** 
				 * õ�� ��
				 * 
				 * **/
				case 89530: // õ�� ��:�ǵ��� ���Ƽ
					switch (targetItemId) {
					// �Ϲ� Ƽ����
					case 22440: 
					case 22441:
					case 22442:
					case 22443:
					case 22444:
					case 22445:
						Soap(pc,useItem, targetItem, 20085);
						break;
					// ������ Ƽ����
					case 22450:
					case 22451:
					case 22452:
					case 22453:
					case 22454:
					case 22455:
						Soap(pc,useItem, targetItem, 20084);
						break;
					default:
						pc.sendPackets(new S_ServerMessage(328));
					}
					break;
				/** 
				 * �ǵ����� ����:Ƽ
				 * 
				 * **/
				case 89550: // �ǵ����� ���� ����:Ƽ
					PatternIn(pc, useItem, targetItem, 1);
					break;
				case 89551: // �ǵ����� Ȧ�� ����:Ƽ
					PatternIn(pc, useItem, targetItem, 2);
					break;
				case 89552: // �ǵ����� ȸ�� ����:Ƽ
					PatternIn(pc, useItem, targetItem, 3);
					break;
				case 89553: // �ǵ����� ��ö ����:Ƽ
					PatternIn(pc, useItem, targetItem, 4);
					break;
				case 89554: // �ǵ����� �긶 ����:Ƽ
					PatternIn(pc, useItem, targetItem, 5);
					break;
				case 89555: // �ǵ����� ü�� ����:Ƽ
					PatternIn(pc, useItem, targetItem, 6);
					break;
				case 89556: // �ǵ����� ���� ����:Ƽ
					PatternIn(pc, useItem, targetItem, 7);
					break;
				case 89557: // �ǵ����� ���� ����:Ƽ
					PatternIn(pc, useItem, targetItem, 8);
					break;
				case 89558: // �ǵ����� ��ȭ ����:Ƽ
					PatternIn(pc, useItem, targetItem, 9);
					break;
				/** 
				 * ��ȯ�� : ����Ƽ���� +1 ����Ͽ� �Ϲ� Ƽ������ ��ȯ.
				 *  
				 * **/
				case 46030: // ��ȯ��: +0 Ƽ
					Transform(pc, useItem, targetItem, 0);
					break;
				case 46031: // ��ȯ��: +1 Ƽ
					Transform(pc, useItem, targetItem, 1);
					break;
				case 46032: // ��ȯ��: +2 Ƽ
					Transform(pc, useItem, targetItem, 2);
					break;
				case 46033: // ��ȯ��: +3 Ƽ
					Transform(pc, useItem, targetItem, 3);
					break;
				case 46034: // ��ȯ��: +4 Ƽ
					Transform(pc, useItem, targetItem, 4);
					break;
				case 46035: // ��ȯ��: +5 Ƽ
					Transform(pc, useItem, targetItem, 5);
					break;
				case 46036: // ��ȯ��: +6 Ƽ
					Transform(pc, useItem, targetItem, 6);
					break;
				case 46037: // ��ȯ��: +7 Ƽ
					Transform(pc, useItem, targetItem, 7);
					break;
				case 46038: // ��ȯ��: +8 Ƽ
					Transform(pc, useItem, targetItem, 8);
					break;
				case 46039: // ��ȯ��: +9 Ƽ
					Transform(pc, useItem, targetItem, 9);
					break;
				case 46040: // ��ȯ��: +10 Ƽ
					Transform(pc, useItem, targetItem, 10);
					break;
				case 46041: // ��ȯ��: +11 Ƽ
					Transform(pc, useItem, targetItem, 11);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ����Ƽ ����
	private boolean createNewItemR(L1PcInstance pc, int item_id, int count, int enchant, int EndTime) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		item.setCount(count);
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + EndTime);//
		item.setEndTime(deleteTime);
		item.setIdentified(true);
		item.setEnchantLevel(enchant);
		if (item != null) {
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(),pc.getMapId()).storeItem(item);
			}
			return true;
		} else {
			return false;
		}
	}
	
	// �Ϲ�Ƽ ����
	private boolean createNewItemR(L1PcInstance pc, int item_id, int count, int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		item.setCount(count);
		item.setIdentified(true);
		item.setEnchantLevel(enchant);
		if (item != null) {
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(),pc.getMapId()).storeItem(item);
			}
			return true;
		} else {
			return false;
		}
	}
	
	// ����Ƽ�� ��ȯ
	private void PandoraTransform(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem, int itemid, int EndTime){
		createNewItemR(pc, itemid,1, targetItem.getEnchantLevel() ,EndTime);
		pc.getInventory().removeItem(useItem, 1);
		pc.getInventory().removeItem(targetItem, 1);
	}
	
	// ��
	private void Soap(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem, int itemid){
		if (targetItem.isEquipped()){
			pc.sendPackets(new S_ServerMessage(1317));
			return;
		}
		createNewItemR(pc, itemid, 1, targetItem.getEnchantLevel());
		pc.getInventory().removeItem(useItem, 1);
		pc.getInventory().removeItem(targetItem, 1);
		pc.saveInventory();
	}
	
	// ����
	private void PatternIn(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem, int pandoraset){
		int targetItemId = targetItem.getItem().getItemId();
		if (targetItem.getPandoraT() == pandoraset) {
			pc.sendPackets(new S_SystemMessage("�̹� ���� ������ �ο��Ǿ� �ֽ��ϴ�."));
			return;
		}
		if (targetItem.isEquipped()){
			pc.sendPackets(new S_ServerMessage(1317));
			return;
		}
		if (targetItemId >= 22440 && targetItemId <= 22445 || targetItemId >= 22405 && targetItemId <= 22455){ // �ǵ��� Ƽ����
			targetItem.setPandoraT(pandoraset);
			pc.getInventory().updateItem(targetItem, L1PcInventory.COL_PANDORA);
			pc.getInventory().saveItem(targetItem, L1PcInventory.COL_PANDORA);
			pc.getInventory().removeItem(useItem, 1);
			pc.saveInventory();
		} else {
			pc.sendPackets(new S_ServerMessage(328));
		}
	}
	
	// ��ȯ��
	private void Transform(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem, int enchant_check){
		int itemid = targetItem.getItem().getItemId();
		if (itemid >= 21028 && itemid <= 21033 || itemid >= 490000 && itemid <= 490017){ // ���� ����Ƽ
			if (targetItem.getEnchantLevel() == enchant_check){
				createNewItemR(pc, 20085, 1, targetItem.getEnchantLevel() + 1);
				pc.getInventory().removeItem(useItem, 1);
				pc.getInventory().removeItem(targetItem, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(328));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(328));
		}
	}
}