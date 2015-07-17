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

package l1j.server.server.model;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.SQLUtil;

public class L1ClanJoin {

	private static Logger _log = Logger.getLogger(L1ClanJoin.class.getName());
	private static L1ClanJoin _instance;
	public static L1ClanJoin getInstance() {
		if (_instance == null) { _instance = new L1ClanJoin(); }
		return _instance;
	}
	private L1ClanJoin() { }
	public boolean ClanJoin(L1PcInstance pc, L1PcInstance joinPc){
		int clan_id = pc.getClanid();
		String clanName = pc.getClanname();
		L1Clan clan = L1World.getInstance().getClan(clanName);
		if (clan != null) {
			int maxMember = 0;
			///////////���͸�����//////////////
			int charisma = 0;
			if(pc.getId() != clan.getLeaderId())
				charisma = pc.getAbility().getTotalCha();
			else
				charisma = getOfflineClanLeaderCha(clan.getLeaderId());
			///////////���͸�����//////////////
			boolean lv45quest = false;
			if (pc.getQuest().isEnd(L1Quest.QUEST_LEVEL45)) {
				lv45quest = true;
			}
			if (pc.getLevel() >= 50) { // Lv50 �̻�
				if (lv45quest == true) { // Lv45 ����Ʈ Ŭ��� ���� ����
					maxMember = charisma * 9;
				} else {
					maxMember = charisma * 3;
				}
			} else { // Lv50 �̸�
				if (lv45quest == true) { // Lv45 ����Ʈ Ŭ��� ���� ����
					maxMember = charisma * 6;
				} else {
					maxMember = charisma * 2;
				}
			}
			if (Config.MAX_CLAN_MEMBER > 0) { // Clan �ο����� ������ ����
				// �־�
				maxMember = Config.MAX_CLAN_MEMBER;
			}

			if (joinPc.getClanid() == 0) { // ũ���̰���
				if (maxMember <= clan.getClanMemberList().size()) {
					joinPc.sendPackets(new S_ServerMessage(188, pc.getName())); 
					return false;
				}
				for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
					clanMembers.sendPackets(new S_ServerMessage(94,joinPc.getName()));
				}

				joinPc.setClanid(clan_id);
				joinPc.setClanname(clanName);
				joinPc.setClanRank(L1Clan.CLAN_RANK_PUBLIC);
				joinPc.setTitle("");
				joinPc.sendPackets(new S_CharTitle(joinPc.getId(), ""));
				Broadcaster.broadcastPacket(joinPc, new S_CharTitle(joinPc.getId(), ""));
				try{
					joinPc.save(); // DB�� ĳ���� ������ �����Ѵ�
				} catch(Exception e) {}
				clan.addClanMember(joinPc.getName(), joinPc.getClanRank());
				pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_PLUS));
				joinPc.sendPackets(new S_ServerMessage(95, clanName)); // \f1%0
				L1Teleport.teleport(joinPc, joinPc.getX(), joinPc.getY(), joinPc.getMapId(),joinPc.getMoveState().getHeading(), false);
				// ���Ϳ�
				// �����߽��ϴ�.
			} else { // ũ�� ������ ���� ����(ũ�� ����)
				if (Config.CLAN_ALLIANCE) {
					changeClan(pc, joinPc, maxMember);
				} else {
					joinPc.sendPackets(new S_ServerMessage(89)); // \f1�����
					// ����
					// ���Ϳ�
					// �����ϰ�
					// �ֽ��ϴ�.
				}
			}
		} else {
			return false;
		}
		return true;
	}


	private void changeClan(L1PcInstance pc, L1PcInstance joinPc, int maxMember) {
		int clanId = pc.getClanid();
		String clanName = pc.getClanname();
		L1Clan clan = L1World.getInstance().getClan(clanName);
		int clanNum = clan.getClanMemberList().size();

		int oldClanId = joinPc.getClanid();
		String oldClanName = joinPc.getClanname();
		L1Clan oldClan = L1World.getInstance().getClan(oldClanName);
		int oldClanNum = oldClan.getClanMemberList().size();
		if (clan != null && oldClan != null && joinPc.isCrown()
				&& joinPc.getId() == oldClan.getLeaderId()) {
			if (maxMember < clanNum + oldClanNum) { // �� ���� ����
				joinPc.sendPackets(new S_ServerMessage(188, pc.getName())); // %0��
				// �����
				// ���Ϳ����μ�
				// �޾Ƶ���
				// ����
				// �����ϴ�.
				return;
			}
			L1PcInstance clanMember[] = clan.getOnlineClanMember();
			for (int cnt = 0; cnt < clanMember.length; cnt++) {
				clanMember[cnt].sendPackets(new S_ServerMessage(94, joinPc
						.getName())); // \f1%0��
				// ������
				// �Ͽ����μ�
				// �޾Ƶ鿩�����ϴ�.
			}

			for (int i = 0; i < oldClan.getClanMemberList().size(); i++) {
				L1PcInstance oldClanMember = L1World.getInstance().getPlayer(
						oldClan.getClanMemberList().get(i).name);
				if (oldClanMember != null) { // �¶������� ��ũ�� ���
					oldClanMember.setClanid(clanId);
					oldClanMember.setClanname(clanName);
					// ���� ���տ� ������ ���ִ� �����
					// ���ְ� ���� �� ���Ϳ��� ���޾�
					if (oldClanMember.getId() == joinPc.getId()) {
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_GUARDIAN);
					} else {
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
					}
					try {
						// DB�� ĳ���� ������ �����Ѵ�
						oldClanMember.save();
					} catch (Exception e) {
						_log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
					}
					clan.addClanMember(oldClanMember.getName(), oldClanMember
							.getClanRank());
					oldClanMember.sendPackets(new S_ServerMessage(95, clanName)); // \f1%0
					// ���Ϳ�
					// �����߽��ϴ�.
				} else { // ���� �������� ��ũ�� ���
					try {
						L1PcInstance offClanMember = CharacterTable
								.getInstance()
								.restoreCharacter(
										oldClan.getClanMemberList().get(i).name);
						offClanMember.setClanid(clanId);
						offClanMember.setClanname(clanName);
						offClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
						offClanMember.save();
						clan.addClanMember(offClanMember.getName(),
								offClanMember.getClanRank());
					} catch (Exception e) {
						_log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
					}
				}
			}
			// �������� ����
			String emblem_file = String.valueOf(oldClanId);
			File file = new File("emblem/" + emblem_file);
			file.delete();
			ClanTable.getInstance().deleteClan(oldClanName);
		}
	}
	// ������������ ���� ī������
	///////////���͸�����//////////////
	public int getOfflineClanLeaderCha(int member) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		java.sql.ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT Cha FROM characters WHERE objid=?");
			pstm.setInt(1, member);
			rs = pstm.executeQuery();
			if(!rs.next()) return 0;

			return rs.getInt("Cha");

		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return 0;
	}

}