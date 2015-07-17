package l1j.server.server.TimeController;

import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class WitsTimeController {
	public static ConcurrentHashMap<String, L1PcInstance> _witsWinPlayers;
	public static boolean witsGameStarted = false; 
	public static int witsGameCount = 0;
	private static final Timer _timer = new Timer();
	private static WitsTimeController _instance;
	private checkChatTime _ChatRegen;
	public boolean isWits = false;	

	public static synchronized WitsTimeController getInstance() {
		if (_instance == null) {
			_instance = new WitsTimeController();
			_witsWinPlayers = new ConcurrentHashMap<String, L1PcInstance>();
		}
		return _instance;
	}
	
	private Collection<L1PcInstance> _witsWinPlayersValues;
	
	public Collection<L1PcInstance> getWitsWinPlayers() {
		Collection<L1PcInstance> vs = _witsWinPlayersValues;
		return (vs != null) ? vs : (_witsWinPlayersValues = Collections.unmodifiableCollection(_witsWinPlayers.values()));
	}

	public void startcheckChatTime(int count) {
		if (!isWits) {
			witsGameCount = count;
			_ChatRegen = new checkChatTime();
			// ���� �޼ҵ� �� 3���� ���� ���� ���� �ð��� 1000* 60 1�� * 3
			_timer.scheduleAtFixedRate(_ChatRegen, 3000, 1000 * 60 * 3);
			isWits = true;
		}

	}

	public void stopcheckChatTime() {
		if (isWits) {
			witsGameStarted = false;
			_witsWinPlayers.clear();
			_ChatRegen.cancel();
			isWits = false;

		}
	}
	
	public static int witsCount = 0; 

	private class checkChatTime extends TimerTask {

		// @Override
		public void run() {// ���Ⱑ ���۵�
			try {
				if (witsGameCount == 0) {
					stopcheckChatTime();
					return;
				}

				Thread.sleep(1000);
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[����] ����� ��ġ ������ ����˴ϴ�."));
				Thread.sleep(1000);
				L1World.getInstance().set_worldChatElabled(false);
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[����] �����ϰ� �ߺ����� ���ڸ� ġ�� �е��� ���� ����."));
				Thread.sleep(5000);
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[����] �� �����Ͻð� 3�� ī��Ʈ�մϴ�."));
				Thread.sleep(1000);
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[����] 3"));
				Thread.sleep(1000);
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[����] 2"));
				Thread.sleep(1000);
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[����] 1"));
				Thread.sleep(1000);
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[����] ��ġ���� ����~"));
				Thread.sleep(500);
				L1World.getInstance().set_worldChatElabled(true);
				// ������ �˸��� ���� ����
				witsGameStarted = true;				
				witsGameCount--;
				
			} catch (Exception e) {
				witsGameStarted = false;// ���ܸ� ������ ����
			}

		}

	}
}