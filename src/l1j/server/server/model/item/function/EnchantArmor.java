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

import java.util.Random;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.AccessoryEnchantList;
import l1j.server.server.datatables.ArmorEnchantList;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class EnchantArmor extends Enchant {

	private static Random _random = new Random(System.nanoTime());

	public EnchantArmor(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(
					packet.readD());
			if (itemId == L1ItemId.SCROLL_OF_ENCHANT_ARMOR_WEAPON) {
				int enchant_level = l1iteminstance1.getEnchantLevel();
				if (enchant_level >= 11) { // ��ȭ�Ұ� ��ġ �˾Ƽ�...
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
				SuccessEnchant(pc, l1iteminstance1, +1);
				pc.getInventory().removeItem(useItem, 1);
				return;
			}
			if (pc.isInvisble()) {
				pc.sendPackets(new S_SystemMessage("������¿����� ��þƮ�� �Ҽ������ϴ�.."));
				return;
			}
			if (pc.getLastEnchantItemid() == l1iteminstance1.getId()) {
				pc.setLastEnchantItemid(l1iteminstance1.getId(),
						l1iteminstance1);
				return;
			}
			if (l1iteminstance1 == null
					|| l1iteminstance1.getItem().getType2() != 2) {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 �ƹ��͵� �Ͼ��
				// �ʾҽ��ϴ�.
				return;
			}
			if (l1iteminstance1.getBless() >= 128) { // ������
				pc.sendPackets(new S_ServerMessage(79)); // \f1 �ƹ��͵� �Ͼ��
				// �ʾҽ��ϴ�.
				return;
			}
			int safe_enchant = ((L1Armor) l1iteminstance1.getItem())
					.get_safeenchant();

			if (safe_enchant < 0) { // ��ȭ �Ұ�
				pc.sendPackets(new S_ServerMessage(79)); // \f1 �ƹ��͵� �Ͼ��
				// �ʾҽ��ϴ�.
				return;
			}
			int armorId = l1iteminstance1.getItem().getItemId();
			int armortype = l1iteminstance1.getItem().getType();
			int enchant_level = l1iteminstance1.getEnchantLevel();
			 /** �γ�Ƽ **/
			   if (armorId == 425108 || armorId == 425107
						|| armorId == 425106 || armorId == 21028 || armorId == 21029
						|| armorId == 21030 || armorId == 21031 || armorId == 21032
						|| armorId == 21033 ||(armorId >= 490000 && armorId <= 490017)) {//�Ӽ�Ƽ������ȣ
				   if (itemId == 40074 || itemId == 140074 || itemId == 140129 || itemId == 240074 
					||( itemId >= L1ItemId.Inadril_T_ScrollA && itemId <= L1ItemId.Inadril_T_ScrollD)){//Ƽ�����ֹ���
			     if (!l1iteminstance1.isEquipped()) {//��������
			     } else {
			      pc.sendPackets(new S_SystemMessage("Ƽ������ ���� �����ϼž� ���˴ϴ�."));
			      return;
			     }
			    } else {
			     pc.sendPackets(new S_ServerMessage(79));
			     return;
			    }
			   }
			   /** �γ�Ƽ **/
				 /** �γ�Ƽ **/
			   if (itemId >= L1ItemId.Inadril_T_ScrollA && itemId <= L1ItemId.Inadril_T_ScrollD){//Ƽ�����ֹ���
			   if (armorId == 425108 || armorId == 425107
						|| armorId == 425106 || armorId == 21028 || armorId == 21029
						|| armorId == 21030 || armorId == 21031 || armorId == 21032
						|| armorId == 21033 || (armorId >= 490000 && armorId <= 490017)) {//�Ӽ�Ƽ������ȣ
			     if (!l1iteminstance1.isEquipped()) {//��������
			     } else {
			    	 pc.sendPackets(new S_SystemMessage("Ƽ������ ���� �����ϼž� ���˴ϴ�."));
			      return;
			     }
			    } else {
			     pc.sendPackets(new S_ServerMessage(79));//�ƹ��ϵ�
			     return;
			    }
			   }
			   /** �γ�Ƽ **/
			/** �������� ���� ���� �ֹ���* */
			/*
			 * int a[] = {20028,20126,20206,20232,20173};
			 */
			if (armorId == 20028 || armorId == 20126 || armorId == 20206
					|| armorId == 20232 || armorId == 20173 || armorId == 20082) {
				if (itemId == L1ItemId.SCROLL_OF_ENCHANT_JOURNEY_ARMOR) {
					if (enchant_level >= 4) {
						 pc.sendPackets(new S_ServerMessage(1453));
						 //�� �̻� ��� ��ȭ�� �� �����ϴ�.
						return;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			if (itemId == L1ItemId.SCROLL_OF_ENCHANT_JOURNEY_ARMOR) {
				if (armorId == 20028 || armorId == 20126 || armorId == 20206
						|| armorId == 20232 || armorId == 20173
						|| armorId == 20082) {
					if (enchant_level >= 4) {
						 pc.sendPackets(new S_ServerMessage(1453));
						 //�� �̻� ��� ��ȭ�� �� �����ϴ�.
						return;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			/** �������� ���� ���� �ֹ���* */

			/** ȯ���� ���� ���� �ֹ���* */
			if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
				if (armorId >= 423000 && armorId <= 423008) {
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			/** ȯ���� ���� ���� �ֹ���* */

			/** âõ�� ���� ���� �ֹ���* */
			if (armorId >= 422000 && armorId <= 422020) {
				if (itemId == L1ItemId.CHANGCHUN_ENCHANT_ARMOR_SCROLL) {
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			if (itemId == L1ItemId.CHANGCHUN_ENCHANT_ARMOR_SCROLL) {
				if (armorId >= 422000 && armorId <= 422020) {
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			/** âõ�� ���� ���� �ֹ���* */

			/** ��ű� ��ȭ �ֹ��� */

			if (itemId == L1ItemId.ACCESSORY_ENCHANT_SCROLL|| itemId == 5000145|| itemId == L1ItemId.HALLOWEEN_2011_ACCESSORY_ENCHANT_SCROLL || itemId == L1ItemId.HALLOWEEN_2011_ACCESSORY_ENCHANT_SCROLL) {
				if (armortype >= 8 && armortype <= 12) {
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			/** ��Ƽ���� ��ȭ �ֹ��� **/
			if (itemId == 5000145){
			if (armorId >= 500007 && armorId <= 500009){
				if (enchant_level >= 8) {
					 pc.sendPackets(new S_ServerMessage(1453));
					 //�� �̻� ��� ��ȭ�� �� �����ϴ�.
			return;
		}
	} else {
		pc.sendPackets(new S_ServerMessage(79));
		return;
	}
}
			if (armorId >= 500007 && armorId <= 500009){
				
			if (itemId == 5000145){
				if (enchant_level >= 8) {
					 pc.sendPackets(new S_ServerMessage(1453));
					 //�� �̻� ��� ��ȭ�� �� �����ϴ�.
		return;
	}
} else {
	pc.sendPackets(new S_ServerMessage(79));
	return;
}
}
			/** ��Ƽ���� ��ȭ �ֹ��� **/ 
			if (itemId == 437027){
				if (armorId >= 5000042 && armorId <= 5000046){
					if (enchant_level >= 8) {
						 pc.sendPackets(new S_ServerMessage(1453));
						 //�� �̻� ��� ��ȭ�� �� �����ϴ�.
				return;
			}
		} else {
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}
	}
				if (armorId >= 5000042 && armorId <= 5000046){
					
				if (itemId == 437027){
					if (enchant_level >= 8) {
						 pc.sendPackets(new S_ServerMessage(1453));
						 //�� �̻� ��� ��ȭ�� �� �����ϴ�.
			return;
		}
	} else {
		pc.sendPackets(new S_ServerMessage(79));
		return;
	}
	}
	            if (armortype >= 8 && armortype <= 12) {
	            	
					if (itemId == L1ItemId.ACCESSORY_ENCHANT_SCROLL|| itemId == 5000145) {
					} else if (itemId == L1ItemId.WHITE_ACCESSORY_ENCHANT_SCROLL) {
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}
				}
			/** ��ű� ��ȭ �ֹ��� */
			if (Config.GAME_SERVER_TYPE == 1
					&& enchant_level >= safe_enchant + 3) {
				pc.sendPackets(new S_SystemMessage("�׽�Ʈ���������� ������þƮ+3 �̻��� ��þƮ�ϽǼ� �����ϴ�."));
				return;
			}
			if (enchant_level >= 11) { // ��þƮ ����

				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR
					|| itemId == L1ItemId.Inadril_T_ScrollC) { // �γ�Ƽ
				pc.getInventory().removeItem(useItem, 1);
				int rnd = _random.nextInt(100) + 1;
				if (safe_enchant == 0 && rnd <= 30) {
					FailureEnchant(pc, l1iteminstance1);
					return;
				}
				if (enchant_level < 1
						&& (armorId == 20011 || armorId == 20110
								|| armorId == 120011  || armorId == 20056
								|| armorId == 120056 || armorId == 220056 || armorId == 20078
								|| armorId == 76796)) {//���ž������
					FailureEnchant(pc, l1iteminstance1); // 0 ���� ���� �ٸ���
															// ���󰡴��۵�

				} else if (enchant_level < -1) { // �⺻�����۵��� -2���� ���� �ٸ��� ����
					FailureEnchant(pc, l1iteminstance1); // �߰�
				} else {
					SuccessEnchant(pc, l1iteminstance1, -1);
				}
			} else if (itemId == L1ItemId.Inadril_T_ScrollD) {// �γ�Ƽ
				pc.getInventory().removeItem(useItem, 1);
				RegistEnchant(pc, l1iteminstance1, itemId);
			} else if (enchant_level < safe_enchant) {
				pc.getInventory().removeItem(useItem, 1);
				SuccessEnchant(pc, l1iteminstance1, RandomELevel(
						l1iteminstance1, itemId));
			} else {
				pc.getInventory().removeItem(useItem, 1);
				int rnd = _random.nextInt(100) + 1;
				int enchant_chance_armor;
				int enchant_level_tmp;
				if (safe_enchant == 0) { // ��, ���ũ�̽����� ����
					enchant_level_tmp = 2;
				} else {
					enchant_level_tmp = 1;
				}
				if (armortype >= 8 && armortype <= 12) {
					int chance = 0;
					try {
						chance = AccessoryEnchantList.getInstance().getAccessoryEnchant(l1iteminstance1.getItemId());
					} catch (Exception e) {
						System.out.println("AccessoryEnchantList chance Error");
					}
					if (enchant_level >= 9) {
						enchant_chance_armor = (70 + enchant_level_tmp * Config.ENCHANT_CHANCE_ACCESSORY) / (enchant_level_tmp * (enchant_level - 1)) + chance;
					} else if (enchant_level >= 5) {
						enchant_chance_armor = (80 + enchant_level_tmp * Config.ENCHANT_CHANCE_ACCESSORY) / (enchant_level_tmp * 5) + chance;
					} else {
						enchant_chance_armor = (90 + enchant_level_tmp * Config.ENCHANT_CHANCE_ACCESSORY) / enchant_level_tmp;
					}
				} else {
					int chance = 0;
					try {
						chance = ArmorEnchantList.getInstance().getArmorEnchant(l1iteminstance1.getItemId());
					} catch (Exception e) {
						System.out.println("WeaponEnchantList chance Error");
					}

					if (enchant_level >= 6) {
						if (l1iteminstance1.getMr() > 0) {
							enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2) / (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + Config.ENCHANT_CHANCE_ARMOR + chance;
						} else {
							enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2) / (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + Config.ENCHANT_CHANCE_ARMOR + chance;
						}
					} else {
						if (l1iteminstance1.getItem().get_safeenchant() == 0) {
							if (l1iteminstance1.getMr() > 0) {
								enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2) / (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + Config.ENCHANT_CHANCE_ARMOR + chance;
							} else {
								enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2) / (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + Config.ENCHANT_CHANCE_ARMOR + chance;
							}
						} else {
							if (l1iteminstance1.getMr() > 0) {
								enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2) / (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + Config.ENCHANT_CHANCE_ARMOR;
							} else {
								enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2) / (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + Config.ENCHANT_CHANCE_ARMOR;
							}
						}
					}

					/** 2011.03.28 SOCOOL ����ΰ�� */
					if (pc.isGm()) {
						pc.sendPackets(new S_SystemMessage("\\fYȮ�� : [ " + enchant_chance_armor + " ]"));
						pc.sendPackets(new S_SystemMessage("\\fY�߰� : [ " + chance + " ]"));
						pc.sendPackets(new S_SystemMessage("\\fY���� : [ " + rnd + " ]"));
					}
				}
				if (rnd < enchant_chance_armor) {
					int randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);
					SuccessEnchant(pc, l1iteminstance1, randomEnchantLevel);
				} else if (enchant_level >= 9 && rnd < (enchant_chance_armor * 2)) {
					String item_name_id = l1iteminstance1.getName();
					String pm = "";
					String msg = "";
					if (enchant_level > 0) {
						pm = "+";
					}
					msg = (new StringBuilder()).append(pm + enchant_level).append(" ").append(item_name_id).toString();
					// \f1%0��%2�� �����ϰ�%1 �������ϴٸ�, ������ �����ϰ� ��ҽ��ϴ�.
					pc.sendPackets(new S_ServerMessage(160, msg, "$252", "$248"));
				} else {
					FailureEnchant(pc, l1iteminstance1);
				}
			}
		}
	}
}
