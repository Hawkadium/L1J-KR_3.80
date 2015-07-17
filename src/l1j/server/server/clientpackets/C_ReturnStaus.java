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

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.utils.CalcStat;
import server.CodeLogger;
import server.LineageClient;

public class C_ReturnStaus extends ClientBasePacket {
	public C_ReturnStaus(byte[] decrypt, LineageClient client) {
		super(decrypt);
		int type = readC();
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		if (type == 1) {
			short init_hp = 0;
			short init_mp = 0;
			int str = readC();
			int intel = readC();
			int wis = readC();
			int dex = readC();
			int con = readC();
			int cha = readC();
			int total = 0;
			total = str + dex + con + wis + cha + intel;

			if (!pc.getAbility().isNormalAbility(pc.getClassId(),
				pc.getLevel(), pc.getHighLevel(), total)) {
				    			CodeLogger.getInstance().hacklog("��ų�ʱ�ȭ ", pc.getName()); 
    		//	eva.writeMessage(14, "ĳ����="+pc.getName()); 
				pc.sendPackets(new S_Disconnect());
				return;
			}

			pc.getAbility().reset();

			pc.getAbility().setBaseStr((byte) str);
			pc.getAbility().setBaseInt((byte) intel);
			pc.getAbility().setBaseWis((byte) wis);
			pc.getAbility().setBaseDex((byte) dex);
			pc.getAbility().setBaseCon((byte) con);
			pc.getAbility().setBaseCha((byte) cha);

			pc.setLevel(1);

			if (pc.isCrown()) { // CROWN
				init_hp = 14;
				switch (pc.getAbility().getBaseWis()) {
				case 11:
					init_mp = 2;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 3;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 4;
					break;
				default:
					init_mp = 2;
					break;
				}
			} else if (pc.isKnight()) { // KNIGHT
				init_hp = 16;
				switch (pc.getAbility().getBaseWis()) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			} else if (pc.isElf()) { // ELF
				init_hp = 15;
				switch (pc.getAbility().getBaseWis()) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 4;
					break;
				}
			} else if (pc.isWizard()) { // WIZ
				init_hp = 12;
				switch (pc.getAbility().getBaseWis()) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 6;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 8;
					break;
				default:
					init_mp = 6;
					break;
				}
			} else if (pc.isDarkelf()) { // DE
				init_hp = 12;
				switch (pc.getAbility().getBaseWis()) {
				case 10:
				case 11:
					init_mp = 3;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 3;
					break;
				}
			} else if (pc.isDragonknight()) { // ����
				init_hp = 16;
				init_mp = 2;
			} else if (pc.isIllusionist()) { // ȯ����
				init_hp = 14;
				switch (pc.getAbility().getBaseWis()) {
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 5;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 5;
					break;
				}
			}
			pc.addBaseMaxHp((short) (init_hp - pc.getBaseMaxHp()));
			pc.addBaseMaxMp((short) (init_mp - pc.getBaseMaxMp()));
			pc.getAC().setAc(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
		} else if (type == 2) {
			int levelup = readC();
			if(ExpTable.getLevelByExp(pc.getExp()) < pc.getLevel()){
				pc.sendPackets(new S_Disconnect());
				sendMessage("���� �õ�: [" + pc.getName()
								+ "]");
				return;
			}
			
			if (CheckAbillity(pc)) {
			/////////////////////�߰�
			int stup = 1;
			if (stup != 1) {
				stup = 1;
			}
			/////////////////�߰�
			switch (levelup) {
			case 0:
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				Checkov(pc);
				break;
			case 1:
				checkStatusBug2(pc);
				pc.getAbility().addStr((byte) stup);//����
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 2:
				checkStatusBug2(pc);
				pc.getAbility().addInt((byte) stup);//����
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 3:
				checkStatusBug2(pc);
				pc.getAbility().addWis((byte) stup);//����
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 4:
				checkStatusBug2(pc);
				pc.getAbility().addDex((byte) stup);//����
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 5:
				checkStatusBug2(pc);
				pc.getAbility().addCon((byte) stup);//����
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 6:
				checkStatusBug2(pc);
				pc.getAbility().addCha((byte) stup);//����
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 7:
				if (pc.getLevel() + 10 < pc.getHighLevel()) {
					for (int m = 0; m < 10; m++)
						statup(pc);
					pc.sendPackets(new S_ReturnedStat(pc,
							S_ReturnedStat.LEVELUP));
				}
				Checkov(pc);
				break;
			case 8:
				int statusup = readC();
				switch (statusup) {// �Ϸ����
				case 1:
					CheckStat(pc);
					pc.getAbility().addStr((byte) stup);//����
					break;
				case 2:
					CheckStat(pc);
					pc.getAbility().addInt((byte) stup);//����
					break;
				case 3:
					CheckStat(pc);
					pc.getAbility().addWis((byte) stup);//����
					break;
				case 4:
					CheckStat(pc);
					pc.getAbility().addDex((byte) stup);//����
					break;
				case 5:
					CheckStat(pc);
					pc.getAbility().addCon((byte) stup);//����
					break;
				case 6:
					CheckStat(pc);
					pc.getAbility().addCha((byte) stup);//����
					break;
				}
				if (pc.getAbility().getElixirCount() > 0) {
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.END));
				} else {
                	try {
                		if(pc.getLevel() >= 51)	pc.getAbility().setBonusAbility(pc.getLevel() - 50);
                		else {	
						pc.getAbility().setBonusAbility(0);

						}
                		
               if(pc.getLevel() >= 51) { // 2��:�����ɸ� ���ʰ��� �׹��׹���by���

								pc.setExp(pc.getReturnStat());
								pc.sendPackets(new S_ReturnedStat(pc,
										S_ReturnedStat.END));
								pc.sendPackets(new S_OwnCharStatus(pc));
								pc.sendPackets(new S_OwnCharAttrDef(pc));
								pc.sendPackets(new S_SPMR(pc));
								pc.setCurrentHp(pc.getMaxHp());
								pc.setCurrentMp(pc.getMaxHp());
								pc.sendPackets(new S_HPUpdate(
										pc.getCurrentHp(), pc.getMaxHp()));
								pc.sendPackets(new S_MPUpdate(
										pc.getCurrentMp(), pc.getMaxMp()));
	                    L1Teleport.teleport(pc, 32612, 32734, (short)4, 5, true);
						CodeLogger.getInstance().hacklog("��ų�ʱ�ȭ ", pc.getName()); 
		    			//eva.writeMessage(14, "ĳ����="+pc.getName()); //�Ŵ���
								pc.setReturnStat(0);
								pc.StatReturnCK = false;
								pc.save();
								pc.CheckStatus();
							}// //�߰�
							else// //�߰�
							{// //�߰�
								return;// //�߰�
							}// /���ʹ��� �߰��Һκ�
						} catch (Exception exception) {
						}
					break;
			}	
			}
			} else {
				 pc.setReturnStat(1);
				 pc.sendPackets(new S_Disconnect());
				}
		} else if (type == 3) { // ���� �ʱ�ȭ�� ������ ó��
			try {
				int str = readC();
				int intel = readC();
				int wis = readC();
				int dex = readC();
				int con = readC();
				int cha = readC();
				int Str = pc.getAbility().getStr();
				int Int = pc.getAbility().getInt();
				int Wis = pc.getAbility().getWis();
				int Dex = pc.getAbility().getDex();
				int Con = pc.getAbility().getCon();
				int Cha = pc.getAbility().getCha();

				if (con < Con || str < Str || intel < Int || wis < Wis || dex < Dex || cha < Cha) {
				 pc.setReturnStat(1);
				pc.sendPackets(new S_Disconnect());
				} else {

				pc.getAbility().addStr((byte) (str - pc.getAbility().getStr()));
				pc.getAbility().addInt(
						(byte) (intel - pc.getAbility().getInt()));
				pc.getAbility().addWis((byte) (wis - pc.getAbility().getWis()));
				pc.getAbility().addDex((byte) (dex - pc.getAbility().getDex()));
				pc.getAbility().addCon((byte) (con - pc.getAbility().getCon()));
				pc.getAbility().addCha((byte) (cha - pc.getAbility().getCha()));

				if (pc.getLevel() >= 51) 
					pc.getAbility().setBonusAbility(pc.getLevel() - 50);
				 else {
					pc.getAbility().setBonusAbility(0);
				}
				if (pc.getLevel() >= 51) {// //�߰�

					pc.setExp(pc.getReturnStat());
					pc.sendPackets(new S_OwnCharStatus(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.setCurrentHp(pc.getMaxHp());
					pc.setCurrentMp(pc.getMaxHp());
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
							.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc
							.getMaxMp()));
					pc.sendPackets(new S_ReturnedStat(pc, 4));
	                L1Teleport.teleport(pc, 32612, 32734, (short)4, 5, true);
			CodeLogger.getInstance().hacklog("��ų�ʱ�ȭ ", pc.getName()); 
		//	eva.writeMessage(14, "ĳ����="+pc.getName()); 
					pc.setReturnStat(0);
					pc.StatReturnCK = false;
					pc.save();
					pc.CheckStatus();
				
				}// //�߰�
				else// //�߰�
				{// //�߰�
					return;// //�߰�
				}// /���ʹ��� �߰��Һκ�
				}
			} catch (Exception exception) {
			}
		}
	}
	public boolean CheckAbillity(L1PcInstance pc) {// �ǻ��Ƚ�
		boolean result = true;
		int MaxAbility = 35;
		int minStr, minDex, minCon, minWis, minCha, minInt;
		int str = pc.getAbility().getStr();
		int dex = pc.getAbility().getDex();
		int con = pc.getAbility().getCon();
		int wis = pc.getAbility().getWis();
		int cha = pc.getAbility().getCha();
		int intel = pc.getAbility().getInt();

		int StatusCount = str+dex+con+wis+cha+intel;

		switch(pc.getType()) {
		case 0:
		 minStr = 13;
		 minDex = 10;
		 minCon = 10;
		 minWis = 11;
		 minCha = 13;
		 minInt = 10;
		 break;
		case 1:
		 minStr = 16;
		 minDex = 12;
		 minCon = 14;
		 minWis = 9;
		 minCha = 12;
		 minInt = 8;
		 break;
		case 2:
		 minStr = 11;
		 minDex = 12;
		 minCon = 12;
		 minWis = 12;
		 minCha = 9;
		 minInt = 12;
		 break;
		case 3:
		 minStr = 8;
		 minDex = 7;
		 minCon = 12;
		 minWis = 12;
		 minCha = 8;
		 minInt = 12;
		 break;
		case 4:
		 minStr = 12;
		 minDex = 15;
		 minCon = 8;
		 minWis = 10;
		 minCha = 9;
		 minInt = 11;
		 break;
		case 5:
		 minStr = 13;
		 minDex = 11;
		 minCon = 14;
		 minWis = 12;
		 minCha = 8;
		 minInt = 11;
		 break;
		case 6:
		 minStr = 11;
		 minDex = 10;
		 minCon = 12;
		 minWis = 12;
		 minCha = 8;
		 minInt = 12;
		 break;
		default:
		 return false;
		}
		if(pc.getLevel() <= 50 && StatusCount > 75) { result = false; }
		if(pc.getAbility().getBaseStr() < minStr || pc.getAbility().getStr() > MaxAbility) result = false;
		if(pc.getAbility().getBaseDex() < minDex || pc.getAbility().getDex() > MaxAbility) result = false;
		if(pc.getAbility().getBaseCon() < minCon || pc.getAbility().getCon() > MaxAbility) result = false;
		if(pc.getAbility().getBaseWis() < minWis || pc.getAbility().getWis() > MaxAbility) result = false;
		if(pc.getAbility().getBaseCha() < minCha || pc.getAbility().getCha() > MaxAbility) result = false;
		if(pc.getAbility().getBaseInt() < minInt || pc.getAbility().getInt() > MaxAbility) result = false;
		return result;
		 }
	public void Checknoend(L1PcInstance pc) {
		pc.setReturnStat(1);
		pc.sendPackets(new S_Disconnect()); // ĳ���͸� ���忡�� �߹�
	}

	public void Checkend(L1PcInstance pc) {
		pc.setReturnStat(0);
		pc.setBonusStats(pc.getLevel() - 50);
		pc.sendPackets(new S_OwnCharStatus(pc));
		pc.sendPackets(new S_OwnCharAttrDef(pc));
		pc.setCurrentHp(pc.getMaxHp());
		pc.setCurrentMp(pc.getMaxHp());
		pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		L1Teleport.teleport(pc, 32612, 32734, (short) 4, 5, true);
		pc.sendPackets(new S_ReturnedStat(pc, 3));

	}

	/*
	 * ���̽� ���� ���� 1���� �ְ��� �� 20 ������ 19 �ɼ� ���� Ŭ���� ���� ���� ���� �ߴٰ� ���� �ʿ� ��� ����
	 */

	public void Checkov(L1PcInstance pc) {

		int[] BaseStat2 = { pc.getAbility().getBaseStr(),
				pc.getAbility().getBaseDex(), pc.getAbility().getBaseCon(),
				pc.getAbility().getBaseWis(), pc.getAbility().getBaseCha(),
				pc.getAbility().getBaseInt() };

		int[] masBase2 = { 21, 19, 19, 19, 19, 19 };
		for (int i = 0; i < BaseStat2.length; i++) {
			if (BaseStat2[i] > masBase2[i]) {
				Checknoend(pc);
				System.out.println("�⺻ ����  ���� : " + pc.getName() + " )");
			}
		}
	}

	/*
	 * Ŭ������ �ּ� �ִ밪 ���� �˻�
	 */
	public void CheckStat(L1PcInstance pc) {

		int[] BaseStat = { pc.getAbility().getBaseStr(),
				pc.getAbility().getBaseDex(), pc.getAbility().getBaseCon(),
				pc.getAbility().getBaseWis(), pc.getAbility().getBaseCha(),
				pc.getAbility().getBaseInt() };
		int[] masBase = null;
		String[] BaseStat5 = { " ��  ", " ��  ", " ��  ", " ���� ", " ī�� ", " ��Ʈ " };
		int maxBase = 35;
		/* �� �� �� ���� ī�� ��Ʈ */
		switch (pc.getType()) {
		case 0:// ����
			masBase = new int[] { 13, 10, 10, 11, 13, 10 };// ����
			break;
		case 1:// ���
			masBase = new int[] { 16, 12, 14, 9, 12, 8 };// ���
			break;
		case 2:// ����
			masBase = new int[] { 11, 12, 12, 12, 9, 12 };// ����
			break;
		case 3:// ������
			masBase = new int[] { 8, 7, 12, 12, 8, 12 };// ������
			break;
		case 4:// �ٿ�
			masBase = new int[] { 12, 15, 8, 10, 9, 11 };// �ٿ�
			break;
		case 5:// ����
			masBase = new int[] { 13, 11, 14, 12, 8, 11 };// ����
			break;
		case 6:// ȯ����
			masBase = new int[] { 11, 10, 12, 12, 8, 12 };// ȯ����
			break;
		}

		for (int i = 0; i < masBase.length; i++) {
			if (BaseStat[i] < masBase[i] || BaseStat[i] > maxBase) {
				Checknoend(pc);
				System.out.println("�ʱ�ȭ ���� / ���̵� : " + pc.getName() + " )");
				break;
			} else {
				System.out.println("[" + BaseStat5[i] + "] /(" + BaseStat[i]
						+ ") /�ʱ�ȭ ��������  ���̵� : " + pc.getName() + " )");
			}
		}

	}// /////

	/*
	 * ���� ���� �˻�
	 */
	public void checkStatusBug2(L1PcInstance pc) {
		int _Elixir = pc.getAbility().getElixirCount();
		int _status2 = 75 + pc.getAbility().getElixirCount();
		int _status = pc.getHighLevel() - 50;
		int status = pc.getAbility().getDex() + pc.getAbility().getCha()
				+ pc.getAbility().getCon() + pc.getAbility().getInt()
				+ pc.getAbility().getStr() + pc.getAbility().getWis();
		int _Allbase = pc.getAbility().getBaseStr()
				+ pc.getAbility().getBaseDex() + pc.getAbility().getBaseCon()
				+ pc.getAbility().getBaseWis() + pc.getAbility().getBaseCha()
				+ pc.getAbility().getBaseInt(); // ����

		if (pc.getHighLevel() <= 50) {// 50�����ϰ��
			if (_status2 > _Allbase + _Elixir) {
				Checknoend(pc);
				System.out.println("���� ���� / ���̵� : " + pc.getName() + " )");
			} else {
			}
		} else if (pc.getHighLevel() >= 51) { // 51�̻��ϰ��
			if (status + _Elixir > _status2 + _status) {
				Checknoend(pc);
				System.out.println("���� ���� / ���̵� : " + pc.getName() + " )");
			} else {
			}
		}

	}

	public void Levelup(L1PcInstance pc) {
		if (pc.getLevel() <= pc.getHighLevel()) { // ���� �Ҷ� �ִ� �������� ������ ����..
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
		} else {
			Checknoend(pc);
		}
	}
	private void sendMessage(String msg) {
		  for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
		   pc.sendPackets(new S_ChatPacket(pc, msg, Opcodes.S_OPCODE_MSG, 18));
		  }
		 }
	public void statup(L1PcInstance pc) {
		int Stathp = 0;
		int Statmp = 0;
        int baseCon = pc.getAbility().getBaseCon();
        int baseWis = pc.getAbility().getBaseWis();
    	if (pc.isCrown()) {
				if (baseWis >= 16) {
					Statmp = 1;
				} else {
					Statmp = 0;
				}
			} else if (pc.isKnight()) {
				Statmp = 0;
			} else if (pc.isElf()) {
				if (baseWis >= 14 && baseWis <= 16) {
					Statmp = 1;
				} else if (baseWis >= 17) {
					Statmp = 2;
				} else {
					Statmp = 0;
				}
			} else if (pc.isDarkelf()) {
				if (baseWis >= 12) {
					Statmp = 1;
				} else {
					Statmp = 0;
				}
			} else if (pc.isWizard()) {
				if (baseWis >= 13 && baseWis <= 16) {
					Statmp = 1;
				} else if (baseWis >= 17) {
					Statmp = 2;
				} else {
					Statmp = 0;
				}
			} else if (pc.isDragonknight()) {
				if (baseWis >= 13 && baseWis <= 15) {
					Statmp = 1;
				} else if (baseWis >= 16) {
					Statmp = 2;
				} else {
					Statmp = 0;
				}
			} else if (pc.isIllusionist()) {
				if (baseWis >= 13 && baseWis <= 15) {
					Statmp = 1;
				} else if (baseWis >= 16) {
					Statmp = 2;
				} else {
					Statmp = 0;
				}
			}
        if (pc.isCrown()) {
			if (baseCon == 12 || baseCon == 13) {
				Stathp = 1;
			} else if (baseCon == 14 || baseCon == 15) {
				Stathp = 2;
			} else if (baseCon >= 16) {
				Stathp = 3;
			} else {
				Stathp = 0;
			}
		} else if (pc.isKnight()) {
			if (baseCon == 15 || baseCon == 16) {
				Stathp = 1;
			} else if (baseCon >= 17) {
				Stathp = 3;
			} else {
				Stathp = 0;
			}
		} else if (pc.isElf()) {
			if (baseCon >= 13 && baseCon <= 17) {
				Stathp = 1;
			} else if (baseCon == 18) {
				Stathp = 2;
			} else {
				Stathp = 0;
			}
		} else if (pc.isDarkelf()) {
			if (baseCon == 10 || baseCon == 11) {
				Stathp = 1;
			} else if (baseCon >= 12) {
				Stathp = 2;
			} else {
				Stathp = 0;
			}
		} else if (pc.isWizard()) {
			if (baseCon == 14 || baseCon == 15) {
				Stathp = 1;
			} else if (baseCon >= 16) {
				Stathp = 2;
			} else {
				Stathp = 0;
			}
		} else if (pc.isDragonknight()) {
			if (baseCon == 15 || baseCon == 16) {
				Stathp = 1;
			} else if (baseCon >= 17) {
				Stathp = 3;
			} else {
				Stathp = 0;
			}
		} else if (pc.isIllusionist()) {
			if (baseCon == 13 || baseCon == 14) {
				Stathp = 1;
			} else if (baseCon >= 15) {
				Stathp = 2;
			} else {
				Stathp = 0;
			}
		}
		pc.setLevel(pc.getLevel() + 1);
		Stathp = CalcStat.calcStatHp(pc.getType(), pc.getBaseMaxHp(), pc
				.getAbility().getCon());
		Statmp = CalcStat.calcStatMp(pc.getType(), pc.getBaseMaxMp(), pc
				.getAbility().getWis());
		pc.resetBaseAc();
		pc.getAC().setAc(pc.getBaseAc());
		pc.addBaseMaxHp((short) Stathp);
		pc.addBaseMaxMp((short) Statmp);
	}
}