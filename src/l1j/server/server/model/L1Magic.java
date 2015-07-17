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

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import static l1j.server.server.model.skill.L1SkillId.AREA_OF_SILENCE;
import static l1j.server.server.model.skill.L1SkillId.ARMOR_BRAKE;
import static l1j.server.server.model.skill.L1SkillId.BIRTH_MAAN;
import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.CALL_LIGHTNING;
import static l1j.server.server.model.skill.L1SkillId.CANCELLATION;
import static l1j.server.server.model.skill.L1SkillId.CHILL_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.CONE_OF_COLD;
import static l1j.server.server.model.skill.L1SkillId.CONFUSION;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MIRROR;
import static l1j.server.server.model.skill.L1SkillId.CURSE_BLIND;
import static l1j.server.server.model.skill.L1SkillId.CURSE_PARALYZE;
import static l1j.server.server.model.skill.L1SkillId.CURSE_POISON;
import static l1j.server.server.model.skill.L1SkillId.DARKNESS;
import static l1j.server.server.model.skill.L1SkillId.DARK_BLIND;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DISEASE;
import static l1j.server.server.model.skill.L1SkillId.DISINTEGRATE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_FALL_DOWN;
import static l1j.server.server.model.skill.L1SkillId.ENERGY_BOLT;
import static l1j.server.server.model.skill.L1SkillId.ENTANGLE;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.FAFU_MAAN;
import static l1j.server.server.model.skill.L1SkillId.FEAR;
import static l1j.server.server.model.skill.L1SkillId.FINAL_BURN;
import static l1j.server.server.model.skill.L1SkillId.FIRE_ARROW;
import static l1j.server.server.model.skill.L1SkillId.FIRE_WALL;
import static l1j.server.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static l1j.server.server.model.skill.L1SkillId.FREEZING_BLIZZARD;
import static l1j.server.server.model.skill.L1SkillId.GUARD_BREAK;
import static l1j.server.server.model.skill.L1SkillId.HORROR_OF_DEATH;
import static l1j.server.server.model.skill.L1SkillId.ICE_DAGGER;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_AVATAR;
import static l1j.server.server.model.skill.L1SkillId.JOY_OF_PAIN;
import static l1j.server.server.model.skill.L1SkillId.LIFE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.LIGHTNING;
import static l1j.server.server.model.skill.L1SkillId.LIGHTNING_STORM;
import static l1j.server.server.model.skill.L1SkillId.LIND_MAAN;
import static l1j.server.server.model.skill.L1SkillId.MANA_DRAIN;
import static l1j.server.server.model.skill.L1SkillId.MASS_SLOW;
import static l1j.server.server.model.skill.L1SkillId.MIND_BREAK;
import static l1j.server.server.model.skill.L1SkillId.MOB_BASILL;
import static l1j.server.server.model.skill.L1SkillId.MOB_COCA;
import static l1j.server.server.model.skill.L1SkillId.PANIC;
import static l1j.server.server.model.skill.L1SkillId.PAP_FIVEPEARLBUFF;
import static l1j.server.server.model.skill.L1SkillId.PAP_MAGICALPEARLBUFF;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.PHANTASM;
import static l1j.server.server.model.skill.L1SkillId.POLLUTE_WATER;
import static l1j.server.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static l1j.server.server.model.skill.L1SkillId.RETURN_TO_NATURE;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.SILENCE;
import static l1j.server.server.model.skill.L1SkillId.SLOW;
import static l1j.server.server.model.skill.L1SkillId.SMASH;
import static l1j.server.server.model.skill.L1SkillId.SPECIAL_COOKING;
import static l1j.server.server.model.skill.L1SkillId.SPECIAL_COOKING2;
import static l1j.server.server.model.skill.L1SkillId.STALAC;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;
import static l1j.server.server.model.skill.L1SkillId.STRIKER_GALE;
import static l1j.server.server.model.skill.L1SkillId.TAMING_MONSTER;
import static l1j.server.server.model.skill.L1SkillId.THUNDER_GRAB;
import static l1j.server.server.model.skill.L1SkillId.TURN_UNDEAD;
import static l1j.server.server.model.skill.L1SkillId.VAMPIRIC_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.WEAKNESS;
import static l1j.server.server.model.skill.L1SkillId.WEAPON_BREAK;
import static l1j.server.server.model.skill.L1SkillId.WIND_CUTTER;
import static l1j.server.server.model.skill.L1SkillId.WIND_SHACKLE;

import java.util.Random;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcStat;

public class L1Magic {

	private int _calcType;

	private final int PC_PC = 1;

	private final int PC_NPC = 2;

	private final int NPC_PC = 3;

	private final int NPC_NPC = 4;

	private L1PcInstance _pc = null;

	private L1PcInstance _targetPc = null;

	private L1NpcInstance _npc = null;

	private L1NpcInstance _targetNpc = null;

	private int _leverage = 13;

	private L1Skills _skill;

	private static Random _random = new Random(System.nanoTime());

	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	public L1Magic(L1Character attacker, L1Character target) {
		if (attacker instanceof L1PcInstance) {
			if (target instanceof L1PcInstance) {
				_calcType = PC_PC;
				_pc = (L1PcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = PC_NPC;
				_pc = (L1PcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		} else {
			if (target instanceof L1PcInstance) {
				_calcType = NPC_PC;
				_npc = (L1NpcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = NPC_NPC;
				_npc = (L1NpcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		}
	}

	/* ���������������� ���� ���� �Լ� ��������������� */

	@SuppressWarnings("unused")
	private int getSpellPower() {
		int spellPower = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			spellPower = _pc.getAbility().getSp();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			spellPower = _npc.getAbility().getSp();
		}
		return spellPower;
	}

	private int getMagicLevel() {
		int magicLevel = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicLevel = _pc.getAbility().getMagicLevel();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicLevel = _npc.getAbility().getMagicLevel();
		}
		return magicLevel;
	}

	private int getMagicBonus() {
		int magicBonus = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicBonus = _pc.getAbility().getMagicBonus();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicBonus = _npc.getAbility().getMagicBonus();
		}
		return magicBonus;
	}

	private int getLawful() {
		int lawful = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			lawful = _pc.getLawful();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			lawful = _npc.getLawful();
		}
		return lawful;
	}

	private int getTargetMr() {
		int mr = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			mr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			mr = _targetNpc.getResistance().getEffectedMrBySkill();
		}
		return mr;
	}

	private int getMagicHitupByArmor() {
		int HitupByArmor = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			HitupByArmor = _pc.getMagicHitupByArmor();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			HitupByArmor = 0;
		}
		return HitupByArmor;
	}

	/* ��������������� ���� ���� �������������� */
	// �ܡܡܡ� Ȯ���� ������ ���� ���� �ܡܡܡ�
	// �����
	// ������ ����Ʈ��LV + ((MagicBonus * 3) * ���� ���� ���)
	// ����� ����Ʈ��((LV / 2) + (MR * 3)) / 2
	// ���� �������������� ����Ʈ - ����� ����Ʈ
	public boolean calcProbabilityMagic(int skillId) {
		int probability = 0;
		boolean isSuccess = false;

		if (_pc != null && _pc.isGm()) {
			return true;
		}
		// Ÿ���� GM�ϰ�� ������ ����
		// if (_targetPc != null && _targetPc.isGm()) {
		// return false;
		// }

		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId >= 45912
					&& npcId <= 45915
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER)) {
				return false;
			}
			if (npcId == 45916
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_MITHRIL_POWDER)) {
				return false;
			}
			if (npcId == 45941
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER_OF_EVA)) {
				return false;
			}
			if (!_pc.getSkillEffectTimerSet().hasSkillEffect(
					STATUS_CURSE_BARLOG)
					&& (npcId == 45752 || npcId == 45753 || npcId == 7000007 || npcId == 7000008
							 || npcId == 7000009 || npcId == 70000010|| npcId == 7000011 
							 || npcId == 7000012 || npcId == 7000013 || npcId == 7000014
							 || npcId == 7000015 || npcId == 7000016 )) {////���θ���
				return false;
			}
			if (!_pc.getSkillEffectTimerSet()
					.hasSkillEffect(STATUS_CURSE_YAHEE)
					&& (npcId == 45675 || npcId == 81082 || npcId == 45625
							|| npcId == 45674 || npcId == 45685)) {
				return false;
			}

			 if(_pc.getProtect() == true && _targetPc.getProtect() == false){ //���ͺ�ȣ 
				   return false;
				   }
			if (npcId >= 46068 && npcId <= 46091
					&& _pc.getGfxId().getTempCharGfx() == 6035) {
				return false;
			}
			if (npcId >= 46092 && npcId <= 46106
					&& _pc.getGfxId().getTempCharGfx() == 6034) {
				return false;
			}
			if (npcId == 4039001
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(PAP_FIVEPEARLBUFF)) {//��������
				return false;
			}
			if (npcId == 4039002
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(PAP_MAGICALPEARLBUFF)) {//��������
				return false;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7720){ //�丣����
			return false; 
		}
		}

		if (!checkZone(skillId)) {
			return false;
		}

		if (skillId == CANCELLATION) {
			if (_calcType == PC_PC && _pc != null && _targetPc != null) {

				if(_targetPc.getMapId() == 5153 || _targetPc.getMapId() == 5001){
					_pc.sendPackets(new S_SystemMessage("\\fY��Ʋ�� �������� ĵ�������� �Ұ����մϴ�.")); 
					return false;
				}
				if (_targetPc.isInvisble()){
					return false;
				}
				if (_pc.getId() == _targetPc.getId()) {
					return true;
				}
				if (_pc.getClanid() > 0
						&& (_pc.getClanid() == _targetPc.getClanid())) {
					return true;
				}
				if (_pc.isInParty()) {
					if (_pc.getParty().isMember(_targetPc)) {
						return true;
					}
				}
				// ����� �κ��� �����϶� �˽� ��ȿ
				if (_targetPc.isInvisble()) {
					return false;
				}
				if (CharPosUtil.getZoneType(_pc) == 1
						|| CharPosUtil.getZoneType(_targetPc) == 1) {
					_pc.sendPackets(new S_SystemMessage("���� �ȿ����� ��� �Ұ����մϴ�."));
					return false;
				}
			}
			// ����� NPC, ����ڰ� NPC�� ����100% ����
			if (_calcType == PC_NPC || _calcType == NPC_PC
					|| _calcType == NPC_NPC) {
				return true;
			}
		}

		if (_calcType == PC_NPC
				&& _targetNpc.getNpcTemplate().isCantResurrect()) { // 50��
			// �̻�
			// npc
			// ����
			// �Ʒ�
			// ����
			// �Ȱɸ�:��
			// �������Ϳ���
			// ���Ұ�
			if (skillId == WEAPON_BREAK || skillId == SLOW
					|| skillId == CURSE_PARALYZE || skillId == MANA_DRAIN
					|| skillId == WEAKNESS || skillId == SILENCE
					|| skillId == DISEASE || skillId == DECAY_POTION
					|| skillId == MASS_SLOW || skillId == ENTANGLE
					|| skillId == ERASE_MAGIC || skillId == DARKNESS
					|| skillId == AREA_OF_SILENCE || skillId == WIND_SHACKLE
					|| skillId == STRIKER_GALE || skillId == RETURN_TO_NATURE
					|| skillId == FOG_OF_SLEEPING || skillId == ICE_LANCE
					|| skillId == FREEZING_BLIZZARD || skillId == POLLUTE_WATER
					|| skillId == THUNDER_GRAB) {
				return false;
			}
		}

if(_calcType == PC_PC){
      if(_targetPc.getLevel() <= Config.MAX_LEVEL || _pc.getLevel() <= Config.MAX_LEVEL){ //�űԺ�ȣ����55����
    	  _skill = SkillsTable.getInstance().getTemplate(skillId);
			if (skillId != L1Skills.TYPE_CHANGE) { // ������
    	  return false;
				}
			}
}
if(_calcType == PC_PC){
if(_targetPc.getMapId() == 777 ||_targetPc.getMapId() == 778||_targetPc.getMapId() == 779 ||_targetPc.getMapId() == 56){ //��������������̷κ���
	if (skillId != L1Skills.TYPE_CHANGE) { // ������
  	  return false;
	}
}
}
if(_calcType == PC_PC){
if(_pc.getMapId() == 777 ||_pc.getMapId() == 778||_pc.getMapId() == 779 ||_pc.getMapId() == 56){ //��������������̷κ���
	if (skillId != L1Skills.TYPE_CHANGE) { // ������
  	  return false;
	}
}
}
		// �ƽ����ε����� WB, �а��� �����̼� �̿� ��ȿ
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
				_skill = SkillsTable.getInstance().getTemplate(skillId);
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION // Ȯ����
						&& _skill.getType() != L1Skills.TYPE_HEAL // �� ��
						&& _skill.getType() != L1Skills.TYPE_CHANGE) { // ������
					return false;
				}
			}
		} else {
			if (_targetNpc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION) {
					return false;
				}
			}
		}
		// 100% Ȯ���� ������ ��ų
		if (skillId == SMASH || skillId == MIND_BREAK || skillId == IllUSION_AVATAR) {
			return true;
		}
		probability = calcProbability(skillId);
		int rnd = 0;

		switch (skillId) {
		case DECAY_POTION:
		case SILENCE:
		case CURSE_PARALYZE:
		case CANCELLATION:
		case SLOW:
		case DARKNESS:
		case WEAKNESS:
		case CURSE_POISON:
		case CURSE_BLIND:
		case WEAPON_BREAK:
		case MANA_DRAIN:
			if (_calcType == PC_PC) {
				rnd = _random.nextInt(_targetPc.getResistance()
						.getEffectedMrBySkill()) + 1;
			} else if (_calcType == PC_NPC) {
				rnd = _random.nextInt(_targetNpc.getResistance()
						.getEffectedMrBySkill()) + 1;
			} else {
				rnd = _random.nextInt(100) + 1;
			}
			break;
		default:
			rnd = _random.nextInt(100) + 1;
			if (probability > 90)
				probability = 90;
			break;
		}
		
		if (probability + getMagicHitupByArmor() >= rnd) {
			isSuccess = true;
		} else {
			isSuccess = false;
		}

		if (!Config.ALT_ATKMSG) {
			return isSuccess;
		}
		if (Config.ALT_ATKMSG) {
			if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
				return isSuccess;
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC)
					&& !_targetPc.isGm()) {
				return isSuccess;
			}
		}

		  String msg0 = "";
		 // String msg1 = " -> ";
		  String msg2 = "";
		  String msg3 = "";
		  String msg4 = "";

		  if (_calcType == PC_PC || _calcType == PC_NPC) {
		    
		   msg0 = _pc.getName();
		  } else if (_calcType == NPC_PC) { 
		   msg0 = _npc.getName();
		  }

		  msg2 = "Ȯ��:" + probability + "%";
		  if (_calcType == NPC_PC || _calcType == PC_PC) { 
		   msg4 = _targetPc.getName();
		  } else if (_calcType == PC_NPC) { 
		   msg4 = _targetNpc.getName();
		  }
		  if (isSuccess == true) {
		   msg3 = "����";
		  } else {
		   msg3 = "����";
		  }

		  if (_calcType == PC_PC || _calcType == PC_NPC) { 
		   _pc.sendPackets(new S_SystemMessage("\\fR["+msg0+"->"+msg4+"] "+msg2+" / "+msg3));
		  }
		  if (_calcType == NPC_PC || _calcType == PC_PC) { 
		   _targetPc.sendPackets(new S_SystemMessage("\\fY["+msg0+"->"+msg4+"] "+msg2+" / "+msg3));
		  }

		  return isSuccess;
		 }

	private boolean checkZone(int skillId) {
		if (_pc != null && _targetPc != null) {
			if (CharPosUtil.getZoneType(_pc) == 1
					|| CharPosUtil.getZoneType(_targetPc) == 1) {
				if (skillId == WEAPON_BREAK || skillId == SLOW
						|| skillId == CURSE_PARALYZE || skillId == MANA_DRAIN
						|| skillId == DARKNESS || skillId == WEAKNESS
						|| skillId == DISEASE || skillId == DECAY_POTION
						|| skillId == MASS_SLOW || skillId == ENTANGLE
						|| skillId == ERASE_MAGIC || skillId == EARTH_BIND
						|| skillId == AREA_OF_SILENCE
						|| skillId == WIND_SHACKLE || skillId == STRIKER_GALE
						|| skillId == SHOCK_STUN || skillId == FOG_OF_SLEEPING
						|| skillId == ICE_LANCE || skillId == FREEZING_BLIZZARD
						|| skillId == HORROR_OF_DEATH
						|| skillId == POLLUTE_WATER || skillId == FEAR
						|| skillId == ELEMENTAL_FALL_DOWN
						|| skillId == GUARD_BREAK
						|| skillId == RETURN_TO_NATURE || skillId == PHANTASM
						|| skillId == JOY_OF_PAIN || skillId == CONFUSION
						|| skillId == SILENCE) {
					return false;
				}
			}
		}
		return true;
	}

	private int calcProbability(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int attackLevel = 0;
		int defenseLevel = 0;
		int probability = 0;
		int attackInt = 0;
		int defenseMr = 0;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			attackLevel = _pc.getLevel();
			attackInt = _pc.getAbility().getTotalInt();
		} else {
			attackLevel = _npc.getLevel();
			attackInt = _npc.getAbility().getTotalInt();
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			defenseLevel = _targetPc.getLevel();
			defenseMr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			defenseLevel = _targetNpc.getLevel();
			defenseMr = _targetNpc.getResistance().getEffectedMrBySkill();
			if (skillId == RETURN_TO_NATURE) {
				if (_targetNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _targetNpc;
					defenseLevel = summon.getMaster().getLevel();
				}
			}
		}

		switch (skillId) {
		case ERASE_MAGIC:// �̷�
		case EARTH_BIND:// ���
		case ARMOR_BRAKE:// �ƸӺ극��ũ
		case STRIKER_GALE:// ����
			int levelbonus = 0;
			probability = (int) (l1skills.getProbabilityValue() + (attackLevel - defenseLevel) * 2);
			if (attackLevel >= defenseLevel) {
				levelbonus = (attackLevel - defenseLevel) * 3;
			} else {
				levelbonus = -(defenseLevel - attackLevel) * 2;
			}
			probability += levelbonus;
			break;
		case ELEMENTAL_FALL_DOWN:
		case RETURN_TO_NATURE:
		case ENTANGLE:
		case AREA_OF_SILENCE:
		case WIND_SHACKLE:
		case POLLUTE_WATER:
			probability = (int) (30 + (attackLevel - defenseLevel) * 2);
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2 * _pc.getBaseMagicHitUp();
			}
			break;
		case SHOCK_STUN:// ����Ȯ��
			probability = (int) (l1skills.getProbabilityValue() + (attackLevel - defenseLevel) * 2.5);
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2.5 * _pc.getBaseMagicHitUp();
			}
			break;
		case COUNTER_BARRIER:// ī���͹踮�����20%Ȯ��
			probability = (int) 20;
			break;
		case THUNDER_GRAB:
			probability = 60;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2 * _pc.getBaseMagicHitUp();
			}
			break;
		case PANIC:
			probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel))
					+ l1skills.getProbabilityValue();
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2 * _pc.getBaseMagicHitUp();
			}
			break;
		case CANCELLATION: // �˽�����ȭ
		case DECAY_POTION:// ������
		case ICE_LANCE: // ���̽�
		case CURSE_PARALYZE:// Ŀ��
			if (attackInt > 25)
				attackInt = 25;
			probability = (int) ((attackInt - (defenseMr / 5.74)) * 4);

			if (_pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
				probability -= 30;
			}
			if (probability < 0)
				probability = 0;
			break;
		case SILENCE:
		case SLOW:
		case DARKNESS:
		case WEAKNESS:
		case CURSE_POISON:
		case CURSE_BLIND:
		case WEAPON_BREAK:
			if (attackInt > 25)
				attackInt = 25;
			probability = (int) ((attackInt - (defenseMr / 5.75)) * l1skills
					.getProbabilityValue());
			if (_pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
				probability /= 2;
			}
			if (probability < 0)
				probability = 0;
			break;
		case MANA_DRAIN:
			if (attackInt > 25)
				attackInt = 25;
			probability = (int) (attackInt - (defenseMr / 5.7))
					* l1skills.getProbabilityValue();
			if (probability < 0)
				probability = 2;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += _pc.getBaseMagicHitUp();
			}
			break;
		case TURN_UNDEAD:
			if (attackInt > 25)
				attackInt = 25;
			if (attackLevel > 52)
				attackLevel = 52; // ������ȭ�� ���� 52�� ����(�⺻�� 49��)
			probability = (int) ((attackInt * 3 + (attackLevel * 2.5) + _pc
					.getBaseMagicHitUp()) - (defenseMr + (defenseLevel / 2)) - 80);
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (!_pc.isWizard()) {
					probability -= 30;
				}
			}
			break; // �߰� �Ͼ𵥵� ����ȭ ^^
		case GUARD_BREAK:
		case FEAR:
		case HORROR_OF_DEATH:
			Random random = new Random();
			int dice = l1skills.getProbabilityDice();
			int value = l1skills.getProbabilityValue();
			int diceCount = 0;
			diceCount = getMagicBonus() + getMagicLevel();

			if (diceCount < 1) {
				diceCount = 1;
			}

			for (int i = 0; i < diceCount; i++) {
				probability += (random.nextInt(dice) + 1 + value);
			}

			probability = probability * getLeverage() / 10;

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2 * _pc.getBaseMagicHitUp();
			}

			if (probability >= getTargetMr()) {
				probability = 100;
			} else {
				probability = 0;
			}
			break;
		case CONFUSION:
		case BONE_BREAK:
		case PHANTASM:
			probability = 80;
			break;
		case JOY_OF_PAIN:
			probability = 80;
			break;
		default: {
			int dice1 = l1skills.getProbabilityDice();
			int diceCount1 = 0;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.isWizard()) {
					diceCount1 = getMagicBonus() + getMagicLevel() + 1;
				} else if (_pc.isElf()) {
					diceCount1 = getMagicBonus() + getMagicLevel() - 1;
				} else if (_pc.isDragonknight()) {
					diceCount1 = getMagicBonus() + getMagicLevel();
				} else {
					diceCount1 = getMagicBonus() + getMagicLevel() - 1;
				}
			} else {
				diceCount1 = getMagicBonus() + getMagicLevel();
			}
			if (diceCount1 < 1) {
				diceCount1 = 1;
			}
			if (dice1 > 0) {
				for (int i = 0; i < diceCount1; i++) {
					probability += (_random.nextInt(dice1) + 1);
				}
			}
			probability = probability * getLeverage() / 10;
			probability -= getTargetMr();

			if (skillId == TAMING_MONSTER) {
				double probabilityRevision = 1;
				if ((_targetNpc.getMaxHp() * 1 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.3;
				} else if ((_targetNpc.getMaxHp() * 2 / 4) > _targetNpc
						.getCurrentHp()) {
					probabilityRevision = 1.2;
				} else if ((_targetNpc.getMaxHp() * 3 / 4) > _targetNpc
						.getCurrentHp()) {
					probabilityRevision = 1.1;
				}
				probability *= probabilityRevision;
			}
		}
			break;
		}

		// / ������ ���� Ȯ�� ���� ///
		switch (skillId) {
		case EARTH_BIND:
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				probability -= _targetPc.getResistance().getHold();
			}
			break;
		case SHOCK_STUN:
		case 30081:
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				probability -= 1 * _targetPc.getResistance().getStun();
			}
			break;
		case CURSE_PARALYZE:
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				probability -= _targetPc.getResistance().getPetrifaction();
			}
			break;
		case FOG_OF_SLEEPING:
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				probability -= _targetPc.getResistance().getSleep();
			}
			break;
		case ICE_LANCE:
		case FREEZING_BLIZZARD:
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				probability -= _targetPc.getResistance().getFreeze();
			}
			break;
		case CURSE_BLIND:
		case DARKNESS:
		case DARK_BLIND:
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				probability -= _targetPc.getResistance().getBlind();
			}
			break;
		// / ������ ���� Ȯ�� ���� ///
		default:
			break;
		}
		return probability;
	}

	/* ��������������� ���� ������ ���� ��������������� */

	public int calcMagicDamage(int skillId) {
		int damage = 0;
		int rand = 0; // ���������� ����
		int disd = 0; // �����Ŀ��� ���� ���������� ����
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			damage = calcPcMagicDamage(skillId);
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			damage = calcNpcMagicDamage(skillId);
			// ////////�������Դ� �������� ������ 0 �����߰�////////////
			if (_calcType == PC_PC) {
				if (_pc.getClanid() > 0
						&& (_pc.getClanid() == _targetPc.getClanid())) {
					if (skillId == 17 || skillId == 22 || skillId == 25
							|| skillId == 53 || skillId == 59 || skillId == 62
							|| skillId == 65 || skillId == 70 || skillId == 74
							|| skillId == 80) { // ��Ƽ�� ������
						// ����������..
						damage = 0;
					}
					if (_calcType == PC_PC) {
						if (_targetPc.getLevel() <= Config.MAX_LEVEL
								|| _pc.getLevel() <= Config.MAX_LEVEL) { // �űԺ�ȣ����55����
							damage = 0;
						}
					}
					if (_calcType == PC_PC) {
						if (_targetPc.getMapId() == 777
								|| _targetPc.getMapId() == 778
										|| _targetPc.getMapId() == 56
								|| _targetPc.getMapId() == 779) { // ��������������̷κ���
							damage = 0;
						}
					}
					if (_calcType == PC_PC) {
						if (_pc.getMapId() == 777 || _pc.getMapId() == 778 || _pc.getMapId() == 56
								|| _pc.getMapId() == 779) { // ��������������̷κ���
							damage = 0;
						}
					}
					if (_calcType == PC_PC) {
						if (_pc.getProtect() == true
								&& _targetPc.getProtect() == false) { // ���ͺ�ȣ
							damage = 0;
						}
					}
				}
			}
			if (skillId == 46 || skillId == 34 || skillId == 45) {// ��������.�̷�.�ݶ��ȭ
				if (_pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
					damage /= 2;
				}
			}
			// ////////�������Դ� �������� ������ 0 �����߰���////////////
			// //////// �� �ּ� ������ ���� ///////////
			if (_calcType == PC_PC) {
				// ������ MR�� 160���� ũ�� ���� hp�� 0���� Ŭ�ÿ� �ߵ��˴ϴ�.
				if (_pc.getCurrentHp() > 0
						&& (_targetPc.getResistance().getMr() > 160)) {
					if (skillId == 77) {
						// ��Ÿ���� �ҽ� �߰�
						disd = _pc.getAbility().getSp();
						if (disd > 58) {
							disd *= 3.2;
						} else {
							disd *= 1.8;
						}
						Random random5 = new Random();
						rand = random5.nextInt(50) + 1;
						damage = 330 + rand + disd;
						// ������ �������̱� ������ ������ �ƴ��� ��Ȯ���� �ʿ���
						if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
								IMMUNE_TO_HARM)) {
							damage -= damage * 0.3;
						}
					}
				}
			}
			// //////// �� �ּ� ������ ���� ///////////
		}
		/** �Ĺ��� ���� ���� ���� (�ӽ�) */
		if (skillId == FINAL_BURN && _targetPc != null) { // final
			// burn's
			// temporary
			// damage
			if (_targetPc.getResistance().getEffectedMrBySkill() <= 50)
				damage = _pc.getCurrentMp()
						+ _random.nextInt(_pc.getCurrentMp() / 2 + 1);
			else if (_targetPc.getResistance().getEffectedMrBySkill() > 50
					&& _targetPc.getResistance().getEffectedMrBySkill() < 100)
				damage = _pc.getCurrentMp()
						- _random.nextInt(_pc.getCurrentMp() / 2 + 1);
			else if (_targetPc.getResistance().getEffectedMrBySkill() > 100)
				damage = _random.nextInt(_pc.getCurrentMp() / 2 + 1);
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (damage > _targetPc.getCurrentHp()) {
				damage = _targetPc.getCurrentHp();
			}
		} else {
			if (damage > _targetNpc.getCurrentHp()) {
				damage = _targetNpc.getCurrentHp();
			}
		}
		if (_calcType == PC_PC) {
			if (_targetPc.getRobotAi() != null && (_targetPc.noPlayerCK || _targetPc.isGm())) {
				if (_targetPc != null && _targetPc.getClanid() != 0 && !_targetPc.getMap().isSafetyZone(_targetPc.getLocation())) {
					if (_targetPc.getClanid() != _pc.getClanid()) {
						_targetPc.getRobotAi().getAttackList().add(_pc, 0);
					}
				} else if (!_targetPc.getMap().isSafetyZone(_targetPc.getLocation())) {
					if (_targetPc.getMap().isTeleportable()) {
						L1Location newLocation = _targetPc.getLocation().randomLocation(200, true);
						int newX = newLocation.getX();
						int newY = newLocation.getY();
						short mapId = (short) newLocation.getMapId();

						L1Teleport.teleport(_targetPc, newX, newY, mapId, _targetPc.getMoveState().getHeading(), true);
					}
				}
			}
		}


		return damage;
	}

	// �ܡܡܡ� �÷��̾�� ���̾���� ���� ������ ���� �ܡܡܡ�
	public int calcPcFireWallDamage() {
		int dmg = 0;
		double attrDeffence = calcAttrResistance(L1Skills.ATTR_FIRE);
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = (int) ((1.0 - attrDeffence) * l1skills.getDamageValue());

		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(ABSOLUTE_BARRIER)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
						FREEZING_BLIZZARD)
				|| _targetPc.getSkillEffectTimerSet()
						.hasSkillEffect(EARTH_BIND)
				|| _targetPc.getSkillEffectTimerSet()
						.hasSkillEffect(MOB_BASILL)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)) {
			dmg = 0;
		}

		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	// �ܡܡܡ� NPC ���� ���̾���� ���� ������ ���� �ܡܡܡ�
	public int calcNpcFireWallDamage() {
		int dmg = 0;
		double attrDeffence = calcAttrResistance(L1Skills.ATTR_FIRE);
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = (int) ((1.0 - attrDeffence) * l1skills.getDamageValue());

		if (_targetNpc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
						FREEZING_BLIZZARD)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
						EARTH_BIND)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
						MOB_BASILL)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)) {
			dmg = 0;
		}

		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	// �ܡܡܡ� �÷��̾NPC �κ��� �÷��̾�� ���� ������ ���� �ܡܡܡ�
	private int calcPcMagicDamage(int skillId) {
		int dmg = 0;
		if (skillId == FINAL_BURN) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				dmg = _pc.getCurrentMp();
			} else {
				dmg = _npc.getCurrentMp();
			}
		} else {
			dmg = calcMagicDiceDamage(skillId);
			// if (_calcType == PC_PC) {
			// dmg = (dmg * getLeverage()) / 15; // �÷��̾�� ���� �޴� ���� ������
			if (_calcType == NPC_PC) {
				dmg = (dmg * getLeverage()) / 15; // ���ͷ� ���� �޴� ���� ������
			}
		}
		dmg -= _targetPc.getDamageReductionByArmor();

		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING)) { // ����ȿ丮��
			// ����
			// ������
			// �氨
			dmg -= 5;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING2)) {
			// ���ο� �丮�� ���� ������ ������ȿ��
			dmg -= 2;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 50) {
				targetPcLvl = 50;
			}
			dmg -= (targetPcLvl - 50) / 5 + 2;
		}

		if (_calcType == NPC_PC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
			if (castleId > 0) {
				isNowWar = WarTimeController.getInstance().isNowWar(castleId);
			}
			if (_npc instanceof L1MonsterInstance) {// ����������
				if (_npc.getMaxHp() > 1000)
					dmg -= 0.8;
			}
			if (!isNowWar) {
				if (_npc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_npc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _npc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
			// Object[] dollList = _targetPc.getDollList().values().toArray();
			// // ���� ������ ���� �߰� ���
			// L1DollInstance doll = null;
			for (L1DollInstance doll : _targetPc.getDollList().values()) {
				// doll = (L1DollInstance) dollObject;
				dmg -= doll.getDamageReductionByDoll();
			}
		}
		/** ������ �����и� �����ߴٸ� * */
		if (_targetPc.getInventory().checkEquipped(20230) || // �ӹ�
				_targetPc.getInventory().checkEquipped(20229)) {// �ݻ����

			Random random = new Random(System.nanoTime());
			int rnd = random.nextInt(100) + 1;
			if (rnd < 4) {
				switch (skillId) {
				case ENERGY_BOLT:// ����
				case LIGHTNING:// ����Ʈ��
				case CALL_LIGHTNING:// �ݶ�
				case LIGHTNING_STORM:// ����
				case DISINTEGRATE:// ��
				}
				dmg -= 100;
				_targetPc
						.sendPackets(new S_SystemMessage("���з� ��� ������ ��ȣ�޾ҽ��ϴ�."));
			}
		}
		/** ������ �����и� �����ߴٸ� * */
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_AVATAR)) {
			dmg += dmg / 5;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(PATIENCE)) {
			dmg -= 2;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(DRAGON_SKIN)) {
			dmg -= 2;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg /= 1.4;
		}
		if (_targetPc.getInventory().checkEquipped(420012)
				|| _targetPc.getInventory().checkEquipped(500042)) {// ���.����
			dmg -= 1;
		}
		if (_targetPc.getInventory().checkEquipped(20117)) {// ��������
			dmg -= 2;
		}
		if (_targetPc.getInventory().checkEquipped(500040)) {// �ݿ����ǹ���
			dmg -= 3;
		}
		if (_calcType == PC_PC) {
			int chance = _random.nextInt(100);
			if (_targetPc.getInventory().checkEquipped(500040)) { // �ݿ����ǹ���
				if (chance <= 2) { // Ȯ�� 2
					dmg /= 2; // ������ ���� 3
				}
			}
		}
		if (_calcType == PC_PC) {
			if (_targetPc.getLevel() <= Config.MAX_LEVEL
					|| _pc.getLevel() <= Config.MAX_LEVEL) { // �űԺ�ȣ����55����
				dmg = 0;
			}
		}
		if (_calcType == PC_PC) {
			if (_targetPc.getMapId() == 777 || _targetPc.getMapId() == 778 || _targetPc.getMapId() == 56
					|| _targetPc.getMapId() == 779) { // ��������������̷κ���
				dmg = 0;
			}
		}
		if (_calcType == PC_PC) {
			if (_pc.getMapId() == 777 || _pc.getMapId() == 778 || _pc.getMapId() == 56
					|| _pc.getMapId() == 779) { // ��������������̷κ���
				dmg = 0;
			}
		}
		// [���ȿ����� ��������]����.����.����.ź��
		if (_targetPc != null) {
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(FAFU_MAAN)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							LIFE_MAAN)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							SHAPE_MAAN)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							BIRTH_MAAN)) {
				int chance = _random.nextInt(100) + 1;
				if (chance <= 20) {
					dmg /= 1.5;
				}
			}
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(MOB_BASILL)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
						FREEZING_BLIZZARD)
				|| _targetPc.getSkillEffectTimerSet()
						.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(COUNTER_MIRROR)) {
			if (_calcType == PC_PC) {
				if (_targetPc.getAbility().getTotalWis() >= _random
						.nextInt(100)) {
					_pc.sendPackets(new S_DoActionGFX(_pc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(_pc,
							new S_DoActionGFX(_pc.getId(),
									ActionCodes.ACTION_Damage));
					_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(),
							4395));
					Broadcaster.broadcastPacket(_targetPc, new S_SkillSound(
							_targetPc.getId(), 4395));
					dmg = dmg / 2;
					_pc.receiveDamage(_targetPc, dmg, false);
					dmg = 0;
					_targetPc.getSkillEffectTimerSet().killSkillEffectTimer(
							COUNTER_MIRROR);
				}
			} else if (_calcType == NPC_PC) {
				int npcId = _npc.getNpcTemplate().get_npcId();
				if (npcId == 45681 || npcId == 45682 || npcId == 45683
						|| npcId == 45684) {
				} else if (!_npc.getNpcTemplate().get_IsErase()) {
				} else {
					if (_targetPc.getAbility().getTotalWis() >= _random
							.nextInt(100)) {
						Broadcaster.broadcastPacket(_npc, new S_DoActionGFX(
								_npc.getId(), ActionCodes.ACTION_Damage));
						_targetPc.sendPackets(new S_SkillSound(_targetPc
								.getId(), 4395));
						Broadcaster.broadcastPacket(_targetPc,
								new S_SkillSound(_targetPc.getId(), 4395));
						_npc.receiveDamage(_targetPc, dmg);
						dmg = 0;
						_targetPc.getSkillEffectTimerSet()
								.killSkillEffectTimer(COUNTER_MIRROR);
					}
				}
			}
		}

		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	// �ܡܡܡ� �÷��̾NPC �κ��� NPC ���� ������ ���� �ܡܡܡ�
	private int calcNpcMagicDamage(int skillId) {
		int dmg = 0;
		if (skillId == FINAL_BURN) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				dmg = _pc.getCurrentMp();
			} else {
				dmg = _npc.getCurrentMp();
			}
		} else {
			dmg = calcMagicDiceDamage(skillId);
			dmg = (dmg * getLeverage()) / 10;
		}
		if (_targetNpc.getNpcId() == 45640) {
			dmg /= 2;
		}
		if (_calcType == PC_NPC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
			if (castleId > 0) {
				isNowWar = WarTimeController.getInstance().isNowWar(castleId);
			}
			if (!isNowWar) {
				if (_targetNpc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_targetNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _targetNpc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
		}

		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId >= 45912
					&& npcId <= 45915
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER)) {
				dmg = 0;
			}
			if (npcId == 45916
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_MITHRIL_POWDER)) {
				dmg = 0;
			}
			if (npcId == 45941
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER_OF_EVA)) {
				dmg = 0;
			}
			if (!_pc.getSkillEffectTimerSet().hasSkillEffect(
					STATUS_CURSE_BARLOG)
					&& (npcId == 45752 || npcId == 45753 || npcId == 7000007
							|| npcId == 7000008 || npcId == 7000009
							|| npcId == 70000010 || npcId == 7000011
							|| npcId == 7000012 || npcId == 7000013
							|| npcId == 7000014 || npcId == 7000015 || npcId == 7000016)) {
				dmg = 0;
			}
			// //////////////////////////////¬��ĳ��//////////////////////////////
			if (!_pc.getSkillEffectTimerSet()
					.hasSkillEffect(STATUS_CURSE_YAHEE)
					&& (npcId == 45675 || npcId == 81082 || npcId == 45625
							|| npcId == 45674 || npcId == 45685)) {
				dmg = 0;
			}
			if (npcId >= 46068 && npcId <= 46091
					&& _pc.getGfxId().getTempCharGfx() == 6035) {
				dmg = 0;
			}
			if (npcId >= 46092 && npcId <= 46106
					&& _pc.getGfxId().getTempCharGfx() == 6034) {
				dmg = 0;
			}
			if (npcId == 4039001
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							PAP_FIVEPEARLBUFF)) {
				dmg = 0;
			}// ��������
			if (npcId == 4039002
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							PAP_MAGICALPEARLBUFF)) {
				dmg = 0;
			}// ��������
		}
		return dmg;
	}

	// �ܡܡܡ� damage_dice, damage_dice_count, damage_value, SP�κ��� ���� �������� ���� �ܡܡܡ�
	private int calcMagicDiceDamage(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int diceCount = l1skills.getDamageDiceCount();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;
		double PowerMr = 0; // ����
		double coefficient = 0; // PC�������

		Random random = new Random();
		for (int i = 0; i < diceCount; i++) {
			magicDamage += (_random.nextInt(dice) + 1);
		}
		magicDamage += value;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int PowerSp = _pc.getAbility().getSp() - getMagicLevel(); // ��SP����
																		// �������ʽ���
																		// ������SP���ϱ�
																		// +1��
																		// ���Ŀ���
																		// �ٷ�
																		// �־����ϴ�.

			int PowerInt = _pc.getAbility().getTotalInt() - getMagicBonus(); // SP��
																				// �������ʴ�
																				// INT
																				// ���ϱ�
																				// -9��
																				// ���Ŀ���
																				// �ٷ�
																				// �־����ϴ�.
			coefficient = (1.0 + (PowerSp + 1) * 0.15 + (PowerInt - 9) * 0.2); // PC����
																				// ���
																				// ���ϱ�
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			int spByItem = _npc.getAbility().getSp()
					- _npc.getAbility().getTrueSp();
			int charaIntelligence = _npc.getAbility().getTotalInt();
			coefficient = (1.0 + (charaIntelligence - 8) * 0.2 + spByItem * 0.15); // NPC����
																					// ���
																					// ���ϱ�
		}

		if (coefficient < 1) {
			coefficient = 1;
		}
		magicDamage *= coefficient; // �⺻ ������������ ������� ���ϱ�
		/** ġ��Ÿ �߻� �κ� �߰� - By �ô� - */
		double criticalCoefficient = 1.4;
		int rnd = random.nextInt(100) + 1;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			switch (skillId) { // 6���� ���� �������� ������ ���ݸ���
			case ENERGY_BOLT:
			case ICE_DAGGER:
			case WIND_CUTTER:
			case CHILL_TOUCH:
			case SMASH:
			case FIRE_ARROW:
			case STALAC:
			case VAMPIRIC_TOUCH:
			case CONE_OF_COLD:
			case CALL_LIGHTNING:
				int propCritical = CalcStat.calcBaseMagicCritical(
						_pc.getType(), _pc.ability.getBaseInt()) + 10; // 12ġ��Ÿ
																		// �⺻12
																		// + ���̽�
																		// ����
																		// ���ʽ�
				if (_calcType == PC_PC || _calcType == PC_NPC) {
					if (_pc.getSkillEffectTimerSet().hasSkillEffect(LIND_MAAN) // ǳ����
																				// ����
																				// -
																				// ����Ȯ����
																				// ����ġ��Ÿ+1
							|| _pc.getSkillEffectTimerSet().hasSkillEffect(
									SHAPE_MAAN) // ������ ���� - ����Ȯ���� ����ġ��Ÿ+1
							|| _pc.getSkillEffectTimerSet().hasSkillEffect(
									LIFE_MAAN)) { // ������ ���� - ����Ȯ���� ����ġ��Ÿ+1
						propCritical += 1; // ���ȿ� ���� ����ġ��Ÿ +1
					}
				}
				if (criticalOccur(propCritical)) {
					magicDamage *= 1.5;
				}
				break;
			}
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			if (rnd <= 15) {
				magicDamage *= criticalCoefficient;
			}
		}
		if (getTargetMr() < 101) {
			PowerMr = getTargetMr() / 200; // ����100�Ǹ� 10�� (�⺻������*�������)�� 5% ������
											// �پ��� ���� ��50%
		} else {
			PowerMr = 0.5 + (getTargetMr() - 100) / 1000; // ����100�ʰ��п� ���� 10��
															// (�⺻������*�������)�� 1%
															// �پ��� ���� 100�� 10%
		} // ���� 600�Ǹ� ���������� 0
		if (skillId == FINAL_BURN) {
			PowerMr = 0;
		}
		magicDamage -= magicDamage * PowerMr; // ���� ���濡 ���� ������ ���Һ��� ó��
		double attrDeffence = calcAttrResistance(l1skills.getAttr()); // �Ӽ����
																		// 100��
																		// 45%�پ��.
																		// 10��
																		// 4.5%
																		// �ʰ��п�
																		// ���ؼ�
																		// 10��
																		// 0.9%
																		// �پ���
																		// ����
		magicDamage -= magicDamage * attrDeffence; // ���濡 ���� ������ ������ �Ӽ��� ����
													// ������ ���� ó��
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicDamage += _pc.getBaseMagicDmg(); // ���̽� ���� ���� ������ ���ʽ� �߰�
		}
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int weaponAddDmg = 0;
			L1ItemInstance weapon = _pc.getWeapon();
			if (weapon != null) {
				weaponAddDmg = weapon.getItem().getMagicDmgModifier();
			}
			magicDamage += weaponAddDmg; // ���⿡ ���� ���� ������ �߰�
		}
		return magicDamage;
	}

	// �ܡܡܡ� �� ȸ��������ȭ(��� ���忡�� ������)�� ���� �ܡܡܡ�
	public int calcHealing(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int diceCount = l1skills.getDamageDiceCount();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;
		double PowerHeal = 0;
		for (int i = 0; i < diceCount; i++) {
			magicDamage += (_random.nextInt(dice) + 1);
		}
		magicDamage += value;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int magicBonus = getMagicBonus();
			int PowerInt = _pc.getAbility().getTotalInt() - getMagicBonus(); // SP��
																				// �������ʴ�
																				// INT
																				// ���ϱ�
			PowerHeal = magicBonus + 1 + (PowerInt * 0.1); // PC���� ��� ���ϱ�
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			int charaIntelligence = _npc.getAbility().getTotalInt();
			PowerHeal = charaIntelligence / 2; // NPC���� ��� ���ϱ�
		}
		magicDamage *= (1 + PowerHeal); // ���������ŭ ���ϱ�
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (getLawful() > 0) {
				magicDamage *= 1 + (getLawful() / 32768.0);
			}
		}
		magicDamage /= 4; // ���ʿ� value dice�� 4���Ѱ��� �������� ����.
		return magicDamage;
	}

	/**
	 * MR�� ���� ���� ������ ���Ҹ� ó�� �Ѵ�
	 * 
	 * @param dmg
	 * @return dmg
	 */
	// �ܡܡܡ� MR�� ���� ������ �氨 �ܡܡܡ�
	public int calcMrDefense(int dmg) {
		double PowerMr = 0;
		if (getTargetMr() < 101) {
			PowerMr = getTargetMr() / 200; // ����100�Ǹ� 10�� (�⺻������*�������)�� 5% ������
											// �پ��� ���� ��50%
		} else {
			PowerMr = 0.5 + (getTargetMr() - 100) / 1000; // ����100�ʰ��п� ���� 10��
															// (�⺻������*�������)�� 1%
															// �پ��� ���� 100�� 10%
		} // ���� 600�Ǹ� ���������� 0
		dmg -= dmg * PowerMr;
		return dmg;
	}

	private boolean criticalOccur(int prop) {
		boolean ok = false;
		int num = _random.nextInt(100) + 1;

		if (prop == 0) {
			return false;
		}
		if (num <= prop) {
			ok = true;
		}
		return ok;
	}

	private double calcAttrResistance(int attr) {
		int resist = 0;
		int resistFloor = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			switch (attr) {
			case L1Skills.ATTR_EARTH:
				resist = _targetPc.getResistance().getEarth();
				break;
			case L1Skills.ATTR_FIRE:
				resist = _targetPc.getResistance().getFire();
				break;
			case L1Skills.ATTR_WATER:
				resist = _targetPc.getResistance().getWater();
				break;
			case L1Skills.ATTR_WIND:
				resist = _targetPc.getResistance().getWind();
				break;
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
		}
		if (resist < 0) {
			resistFloor = (int) (-0.45 * Math.abs(resist));
		} else if (resist < 101) {
			resistFloor = (int) (0.45 * Math.abs(resist));
		} else {
			resistFloor = (int) (45 + 0.09 * Math.abs(resist)); // �Ӽ�100�ʰ��п� ����
																// 0.45�� 1/5����
																// ���ҵǰ� ����
		}
		double attrDeffence = resistFloor / 100;
		return attrDeffence;
	}

	public void commit(int damage, int drainMana) {
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			commitPc(damage, drainMana);
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			commitNpc(damage, drainMana);
		}

		if (!Config.ALT_ATKMSG) {
			return;
		}
		if (Config.ALT_ATKMSG) {
			if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
				return;
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC)
					&& !_targetPc.isGm()) {
				return;
			}
		}
		String msg0 = "";
	//	String msg1 = " -> ";
		String msg2 = "";
		String msg3 = "";
		String msg4 = "";

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			msg0 = _pc.getName();
		} else if (_calcType == NPC_PC) {
			msg0 = _npc.getName();
		}

		if (_calcType == NPC_PC || _calcType == PC_PC) {
			msg4 = _targetPc.getName();
			msg2 = "HP:" + _targetPc.getCurrentHp();
		} else if (_calcType == PC_NPC) {
			msg4 = _targetNpc.getName();
			msg2 = "HP:" + _targetNpc.getCurrentHp();
		}

		msg3 = "DMG:" + damage;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			_pc.sendPackets(new S_SystemMessage("\\fR[" + msg0 + "->" + msg4
					+ "] " + msg3 + " / " + msg2));
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			_targetPc.sendPackets(new S_SystemMessage("\\fY[" + msg0 + "->"
					+ msg4 + "] " + msg3 + " / " + msg2));
		}
	}

	private void commitPc(int damage, int drainMana) {
		if (_calcType == PC_PC) {
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
					ABSOLUTE_BARRIER)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							ICE_LANCE)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							FREEZING_BLIZZARD)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							EARTH_BIND)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_BASILL)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_COCA)) {
				damage = 0;
				drainMana = 0;
			}
			if (drainMana > 0 && _targetPc.getCurrentMp() > 0) {
				if (drainMana > _targetPc.getCurrentMp()) {
					drainMana = _targetPc.getCurrentMp();
				}
				int newMp = _pc.getCurrentMp() + drainMana;
				_pc.setCurrentMp(newMp);
			}
			_targetPc.receiveManaDamage(_pc, drainMana);
			_targetPc.receiveDamage(_pc, damage, true);
		} else if (_calcType == NPC_PC) {
			_targetPc.receiveDamage(_npc, damage, true);
		}
	}

	private void commitNpc(int damage, int drainMana) {
		if (_calcType == PC_NPC) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId == 4039001
					&& _pc.getSkillEffectTimerSet().hasSkillEffect(
							PAP_FIVEPEARLBUFF)) {
				damage = 1;
			}// ��������
			if (npcId == 4039002
					&& _pc.getSkillEffectTimerSet().hasSkillEffect(
							PAP_MAGICALPEARLBUFF)) {
				damage = 1;
			}// ��������
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7720) {
				damage = 1;
			} // �丣����
			if (_targetNpc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							FREEZING_BLIZZARD)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							EARTH_BIND)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_BASILL)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_COCA)) {
				damage = 0;
				drainMana = 0;
			}
			/** ����������üũ **/
			if (_pc.isGm()) {
				if (npcId == 45001 || npcId == 45002) {
					_pc.sendPackets(new S_SystemMessage("\\fC����������:[" + damage
							+ "]�Դϴ�."));
					return;
				}
			}
			/** ����������üũ **/
			if (drainMana > 0) {
				int drainValue = _targetNpc.drainMana(drainMana);
				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);
			}
			_targetNpc.ReceiveManaDamage(_pc, drainMana);
			_targetNpc.receiveDamage(_pc, damage);
		} else if (_calcType == NPC_NPC) {
			_targetNpc.receiveDamage(_npc, damage);
		}
	}
}
