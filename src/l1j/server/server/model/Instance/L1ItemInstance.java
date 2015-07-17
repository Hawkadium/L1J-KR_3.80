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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ScheduledFuture;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.CrockSystem;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.SQLUtil;
import server.controller.ItemTimerController;

// Referenced classes of package l1j.server.server.model:
// L1Object, L1PcInstance

public class L1ItemInstance extends L1Object {

	private static final long serialVersionUID = 1L;

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"MM-dd HH:mm", Locale.KOREA);

	private int _count;

	private int _itemId;

	private L1Item _item;

	private boolean _isEquipped = false;

	private int _enchantLevel;

	private int _attrenchantLevel;

	private boolean _isIdentified = false;

	private int _durability;

	private int _chargeCount;

	private int _remainingTime;

	private Timestamp _lastUsed = null;
	
	private int _pandorat;                                                                    // �ǵ��� Ƽ����

	private int bless;

	private int _lastWeight;

	private final LastStatus _lastStatus = new LastStatus();

	public L1PcInstance _pc;

	public boolean _isRunning = false;
	
	private Timestamp _buyTime = null;

	private Timestamp _endTime = null;
	
	private boolean _isPackage = false;
	////////////////////////////////////////////////////
	private int _clock;
private Timestamp _maanTime = null;
	public Timestamp getMaanTime() {
		return _maanTime;
	}

	public void setMaanTime(Timestamp t) {
		_maanTime = t;
	}
	public int getClock() {
	   return _clock;
	  } 
	  public void setClock(int clock) {
	   _clock = clock;
	  }
	  ////////////////////////////////////////////////
	private int _registlevel;// �γ�Ƽ

	public int getRegistLevel() {
		return _registlevel;
	}// �γ�Ƽ

	public void setRegistLevel(int level) {
		_registlevel = level;
	}// �γ�Ƽ
	// �ǵ��� Ƽ����
	public int getPandoraT() { return _pandorat; }
	public void setPandoraT(int pandorat) { _pandorat = pandorat; }
	///////////////���ȣ///////////////
	private int _protection; //�߰� 
	public int getProtection() { 
		return _protection; 
		}
	 public void setProtection(int protection) { 
		 _protection = protection; 
		 } //�߰�
	//////////////���ȣ////////////////

	public long OwnerTime = 0;

	public long EnchantTime = 0;

	public L1ItemInstance() {
		_count = 1;
		_enchantLevel = 0;
		_clock = 0; //�߰�
	}

	public L1ItemInstance(L1Item item, int count) {
		this();
		setItem(item);
		setCount(count);
	}

	public L1ItemInstance(L1Item item) {
		this(item, 1);
	}

	public void clickItem(L1Character cha, ClientBasePacket packet) {
	}

	public boolean isIdentified() {
		return _isIdentified;
	}

	public void setIdentified(boolean identified) {
		_isIdentified = identified;
	}

	public String getName() {
		return _item.getName();
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int count) {
		_count = count;
	}

	public boolean isEquipped() {
		return _isEquipped;
	}

	public void setEquipped(boolean equipped) {
		_isEquipped = equipped;
	}

	public L1Item getItem() {
		return _item;
	}

	public void setItem(L1Item item) {
		_item = item;
		_itemId = item.getItemId();
	}

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int itemId) {
		_itemId = itemId;
	}

	public boolean isStackable() {
		return _item.isStackable();
	}

	@Override
	public void onAction(L1PcInstance player) {
	}

	public int getEnchantLevel() {
		return _enchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		_enchantLevel = enchantLevel;
	}

	public int getAttrEnchantLevel() {
		return _attrenchantLevel;
	}

	public void setAttrEnchantLevel(int attrenchantLevel) {
		_attrenchantLevel = attrenchantLevel;
	}

	public int get_gfxid() {
		return _item.getGfxId();
	}

	public int get_durability() {
		return _durability;
	}

	public int getChargeCount() {
		return _chargeCount;
	}

	public void setChargeCount(int i) {
		_chargeCount = i;
	}

	public int getRemainingTime() {
		return _remainingTime;
	}

	public void setRemainingTime(int i) {
		_remainingTime = i;
	}

	public void setLastUsed(Timestamp t) {
		_lastUsed = t;
	}

	public Timestamp getLastUsed() {
		return _lastUsed;
	}

	public int getBless() {
		return bless;
	}

	public void setBless(int i) {
		bless = i;
	}

	public int getLastWeight() {
		return _lastWeight;
	}

	public void setLastWeight(int weight) {
		_lastWeight = weight;
	}

	public Timestamp getBuyTime() {
		return _buyTime;
	}

	public void setBuyTime(Timestamp t) {
		_buyTime = t;
	}

	public Timestamp getEndTime() {
		return _endTime;
	}

	public void setEndTime(Timestamp t) {
		_endTime = t;
	}
	public boolean isPackage() {
		return _isPackage;
	}

	public void setPackage(boolean _isPackage) {
		this._isPackage = _isPackage;
	}
	public int getsp() {
		int sp = _item.get_addsp();
		int itemid = getItemId();
		if (itemid == 134 || itemid == 134000 ) { // ������ �ڵ��ȣ
			sp += getEnchantLevel() * 1;
		}
		if (sp < 0)
			sp = 0;
		return sp;
	}
	public int getMr() {
		int mr = _item.get_mdef();
		int itemid = getItemId();
		if (itemid == 20011 || itemid == 20110 || itemid == 120011
				 || itemid == 490008 || itemid == 490017) {// �γ�Ƽ
			mr += getEnchantLevel();
		}
	///////////////////���ž������////////////////
		 else if (itemid == 76796 || itemid == 20117) {
			mr += getEnchantLevel() * 1;
		}
       ///////////////////���ž������////////////////
		///////////////////����������////////////////
		 else if (itemid == 500012) {
			mr += getEnchantLevel() * 3;
		}
       ///////////////////����������////////////////
		 else if (itemid == 20056 || itemid == 120056 || itemid == 220056) {
			mr += getEnchantLevel() * 2;
		}
		 else if (itemid == 20078) {
			mr += getEnchantLevel() * 3;
		}
		if (getItem().getGrade() == 1 && getEnchantLevel() > 5) {// ��ű� �߰�
			mr = mr + getEnchantLevel() - 5;
		}
		if (mr < 0)
			mr = 0;
		return mr;
	}

	public void set_durability(int i) {
		if (i < 0) {
			i = 0;
		}

		if (i > 127) {
			i = 127;
		}
		_durability = i;
	}

	public int getWeight() {
		if (getItem().getWeight() == 0) {
			return 0;
		} else {
			return Math.max(getCount() * getItem().getWeight() / 1000, 1);
		}
	}

	public class LastStatus {
		
		public int clock;

		public Timestamp endTime = null;

		public int count;

		public int itemId;

		public boolean isEquipped = false;

		public int enchantLevel;

		public boolean isIdentified = true;

		public int durability;

		public int chargeCount;

		public int remainingTime;

		public Timestamp lastUsed = null;

		public int bless;

		public int attrenchantLevel;

		public int registLevel;// �γ�Ƽ
		
		///////////////���ȣ///////////////
		public int protection; //�߰�
		//////////////���ȣ////////////////
		public int pandorat;                    // �ǵ��� Ƽ����

		public void updateAll() {
			count = getCount();
			itemId = getItemId();
			isEquipped = isEquipped();
			isIdentified = isIdentified();
			enchantLevel = getEnchantLevel();
			durability = get_durability();
			chargeCount = getChargeCount();
			remainingTime = getRemainingTime();
			lastUsed = getLastUsed();
			bless = getBless();
			attrenchantLevel = getAttrEnchantLevel();
			registLevel = getRegistLevel();// �γ�Ƽ
			///////////////���ȣ///////////////
			protection = getProtection(); //�߰�
			//////////////���ȣ////////////////
			pandorat = getPandoraT();                                  // �ǵ��� Ƽ����
			clock = getClock();
			endTime = getEndTime(); //�߰�
		}

		public void updateCount() {
			count = getCount();
		}

		public void updateItemId() {
			itemId = getItemId();
		}

		public void updateEquipped() {
			isEquipped = isEquipped();
		}

		public void updateIdentified() {
			isIdentified = isIdentified();
		}

		public void updateEnchantLevel() {
			enchantLevel = getEnchantLevel();
		}

		public void updateDuraility() {
			durability = get_durability();
		}

		public void updateChargeCount() {
			chargeCount = getChargeCount();
		}

		public void updateRemainingTime() {
			remainingTime = getRemainingTime();
		}

		public void updateLastUsed() {
			lastUsed = getLastUsed();
		}

		public void updateBless() {
			bless = getBless();
		}

		public void updateAttrEnchantLevel() {
			attrenchantLevel = getAttrEnchantLevel();
		}

		public void updateRegistLevel() {
			registLevel = getRegistLevel();
		}// �γ�Ƽ
		///////////////���ȣ///////////////
		public void updateProtection() { protection = getProtection(); } //�߰�
		//////////////���ȣ////////////////
		// �ǵ��� Ƽ����
		public void updatePandoraT() { pandorat = getPandoraT(); }
		public void updateClock() {
		      clock = getClock();
		  }

		  public void updateEndTime() {
		      endTime = getEndTime();
		  }   //�߰�
	}

	public LastStatus getLastStatus() {
		return _lastStatus;
	}

	public int getRecordingColumns() {
		int column = 0;
		if (getClock() != _lastStatus.clock) {
		      column += L1PcInventory.COL_CLOCK;
		  }
		if (getCount() != _lastStatus.count) {
			column += L1PcInventory.COL_COUNT;
		}
		if (getItemId() != _lastStatus.itemId) {
			column += L1PcInventory.COL_ITEMID;
		}
		if (isEquipped() != _lastStatus.isEquipped) {
			column += L1PcInventory.COL_EQUIPPED;
		}
		if (getEnchantLevel() != _lastStatus.enchantLevel) {
			column += L1PcInventory.COL_ENCHANTLVL;
		}
		if (get_durability() != _lastStatus.durability) {
			column += L1PcInventory.COL_DURABILITY;
		}
		if (getChargeCount() != _lastStatus.chargeCount) {
			column += L1PcInventory.COL_CHARGE_COUNT;
		}
		if (getLastUsed() != _lastStatus.lastUsed) {
			column += L1PcInventory.COL_DELAY_EFFECT;
		}
		if (isIdentified() != _lastStatus.isIdentified) {
			column += L1PcInventory.COL_IS_ID;
		}
		if (getRemainingTime() != _lastStatus.remainingTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		if (getBless() != _lastStatus.bless) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getAttrEnchantLevel() != _lastStatus.attrenchantLevel) {
			column += L1PcInventory.COL_ATTRENCHANTLVL;
		}
		if (getRegistLevel() != _lastStatus.registLevel) {// �γ�Ƽ
			column += L1PcInventory.COL_INNA;
		}
		///////////////���ȣ///////////////
		 if (getProtection() != _lastStatus.protection) { //�߰�
			   column += L1PcInventory.COL_PROTEC;
			  }
		//////////////���ȣ////////////////
			if (getEnchantLevel() != _lastStatus.enchantLevel
					|| getRegistLevel() != _lastStatus.registLevel
					|| getPandoraT() != _lastStatus.pandorat              // �ǵ��� Ƽ����
					) {
				column += L1PcInventory.COL_PANDORA;
			}
		return column;
	}

	public String getNumberedViewName(int count) {
		StringBuilder name = new StringBuilder(getNumberedName(count));
		int itemType2 = getItem().getType2();
		int itemId = getItem().getItemId();

		if (itemId == 40314 || itemId == 40316) {
			L1Pet pet = PetTable.getInstance().getTemplate(getId());
			if (pet != null) {
				L1Npc npc = NpcTable.getInstance().getTemplate(pet.get_npcid());
				name.append("[Lv." + pet.get_level() + " " + pet.get_name()
						+ "]");
			}
		}

		if (getItem().getType2() == 0 && getItem().getType() == 2) { // light
			if (isNowLighting()) {
				name.append(" ($10)");
			}
			if (itemId == 40001 || itemId == 40002) {
				if (getRemainingTime() <= 0) {
					name.append(" ($11)");
				}
			}
		}
		if (itemId == L1ItemId.KILLTON_CONTRACT  || itemId == L1ItemId.MERIN_CONTRACT ) { 
			   name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
			  }
		if (getItem().getItemId() == L1ItemId.TEBEOSIRIS_KEY
				|| getItem().getItemId() == L1ItemId.TIKAL_KEY) {
			name.append(" [" + CrockSystem.getInstance().OpenTime() + "]");
		}
		

		if (itemId >= 50056 && itemId <= 50058) { //����4����Ʈ
			name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
		}
		if (itemId >= 720 && itemId <= 722) { //����3��
			name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
		}

		
		if (itemId >= 500144 && itemId <= 500146) {//���������
			name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
		}
		if (itemId == 490018) {//�丮���Ǹ���
			name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
		}
					if (itemId >= 303 && itemId <= 310 || itemId >= 500031 && itemId <= 500038 || itemId == 5000297) { // ���׸��� ����, ���� �ð�ǥ��
				name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
			}

		if (itemId >= L1ItemId.MOPO_JUiCE_CONTRACT1  && itemId <= L1ItemId.MOPO_JUiCE_CONTRACT6 ||itemId >= 76767 && itemId <= 76784
				|| getItem().getItemId() == L1ItemId.DRAGON_KEY) {// �巡�� Ű
			if (isIdentified()) {
			name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
			}
		}
//		 �ð��� ������ ǥ��
		  if (itemId >= 22440 && itemId <= 22455){
		   if (isIdentified()){
		    name.append(" [" + sdf.format(getEndTime().getTime()) + "]");
		   }
		  }
		if (getItem().getItemId() == 40309) {
			name.append(" " + getRoundId() + "-" + (getTicketId() + 1));
		}

		if (isEquipped()) {
			if (itemType2 == 1) {
				name.append(" ($9)");
			} else if (itemType2 == 2 && !getItem().isUseHighPet()) {
				name.append(" ($117)");
			}
		}
		return name.toString();
	}

	public String getViewName() {
		return getNumberedViewName(_count);
	}

	public String getLogName() {
		return getNumberedName(_count);
	}

	public String getNumberedName(int count) {// �Ӽ�ǥ��
		StringBuilder name = new StringBuilder();

		if (isIdentified()) {
			if (getItem().getType2() == 1 || getItem().getType2() == 2) {
				switch (getAttrEnchantLevel()) {
				case 1:
					name.append("$6115");
					break;
				case 2:
					name.append("$6116");
					break;
				case 3:
					name.append("$6117");
					break;
				case 4:
					name.append("$6118");
					break;
				case 5:
					name.append("$6119");
					break;
				case 6:
					name.append("$6120");
					break;
				case 7:
					name.append("$6121");
					break;
				case 8:
					name.append("$6122");
					break;
				case 9:
					name.append("$6123");
					break;
				case 10:
					name.append("$6124");
					break;
				case 11:
					name.append("$6125");
					break;
				case 12:
					name.append("$6126");
					break;
				default:
					break;
				}
				if (getEnchantLevel() >= 0) {
					name.append("+" + getEnchantLevel() + " ");
				} else if (getEnchantLevel() < 0) {
					name.append(String.valueOf(getEnchantLevel()) + " ");
				}
			}
		}
		name.append(_item.getNameId());
		if (isIdentified()) {
			if (getItem().getItemId() == 20383) {
				name.append(" (" + getChargeCount() + ")");
			}
			if (getItem().getMaxUseTime() > 0 && getItem().getType2() != 0) {
				name.append(" [" + getRemainingTime() + "]");
			}
///////////////���ȣ///////////////
			 if (getProtection() == 1)
				    name.append("(��ȣ��)"); //�߰�
		}
///////////////���ȣ///////////////
		if (getItem().getMaxChargeCount() > 0) {
		    name.append(" (" + getChargeCount() + ")");
		   }
		if (count > 1) {
			name.append(" (" + count + ")");
		}

		return name.toString();
	}

	/** ������ ���·κ��� ���� ��Ŷ���� �̿��ϴ� ������ ����Ʈ���� ������, �����ش�. 
	  1: Ÿ��ġ , 2: ��æƮ ����, 3: �ջ�, 4: ��հ�, 5: ���� ����, 6: �߰� Ÿ��
	  7: ����/���� , 8: Str, 9: Dex, 10: Con, 11: Wiz, 12: Int, 13: Cha, 14: Hp,Mp
	  15: Mr, 16: �������, 17: �ּ���, 18: ���̽�Ʈȿ��, 19: Ac, 20: ���, 21: ����,
	  22: ���, 23:  ����, 24: Ȱ ����ġ, 25: ����[writeH], 26: ����[writeH], 27: �ҼӼ� 29: ���Ӽ�,
	  29: �ٶ��Ӽ�, 30: ���Ӽ�, 31: �ִ�Hp, 32: �ִ�Mp, 33: ����, 34: �������,
	  35: Ȱ Ÿ��ġ, 36: branch��dummy, 37: ü��ȸ����, 38: ����ȸ����, 39: `,*/
	public byte[] getStatusBytes() {
		int itemType2 = getItem().getType2();
		int itemId = getItemId();
		BinaryOutputStream os = new BinaryOutputStream();
		if (itemId == 40314 || itemId == 40316) {
			int a = 0;
			int b = 0;
			L1Pet pet = PetTable.getInstance().getTemplate(getId());
			if (pet != null)
				switch (pet.get_npcid()) {
				case 45043:
					a = 12;
					b = 1;
					break;
				case 45040:
					a = 215;
					b = 5;
					break;
				case 45047:
					a = 136;
					b = 3;
					break;
				case 45042:
					a = 137;
					b = 3;
					break;
				case 45054:
					a = 138;
					b = 3;
					break;
				case 45034:
					a = 139;
					b = 3;
					break;
				case 45046:
					a = 140;
					b = 3;
					break;
				case 45048:
					a = 117;
					b = 5;
					break;
				case 45053:
					a = 252;
					b = 6;
					break;
				case 45049:
					a = 10;
					b = 3;
					break;
				case 45039:
					a = 141;
					b = 10;
					break;
				case 45044:
					a = 180;
					b = 13;
					break;
				case 45711:
					a = 232;
					b = 15;
					break;
				case 45313:
					a = 234;
					b = 15;
					break;
				case 46044:
					a = 237;
					b = 15;
					break;
				case 46042:
					a = 239;
					b = 15;
					break;
				case 45688:
					a = 132;
					b = 10;
					break;
				case 45690:
					a = 133;
					b = 10;
					break;
				case 45687:
					a = 134;
					b = 10;
					break;
				case 45693:
					a = 135;
					b = 10;
					break;
				case 45694:
					a = 136;
					b = 10;
					break;
				case 45695:
					a = 137;
					b = 10;
					break;
				case 45692:
					a = 138;
					b = 10;
					break;
				case 45689:
					a = 139;
					b = 10;
					break;
				case 45696:
					a = 142;
					b = 10;
					break;
				case 45686:
					a = 143;
					b = 10;
					break;
				case 45691:
					a = 144;
					b = 10;
					break;
				case 45697:
					a = 182;
					b = 13;
					break;
				case 45712:
					a = 233;
					b = 15;
					break;
				case 45710:
					a = 235;
					b = 15;
					break;
				case 46046:
					a = 236;
					b = 15;
					break;
				case 46045:
					a = 238;
					b = 15;
					break;
				case 46043:
					a = 240;
					b = 15;
					break;
				default:
					break;
				}
			os.writeC(25);// ����
			os.writeC(a);
			os.writeC(b);
			os.writeC(26);// ����
			os.writeH(pet.get_level());
			os.writeC(31);// hp
			os.writeH(pet.get_hp());
		}
		if (itemId == 430105) {//ǳ��
		    os.writeC(39);
		    os.writeS(" ���� ��Ÿ");
		    os.writeC(39);
		    os.writeS("���� ����");
		}else if (itemId == 430104) {//����
		    os.writeC(39);
		    os.writeS(" ���� ȸ��");
		    os.writeC(39);
		    os.writeS("���� ����");
		}else if (itemId == 430107) {//ȭ��
		    os.writeC(39);
		    os.writeS(" �߰� Ÿ��");
		    os.writeC(39);
		    os.writeS("���� ����");
		}else if (itemId == 430106) {//����
		    os.writeC(39);
		    os.writeS(" ���� ȸ��");
		    os.writeC(39);
		    os.writeS("Ȧ�� ����");
		}else if (itemId == 430108) {//ź��
		    os.writeC(39);
		    os.writeS(" ���� ȸ��");
		    os.writeC(39);
		    os.writeS("���� ȸ��");
		    os.writeC(39);
		    os.writeS("Ȧ�� ����");
		    os.writeC(39);
		    os.writeS("���� ����");
		}else if (itemId == 430109) {//����
		    os.writeC(39);
		    os.writeS(" ���� ȸ��");
		    os.writeC(39);
		    os.writeS("���� ȸ��");
		    os.writeC(39);
		    os.writeS("���� ��Ÿ");
		    os.writeC(39);
		    os.writeS("Ȧ�� ����");
		    os.writeC(39);
		    os.writeS("���� ����");
		    os.writeC(39);
		    os.writeS("���� ����");
		}else if (itemId == 430110) {//����
		    os.writeC(39);
		    os.writeS(" ���� ȸ��");
		    os.writeC(39);
		    os.writeS("�߰� Ÿ��");
		    os.writeC(39);
		    os.writeS("���� ȸ��");
		    os.writeC(39);
		    os.writeS("���� ��Ÿ");
		    os.writeC(39);
		    os.writeS("Ȧ�� ����");
		    os.writeC(39);
		    os.writeS("���� ����");
		    os.writeC(39);
		    os.writeS("���� ����");
		    os.writeC(39);
		    os.writeS("���� ����");
		}else if (itemId == 437004) {//����
		    os.writeC(39);
		    os.writeS(" �߰� Ÿ�� +3");
		    os.writeC(39);
		    os.writeS("���� ���� +3");
		    os.writeC(39);
		    os.writeS("Ȱ Ÿ��ġ +3");
		    os.writeC(39);
		    os.writeS("Ȱ ����ġ +3");
		    os.writeC(39);
		    os.writeS("�ּ��� +3");
		}else if (itemId == 439117) {//�嵹
		    os.writeC(39);
		    os.writeS(" �߰� Ÿ�� +3");
		    os.writeC(39);
		    os.writeS("���� ���� +3");
		    os.writeC(39);
		    os.writeS("Ȱ Ÿ��ġ +3");
		    os.writeC(39);
		    os.writeS("Ȱ ����ġ +3");
		    os.writeC(39);
		    os.writeS("�ּ��� +3");
		}else if (itemId == 437003) {//����
		    os.writeC(39);
		    os.writeS(" �ִ� MP +40");
		    os.writeC(39);
		    os.writeS("MP ȸ�� +4");
		}else if (itemId == 437002) {//ü��
		    os.writeC(39);
		    os.writeS(" �ִ� HP +50");
		    os.writeC(39);
		    os.writeS("HP ȸ�� +4");
		}
		if (itemType2 == 0) { // etcitem
			switch (getItem().getType()) {
			case 2: // light
				os.writeC(22);
				os.writeH(getItem().getLightRange());
				break;
			case 7: // food
				os.writeC(21);
				os.writeH(getItem().getFoodVolume());
				break;
			case 0: // arrow
			case 15: // sting
				os.writeC(1);
				os.writeC(getItem().getDmgSmall());
				os.writeC(getItem().getDmgLarge());
				break;
			default:
				os.writeC(23);
				break;
			}
			os.writeC(getItem().getMaterial());
			os.writeD(getWeight());
			 if (itemId == 7475 && get_durability() > 0) {//������ �ջ���� ������ �۾��ߴ��� ����Ŀ��..;;
				    Connection con = null;
				    PreparedStatement pstm = null;
				    ResultSet rs = null;
				    String maker = null;
				    try {
				     con = L1DatabaseFactory.getInstance().getConnection();
				     pstm = con.prepareStatement("SELECT char_name FROM characters WHERE objid='" + get_durability() + "'");
				     rs = pstm.executeQuery();
				     while (rs.next())
				      maker = rs.getString("char_name");
				    } catch (SQLException e) {
				    } finally {
				     SQLUtil.close(rs, pstm, con);
				    }
				    os.writeC(39);
				    os.writeS("������:" + maker);//��Ŷ�� ����.. ���� �� �ܾ�� ���..
				   }


		} else if (itemType2 == 1 || itemType2 == 2) { // weapon | armor
	//		int safe_enchant = getItem().get_safeenchant();
	//		os.writeC(39);
	//		os.writeS("\\fY ���� ��þƮ : "+ safe_enchant);
			if (itemType2 == 1) { // weapon
				os.writeC(1);
				os.writeC(getItem().getDmgSmall());
				os.writeC(getItem().getDmgLarge());
				os.writeC(getItem().getMaterial());
				os.writeD(getWeight());
			} else if (itemType2 == 2 ) { // armor
				// AC
				os.writeC(19);
				int ac = ((L1Armor) getItem()).get_ac();
				if (ac < 0  ) ac = ac - ac - ac;
				if (getPandoraT() == 4){
					os.writeC(ac + 1); // �ǵ��� Ƽ���� ac
				} else {
					os.writeC(ac);			
				}
				os.writeC(getItem().getMaterial());
				os.writeC(getItem().getGrade());
				os.writeD(getWeight());
			}
			if (getEnchantLevel() != 0
					&& !(itemType2 == 2 && getItem().getGrade() >= 0)) {
				os.writeC(2);
				os.writeC(getEnchantLevel());
			}
			if (get_durability() != 0) {
				os.writeC(3);
				os.writeC(get_durability());
			}
			if (getItem().isTwohandedWeapon()) {
				os.writeC(4);
			}
			// ���� ����
			if (getItem().getHitModifier() != 0) {
				os.writeC(5);
				os.writeC(getItem().getHitModifier());
			}
			// �߰� Ÿ��
			if (getItem().getGrade() == 3 && getEnchantLevel() > 4) {
				os.writeC(6);
				os.writeC(getItem().getDmgModifier() + getEnchantLevel() - 4);
			} else if (getItem().getDmgModifier() != 0) {
				os.writeC(6);
				os.writeC(getItem().getDmgModifier());
			}
			if (getItem().getHitup() != 0) {
				os.writeC(5);
				os.writeC(getItem().getHitup());
			}
			if (getItem().getDmgup() != 0) {
				os.writeC(6);
				os.writeC(getItem().getDmgup());
			}
			if (getItem().getBowHitup() != 0) {
				os.writeC(24);
				os.writeC(getItem().getBowHitup());
			}

			if (getItem().getBowDmgup() != 0) {
				os.writeC(35);
				os.writeC(getItem().getBowDmgup());
			}
			if (itemId == 126 || itemId == 127 || itemId == 412002
					|| itemId == 4500091 || itemId == 4500111
					|| itemId == 450011 || itemId == 450012 || itemId == 450013) { // �������
				os.writeC(16);
			}
			if (itemId == 412001 || itemId == 112001 || itemId == 450008
					|| itemId == 450009 || itemId == 450010
					|| itemId == 4500081 || itemId == 4500101) {// �����
				os.writeC(34);
			}
			// STR~CHA
			if (getItem().get_addstr() != 0) {
				os.writeC(8);
				os.writeC(getItem().get_addstr());
			}
			if (getItem().get_adddex() != 0) {
				os.writeC(9);
				os.writeC(getItem().get_adddex());
			}
			if (getItem().get_addcon() != 0) {
				os.writeC(10);
				os.writeC(getItem().get_addcon());
			}
			if (getItem().get_addwis() != 0) {
				os.writeC(11);
				os.writeC(getItem().get_addwis());
			}
			if (getItem().get_addint() != 0) {
				os.writeC(12);
				os.writeC(getItem().get_addint());
			}
			if (getItem().get_addcha() != 0) {
				os.writeC(13);
				os.writeC(getItem().get_addcha());
			}
			if (itemId == 20108 || itemId == 420108|| itemId == 420109|| itemId == 420110|| itemId == 420111) {//����
			    os.writeC(39);
			    os.writeS("���������ǰ�ȣ");
			}else if (itemId == 20153 || itemId == 420107|| itemId == 420106|| itemId == 420105|| itemId == 420104) {//��Ǫ
			    os.writeC(39);
			    os.writeS("��Ǫ�����ǰ�ȣ");
			}else if (itemId == 20119 || itemId == 420112|| itemId == 420113|| itemId == 420114|| itemId == 420115) {//�߶�
			    os.writeC(39);
			    os.writeS("�߶�ī���ǰ�ȣ");
			}else if (itemId == 20130 || itemId == 420100|| itemId == 420101|| itemId == 420102|| itemId == 420103) {//��Ÿ
			    os.writeC(39);
			    os.writeS("��Ÿ���ǰ�ȣ");
			}
//			��Ƽ���� ������Ͱ���
			else if (itemId == 500009 && getEnchantLevel() == 1) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +10");
			    os.writeC(39);
			    os.writeS("���� ��� +3");
			}else if (itemId == 500009 && getEnchantLevel() == 2) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +15");
			    os.writeC(39);
			    os.writeS("���� ��� +4");
			}else if (itemId == 500009 && getEnchantLevel() == 3) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +20");
			    os.writeC(39);
			    os.writeS("���� ��� +5");
			}else if (itemId == 500009 && getEnchantLevel() == 4) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +24");
			    os.writeC(39);
			    os.writeS("���� ��� +6");
			}else if (itemId == 500009 && getEnchantLevel() == 5) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +28");
			    os.writeC(39);
			    os.writeS("���� ��� +7");
			    os.writeC(39);
			    os.writeS("�ּ��� +1");
			}else 	if (itemId == 500009 && getEnchantLevel() == 6) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +31");
			    os.writeC(39);
			    os.writeS("���� ��� +8");
			    os.writeC(39);
			    os.writeS("�ּ��� +1");
			}else if (itemId == 500009 && getEnchantLevel() == 7) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +34");
			    os.writeC(39);
			    os.writeS("���� ��� +9");
			    os.writeC(39);
			    os.writeS("�ּ��� +2");
			}else if (itemId == 500009 && getEnchantLevel() == 8) {
			    os.writeC(39);
			    os.writeS("�ִ� MP +36");
			    os.writeC(39);
			    os.writeS("���� ��� +10");
			    os.writeC(39);
			    os.writeS("�ּ��� +2");
			}
//			��Ƽ���� ������Ͱ���
//			��Ƽ���� �������Ͱ���
			else if (itemId == 500007 && getEnchantLevel() == 1) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +20");
			}else if (itemId == 500007 && getEnchantLevel() == 2) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +30");
			}else if (itemId == 500007 && getEnchantLevel() == 3) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +40");
			    os.writeC(39);
			    os.writeS("���������� +1");
			}else if (itemId == 500007 && getEnchantLevel() == 4) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +45");
			    os.writeC(39);
			    os.writeS("���������� +1");
			}else if (itemId == 500007 && getEnchantLevel() == 5) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +50");
			    os.writeC(39);
			    os.writeS("���������� +2");
			}else if (itemId == 500007 && getEnchantLevel() == 6) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +55");
			    os.writeC(39);
			    os.writeS("���������� +2");
			}else if (itemId == 500007 && getEnchantLevel() == 7) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +60");
			    os.writeC(39);
			    os.writeS("���������� +3");
			}else if (itemId == 500007 && getEnchantLevel() == 8) {
			    os.writeC(39);
			    os.writeS("�ִ� HP +65");
			    os.writeC(39);
			    os.writeS("���������� +3");
			}
//			��Ƽ���� �������Ͱ���
//			��Ƽ���� Ǫ�����Ͱ���
			else if (itemId == 500008 && getEnchantLevel() == 0) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +2%");
			}else if (itemId == 500008 && getEnchantLevel() == 1) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +4%");
			}else if (itemId == 500008 && getEnchantLevel() == 2) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +6%");
			}else if (itemId == 500008 && getEnchantLevel() == 3) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +8%");
			}else if (itemId == 500008 && getEnchantLevel() == 4) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +10%");
			}else if (itemId == 500008 && getEnchantLevel() == 5) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +12%");
			    os.writeC(39);
			    os.writeS("AC -1");
			}else if (itemId == 500008 && getEnchantLevel() == 6) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +14%");
			    os.writeC(39);
			    os.writeS("AC -2");
			}else if (itemId == 500008 && getEnchantLevel() == 7) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +16%");
			    os.writeC(39);
			    os.writeS("AC -2");
			}else if (itemId == 500008 && getEnchantLevel() == 8) {
			    os.writeC(39);
			    os.writeS("���� ȸ���� +18%");
			    os.writeC(39);
			    os.writeS("AC -3");
			}
//			��Ƽ���� Ǫ�����Ͱ���
			// ��ű��� ���� HP
if (getItem().getGrade() == 1 && getEnchantLevel() != 0 ){

				os.writeC(14);
				os.writeH(getItem().get_addhp() + (getEnchantLevel() * 2));
			} else if (getItem().getGrade() == 3 && getEnchantLevel() != 0) {
				os.writeC(14);
				os.writeH((getItem().get_addhp() + getEnchantLevel() * 5) + 10);
			} else if (getItem().get_addhp() != 0) {
				os.writeC(14);
				os.writeH(getItem().get_addhp());
				// �ǵ����� Ƽ���� Hp
		    }  else if (getPandoraT() == 6) {
			     os.writeC(14);
			     os.writeH(getItem().get_addhp() + 50);		    	
		    }
			// ��ű��� ���� MP
			// ��ƽ,��ƽǥ��
			if (itemType2 == 2 && getItem().getGrade() == 0
					&& getEnchantLevel() >= 6) {
				os.writeC(37);
				os.writeC(getItem().get_addhpr() + getEnchantLevel() - 5);
			} else if (getItem().get_addhpr() != 0) {
				os.writeC(37);
				os.writeC(getItem().get_addhpr());
			// �ǵ����� Ƽ���� ��ƽ
		} else if (getPandoraT() == 3) {
			os.writeC(37);
			os.writeC(getItem().get_addhpr() + 1);				
		}
			if (itemType2 == 2 && getItem().getGrade() == 0
					&& getEnchantLevel() >= 6) {
				os.writeC(38);
				os.writeC(getItem().get_addmpr() + getEnchantLevel() - 5);
			} else if (getItem().get_addmpr() != 0) {
			    if (itemId == 4678) { // �����ڵ�
			    os.writeC(38);
			    os.writeC(getItem().get_addmpr() + getEnchantLevel() * 1);
			    } else {
			    os.writeC(38);
			    os.writeC(getItem().get_addmpr());
			    }
				// �ǵ����� Ƽ���� ��ƽ
			} else if (getPandoraT() == 3){
				os.writeC(38);
				os.writeC(getItem().get_addmpr() + 1);				
			}
			
			//���.��ű��� ���� MP			 
			if (getItem().getGrade() == 2 && getEnchantLevel() != 0 ){
				os.writeC(32);
				os.writeC(getItem().get_addmp() + getEnchantLevel());
			} else if (getItem().get_addmp() != 0) {
				os.writeC(32);
				os.writeC(getItem().get_addmp());
			// �ǵ����� Ƽ���� Mp
		} else if (getPandoraT() == 7) {
			os.writeC(32);
			os.writeC(getItem().get_addmp() + 30);				
		}
			// MR
			if (getMr() != 0) {
				os.writeC(15);
				os.writeH(getMr());
				// �ǵ����� Ƽ���� Mr
			} else if (getPandoraT() == 5) {
				os.writeC(15);
				os.writeH(getMr() + 10);
			}
			// ���⿡ ǥ�úκ�
			if (getsp() != 0) {
				os.writeC(17);
				os.writeH(getsp());
			}
			// �����Ŀ� ������
			// SP
			if (getItem().get_addsp() != 0) {
				os.writeC(17);
				os.writeC(getItem().get_addsp());
			} else if (getItem().getGrade() == 2 && getEnchantLevel() > 5 ){
				os.writeC(17);
				//os.writeC(getItem().get_addsp() + (getEnchantLevel() - 5));
				os.writeC(getItem().get_addsp() + 1);//[����ȭ]�ϱ޾Ǽ��縮��2012��7��12��
			}
			if (getItem().isHasteItem()) {
				os.writeC(18);
			}

			int bit = 0;
			bit |= getItem().isUseRoyal() ? 1 : 0;
			bit |= getItem().isUseKnight() ? 2 : 0;
			bit |= getItem().isUseElf() ? 4 : 0;
			bit |= getItem().isUseMage() ? 8 : 0;
			bit |= getItem().isUseDarkelf() ? 16 : 0;
			bit |= getItem().isUseDragonKnight() ? 32 : 0;
			bit |= getItem().isUseBlackwizard() ? 64 : 0;
			bit |= getItem().isUseHighPet() ? 128 : 0;
			os.writeC(7);
			os.writeC(bit);

			if (getItem().get_defense_fire() != 0) {
				os.writeC(27);
				os.writeC(getItem().get_defense_fire());
				// �ǵ����� Ƽ���� �Ӽ� : ��
			} else if (getPandoraT() == 8) {
				os.writeC(27);
				os.writeC(getItem().get_defense_fire() + 10);				
			} else if (itemType2 == 2 && getItem().getGrade() == 0
					&& getEnchantLevel() > 0) {
				os.writeC(27);
				os.writeC(getItem().get_defense_fire() + getEnchantLevel());
			}
			// ���� �Ӽ�
			if (getItem().get_defense_water() != 0) {
				os.writeC(28);
				os.writeC(getItem().get_defense_water());
				// �ǵ����� Ƽ���� �Ӽ� : ��
			} else if (getPandoraT() == 8) {
				os.writeC(28);
				os.writeC(getItem().get_defense_water() + 10);
			} else if (itemType2 == 2 && getItem().getGrade() == 0
					&& getEnchantLevel() > 0) {
				os.writeC(28);
				os.writeC(getItem().get_defense_water() + getEnchantLevel());
			}
			 // �ٶ��� �Ӽ�
			if (getItem().get_defense_wind() != 0) {
				os.writeC(29);
				os.writeC(getItem().get_defense_wind());
				// �ǵ����� Ƽ���� �Ӽ� : �ٶ�
			} else if (getPandoraT() == 8) {
				os.writeC(29);
				os.writeC(getItem().get_defense_wind() + 10);
			} else if (itemType2 == 2 && getItem().getGrade() == 0
					&& getEnchantLevel() > 0) {
				os.writeC(29);
				os.writeC(getItem().get_defense_wind() + getEnchantLevel());
			}
			// ���� �Ӽ�
			if (getItem().get_defense_earth() != 0) {
				os.writeC(30);
				os.writeC(getItem().get_defense_earth());
				// �ǵ����� Ƽ���� �Ӽ� : ��
			} else if (getPandoraT() == 8) {
				os.writeC(30);
				os.writeC(getItem().get_defense_earth() + 10);
			} else if (itemType2 == 2 && getItem().getGrade() == 0
					&& getEnchantLevel() > 0) {
				os.writeC(30);
				os.writeC(getItem().get_defense_earth() + getEnchantLevel());
			}

			/**
			 * getMr() �߰������ �ߺ�ǥ�� ���� ���� ������ ǥ�úκ� ���� ������
			 */
			if (getItem().get_regist_freeze() != 0 && getMr() == 0) {//
				os.writeC(15);
				os.writeH(getItem().get_regist_freeze());
				os.writeC(33);
				os.writeC(1);
			}
			if (getItem().get_regist_stone() != 0 && getMr() == 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_stone());
				os.writeC(33);
				os.writeC(2);
				// �ǵ����� Ƽ���� ��ȭ ����
			} else if (getPandoraT() == 9) {
				os.writeC(15);
				os.writeH(getItem().get_regist_stone() + 10);
				os.writeC(33);
				os.writeC(2);				
			}
			if (getItem().get_regist_sleep() != 0 && getMr() == 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_sleep());
				os.writeC(33);
				os.writeC(3);
			}
			if (getItem().get_regist_blind() != 0 && getMr() == 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_blind());
				os.writeC(33);
				os.writeC(4);
			}
			if (getItem().get_regist_stun() != 0 && getMr() == 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_stun());
				os.writeC(33);
				os.writeC(5);
				// �ǵ����� Ƽ���� ���� ����
			} else if (getPandoraT() == 1){
				os.writeC(15);
				os.writeH(getItem().get_regist_stun() + 10);
				os.writeC(33);
				os.writeC(5);				
			}
			if (getItem().get_regist_sustain() != 0 && getMr() == 0) {
				os.writeC(15);
				os.writeH(getItem().get_regist_sustain());
				os.writeC(33);
				os.writeC(6);
				// �ǵ����� Ƽ���� Ȧ�� ����
			} else if (getPandoraT() == 2){
				os.writeC(15);
				os.writeH(getItem().get_regist_sustain() + 10);
				os.writeC(33);
				os.writeC(6);				
			}
			// �ǵ����� Ƽ���� �ɼ� ǥ��
			if (getPandoraT() !=0){
				os.writeC(39);
				os.writeS(pandorat());
			}
			if (getRegistLevel() != 0 && itemId == 425108 || itemId == 425107
					|| itemId == 425106 || itemId == 21028 || itemId == 21029
					|| itemId == 21030 || itemId == 21031 || itemId == 21032
					|| itemId == 21033 ||( itemId >= 490000 && itemId <= 490017)) {// �γ�Ƽ
				os.writeC(39);
				os.writeS(spirit());
			}
		}
		return os.getBytes();
	}

	private ScheduledFuture<?> _ETTimer = null;

	private ScheduledFuture<?> _ETWTimer = null;

	private int _acByMagic = 0;

	private int _hitByMagic = 0;

	private int _holyDmgByMagic = 0;

	private int _dmgByMagic = 0;

	public int getAcByMagic() {
		return _acByMagic;
	}

	public void setAcByMagic(int i) {
		_acByMagic = i;
	}

	public int getDmgByMagic() {
		return _dmgByMagic;
	}

	public void setDmgByMagic(int i) {
		_dmgByMagic = i;
	}

	public int getHolyDmgByMagic() {
		return _holyDmgByMagic;
	}

	public void setHolyDmgByMagic(int i) {
		_holyDmgByMagic = i;
	}

	public int getHitByMagic() {
		return _hitByMagic;
	}

	public void setHitByMagic(int i) {
		_hitByMagic = i;
	}
	private String spirit() {// �γ�Ƽ
		int lvl = getRegistLevel();
		String in = "";
		switch (lvl) {
		case 1:
			in = "������ ��(I)";
			break;
		case 2:
			in = "������ ��(II)";
			break;
		case 3:
			in = "������ ��(III)";
			break;
		case 4:
			in = "������ ��(IV)";
			break;
		case 5:
			in = "������ ��(V)";
			break;
		default:
			break;
		}
		return in;
	}
	// �ǵ����� Ƽ���� : �ɼ�
	private String pandorat() {
		int lvl = getPandoraT();
		String in = "";
		switch(lvl){
		case 1:
			in = "$13089";
			break;
		case 2:
			in = "$13090";
			break;
		case 3:
			in = "$13091";
			break;
		case 4:
			in = "$13092";
			break;
		case 5:
			in = "$13093";
			break;
		case 6:
			in = "$13094";
			break;
		case 7:
			in = "$13095";
			break;
		case 8:
			in = "$13080";
			break;
		case 9:
			in = "$13086";
			break;
		default:
			break;
		}
		return in;
	}
	public void setSkillArmorEnchant(L1PcInstance pc, int skillId, int skillTime) {
		int type = getItem().getType();
		int type2 = getItem().getType2();
		if (_isRunning) {
			int itemId = getItem().getItemId();
			if (pc != null && pc.getInventory().checkItem(itemId)) {
				if (type == 2 && type2 == 2 && isEquipped()) {
					pc.getAC().addAc(3);
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			setAcByMagic(0);

			_isRunning = false;

			ItemTimerController.getInstance().removeEnchant(this);
		}

		if (type == 2 && type2 == 2 && isEquipped()) {
			pc.getAC().addAc(-3);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
		setAcByMagic(3);
		_pc = pc;
		_isRunning = true;
		EnchantTime = skillTime + System.currentTimeMillis();
		ItemTimerController.getInstance().addEnchant(this);
	}

	public void setSkillWeaponEnchant(L1PcInstance pc, int skillId,
			int skillTime) {
		if (getItem().getType2() != 1) {
			return;
		}
		if (_isRunning) {

			setDmgByMagic(0);
			setHolyDmgByMagic(0);
			setHitByMagic(0);
			_isRunning = false;

			ItemTimerController.getInstance().removeEnchant(this);
		}

		switch (skillId) {
		case L1SkillId.HOLY_WEAPON:
			setHolyDmgByMagic(1);
			setHitByMagic(1);
			break;

		case L1SkillId.ENCHANT_WEAPON:
			setDmgByMagic(2);
			break;

		case L1SkillId.BLESS_WEAPON:
			setDmgByMagic(2);
			setHitByMagic(2);
			break;

		case L1SkillId.SHADOW_FANG:
			setDmgByMagic(5);
			break;

		default:
			break;
		}

		_pc = pc;
		_isRunning = true;
		EnchantTime = skillTime + System.currentTimeMillis();
		ItemTimerController.getInstance().addEnchant(this);
	}

	public void startItemOwnerTimer(L1PcInstance pc) {
		setItemOwner(pc);
		OwnerTime = 10000 + System.currentTimeMillis();
		ItemTimerController.getInstance().addOwner(this);
	}

	public L1PcInstance EquipPc = null;

	public void startEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			EquipPc = pc;
			ItemTimerController.getInstance().addEquip(this);

		}
	}

	public void stopEquipmentTimer() {
		if (getRemainingTime() > 0) {
			EquipPc = null;
			ItemTimerController.getInstance().removeEquip(this);

		}
	}

	public void cencelEquipmentTimer() {
		EquipPc = null;
		ItemTimerController.getInstance().removeEquip(this);

	}

	private L1PcInstance _itemOwner;

	public L1PcInstance getItemOwner() {
		return _itemOwner;
	}

	public void setItemOwner(L1PcInstance pc) {
		_itemOwner = pc;
	}

	private boolean _isNowLighting = false;

	public boolean isNowLighting() {
		return _isNowLighting;
	}

	public void setNowLighting(boolean flag) {
		_isNowLighting = flag;
	}

	private int _secondId;

	public int getSecondId() {
		return _secondId;
	}

	public void setSecondId(int i) {
		_secondId = i;
	}

	private int _roundId;

	public int getRoundId() {
		return _roundId;
	}

	public void setRoundId(int i) {
		_roundId = i;
	}

	private int _ticketId = -1; // Ƽ�� ��ȣ

	public int getTicketId() {
		return _ticketId;
	}

	public void setTicketId(int i) {
		_ticketId = i;
	}

	private int _DropMobId = 0;

	public int isDropMobId() {
		return _DropMobId;
	}

	public void setDropMobId(int i) {
		_DropMobId = i;
	}

	private boolean _isWorking = false;

	public boolean isWorking() {
		return _isWorking;
	}

	public void setWorking(boolean flag) {
		_isWorking = flag;
	}
	// �������� �д�üũ�ؼ� �����ϱ� ���ؼ� �߰�!!
    private long _itemdelay3;  
    public long getItemdelay3(){
     return _itemdelay3;
    } 
    public void setItemdelay3(long itemdelay3){
     _itemdelay3 = itemdelay3;
    }
	// �������� �д�üũ�ؼ� �����ϱ� ���ؼ� �߰�!!
	private int _deleteItemTime = 0;

	public int get_DeleteItemTime() {
		return _deleteItemTime;
	}

	public void add_DeleteItemTime() {
		_deleteItemTime++;
	}

	public void init_DeleteItemTime() {
		_deleteItemTime = 0;
	}
}
