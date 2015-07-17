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

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;


public class L1GeneralBuff implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1GeneralBuff.class.getName());

	private L1GeneralBuff() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1GeneralBuff();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String type = st.nextToken();
			
			if(type.equalsIgnoreCase("1")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION)) {           // õ���ǹ���
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.EXP_POTION);
							} 
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_COMA_3)) {     // �ڸ� 3����
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_COMA_3);
							} 
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_COMA_5)) {     // �ڸ� 5����
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_COMA_5);
							} 
							
							new L1SkillUse().handleCommands(player, L1SkillId.STATUS_COMA_3, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� �ڸ�(3) �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("2")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION)) {           // õ���ǹ���
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.EXP_POTION);
							} 
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_COMA_3)) {     // �ڸ� 3����
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_COMA_3);
							} 
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_COMA_5)) {     // �ڸ� 5����
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_COMA_5);
							} 
							
							new L1SkillUse().handleCommands(player, L1SkillId.STATUS_COMA_5, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� �ڸ�(5) �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("3")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_TIKAL_BOSSJOIN)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_TIKAL_BOSSJOIN);
							}
							
							new L1SkillUse().handleCommands(player, L1SkillId.STATUS_TIKAL_BOSSDIE, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ���ž�ູ ���� �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("4")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						
						
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)	
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� �巡������ ���� �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}				
				return;
			}
			
			if(type.equalsIgnoreCase("5")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_A)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_A);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_B)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_B);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_C)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_C);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_D)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_D);
							}
							
							new L1SkillUse().handleCommands(player, L1SkillId.STATUS_LUCK_A, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ��������(A)�����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("6")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_A)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_A);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_B)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_B);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_C)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_C);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_D)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_D);
							}
							
							new L1SkillUse().handleCommands(player, L1SkillId.STATUS_LUCK_B, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ��������(B)�����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("7")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_A)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_A);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_B)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_B);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_C)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_C);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_D)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_D);
							}
							
							new L1SkillUse().handleCommands(player, L1SkillId.STATUS_LUCK_C, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ��������(C)�����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("8")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_A)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_A);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_B)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_B);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_C)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_C);
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_LUCK_D)) {
								player.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_LUCK_D);
							}
							
							new L1SkillUse().handleCommands(player, L1SkillId.STATUS_LUCK_D, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ��������(D)�����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("9")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(7671)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7671);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7672)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7672);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7673)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7673);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7674)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7674);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7675)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7675);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7676)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7676);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7677)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7677);	
							}
							
							new L1SkillUse().handleCommands(player, 7671, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ���渶�� ������ �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("10")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(7671)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7671);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7672)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7672);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7673)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7673);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7674)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7674);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7675)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7675);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7676)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7676);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7677)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7677);	
							}
							
							new L1SkillUse().handleCommands(player, 7672, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ���渶�� ������ �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("11")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(7671)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7671);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7672)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7672);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7673)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7673);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7674)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7674);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7675)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7675);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7676)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7676);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7677)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7677);	
							}
							
							new L1SkillUse().handleCommands(player, 7673, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ȭ�渶�� ������ �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("12")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(7671)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7671);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7672)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7672);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7673)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7673);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7674)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7674);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7675)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7675);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7676)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7676);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7677)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7677);	
							}
							
							new L1SkillUse().handleCommands(player, 7674, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ǳ�渶�� ������ �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("13")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(7671)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7671);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7672)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7672);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7673)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7673);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7674)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7674);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7675)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7675);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7676)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7676);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7677)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7677);	
							}
							
							new L1SkillUse().handleCommands(player, 7675, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ź������ ������ �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("14")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(7671)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7671);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7672)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7672);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7673)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7673);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7674)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7674);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7675)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7675);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7676)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7676);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7677)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7677);	
							}
							
							new L1SkillUse().handleCommands(player, 7676, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ���󸶾� ������ �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
			
			if(type.equalsIgnoreCase("15")){
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					try {
						if (player == null || player.isPrivateShop()) {
							continue;
						}	
						if (player.getMapId() != 5166) { // ȸ���Ƕ� (�����ʱ�ȭ �ɸ��� ����ɸ�)							
							if (player.getSkillEffectTimerSet().hasSkillEffect(7671)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7671);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7672)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7672);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7673)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7673);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7674)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7674);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7675)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7675);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7676)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7676);	
							}
							if (player.getSkillEffectTimerSet().hasSkillEffect(7677)) {
								player.getSkillEffectTimerSet().removeSkillEffect(7677);	
							}
							
							new L1SkillUse().handleCommands(player, 7677, player.getId(), player.getX(), player.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
							player.sendPackets(new S_SystemMessage("\\fY��Ƽ��: ��Ų� ������ ������ �����߽��ϴ�."));
						}
					} catch (Exception e) { }
				}
				return;
			}
		} catch (Exception e) { }
		pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
		pc.sendPackets(new S_SystemMessage( ".Ư������ [�ɼ�]" ));
		pc.sendPackets(new S_SystemMessage( " [1:�ڸ�(3) 2.�ڸ�(5) 3:���ž�ູ 4:�巡������]"));
		pc.sendPackets(new S_SystemMessage( " [5:��������A 6.��������B 7:��������C 8:��������D]"));
		pc.sendPackets(new S_SystemMessage( " [9:���渶�� 10.���渶�� 11:ȭ�渶�� 12:ǳ�渶��]"));
		pc.sendPackets(new S_SystemMessage( " [13:ź������ 14.���󸶾� 15:������]"));
		pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));		
	}
}
