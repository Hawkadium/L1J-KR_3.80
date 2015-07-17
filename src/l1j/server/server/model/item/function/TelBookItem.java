package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;


@SuppressWarnings("serial")
public class TelBookItem extends L1ItemInstance{
 public TelBookItem(L1Item item){ super(item); }

 @Override
 public void clickItem(L1Character cha, ClientBasePacket packet){ 
  if(cha instanceof L1PcInstance){
   L1PcInstance pc = (L1PcInstance)cha;
   L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
   int itemId = useItem.getItemId();
   int BookTel = packet.readH();
   switch(itemId){
   case 560025:
   case 560026:
   final int[][] TownAddBook = {
     { 34060, 32281, 4 },   // ����
     { 33079, 33390, 4 },   // �����
     { 32750, 32439, 4 },   // ��ũ��
     { 32612, 33188, 4 },   // ���ٿ��
     { 33720, 32492, 4 },   // ����
     { 32872, 32912, 304 }, // ħ���� ����
     { 32612, 32781, 4 },   // �۷���
     { 33067, 32803, 4 },   // ��Ʈ
     { 33933, 33358, 4 },   // �Ƶ�
     { 33601, 33232, 4 },   // ���̳�
     { 32574, 32942, 0 },   // ���ϴ� ��
     { 33430, 32815, 4 },}; // ���
   int[] TownAddBookList = TownAddBook[BookTel];
   if(TownAddBookList != null){
    L1Teleport.teleport(pc, TownAddBookList[0], TownAddBookList[1], (short)TownAddBookList[2], 3, true);
    pc.getInventory().removeItem(useItem, 1);
   }
   break;
   case 560027:
    final int[][] DungeonAddBook = {
			{ 32791, 32800, 101 }, // ����1
			{ 32764, 32842, 77 }, // ����3
			{ 32676, 32859, 59 }, // ���ٿձ�
			{ 32750, 32799, 49 }, // ���̱�
			{ 32549, 32801, 400 }, // �����
			{ 32925, 32804, 430 }, // ����
			{ 32929, 32995, 410 }, // ����
			{ 34267, 32189, 4 }, //  �׽�
			{ 32760, 33461, 4 }, // ���
			{ 32693, 32795, 450 }, // ��Ÿ���� ����
			{ 32843, 32693, 550 }, // ������ ����
	};
    int[] DungeonAddBookList = DungeonAddBook[BookTel];
    if(DungeonAddBookList != null){ 
     L1Teleport.teleport(pc, DungeonAddBookList[0], DungeonAddBookList[1], (short)DungeonAddBookList[2], 3, true); 
     pc.getInventory().removeItem(useItem, 1);
    }
    break;
   case 560028:
    final int[][] OmanTowerAddBook = {
      { 33766, 32863, 106 },    // ����6
      { 32744, 32862, 116 },    // ����16
      { 32741, 32854, 126 },    // ����26
      { 32739, 32864, 136 },    // ����36
      { 32735, 32869, 146 },    // ����46
      { 32807, 32805, 156 },    // ����56
      { 32736, 32802, 166 },    // ����66
      { 32727, 32789, 176 },    // ����76
      { 32734, 32794, 186 },    // ����86
      { 32733, 32787, 196 },};  // ����96
    int[] OmanTowerAddBookList = OmanTowerAddBook[BookTel];
    if(OmanTowerAddBookList != null){ 
     L1Teleport.teleport(pc, OmanTowerAddBookList[0], OmanTowerAddBookList[1], (short)OmanTowerAddBookList[2], 3, true); 
     pc.getInventory().removeItem(useItem, 1);
    }
    break;
    }
   }
  }
}

