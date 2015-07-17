package l1j.server.server.model;

import javolution.util.FastTable;
import javolution.util.FastMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_DropItem;

public class L1GroundInventory extends L1Inventory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Timer _timer = new Timer();

	private Map<Integer, DeletionTimer> _reservedTimers = new FastMap<Integer, DeletionTimer>();

	private class DeletionTimer extends TimerTask {
		private final L1ItemInstance _item;

		public DeletionTimer(L1ItemInstance item) {
			_item = item;
		}

		@Override
		public void run() {
			try {
				synchronized (L1GroundInventory.this) {
					if (!_items.contains(_item)) {// �ֿ��� Ÿ�ֿ̹� ���󼭴� �� ������ ä�� ��
						// �ִ�
						return; // �̹� �ֿ����� �ִ�
					}
					removeItem(_item);
				}
			} catch (Throwable t) {
				_log.log(Level.SEVERE, "L1GroundInventory[]Error", t);
			}
		}
	}

	private void setTimer(L1ItemInstance item) {
		if (!Config.ALT_ITEM_DELETION_TYPE.equalsIgnoreCase("std")) {
			return;
		}
		if (item.getItemId() == 40515) { // ������ ��
			return;
		}
		_timer.schedule(new DeletionTimer(item), 180 * 1000);
	}

	private void cancelTimer(L1ItemInstance item) {
		DeletionTimer timer = _reservedTimers.get(item.getId());
		if (timer == null) {
			return;
		}
		timer.cancel();
	}

	public L1GroundInventory(int objectId, int x, int y, short map) {
		setId(objectId);
		setX(x);
		setY(y);
		setMap(map);
		L1World.getInstance().addVisibleObject(this);
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		List<L1ItemInstance> list = getItems();
		for (L1ItemInstance item : list) {
			if (!perceivedFrom.getNearObjects().knownsObject(item)) {
				perceivedFrom.getNearObjects().addKnownObject(item);
				perceivedFrom.sendPackets(new S_DropItem(item)); // �÷��̾
				// DROPITEM
				// ������ ����
			}
		}
	}

	// �ν� �������� �ִ� �÷��̾ ������Ʈ �۽�
	@Override
	public void insertItem(L1ItemInstance item) {
		setTimer(item);
		FastTable<L1PcInstance> list = L1World.getInstance()
				.getRecognizePlayer(item);
		for (L1PcInstance pc : list) {
			pc.sendPackets(new S_DropItem(item));
			pc.getNearObjects().addKnownObject(item);
		}
	}

	// ���̴� �������� �ִ� �÷��̾��� ������Ʈ ����
	@Override
	public void updateItem(L1ItemInstance item) {
		FastTable<L1PcInstance> list = L1World.getInstance()
				.getRecognizePlayer(item);
		for (L1PcInstance pc : list) {
			pc.sendPackets(new S_DropItem(item));
		}
	}

	// �ϴ� ��� �ı� �� ���̴� �������� �ִ� �÷��̾��� ������Ʈ ����
	@Override
	public void deleteItem(L1ItemInstance item) {
		cancelTimer(item);
		FastTable<L1PcInstance> list = L1World.getInstance()
				.getRecognizePlayer(item);
		for (L1PcInstance pc : list) {
			pc.sendPackets(new S_RemoveObject(item));
			pc.getNearObjects().removeKnownObject(item);
		}

		_items.remove(item);
		if (_items.size() == 0) {
			L1World.getInstance().removeVisibleObject(this);
		}
	}

	private static Logger _log = Logger
			.getLogger(L1PcInventory.class.getName());
}
