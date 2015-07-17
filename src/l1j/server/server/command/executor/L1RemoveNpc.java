package l1j.server.server.command.executor;


import java.util.StringTokenizer;

import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;


public class L1RemoveNpc implements L1CommandExecutor {

	private L1RemoveNpc() {}

	public static L1CommandExecutor getInstance() {
		return new L1RemoveNpc();
	}
	
	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		// TODO Auto-generated method stub
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			
			int npcid = Integer.parseInt(tok.nextToken());
			int time;

			try {
				time = Integer.parseInt(tok.nextToken());
			} catch (Exception e) {
				time = 0;
			}
			
			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc)) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;

					if (npc.getNpcId() == npcid) {
						NpcSpawnTable.getInstance().removeSpawn(npc);
						npc.setRespawn(false);
						new L1NpcDeleteTimer(npc, time * 60 * 1000).begin();
						pc.sendPackets(new S_SystemMessage(npc.getName() + "��(��) " + time + "�� �ڿ� ���� �մϴ�."));
					}
				}
			}
			
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".���� [�ð�(��)] (�þ߿� �ִ� npc�� id�� �Է��ϸ� �Է½ð� �� ����(DB���� ����) �˴ϴ�)"));
		}
	}
}
