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

package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.CodeLogger;
import server.LineageClient;
import server.manager.eva;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_ChatWhisper extends ClientBasePacket {

	private static final String C_CHAT_WHISPER = "[C] C_ChatWhisper";

	public C_ChatWhisper(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		String targetName = readS();
		String text = readS();
		L1PcInstance whisperFrom = client.getActiveChar();

		if (L1World.getInstance().getNpcShop(targetName)) {
			whisperFrom.sendPackets(new S_SystemMessage("-> (" + targetName
					+ ") " + text));
			return;
		}

		if (text.length() > 25) {
			whisperFrom.sendPackets(new S_SystemMessage("�Ӹ��� ���� �� �ִ� ���ڼ��� �ʰ��Ͽ����ϴ�."));
			return;
		}
		// ä�� �������� ���
		if (whisperFrom.getSkillEffectTimerSet().hasSkillEffect(
				L1SkillId.STATUS_CHAT_PROHIBITED)) {
			whisperFrom.sendPackets(new S_ServerMessage(242)); // ���� ä�� �������Դϴ�.
			return;
		}
		if (whisperFrom.getLevel() < Config.WHISPER_CHAT_LEVEL) {
			whisperFrom.sendPackets(new S_ServerMessage(404, String
					.valueOf(Config.WHISPER_CHAT_LEVEL)));
			return;
		}

		L1PcInstance whisperTo = L1World.getInstance().getPlayer(targetName);

		if (whisperFrom.getAccessLevel() == 0) { // �Ӹ��� �ڵ� ���
			if (whisperTo.getName().equalsIgnoreCase("��Ƽ��") || whisperTo.getName().equalsIgnoreCase("�̼��Ǿ�")) {
				whisperTo.sendPackets(new S_ChatPacket(whisperFrom, text, Opcodes.S_OPCODE_WHISPERCHAT, 16));
				whisperFrom.sendPackets(new S_ChatPacket(whisperTo,"������ �����ֽø� ���� �� ��ȯ�ϰڽ��ϴ�.", Opcodes.S_OPCODE_MSG, 9));
				CodeLogger.getInstance().chatlog("�Ӹ�",whisperFrom.getName() + "-->" + whisperTo.getName() + "	" + text);
				eva.LogChatWisperAppend("[�Ӹ�]", whisperFrom.getName(), "******", text, ">");
				return;
			}
		}

		// ���忡 ���� ���
		if (whisperTo == null) {
			whisperFrom.sendPackets(new S_ServerMessage(73, targetName)); // \f1%0��
			// ������
			// �ϰ�
			// ����
			// �ʽ��ϴ�.
			return;
		}
		if (whisperTo.getSkillEffectTimerSet().hasSkillEffect(
				L1SkillId.STATUS_CHAT_PROHIBITED)) {
			whisperFrom.sendPackets(new S_SystemMessage("ä�ñ������� PC���Դ� �Ӹ��� �Ҽ� �����ϴ�."));
			return;
		}
		// �ڱ� �ڽſ� ���� wis�� ���
		if (whisperTo.equals(whisperFrom)) {
			return;
		}

		// ���ܵǰ� �ִ� ���
	if(whisperTo != null){
		if (whisperTo.getExcludingList().contains(whisperFrom.getName())) {
			whisperFrom.sendPackets(new S_ServerMessage(117, whisperTo
					.getName())); // %0��
			// �����
			// �����߽��ϴ�.
			return;
		}
	}
		if (!whisperTo.isCanWhisper()) {
			whisperFrom.sendPackets(new S_ServerMessage(205, whisperTo
					.getName()));
			return;
		}

		whisperFrom.sendPackets(new S_ChatPacket(whisperTo, text,
				Opcodes.S_OPCODE_MSG, 9));
		whisperTo.sendPackets(new S_ChatPacket(whisperFrom, text,
				Opcodes.S_OPCODE_WHISPERCHAT, 16));
	//}
	CodeLogger.getInstance().chatlog(
			"�Ӹ�",
			whisperFrom.getName() + "-->" + whisperTo.getName() + "	"
					+ text);
	eva.LogChatWisperAppend("[�Ӹ�]", whisperFrom.getName(), whisperTo.getName(), text, ">");
	}
	@Override
	public String getType() {
		return C_CHAT_WHISPER;
	}
}
