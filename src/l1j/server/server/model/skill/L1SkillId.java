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
package l1j.server.server.model.skill;

public class L1SkillId {
	public static final int SKILLS_BEGIN = 1;

	/*
	 * Regular Magic Lv1-10
	 */
	/** �⸣Ÿ�� ��ų */
	 public static final int TROGIR_MILPITAS   = 242;
	 public static final int TROGIR_MILPITAS1  = 243;
	 public static final int TROGIR_MILPITAS2  = 244;
	 public static final int TROGIR_MILPITAS3  = 10126;
	 public static final int TROGIR_MILPITAS4  = 10127;
	 public static final int TROGIR_MILPITAS5  = 10128;
	 public static final int TROGIR_MILPITAS6  = 10129;
	 public static final int TROGIR_MILPITAS7  = 10130;

public static final int Changgo_TIMER= 6524; // â�� �̿� �ð� �߰� 
public static final int Armor_TIMER= 6525; //�Ƹ� ����ð�

public static final int benz_TIMER= 6599; //���̵�����
public static final int benz_TIMER1= 6600; //���̵�����

public static final int STATUS_DRAGON_PEARL = 1030;

	// 1�ܰ� �Ϲݸ���
	public static final int HEAL = 1; //��

	public static final int LIGHT = 2;//����Ʈ

	public static final int SHIELD = 3;//�ǵ�

	public static final int ENERGY_BOLT = 4;//����

	public static final int TELEPORT = 5;//��

	public static final int ICE_DAGGER = 6;//���̽����

	public static final int WIND_CUTTER = 7; //�����Ŀ

	public static final int HOLY_WEAPON = 8;//Ȧ������

	// 2�ܰ� �Ϲݸ���
	public static final int CURE_POISON = 9;//ť��������

	public static final int CHILL_TOUCH = 10;//ĥ��ġ

	public static final int CURSE_POISON = 11;//Ŀ��������

	public static final int ENCHANT_WEAPON = 12;//��æƮ����

	public static final int DETECTION = 13;//����

	public static final int DECREASE_WEIGHT = 14;//��ũ�������Ʈ

	public static final int FIRE_ARROW = 15;//���̾�ַο�

	public static final int STALAC = 16;//��Ż��

	// 3�ܰ� �Ϲݸ���
	public static final int LIGHTNING = 17;//����Ʈ��

	public static final int TURN_UNDEAD = 18;//��

	public static final int EXTRA_HEAL = 19; //����

	public static final int CURSE_BLIND = 20;//Ŀ������ε�

	public static final int BLESSED_ARMOR = 21;//������Ƹ�

	public static final int FROZEN_CLOUD = 22;//������Ŭ����

	public static final int WEAK_ELEMENTAL = 23; //��ũ������Ż

	// 4�ܰ� �Ϲݸ��� // none = 24
	public static final int FIREBALL = 25;

	public static final int PHYSICAL_ENCHANT_DEX = 26; // E: ENCHANT_DEXTERITY

	public static final int WEAPON_BREAK = 27;

	public static final int VAMPIRIC_TOUCH = 28;

	public static final int SLOW = 29;

	public static final int EARTH_JAIL = 30;

	public static final int COUNTER_MAGIC = 31;//ī���͸���

	public static final int MEDITATION = 32;

	// 5�ܰ� �Ϲݸ���
	public static final int CURSE_PARALYZE = 33;

	public static final int CALL_LIGHTNING = 34;//�ݶ���Ʈ��

	public static final int GREATER_HEAL = 35;

	public static final int TAMING_MONSTER = 36; // E: TAME_MONSTER

	public static final int REMOVE_CURSE = 37;

	public static final int CONE_OF_COLD = 38;

	public static final int MANA_DRAIN = 39;

	public static final int DARKNESS = 40;

	// 6�ܰ� �Ϲݸ���
	public static final int CREATE_ZOMBIE = 41;

	public static final int PHYSICAL_ENCHANT_STR = 42; // E: ENCHANT_MIGHTY

	public static final int HASTE = 43;

	public static final int CANCELLATION = 44; // E: CANCEL MAGIC

	public static final int ERUPTION = 45;//�̷���
	
	public static final int SUNBURST = 46;//������Ʈ

	public static final int WEAKNESS = 47;

	public static final int BLESS_WEAPON = 48;

	// 7�ܰ� �Ϲݸ���
	public static final int HEAL_ALL = 49; // E: HEAL_PLEDGE

	public static final int ICE_LANCE = 50;

	public static final int SUMMON_MONSTER = 51;

	public static final int HOLY_WALK = 52;

	public static final int TORNADO = 53;

	public static final int GREATER_HASTE = 54;

	public static final int BERSERKERS = 55;

	public static final int DISEASE = 56;

	// 8�ܰ� �Ϲݸ���
	public static final int FULL_HEAL = 57;

	public static final int FIRE_WALL = 58;

	public static final int BLIZZARD = 59;

	public static final int INVISIBILITY = 60;

	public static final int RESURRECTION = 61;//��������

	public static final int EARTHQUAKE = 62;

	public static final int LIFE_STREAM = 63;

	public static final int SILENCE = 64;

	// 9�ܰ� �Ϲݸ���
	public static final int LIGHTNING_STORM = 65;//����Ʈ�׽���

	public static final int FOG_OF_SLEEPING = 66;

	public static final int SHAPE_CHANGE = 67; // E: POLYMORPH

	public static final int IMMUNE_TO_HARM = 68;

	public static final int MASS_TELEPORT = 69;

	public static final int FIRE_STORM = 70;

	public static final int DECAY_POTION = 71;

	public static final int COUNTER_DETECTION = 72;

	// 10�ܰ� �Ϲݸ���
	public static final int CREATE_MAGICAL_WEAPON = 73;

	public static final int METEOR_STRIKE = 74;

	public static final int GREATER_RESURRECTION = 75;

	public static final int MASS_SLOW = 76;

	public static final int DISINTEGRATE = 77; //��

	public static final int ABSOLUTE_BARRIER = 78;

	public static final int ADVANCE_SPIRIT = 79;//�

	public static final int FREEZING_BLIZZARD = 80;

	// none = 81 - 86
	/*
	 * Knight skills
	 */
	public static final int SHOCK_STUN = 87; // E: STUN_SHOCK

	public static final int REDUCTION_ARMOR = 88;

	public static final int BOUNCE_ATTACK = 89;

	public static final int SOLID_CARRIAGE = 90;

	public static final int COUNTER_BARRIER = 91;

	// none = 92-96
	/*
	 * Dark Spirit Magic
	 */
	public static final int BLIND_HIDING = 97;

	public static final int ENCHANT_VENOM = 98;

	public static final int SHADOW_ARMOR = 99;

	public static final int BRING_STONE = 100;

	public static final int MOVING_ACCELERATION = 101; // E: PURIFY_STONE

	public static final int BURNING_SPIRIT = 102;

	public static final int DARK_BLIND = 103;

	public static final int VENOM_RESIST = 104;

	public static final int DOUBLE_BRAKE = 105;

	public static final int UNCANNY_DODGE = 106;

	public static final int SHADOW_FANG = 107;

	public static final int FINAL_BURN = 108;

	public static final int DRESS_MIGHTY = 109;

	public static final int DRESS_DEXTERITY = 110;

	public static final int DRESS_EVASION = 111;
	
	public static final int ARMOR_BRAKE = 112; //�ƸӺ극��ũ
	

	// none = 112
	/*
	 * Royal Magic
	 */
	public static final int TRUE_TARGET = 113;

	public static final int GLOWING_AURA = 114;

	public static final int SHINING_AURA = 115;

	public static final int CALL_CLAN = 116; // E: CALL_PLEDGE_MEMBER

	public static final int BRAVE_AURA = 117;

	public static final int RUN_CLAN = 118;
	
		// unknown = 119 - 120
	// none = 121 - 128
	/*
	 * Spirit Magic
	 */
	public static final int RESIST_MAGIC = 129;

	public static final int BODY_TO_MIND = 130;

	public static final int TELEPORT_TO_MOTHER = 131;

	public static final int TRIPLE_ARROW = 132;

	public static final int ELEMENTAL_FALL_DOWN = 133;

	public static final int COUNTER_MIRROR = 134;

	// none = 135 - 136
	public static final int CLEAR_MIND = 137;

	public static final int RESIST_ELEMENTAL = 138;

	// none = 139 - 144
	public static final int RETURN_TO_NATURE = 145;

	public static final int BLOODY_SOUL = 146; // E: BLOOD_TO_SOUL

	public static final int ELEMENTAL_PROTECTION = 147; // E:PROTECTION_FROM_ELEMENTAL

	public static final int FIRE_WEAPON = 148;

	public static final int WIND_SHOT = 149;

	public static final int WIND_WALK = 150;

	public static final int EARTH_SKIN = 151;

	public static final int ENTANGLE = 152;//���ʱ�

	public static final int ERASE_MAGIC = 153;//�̷��������

	public static final int LESSER_ELEMENTAL = 154; // E:SUMMON_LESSER_ELEMENTAL

	public static final int FIRE_BLESS = 155; // E: BLESS_OF_FIRE

	public static final int STORM_EYE = 156; // E: EYE_OF_STORM

	public static final int EARTH_BIND = 157;

	public static final int NATURES_TOUCH = 158;

	public static final int EARTH_BLESS = 159; // E: BLESS_OF_EARTH

	public static final int AQUA_PROTECTER = 160;

	public static final int AREA_OF_SILENCE = 161;

	public static final int GREATER_ELEMENTAL = 162; // E:SUMMON_GREATER_ELEMENTAL

	public static final int BURNING_WEAPON = 163;

	public static final int NATURES_BLESSING = 164;

	public static final int CALL_OF_NATURE = 165; // E: NATURES_MIRACLE

	public static final int STORM_SHOT = 166;

	public static final int WIND_SHACKLE = 167;

	public static final int IRON_SKIN = 168;//�վ

	public static final int EXOTIC_VITALIZE = 169;

	public static final int WATER_LIFE = 170;

	public static final int ELEMENTAL_FIRE = 171;

	public static final int STORM_WALK = 172;

	public static final int POLLUTE_WATER = 173;

	public static final int STRIKER_GALE = 174;//��Ʈ����Ŀ����

	public static final int SOUL_OF_FLAME = 175;

	public static final int ADDITIONAL_FIRE = 176;

	// ����
	public static final int DRAGON_SKIN = 181;

	public static final int BURNING_SLASH = 182;

	public static final int GUARD_BREAK = 183;

	public static final int MAGMA_BREATH = 184;

	public static final int SCALES_EARTH_DRAGON = 185;

	public static final int BLOOD_LUST = 186;

	public static final int FOU_SLAYER = 187;

	public static final int FEAR = 188;

	public static final int SHOCK_SKIN = 189;

	public static final int SCALES_WATER_DRAGON = 190;

	public static final int MORTAL_BODY = 191;

	public static final int THUNDER_GRAB = 192;

	public static final int HORROR_OF_DEATH = 193;

	public static final int FREEZING_BREATH = 194;

	public static final int SCALES_FIRE_DRAGON = 195;

	// ȯ����
	public static final int MIRROR_IMAGE = 201;

	public static final int CONFUSION = 202;

	public static final int SMASH = 203;

	public static final int IllUSION_OGRE = 204;

	public static final int CUBE_IGNITION = 205;

	public static final int CONCENTRATION = 206;

	public static final int MIND_BREAK = 207;

	public static final int BONE_BREAK = 208;

	public static final int IllUSION_LICH = 209;

	public static final int CUBE_QUAKE = 210;

	public static final int PATIENCE = 211;

	public static final int PHANTASM = 212;

	public static final int AM_BREAK = 213;

	public static final int IllUSION_DIAMONDGOLEM = 214;

	public static final int CUBE_SHOCK = 215;

	public static final int INSIGHT = 216;

	public static final int PANIC = 217;

	public static final int JOY_OF_PAIN = 218;

	public static final int IllUSION_AVATAR = 219;

	public static final int CUBE_BALANCE = 220;

	public static final int SKILLS_END = 220;

	/** ���� ���� = 308~ 309* */
	public static final int EXPLORER = 308; // ���극�� ��� ��

	public static final int EXPLORER2 = 309; // ���극�� ���� ��

	/** ���� ���� = 308~ 309* */
	/*
	 * Status
	 */
	public static final int STATUS_BEGIN = 1000;

	public static final int STATUS_BRAVE = 1000;

	public static final int STATUS_AUTOROOT = 76261;// �������

	public static final int STATUS_MENT = 7626; // �߰� ��Ʈ��ɾ�
	
	public static final int STATUS_PMENT = 7627;

	public static final int STATUS_HASTE = 1001;

	public static final int STATUS_BLUE_POTION = 1002;

	public static final int STATUS_UNDERWATER_BREATH = 1003;

	public static final int STATUS_WISDOM_POTION = 1004;

	public static final int STATUS_POISON = 1006;

	public static final int STATUS_POISON_SILENCE = 1007;

	public static final int STATUS_POISON_PARALYZING = 1008;

	public static final int STATUS_POISON_PARALYZED = 1009;

	public static final int STATUS_CURSE_PARALYZING = 1010;

	public static final int STATUS_CURSE_PARALYZED = 1011;

	public static final int STATUS_FLOATING_EYE = 1012;

	public static final int STATUS_HOLY_WATER = 1013;

	public static final int STATUS_HOLY_MITHRIL_POWDER = 1014;

	public static final int STATUS_HOLY_WATER_OF_EVA = 1015;

	public static final int STATUS_ELFBRAVE = 1016;

	public static final int STATUS_CANCLEEND = 1016;

	public static final int STATUS_CURSE_BARLOG = 1017;

	public static final int STATUS_CURSE_YAHEE = 1018;

	public static final int STATUS_PET_FOOD = 1019;

	public static final int STATUS_PINK_NAME = 1020;

	public static final int STATUS_TIKAL_BOSSJOIN = 1021;

	public static final int STATUS_TIKAL_BOSSDIE = 1022;

	public static final int STATUS_CHAT_PROHIBITED = 1023;

	public static final int CHAR_BUFF_START = 6993;

	public static final int STATUS_COMA_3 = 1024; // �ڸ�����3��

	public static final int STATUS_COMA_5 = 1025; // �ڸ�����5��

	public static final int BUFF_CRAY = 1026; // ũ���̹���

	public static final int CHAR_BUFF_END = 7950; // �ɸ��� ���� ������ ���� �߰�

	/** ������ �丮��(�����) */
	public static final int STATUS_LUCK_A = 7947;

	public static final int STATUS_LUCK_B = 7948;

	public static final int STATUS_LUCK_C = 7949;

	public static final int STATUS_LUCK_D = 7950;

	/** ������ �丮��(�����) */
	// public static final int STATUS_END = 1023;
	public static final int GMSTATUS_BEGIN = 2000;

	public static final int GMSTATUS_INVISIBLE = 2000;

	public static final int GMSTATUS_HPBAR = 2001;

	public static final int GMSTATUS_SHOWTRAPS = 2002;

	public static final int GMSTATUS_END = 2002;

	public static final int COOKING_NOW = 2999;

	public static final int COOKING_BEGIN = 3000;

	public static final int BUFF_SAEL = 10499; // �翤����

	/** 1���丮 ȿ�� (���) */
	public static final int COOKING_1_0_N = 3000;

	public static final int COOKING_1_1_N = 3001;

	public static final int COOKING_1_2_N = 3002;

	public static final int COOKING_1_3_N = 3003;

	public static final int COOKING_1_4_N = 3004;

	public static final int COOKING_1_5_N = 3005;

	public static final int COOKING_1_6_N = 3006;

	public static final int COOKING_1_7_N = 3007;

	/** 2���丮 ȿ�� (���) */
	public static final int COOKING_1_8_N = 3008;

	public static final int COOKING_1_9_N = 3009;

	public static final int COOKING_1_10_N = 3010;

	public static final int COOKING_1_11_N = 3011;

	public static final int COOKING_1_12_N = 3012;

	public static final int COOKING_1_13_N = 3013;

	public static final int COOKING_1_14_N = 3014;

	public static final int COOKING_1_15_N = 3015;

	/** 3���丮 ȿ�� (���) */
	public static final int COOKING_1_16_N = 3016;

	public static final int COOKING_1_17_N = 3017;

	public static final int COOKING_1_18_N = 3018;

	public static final int COOKING_1_19_N = 3019;

	public static final int COOKING_1_20_N = 3020;

	public static final int COOKING_1_21_N = 3021;

	public static final int COOKING_1_22_N = 3022;

	public static final int COOKING_1_23_N = 3023;

	/** 1���丮 ȿ�� (ȯ��) */
	public static final int COOKING_1_0_S = 3050;

	public static final int COOKING_1_1_S = 3051;

	public static final int COOKING_1_2_S = 3052;

	public static final int COOKING_1_3_S = 3053;

	public static final int COOKING_1_4_S = 3054;

	public static final int COOKING_1_5_S = 3055;

	public static final int COOKING_1_6_S = 3056;

	public static final int COOKING_1_7_S = 3057;

	/** 2���丮 ȿ�� (ȯ��) */
	public static final int COOKING_1_8_S = 3058;

	public static final int COOKING_1_9_S = 3059;

	public static final int COOKING_1_10_S = 3060;

	public static final int COOKING_1_11_S = 3061;

	public static final int COOKING_1_12_S = 3062;

	public static final int COOKING_1_13_S = 3063;

	public static final int COOKING_1_14_S = 3064;

	public static final int COOKING_1_15_S = 3065;

	/** 3���丮 ȿ�� (ȯ��) */
	public static final int COOKING_1_16_S = 3066;

	public static final int COOKING_1_17_S = 3067;

	public static final int COOKING_1_18_S = 3068;

	public static final int COOKING_1_19_S = 3069;

	public static final int COOKING_1_20_S = 3070;

	public static final int COOKING_1_21_S = 3071;

	public static final int COOKING_1_22_S = 3072;

	public static final int COOKING_1_23_S = 3073;

	public static final int SPECIAL_COOKING = 3074;

	public static final int DRAGONBLOOD_ANTA = 5021; // ��Ÿ��������[��Ǫ1.5�赩/���Ӽ�+50]

	public static final int DRAGONBLOOD_PAP = 5022; // ��Ǫ������ ����[����1.5�赩/�ٶ��Ӽ�+50]
  
    public static final int DRAGONBLOOD_RIND = 5023; //���巹�̵�
	
  public static final int COOKING_END = 3075;

	public static final int STATUS_FREEZE = 10071;

	public static final int CURSE_PARALYZE2 = 10101;

	public static final int STATUS_SPOT1 = 20072;

	public static final int STATUS_SPOT2 = 20073;

	public static final int STATUS_SPOT3 = 20074;

	public static final int STATUS_IGNITION = 20075;

	public static final int STATUS_QUAKE = 20076;

	public static final int STATUS_SHOCK = 20077;

	public static final int STATUS_BALANCE = 20078;

	public static final int STATUS_FRUIT = 20079;

	public static final int STATUS_OVERLAP = 20080;
	public static final int EXP_POTION = 20081;
    public static final int EXP_POTION3 = 9278;
	public static final int EXP_POTION2 = 9279;
	public static final int STATUS_BLUE_POTION2 = 20082;

	public static final int STATUS_DESHOCK = 20083;

	public static final int STATUS_CUBE = 20084;

	public static final int STATUS_CASHSCROLL = 6993;

	public static final int STATUS_CASHSCROLL2 = 6994;

	public static final int STATUS_CASHSCROLL3 = 6995;

	public static final int STATUS_BLUE_POTION3 = 22004;

	public static final int STATUS_CASHSCROLL4 = 69991;

	public static final int STATUS_CASHSCROLL5 = 69992;

	public static final int STATUS_CASHSCROLL6 = 69993;

	public static final int STATUS_CASHSCROLL7 = 69994;

	public static final int STATUS_CASHSCROLL8 = 69995;

	public static final int STATUS_CASHSCROLL9 = 69996;

	public static final int STATUS_CASHSCROLL10 = 69997;

	public static final int MOB_SLOW_18 = 30000; // ���ο� 18�����

	public static final int MOB_SLOW_1 = 30001; // ���ο� 1�����

	public static final int MOB_CURSEPARALYZ_19 = 30002; // Ŀ�� 19�����

	public static final int MOB_COCA = 30003; // ��īƮ���� �󸮱����

	public static final int MOB_BASILL = 30004; // �ٽǸ���ũ �󸮱⿡��

	public static final int MOB_RANGESTUN_19 = 30005; // �������� 19�����

	public static final int MOB_RANGESTUN_18 = 30006; // �������� 18�����

	public static final int MOB_CURSEPARALYZ_18 = 30007; // Ŀ�� 18�����

	public static final int MOB_DISEASE_30 = 30008; // ������ 30�����

	public static final int MOB_WEAKNESS_1 = 30009; // ��ũ�Ͻ� 1�����

	public static final int MOB_DISEASE_1 = 30079; // ������ 1�����

	public static final int MOB_SHOCKSTUN_30 = 30081; // ��ũ���� 30�����

	public static final int MOB_WINDSHACKLE_1 = 30084; // �����Ŭ 1�����

	public static final int ANTA_MAAN = 7671; // ������ ����

	public static final int FAFU_MAAN = 7672; // ������ ����

	public static final int LIND_MAAN = 7674; // ǳ���� ����

	public static final int VALA_MAAN = 7673; // ȭ���� ����

	public static final int BIRTH_MAAN = 7675; // ź���� ����

	public static final int SHAPE_MAAN = 7676; // ������ ����

	public static final int LIFE_MAAN = 7677; // ������ ����

	public static final int MAAN_TIMER = 7670; // ���� Ÿ�̸�
	// ////////////////////////////////////////////////////���Ϳ��� ����� ���� ���� �߰�
	// - ����ų���� �߰� : ����

	public static final int Mob_SLOW_1 = 30060;

	// ���ο� 1�����
	public static final int Mob_SLOW_18 = 30077;

	// ���ο� 18�����
	public static final int Mob_WEAKNESS_1 = 30078;

	// ��ũ�Ͻ� 1�����
	public static final int Mob_DISEASE_1 = 30079;

	// ������ 1�����
	public static final int Mob_Basill = 30011;

	// �ٽǸ���ũ �󸮱⿡��
	public static final int Mob_SHOCKSTUN_18 = 30081;

	// ��ũ���� 18�����
	public static final int Mob_RANGESTUN_19 = 30082;

	// �������� 19�����
	public static final int Mob_RANGESTUN_18 = 30083;

	// �������� 18�����
	public static final int Mob_RANGESTUN_30 = 20167;

	// �������� 30�����
	public static final int Mob_RANGESTUN_20 = 30104;

	// �������� 20�����
	public static final int Mob_DISEASE_30 = 30080;

	// ������ 30�����
	public static final int Mob_WINDSHACKLE_1 = 30084;

	// �����Ŭ 1�����
	public static final int Mob_Coca = 30010;

	// ��īƮ���� �󸮱����
	public static final int Mob_CURSEPARALYZ_19 = 30085;

	// Ŀ�� 19�����
	public static final int Mob_CURSEPARALYZ_18 = 30086;

	// Ŀ�� 18�����
	public static final int Mob_CURSEPARALYZ_30 = 30096;

	// Ŀ�� 30�����
	public static final int Mob_CURSEPARALYZ_SHORT_18 = 30101;

	// Ŀ�� �˼��޵λ�� 18�����
	public static final int Mob_VAMPIRIC_TOUCH_1 = 30102;

	// �����̾��ġ 1�����
	public static final int Mob_AREA_ICE_LANCE = 20315;

	// ��Ǫ���� �����󸮱� 18�����
	public static final int Mob_AREA_FIRE_WALL = 20294;

	// �߶�ī�� �������̾�� 18�����
	public static final int Mob_AREA_POISON_18 = 30086;

	// ���� ������ 18�����
	public static final int Mob_AREA_POISON_30 = 20293;

	// ���� ������ 30�����
	public static final int Mob_AREA_POISON = 20292;

	// ��Ÿ�� ���������� 18�����
	public static final int Mob_AREA_POISON_20 = 10152;

	// ���� ������ 20����� 5��ġ
	public static final int Mob_CURSPOISON_30 = 30088;

	// Ŀ�� ������ 30�����
	public static final int Mob_CURSPOISON_18 = 10012;

	// Ŀ�� ������ 18�����
	public static final int Mob_CALL_LIGHTNING_ICE = 20158;

	// ���� ����(��) �ݶ���Ʈ��(�󸮱�) 1�� ���
	public static final int Mob_AREA_CANCELLATION_19 = 10153;

	// ĵ�����̼� 19�����
	public static final int Mob_AREA_EARTHQUAKE = 30106;

	// ��Ÿ��(������) ��� 11��
	public static final int ANTA_SKILL_1 = 10188;

	public static final int ANTA_SKILL_2 = 10189;

	public static final int ANTA_SKILL_3 = 10190;

	public static final int ANTA_SKILL_4 = 10191;

	public static final int ANTA_SKILL_5 = 10192;

	public static final int ANTA_SKILL_6 = 10193;

	public static final int ANTA_SKILL_7 = 10194;

	public static final int ANTA_SKILL_8 = 10195;

	public static final int ANTA_SKILL_9 = 10196;

	public static final int ANTA_SKILL_10 = 10197;

	public static final int PAPOO_SKILL = 10502; // ����Ÿ! ������ īǪ

	public static final int PAPOO_SKILL2 = 10508; // ����Ÿ! �� ����

	public static final int PAPOO_SKILL3 = 10509; // ������! ���� ����

	public static final int PAPOO_SKILL4 = 10510; // ����Ÿ!�׳� �е�

	public static final int PAPOO_SKILL5 = 10512; // ����Ÿ!�׳� �е�

	public static final int PAPOO_SKILL6 = 10514; // ����Ÿ!�� ���̹�

	public static final int PAPOO_SKILL7 = 10515; // ����Ÿ! ���� ���̹� //7870���׸�����

	public static final int PAPOO_SKILL8 = 10516; // ����Ÿ! �׳� ���ζ�ũ

	public static final int PAPOO_SKILL9 = 10519; // ����Ÿ! �׳� �촩��

	public static final int PAPOO_SKILL10 = 10520; // ����Ÿ! ������ ���ζ�ũ

	public static final int PAPOO_SKILL11 = 10521; // ����Ÿ! ������ ���� ���ζ�ũ

	public static final int ANTA_BLOOD = 7783; // ���� ����
	
	public static final int PAP_FIVEPEARLBUFF = 10501;        // ��Ǫ[�������� �ı� ����]
	
	public static final int PAP_MAGICALPEARLBUFF = 10500;     // ��Ǫ[�ź��ѿ������� �ı�����]

	public static final int STATUS_DRAGONPERL = 999; // �巡������

	public static final int SANGA = 7322; // ���ž ����

	public static final int SANGABUFF = 7323; // ���ž ����
	
	public static final int DRAGON_EMERALD_NO = 7785;// �巡�￡�޶���
	
	public static final int DRAGON_EMERALD_YES = 7786;// �巡�￡�޶���
	 
	public static final int P_WIND_SHACKLE = 5600;//�ҷ����̺�Ʈ
	 
	public static final int ICE_ERUPTION= 20043;//����ű���
	
	// ���巹�̵�
	public static final int RINDVIOR_WIND_SHACKLE = 7001; // ���弼Ŭ
	public static final int RINDVIOR_PREDICATE_CANCELLATION = 7002; // ���� ��ȯ
	public static final int RINDVIOR_TORNADO_FORE = 7003; // ȸ���� 4�� ��ü ����
	public static final int RINDVIOR_WEAPON = 7004; // ����
	public static final int RINDVIOR_BOW = 7005;
	
	//4��õ��

	// 7006 ����Ʈ�� ����
	public static final int RINDVIOR_WIND_SHACKLE_1 = 7007;
	public static final int RINDVIOR_WEAPON_2 = 7008;
	public static final int RINDVIOR_STORM = 7009;
	public static final int RINDVIOR_CANCELLATION = 7010;
	// 7011 �극��
	// 7012 ����Ʈ�� ���� SILENCE
	public static final int RINDVIOR_SILENCE = 7013;
	public static final int RINDVIOR_SUMMON_MONSTER = 7018;
	public static final int RINDVIOR_PREDICATE = 7019;
	public static final int RINDVIOR_SUMMON_MONSTER_CLOUD  = 7023;
	
	 // �Ǵн�
	 public static final int PHOENIX_CANCELLATION = 7024;
	 public static final int PHOENIX_SUMMON_MONSTER = 7025;
	 
	 // ������Ʈ
	 public static final int EFRETE_SUMMON_MONSTER = 7028; 
	 
	 // �����
	 public static final int BLACKELDER_DEATH_POTION = 7030;
	 public static final int BLACKELDER_DEATH_HELL = 7031;
	 
	 // �巹��ũ
	 public static final int DRAKE_WIND_SHACKLE = 7035;
	 public static final int DRAKE_MASSTELEPORT = 7036;
	 
	 //�Ұ񷽸����󹫱�
	 public static final int weapon_A = 7037;//���Ű�
	 public static final int weapon_B = 7038;//��õ
	 public static final int weapon_C = 7039;//��ǳ
	 public static final int weapon_D = 7040;//Ȥ��
	 public static final int weapon_E = 7041;//�ı����̵���,ũ�ο�
	//�űԿ丮
	 public static final int COOKING_NEW_1 = 3076;
		public static final int COOKING_NEW_2 = 3077;
		public static final int COOKING_NEW_3 = 3078;
		public static final int COOKING_NEW_4 = 3079;
		public static final int SPECIAL_COOKING2 = 3080;

		public static final int CUTS_DEATH_POTION = 5050;
		public static final int CUTS_DEATH_HELL = 5051;
}
