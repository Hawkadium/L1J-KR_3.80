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

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.templates.L1NpcChat;

/** ��I��c��e AeCO����������a��I ��A��e * */
public class L1NpcChatTimer implements Runnable {// extends TimerTask {
	private static final Logger _log = Logger.getLogger(L1NpcChatTimer.class
			.getName());

	private final L1NpcInstance _npc;

	private final L1NpcChat _npcChat;

	public L1NpcChatTimer(L1NpcInstance npc, L1NpcChat npcChat) {
		_npc = npc;
		_npcChat = npcChat;
	}

	public void run() {
		try {
			if (_npc == null || _npcChat == null) {
				_npc.set_NpcChatDel();
				return;
			}

			if (_npc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE
					|| _npc._destroyed) {
				_npc.set_NpcChatDel();
				return;
			}

			int chatTiming = _npcChat.getChatTiming();
			int chatInterval = _npcChat.getChatInterval();
			boolean isShout = _npcChat.isShout();
			boolean isWorldChat = _npcChat.isWorldChat();
			String chatId1 = _npcChat.getChatId1();
			String chatId2 = _npcChat.getChatId2();
			String chatId3 = _npcChat.getChatId3();
			String chatId4 = _npcChat.getChatId4();
			String chatId5 = _npcChat.getChatId5();

			if (!chatId1.equals("")) {
				chat(_npc, chatTiming, chatId1, isShout, isWorldChat);
			}

			if (!chatId2.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId2, isShout, isWorldChat);
			}

			if (!chatId3.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId3, isShout, isWorldChat);
			}

			if (!chatId4.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId4, isShout, isWorldChat);
			}

			if (!chatId5.equals("")) {
				Thread.sleep(chatInterval);
				chat(_npc, chatTiming, chatId5, isShout, isWorldChat);
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	private void chat(L1NpcInstance npc, int chatTiming, String chatId,
			boolean isShout, boolean isWorldChat) {
		if (chatTiming == L1NpcInstance.CHAT_TIMING_APPEARANCE && npc.isDead()) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_DEAD && !npc.isDead()) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_HIDE && npc.isDead()) {
			return;
		}

		if (!isShout) {
			Broadcaster.broadcastPacket(npc,
					new S_NpcChatPacket(npc, chatId, 0));
		} else {
			Broadcaster.wideBroadcastPacket(npc, new S_NpcChatPacket(npc,
					chatId, 2));
		}

		if (isWorldChat) {
			Collection<L1PcInstance> list = L1World.getInstance()
					.getAllPlayers();
			for (L1PcInstance pc : list) {
				if (pc != null) {
					pc.sendPackets(new S_NpcChatPacket(npc, chatId, 3));
				}
				break;
			}
		}
	}

}
