
package l1j.server.server.serverpackets;

import java.util.logging.Logger;

import l1j.server.server.Opcodes;

public class S_GMMaps extends ServerBasePacket {

	private static final String S_GMMaps = "[C] S_GMMaps";

	private static Logger _log = Logger.getLogger(S_GMMaps.class.getName());

	private byte[] _byte = null;

	public S_GMMaps(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_OPCODE_BOARDREAD);
		writeD(number);//�ѹ�
		writeS("�̼��Ǿ�");//�۾���?
		writeS("����� �̵� ��ɾ�");//��¥?
		writeS("");//����?
		writeS("[������ž  �̵�]\n" +
				"��1 ����10���� �� 2 ����20��\n" +
				"��3 ����30���� �� 4 ����40��\n" +
				"��5 ����50���� �� 6 ����60��\n" +
				"��7 ����70���� �� 8 ����80��\n" +
				"��9 ����90����   10 ����100��\n" +
				"\n" +
				"[���ž ����]\n" +
				"��11 ���ž4��   12 ���ž7��\n" +
				"\n" +
				"[��Ÿ�ٵ� ����]\n" +
				"��13 ��Ұ��     14 ������\n" +
				"\n" +
		        "[4�� �븮��Ʈ]\n" +
				"��15 �߶�ī���� 16 ��Ÿ��\n" +
		        "��17 ��Ǫ���¡� 18 ��������");
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GMMaps;
	}
}

