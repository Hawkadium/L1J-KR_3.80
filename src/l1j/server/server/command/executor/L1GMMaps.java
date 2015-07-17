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

import java.util.logging.Logger;

import l1j.server.server.GMCommandsConfig;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_GMMaps;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1GMMaps implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(S_GMMaps.class.getName());

	private L1GMMaps() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1GMMaps();
	}

	
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int i = 0;
			try {
				i = Integer.parseInt(arg);
			} catch (NumberFormatException e) {
			}

			if (i == 1) {
				L1Teleport.teleport(pc, 32800, 32799, (short) 110, 5, false); // [����] 10�� ���Ͻ���
			} else if (i == 2) {
				L1Teleport.teleport(pc , 32855, 32830, (short) 120, 5, false); //[����] 20�� ��    ��
			} else if (i == 3) {
				L1Teleport.teleport(pc , 32799, 32799, (short) 130, 5, false); // [����] 30�� �����̾�
			} else if (i == 4) {
				L1Teleport.teleport(pc , 32800, 32797, (short) 140, 5, false); // [����] 40�� ����ε�
			} else if (i == 5) {
				L1Teleport.teleport(pc , 32794, 32800, (short) 150, 5, false); // [����] 50�� ��    ��
			} else if (i == 6) {
				L1Teleport.teleport(pc , 32720, 32824, (short) 160, 5, false); // [����] 60�� �ӹ̷ε�
			} else if (i == 7) {
				L1Teleport.teleport(pc , 32720, 32824, (short) 170, 5, false); // [����] 70�� ���̸���
			} else if (i == 8) {
				L1Teleport.teleport(pc , 32724, 32822, (short) 180, 5, false); // [����] 80�� ����Ʈ�ߵ�
			} else if (i == 9) {
				L1Teleport.teleport(pc , 32716, 32817, (short) 190, 5, false); // [����] 90�� ��    ġ
			} else if (i == 10) {
				L1Teleport.teleport(pc , 32731, 32856, (short) 200, 5, false); // [����]100�� �׸�����
				/** ���ž**/
			} else if (i == 11) {
				L1Teleport.teleport(pc , 32900, 32760, (short) 78, 5, false); // ���ž 4��
			} else if (i == 12) {
				L1Teleport.teleport(pc , 32801, 32867, (short) 81, 5, false); // ���ž 7��
				/** ��Ÿ�ٵ�**/
			} else if (i == 13) {
				L1Teleport.teleport(pc , 32665, 32852, (short) 457, 5, false); // 1����Ұ��
			} else if (i == 14) {
				L1Teleport.teleport(pc , 32663, 32850, (short) 467, 5, false); // 2��������
				/** 4���**/
			} else if (i == 15) {
				L1Teleport.teleport(pc , 32725, 32800, (short) 67, 5, false); // 15 �߶�ī��
			} else if (i == 16) {
				L1Teleport.teleport(pc , 32691, 32820, (short) 37, 5, false); // 16 ��Ÿ��
			} else if (i == 17) {
				L1Teleport.teleport(pc , 32771, 32831, (short) 65, 5, false); // 17 ��Ǫ����
			} else if (i == 18) {
				L1Teleport.teleport(pc , 34041, 33007, (short) 4, 5, false); // 18 ��������
				
			} else {
				L1Location loc = GMCommandsConfig.ROOMS.get(arg.toLowerCase());
				if (loc == null) {
					pc.sendPackets(new S_GMMaps(1));
                    return;
				}
				L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, false);
			}
			
			if(i > 0 && i < 18) {
				pc.sendPackets(new S_SystemMessage("��� �����("+ i + ")������ �̵��߽��ϴ�."));
			}
		} catch (Exception exception) {
			pc.sendPackets(new S_SystemMessage(
					".����� [��Ҹ�]�� �Է� ���ּ���.(��Ҹ��� GMCommands.xml�� ����)"));
		}
	}
}
