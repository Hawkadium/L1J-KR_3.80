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
package l1j.server.server.serverpackets;

import java.util.ArrayList;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanMatching;
import l1j.server.server.model.L1ClanMatching.ClanMatchingList;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ClanMatching extends ServerBasePacket {
	private static final String S_ClanMatching = "[C] S_ClanMatching";

	/**
	 * type
	 * 0: ���, ����				' �Ϸ� '
	 * 1: ������, ���ֿ��Ը�
	 * 2: ��õ����, ���ΰ�ħ		' �Ϸ� ' 
	 * 3: ��û���, ���ΰ�ħ
	 * 4: ��û���, ���ΰ�ħ
	 * 5: ��û�ϱ�. 8c bb 84 10
	 * 6: ��û���.
	 */
	public S_ClanMatching(L1PcInstance pc, int type, int objid, String text1, int htype) {
		L1Clan clan = null;
		L1ClanMatching cml = L1ClanMatching.getInstance();
		String clanname = null;
		String text = null;

		writeC(Opcodes.S_OPCODE_CLANMATCHING);
		writeC(type);
		if (type == 2) { // ��õ����
			ArrayList<ClanMatchingList> _list = new ArrayList<ClanMatchingList>();
			String result = null;
			for (int i=0; i<cml.getMatchingList().size(); i++) {
				result = cml.getMatchingList().get(i)._clanname;
				if (!pc.getCMAList().contains(result)){
					_list.add(cml.getMatchingList().get(i));
				}
			}
			int type2 = 0;
			int size = _list.size();
			writeC(0x00);
			writeC(size); // ����.
			for (int i=0; i<size; i++) {
				clanname = _list.get(i)._clanname;
				text = _list.get(i)._text;
				type2 = _list.get(i)._type;
				clan = L1World.getInstance().getClan(clanname);
				writeD(clan.getClanId()); // ����ũ
				writeS(clan.getClanName()); // ���� �̸�.
				writeS(clan.getLeaderName()); // �����̸�
				writeD(clan.getOnlineMaxUser()); // ���� �Ը� : �ְ� �ִ� ������ �� 
				writeC(type2); // 0: ���, 1: ����, 2: ģ��

				if (clan.getHouseId()!=0) writeC(0x01); // ����Ʈ 0: X , 1: O
				else writeC(0x00);

				boolean inWar = false;
				List<L1War> warList = L1World.getInstance().getWarList(); // ���� ����Ʈ�� ���
				for (L1War war : warList) {
					if (war.CheckClanInWar(clanname)) { // ��ũ���� �̹� ������
						inWar = true;
						break;
					}
				}

				if (inWar) writeC(0x01); // ���� ����	0: X , 1: O
				else writeC(0x00);
				writeC(0x00); // ������.
				writeS(text);// �Ұ���Ʈ.
				writeD(clan.getClanId()); // ���� objid
			}
			_list.clear();
			_list = null;
		} else if (type == 3) { // ��û���
			int size = pc.getCMAList().size();
			int type2 = 0;
			writeC(0x00);
			writeC(size); // ����.
			
			for (int i=0; i<size; i++) {
				clanname = pc.getCMAList().get(i);
				text = cml.getClanMatchingList(clanname)._text;
				type2 = cml.getClanMatchingList(clanname)._type;
				clan = L1World.getInstance().getClan(clanname);
				writeD(clan.getClanId()); // ���� ������ �ߴ� obj��
				writeC(0x00);
				writeD(clan.getClanId()); // ����ũ.
				writeS(clan.getClanName()); // ���� �̸�.
				writeS(clan.getLeaderName()); // �����̸�
				writeD(clan.getOnlineMaxUser()); // ���� �Ը� : �ְ� �ִ� ������ �� 
				writeC(type2); // 0: ���, 1: ����, 2: ģ��

				if (clan.getHouseId()!=0) writeC(0x01); // ����Ʈ 0: X , 1: O
				else writeC(0x00);

				boolean inWar = false;
				List<L1War> warList = L1World.getInstance().getWarList(); // ���� ����Ʈ�� ���
				for (L1War war : warList) {
					if (war.CheckClanInWar(clanname)) { // ��ũ���� �̹� ������
						inWar = true;
						break;
					}
				}

				if (inWar) writeC(0x01); // ���� ����	0: X , 1: O
				else writeC(0x00);
				writeC(0x00); // ������.
				writeS(text);// �Ұ���Ʈ.
				writeD(clan.getClanId()); // ���� objid
			}
		} else if (type == 4) { // ��û���
			
			if (!cml.isClanMatchingList(pc.getClanname())) {
				writeC(0x82); // ��û ����� ������ �̰͸� ������.
			} else {
				int size = pc.getCMAList().size();
				String username = null;
				writeC(0x00);
				writeC(0x02);
				writeC(0x00);// ����
				writeC(size); // size
				L1PcInstance user = null;
				for (int i=0; i<size; i++) {
					username = pc.getCMAList().get(i);
					user = L1World.getInstance().getPlayer(username);
					if (user == null) {
						try {
							user = CharacterTable.getInstance().restoreCharacter(username);
							if (user == null) {
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					writeD(user.getId()); // ��û���� objectid
					writeC(0x00);
					writeC(user.getOnlineStatus()); // 0x01:����,  0x00:������
					writeS(username); // ��û���� �̸�.
					writeC(user.getType()); // ĳ���� Ŭ����
					writeH(user.getLawful()); // ���Ǯ
					writeC(user.getLevel()); // ����
					writeC(0x01); // �̸��տ� ������ Ǯ���� ����
				}
			}
		} else if (type == 5 || type == 6) {
			writeC(0x00);
			writeD(objid);
			writeC(htype);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_ClanMatching;
	}
}
