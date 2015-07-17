package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SendInvOnLogin

public class S_Frame extends ServerBasePacket {

	private static final String S_Frame = "[S] S_Frame";

	/**
	 * �������� ����
	 */
	// ����2 + 0x66 + Ű������Ʈ + ��Ÿ + ��Ǫ + ���� + �߶�
	public S_Frame(int key,int anta,int papoo,int lind,int bala) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(0x66);
		writeD(key);
		writeC(anta);
		writeC(papoo);
		writeC(lind);
		writeC(bala);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_Frame;
	}
}
