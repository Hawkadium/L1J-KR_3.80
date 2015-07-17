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

package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class Armor extends L1ItemInstance {

	
	public Armor(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			/** �δ� ���� �ȵǵ��� */
			if (pc.getMapId() == 5302|| pc.getMapId() == 5083|| pc.getMapId() == 5153) { // ��Ʋ/�δ�/������
				if (itemId == 20077 || itemId == 120077 || itemId == 20062 || itemId ==421003 || itemId ==421004
						|| itemId == 421005 || itemId == 21005 || itemId == 421006 || itemId == 421007 || itemId == 421008 || itemId == 30458) {
				pc.sendPackets(new S_ServerMessage(1170));
				return;
			}
			}
			/** �δ� ���� �ȵǵ��� */
			if (useItem.getItem().getType2() == 2) { // ���������� �ⱸ
				if (pc.isGm()) {
					UseArmor(pc, useItem);
				} else if (pc.isCrown() && useItem.getItem().isUseRoyal()
						|| pc.isKnight() && useItem.getItem().isUseKnight()
						|| pc.isElf() && useItem.getItem().isUseElf()
						|| pc.isWizard() && useItem.getItem().isUseMage()
						|| pc.isDarkelf() && useItem.getItem().isUseDarkelf()
						|| pc.isDragonknight()
						&& useItem.getItem().isUseDragonKnight()
						|| pc.isIllusionist()
						&& useItem.getItem().isUseBlackwizard()) {

					int min = ((L1Armor) useItem.getItem()).getMinLevel();
					int max = ((L1Armor) useItem.getItem()).getMaxLevel();
					if (min != 0 && min > pc.getLevel()) {
						// �� ��������%0���� �̻��� ���� ������ ����� �� �����ϴ�.
						pc.sendPackets(new S_ServerMessage(318, String
								.valueOf(min)));
					} else if (max != 0 && max < pc.getLevel()) {
						// �� ��������%d���� ���ϸ� ����� �� �ֽ��ϴ�.
						// S_ServerMessage������ �μ��� ǥ�õ��� �ʴ´�
						if (max < 50) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max));
						} else {
							pc.sendPackets(new S_SystemMessage("�� ��������" + max+ "���� ���ϸ� ����� �� �ֽ��ϴ�. "));
						}
					} else {
						UseArmor(pc, useItem);
					}
				} else {
					// \f1����� Ŭ���������� �� �������� ����� �� �����ϴ�.
					pc.sendPackets(new S_ServerMessage(264));
				}
			}
		}
	}

	private void UseArmor(L1PcInstance activeChar, L1ItemInstance armor) {
		int type = armor.getItem().getType();

		L1PcInventory pcInventory = activeChar.getInventory();
		boolean equipeSpace; // ��� �ϴ� ���Ұ� ��� ������
		if (type == 9) { // ���� ���
			equipeSpace = pcInventory.getTypeEquipped(2, 9) <= 4;
		} else {
			equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
		}
		if (equipeSpace && !armor.isEquipped()) { // ����� ���� �ⱸ�� ��� �ϰ� ���� �ʾ�,
			// �� ��� ���Ұ� ��� �ִ� ���(������
			// �õ��Ѵ�)
			/*if(type == 9){ // ����
				if(pcInventory.getTypeEquipped(2, 9) == 2){ // 2�� ������ ����
					if(activeChar.getLevel() < 76){ // ��������
						activeChar.sendPackets(new S_SystemMessage("\\aD76����<��������>����Ȯ��:�ش���� �߰� ���� �Ұ�"));
						return;
					}

				}else if(pcInventory.getTypeEquipped(2, 9) == 3){ // 3�� ������ ����
					if(activeChar.getLevel() < 81){ // ��������
						activeChar.sendPackets(new S_SystemMessage("\\aD81����<��������>����Ȯ��:�ش���� �߰� ���� �Ұ�"));
						return;
					}
				}
				// ���� ���� ����
				if(pcInventory.getTypeAndItemIdEquipped(2, 9, armor.getItem().getItemId()) == 2){ // �̹� 2�� ���� ��
					switch(armor.getItem().getItemId()){
					case 5000042:case 5000043:case 5000044:case 5000045:case 5000046: //�����
					case 420008: case 420009:case 500039:case 20286:case 20282:case 22004:
					case 20280:case 20285:case 20302:case 20289:case 20281:case 20284:
					case 20287:case 20288:case 20295:case 20296:case 20300:case 20304:
					case 120285:case 120280:case 120289:case 120300:case 120304:case 220294:
					case 423011:case 425100:case 425101:case 425102:case 425103:case 425104:case 425105:
						activeChar.sendPackets(new S_SystemMessage("\\aD�ش���� �߰� ���� �Ұ�"));
						return; 
					default:
						activeChar.sendPackets(new S_SystemMessage("\\aD<������ �����ϰų� ���� ������ ����Ұ���>"));
						return;
					}
				}
			}*/
			if(type == 9){ // Ÿ���� 9 ���
				if(!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT76) && /*activeChar.getLevel() >= 1 && activeChar.getLevel() <= 75 &&*/ pcInventory.getTypeEquipped(2, 9) >= 2){ // 1~75����
					activeChar.sendPackets(new S_SystemMessage("\\aD76����<������>Ȯ�彽�԰����� ��밡���մϴ�."));
					return;
				}
				else if(!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT81) && /*activeChar.getLevel() >= 76 && activeChar.getLevel() <= 80 &&*/ pcInventory.getTypeEquipped(2, 9) >= 3){ // 76~80����
					activeChar.sendPackets(new S_SystemMessage("\\aD81����<������>Ȯ�彽�԰����� ��밡���մϴ�."));
					return;
				}
				else if(pcInventory.getTypeEquipped(2, 9) == 4){ // 4���� �������̸� ���̻�����Ұ�
					activeChar.sendPackets(new S_SystemMessage("\\aD���̻� ������ �Ұ����մϴ�."));
					return;
				}
			}
			if(pcInventory.getTypeAndItemIdEquipped(2, 9, armor.getItem().getItemId()) == 2){ // �̹� 2�� ���� ��
			   activeChar.sendPackets(new S_SystemMessage("\\aG������ �̸��� �������� �ִ� 2������ ������ �����մϴ�.")); //�̹� ����ü��2�� ����ü�� �Ѱ� �� 
				return;	
			}
		
			
			int polyid = activeChar.getGfxId().getTempCharGfx();

			if (!L1PolyMorph.isEquipableArmor(polyid, type)) { // �� ���ſ����� ��� �Ұ�
				activeChar.sendPackets(new S_ServerMessage(2055,armor.getName()));
				return;
			}
			if (type == 7 && pcInventory.getTypeEquipped(2, 13) >= 1
					|| type == 13 && pcInventory.getTypeEquipped(2, 7) >= 1) {
				activeChar.sendPackets(new S_ServerMessage(124)); // \f1 ����
				// �����ΰ��� ���
				// �ϰ� �ֽ��ϴ�.
				return;
			}

			if (type == 7 && activeChar.getWeapon() != null) { // ����(shield)��
				// ���, ���⸦ ��� �ϰ�
				// ������(��) ��� ����
				// üũ
				if (activeChar.getWeapon().getItem().isTwohandedWeapon()&& armor.getItem().getUseType() != 13) { // ���
					// ����
					activeChar.sendPackets(new S_ServerMessage(129)); // \f1����� ���⸦������ä�� ������Ұ�
					return;
				}
			}
			/*if (type == 3 && pcInventory.getTypeEquipped(2, 4) >= 1) { // ������
				// ���,
				// ���並
				// ����
				// ������
				// Ȯ��
				activeChar.sendPackets(new S_ServerMessage(126, "$224", "$225")); // \f1%1��%0��
				// ���� ��
				// �����ϴ�.
				return;
			} else if ((type == 3) && pcInventory.getTypeEquipped(2, 2) >= 1) { // ������
				// ���,
				// ������
				// ����
				// ������
				// Ȯ��
				activeChar
						.sendPackets(new S_ServerMessage(126, "$224", "$226")); // \f1%1��%0��
				// ���� ��
				// �����ϴ�.
				return;
			} else if ((type == 2) && pcInventory.getTypeEquipped(2, 4) >= 1) { // ������
				// ���,
				// ���並
				// ����
				// ������
				// Ȯ��
				activeChar
						.sendPackets(new S_ServerMessage(126, "$226", "$225")); // \f1%1��%0��
				// ���� ��
				// �����ϴ�.
				return;
			}*/

			activeChar.cancelAbsoluteBarrier(); // �ƺ�Ҹ�Ʈ�ٸ����� ����
			pcInventory.setEquipped(armor, true);
		} else if (armor.isEquipped()) { // ����� ���� �ⱸ�� ��� �ϰ� �־��� ���(Ż����
			// �õ��Ѵ�)
			if (armor.getItem().getBless() == 2) { // ���������� �־��� ���
				activeChar.sendPackets(new S_ServerMessage(150)); // \f1 �� ����
				// �����ϴ�. ���ָ�
				// ��ĥ �� �ְ�
				// �ִ� ��
				// �����ϴ�.
				return;
			}
		/*	if (type == 3 && pcInventory.getTypeEquipped(2, 2) >= 1) { // ������
				// ���,
				// ������
				// ����
				// ������
				// Ȯ��
				activeChar.sendPackets(new S_ServerMessage(127)); // \f1�װ��� ����
				// ���� �����ϴ�.
				return;
			} else if ((type == 2 || type == 3)
					&& pcInventory.getTypeEquipped(2, 4) >= 1) { // ������ ������
				// ���, ���並
				// ���� ������ Ȯ��
				activeChar.sendPackets(new S_ServerMessage(127)); // \f1�װ��� ����
				// ���� �����ϴ�.
				return;
			}*/
			if (type == 7) {
				if (activeChar.getSkillEffectTimerSet().hasSkillEffect(	L1SkillId.SOLID_CARRIAGE)) {
					activeChar.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.SOLID_CARRIAGE);
				}
			}
			pcInventory.setEquipped(armor, false);
		} else {
/*			// ���� ���� �޼��� ó��.
			if (type == 9) {
				  if (activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT76))
					activeChar.sendPackets(new S_SystemMessage("\\fY76���� 3���� ���� �����Ͻ� �� �ֽ��ϴ�."));
				else   if (activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT81)){
					activeChar.sendPackets(new S_SystemMessage("\\fY81���� 4���� ���� �����Ͻ� �� �ֽ��ϴ�."));
				else
					activeChar.sendPackets(new S_ServerMessage(124));
			// ����
			} else {*/
				activeChar.sendPackets(new S_ServerMessage(124)); // \f1 ���� �����ΰ��� ��� �ϰ� �ֽ��ϴ�.
			//}
		}
		activeChar.setCurrentHp(activeChar.getCurrentHp());
		activeChar.setCurrentMp(activeChar.getCurrentMp());
		activeChar.sendPackets(new S_OwnCharAttrDef(activeChar));
		activeChar.sendPackets(new S_OwnCharStatus(activeChar));
		activeChar.sendPackets(new S_SPMR(activeChar));
		L1ItemDelay.onItemUse(activeChar, armor); // ������ ���� ����
	}
}
