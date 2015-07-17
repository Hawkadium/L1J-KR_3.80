package l1j.server.server.serverpackets;

import java.util.logging.Logger;

import l1j.server.server.Opcodes;

public class S_GMLava extends ServerBasePacket {

	private static final String S_GMLava = "[C] S_GMLava";

	private static Logger _log = Logger.getLogger(S_GMLava.class.getName());

	private byte[] _byte = null;

	public S_GMLava(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_OPCODE_BOARDREAD);
		writeD(number);//�ѹ�
		writeS("�̼��Ǿ�");//�۾���?
		writeS("��� �̵� ��ɾ�");//��¥?
		writeS("");//����?
		writeS("[��Ÿ�ٵ� ����]\n" +
				"��1 �����Ʒ��� 19�����뺴��\n" +
				"��2 ���������� 20���ϰ�����\n" +
				"��3 �߼������� 21����ó����\n" +
				"��4 �߼��Ʒý� 22����������\n" +
				"��5 ������ȯ�� 23�ϻ�������\n" +
				"��6 �渶���Ʒ� 24 ���������\n" +
				"��7 �渶������ 25 �����Ʒ���\n" +
				"��8 ���������� 26 ���Ա���\n" +
				"��9 �����Ǽ��� 27 ���ϰ�����\n" +
				"��10 ���ɼ�ȯ�� 28 ���̳��ǹ�\n" +
				"��11 ���ɻ����� 29 ����ƽ���\n" +
				"��12 ���ɿ����� 30 ���Ÿ����\n" +
				"��13 �Ƿ������� 31 �ٷθ޽���\n" +
				"��14 ���������� 32 �̵����ǹ�\n" +
				"��15 �뺴�Ʒ��� 33 Ƽ�Ƹ޽���\n" +
				"��16 ����Ʒ��� 34 ��̾ƽ���\n" +
				"��17 ��������� 35 �ٷε��ǹ�\n" +
				"��18 �߾������� 36 ������ǹ�\n");
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GMLava;
	}
}