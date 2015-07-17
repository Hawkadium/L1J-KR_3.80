package l1j.server.server.model;

import javolution.util.FastTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class Broadcaster {
	/**
	 * ĳ������ ���� ������ �ִ� �÷��̾, ��Ŷ�� �۽��Ѵ�.
	 * 
	 * @param packet
	 *            �۽��ϴ� ��Ŷ�� ��Ÿ���� ServerBasePacket ������Ʈ.
	 */
	public static void broadcastPacket(L1Character cha, ServerBasePacket packet) {
		FastTable<L1PcInstance> list = null;
		list = L1World.getInstance().getVisiblePlayer(cha);
		for (L1PcInstance pc : list) {
			pc.sendPackets(packet);
		}
	}

	/**
	 * ĳ������ ���� ������ �ִ� �÷��̾, ��Ŷ�� �۽��Ѵ�. �ٸ� Ÿ���� ȭ�鳻���� �۽����� �ʴ´�.
	 * 
	 * @param packet
	 *            �۽��ϴ� ��Ŷ�� ��Ÿ���� ServerBasePacket ������Ʈ.
	 */
	public static void broadcastPacketExceptTargetSight(L1Character cha,
			ServerBasePacket packet, L1Character target) {
		FastTable<L1PcInstance> list = null;
		list = L1World.getInstance().getVisiblePlayerExceptTargetSight(cha,
				target);
		for (L1PcInstance pc : list) {
			pc.sendPackets(packet);
		}
	}

	/**
	 * ĳ������ 50 �Ž� �̳��� �ִ� �÷��̾, ��Ŷ�� �۽��Ѵ�.
	 * 
	 * @param packet
	 *            �۽��ϴ� ��Ŷ�� ��Ÿ���� ServerBasePacket ������Ʈ.
	 */
	public static void wideBroadcastPacket(L1Character cha,
			ServerBasePacket packet) {
		FastTable<L1PcInstance> list = null;
		list = L1World.getInstance().getVisiblePlayer(cha, 50);
		for (L1PcInstance pc : list) {
			pc.sendPackets(packet);
		}
	}
}
