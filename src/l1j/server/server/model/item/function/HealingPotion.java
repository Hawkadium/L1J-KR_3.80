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

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.Random;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;

import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class HealingPotion extends L1ItemInstance {
	private static Random _random = new Random(System.nanoTime());

	public HealingPotion(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
		L1PcInstance pc = (L1PcInstance) cha;
		L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
		int itemId = useItem.getItemId();
		int delay_id = 0;
		delay_id = ((L1EtcItem) useItem.getItem()).get_delayid();
		if (delay_id != 0) { // ���� ���� �־�
		if (pc.hasItemDelay(delay_id) == true) {
		return;
		}
		}
//		�� ��ġ���� ����Ī
		SwitchingPotions(pc, itemId);
		pc.getInventory().removeItem(useItem, 1);
		L1ItemDelay.onItemUse(pc, useItem); // ������ ���� ����
		}
		}
	/** ����Ʈ ��ȣ 

	* ������ : 189

	* ��ȫ�� : 194

	* ������ : 197

	**/
	private void SwitchingPotions(L1PcInstance pc, int item_id){
	int effect = 0;
	int heal = 0;
	switch (item_id) {
	case 40010: //ü�� ȸ����
	case 40019: //���� ü�� ȸ����
	case 40022://����� ü�� ȸ����
	case 40029: //���ž�� ü�� ȸ����
	heal = calcHealing(pc, 9, 45, 0); effect = 189;
	break;
	case L1ItemId.MOPO_JUiCE1:    // ���̽� ������ �߰� ���ֽñ���
		heal = calcHealing(pc, 33, 89, 0); effect = 194;
    break;
	case L1ItemId.MOPO_JUiCE2:
		heal = calcHealing(pc, 33, 89, 0); effect = 194;
	break;
	case 140010: //�ູ���� ü�� ȸ����
	heal = calcHealing(pc, 9, 45, 1); effect = 189;
	break;
	case 240010: //���ֹ��� ü�� ȸ����
	heal = calcHealing(pc, 9, 45, -1); effect = 189;
	break;
	case 40011://��� ü�� ȸ����
	case 40020://��� ���� ü�� ȸ����
	case 40023: //����� ��� ü�� ȸ����
	heal = calcHealing(pc, 33, 89, 0); effect = 194;
	break;
	case 140011: //�ູ���� ��� ü�� ȸ����
	heal = calcHealing(pc, 33, 89, 1); effect = 194;
	break;
	case 40012://���� ü�� ȸ����
	case 40021://���� ���� ü�� ȸ����
	case 40024://����� ���� ü�� ȸ����
	case 404081://�Ƚ��� ���� ����
	case 435000://�ҷ��� ȣ�� ����(2009)
	heal = calcHealing(pc, 55, 135, 0); effect = 197;
	break;
	case 140012: //�ູ���� ���� ü�� ȸ����
	heal = calcHealing(pc, 55, 135, 1); effect = 197;
	break;
    case 40026: //�ٳ��� �ֽ�
	case 40027: //������ �ֽ�
	case 40028: //��� �ֽ�
	heal = calcHealing(pc, 11, 65, 0); effect = 189;
	break;
	case 41141://�ź��� ���� ����
//	[���� : �ź��� ���� �������� �߰�] �ź����������ǰ� ȸ������ ������ ���� ����
	heal = calcHealing(pc, 23, 56, 0); effect = 189;
	break;
	case 40043: //�䳢�� ��
	case 50006:
	heal = calcHealing(pc, 141, 1384, 0); effect = 189;
	break;
	case 40058: //������ ������
	heal = calcHealing(pc, 18, 58, 0); effect = 189;
	break;
	case 40071: //Ÿ�ٳ��� ������
	heal = calcHealing(pc, 46, 137, 0); effect = 197;
	break;
	case 40506: //��Ʈ�� ����
	heal = calcHealing(pc, 56, 136, 0); effect = 197;
	break;
	case 140506://�ູ���� ��Ʈ�� ����
	heal = calcHealing(pc, 56, 136, 1); effect = 197;
	break;
	case 40930: //�ٺ�ť
	heal = calcHealing(pc, 79, 183, 0); effect = 189;
	break;
	case 41298: //� �����
	heal = calcHealing(pc, 8, 10, 0); effect = 189;
	break;
	case 41299: //����� �����
	heal = calcHealing(pc, 7, 23, 0); effect = 194;
	break;
	case 41300: //���� �����
	heal = calcHealing(pc, 11, 65, 0); effect = 197;
	break;
	case 41337: //�ູ���� ������
	heal = calcHealing(pc, 44, 107, 0); effect = 197;
	break;
	case 41403: //������ �ķ�
	heal = calcHealing(pc, 124, 600, 0); effect = 189;
	break;
	/** ������ ���� ������ **/
	case 41417: //�� ���� ����(������ ���� ������) 197
	case 41418: //�� ���� ����(������ ���� ������) 197
	case 41419: //�� ���� ����(������ ���� ������) 197
	case 41420: //�� ���� ���(������ ���� ������) 197
	case 41421: //�� ���� ������(������ ���� ������) 197
	heal = calcHealing(pc, 50, 80, 0); effect = 197;
	break;
	case 40734: //�ŷ��� ���� (������ ���� ������) 189
	heal = calcHealing(pc, 40, 50, 1); effect = 189;
	break;
	case 41411: //������ (������ ���� �븸 ��������) 
	heal = calcHealing(pc, 40, 50, 1); effect = 197;
	break;
	case 41412: //������ (������ ���� �븸 ��������)
	heal = calcHealing(pc, 50, 60, 1); effect = 194;
	break;
	}
	UseHeallingPotion(pc, heal, effect);
	}
	/** ������ ȸ���� ���� �Լ� **/

	private int calcHealing(L1PcInstance pc, int minheal, int maxheal, int blessed){
	int heal = 0;
	int variable = 0;
	if(maxheal > 0){
	heal = minheal;
	variable = maxheal - minheal;
	heal += _random.nextInt(variable)+1;
	}
	if(blessed == 1){
	heal += _random.nextInt(minheal/2);
	}
	if(blessed == -1){
	heal -= _random.nextInt(minheal/2);
	}
//	����Ʈ ������ ��� ȸ���� �ݰ�
	if (pc.getSkillEffectTimerSet().hasSkillEffect(POLLUTE_WATER)) {
	heal /= 2;
	}
	return heal;
	}
	private void UseHeallingPotion(L1PcInstance pc, int healHp, int gfxid) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // ����������
			// ����
			pc.sendPackets(new S_ServerMessage(698)); // ���¿� ���� �ƹ��͵� ���� ����
			// �����ϴ�.
			return;
		}

		// �ۼַ�Ʈ�������� ����
		pc.cancelAbsoluteBarrier();
		
		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
		if (Config.WarPotionEffect)
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid));
	//	pc.sendPackets(new S_ServerMessage(77)); // \f1����� ���������ϴ�.
//		pc.sendPackets(new S_ServerMessage(77)); // \f1����� ���������ϴ�.
	//	S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"����� ���������ϴ�.", Opcodes.S_OPCODE_MSG, 20);
		//pc.sendPackets(s_chatpacket); 
		healHp *= (_random.nextGaussian() / 5.0D) + 1.0D;
		int ��Ƽ��Hp = 0;
		if (pc.isPinkName()) {
			/** Ǫ�� �����ζ� ȸ���� */
			if(pc.isPinkName()) {
				if ( pc.getInventory().getEnchantEquipped(500008, 1)
						|| pc.getInventory().getEnchantEquipped(500008, 2)
						|| pc.getInventory().getEnchantEquipped(500008, 3)
						|| pc.getInventory().getEnchantEquipped(500008, 4)
						|| pc.getInventory().getEnchantEquipped(500008, 5)
						|| pc.getInventory().getEnchantEquipped(500008, 6)
						|| pc.getInventory().getEnchantEquipped(500008, 7)
						|| pc.getInventory().getEnchantEquipped(500008, 8)){ //��Ƽ���� Ǫ�����Ͱ���
					��Ƽ��Hp = 2 * (0 + 1);
					healHp = healHp * (��Ƽ��Hp + 100) / 100 + ��Ƽ��Hp;
				}
			}
			/** Ǫ�� �����ζ� ȸ���� */
		}else {
		// //��Ƽ���� �Ͱ��� ����ȿ��
		if(pc.getInventory().checkEquipped(500008))
		  {
		   int cnt_enchant = pc.getInventory().getEnchantCount(500008);
		   ��Ƽ��Hp = 2*(cnt_enchant+1);
		   healHp = healHp * (��Ƽ��Hp + 100) / 100 + ��Ƽ��Hp;   
		  }
		}
		if (pc.getSkillEffectTimerSet().hasSkillEffect(POLLUTE_WATER)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(21023)) { // ����Ʈ��Ÿ����
																		// ȸ����1/2��
			healHp *= 0.5;
		}
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item.getItemId() == 500008 && item.isEquipped()) {
				int percent = 0;

				if (item.getEnchantLevel() < 9) {
					percent = (item.getEnchantLevel() * 2 + 2);
				} else if (item.getEnchantLevel() == 9) {
					percent = 18;
				}

				StringBuffer sb = new StringBuffer();
				sb.append("\\fY[ ���� ȸ���� : " + healHp + " ] \n");

				float addHp = ((float)healHp / 100) * percent;
				healHp = healHp + (int)addHp + percent;					

				sb.append("\\fU[ �߰� ȸ���� : +" + (int)addHp + " +" + percent + " ] \n");
				sb.append("\\fT[ ���� ȸ���� : " + healHp + " ]\n");
				if (pc.isGm()){
					pc.sendPackets(new S_SystemMessage(sb.toString()));
				}
				break;
			}
		}
		if (pc.getSkillEffectTimerSet().hasSkillEffect(10513)) {
			pc.setCurrentHp(pc.getCurrentHp() - healHp);
		} else {
			pc.setCurrentHp(pc.getCurrentHp() + healHp);
		}
	}
}
