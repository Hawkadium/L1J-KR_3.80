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

import l1j.server.Config;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.CodeLogger;
import server.LineageClient;
import server.manager.eva;
import server.system.autoshop.AutoShopManager;

public class C_DropItem extends ClientBasePacket {

	private static final String C_DROP_ITEM = "[C] C_DropItem";

	public C_DropItem(byte[] decrypt, LineageClient client) throws Exception {
		super(decrypt);
		int x = readH();
		int y = readH();
		int objectId = readD();
		int count = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		// �����������Ŵ
		pc.saveInventory();
		// �����������Ŵ

		if (pc.getOnlineStatus() != 1) {
			pc.sendPackets(new S_Disconnect());
			return;
		}
		if (pc.isGhost())
			return;
					if (pc.getInventory().getWeight240() > 240) { // 28 == 100% 
			pc.sendPackets(new S_SystemMessage("���� ���׿� �ɸ��� ����� ���ѵ˴ϴ�."));
					return;
				}
		if (isTwoLogin(pc))
			return;

		L1ItemInstance item = pc.getInventory().getItem(objectId);
		if (item != null) {
			if (!pc.isGm() && !item.getItem().isTradable()) {
				// \f1%0�� �����ų� �Ǵ� Ÿ�ο��� ������ �� �� �����ϴ�.
				pc.sendPackets(new S_ServerMessage(210, item.getItem() .getName()));
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
			int itemType = item.getItem().getType2();

			if ((itemType == 1 && count != 1) || (itemType == 2 && count != 1)) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			 //** �߰�� ������� ���� ���� **//
			  long nowtime = System.currentTimeMillis();
			  if(item.getItemdelay3() >=  nowtime ){
			   return;
			  }  
			  //** �߰�� ������� ���� ���� **//
			if (item.getCount() <= 0) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (item.getCount() < count || count <= 0 || count > 2000000000) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (count > item.getCount()) {
				count = item.getCount();
				pc.sendPackets(new S_Disconnect());
				return;
			}

			if (x > pc.getX() + 1 || x < pc.getX() - 1 
					|| y > pc.getY() + 1 || y < pc.getY() - 1) {
				return;
			}
			if((item.getItem().getItemId() == 40074 || item.getItem().getItemId() == 40087
					||	 item.getItem().getItemId() == 140074 || item.getItem().getItemId() == 140087
					||	 item.getItem().getItemId() == 240074 || item.getItem().getItemId() == 240087)
					&& count >= 100){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {	
					if(player.isGm()){
						player.sendPackets(new S_SystemMessage("\\fRĳ���� :["+pc.getName()+"] ������ :["+item.getItem().getName()+"] ���� :["+count+"] ���"));
					}else if(player.getInventory().checkEquipped(20305)){ 
						player.sendPackets(new S_SystemMessage("\\fRĳ���� :["+pc.getName()+"] ������ :["+item.getItem().getName()+"] ���� :["+count+"] ���"));
					}
				}

			}
			if(item.getItem().getItemId() == 40308 && count >= 50000000){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {	
					if(player.isGm()){
						player.sendPackets(new S_SystemMessage("\\fRĳ���� :["+pc.getName()+"] �Ƶ��� :["+count+"] ���"));
					}else if(player.getInventory().checkEquipped(20305)){ 
						player.sendPackets(new S_SystemMessage("\\fRĳ���� :["+pc.getName()+"] �Ƶ��� :["+count+"] ���"));
					}
				} 
			}
			
			if (pc.getLocation().getTileLineDistance(pc.getLocation()) > 1) {
				pc.sendPackets(new S_SystemMessage("1ĭ �̳����� �������� ������ �ֽ��ϴ�."));
				return;
			}

			if(item.getCount() > 1 && pc.getLocation().getTileLineDistance(pc.getLocation()) > 1) {
				pc.sendPackets(new S_SystemMessage("�ش� ��Ŷ�� ����� ������ ���� ��Ŷ �Դϴ�."));
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
			if (item.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem()
						.getName())); // \f1%0��
				// �����ų�
				// �Ǵ�
				// Ÿ�ο���
				// ������
				// �� ��
				// �����ϴ�.
				return;
			}
			if (pc.getMaxWeight() <= pc.getInventory().getWeight()) {
				pc.sendPackets(new S_SystemMessage("����ǰ�� �ʹ� ���ſ��� �ൿ�� �� �����ϴ�."));
				return;
			}
			/** ���������Ƚ� * */
			L1DollInstance doll = null;
			Object[] dollList = pc.getDollList().values().toArray();
			for (Object dollObject : dollList) {
				doll = (L1DollInstance) dollObject;
				if (item.getId() == doll.getItemObjId()) {
					pc.sendPackets(new S_ServerMessage(1181));
					return;
				}
			}
			/** ���������Ƚ� * */

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
			if (pc.getSkillEffectTimerSet().hasSkillEffect(6524)) { 
			pc.sendPackets(new S_SystemMessage("������ 30�ʰ��� �ٴڿ� �������� ������ �����ϴ�."));
			return;
	        }
			if (pc.getLevel() < Config.ALT_DROPLEVELLIMIT) {
			pc.sendPackets(new S_SystemMessage("���� " + Config.ALT_DROPLEVELLIMIT + "���� �������� ���� �� �ֽ��ϴ�."));
			return;
			}
			if (!pc.isGm()
					&& (item.getItemId() >= 0 && (item.getItemId() == 40219
							|| item.getItemId() == 40222
							|| item.getItemId() == 40636
							|| item.getItemId() == 40639
							|| item.getItemId() == 40640
							|| item.getItemId() == 41148
							|| item.getItemId() == 80806
							|| item.getItemId() == 41247
							|| item.getItemId() == 350019
							|| item.getItemId() == 350020
							|| item.getItemId() == 350196
							|| item.getItemId() == 350195
							|| item.getItemId() == 40223
							|| item.getItemId() == 41247
							|| item.getItemId() == 61
							|| item.getItemId() == 86
							|| item.getItemId() == 40308// �Ƶ�
							// ////////�ֹ���
							|| item.getItemId() == 140087
							|| item.getItemId() == 140074
							|| item.getItemId() == 240087
							|| item.getItemId() == 240074
							|| item.getItemId() == 40087
							|| item.getItemId() == 40074
							|| item.getItemId() == 41159
					// ///////����ü
					|| item.getItemId() == 41246))) {// ���۸�������
				pc.sendPackets(new S_SystemMessage("�� �������� ������ �����ϴ�."));
				return;
			}
			// ��þ�� ������ ������ ���� ����!
			if (item.getEnchantLevel() >= 1 && !pc.isGm()) { // 1�̻� ��þ�� ��������
																// ��ӺҰ�
				pc.sendPackets(new S_SystemMessage("��þ�� �������� ������ �����ϴ�."));
				return;
			}
			// �������

			// * ���λ��������Ƚ�*//
				 //** ������ ������ ������Ÿ�̸� �߰� **//
			   int delay_time = 5000;
				 if(item != null){
				   if (item.isStackable()) {
				    if(item.getItemdelay3() <=  nowtime ){
				    	item.setItemdelay3( nowtime + delay_time );
						pc.sendPackets(new S_SystemMessage("5���Ŀ� �������� ������ �ֽ��ϴ�."));    	
				    	}
				        }
				    	}
				    //** ������ ������ ������Ÿ�̸� �߰� **//
			if (item.getId() >= 0
					&& (pc.getMapId() == 350 || pc.getMapId() == 340 || pc
							.getMapId() == 370)) {// ���忡���۸�������
				pc.sendPackets(new S_SystemMessage("���忡���� �������� ������ �����ϴ�."));
				return;
			}
			// * ���λ��������Ƚ�*//

			if (item.isEquipped()) {
				// \f1������ �� ���� �������̳� ��� �ϰ� �ִ� �������� ���� �� �����ϴ�.
				pc.sendPackets(new S_ServerMessage(125));
				return;
			}
						if (x > pc.getX() + 1 || x < pc.getX() - 1 
					|| y > pc.getY() + 1 || y < pc.getY() - 1) {
				return;
			} // �ż��ѿ��� ������ 1���� ����� ��������.
			CodeLogger.getInstance().oblog("�۵��", pc.getName(), item, count);  
			//eva.writeMessage(16,"<"+pc.getName()+">"+item.getName() +"(+"+item.getEnchantLevel()+")"+"[����]"+" ("+item.getCount()+")"); //�߰�
			eva.LogObserverAppend("�۵��", pc.getName(), item, count, objectId);
			pc.getInventory().tradeItem(item, count,L1World.getInstance().getInventory(x, y, pc.getMapId()));
			pc.getLight().turnOnOffLight();
			// �����������Ŵ
			pc.saveInventory();
			// �����������Ŵ
		}
	}

	@Override
	public String getType() {
		return C_DROP_ITEM;
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
	
}
