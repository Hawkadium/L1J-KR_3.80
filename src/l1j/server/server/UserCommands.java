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

package l1j.server.server;

import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Serchdrop2;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

// Referenced classes of package l1j.server.server:
// ClientThread, Shutdown, IpTable, MobTable,
// PolyTable, IdFactory

public class UserCommands {
	private static Logger _log = Logger.getLogger(UserCommands.class.getName());

	boolean spawnTF = false;

	private static UserCommands _instance;

	private UserCommands() {
	}

	public static UserCommands getInstance() {
		if (_instance == null) {
			_instance = new UserCommands();
		}
		return _instance;
	}

	public void handleCommands(L1PcInstance pc, String cmdLine) {
		StringTokenizer token = new StringTokenizer(cmdLine);
		String cmd = token.nextToken();
		String param = "";

		while (token.hasMoreTokens()) {
			param = new StringBuilder(param).append(token.nextToken()).append(
					' ').toString();
		}
		param = param.trim();
		try {

			if (cmd.equalsIgnoreCase("����")) {
				showHelp(pc);
			} else if (cmd.equalsIgnoreCase("���ܽ�û")) {
				LockItem1(pc, param);
			/*} else if (cmd.equalsIgnoreCase("����ŷ")) {
				rank(pc);*/
			} else if (cmd.equalsIgnoreCase(".")) {
				telrek(pc);
/*			} else if (cmd.equalsIgnoreCase("��ŷ�˻�")) {
				rank1(pc, param);*/
			} else if (cmd.equalsIgnoreCase("������Ƽ")) {
				BloodParty(pc);
			// } else if (cmd.equalsIgnoreCase("���ͺ�ȣ")) { 
				//  BloodProtect(pc);
			} else if (cmd.equalsIgnoreCase("���͹���")) {
					   clanbuff(pc);
			} else if (cmd.equalsIgnoreCase("����")) {
				buff(pc);
		    } else if (cmd.equalsIgnoreCase("��ġ����")) {
				TargetLoc(pc, param);
			 } else if (cmd.equalsIgnoreCase("�����Ʈ")) {
				bugment(pc, param);
			} else if (cmd.equalsIgnoreCase("����")) {
				Hunt(pc, param);
			} else if (cmd.equalsIgnoreCase("�������")) {
				autoroot(pc, cmd, param);
			} else if (cmd.equalsIgnoreCase("�����Ʈ")) {
				ment(pc, cmd, param);
			} else if (cmd.equalsIgnoreCase("������ġ��")) {
				featherAdd(pc);
			} else if (cmd.equalsIgnoreCase("�Ƶ���ġ��")) {
				adenaAdd(pc);
			} else if (cmd.equalsIgnoreCase("���λ���")) {
				LetsbeShop(pc);
			} else if (cmd.equalsIgnoreCase("���͸�ũ")) {
				MarkView(pc, param);
			} else if (cmd.equalsIgnoreCase("..")) {
				 if (pc.getAccessLevel() != 2) {
						return;
					}
				gjtkd(pc);
			} else if (cmd.equalsIgnoreCase("��ȣ����")) {
				changePassword(pc, param);
			} else if (cmd.equalsIgnoreCase("�����")) {
				quize(pc, param);
			} else if (cmd.equalsIgnoreCase("�������")) {
				quize1(pc, param);
			} else if (cmd.equalsIgnoreCase("����")) {
				age(pc, param);
			} else if (cmd.equalsIgnoreCase("���̸�")) {
				serchdroplist2(pc, param);
			} else if (cmd.equalsIgnoreCase("ĳ������")) {
				changename(pc, param);
			} else if (cmd.equalsIgnoreCase("������ȯ")) {
				CallClan(pc, param);
			} else if (cmd.equalsIgnoreCase("����ð�")) { entertime(pc);
		} else if (cmd.equalsIgnoreCase("������û")) {
			phone(pc, param);
			} else {
				String msg = new StringBuilder().append("Ŀ��壺").append(cmd)
						.append(" �� �������� �ʽ��ϴ� .���� ��ó������!").toString();
				pc.sendPackets(new S_SystemMessage(msg));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "UserCommands[handle]Error", e);
		}
	}

	private void showHelp(L1PcInstance pc) {
		pc.sendPackets(new S_SystemMessage("------------ User Commands ------------"));
		pc.sendPackets(new S_SystemMessage(" .���� .���� .��ȣ���� ..(�ڷ�) .����"));
		pc.sendPackets(new S_SystemMessage(" .���̸� .�Ƶ�(����)��ġ�� .��ġ����"));
		pc.sendPackets(new S_SystemMessage(" .������û  .���λ��� .ĳ������ .�����Ʈ"));
		pc.sendPackets(new S_SystemMessage(" .�����Ʈ .������� .����� .�������"));
		pc.sendPackets(new S_SystemMessage(" .���ܽ�û .����ŷ .��ŷ�˻� .����ð�"));
		pc.sendPackets(new S_SystemMessage(" +��������+: .������ȯ .������Ƽ .���͸�ũ "));
		pc.sendPackets(new S_SystemMessage("------------ Have a Good Time ---------"));
	}
	private void phone(L1PcInstance pc, String param) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 20 > curtime) {
				long sec = (pc.getQuizTime() + 20) - curtime;
				pc.sendPackets(new S_SystemMessage(sec + "�ʰ��� �����ð��� �ʿ��մϴ�."));
				return;
			}
			
			StringTokenizer tok = new StringTokenizer(param);
			String phone = tok.nextToken();
			Account account = Account.load(pc.getAccountName());

			if (phone.length() < 10) {
				pc.sendPackets(new S_SystemMessage("��ȣ�� �߸� �ԷµǾ����ϴ�."));
				pc.sendPackets(new S_SystemMessage(".������û [��ȭ��ȣ]�� �Է����ּ���."));
				return;
			}

			if (isDisitAlpha(phone) == false) {
				pc.sendPackets(new S_SystemMessage("��ȣ�� ������ �ʴ� ���ڰ� ���ԵǾ����ϴ�."));
				return;
			}

			if (account.getphone() != null) {
				pc.sendPackets(new S_SystemMessage("�̹� ������û�� �����Ǿ� �ֽ��ϴ�."));
				pc.sendPackets(new S_SystemMessage("�߸� �Է��ϼ��� ��� �̼��ǾƷ� �����ּ���."));
				return;
			}

			account.setphone(phone);
			Account.updatePhone(account);
			pc.sendPackets(new S_SystemMessage("��ȣ  (" + phone + ") �� �����Ǿ����ϴ�."));
			pc.sendPackets(new S_SystemMessage("���¶� �׻� ���� �帱����!"));
			//pc.getInventory().storeItem(204601, 1);//���������
			pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_PC_BUFF));
			S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"[!] PC����� ������ �����ĺ��� ����˴ϴ�.", Opcodes.S_OPCODE_MSG, 19); 
			pc.sendPackets(s_chatpacket);
			pc.setQuizTime(curtime);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".������û [��ȭ��ȣ(���ڸ�)]�� �Է����ּ���."));
		}
	}
	public void bugment(L1PcInstance pc, String param){
        if (param.equalsIgnoreCase("��")) {
            pc.sendPackets(new S_SystemMessage("[!] ���׺��� ���̽� ���� �޼����� �����մϴ�."));
            pc.isbugment(false);
        }
        else if (param.equalsIgnoreCase("��")){
            pc.sendPackets(new S_SystemMessage("[!] ���׺��� ���̽� ���� �޼����� Ȱ��ȭ�մϴ�."));
            pc.isbugment(true);
        }else {
            pc.sendPackets(new S_SystemMessage("\\fY[�����] .�����Ʈ (��)or(��)"));
            if (!pc.isbugment()) {
                pc.sendPackets(new S_SystemMessage("���� ����޼��� ���� : [OFF]"));
            }else {
                pc.sendPackets(new S_SystemMessage("���� ����޼��� ���� : [ON]"));
            }
        }
        }

	 public void CallClan(L1PcInstance pc, String param) {
	    	try {
				StringTokenizer tok = new StringTokenizer(param);
				String charName = tok.nextToken();
				int castle_id;

				if (!pc.isCrown()) { // ���� �̿�
					pc.sendPackets(new S_ServerMessage(92, pc.getName())); // \f1%0�� �������� ���������� �ƴմϴ�.
					return;
				}
				
				if (pc.getMapId() == 2006 
						|| pc.getMapId() == 88
						|| pc.getMapId() == 89
						|| pc.getMapId() == 90
						|| pc.getMapId() == 5069
						|| pc.getMapId() == 99 
						|| pc.getMapId() == 610 
						|| pc.getMapId() == 666 
						|| pc.getMapId() == 251 || pc.getMapId() == 208 || pc.getMapId() == 37 || pc.getMapId() == 65 || pc.getMapId() == 67 || pc.getMapId() == 784 || pc.getMapId() == 782 || pc.getMapId() == 603 || pc.getMapId() == 244 || pc.getMapId() == 1005 || pc.getMapId() == 1011 || pc.getMapId() == 5153) {
					pc.sendPackets(new S_SystemMessage("��ȯ�� ����� �� ���� ����Դϴ�."));
					return;
				}

				if (!pc.getInventory().checkItem(40308, 1000000)) {
					pc.sendPackets(new S_SystemMessage("\\fY��ȯ�� ����ϱ� ���� �鸸 �Ƶ����� �ʿ��մϴ�."));
					return;
				}

				for (castle_id = 1; castle_id <= 8; castle_id++) {
					if (WarTimeController.getInstance().isNowWar(castle_id) && L1CastleLocation.getCastleIdByArea(pc) == castle_id) {
						pc.sendPackets(new S_SystemMessage("������ �߿��� ��ȯ�� ����� �� �����ϴ�."));
						return;
					}
				}

				L1PcInstance player = L1World.getInstance().getPlayer(charName);

				if (player != null) {
					if (player.getClanid() == pc.getClanid()) {
					// 99,610,666,251,208,37,65,67,784,782,603,244,1005,1011,5153
					if (player.getMapId() == 99 
							|| player.getMapId() == 88 
							|| player.getMapId() == 89 
							|| player.getMapId() == 90 
							|| player.getMapId() == 5069 
							|| player.getMapId() == 360 
							|| player.getMapId() == 610 
							|| player.getMapId() == 666 
							|| player.getMapId() == 251 || player.getMapId() == 208 || player.getMapId() == 37 || player.getMapId() == 65 || player.getMapId() == 67 || player.getMapId() == 784 || player.getMapId() == 782 || player.getMapId() == 603 || player.getMapId() == 244 || player.getMapId() == 1005 || player.getMapId() == 1011 || player.getMapId() == 5153) {
						pc.sendPackets(new S_SystemMessage("���Ϳ� " + player.getName() + "���� ��ȯ�Ҽ� ���� ��ġ�� �ֽ��ϴ�."));
					} else {
						if (player.isPrivateShop()) {
							
							return;
						}
						L1Teleport.teleportToTargetFront(player, pc, 2);
						pc.sendPackets(new S_SystemMessage("\\fY" + charName + "���� ��ȯ�Ͽ����ϴ�."));
						pc.getInventory().consumeItem(40308, 1000000);
					}
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY" + charName + "���� ���� ���Ϳ��� �ƴϰų� ���� �����ϰ� ���� �ʽ��ϴ�."));
				  }
				}
			} catch (Exception e) {
				pc.sendPackets(new S_SystemMessage(".������ȯ [ĳ���͸�]"));
			}

		}
	
	private void entertime(L1PcInstance pc) {
		try {
			int entertime1 = 180 - pc.getGdungeonTime() % 1000;
			int entertime2 = 180 - pc.getLdungeonTime() % 1000;
			int entertime3 = 120 - pc.getTkddkdungeonTime() % 1000;
			int entertime4 = 120 - pc.getDdungeonTime() % 1000;
			int entertime5 = 120 - pc.getoptTime() % 1000;
			   
			String time1 = Integer.toString(entertime1);
			String time2 = Integer.toString(entertime2);
			String time3 = Integer.toString(entertime3);
			String time4 = Integer.toString(entertime4);
			String time5 = Integer.toString(entertime5);
			
			
			pc.sendPackets(new S_ServerMessage(2535, "\\fY��� ���� ����", time1)); // 2535 %0 : ���� �ð� %1 �� // ������ ��� ����: , ���ž:, ��Ÿ�ٵ� ����:
			pc.sendPackets(new S_ServerMessage(2535, "\\fY��Ÿ�ٵ� ����", time2));
			pc.sendPackets(new S_ServerMessage(2535, "\\fY���ž ����", time3));
			pc.sendPackets(new S_ServerMessage(2535, "\\fY���� ����", time4));
			pc.sendPackets(new S_ServerMessage(2535, "\\fY��ũ���ʱ���", time5));
		} catch (Exception e) {
		}
	}
	
	/** ��ġ���� */
	private void TargetLoc(L1PcInstance pc, String param) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			String para1 = stringtokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(para1);

			String msg = null;
			// ��� ��ġ���� ����.
			if (para1.equalsIgnoreCase("��Ƽ��")  || para1.equalsIgnoreCase("���") || para1.equalsIgnoreCase("ī�ÿ����")|| para1.equalsIgnoreCase("�����") || para1.equalsIgnoreCase("�̼��Ǿ�")){
				pc.sendPackets(new S_SystemMessage(param + "���� �������� �ƴմϴ�."));
				return;
			}
			if (target != null) {
				if (pc.getInventory().checkItem(40308, 100000)){
					pc.getInventory().consumeItem(40308, 100000);
					int mapid = target.getMapId();
					if (mapid == 1){ msg = "���ϴ� �� ����";
					} else if (target.getMap().isSafetyZone(target.getLocation())){ msg = "����";
					} else if (mapid == 4 || mapid == 0 && target.getMap().isNormalZone(target.getLocation())){ msg = "�ʵ�";
					} else if (target.isPrivateShop()){ msg ="����";
					} else if (mapid >= 7 && mapid <= 13){ msg = "�۷��� ����";
					} else if (mapid >= 18 && mapid <= 20){ msg = "������ ����";
					} else if (mapid >= 25 && mapid <= 28){ msg = "���� ����";
					} else if (mapid >= 30 && mapid <= 33 || mapid >= 35 && mapid <= 36){ msg = "���� ��� ����";
					} else if (mapid >= 43 && mapid <= 51){ msg = "���� ����";
					} else if (mapid >= 53 && mapid <= 56){ msg = "��� ����";
					} else if (mapid >= 59 && mapid <= 63){ msg = "���� �ձ�";
					} else if (mapid == 70){ msg = "������ ��";
					} else if (mapid >= 271 && mapid <= 278){ msg = "���� ����";
					} else if (mapid >= 75 && mapid <= 82){ msg = "���ž";
					} else if (mapid >= 101 && mapid <= 200){ msg = "������ ž";
					} else if (mapid == 301){ msg = "���ϼ���";
					} else if (mapid == 303){ msg = "��ȯ�� ��";
					} else if (mapid == 304){ msg = "ħ���� ����";
					} else if (mapid >= 307 && mapid <= 309){ msg = "���� ħ����";
					} else if (mapid == 400){ msg = "����� ���ױ� ����";
					} else if (mapid == 401){ msg = "����� ������ ����";
					} else if (mapid == 410){ msg = "���� ����";
					} else if (mapid == 420){ msg = "���� ȣ��";
					} else if (mapid == 430){ msg = "������ ����";
					} else if (mapid == 5167){ msg = "�Ǹ����� ����";
					} else if (mapid == 5153){ msg = "��Ʋ��";
					} else if (mapid >= 440 && mapid <= 444){ msg = "������";
					} else if (mapid >= 450 && mapid <= 478 || mapid >= 490 && mapid <= 496 || mapid >= 530 && mapid <= 536 ){ msg = "��Ÿ�ٵ� ��";
					} else if (mapid >= 521 && mapid <= 524){ msg = "�׸��� ����";
					} else if (mapid >= 600 && mapid <= 607){ msg = "����� ����";
					} else if (mapid >= 777 && mapid <= 779){ msg = "�������� ��";
					} else if (mapid >= 780 && mapid <= 782){ msg = "�׺�";
					} else if (mapid >= 783 && mapid <= 784){ msg = "ƼĮ";
					} else if (mapid == 5302 && mapid == 5490){ msg = "������";
					} else { msg = "������ �ȵǴ� ��";
					}
					pc.sendPackets(new S_SystemMessage(target.getName() + "���� ��ġ�� �������Դϴ�."));
					Thread.sleep(2000);
					pc.sendPackets(new S_SystemMessage(target.getName()+"���� ��ġ�� "+msg+"�Դϴ�."));
				} else {
					pc.sendPackets(new S_SystemMessage("�Ƶ����� �����մϴ�."));
				}
			} else {
				pc.sendPackets(new S_SystemMessage(param + "���� �������� �ƴմϴ�."));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("------------------< ��ġ���� >--------------------"));
			pc.sendPackets(new S_SystemMessage("������ ��ġ�� �����ϴ� �ý����Դϴ�."));
			pc.sendPackets(new S_SystemMessage("��ġ ���� ����� 10�� �Ƶ����� �Ҹ�˴ϴ�."));
			pc.sendPackets(new S_SystemMessage(".��ġ���� ĳ���͸�"));
			pc.sendPackets(new S_SystemMessage("--------------------------------------------------"));
		}
	}
	
	private void telrek(L1PcInstance pc) {
		try {
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			// /////////////////////// Ÿ��/////////////////////////////////
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 20 > curtime) {
				pc.sendPackets(new S_SystemMessage("20�ʰ��� �����ð��� �ʿ��մϴ�."));
				return;
			}
			// /////////////////////// Ÿ��/////////////////////////////////
			if (pc.getMapId() == 5302 || pc.getMapId() == 5490 ) {
				pc.sendPackets(new S_SystemMessage("�̰������� ����� �� �����ϴ�."));
				return;
			}
			if (CharPosUtil.getZoneType(pc) == 0 && castle_id != 0) {
				pc.sendPackets(new S_SystemMessage("���ֺ������� ��� �� �� �����ϴ�."));
				return;
			}
			if (pc.getMapId() == 350|| pc.getMapId() == 5153) {
				pc.sendPackets(new S_SystemMessage("�̰������� ����� �� �����ϴ�."));
				return;
			}
			if (pc.isPinkName() || pc.isParalyzed() || pc.isSleeped()) {
				pc.sendPackets(new S_SystemMessage("������ ������ ����߿��� ����� �� �����ϴ�."));
				return;
			}
			if (pc.isDead()) {
				pc.sendPackets(new S_SystemMessage("���� ���¿��� ����� �� �����ϴ�."));
				return;
			}
			if (!(pc.getInventory().checkItem(40308, 1000))) {
				pc.sendPackets(new S_SystemMessage("1000 �Ƶ����� �����մϴ�."));
				return;
			}
			pc.getInventory().consumeItem(40308, 1000);

			 L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(),pc.getMoveState().getHeading(), false);
			pc.sendPackets(new S_SystemMessage("1000 �Ƶ����� �Ҹ� �Ǿ����ϴ�."));
			//pc.sendPackets(new S_SystemMessage("�ֺ� ������Ʈ�� ��ε� �Ͽ����ϴ�."));
			pc.setQuizTime(curtime);
	//	} catch (Exception e) {
		} catch (Exception exception35) {
		}
	}

	private void Hunt(L1PcInstance pc, String cmd) {
		try {
			StringTokenizer st = new StringTokenizer(cmd);
			String char_name = st.nextToken();
			int price = Integer.parseInt(st.nextToken());
			String story = st.nextToken();

			L1PcInstance target = null;
			target = L1World.getInstance().getPlayer(char_name);
			if (target != null) {
				if (target.isGm()) {
					return;
				}
			/*	if (char_name.equals(pc.getName())) {
					pc.sendPackets(new S_SystemMessage("�ڽſ��� ������� �ɼ� �����ϴ�."));
					return;
				}*/
				if (target.getHuntCount() == 1) {
					pc.sendPackets(new S_SystemMessage("�̹� ���� �Ǿ��ֽ��ϴ�"));
					return;
				}
				if (price < 1000000) {
					pc.sendPackets(new S_SystemMessage("�ּ� �ݾ��� 100�� �Ƶ����Դϴ�"));
					return;
				}
				if (!(pc.getInventory().checkItem(40308, price))) {
					pc.sendPackets(new S_SystemMessage("�Ƶ����� �����մϴ�"));
					return;
				}
				if (story.length() > 20) {
					pc.sendPackets(new S_SystemMessage("������ ª�� 20���ڷ� �Է��ϼ���"));
					return;
				}
				target.setHuntCount(1);
				target.setHuntPrice(target.getHuntPrice() + price);
				target.setReasonToHunt(story);
				target.save();
				L1World.getInstance().broadcastServerMessage("\\fYL(" + target.getName() + ")�� �� ������� �ɷȽ��ϴ�.");
				L1World.getInstance().broadcastPacketToAll(
						new S_SystemMessage("\\fY[����:  " + story + "  ]"));
				L1World.getInstance().broadcastPacketToAll(
						new S_SystemMessage("\\fY[������:  " + target.getName() + "  ]"));
				pc.getInventory().consumeItem(40308, price);
			} else {
				pc.sendPackets(new S_SystemMessage("���������� �ʽ��ϴ�."));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("[�ݾ�] 100�� �߰�Ÿ��ġ �ο�"));
			pc.sendPackets(new S_SystemMessage(".���� [ĳ���͸�] [�ݾ�] [����]"));
		}
	}

	/** ���� * */
	private void age(L1PcInstance pc, String cmd) {
		try {
			StringTokenizer tok = new StringTokenizer(cmd);
			String AGE = tok.nextToken();
			int AGEint = Integer.parseInt(AGE);

			if (AGEint > 99) {
				pc.sendPackets(new S_SystemMessage("�Է��Ͻ� ���̴� �ùٸ� ���� �ƴմϴ�."));
				return;
			}
			pc.setAge(AGEint);
			pc.save();
			pc.sendPackets(new S_SystemMessage(pc.getName() + "���� ���̰� " + AGEint + "���� �����Ǿ����ϴ�."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".���� [����]�� �Է��ϼ���"));
		}
	}
	private void LockItem1(L1PcInstance pc, String param) {
		try {
			if (pc.getInventory().checkItem(50021, 1)) {
				pc.sendPackets(new S_SystemMessage("���������ֹ����� ��δ� ����� ��û�ϼ���."));
				return;
			}
			if (!(pc.getInventory().checkItem(40308, 300000))) {
				pc.sendPackets(new S_SystemMessage("���� ���� �ֹ��� ��û ����� �����մϴ�."));
				return;
			}
			pc.getInventory().consumeItem(40308, 300000);

			pc.getInventory().storeItem(50021, 10);
			pc.sendPackets(new S_SystemMessage("���� ���� �ֹ���(10)���� ���޵Ǿ����ϴ�."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("��� ��).���ܽ�û "));
		}
	}

	private void changename(L1PcInstance pc, String name) {
		if (BadNamesList.getInstance().isBadName(name)) {
			pc.sendPackets(new S_SystemMessage("���� ������ ĳ�����Դϴ�"));
			return;
		}
		if (pc.getClanid() > 0) {
			pc.sendPackets(new S_SystemMessage("����Ż���� ĳ������ �����Ҽ� �ֽ��ϴ�."));
			return;
		}
		if (CharacterTable.doesCharNameExist(name)) {
			pc.sendPackets(new S_SystemMessage("������ ĳ������ ���� �մϴ�"));
			return;
		}
		if (pc.isCrown()) {
			pc.sendPackets(new S_SystemMessage("����ĳ���� ĳ������ �����ϽǼ� �����ϴ�"));
			return;
		}
		if (pc.getSkillEffectTimerSet().hasSkillEffect(1005)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(2005)) { // ä��
			pc.sendPackets(new S_SystemMessage("ä���߿��� �����ϽǼ� �����ϴ�."));
			return;
		}
		try {
			if (pc.getLevel() >= 55) {
				for (int i = 0; i < name.length(); i++) {
					if (name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��
							name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��
							name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��' || name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��' || name.charAt(i) == '��'
							|| name.charAt(i) == '��' || name.charAt(i) == '��'
							|| // �ѹ���(char)������ ��.
							name.charAt(i) == '��' || name.charAt(i) == '��'
							|| name.charAt(i) == '��' || name.charAt(i) == '��') {
						pc.sendPackets(new S_SystemMessage("ĳ������ �ùٸ��� �ʽ��ϴ�"));
						return;
					}
				}

				for (int i = 0; i < name.length(); i++) {
					if (!Character.isLetterOrDigit(name.charAt(i))) {

						pc.sendPackets(new S_SystemMessage("Ư������ ������� ������� ���մϴ�"));
						return;
					}
				}

				int numOfNameBytes = 0;
				numOfNameBytes = name.getBytes("EUC-KR").length;

				if (numOfNameBytes < 2 || numOfNameBytes > 12) {
					pc.sendPackets(new S_SystemMessage("�ѱ� 6�� ���� 12�� �̳����� �Է¹ٶ��ϴ�"));
					return;
				}

				if (pc.getInventory().consumeItem(467009, 1)) {// xxxxxxx��� �Ͻ� �����ۺ�ȣ
					Connection con = null;
					PreparedStatement pstm = null;
					try {
						con = L1DatabaseFactory.getInstance().getConnection();
						pstm = con.prepareStatement("UPDATE characters SET char_name =? WHERE char_name = ?");
						pstm.setString(1, name);
						pstm.setString(2, pc.getName());
						pstm.execute();
					} catch (SQLException e) {
					} finally {
						SQLUtil.close(pstm);
						SQLUtil.close(con);
					}

					pc.save(); // ����
					/** **** ���� ���Ϸ� ĳ������ ���� �ۼ� �κ�****** */

					/** ****LogDB ��� ������ �̸� ���� �صμ���****** */
					Calendar rightNow = Calendar.getInstance();
					int date = rightNow.get(Calendar.DATE);
					int hour = rightNow.get(Calendar.HOUR);
					int min = rightNow.get(Calendar.MINUTE);
					String strDate = "";
					String strhour = "";
					String strmin = "";
					strDate = Integer.toString(date);
					strhour = Integer.toString(hour);
					strmin = Integer.toString(min);
					String str = "";
					str = new String("[" + strDate + ":" + strhour + ":"
							+ strmin + "] " + pc.getName() + "  >  " + name
							+ "  [ĳ������]");// �α� �ۼ�
					StringBuffer FileName = new StringBuffer(
							"LogDB/ChangPcName.txt");
					PrintWriter out = null;
					try {
						out = new PrintWriter(new FileWriter(FileName
								.toString(), true));
						out.println(str);
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					str = "";// �ʱ�ȭ

					pc.sendPackets(new S_SystemMessage(
							"�������Ͻø� ���ο� ĳ�������� ����˴ϴ�."));
					L1World.getInstance().broadcastPacketToAll(
							new S_SystemMessage("("+pc.getName() + ")���� ĳ���͸���("+ name
							+")���� �����Ͽ����ϴ�."));
					buddys(pc);// ����� ���̵� ��񿡼� ����....�׷��� ������ �����ϴ� ����
					Thread.sleep(500);
					pc.sendPackets(new S_Disconnect());

				} else {
					pc.sendPackets(new S_SystemMessage("ĳ���� ���� �ֹ����� �����ϴ�."));
				}
			} else {
				pc.sendPackets(new S_SystemMessage("55���� �̻� �ɸ��� ��밡���մϴ�"));
			}

		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".ĳ������ �ɸ��� ���� �Է����ּ���"));
		}
	}

	/** *******��� ģ����Ͽ��� ����� ���̵� �����*********** */

	private void buddys(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;

		String aaa = pc.getName();

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("DELETE FROM character_buddys WHERE buddy_name=?");

			pstm.setString(1, aaa);
			pstm.execute();

		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/** ���� ��Ƽ ��û ��ɾ� * */
	public void BloodParty (L1PcInstance pc){
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 20 > curtime) {
			pc.sendPackets(new S_SystemMessage("20�ʰ��� �����ð��� �ʿ��մϴ�."));
			return;
		}
		if (pc.isDead()) {
			pc.sendPackets(new S_SystemMessage("���� ���¿��� ����� �� �����ϴ�."));
			return;
		}
	
		int ClanId = pc.getClanid();
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		
		if(clan == null){
			pc.sendPackets(new S_SystemMessage("������ �������� �ʾ� ����� �� �����ϴ�.")); 
			return;
		}
		
		if (pc.getClanRank() == 6 || pc.isCrown()){ //Clan[O] [����,��ȣ���]
			for (L1PcInstance SearchBlood : clan.getOnlineClanMember()) {
				if(SearchBlood.getClanid()!= ClanId || SearchBlood.isPrivateShop() || SearchBlood.isInParty()){ // Ŭ���� �����ʴٸ�[X], �̹���Ƽ���̸�[X], ������[X]
					continue; // ����Ż��
				} else if(SearchBlood.getName() != pc.getName()){
					pc.setPartyType(1); // ��ƼŸ�� ����
					SearchBlood.setPartyID(pc.getId()); // ��Ƽ���̵� ����
					SearchBlood.sendPackets(new S_Message_YN(954, pc.getName())); // ������Ƽ ��û
					pc.sendPackets(new S_SystemMessage("����� ["+SearchBlood.getName()+"]���� ��Ƽ�� ��û�߽��ϴ�."));
				}
			}
			pc.setQuizTime(curtime);
		} else { // Ŭ���� ���ų� ���� �Ǵ� ��ȣ��� [X]
			pc.sendPackets(new S_SystemMessage("������ [����], [��ȣ���]�� ����Ҽ� �ֽ��ϴ�."));
		}
	}

	/** ���� ��Ƽ ��û ��ɾ� * */
	private void featherAdd(L1PcInstance pc) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 20 > curtime) {
				pc.sendPackets(new S_SystemMessage("20�ʰ��� �����ð��� �ʿ��մϴ�."));
				return;
			}
			if (pc.getMapId() != 4) {
				pc.sendPackets(new S_SystemMessage("�Ƶ� ���������  ����� �� �ֽ��ϴ�."));
				return;
			}
			if (pc.isPinkName() || pc.isParalyzed() || pc.isSleeped()) {
				pc.sendPackets(new S_SystemMessage("������ ������ ����߿��� ����� �� �����ϴ�."));
				return;
			}
			if (pc.getMapId() == 5302 || pc.getMapId() == 5490 ) {
				pc.sendPackets(new S_SystemMessage("�̰������� ����� �� �����ϴ�."));
				return;
			}
			if (pc.getMapId() == 350) {
				pc.sendPackets(new S_SystemMessage("�̰������� ����� �� �����ϴ�."));
				return;
			}
			L1ItemInstance[] items = pc.getInventory().findItemsId(41159);
			int count = 0;

			for (L1ItemInstance item : items) {
				count += pc.getInventory().removeItem(item, item.getCount());
			}
			pc.getInventory().storeItem(41159, count, "�߰�:��ġ��");
			pc.sendPackets(new S_SystemMessage("�� " + count + "������ ���ƽ��ϴ�."));
			pc.setQuizTime(curtime);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".������ġ�� �� �Է����ּ���."));
		}
	}

	/**
	 * ����� ���� Ŀ�ǵ� ���� �Լ�
	 */
	public void ment(L1PcInstance pc, String cmd, String param) {
		if (param.equalsIgnoreCase("��")) {
			pc.getSkillEffectTimerSet()
					.setSkillEffect(L1SkillId.STATUS_MENT, 0);
			pc.sendPackets(new S_SystemMessage("������� ��Ʈ�� ���ϴ�."));
		} else if (param.equalsIgnoreCase("��")) {
			pc.getSkillEffectTimerSet()
					.removeSkillEffect(L1SkillId.STATUS_MENT);
			pc.sendPackets(new S_SystemMessage("������� ��Ʈ�� �մϴ�."));

		} else {
			pc.sendPackets(new S_SystemMessage(cmd + " [��,��] ��� �Է��� �ּ���. "));
		}
	}
	
	private void clanbuff(L1PcInstance pc) {
		   String clanName = pc.getClanname();
		   L1Clan clan = L1World.getInstance().getClan(clanName);
		   int[] allBuffSkill = { 43, 48, 79, 26,42};
		   if (pc.isDead()){ 
		    return;
		   }
		   long curtime = System.currentTimeMillis() / 1000;
		   if (pc.getQuizTime() + 20 > curtime) {
		    pc.sendPackets(new S_SystemMessage("20�ʰ��� �����ð��� �ʿ��մϴ�."));
		    return;
		   }
		   if (pc.getClanid() != 0 && clan.getOnlineClanMember().length >= 8) {
		    try {
		     L1SkillUse l1skilluse = new L1SkillUse();
		     for (int i = 0; i < allBuffSkill.length; i++) {
		      l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(),
		        pc.getX(), pc.getY(), null, 0,
		        L1SkillUse.TYPE_GMBUFF);
		      pc.setQuizTime(curtime);
		     }
		    } catch (Exception e) {
		    }
		   } else {
		    pc.sendPackets(new S_SystemMessage("������ ���ų� ���Ϳ� (8��)�̸��� ���¿�����  ������ ������ �����ϴ�."));
		   }
		  }

	public void autoroot(L1PcInstance pc, String cmd, String param) {
		if (param.equalsIgnoreCase("��")) { // ������� �Բ� ��ɾ�
			pc.getSkillEffectTimerSet().setSkillEffect(
					L1SkillId.STATUS_AUTOROOT, 0);
			pc.sendPackets(new S_SystemMessage("������� �� �����մϴ�. "));

		} else if (param.equalsIgnoreCase("��")) { // ������� �Բ� ��ɾ�
			pc.getSkillEffectTimerSet().removeSkillEffect(
					L1SkillId.STATUS_AUTOROOT);
			pc.sendPackets(new S_SystemMessage("������� �� Ȱ��ȭ�մϴ�. "));

		} else { // ������� �Բ� ��ɾ�
			pc.sendPackets(new S_SystemMessage(cmd + " [��,��] ��� �Է��� �ּ���. "));
		}
	}

	private void adenaAdd(L1PcInstance pc) {
		try {
			// /////////////////////// Ÿ��/////////////////////////////////
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 20 > curtime) {
				pc.sendPackets(new S_SystemMessage("20�ʰ��� �����ð��� �ʿ��մϴ�."));
				return;
			}
			// /////////////////////// Ÿ��/////////////////////////////////
			if (pc.getMapId() != 4) {
				pc.sendPackets(new S_SystemMessage("�Ƶ� ���������  ����� �� �ֽ��ϴ�."));
				return;
			}
			if (pc.getMapId() == 5302 || pc.getMapId() == 5490) {
				pc.sendPackets(new S_SystemMessage("�̰������� ����� �� �����ϴ�."));
				return;
			}
			if (pc.isPinkName() || pc.isParalyzed() || pc.isSleeped()) {
				pc.sendPackets(new S_SystemMessage("������ ������ ����߿��� ����� �� �����ϴ�."));
				return;
			}
			if (pc.getMapId() == 350) {
				pc.sendPackets(new S_SystemMessage("�̰������� ����� �� �����ϴ�."));
				return;
			}
			L1ItemInstance[] items = pc.getInventory().findItemsId(40308);
			int count = 0;

			for (L1ItemInstance item : items) {
				count += pc.getInventory().removeItem(item, item.getCount());
			}
			pc.getInventory().storeItem(40308, count, "�߰�:��ġ��");
			pc.sendPackets(new S_SystemMessage("�� " + count + "�Ƶ����� ���ƽ��ϴ�."));
			pc.setQuizTime(curtime);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".�Ƶ���ġ�� �� �Է����ּ���."));
		}
	}
	/*private void BloodProtect(L1PcInstance pc){ //���ͺ�ȣ ������ɾ�κ� 
		 try{
		  if(pc.getProtect() == true){
		   pc.sendPackets(new S_SystemMessage("���ͺ�ȣ ���¸� �����մϴ�."));
		   pc.setProtect(false);
		  }else{
		   pc.sendPackets(new S_SystemMessage("���ͺ�ȣ ���¸� Ȱ��ȭ�մϴ�."));
		   pc.setProtect(true);
		  }
		 }catch(Exception e){
		  pc.sendPackets(new S_SystemMessage("���ͺ�ȣ ��ɾ� ����."));
		 }
		}*/
	private void serchdroplist2(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String nameid = tok.nextToken();

			int npcid = 0;
			try {
				npcid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				npcid = NpcTable.getInstance().findNpcIdByName(nameid);
				if (npcid == 0) {
					pc.sendPackets(new S_SystemMessage("�ش� ���Ͱ� �߰ߵ��� �ʾҽ��ϴ�."));
					return;
				}
			}
			pc.sendPackets(new S_Serchdrop2(npcid));
		} catch (Exception e) {
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc.sendPackets(new S_SystemMessage(".���̸� [NPCID]�� �Է��� �ּ���."));
			pc.sendPackets(new S_SystemMessage("���� ID�� ������� ��Ȯ�� �Է��ؾ� �մϴ�."));
			pc.sendPackets(new S_SystemMessage("ex) .���̸� ��������Ʈ -- > �˻� O"));
			pc.sendPackets(new S_SystemMessage("ex) .���̸� ���� ����Ʈ -- > �˻� X"));
		}
	}
	
	private void buff(L1PcInstance pc) {
		
		int[] allBuffSkill = { PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR, BLESS_WEAPON, ADVANCE_SPIRIT };
		if (pc.isDead())
			return;

		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 20 > curtime) {
			long sec = (pc.getQuizTime() + 20) - curtime;
			pc.sendPackets(new S_SystemMessage(sec + "�ʰ��� �����ð��� �ʿ��մϴ�."));
			return;
		}
		if (pc.getLevel() <= 65) {
			try {
				L1SkillUse l1skilluse = new L1SkillUse();
				for (int i = 0; i < allBuffSkill.length; i++) {
					l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					pc.setQuizTime(curtime);
				}
			} catch (Exception e) {
			}
		} else {
		//	pc.sendPackets(new S_SystemMessage("60���� ���Ĵ� ������ ������ �����ϴ�."));
			S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"Lv:65 �̻��� ������ ������ �����ϴ�.", Opcodes.S_OPCODE_MSG, 15); 
			pc.sendPackets(s_chatpacket);
		}
		pc.setQuizTime(curtime);
	}
	
	private void MarkView(L1PcInstance pc, String param) {//9��26��������ũ����
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 20 > curtime) {
			long sec = (pc.getQuizTime() + 20) - curtime;
			pc.sendPackets(new S_SystemMessage(sec + "�ʰ��� �����ð��� �ʿ��մϴ�."));
			return;
		}
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String Yn = tok.nextToken();

			String clan_name = pc.getClanname();
			L1Clan clan = L1World.getInstance().getClan(clan_name);
			int castle_id;

			if (Yn.equals("��")) {
				if (clan == null) {
					return;
				}
				if (pc.getId() != clan.getLeaderId() && pc.getClanRank() < 3){//!(pc.getClanRank() == CLAN_RANK_GUARDIN)) { // ��밡 ������ �̿�	
					pc.sendPackets(new S_ServerMessage(92, pc.getName())); // \f1%0�� �������� ���������� �ƴմϴ�.
					return;
				}

				for (castle_id = 1; castle_id <= 8; castle_id++) {
					if (WarTimeController.getInstance().isNowWar(castle_id)) {
						pc.sendPackets(new S_SystemMessage("������ �߿��� ���������� ���� �� �� �����ϴ�."));
						return;
					}
				}

				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInSameWar(clan_name, clan_name) == true) {
						return;
					}
				}
				L1War war = new L1War();

				war.handleCommands(2, clan_name, clan_name); // ������ ����
				L1World.getInstance().broadcastServerMessage("\\fY���͸�ũ ���� �Ϸ�");
			} else if (Yn.equals("��")) {
				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInSameWar(clan_name, clan_name) == true) {
						war.CeaseWar(clan_name, clan_name);
						L1World.getInstance().broadcastServerMessage("\\fY���͸�ũ ���� ����");
						return;
					}
				}
			}
			pc.setQuizTime(curtime);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".���͸�ũ [��,��]"));
		}
	}

	// ���ο�����
	/** ��ŷ Ȯ�� * */
	private void rank1(L1PcInstance pc, String param) {
		Connection con = null;
		int q = 0;
		int i = 0;
		int j = 0;
		String type = null;
		String type1 = null;
		String Clan = null;
		String poly = null;

		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			String para1 = stringtokenizer.nextToken();
			L1PcInstance TargetPc = L1World.getInstance().getPlayer(para1);
			int objid = TargetPc.getId();
			int ClanID = TargetPc.getClanid();
			if (param != null) {

				switch (pc.getType()) {
				case 0:
					type = "����";
					break;
				case 1:
					type = "���";
					break;
				case 2:
					type = "����";
					break;
				case 3:
					type = "������";
					break;
				case 4:
					type = "��ũ����";
					break;
				case 5:
					type = "����";
					break;
				case 6:
					type = "ȯ����";
					break;
				}

				if (!(pc.getInventory().checkItem(40308, 30000))) {
					pc
							.sendPackets(new S_SystemMessage(
									"�Ƶ����� �����Ͽ� ����� �� �����ϴ�."));
					return;
				}
				pc.getInventory().consumeItem(40308, 30000);

				con = L1DatabaseFactory.getInstance().getConnection();
				Statement pstm = con.createStatement();
				ResultSet rs = pstm
						.executeQuery("SELECT objid FROM characters WHERE AccessLevel = 0 order by Exp desc");

				if (TargetPc.getType() == 0) {
					Statement pstm2 = con.createStatement();
					ResultSet rs2 = pstm2
							.executeQuery("SELECT objid FROM characters WHERE type = 0 and AccessLevel = 0 order by Exp desc");
					while (rs2.next()) {
						j++;
						if (objid == rs2.getInt(1))
							break;
					}
					rs2.close();
					pstm2.close();
				} else if (TargetPc.getType() == 1) {
					Statement pstm2 = con.createStatement();
					ResultSet rs2 = pstm2
							.executeQuery("SELECT objid FROM characters WHERE type = 1 and AccessLevel = 0 order by Exp desc");
					while (rs2.next()) {
						j++;
						if (objid == rs2.getInt(1))
							break;
					}
					rs2.close();
					pstm2.close();
				} else if (TargetPc.getType() == 2) {
					Statement pstm2 = con.createStatement();
					ResultSet rs2 = pstm2
							.executeQuery("SELECT objid FROM characters WHERE type = 2 and AccessLevel = 0 order by Exp desc");
					while (rs2.next()) {
						j++;
						if (objid == rs2.getInt(1))
							break;
					}
					rs2.close();
					pstm2.close();
				} else if (TargetPc.getType() == 3) {
					Statement pstm2 = con.createStatement();
					ResultSet rs2 = pstm2
							.executeQuery("SELECT objid FROM characters WHERE type = 3 and AccessLevel = 0 order by Exp desc");
					while (rs2.next()) {
						j++;
						if (objid == rs2.getInt(1))
							break;
					}
					rs2.close();
					pstm2.close();
				} else if (TargetPc.getType() == 4) {
					Statement pstm2 = con.createStatement();
					ResultSet rs2 = pstm2
							.executeQuery("SELECT objid FROM characters WHERE type = 4 and AccessLevel = 0 order by Exp desc");
					while (rs2.next()) {
						j++;
						if (objid == rs2.getInt(1))
							break;
					}
					rs2.close();
					pstm2.close();
				} else if (TargetPc.getType() == 5) {
					Statement pstm2 = con.createStatement();
					ResultSet rs2 = pstm2
							.executeQuery("SELECT objid FROM characters WHERE type = 5 and AccessLevel = 0 order by Exp desc");
					while (rs2.next()) {
						j++;
						if (objid == rs2.getInt(1))
							break;
					}
					rs2.close();
					pstm2.close();
				} else if (TargetPc.getType() == 6) {
					Statement pstm2 = con.createStatement();
					ResultSet rs2 = pstm2
							.executeQuery("SELECT objid FROM characters WHERE type = 6 and AccessLevel = 0 order by Exp desc");
					while (rs2.next()) {
						j++;
						if (objid == rs2.getInt(1))
							break;
					}
					rs2.close();
					pstm2.close();
				}

				while (rs.next()) {

					q++;
					if (objid == rs.getInt(1))
						break;
				}

				if (TargetPc.getType() == 0) {
					type1 = "����";
				} else if (TargetPc.getType() == 1) {
					type1 = "���";
				} else if (TargetPc.getType() == 2) {
					type1 = "����";
				} else if (TargetPc.getType() == 3) {
					type1 = "������";
				} else if (TargetPc.getType() == 4) {
					type1 = "��ũ����";
				} else if (TargetPc.getType() == 5) {
					type1 = "����";
				} else if (TargetPc.getType() == 6) {
					type1 = "ȯ����";
				}

				if (ClanID != 0) {
					Clan = TargetPc.getClanname();
				} else {
					Clan = "-";
				}
				if (TargetPc.getLevel() <= 39) {
					poly = "�ذ�";
				} else if (TargetPc.getLevel() >= 40
						&& TargetPc.getLevel() <= 44) {
					poly = "��ť����";
				} else if (TargetPc.getLevel() >= 45
						&& TargetPc.getLevel() <= 49) {
					poly = "ī����";
				} else if (TargetPc.getLevel() == 50) {
					poly = "������Ʈ";
				} else if (TargetPc.getLevel() == 51) {
					poly = "Ŀ��";
				} else if (TargetPc.getLevel() >= 52
						&& TargetPc.getLevel() <= 54) {
					poly = "��������Ʈ";
				} else if (TargetPc.getLevel() >= 55
						&& TargetPc.getLevel() <= 59) {
					poly = "��ũ����Ʈ";
				} else if (TargetPc.getLevel() >= 60
						&& TargetPc.getLevel() <= 64) {
					poly = "�ǹ�����Ʈ";
				} else if (TargetPc.getLevel() >= 65
						&& TargetPc.getLevel() <= 69) {
					poly = "�ҵ帶����";
				} else if (TargetPc.getLevel() >= 70
						&& TargetPc.getLevel() <= 74) {
					poly = "��ũ����Ʈ";
				} else if (TargetPc.getLevel() >= 75
						&& TargetPc.getLevel() <= 79) {
					poly = "�˶����";
				} else if (TargetPc.getLevel() >= 80
						&& TargetPc.getLevel() <= 100) {
					poly = "����";
				}

				pc.sendPackets(new S_SystemMessage("[" + TargetPc.getName()
						+ "] ��������:" + q + "��   Ŭ��������:" + j + "�� \n���ŷ���:" + poly
						+ "   Ŭ����:" + type1 + "   ����:" + Clan + ""));
				rs.close();
				pstm.close();
				con.close();
			} else if (param == pc.getName()) {
				pc.sendPackets(new S_SystemMessage("�ɸ��Ͱ� ���峻�� ���� ���� �ʽ��ϴ�"));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".��ŷ�˻� [�ɸ��͸�]���� �Է��� �ֽʽÿ�."));
		}

	}

	private void rank(L1PcInstance pc) {
		Connection con = null;
		int q = 0;
		int i = 0;
		int j = 0;
		String type = null;
		String type1 = null;
		String Clan = null;
		String poly = null;
		try {
			int objid = pc.getId();
			int ClanID = pc.getClanid();

			switch (pc.getType()) {
			case 0:
				type = "����";
				break;
			case 1:
				type = "���";
				break;
			case 2:
				type = "����";
				break;
			case 3:
				type = "������";
				break;
			case 4:
				type = "��ũ����";
				break;
			case 5:
				type = "����";
				break;
			case 6:
				type = "ȯ����";
				break;
			}
			con = L1DatabaseFactory.getInstance().getConnection();
			Statement pstm = con.createStatement();
			ResultSet rs = pstm
					.executeQuery("SELECT objid FROM characters WHERE AccessLevel = 0 order by Exp desc");

			if (pc.getType() == 0) {
				Statement pstm2 = con.createStatement();
				ResultSet rs2 = pstm2
						.executeQuery("SELECT objid FROM characters WHERE type = 0 and AccessLevel = 0 order by Exp desc");
				while (rs2.next()) {
					j++;
					if (objid == rs2.getInt(1))
						break;
				}
				rs2.close();
				pstm2.close();
			} else if (pc.getType() == 1) {
				Statement pstm2 = con.createStatement();
				ResultSet rs2 = pstm2
						.executeQuery("SELECT objid FROM characters WHERE type = 1 and AccessLevel = 0 order by Exp desc");
				while (rs2.next()) {
					j++;
					if (objid == rs2.getInt(1))
						break;
				}
				rs2.close();
				pstm2.close();
			} else if (pc.getType() == 2) {
				Statement pstm2 = con.createStatement();
				ResultSet rs2 = pstm2
						.executeQuery("SELECT objid FROM characters WHERE type = 2 and AccessLevel = 0 order by Exp desc");
				while (rs2.next()) {
					j++;
					if (objid == rs2.getInt(1))
						break;
				}
				rs2.close();
				pstm2.close();
			} else if (pc.getType() == 3) {
				Statement pstm2 = con.createStatement();
				ResultSet rs2 = pstm2
						.executeQuery("SELECT objid FROM characters WHERE type = 3 and AccessLevel = 0 order by Exp desc");
				while (rs2.next()) {
					j++;
					if (objid == rs2.getInt(1))
						break;
				}
				rs2.close();
				pstm2.close();
			} else if (pc.getType() == 4) {
				Statement pstm2 = con.createStatement();
				ResultSet rs2 = pstm2
						.executeQuery("SELECT objid FROM characters WHERE type = 4 and AccessLevel = 0 order by Exp desc");
				while (rs2.next()) {
					j++;
					if (objid == rs2.getInt(1))
						break;
				}
				rs2.close();
				pstm2.close();
			} else if (pc.getType() == 5) {
				Statement pstm2 = con.createStatement();
				ResultSet rs2 = pstm2
						.executeQuery("SELECT objid FROM characters WHERE type = 5 and AccessLevel = 0 order by Exp desc");
				while (rs2.next()) {
					j++;
					if (objid == rs2.getInt(1))
						break;
				}
				rs2.close();
				pstm2.close();
			} else if (pc.getType() == 6) {
				Statement pstm2 = con.createStatement();
				ResultSet rs2 = pstm2
						.executeQuery("SELECT objid FROM characters WHERE type = 6 and AccessLevel = 0 order by Exp desc");
				while (rs2.next()) {
					j++;
					if (objid == rs2.getInt(1))
						break;
				}
				rs2.close();
				pstm2.close();
			}

			while (rs.next()) {

				q++;
				if (objid == rs.getInt(1))
					break;
			}

			if (pc.getType() == 0) {
				type1 = "����";
			} else if (pc.getType() == 1) {
				type1 = "���";
			} else if (pc.getType() == 2) {
				type1 = "����";
			} else if (pc.getType() == 3) {
				type1 = "������";
			} else if (pc.getType() == 4) {
				type1 = "��ũ����";
			} else if (pc.getType() == 5) {
				type1 = "����";
			} else if (pc.getType() == 6) {
				type1 = "ȯ����";
			}

			if (ClanID != 0) {
				Clan = pc.getClanname();
			} else {
				Clan = "-";
			}
			if (pc.getLevel() <= 39) {
				poly = "�ذ�";
			} else if (pc.getLevel() >= 40 && pc.getLevel() <= 44) {
				poly = "��ť����";
			} else if (pc.getLevel() >= 45 && pc.getLevel() <= 49) {
				poly = "ī����";
			} else if (pc.getLevel() == 50) {
				poly = "������Ʈ";
			} else if (pc.getLevel() == 51) {
				poly = "Ŀ��";
			} else if (pc.getLevel() >= 52 && pc.getLevel() <= 54) {
				poly = "��������Ʈ";
			} else if (pc.getLevel() >= 55 && pc.getLevel() <= 59) {
				poly = "��ũ����Ʈ";
			} else if (pc.getLevel() >= 60 && pc.getLevel() <= 64) {
				poly = "�ǹ�����Ʈ";
			} else if (pc.getLevel() >= 65 && pc.getLevel() <= 69) {
				poly = "�ҵ帶����";
			} else if (pc.getLevel() >= 70 && pc.getLevel() <= 74) {
				poly = "��ũ����Ʈ";
			} else if (pc.getLevel() >= 75
					&& pc.getLevel() <= 79) {
				poly = "�˶����";
			} else if (pc.getLevel() >= 80
					&& pc.getLevel() <= 100) {
				poly = "����";
			}
			pc.sendPackets(new S_SystemMessage("�� ŷ �� ȸ : ��������:" + q
					+ "��   Ŭ��������:" + j + "�� \n���ŷ���:" + poly + "   Ŭ����:" + type1
					+ "   ����:" + Clan + ""));
			rs.close();
			pstm.close();
			con.close();
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".����ŷ ���� �Է����ּ���."));
		}

	}

	/** ��ŷ Ȯ�� * */
	private static boolean isDisitAlpha(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)) // ���ڰ� �ƴ϶��
					&& Character.isLetterOrDigit(str.charAt(i)) // Ư�����ڶ��
					&& !Character.isUpperCase(str.charAt(i)) // �빮�ڰ� �ƴ϶��
					&& !Character.isLowerCase(str.charAt(i))) { // �ҹ��ڰ� �ƴ϶��
				check = false;
				break;
			}
		}
		return check;
	}
	 private void to_Change_Passwd(L1PcInstance pc, String passwd) {
			PreparedStatement statement = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			java.sql.Connection con = null;
		  	try {
				String login = null;
				String password = null;
				con = L1DatabaseFactory.getInstance().getConnection();
				password = passwd;
				statement = con.prepareStatement("select account_name from characters where char_name Like '" + pc.getName() + "'");
				rs = statement.executeQuery();

				while (rs.next()){
					login = rs.getString(1);
					pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login Like '" + login + "'");
					pstm.setString(1, password);
					pstm.execute();
					pc.sendPackets(new S_ChatPacket(pc,"��ȣ�������� :(" + passwd + ")�� ������ �Ϸ�Ǿ����ϴ�.", Opcodes.S_OPCODE_NORMALCHAT, 2));
					pc.sendPackets(new S_SystemMessage("��ȣ ������ ���������� �Ϸ�Ǿ����ϴ�."));
				}
				login = null;
				password = null;
			}catch(Exception e){
				System.out.println("to_Change_Passwd() Error : "+e);
			}finally{
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(statement);
				SQLUtil.close(con);
			}
		 }
	private void changePassword(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String passwd = tok.nextToken();
			Account account = Account.load(pc.getAccountName()); // �߰�
			if (account.getquize() != null) {
				pc.sendPackets(new S_SystemMessage("��� �������� ������ ������ �� �����ϴ�."));
				return;
			} // ��ȣ����� ��� �����Ǿ� ���� �ʴٸ� �ٲ� �� ������.
			if (passwd.length() < 4) {
				pc.sendPackets(new S_SystemMessage("�Է��Ͻ� ��ȣ�� �ڸ����� �ʹ� ª���ϴ�."));
				pc.sendPackets(new S_SystemMessage("�ּ� 4�� �̻� �Է��� �ֽʽÿ�."));
				return;
			}

			if (passwd.length() > 12) {
				pc.sendPackets(new S_SystemMessage("�Է��Ͻ� ��ȣ�� �ڸ����� �ʹ� ��ϴ�."));
				pc.sendPackets(new S_SystemMessage("�ִ� 12�� ���Ϸ� �Է��� �ֽʽÿ�."));
				return;
			}

			if (isDisitAlpha(passwd) == false) {
				pc.sendPackets(new S_SystemMessage("��ȣ�� ������ �ʴ� ���ڰ� ���� �Ǿ� �ֽ��ϴ�."));
				return;
			}

			to_Change_Passwd(pc, passwd);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".��ȣ���� [������ ��ȣ]�� �Է� ���ּ���."));
		}
	}

	private void quize(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String quize = tok.nextToken();
			Account account = Account.load(pc.getAccountName());

			if (quize.length() < 4) {
				pc.sendPackets(new S_SystemMessage("�ּ� 4�� �̻� �Է��� �ֽʽÿ�."));
				return;
			}

			if (quize.length() > 12) {
				pc.sendPackets(new S_SystemMessage("�ִ� 12�� ���Ϸ� �Է��� �ֽʽÿ�."));
				return;
			}
			if (isDisitAlpha(quize) == false) {
				pc.sendPackets(new S_SystemMessage("��� ������ �ʴ� ���ڰ� ���ԵǾ����ϴ�."));
				return;
			}

			if(account.getquize() != null){
				pc.sendPackets(new S_SystemMessage("�̹� ��� �����Ǿ� �ֽ��ϴ�."));
				return;
			}
				account.setquize(quize);
				Account.updateQuize(account);
			pc.sendPackets(new S_SystemMessage("\\fT�����ȣ (" + quize + ") �� �����Ǿ����ϴ�.\\fY[����������!]"));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".����� [�����Ͻ�����]�� �Է����ּ���."));
		}
	}
	private void quize1(L1PcInstance pc, String cmd) {
		try {
			StringTokenizer tok = new StringTokenizer(cmd);
			String quize2 = tok.nextToken();
			Account account = Account.load(pc.getAccountName());

			if (quize2.length() < 4) {
				pc.sendPackets(new S_SystemMessage("�Է��Ͻ� ������ �ڸ����� �ʹ� ª���ϴ�."));
				pc.sendPackets(new S_SystemMessage("�ּ� 4�� �̻� �Է��� �ֽʽÿ�."));
				return;
			}

			if (quize2.length() > 12) {
				pc.sendPackets(new S_SystemMessage("�Է��Ͻ� ������ �ڸ����� �ʹ� ��ϴ�."));
				pc.sendPackets(new S_SystemMessage("�ִ� 12�� ���Ϸ� �Է��� �ֽʽÿ�."));
				return;
			}

			if (account.getquize() == null || account.getquize() == "") {
				pc.sendPackets(new S_SystemMessage("��� �����Ǿ� ���� �ʽ��ϴ�."));
				return;
			}

			if (!quize2.equals(account.getquize())) {
				pc.sendPackets(new S_SystemMessage("������ ����� ��ġ���� �ʽ��ϴ�."));
				return;
			}

			if (isDisitAlpha(quize2) == false) {
				pc.sendPackets(new S_SystemMessage("��� ������ �ʴ� ���ڰ� ���ԵǾ����ϴ�."));
				return;
			}
			account.setquize(null);
			Account.updateQuize(account);
			pc.sendPackets(new S_SystemMessage("��� �����Ǿ����ϴ�."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("��� ��).������� ��ȣ(����)"));
		}
	}

	/** ���� Ű������� ��ɾ� * */
	private void gjtkd(L1PcInstance pc) {
		try {
			if (pc.isDead()) {
				pc.sendPackets(new S_SystemMessage("���� ���¿��� ���� �Ҽ������ϴ�."));
				return;
			}
			LineageClient client = pc.getNetConnection();
			client.quitGame(pc);
			synchronized (pc) {
				LetsbeSave(pc);
				pc.stopHpRegenerationByDoll();
				pc.stopMpRegenerationByDoll();
				pc.getNearObjects().removeAllKnownObjects();
				pc.setOnlineStatus(0);
				client.setActiveChar(null);
				client.setloginStatus(1);
				client.CharReStart(true);
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.LOGOUT));//ĳ������â����
			pc.setNetConnection(null);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("...�� �Է����ּ���."));
		}
	}
	/** ���� Ű����, �н����� ���� ��ɾ� * */
	private void LetsbeShop(L1PcInstance pc) {
		try {
			if (!pc.isPrivateShop()) {
				pc.sendPackets(new S_SystemMessage("���� ���λ����� �����ϼž� �մϴ�."));
				return;
			}
			if (pc.isPinkName() || pc.isParalyzed() || pc.isSleeped()) {
				pc.sendPackets(new S_SystemMessage("������ ������ ����߿��� ����� �� �����ϴ�."));
				return;
			}
			if (pc.isDead()) {
				pc.sendPackets(new S_SystemMessage("���� ���¿��� ���λ����� ���������ϴ�."));
				return;
			}
			for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
				if (target.getId() != pc.getId()
						&& target.getAccountName().toLowerCase().equals(
								pc.getAccountName().toLowerCase())
						&& target.isPrivateShop()) {
					pc.sendPackets(new S_SystemMessage("���λ����� �Ѱ��� ĳ���͸� �����մϴ�."));
					return;
				}
			}

			LineageClient client = pc.getNetConnection();
			client.quitGame(pc);
			synchronized (pc) {
				LetsbeSave(pc);
				pc.stopHpRegenerationByDoll();
				pc.stopMpRegenerationByDoll();
				pc.getNearObjects().removeAllKnownObjects();
				client.setActiveChar(null);
			}
			pc.sendPackets(new S_Disconnect());
			pc.setNetConnection(null);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".Ŭ������ �� �Է����ּ���."));
		}
	}


	private void LetsbeSave(L1PcInstance pc) {
		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception ex) {

			pc.saveInventory();
		}
	}
}
