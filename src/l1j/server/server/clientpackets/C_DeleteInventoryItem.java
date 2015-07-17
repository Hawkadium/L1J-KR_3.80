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

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import server.CodeLogger;
import server.LineageClient;
import server.manager.eva;

// import server.manager.eva;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_DeleteInventoryItem extends ClientBasePacket {

	private static final String C_DELETE_INVENTORY_ITEM = "[C] C_DeleteInventoryItem";

	public C_DeleteInventoryItem(byte[] decrypt, LineageClient client) {
		  super(decrypt);
		  if (client == null){
		   return;
		  }
		  int itemObjectId = readD();
		  int count = readD();//�߰�
		  L1PcInstance pc = client.getActiveChar();
		  L1ItemInstance item = pc.getInventory().getItem(itemObjectId);

		  // �����Ϸ��� �� �������� ������ ���� ���
		  if (item == null) {
		   return;
		  }
		  if (item.getItem().isCantDelete()) {
		   pc.sendPackets(new S_ServerMessage(125));
		   return;
		  }
		  if (item.isEquipped()) {
		   // \f1������ �� ���� �������̳� ��� �ϰ� �ִ� �������� ���� �� �����ϴ�.
		   pc.sendPackets(new S_ServerMessage(125));
		   return;
		  }
		  if (!pc.isGm()
		    && (item.getItemId() >= 0 && (item.getItemId() == 46115//����
		      || item.getItemId() == 46116//��������
		      || item.getItemId() == 46118//��������
		      || item.getItemId() == 4500011//��������
		    		  || item.getItemId() == 41159//��������
		      || item.getItemId() == 46193))) {//������ħ
		   pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
		   return;
		  }
		  //if (item.getBless() >= 128) {//�����ۻ�������
		   //pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0��
		   // �����ų�
		   // �Ǵ�
		   // Ÿ�ο���
		   // ������
		   // �� ��
		   // �����ϴ�.
		   //return;
		  //}

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
		  CodeLogger.getInstance().oblog("�κ�����", pc.getName(), item,item.getCount());
		//eva.LogObserverAppend("�ۻ���","<"+pc.getName()+">"+item.getName() +"(+"+item.getEnchantLevel()+")"+"[����]"+" ("+item.getCount()+")"); // �߰�
			eva.LogObserverAppend("�ۻ���", pc.getName(), item, item.getCount(), itemObjectId);
		     if (count == 0)
		  count = item.getCount();
		  pc.getInventory().removeItem(item, count);
		  pc.getLight().turnOnOffLight();
		 }

		 @Override
		 public String getType() {
		return C_DELETE_INVENTORY_ITEM;
	}
}
