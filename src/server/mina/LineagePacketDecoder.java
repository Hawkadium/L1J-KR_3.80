package server.mina;

import server.LineageClient;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class LineagePacketDecoder implements ProtocolDecoder {

	public synchronized void decode(IoSession session, IoBuffer buffer,
			ProtocolDecoderOutput out) {
		try {
			// ����ȭ �ؾ���
			// Must synchronize
			LineageClient client = (LineageClient) session
					.getAttribute(LineageClient.CLIENT_KEY);
			// if(client!=null) client.encryptD(buffer);
			if (client != null) {
				int size = buffer.limit();
				if (size > 0 && size < 1024) {
					if(!client.doAutoPacket()){
						byte[] data = buffer.array();
						System.arraycopy(data, 0, client.PacketD, client.PacketIdx,size);
						client.PacketIdx += size;
					}
				} else {
					// ������ ���ٷ� �����ع���.
					// Obama to shut down a deserted size
					client.close();
				}
			}
			client = null;
		} catch (Exception e) {
		}
	}

	public void dispose(IoSession client) throws Exception {

	}

	public void finishDecode(IoSession client, ProtocolDecoderOutput output)
			throws Exception {

	}

	// ��Ŷũ�� �� ����.
	// Returns packet size value
	private int PacketSize(byte[] data) {
		int length = data[0] & 0xff;
		length |= data[1] << 8 & 0xff00;
		return length;
	}
}
