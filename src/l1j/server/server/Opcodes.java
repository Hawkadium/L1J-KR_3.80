/** Test Server 11�� 5�� 19���ڿ��ڵ� **/
package l1j.server.server;

public class Opcodes {

	public Opcodes() {
	}
	/** 3.80c ���ڵ� ���� ���� **/
	public static final int C_OPCODE_TRADE = 2; // [/��ȯ]
	  public static final int C_OPCODE_BOOKMARKDELETE = 3; // [/��� �� �����Ŭ�� delete]
	  public static final int C_OPCODE_BUDDYLIST = 4; // ģ������Ʈ
	  public static final int C_OPCODE_FIGHT = 5; // [/����]
	  public static final int C_OPCODE_USESKILL = 6; // ��ų ��� �κ�
	  public static final int C_OPCODE_RESTART = 7; // ���߿� ����â���� ����. �븸: C_OPCODE_CHANGECHAR
	  public static final int C_OPCODE_BOARD = 10; // �Խ��� �б�
	  public static final int C_OPCODE_AMOUNT = 11; // ������ ������ ���� ����
	  public static final int C_OPCODE_WAREHOUSEPASSWORD = 13; //â�� ���. �븸: C_OPCODE_WAREHOUSELOCK
	  public static final int C_OPCODE_CLIENTVERSION = 14; //v Ŭ�󿡼� ���� ���� ��û �ϴ� �κ�
	  public static final int C_OPCODE_TAXRATE = 19; // ���� ����
	  public static final int C_OPCODE_SELECTLIST = 20; // �긮��Ʈ���� ��ã��
	  public static final int C_OPCODE_DROPITEM = 25; // ������ ������
	  public static final int C_OPCODE_LOGINTOSERVEROK = 26; // [ȯ�漳��->��ê��,��]
	  public static final int C_OPCODE_MOVECHAR = 29; // �̵���û �κ�
	  public static final int C_OPCODE_LEAVEPARTY = 33; // ��Ƽ Ż��
	  public static final int C_OPCODE_NPCTALK = 34; // Npc�� ��ȭ�κ�
	  public static final int C_OPCODE_TRADEADDITEM = 37; // ��ȯâ�� ������ �߰�
	  public static final int C_OPCODE_SHOP = 38; // [/���� -> OK]
	  public static final int C_OPCODE_SKILLBUY = 39; // ��ų ����
	  public static final int C_OPCODE_CHATGLOBAL = 40; // ��üä��
	  public static final int C_OPCODE_DOOR = 41; // ��¦ Ŭ�� �κ�
	  public static final int C_OPCODE_PARTY = 43; // [/��Ƽ]. �븸 : C_OPCODE_PARTYLIST
	  public static final int C_OPCODE_DRAWAL = 44; // ���� ���[�ڱ��� �����Ѵ�]
	  public static final int C_OPCODE_GIVEITEM = 45; // ������ ������ �ֱ�
	  public static final int C_OPCODE_PRIVATESHOPLIST = 47; // ���λ��� buy, sell
	  public static final int C_OPCODE_PROPOSE = 50; // [/ûȥ]
	  public static final int C_OPCODE_CHECKPK = 51; // [/checkpk]
	  public static final int C_OPCODE_TELEPORT = 52; // �ڷ���Ʈ ���
	  public static final int C_OPCODE_DEPOSIT = 56; // �� ���� �Ա�
	  public static final int C_OPCODE_LEAVECLANE = 61; // ���� Ż��
	  public static final int C_OPCODE_FISHCLICK = 62; // ���� ���� Ŭ��
	  public static final int C_OPCODE_RANK = 63; // [/����]
	  public static final int C_OPCODE_PLEDGE = 68; // [/����]
	  public static final int C_OPCODE_BANCLAN = 69; // ���� �߹� ��ɾ�
	  public static final int C_OPCODE_TRADEADDOK = 71; // ��ȯ OK
	  public static final int C_OPCODE_CREATE_CHARACTER = 84;  // �ɸ� ����. C_OPCODE_NEWCHAR
	  public static final int C_OPCODE_TRADEADDCANCEL = 86; // ��ȯ ���
	  public static final int C_OPCODE_MAIL = 87; // ������ Ŭ���� �������� �Դٰ���
	  public static final int C_OPCODE_TITLE = 90; // ȣĪ ��ɾ�
	  public static final int C_OPCODE_KEEPALIVE = 95; // 1�и��� �ѹ��� ��
	  public static final int C_OPCODE_BASERESET = 98; // ���� �ʱ�ȭ. �븸: C_OPCODE_CHARRESET
	  public static final int C_OPCODE_PETMENU = 103; // �� �޴�
	  public static final int C_OPCODE_PICKUPITEM = 112; // ������ �ݱ�.��
	  public static final int C_OPCODE_BOARDREAD = 114; // �Խ��� �б�
	  public static final int C_OPCODE_FIX_WEAPON_LIST = 118; // �������
	  public static final int C_OPCODE_LOGINPACKET = 119; //v ���������� ��� ��Ŷ.
	  public static final int C_OPCODE_EXTCOMMAND = 120; // <��Ʈ+1 ~ 5 ���� �׼� >
	  public static final int C_OPCODE_ATTR = 121; // [ Y , N ] ���� �κ�
	  public static final int C_OPCODE_QUITGAME = 122; // v�α���â���� �� �����Ҷ�
	  public static final int C_OPCODE_ARROWATTACK = 123; // Ȱ���� �κ�
	  public static final int C_OPCODE_NPCACTION = 125; // Npc ��ȭ �׼� �κ�
	  public static final int C_OPCODE_CHAT = 136; // �Ϲ� ä��
	  public static final int C_OPCODE_SELECT_CHARACTER = 137; // ����â���� �ɸ� ���� �븸: C_OPCODE_LOGINTOSERVER
	  public static final int C_OPCODE_DELETEINVENTORYITEM = 138; // �����뿡 ������ ����
	  public static final int C_OPCODE_BOARDWRITE = 141; // �Խ��� ����
	  public static final int C_OPCODE_BOARDDELETE = 153; // �Խñ� ����
	  public static final int C_OPCODE_SHOP_N_WAREHOUSE = 161; // ���� ��� ó��. �븸: C_OPCODE_RESULT
	  public static final int C_OPCODE_DELETECHAR = 162; // �ɸ��� ����
	  public static final int C_OPCODE_USEITEM = 164; // ������ ��� �κ�
	  public static final int C_OPCODE_BOOKMARK = 165; // [/��� OO]
	  public static final int C_OPCODE_EXCLUDE = 171; // [/����]
	  public static final int C_OPCODE_EXIT_GHOST = 173; // ���Ѵ��� ������� Ż��
	  public static final int C_OPCODE_RESTART_AFTER_DIE = 177; // ���߿� �׾ ���� ��������. �븸: C_OPCODE_RESTART
	  public static final int C_OPCODE_CHATWHISPER = 184; // �Ӽ� ä��
	  public static final int C_OPCODE_CALL = 185; // CALL��ư .����
	  public static final int C_OPCODE_JOINCLAN = 194; // [/����]
	  public static final int C_OPCODE_CHATPARTY = 199; // ä�� ��Ƽ ����Ʈ. �븸: C_OPCODE_CAHTPARTY
	  public static final int C_OPCODE_DELBUDDY = 202; // ģ������. �븸: C_OPCODE_DELETEBUDDY
	  public static final int C_OPCODE_WHO = 206; // [/����]
	  public static final int C_OPCODE_ADDBUDDY = 207; // ģ���߰�
	  public static final int C_OPCODE_ENTERPORTAL = 219; // ������ ��ư���� ��Ż ����
	  public static final int C_OPCODE_CREATECLAN = 222; // ���� â��
	  public static final int C_OPCODE_SELECTTARGET = 223; // �� ���� ��ǥ ����
	  public static final int C_OPCODE_CHANGEHEADING = 225; // ���� ��ȯ �κ�
	  public static final int C_OPCODE_WAR = 227; // ����
	  public static final int C_OPCODE_ATTACK = 229; // �Ϲݰ��� �κ�
	  public static final int C_OPCODE_CREATEPARTY = 230; // ��Ƽ �ʴ�
	  public static final int C_OPCODE_SHIP = 231; // ��Ÿ�� ������ ����
	  public static final int C_OPCODE_CHARACTERCONFIG = 244; // ĳ���κ���������
	  public static final int C_OPCODE_REPORT = 254; // �ҷ� ���� �Ű�(/�Ű�). �븸: C_OPCODE_SENDLOCATION
	  public static final int C_OPCODE_BANPARTY = 255; // ��Ƽ �߹�
	  
	  
	   
	  //
	   
	  
	  
	  public static final int C_OPCODE_NOTICECLICK = -53; // �������� Ȯ�� ��������. �븸: C_OPCODE_COMMONCLICK
	  public static final int C_OPCODE_ADDSERVICE = -72; // �ΰ�������
	  public static final int C_OPCODE_WHISPERINCLUDE = -102; // [/����]
	  public static final int C_OPCODE_EMBLEM = -107; // ���嵥��Ÿ�� ������ ��û��
	  public static final int C_OPCODE_RETURNTOLOGIN = 1000006; // �ٽ� �α�â���� �Ѿ��
	  public static final int C_OPCODE_BOARDNEXT = -221; // �Խ��� next
	  public static final int C_OPCODE_CLANMATCHING = -228; // ���� �ϴ� ��� Ŭ����.
	  public static final int C_OPCODE_CLAN = -246; // ���ù����� ���� ��ũ ��û[������ emblem����]
	 
	  
	  
	  /** �Ⱦ��� C ���ڵ� */
	  public static final int C_OPCODE_SKILLBUYOK = 44444; // ��ų ���� OK
	  public static final int C_OPCODE_USEPETITEM = 78555; // �� �κ��丮 ������ ���
	  public static final int C_OPCODE_WARTIMESET = 1443; // �����ð� ����.
	  public static final int C_OPCODE_SOLDIERGIVE = -10;
	  public static final int C_OPCODE_SOLDIERGIVEOK = -11;
	  public static final int C_OPCODE_HOTEL_ENTER = -12;
	  public static final int C_OPCODE_SOLDIERBUY = -13;
	  public static final int C_OPCODE_HORUNOK = -176;
	  public static final int C_OPCODE_HORUN = -183; // ȣ��
	  public static final int C_OPCODE_SECURITYSTATUS = -125;
	  public static final int C_OPCODE_SECURITYSTATUSSET = -149;
	   public static final int C_OPCODE_WARTIMELIST = -151; // �����ð� ����Ʈ


	  
	  
	  
	  /** SERVER OPCODE */
	  
	  public static final int S_OPCODE_DEPOSIT = 4; // ���� �Ա�
	  public static final int S_OPCODE_INVLIST = 5; // �κ��丮�� �����۸���Ʈ
	  public static final int S_OPCODE_DETELECHAROK = 6; // �ɸ� ����
	  public static final int S_OPCODE_OWNCHARSTATUS = 8; // �ɸ� ���� ����
	  public static final int S_OPCODE_MOVEOBJECT = 10; // �̵� ������Ʈ
	  public static final int S_OPCODE_TRUETARGET = 11; // Ʈ��Ÿ��
	  public static final int S_OPCODE_ADDITEM = 15; // ������ ����[������ ���ɴٰ��Ա�]
	  public static final int S_OPCODE_SOUND = 22; // ���� ����Ʈ �κ�
	  public static final int S_OPCODE_SKILLBUY = 23; // ��ų ���� â
	  public static final int S_OPCODE_ITEMSTATUS = 24; // �κ� ������ ����
	  public static final int S_OPCODE_ITEMAMOUNT = 24; // �κ��� ������ ���� ���� �ٲٱ�(��ܾ��� ITEMSTATUS�� ���̿�)
	  public static final int S_OPCODE_ATTACKPACKET = 30; // ���� ǥ�� �κ�
	  public static final int S_OPCODE_MPUPDATE = 33; // MP ������Ʈ
	  public static final int S_OPCODE_LAWFUL = 34; // ���Ǯ
	  public static final int S_OPCODE_TRADEADDITEM = 35; // �ŷ�â ������ �߰� �κ�
	  public static final int S_OPCODE_ABILITY = 36; // �̹�, �ҹ�  ������ ���
	  public static final int S_OPCODE_SPMR = 37; // sp�� mr����
	  public static final int S_OPCODE_SHOWHTML = 39; // NpcŬ�� Html����
	  public static final int S_OPCODE_LIGHT = 40; // ���
	  public static final int S_OPCODE_RANGESKILLS = 42; // ���� �������� ��ų
	  public static final int S_OPCODE_CHANGENAME = 46; // ������Ʈ ���Ӻ����
	  public static final int S_OPCODE_CURSEBLIND = 47; // ���ֱ� ȿ��
	  public static final int S_OPCODE_NOTICE = 48; // ���� �븸: S_OPCODE_COMMONNEWS
	  public static final int S_OPCODE_TRADE = 52; // �ŷ�â �κ�
	  public static final int S_OPCODE_SELECTTARGET = 54; // �� ���� ��ǥ����
	  public static final int S_OPCODE_SKILLSOUNDGFX = 55; // ����Ʈ �κ� (���̽�Ʈ��)
	  public static final int S_OPCODE_TELEPORT = 566; // �ڷ���Ʈ
	  public static final int S_OPCODE_DELETEINVENTORYITEM = 57; // �κ��丮 ������ ����
	  public static final int S_OPCODE_PINKNAME = 60; // ������
	  public static final int S_OPCODE_RETURNEDSTAT = 64; //v ���� �ʱ�ȭ ����. �븸: S_OPCODE_CHARRESET
	  public static final int S_OPCODE_SHOWSHOPSELLLIST = 65; // ������ �Ǹ� �κ�
	  public static final int S_OPCODE_SKILLBRAVE = 67; // ���
	  public static final int S_OPCODE_BOARD = 68; // �Խ���
	  public static final int S_OPCODE_CASTLEMASTER = 69; // ��������� ����
	  public static final int S_OPCODE_SHOWSHOPBUYLIST = 70; // ���� ���� �κ�
	  public static final int S_OPCODE_SERVERMSG = 71; // ���� �޼���[���ߺ�����üũ]
	  public static final int S_OPCODE_WHISPERCHAT = 73; // �ӼӸ�
	  public static final int S_OPCODE_POLY = 76; // ����
	  public static final int S_OPCODE_NORMALCHAT = 81; // �Ϲ� ä��
	  public static final int S_OPCODE_SELECTLIST = 83; // �������
	  public static final int S_OPCODE_WAR = 84; // ����
	  public static final int S_OPCODE_RESURRECTION = 85; // ��Ȱ ó�� �κ�
	  public static final int S_OPCODE_SHOWOBJ = 87; // ������Ʈ �׸���. �븸: S_OPCODE_CHARPACK
	  public static final int S_OPCODE_BOOKMARKS = 92; // ��� ����Ʈ
	  public static final int S_OPCODE_CHARLIST = 93; //v �ɸ��͸���Ʈ�� �ɸ�����
	  public static final int S_OPCODE_NEWCHARWRONG = 98; // ĳ���� ������ ó���κ�
	  public static final int S_OPCODE_ITEMNAME = 100; // ������ ���� (Eǥ��)
	  public static final int S_OPCODE_LIQUOR = 103; // ��
	  public static final int S_OPCODE_EFFECTLOCATION = 106; // Ʈ�� (��ǥ�� ����Ʈ)
	  public static final int S_OPCODE_TRADESTATUS = 112; // �ŷ� ���, �Ϸ�
	  public static final int S_OPCODE_WEATHER = 115; // ���� �����ϱ�
	  public static final int S_OPCODE_EMBLEM = 118; // Ŭ�� ������ ��û
	  public static final int S_OPCODE_CHARVISUALUPDATE = 119; // ���� ��,Ż �κ�
	  public static final int S_OPCODE_REMOVE_OBJECT = 120; // ������Ʈ ���� (���etc)
	  public static final int S_OPCODE_CHANGEHEADING = 122; // ���� ��ȯ �κ�
	  public static final int S_OPCODE_GAMETIME = 123; // ���� �ð�
	  public static final int S_OPCODE_BLESSOFEVA = 126; // ���� ������
	  public static final int S_OPCODE_NEWCHARPACK = 127; // �ɸ� ���� ����� ������
	  public static final int S_OPCODE_INPUTAMOUNT = 136; // ������ ������ ���� ����
	  public static final int S_OPCODE_SERVERVERSION = 139; // ��������
	  public static final int S_OPCODE_PRIVATESHOPLIST = 140; // ���λ��� ��ǰ ����
	  public static final int S_OPCODE_DRAWAL = 141; // ���� ���
	  public static final int S_OPCODE_BOARDREAD = 148; // �Խ��� �б�
	  public static final int S_OPCODE_INITPACKET = 150; // Key��Ŷ������ opcode�κ��� �����Ѵ�. ���ٷ� ������ Ű ��Ŷ����  ���� �տ� �� ���ڸ� �θ� �ȴ�.
	  public static final int S_OPCODE_OWNCHARSTATUS2 = 155; // �������ͽ� ����(��ũ����,����) 
	  public static final int S_OPCODE_HOUSELIST = 156; // ����Ʈ ����Ʈ
	  public static final int S_OPCODE_DOACTIONGFX = 158; // �׼� �κ�(�´¸����)
	  public static final int S_OPCODE_DELSKILL = 160; // ��ų ���� (���ɷ� ����)
	  public static final int S_OPCODE_NPCSHOUT = 161; // ������ ��
	  public static final int S_OPCODE_ADDSKILL = 164;  // ��ų �߰�[������Ŷ�ڽ� ����]
	  public static final int S_OPCODE_POISON = 165; // ���� ���� ���� ǥ��
	  public static final int S_OPCODE_STRUP = 166; // ����
	  public static final int S_OPCODE_INVIS = 171; // ����
	  public static final int S_OPCODE_OWNCHARATTRDEF = 174; // AC �� �Ӽ���� ����
	  public static final int S_OPCODE_SHOWRETRIEVELIST = 176; // â�� ����Ʈ
	  public static final int S_OPCODE_CHARAMOUNT = 178; //v �ش� ������ �ɸ� ����
	  public static final int S_OPCODE_CHARTITLE = 183; // ȣĪ ����
	  public static final int S_OPCODE_TAXRATE = 185; // ���� ����
	  public static final int S_OPCODE_LETTER = 186; // ���� �б�. �븸: S_OPCODE_MAIL
	  public static final int S_OPCODE_HOUSEMAP = 187; // ����Ʈ ��
	  public static final int S_OPCODE_DEXUP = 188; // ������
	  public static final int S_OPCODE_PARALYSIS = 202; // �ൿ ���� (Ŀ���з� ����)
	  public static final int S_OPCODE_MAPID = 206; // �� ���̵�
	  public static final int S_OPCODE_UNDERWATER = 206;
	  public static final int S_OPCODE_ATTRIBUTE = 209; // ��ġ���� �̵�����&�Ұ��� ���� �κ�
	  public static final int S_OPCODE_SKILLICONSHIELD = 216; // ����
	  public static final int S_OPCODE_YES_NO = 219; // [ Y , N ] �޼���
	  public static final int S_OPCODE_UNKNOWN1 = 223; // ���Ӵ�� �븸: S_OPCODE_LOGINTOGAME
	  public static final int S_OPCODE_HPUPDATE = 225; // HP ������Ʈ
	  public static final int S_OPCODE_DISCONNECT = 227; // �ش� �ɸ� ���� ����
	  public static final int S_OPCODE_LOGINRESULT = 233; // �α��� ó���Ǵ��� �亯
	  public static final int S_OPCODE_HPMETER = 237; // �̴� HPǥ�� �κ�
	  public static final int S_OPCODE_ITEMCOLOR = 240; // ���� �ֹ���
	  public static final int S_OPCODE_MSG = 243; // �ý��� �޼��� (��ê). �븸: S_OPCODE_GLOBALCHAT
	  public static final int S_OPCODE_IDENTIFYDESC = 245; // Ȯ���ֹ���
	  public static final int S_OPCODE_PACKETBOX = 250; //v ���� ��Ŷ ���� ���
	  public static final int S_OPCODE_SKILLHASTE = 255; // ���̽�Ʈ
	  
	 
	  
	  public static final int S_OPCODE_SPOLY = 908;
	  /////////////////////////
	  
	  
	  
	  public static final int S_OPCODE_DRAGONPERL = 31;
	  public static final int S_OPCODE_BOOKMARK_LOAD                 = 33;
	  public static final int S_OPCODE_PETCTRL = 33; //�꿡 ���� ������ �����ֱ� 
	  public static final int S_OPCODE_NEWMASTER = 66; //���ջ̰� ������ ����
	  public static final int S_OPCODE_CLANMATCHING = 192;//���͸�Ī
	  

	  

	  public static final int S_OPCODE_EXP = 1000001; // ����ġ ����
	  public static final int S_OPCODE_WARTIME = 1000003;
	  public static final int S_OPCODE_BLUEMESSAGE = 1000005; // ������ Ƚ�� �޽���[REDMESSAGE]
	  public static final int S_OPCODE_HORUN = 1000006; // ȣ�� ���� ����â
	  public static final int S_OPCODE_USEMAP = 1000007;
	  public static final int S_OPCODE_HOTELENTER = 1000008;
	  public static final int S_OPCODE_SHORTOFMATERIAL = 1000009;
	  public static final int S_OPCODE_SOLDIERBUYLIST = 1000010; // �� �뺴 ���
	  public static final int S_OPCODE_SOLDIERGIVE = 1000011; // ������ �뺴 �ֱ�
	  public static final int S_OPCODE_SOLDIERGIVELIST = 1000012; // ����� �뺴�� ��ġ
	  public static final int S_OPCODE_HIRESOLDIER = 22224;





}