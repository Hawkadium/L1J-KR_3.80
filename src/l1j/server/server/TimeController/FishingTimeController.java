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
package l1j.server.server.TimeController;

import javolution.util.FastTable;

import java.util.List;
import java.util.Random;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ServerMessage;

public class FishingTimeController implements Runnable {
	private static FishingTimeController _instance;

	private final List<L1PcInstance> _fishingList = new FastTable<L1PcInstance>();

	private static Random _random = new Random(System.nanoTime());

	public static FishingTimeController getInstance() {
		if (_instance == null) {
			_instance = new FishingTimeController();
		}
		return _instance;
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(2000);
				fishing();
			}
		} catch (Exception e1) {
		}
	}

	public void addMember(L1PcInstance pc) {
		if (pc == null || _fishingList.contains(pc)) {
			return;
		}
			_fishingList.add(pc);

		}

	public void removeMember(L1PcInstance pc) {
		if (pc == null || !_fishingList.contains(pc)) {
			return;
		}
		_fishingList.remove(pc);
	}

	private void fishing() {
		if (_fishingList.size() > 0) {
			long currentTime = System.currentTimeMillis();
			L1PcInstance pc = null;
			for (int i = 0; i < _fishingList.size(); i++) {
				pc = _fishingList.get(i);
				if (pc == null)
					continue;
				if (pc.isFishing()) {
					long time = pc.getFishingTime();
					if (currentTime > (time + 1000)) {
						/** �̳��� ������� * */
						if (pc.getInventory().consumeItem(41295, 1)) {
							long time2 = System.currentTimeMillis() + 10000
									+ _random.nextInt(3) * 1000;
							pc.setFishingTime(time2);
							SuccessCK(pc);
						} else {
							pc.setFishingTime(0);
							pc.setFishingReady(false);
							pc.setFishing(false);
							pc.sendPackets(new S_CharVisualUpdate(pc));
							Broadcaster.broadcastPacket(pc,
									new S_CharVisualUpdate(pc));
							pc.sendPackets(new S_ServerMessage(1163, "")); // ���ð� �����߽��ϴ�.
							removeMember(pc);
						}
					}
				}
			}
		}
	}
	private void SuccessCK(L1PcInstance pc) {
		int chance = _random.nextInt(200) + 1;

		if (chance < 50) {
			successFishing(pc, 41298, "$5256"); // 25%� �����

		} else if (chance < 65) {
			successFishing(pc, 41300, "$5258"); // 7.5% ���� �����

		} else if (chance < 80) {
			successFishing(pc, 41299, "$5257"); // 7.5%����� �����

		} else if (chance < 90) {
			successFishing(pc, 41296, "$5249"); // 5%�ؾ�

		} else if (chance < 100) {
			successFishing(pc, 41297, "$5250"); // 5%�׾�

		} else if (chance < 105) {
			successFishing(pc, 41301, "$5259"); // 2.5%���� �� ���� �����

		} else if (chance < 110) {
			successFishing(pc, 41302, "$5260"); // 2.5%�ʷ� �� ���� �����

		} else if (chance < 130) {
			successFishing(pc, 40066, "����"); // 2.5%�Ķ� �� ���� �����

		} else if (chance < 140) {
			successFishing(pc, 435014, "���ٱ���"); // 2.5%�� �� ���� �����

		} else if (chance < 155) {
			successFishing(pc, 437029, "���� �����"); // ���ڹ����

/*		} else if (chance < 193) {
			successFishing(pc, 41252, " $5248"); // ������ �ź���
*/
		} else if (chance < 194) {
			successFishing(pc, 560018, "��¦�̴� ���"); // ��¦�̴º��
		} else {
			pc.sendPackets(new S_ServerMessage(1136, "")); // ���ÿ� �����߽��ϴ�.
		}
	}

	private void successFishing(L1PcInstance pc, int itemid, String message) {
		if(pc.getInventory().getSize() > (180 - 16)) {
			pc.sendPackets(new S_ServerMessage(263));
			return;
		}
		L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
		if (item != null)
			pc.sendPackets(new S_ServerMessage(1185, message)); // ���ÿ� ������%0%o�� �����߽��ϴ�.
	}

}