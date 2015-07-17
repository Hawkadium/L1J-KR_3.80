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
package l1j.server.server.model.Instance;

import java.util.List;

import java.util.Collection;
import javolution.util.FastTable;

import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1CrownInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1CrownInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		boolean in_war = false;
		if (player.getClanid() == 0) { // ũ���̼Ҽ�
			return;
		}
		String playerClanName = player.getClanname();
		L1Clan clan = L1World.getInstance().getClan(playerClanName);
		if (clan == null) {
			return;
		}
		if (!player.isCrown()) { // ���� �̿�
			return;
		}
		if (player.getLevel() < 50) {
            player.sendPackets(new S_SystemMessage("50�� �̻� ����ĳ���� �հ��� Ŭ�� �����մϴ�"));
   return; 
        }
		if (player.getGfxId().getTempCharGfx() != 0 && // ������
				player.getGfxId().getTempCharGfx() != 1) {
			return;
		}
		if (player.getId() != clan.getLeaderId()) { // ������ �̿�
			return;
		}
		if (!checkRange(player)) { // ũ����� 1 �� �̳�
			return;
		}
		if (clan.getCastleId() != 0) {
			// ���� ũ��
			// ����� ���� ���� �����ϰ� �����Ƿ�, �ٸ� �÷θ� ���� �� �����ϴ�.
			player.sendPackets(new S_ServerMessage(474));
			return;
		}
		player.setCurrentHp(player.getMaxHp());
		new L1SkillUse().handleCommands(player, L1SkillId.ABSOLUTE_BARRIER, player.getId(), player.getX(), player.getY(), null, 0 ,L1SkillUse.TYPE_GMBUFF);


		// ũ����� ��ǥ�κ��� castle_id�� ���
		int castle_id = L1CastleLocation
				.getCastleId(getX(), getY(), getMapId());

		// �����ϰ� ������ üũ.��, ���ְ� ���� ���� ���� �ҿ�
		boolean existDefenseClan = false;
		L1Clan defence_clan = null;
		for (L1Clan defClan : L1World.getInstance().getAllClans()) {
			if (castle_id == defClan.getCastleId()) {
				// ���� ���� ũ��
				defence_clan = L1World.getInstance().getClan(
						defClan.getClanName());
				existDefenseClan = true;
				break;
			}
		}
		List<L1War> wars = L1World.getInstance().getWarList(); // ������ ����Ʈ�� ���
		for (L1War war : wars) {
			if (castle_id == war.GetCastleId()) { // �̸��̼��� ����
				in_war = war.CheckClanInWar(playerClanName);
				break;
			}
		}
		if (existDefenseClan && in_war == false) { // ���ְ� �־�, �����ϰ� ���� �ʴ� ���
			return;
		}

		// clan_data�� hascastle�� ������, ĳ���Ϳ� ũ����� ���δ�
		if (existDefenseClan && defence_clan != null) { // ���� ���� ũ���� �ִ�
			defence_clan.setCastleId(0);
			ClanTable.getInstance().updateClan(defence_clan);
			L1PcInstance defence_clan_member[] = defence_clan
					.getOnlineClanMember();
			for (int m = 0; m < defence_clan_member.length; m++) {
				if (defence_clan_member[m].getId() == defence_clan
						.getLeaderId()) { // ����
					// ����
					// ũ����
					// ����
					defence_clan_member[m].sendPackets(new S_CastleMaster(0,
							defence_clan_member[m].getId()));
					L1World.getInstance().broadcastPacketToAll(
							new S_CastleMaster(0, defence_clan_member[m]
									.getId()));
					break;
				}
			}
		} 
		//���뿡�� ���ֶ�� ����ġ�� ���ְ�
		player.setCastleIn(true);
		//
		clan.setCastleId(castle_id);
		ClanTable.getInstance().updateClan(clan);
		player.sendPackets(new S_CastleMaster(castle_id, player.getId()));
		L1World.getInstance().broadcastPacketToAll(
				new S_CastleMaster(castle_id, player.getId()));

		// ũ���� �ܸ̿� �Ÿ��� ���� �ڷ���Ʈ
		int[] loc = new int[3];
		Collection<L1PcInstance> _list = null;
		_list = L1World.getInstance().getAllPlayers();
		for (L1PcInstance pc : _list) {
			if (pc.getClanid() != player.getClanid() && !pc.isGm()) {
				if (L1CastleLocation.checkInWarArea(castle_id, pc)) { //������� �׷��� �����Ȱɸ��µ� 
					// �⳻�� �ִ�
					loc = L1CastleLocation.getGetBackLoc(castle_id); //�̰Ŷ��� �����ɷ���
					int locx = loc[0];
					int locy = loc[1];
					short mapid = (short) loc[2];
					L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
				}
			}
		}
		_list = null;
		// �޼��� ǥ��
		for (L1War war : wars) {
			if (war.CheckClanInWar(playerClanName) && existDefenseClan) {
				// ��ũ���� �����߿���, ���ְ� ����
				war.WinCastleWar(playerClanName);
				break;
			}
		}
		L1PcInstance[] clanMember = clan.getOnlineClanMember();

		if (clanMember.length > 0) {
			// ���� �����߽��ϴ�.
			S_ServerMessage s_serverMessage = new S_ServerMessage(643);
			for (L1PcInstance pc : clanMember) {
				pc.sendPackets(s_serverMessage);
			}
		}
		deleteMe();
		L1TowerInstance lt = null;
		Collection<L1Object> list2 = null;
		list2 = L1World.getInstance().getObject();
		for (L1Object l1object : list2) {
			if (l1object == null)
				continue;
			if (l1object instanceof L1TowerInstance) {
				lt = (L1TowerInstance) l1object;
				if (L1CastleLocation.checkInWarArea(castle_id, lt)) {
					lt.deleteMe();
				}
			}

		}
		// Ÿ���� spawn �Ѵ�
		L1WarSpawn warspawn = new L1WarSpawn();
		warspawn.SpawnTower(castle_id);
		//���뿡�� ����ġ�� ���ָ� �������̾���������
		player.setCastleIn(false);
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		FastTable<L1PcInstance> list = null;
		list = L1World.getInstance().getRecognizePlayer(this);
		for (L1PcInstance pc : list) {
			if (pc == null)
				continue;
			pc.getNearObjects().removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		getNearObjects().removeAllKnownObjects();
	}

	private boolean checkRange(L1PcInstance pc) {
		return (getX() - 1 <= pc.getX() && pc.getX() <= getX() + 1
				&& getY() - 1 <= pc.getY() && pc.getY() <= getY() + 1);
	}
}
