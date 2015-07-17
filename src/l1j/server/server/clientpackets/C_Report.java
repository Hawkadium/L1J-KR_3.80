/*
 * make by Eva Team (http://eva.gg.gg)
 *	/�Ű� 
 * 
 */

package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.GameSystem.Antaras.AntarasRaidSystem;
import l1j.server.GameSystem.Papoo.PaPooRaidSystem;
import l1j.server.GameSystem.Rind.RindRaidSystem;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TrueTarget;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Report extends ClientBasePacket {

	private static final String C_REPORT = "[C] C_Report";

	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(C_Report.class.getName());
	public static final int MapSend = 0x0B;
	public static final int DRAGONPORTAL = 0x06;  /** �巡�� ��Ż �޴� */ 
	/** ����â ȹ������ ���� */ 
	public static final int RESET = 0x2c; 
	public static final int WEB = 0x13; 

/*	public static final int BOOKMARK_SAVE = 0x22;
    public static final int BOOKMARK_COLOR = 0x27;
	public static final int BOOKMARK_LOADING_SAVE = 0x28;*/

	
	
	 public C_Report(byte abyte0[], LineageClient client) throws Exception {
	  super(abyte0);
	  int type = readC();
	  L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
	  switch(type){
	  
	 /* case BOOKMARK_COLOR:// 27
		    int size = readD();
		    int id;
		    String name;
		    for (int i = 0; i < size; i++) {
		     id = readD();
		     name = readS();
		     Connection con = null;
		     PreparedStatement pstm = null;
		     try {
		      con = L1DatabaseFactory.getInstance().getConnection();
		      pstm = con.prepareStatement("UPDATE character_teleport SET name='" + "\\" + name + "' WHERE id='" + id + "'");
		      pstm.execute();
		     } catch (SQLException e) {
		     } finally {
		      SQLUtil.close(pstm);
		      SQLUtil.close(con);
		     }
		    }
		    break;
		   case BOOKMARK_SAVE:// 22
		    readC();// 2 ����? ����?
		    int num;
		    pc.normalbookmark_list.clear();
		    pc.speedbookmark_list.clear();
		    int size2 = pc.getBookMarkSize();
		    for (int i = 0; i < size2; i++) {
		     num = readC();
		     pc.normalbookmark_list.add(num);
		    }
		    for (int i = 0; i < 127 - size2; i++) {
		     num = readC();
		     if (num != 0xff)
		      pc.speedbookmark_list.add(num);
		     else
		      break;
		    }
		    break;
		   case BOOKMARK_LOADING_SAVE:
		    if (pc.getBookMarkSize() == 0) {
		     pc.sendPackets(new S_ServerMessage(2963));// ��� ���� ����: ���� �Ұ�(��� ��� ����� ����)
		    } else {
		     int citemid = readD();
		     L1ItemInstance SaveMarble = pc.getInventory().getItem(citemid);
		     pc.getInventory().removeItem(SaveMarble);
		     createNewItem(pc, pc.getId());
		     pc.sendPackets(new S_ServerMessage(2920));// ��� ���� ����: ��� ��� ��� ���� �Ϸ�
		    }
		    break;*/

	  
	  
	  case WEB:
		  for (L1Object obj : L1World.getInstance().getObject()) {
              if (obj instanceof L1BoardInstance) {
               L1NpcInstance board = (L1NpcInstance)obj;
               if (board.getNpcTemplate().get_npcId() == 4500300) {//�Խ��� ��ȣ
                pc.sendPackets(new S_Board(board, 0));
               }
              }
		  }
		  pc.sendPackets(new S_TrueTarget(pc.getId(), pc.getX(),pc.getY(), "Ctrl+F ĳ���� ����"));
			S_ChatPacket s_chatpacket = new S_ChatPacket(pc,"�ۼ����Ŀ����� Ctrl + F �ڽ��� Ŭ���ϼ���", Opcodes.S_OPCODE_MSG, 19); 
			pc.sendPackets(s_chatpacket);
		  
			  break;
			  
				case RESET:
				    pc.setMonsterKill(0);
				    break;
	  
	   case MapSend:
	    String targetName = null;
	    int mapid = 0, x = 0, y = 0, Mid = 0;
	//    String text = null;
	    try{
	     targetName = readS();
	     mapid = readH();
	     x = readH();
	     y = readH();
	     Mid = readH();
	    }catch(Exception e){
	     return;
	    }
	    L1PcInstance target = L1World.getInstance().getPlayer(targetName); 
	    if (target == null)
	     pc.sendPackets(new S_SystemMessage("���������� �ʰų� �߸��� ĳ�����Դϴ�."));
	    else if(pc == target)
	     pc.sendPackets(new S_SystemMessage("�ڽſ��Դ� �̴ϸ���ġ�� ������ �����ϴ�."));
	    else{
	     target.sendPackets(new S_PacketBox(pc.getName(), mapid, x, y, Mid));
	     pc.sendPackets(new S_SystemMessage(target.getName()+"�Կ��� ������ �Ϸ�Ǿ����ϴ�."));
	    }
	    break;

		case DRAGONPORTAL:
			// 1892 : �̰����� �巡�� Ű�� ����� �� �����ϴ�.
			// 1729 : ������ ����� �� �����ϴ�.
			// 1413 : ���� ���¿����� ����Ҽ� �����ϴ�.
			int itemId = readD();
			int Dragon_Type = readC();
			int Castle_Id = L1CastleLocation.getCastleIdByArea(pc);
			L1ItemInstance useItem = pc.getInventory().getItem(itemId);
			   if (useItem == null){return;}
			   if (AntarasRaidSystem.getInstance().countRaidPotal() >= 6){
				   pc.sendPackets(new S_ServerMessage(1413)); return;}
			   if (Castle_Id != 0){ pc.sendPackets(new S_ServerMessage(1892)); return; }
			   if (pc.getMap().isCombatZone(pc.getLocation())||pc.getMap().isSafetyZone(pc.getLocation())){
				   pc.sendPackets(new S_ServerMessage(1892)); return; }
			   if (pc.getMapId() >= 1005 && pc.getMapId() <= 1011){ pc.sendPackets(new S_ServerMessage(1892)); return; }
			   switch(Dragon_Type){ // 0: ��Ÿ, 1:��Ǫ, 2:����, 3:�߶�
			   case 0: AntarasRaidSystem.getInstance().startRaid(pc);   //��Ÿ 
			           pc.getInventory().consumeItem(L1ItemId.DRAGON_KEY, 1);
			           break;
			   case 1: PaPooRaidSystem.getInstance().startRaid(pc);   //��Ǫ 
			           pc.getInventory().consumeItem(L1ItemId.DRAGON_KEY, 1);
					   break; //��Ǫ
			   case 2: RindRaidSystem.getInstance().startRaid(pc);   //���巹�̵�
				       pc.getInventory().consumeItem(L1ItemId.DRAGON_KEY, 1);
			   break; //����
			   case 3: pc.sendPackets(new S_ServerMessage(1729));break; //�߶�
			   default: break; }    
			   break; default: break; } 
	  
	 }
	 


	 private void createNewItem(L1PcInstance pc, int i) {
		  L1ItemInstance item = ItemTable.getInstance().createItem(7475);
		  item.setCount(1);
		  item.set_durability(i);
		  item.setIdentified(true);
		  if (item != null && pc != null) {
		   if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
		    pc.getInventory().storeItem(item);
		    pc.getInventory().updateItem(item, L1PcInventory.COL_DURABILITY);
		   } else {
			L1World.getInstance().getInventory(pc.getX(), pc.getY(),pc.getMapId()).storeItem(item);
		   }
		   pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
		  }
		 }


	 
	 
	 @Override
	 public String getType() {
	  return C_REPORT;
	 }
	}
