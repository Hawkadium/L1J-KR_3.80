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

import l1j.server.Config;
import l1j.server.GameSystem.CrockSystem;
import l1j.server.GameSystem.RobotThread;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.AccessoryEnchantList;
import l1j.server.server.datatables.ArmorEnchantList;
import l1j.server.server.datatables.AutoShopBuyTable;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.DropItemTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.EvaSystemTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MapFixKeyTable;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.WeaponAddDamage;
import l1j.server.server.datatables.WeaponEnchantList;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Reload implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1Reload.class.getName());

	private L1Reload() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Reload();
	}

	public void execute(L1PcInstance gm, String cmdName, String arg) {
		if (arg.equalsIgnoreCase("���")) {
			DropTable.reload();
			gm.sendPackets(new S_SystemMessage("DropTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("���������")) {
			DropItemTable.reload();
			gm.sendPackets(new S_SystemMessage("DropItemTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("����")) {
			PolyTable.reload();
			gm.sendPackets(new S_SystemMessage("PolyTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("������")) {
			ResolventTable.reload();
			gm.sendPackets(new S_SystemMessage("ResolventTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("�ڽ�")) {
			L1TreasureBox.load();
			gm.sendPackets(new S_SystemMessage("TreasureBox Reload Complete..."));
		} else if (arg.equalsIgnoreCase("��ų")) {
			SkillsTable.reload();
			gm.sendPackets(new S_SystemMessage("Skills Reload Complete..."));
		} else if (arg.equalsIgnoreCase("����ų")) {
			MobSkillTable.reload();
			gm.sendPackets(new S_SystemMessage("MobSkills Reload Complete..."));
		} else if (arg.equalsIgnoreCase("��")) {
			MapFixKeyTable.reload();
			gm.sendPackets(new S_SystemMessage("Map Reload Complete..."));	
		} else if (arg.equalsIgnoreCase("������")) {
			ItemTable.reload();
			gm.sendPackets(new S_SystemMessage("ItemTable Reload Complete..."));
		} else if (arg.equalsIgnoreCase("����")) {
			AutoShopBuyTable.reload();
			gm.sendPackets(new S_SystemMessage("AutoShopBuyTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("����")) {
			ShopTable.reload();
			gm.sendPackets(new S_SystemMessage("ShopTable Reload Complete..."));
			
		} else if (arg.equalsIgnoreCase("���ⵥ����")) {
			WeaponAddDamage.reload();
			gm.sendPackets(new S_SystemMessage("WeaponAddDamege Update Complete..."));	
			
		} else if (arg.equalsIgnoreCase("���ڻ���")) {
			NpcShopTable.reloding();
			gm.sendPackets(new S_SystemMessage("NpcShopTable Reload Complete..."));

			
		} else if (arg.equalsIgnoreCase("���Ǿ��׼�")) { 
			NPCTalkDataTable.reload();
			gm.sendPackets(new S_SystemMessage("NpcAction Reload Complete..."));
		} else if (arg.equalsIgnoreCase("��������Ʈ")) {
			NpcSpawnTable.reload();
			gm.sendPackets(new S_SystemMessage("spawnlist Reload Complete..."));
		} else if (arg.equalsIgnoreCase("�ٹ鸮��")) {
			GetBackRestartTable.reload();
			gm.sendPackets(new S_SystemMessage("GetBackRestartTable Complete..."));
		} else if (arg.equalsIgnoreCase("�����ð�")) {
			WarTimeController.getInstance().reload();
			gm.sendPackets(new S_SystemMessage("CastleTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("���Ǿ�")) {
			NpcTable.reload();
			gm.sendPackets(new S_SystemMessage("NpcTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("������")) {
			CastleTable.reload();
			gm.sendPackets(new S_SystemMessage("CastleTable Update Complete..."));
		} else if (arg.equalsIgnoreCase("��������")) {
			Config.load();
			gm.sendPackets(new S_SystemMessage("Config Update Complete..."));
		} else if (arg.equalsIgnoreCase("�κ�")) {
			RobotThread.reload();
			gm.sendPackets(new S_SystemMessage("Robot Update Complete..."));
		} else if (arg.equalsIgnoreCase("�տ�")) {
			EvaSystemTable.reload();
			CrockSystem.reload();
			gm.sendPackets(new S_SystemMessage("TimeSystem Reload Complete..."));
		} else if (arg.equalsIgnoreCase("������æ")) {
			WeaponEnchantList.reload();
			gm.sendPackets(new S_SystemMessage("WeaponEnchantList Update Complete..."));
		} else if (arg.equalsIgnoreCase("�Ƹ���æ")) {
			ArmorEnchantList.reload();
			gm.sendPackets(new S_SystemMessage("ArmorEnchantList Update Complete..."));
		} else if (arg.equalsIgnoreCase("�Ǽ���æ")) {
			AccessoryEnchantList.reload();
			gm.sendPackets(new S_SystemMessage("AccessoryEnchantList Update Complete..."));
		} else {
			gm.sendPackets(new S_SystemMessage("\\fY--------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("   ���, ���������, ����, ������, �ڽ�, ����"));
			gm.sendPackets(new S_SystemMessage("   �տ�, ��ų, ��, ������, ����, ��������"));
			gm.sendPackets(new S_SystemMessage("   ��������Ʈ, ���Ǿ��׼�, ���Ǿ�, �ٹ鸮��"));
			gm.sendPackets(new S_SystemMessage("   ����ų, ���� , �����ð�, ����, �κ�, ������"));
			gm.sendPackets(new S_SystemMessage("   �κ�, ���Ǿ��׼�, ��������Ʈ, �ٹ鸮��"));
			gm.sendPackets(new S_SystemMessage("   ������æ, �Ƹ���æ, �Ǽ���æ"));
			gm.sendPackets(new S_SystemMessage("\\fY--------------------------------------------------"));
		}
	}
}
