package server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LineageDecoderThread implements Runnable {

	public LineageDecoderThread() {
		_client = new CopyOnWriteArrayList<LineageClient>();
	}

	private List<LineageClient> _client;

	public void run() {
		while (true) {
			try {
				for (LineageClient client : _client) {
					if (client != null) {
						// ���� �����Ȱ� ����.
						// when disconnect happens
						if (!client.isConnected()) {
							client.close();
							removeClient(client);
						}
						// ���ڴ�
						// decoder
						if (!client.DecodingCK) {
							client.DecodingCK = true;
							// synchronized(client.PacketD){
							int length = PacketSize(client.PacketD);
							if (length != 0 && length <= client.PacketIdx) {
								byte[] temp = new byte[length];
								System.arraycopy(client.PacketD, 0, temp, 0,
										length);
								System.arraycopy(client.PacketD, length,
										client.PacketD, 0, client.PacketIdx
												- length);
								client.PacketIdx -= length;
								client.encryptD(temp);
							}
							client.DecodingCK = false;
						}
					} else {
						removeClient(client);
					}
				}
				Thread.sleep(20);
			} catch (Exception e) {
				// Logger.getInstance().error(getClass().toString()+"
				// run()\r\n"+e.toString(), Config.LOG.error);
			}
		}
	}

	// ��Ŷũ�� �� ����.
	// return packet size value
	private int PacketSize(byte[] data) {
		int length = data[0] & 0xff;
		length |= data[1] << 8 & 0xff00;
		return length;
	}

	// Ŭ�� ���
	// client registration
	public void putClient(LineageClient c) {
		try {
			if (!_client.contains(c)) {
				_client.add(c);
			}
		} catch (Exception e) {
			// Logger.getInstance().error(getClass().toString()+"
			// putClient(LineageClient c)\r\n"+e.toString(), Config.LOG.error);
		}
	}

	// Ŭ�� ã��
	// serach client
	public LineageClient getClient(String id) {
		if (id != null) {
			try {
				for (LineageClient c : _client) {
					if (c != null) {
						if (c.getID() != null && c.getID().equalsIgnoreCase(id)) {
							return c;
						}
					} else {
						removeClient(c);
					}
				}
			} catch (Exception e) {
				// Logger.getInstance().error(getClass().toString()+"
				// getClient(String id)\r\n"+e.toString(), Config.LOG.error);
			}
		}
		return null;
	}

	// Ŭ�� ����
	// client deleted
	public void removeClient(LineageClient c) {
		if (_client.contains(c))
			_client.remove(c);
	}

	// Ŭ�� ����
	// client number
	public int ClientCount() {
		return _client.size();
	}

	// Ŭ�� ��ϵǾ� �ִ��� üũ
	// check client is registered
	public boolean ContainsClient(LineageClient c) {
		return _client.contains(c);
	}

	public List<LineageClient> getAllClient() {
		return this._client;
	}
}
