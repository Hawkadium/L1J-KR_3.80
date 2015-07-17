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

import server.LineageClient;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_TradeOK extends ClientBasePacket {

	private static final String C_TRADE_CANCEL = "[C] C_TradeOK";

	public C_TradeOK(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		}
		L1Object trading_partner = L1World.getInstance().findObject(
				player.getTradeID());
		if (trading_partner != null) {
			if(trading_partner instanceof L1PcInstance){
				L1PcInstance tradepc = (L1PcInstance)trading_partner;
				if (player.getTradeID() == 0) { // Ʈ���̵����� �ƴѰ��
					return;
				}
				player.setTradeOk(true);
				/**���Ի��� */
				if (player.getTradeOk() && tradepc.isAutoshop()) {
					if (player.getTradeWindowInventory().getSize() == 0) {
						L1Trade trade = new L1Trade();
						trade.TradeCancel(player);
						S_ChatPacket s_chatpacket = new S_ChatPacket(tradepc, "��ǰ�� �÷� �ּ���. �ٽ� �ŷ��ϽǷ��� Į����~", Opcodes.S_OPCODE_NORMALCHAT, 0);			
						for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(tradepc)) {
							if (!listner.getExcludingList().contains(tradepc.getName())) {
								listner.sendPackets(s_chatpacket);
							}
						}
						return;
					}
					tradepc.setTradeOk(true);
				}
				/**���Ի���*/
			if (player.getTradeOk() && tradepc.getTradeOk()){ // ��� OK�� ������
				if (tradepc.isChaTradeSlot()) {
					player.sendPackets(new S_SystemMessage("�ŷ� ��󿡰� �� ĳ���� ������ �����ϴ�."));
					tradepc.sendPackets(new S_SystemMessage("�� ĳ���� ������ �����ϴ�. ĳ���� ������ Ȯ���ϰ� �ٽ� �õ����ֽñ� �ٶ��ϴ�."));	
					return;
				}else if (player.isChaTradeSlot()) {
					tradepc.sendPackets(new S_SystemMessage("�ŷ� ��󿡰� �� ĳ���� ������ �����ϴ�."));
					player.sendPackets(new S_SystemMessage("�� ĳ���� ������ �����ϴ�. ĳ���� ������ Ȯ���ϰ� �ٽ� �õ����ֽñ� �ٶ��ϴ�."));	

					return;
				}
			
				// (180 - 16) ���̸��̶�� Ʈ���̵� ����.
				// ������ ��ġ�� ������(�Ƶ����� )�� �̹� ������ �ִ� ��츦 ������� �ʴ� ���� �� �ȴ�.
				if (player.getInventory().getSize() < (180 - 16)
						&& tradepc.getInventory().getSize() < (180 - 16)){ // ������ �������� ��뿡�� �ǳ��ش�			
					L1Trade trade = new L1Trade();
					trade.TradeOK(player);
				} else {// ������ �������� ���߿� �ǵ�����
					player.sendPackets(new S_ServerMessage(263)); // \f1�ѻ���� ĳ���Ͱ� ������ ���� �� �ִ� �������� �ִ� 180�������Դϴ�.
					tradepc.sendPackets(new S_ServerMessage(263)); // \f1�ѻ���� ĳ���Ͱ� ������ ���� �� �ִ� �������� �ִ� 180�������Դϴ�.
					return;
			}
			}
					/**���Ի��� */
					if (tradepc.isAutoshop()) {		
						tradepc.setTradeOk(false);
						tradepc.setTradeID(0);
						tradepc.getLight().turnOnOffLight();
						S_ChatPacket s_chatpacket = new S_ChatPacket(tradepc, "�����մϴ�. �㿡 �� �̿��� �ּ���.", Opcodes.S_OPCODE_NORMALCHAT, 0);			
						for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(tradepc)) {
							if (!listner.getExcludingList().contains(tradepc.getName())) {
								listner.sendPackets(s_chatpacket);
							}
						}
						player.setTradeOk(false);
						player.setTradeID(0);
						player.getLight().turnOnOffLight();
					}
			/**���Ի���*/
			
		}else if(trading_partner instanceof L1BuffNpcInstance){
			L1BuffNpcInstance target = (L1BuffNpcInstance)trading_partner;
			player.setTradeOk(true);
			if (player.getTradeOk()) // ��� OK�� ������
			{
				// (180 - 16) ���̸��̶�� Ʈ���̵� ����.
				// ������ ��ġ�� ������(�Ƶ����� )�� �̹� ������ �ִ� ��츦 ������� �ʴ� ���� �� �ȴ�.
				if (player.getInventory().getSize() < (180 - 16)
						&& target.getInventory().getSize() < (180 - 16)) {// ������ �������� ��뿡�� �ǳ��ش�				
					L1Trade trade = new L1Trade();
					trade.TradeOK(player);
				} else {// ������ �������� ���߿� �ǵ�����				
					player.sendPackets(new S_ServerMessage(263)); // \f1�ѻ���� ĳ���Ͱ� ������ ���� �� �ִ� �������� �ִ� 180�������Դϴ�.
					return;
				}

		}	

	}
		}
	}

	@Override
	public String getType() {
		return C_TRADE_CANCEL;
	}

}
