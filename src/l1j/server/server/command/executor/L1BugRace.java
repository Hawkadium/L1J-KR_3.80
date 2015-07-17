/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.command.executor;

import java.util.StringTokenizer;
import java.util.logging.Logger;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.Instance.L1LittleBugInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1BugRace implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1CheckCharacter.class.getName());

	private L1BugRace() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1BugRace();
	}

	
	public void execute(L1PcInstance pc, String cmdName, String arg) {

		try {
			StringTokenizer st = new StringTokenizer(arg);

			int select = Integer.parseInt(st.nextToken());
			int speed = Integer.parseInt(st.nextToken());
			int searchCount = 0;

			try {

				pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
				for (L1LittleBugInstance littlebug : L1BugBearRace.getInstance()._littleBug) {
					if (searchCount == select) {
						pc.sendPackets(new S_SystemMessage("* ��Ī : " + littlebug.getNameId()));
						littlebug.setPassispeed(littlebug.getPassispeed() + speed);
						littlebug.setCondition(0);
						pc.sendPackets(new S_SystemMessage("* ���ǵ尡 " + littlebug.getPassispeed() + "�� ���� �Ǿ����ϴ�."));
					}

					searchCount++;
				}
				pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
			} catch (Exception e) {
				pc.sendPackets(new S_SystemMessage("\\fW** ���׺��� �˻� ���� **"));
			}

		} catch (Exception e) {
			//_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc.sendPackets(new S_SystemMessage(".����  [����0~4] [-(����)������/+(����)������]"));
			int searchCount = 0;
			String condition = "";
			for (L1LittleBugInstance littlebug : L1BugBearRace.getInstance()._littleBug) {

				switch (littlebug.getCondition()) {
				case 0:
					condition = "����";
					break;
				case 1:
					condition = "����";
					break;
				case 2:
					condition = "����";
					break;
				default:
				}

				pc.sendPackets(new S_SystemMessage(searchCount + ". " + condition + ", " + littlebug.getPassispeed() + ", " + littlebug.getNameId()));

				searchCount++;
			}

		} finally {

		}
	}
}
