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
import l1j.server.server.serverpackets.S_GMLava;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1GMLava implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(S_GMLava.class.getName());

	private L1GMLava() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1GMLava();
	}


	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int i = 0;
			try {
				i = Integer.parseInt(arg);
			} catch (NumberFormatException e) {
			}

			if (i == 1) {
				L1Teleport.teleport(pc, 32811, 32836, (short) 452, 5, false); // ���ݴ� �Ʒ���
			} else if (i == 2) {
				L1Teleport.teleport(pc , 32749, 32736, (short) 453, 5, false); //����������������
			} else if (i == 3) {
				L1Teleport.teleport(pc , 32748, 32855, (short) 454, 5, false); // �߼� ������
			} else if (i == 4) {
				L1Teleport.teleport(pc , 32801, 32862, (short) 455, 5, false); // �߼� �Ʒý�
			} else if (i == 5) {
				L1Teleport.teleport(pc , 32792, 32795, (short) 456, 5, false); // ������ȯ��
			} else if (i == 6) {
				L1Teleport.teleport(pc , 32814, 32819, (short) 460, 5, false); // �渶�� �Ʒ���
			} else if (i == 7) {
				L1Teleport.teleport(pc , 32782, 32812, (short) 461, 5, false); // �渶�� ������
			} else if (i == 8) {
				L1Teleport.teleport(pc , 32813, 32865, (short) 462, 5, false); // ���ɱ����� ������
			} else if (i == 9) {
				L1Teleport.teleport(pc , 32734, 32854, (short) 463, 5, false); // ���ɱ����� ����
			} else if (i == 10) {
				L1Teleport.teleport(pc , 32806, 32829, (short) 464, 5, false); // ���ɼ�ȯ��			
			} else if (i == 11) {
				L1Teleport.teleport(pc , 32811, 32808, (short) 465, 5, false); // �����ǻ�����
			} else if (i == 12) {
				L1Teleport.teleport(pc , 32792, 32835, (short) 466, 5, false); // ���� ���� ������			
			} else if (i == 13) {
				L1Teleport.teleport(pc , 32751, 32839, (short) 470, 5, false); // �Ƿ�������
			} else if (i == 14) {
				L1Teleport.teleport(pc , 32766, 32851, (short) 471, 5, false); // ���� �ε��� ����		
			} else if (i == 15) {
				L1Teleport.teleport(pc , 32748, 32806, (short) 472, 5, false); // �뺴 �Ʒ���
			} else if (i == 16) {
				L1Teleport.teleport(pc , 32885, 32816, (short) 473, 5, false); // ��������� �Ʒ���
			} else if (i == 17) {
				L1Teleport.teleport(pc , 32705, 32849, (short) 475, 5, false); // ��������� ������
			} else if (i == 18) {
				L1Teleport.teleport(pc , 32805, 32800, (short) 476, 5, false); // �߾� ������
			} else if (i == 19) {
				L1Teleport.teleport(pc , 32803, 32857, (short) 477, 5, false); // ���� �ε��� �뺴��
			} else if (i == 20) {
				L1Teleport.teleport(pc , 32814, 32818, (short) 495, 5, false); // ���� ������
			} else if (i == 21) {
				L1Teleport.teleport(pc , 32856, 32758, (short) 494, 5, false); // ���� ó����
			} else if (i == 22) {
				L1Teleport.teleport(pc , 32746, 32723, (short) 493, 5, false); // ���� ������
			} else if (i == 23) {
				L1Teleport.teleport(pc , 32843, 32847, (short) 492, 5, false); // �ϻ챺����������
			} else if (i == 24) {
				L1Teleport.teleport(pc , 32711, 32860, (short) 491, 5, false); // ���� ���
			} else if (i == 25) {
				L1Teleport.teleport(pc , 32731, 32810, (short) 490, 5, false); // ���� �Ʒ���
			} else if (i == 26) {
				L1Teleport.teleport(pc , 32750, 32812, (short) 478, 5, false); // ���� ����������
			} else if (i == 27) {
				L1Teleport.teleport(pc , 32773, 32785, (short) 495, 5, false); // ���� ������
			} else if (i == 28) {
				L1Teleport.teleport(pc , 32877, 32817, (short) 530, 5, false); // ���̳��ǹ�
			} else if (i == 29) {
				L1Teleport.teleport(pc , 32851, 32872, (short) 531, 5, false); // ����ƽ�
			} else if (i == 30) {
				L1Teleport.teleport(pc , 32789, 32739, (short) 531, 5, false); // ���Ÿ��
			} else if (i == 31) {
				L1Teleport.teleport(pc , 32773, 32815, (short) 531, 5, false); // �ٷθ޽�
			} else if (i == 32) {
				L1Teleport.teleport(pc , 32777, 32791, (short) 532, 5, false); // �̵���
			} else if (i == 33) {
				L1Teleport.teleport(pc , 32824, 32885, (short) 533, 5, false); // Ƽ�Ƹ޽�
			} else if (i == 34) {
				L1Teleport.teleport(pc , 32765, 32856, (short) 533, 5, false); // ��̾ƽ�
			} else if (i == 35) {
				L1Teleport.teleport(pc , 32727, 32797, (short) 533, 5, false); // �ٷε�
			} else if (i == 36) {
				L1Teleport.teleport(pc , 32879, 32842, (short) 534, 5, false); // ī����				
				
			} else {
				L1Location loc = GMCommandsConfig.ROOMS.get(arg.toLowerCase());
				if (loc == null) {
					pc.sendPackets(new S_GMLava(1));
                    return;
				}
				L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, false);
			}
			
			if(i > 0 && i < 36) {
				pc.sendPackets(new S_SystemMessage("��� ���("+ i + ")������ �̵��߽��ϴ�."));
			}
		} catch (Exception exception) {
			pc.sendPackets(new S_SystemMessage(
					".��� [��Ҹ�]�� �Է� ���ּ���.(��Ҹ��� GMCommands.xml�� ����)"));
		}
	}
}
