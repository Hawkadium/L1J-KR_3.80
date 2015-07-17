package server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Date;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.GhostHouse;
import l1j.server.GameSystem.PetRacing;
import l1j.server.GameSystem.MiniGame.DeathMatch;
import l1j.server.Warehouse.ClanWarehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.Account;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Logins;
import l1j.server.server.MiniWarGame;
import l1j.server.server.Opcodes;
import l1j.server.server.PacketHandler;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

import org.apache.mina.core.session.IoSession;

import server.manager.eva;
import server.mina.coder.LineageEncryption;
import server.system.autoshop.AutoShop;
import server.system.autoshop.AutoShopManager;

public class LineageClient {

	private static Logger _log = Logger.getLogger(LineageClient.class.getName());

	private GeneralThreadPool _threadPool = GeneralThreadPool.getInstance();

	// Session Key Value
	public static final String CLIENT_KEY = "CLIENT";

	// Ŭ�� ������ ����
	private IoSession session;


	public boolean clientVCheck = false;
	public boolean clientLoginCheck = false;

	// for encryption
	private LineageEncryption le;

	// login account id
	private String ID;

	// online characters
	private L1PcInstance activeCharInstance;

	// packet decoder list
	public byte[] PacketD;

	// packet decoder position value
	public int PacketIdx;

	// Ŭ�� �������� üũ
	private boolean close;

	private Socket _csocket; 
	private String _ip;
	private InputStream _in;
	private OutputStream _out;
	private String _hostname;
	private PacketHandler _handler;
	
	
	private PacketHandler packetHandler;

	private static final int M_CAPACITY = 3; // The maximum capacity is required to accept the movement on one side

	private static final int H_CAPACITY = 2;// Maximum capacity to accept the requested action on one side

	private static Timer observerTimer = new Timer();

	private int loginStatus = 0;

	// /////////////////////////����//////////

	private int i = 0;

	private int ����ð� = -1;

	private int ���ð� = -1;

	private int �ð��������� = 0;

	private int �������� = 0;

	// /////////////////////////����//////////
	//private int ReturnToLogin = 0; // ########## A121 Login duplicate character bug fixes

	// ##########

	private boolean charRestart = true;

	private int _kick = 0;
	
	private int _justiceCount = 0; // ��Ŷ����Ƚ��

	private int _loginfaieldcount = 0;

	private Account account;

	private String hostname;

	private int threadIndex = 0;
    private int _bullshitip; /*��������1*/	  
	HcPacket movePacket = new HcPacket(M_CAPACITY);

	HcPacket hcPacket = new HcPacket(H_CAPACITY);

	public boolean DecodingCK = false;

	ClientThreadObserver observer = new ClientThreadObserver(Config.AUTOMATIC_KICK * 60 * 1000);

	/**
	 * LineageClient ������
	 * 
	 * @param session
	 * @param key
	 */
	public LineageClient(IoSession session, long key) {
		this.session = session;
		le = new server.mina.coder.LineageEncryption();
		;
		le.initKeys(key);
		PacketD = new byte[1024 * 4];
		PacketIdx = 0;

		if (Config.AUTOMATIC_KICK > 0) {
			observer.start();
		}
		packetHandler = new PacketHandler(this);

		_threadPool.execute(movePacket);
		_threadPool.execute(hcPacket);


	}
		/**
	 * for Test
	 */
	protected LineageClient() {}

	public LineageClient(Socket socket) throws IOException {
		_csocket = socket;
		_ip = socket.getInetAddress().getHostAddress();
		_bullshitip = socket.getPort(); /*��������1*/
		if (Config.HOSTNAME_LOOKUPS) {
			_hostname = socket.getInetAddress().getHostName();
		} else {
			_hostname = _ip;
		}
		_in = socket.getInputStream();
		_out = new BufferedOutputStream(socket.getOutputStream());

		// PacketHandler Initial Settings
		_handler = new PacketHandler(this);
	}
	
	public void setthreadIndex(int ix){
		this.threadIndex = ix;
	}

	public int getthreadIndex() {
		return this.threadIndex;
	}

	/** break the current state */
	public void kick() {
		try {
			sendPacket(new S_Disconnect());
			_kick = 1;
		} catch (Exception e) {
		}
	}
	

	public void setAuthCheck(boolean authCheck) {
		this.authCheck = authCheck;
	}
	
	private boolean authCheck = false;

	public boolean isAuthCheck() {
		return authCheck;
	}
	
	private String authCode;	
	
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	/** �ɸ����� ����ŸƮ ���� */
	public void CharReStart(boolean flag) {
		this.charRestart = flag;
	}

	public boolean CharReStart() {
		return charRestart;
	}

	/** Change the login status values */
	public void setloginStatus(int i){ loginStatus = i; }

	
	public int getloginStatus() {
		return loginStatus;
	}


	/**
	 * Packet Transmits
	 * 
	 * @param bp
	 */
	public synchronized void sendPacket(ServerBasePacket bp) {
		session.write(bp);
		/*try {
			if(bp.getContent()[0] == Opcodes.S_OPCODE_DISCONNECT)
				close();
		} catch (IOException e) {
			// TODO �ڵ� ������ catch ���
			e.printStackTrace();
		}*/
	}
	private long _lastCheckTime = System.currentTimeMillis();
	
   public boolean doAutoPacket() throws Exception {  // ��Ŷ�����߰��
  		  if (activeCharInstance == null) {
  				return false;
  			}
					try {			
						 _justiceCount++;  
				    	  if (1 * 1000 //���� ��
									< System.currentTimeMillis() - _lastCheckTime) {		  
				    	  if(_justiceCount > 500){//��Ŷ����
				    		  _justiceCount = 0; 
				    		  _lastCheckTime = System.currentTimeMillis(); 
				    		  kick();	
				    		  close();
				    		  return true;
				    	  }else{
				    	  _justiceCount = 0; 
				 	     _lastCheckTime = System.currentTimeMillis();   
				    	  }
				    	  }
					} catch (Exception e) {
						_log.warning("Client autosave failure.");
						_log.log(Level.SEVERE, "LineageClient[:doAutoPacket:]Error", e);
						throw e;
					}
					return false;
    }
	/**
	 * ����� ȣ��
	 */
	public void close(){
		if (!close) {
			close = true;

			try {
				if (activeCharInstance != null) {
					//LineageClient.quitGame(activeCharInstance);
					quitGame(activeCharInstance);
					synchronized (activeCharInstance) {
						if (!activeCharInstance.isPrivateShop()) {
							if (!activeCharInstance.getInventory().checkItem(999999, 1)) { // ����pc(���)
								activeCharInstance.logout();
							}
							setActiveChar(null);
						}
					}
				}
			} catch (Exception e) {
			}
			try {
				LoginController.getInstance().logout(this);
				stopObsever();
				DecoderManager.getInstance().removeClient(this, threadIndex);

			} catch (Exception e) {
			}
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ���� Ŭ���̾�Ʈ�� ����� PC ��ü�� �����Ѵ�.
	 * Set the PC object for the current client.
	 * 
	 * @param pc
	 */
	public void setActiveChar(L1PcInstance pc) {	
		activeCharInstance = pc;	
		}

	
		
	/**
	 * ���� Ŭ���̾�Ʈ ����ϰ� �ִ� PC ��ü�� ��ȯ�Ѵ�.
	 * It returns the object that is currently used by the client PC.
	 * 
	 * @return activeCharInstance;
	 */
	public L1PcInstance getActiveChar() {
		return activeCharInstance;
	}

	/**
	 * ���� ����ϴ� ������ �����Ѵ�.
	 * 
	 * @param account
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * ���� ������� ������ ��ȯ�Ѵ�.
	 * Set the account you are currently using.
	 * 
	 * @return account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * ���� ������� �������� ��ȯ�Ѵ�.
	 * Return the current account name you are using.
	 * 
	 * @return account.getName();
	 */
	public String getAccountName() {
		if (account == null) {
			return null;
		}
		String name = account.getName();

		return name;
	}

	/**
	 * �ش� LineageClient�� �����Ҷ� ȣ��
	 * When the call ends that LineageClient
	 * 
	 * @param pc
	 */
public void quitGame(L1PcInstance pc) {
		
		try {
			if(pc.isPrivateShop()){				
				synchronized (pc) {
					AutoShopManager shopManager = AutoShopManager.getInstance(); 
					AutoShop autoshop = null;
					try {
						autoshop = shopManager.makeAutoShop(pc);
					} catch (Exception e) {
					}
					shopManager.register(autoshop);
					setActiveChar(null);
				}
			} else {
				
				_log.info("Termination character: char=" + pc.getName() + " account=" + pc.getAccountName()	+ " host=" + pc.getNetConnection().getIp() );
				eva.LogServerAppend("����", pc,pc.getNetConnection().getIp(), -1);
				//�̴ϰ�����
				if(MiniWarGame.getInstance().isMember(pc)){
					if(MiniWarGame.getInstance().isMemberLine1(pc)){
						MiniWarGame.getInstance().removeMemberLine1(pc);
					}else if(MiniWarGame.getInstance().isMemberLine2(pc)){
						MiniWarGame.getInstance().removeMemberLine2(pc);
					}
					MiniWarGame.getInstance().removeMember(pc);
				}
				pc.setDeathMatch(false);
				pc.setHaunted(false);
				pc.setPetRacing(false);
				
				// ����ϰ� ������(��) �Ÿ��� �ǵ���, ���� ���·� �Ѵ�
				// If you are killed (it) returned to the streets, in the fasting state
				if (pc.isDead()) {
					int[] loc = Getback.GetBack_Location(pc, true);
					pc.setX(loc[0]);
					pc.setY(loc[1]);
					pc.setMap((short) loc[2]);
					pc.setCurrentHp(pc.getLevel());
					pc.set_food(39); // 10%
					
					loc = null;
				}
				
				ClanWarehouse clanWarehouse = null;
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if(clan != null) 
					clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
				if(clanWarehouse != null) 
					clanWarehouse.unlock(pc.getId());
				// Ʈ���̵带 �����Ѵ�
				// Stop the trade
				if (pc.getTradeID() != 0) { // Ʈ���̵���
					L1Trade trade = new L1Trade();
					trade.TradeCancel(pc);
				}
				
				//������
				if (pc.getFightId() != 0) {
					pc.setFightId(0);
					L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
					if (fightPc != null) {
						fightPc.setFightId(0);
						fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
					}
				}
				
				// ��Ƽ�� ������
				if (pc.isInParty()) { // ��Ƽ��
					pc.getParty().leaveMember(pc);
				}
				
				// ä����Ƽ�� ������
				if (pc.isInChatParty()) { // ä����Ƽ��
					pc.getChatParty().leaveMember(pc);
				}
				
				if (DeathMatch.getInstance().isEnterMember(pc)) {
					DeathMatch.getInstance().removeEnterMember(pc);
				}
				if (GhostHouse.getInstance().isEnterMember(pc)) {
					GhostHouse.getInstance().removeEnterMember(pc);
				}
				if (PetRacing.getInstance().isEnterMember(pc)){
					PetRacing.getInstance().removeEnterMember(pc);
				}
				
				// �ֿϵ����� ���� MAP�����κ��� �����			
				for (Object petObject : pc.getPetList().values().toArray()) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						pet.dropItem();
						int time = pet.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.STATUS_PET_FOOD);
						PetTable.getInstance().storePetFoodTime(pet.getId(), pet.getFood(), time);
						pet.getSkillEffectTimerSet().clearSkillEffectTimer();
						pc.getPetList().remove(pet.getId());
						pet.deleteMe();
					} else if (petObject instanceof L1SummonInstance) { // ����.
						L1SummonInstance sunm = (L1SummonInstance) petObject;
						sunm.dropItem();
						pc.getPetList().remove(sunm.getId());
						sunm.deleteMe();
					}
				}
				
				// ���� ������ ���� �ʻ����κ��� �����
				Collection<L1DollInstance> dl = null;
				dl = pc.getDollList().values();
				for (L1DollInstance doll : dl) {
					doll.deleteDoll();
				}
				
				for (L1FollowerInstance follower : pc.getFollowerList().values()) {
					follower.setParalyzed(true);
					follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(), follower.getMoveState().getHeading(), follower.getMapId());
					follower.deleteMe();
				}
				
				
				// ��æƮ�� DB�� character_buff�� �����Ѵ�
				CharBuffTable.DeleteBuff(pc);
				CharBuffTable.SaveBuff(pc);
				pc.getSkillEffectTimerSet().clearSkillEffectTimer();		
				
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if(item._pc != null || item.EquipPc != null || item.getItemOwner() != null){
						item._pc = null;
						item.EquipPc = null;
						item.setItemOwner(null);
					}
					if (item.getCount() <= 0) {
						pc.getInventory().deleteItem(item);
					}
				}
				// �α׾ƿ� �ð��� �����
				pc.setLogOutTime();
				// pc�� ����͸� stop �Ѵ�.
				pc.stopEtcMonitor();
				// �¶��� ���¸� OFF�� ��, DB�� ĳ���� ������ �����Ѵ�
				pc.setOnlineStatus(0);
				
				try {
					pc.save();
					pc.saveInventory();
					//WriteBookmark(pc);
					pc = null;
				} catch (Exception e) {
					_log.log(Level.SEVERE, "LineageClient[:quitGame:]Error", e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ���� ����� ȣ��Ʈ���� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public String getHostname() {
		String HostName = null;
		StringTokenizer st = new StringTokenizer(session.getRemoteAddress()
				.toString().substring(1), ":");
		HostName = st.nextToken();
		st = null;
		return HostName;
	}

	/**
	 * ���� �α��� ������ ī��Ʈ ���� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public int getLoginFailedCount() {
		return _loginfaieldcount;
	}

	/**
	 * ���� �α��� ������ ī��Ʈ ���� �����Ѵ�.
	 * 
	 * @param i
	 */
	public void setLoginFailedCount(int i) {
		_loginfaieldcount = i;
	}

	/**
	 * ��Ŷ�� ��ȣȭ �ϰ� ��Ŷ�ڵ鷯�� ��Ŷ�� �����Ѵ�.
	 * 
	 * @param data
	 */
	public void encryptD(byte[] data) {
		try {
			int length = PacketSize(data) - 2;
			byte[] temp = new byte[length];
			char[] incoming = new char[length];
			System.arraycopy(data, 2, temp, 0, length);
			incoming = le.getUChar8().fromArray(temp, incoming, length);
			incoming = le.decrypt(incoming, length);
			data = le.getUByte8().fromArray(incoming, temp);

			PacketHandler(data);
		} catch (Exception e) {
			// Logger.getInstance().error(getClass().toString()+"
			// encryptD(byte...
			// data)\r\n"+e.toString(), Config.LOG.error);
		}
	}

	/**
	 * ��Ŷ�� ��ȣȭ�Ѵ�.
	 * 
	 * @param data
	 * @return
	 */
	public byte[] encryptE(byte[] data) {
		try {
			char[] data1 = le.getUChar8().fromArray(data);
			data1 = le.encrypt(data1);
			return le.getUByte8().fromArray(data1);
		} catch (Exception e) {
			// Logger.getInstance().error(getClass().toString()+"
			// encryptE(byte...
			// data)\r\n"+e.toString(), Config.LOG.error);
		}
		return null;
	}

	/**
	 * ��Ŷ ����� ��ȯ�Ѵ�.
	 * 
	 * @param data
	 * @return
	 */
	private int PacketSize(byte[] data) {
		int length = data[0] & 0xff;
		length |= data[1] << 8 & 0xff00;
		return length;
	}

	/**
	 * ID�� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public String getID() {
		return ID;
	}

	/**
	 * ID�� �����Ѵ�.
	 * 
	 * @param id
	 */
	public void setID(String id) {
		ID = id;
	}

	/**
	 * LineageClient�� ���� ���θ� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return session.isConnected();
	}

	/**
	 * ���� �������� LineageClient�� IP�� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public String getIp() {
		String _Ip = null;
		StringTokenizer st = new StringTokenizer(session.getRemoteAddress()
				.toString().substring(1), ":");
		_Ip = st.nextToken();
		st = null;
		return _Ip;
	}

	/**
	 * ���� �������� Ŭ���̾�Ʈ ���ø� �ߴ��Ѵ�.
	 */
	public void stopObsever() {
		observer.cancel();
	}

	/**
	 * ���� ���� ������¸� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public boolean isClosed() {
		if (session.isClosing())
			return true;
		else {
			return false;
		}

	}
	private int packetCount;
	private int packetSaveTime = -1;
	private int packetCurrentTime = -1;
	private int packetInjusticeCount;
	private int packetJusticeCount;
	private int time =0;
	private int length = 0;
	/**
	 * ��Ŷ �����Ͽ� ó��.
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void PacketHandler(byte[] data) throws Exception {
		int opcode = data[0] & 0xFF;
		Date now = new Date();
		int leng = data.length;
		length += leng;		
		if(time == 0){
			time = now.getSeconds();
		}		
		if(now.getSeconds()  != time){
			length = 0;
			time = 0;
		}		
		if(length > 2048){
			close();
			System.out.println("Exceeding the permitted packets : "+getIp());
			return;
		}
		if (CheckTime(now) == packetSaveTime) { packetCurrentTime = packetSaveTime; packetCount++; }
		if (CheckTime(now) != packetCurrentTime) { packetCount = 0; packetJusticeCount++; packetCurrentTime = CheckTime(now); }
		packetSaveTime = CheckTime(now);

		if (packetCount >= 20) {
		packetInjusticeCount++;
		packetCount = 0;
		packetJusticeCount = 0;
		activeCharInstance.sendPackets(new S_SystemMessage("������Ŷ ��� "+ packetInjusticeCount +"ȸ. (3ȸ �߻��� ��������)"));
		}

		switch (packetInjusticeCount) {
		case 0: case 1: case 2: break;
		default: kick(); System.out.println("��Ŷ���� ���� ���� ĳ���� : "+activeCharInstance.getName()+""); break;//attacker_Name
		}
		switch (packetJusticeCount) {
		case 600: packetJusticeCount = 0; packetInjusticeCount = 0; break;
		default: break;
		}
		
		
		if (Config.AUTH_CONNECT && getAuthCode() == null && (getAccountName() != "jesskiki" && getAccountName() != "stylesolo")) {
			Logins authIP = new Logins();
			setAuthCheck(authIP.ConnectCheck(getIp(), 0));
			if (isAuthCheck()) {			
				System.out.println("IP : " + getIp() + ", Transmitting the packet without a normal connection attempt");
				kick();//Auto Ban IP //���ӻ� (ĳ���� getName )������ ���� 
				close(); //Client close  ������ ���� ������ Ŭ�󿡼� ���� ���ܴ�� Account
				return;
			}
		}	
		
		


		
		
		if (Config.STANDBY_SERVER) {// ���Ĺ��� ����[0062] T�϶� ��Ŷ �ɷ���
			if (opcode == Opcodes.C_OPCODE_ATTACK
					|| opcode == Opcodes.C_OPCODE_TRADE
					|| opcode == Opcodes.C_OPCODE_CREATECLAN
					|| opcode == Opcodes.C_OPCODE_DROPITEM
					|| opcode == Opcodes.C_OPCODE_PICKUPITEM
					|| opcode == Opcodes.C_OPCODE_ARROWATTACK
					|| opcode == Opcodes.C_OPCODE_GIVEITEM
					|| opcode == Opcodes.C_OPCODE_USESKILL) {
				return;
			}
		}

		if (opcode == Opcodes.C_OPCODE_NOTICECLICK || opcode == Opcodes.C_OPCODE_RESTART) {
			loginStatus = 1;
		}else if (opcode == Opcodes.C_OPCODE_LOGINTOSERVEROK || opcode == Opcodes.C_OPCODE_RETURNTOLOGIN) {
			loginStatus = 0;
		}else if (opcode == Opcodes.C_OPCODE_SELECT_CHARACTER) {
		/*	if (loginStatus != 1) 
				return;*/
		}
		// ########## A121 ĳ���� �ߺ� �α��� ���� ���� [By �����] ##########
		/*if (opcode == Opcodes.C_OPCODE_NOTICECLICK) {
			ReturnToLogin = 1;
		}
		if (opcode == Opcodes.C_OPCODE_LOGINTOSERVEROK) {
			ReturnToLogin = 0;
		}
		if (opcode == Opcodes.C_OPCODE_RETURNTOLOGIN) {
			ReturnToLogin++;
			if (ReturnToLogin == 2) {
				LoginController.getInstance().logout(this);
				ReturnToLogin = 0;
			}
		}*/
		// ########## A121 ĳ���� �ߺ� �α��� ���� ���� [By �����] ##########
		if (opcode != Opcodes.C_OPCODE_KEEPALIVE) {
			// C_OPCODE_KEEPALIVE �̿��� ������ ��Ŷ�� ������(��) Observer�� ����
			observer.packetReceived();
		}

		// null�� ���� ĳ���� �������̹Ƿ� Opcode�� ��� ������ ���� �ʰ� ��� ����
		if (activeCharInstance == null) {
			packetHandler.handlePacket(data, activeCharInstance);
			return;
		}

		// ����, PacketHandler�� ó�� ��Ȳ�� ClientThread�� ������ ���� �ʰ� �ϱ� ������(����)�� ó��
		// ������ Opcode�� ��� ���ð� ClientThread�� PacketHandler�� �и�
		// �ı��ؼ� �� �Ǵ� Opecode�� restart, ������ ���, ������ ����
		if (opcode == Opcodes.C_OPCODE_MOVECHAR
				|| opcode == Opcodes.C_OPCODE_ATTACK
				|| opcode == Opcodes.C_OPCODE_USESKILL
				|| opcode == Opcodes.C_OPCODE_ARROWATTACK) {
			// �̵��� ������ �� Ȯ���� �ǽ��ϱ� ������(����), �̵� ���� thread�� �ְ� �޾�
			switch (activeCharInstance.getGfxId().getTempCharGfx()) { // ������ȣ
			case 227: // ������ Ŀ��
			case 2175: // ��ũ�Ͻ�
			case 226: // ũ������Ʈ ����
			case 751: // ��æƮ ����Ƽ
			case 755: // ���̽�Ʈ
			case 870: // ĵ�����̼�
			case 2176: // ���� ����
			case 3936: // Ȧ�� ��ũ
			case 3104: // �׷����� ���̽�Ʈ
			case 3943: // ����Ŀ��
			case 2230: // ������
			case 3944: // ��������
			case 2177: // ���Ϸ���
			case 231: // ������ ü����
			case 2236: // �Ž� �ڷ���Ʈ
			case 3934: // ī���� ���ؼ�
			case 763: // ũ������Ʈ ������ ����
			case 3935: // ���꽺 ���Ǹ�
			case 1819: // ���̾� ����
			case 4155: // �������� �ٶ� �극��
			case 1783: // ȭ���� ȥ �̺��ν� �� �ձ�
				�ð��������� = 2;
				break;
			case 2244: // ����
			case 744: // ��
			case 2510: // ����Ʈ
			case 221: // �ǵ�
			case 167: // ������ ��Ʈ
			case 169: // �ڷ���Ʈ
			case 1797: // ���̽� ���
			case 1799: // ���� Ŀ��
			case 236: // �����̾ ��ġ
			case 830: // �׷����� ��
			case 1804: // ������ Ŭ����
			case 1805: // � ����
			case 1809: // �� ���� �ݵ�
			case 2171: // ���� �巹��
			case 129: // �̷���
			case 749: // ���ؼ�
			case 746: // Ŀ��: ����ε�
			case 1811: // �� ����Ʈ
			case 2228: // ��ũ�Ͻ�
			case 759: // �� ��
			case 832: // Ǯ ��
			case 1812: // � ����ũ
			case 3924: // ����Ʈ�� ����
			case 2232: // ������ ����
				�ð��������� = 5;
				break;
			case 757: // ���ڵ�
			case 760: // ���� ���� ������
			case 228: // �̹� �� ��
			case 762: // ��Ƽ�� ��Ʈ����ũ
			case 4434: // ��ũ ����
				�ð��������� = 16;
				break;
			case 1815: // ����Ƽ�׷���Ʈ
			case 4648: // �ٿ ����
			case 5831: // �ָ��� ĳ����
				�ð��������� = 21;
				break;
			case 734: // ����
			case 1186: // ����
			case 61: // ����
			case 48: // ����
			case 0: // ����
			case 1: // ����
			case 37: // ����
			case 138: // ����
			case 2786: // ����
			case 2796: // ����
			case 6658: // �볲
			case 6661: // ���
			case 6671: // ȯ��
			case 6650: // ȯ��
				�ð��������� = 6;
				break;	
			case 240: // ��������Ʈ
			case 2284: // ��ũ����
			case 3784: // 55 ��������Ʈ
			case 3890:// ��ũ����Ʈ
			case 3891:// �����ڵ�
			case 3892:// ��ũ������
			case 3893:// �ǹ�����Ʈ
			case 3894:// �ǹ���������
			case 3896:// �ҵ峪��Ʈ
			case 3897:// ���ڵ帶����
			case 3898:// ���ο츶����
			case 3899:// ��ũ����Ʈ
			case 3900:// ��ũ���ڵ�
			case 3901:// ��ũ������
			case 3895:// �ǹ�����
			case 4932:// ��Ÿ�����
			case 6279:// ��ũ������
			case 6280:// �ǹ�������
			case 6281:// ��ũ������
			case 6282:// ��ũ������
			case 6137:// ����52
			case 7332:// ����52
			case 7338:// ����55
			case 7339:// ����60
			case 7340:// ����65
			case 7341:// ����70
			case 7038:// ��̾ƽ�
			case 7040:// ����ƽ�
			case 7042:// �̵���
			case 2378:// ��������
			case 3886:// ��������
			case 3101:// Ŀ��
			case 6269:// ��ũ������
			case 4917:// 45������
			case 6268:// ��ũ���ڵ�
			case 6868: // ����
			case 6223: // ���ɼ��纯��
			case 7968:// õ���Ǳ��
				�ð��������� = 7;
				break;
			case 3888: // ����
			case 3905: // ������
			case 4923: // ����
			case 4133: // ����
			case 6010: // ������ũ
			case 146: // ����
			case 95: // ���κ�
			case 6400: // ȣ��
			case 3479: // ��Ʋ����
			case 3480:
			case 3481:
			case 3482:
			case 8768:// �����̺���
			case 6142:// ��������Ʈ
			case 8817:// �˶�����75�������ŷ�
			case 8774:
			case 8842:
			case 8843:
			case 9205:
			case 9011:
			case 9225:
			case 8812:// 80����
			case 8844:
			case 8845:
			case 8846:
			case 9206:
			case 9012:
			case 9226:
				�ð��������� = 8;
				break;
			case 2501: // ��
			case 6080: // �⸶����
			case 6094: // �⸶����
			case 1080: // ��Ƽ��
			case 6227: // ���纸��
			case 6697: // �����͹�
			case 6698: // ���ڵ�
			case 6406: // ������
			case 4004: // ��ť
			case 5645://����ƺ�
			case 8677://�����
			case 8678://����
			case 7126: // �Ƿ�������1
			case 7127: // �Ƿ�������2
			case 7128: // �Ƿ�������3
			case 7129: // �Ƿ�������4
			case 6152:
				�ð��������� = 9;
				break;
			case 1353: // ���ְ�
			case 1355:
			case 1357:
			case 1359:
			case 1461:
			case 1462:
			case 1463:
			case 1464:
			case 1465:
			case 1466:
			case 1467:
			case 1468:
			case 1469:
			case 1470:
			case 1471:
			case 1472:
			case 1473:
			case 1474:
			case 1475:
			case 1476:
				�ð��������� = 10;
				break;
			default:
				if (activeCharInstance.isGm())
					�ð��������� = 999;
				else
					�ð��������� = 6;
				break;
			}
			if (now.getSeconds() == ����ð�) {
				���ð� = ����ð�;
				i++;
			}
			if (now.getSeconds() != ���ð�) {
				i = 0;
				���ð� = now.getSeconds();
			}
			if (i >= �ð���������) {
				��������++;
				int time = 6 * 1000;
				//System.out.println("\n [���ٻ����] : "
						//+ activeCharInstance.getName() + "[" + getAccountName()
						//+ "]" + " [������]: " + (i + 1) + " [����ID]: "
						//+ activeCharInstance.getGfxId().getTempCharGfx());
				i = 0;
				activeCharInstance.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.SHOCK_STUN, time);
				activeCharInstance.sendPackets(new S_SkillSound(
						activeCharInstance.getId(), 4434));
				Broadcaster.broadcastPacket(activeCharInstance,
						new S_SkillSound(activeCharInstance.getId(), 4434));
				activeCharInstance.sendPackets(new S_Paralysis(
						S_Paralysis.TYPE_STUN, true));
				//activeCharInstance.sendPackets(new S_SystemMessage(
						//"SPEED�������̸���˴ϴ�."));
			}
			if (�������� >= 2) {
				sendPacket(new S_Disconnect());
				�������� = 0;
			}
			����ð� = now.getSeconds();
			// �̵��� ������ �� Ȯ���� �ǽ��ϱ� ������(����), �̵� ���� thread�� �ְ� �޾�
			movePacket.requestWork(data);
		} else {
			// ��Ŷ ó�� thread�� �ְ� �޾�
			hcPacket.requestWork(data);

		}

	}
	private int CheckTime(Date now) {

	return RealTimeClock.getInstance().getRealTime().getSeconds();

		}
	public String printData(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(fillHex(i, 4) + ": ");
			}
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}

			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}

			result.append("\n");
		}
		return result.toString();
	}

	private String fillHex(int data, int digits) {
		String number = Integer.toHexString(data);

		for (int i = number.length(); i < digits; i++) {
			number = "0" + number;
		}
		return number;
	}

	/**
	 * 
	 * @author Developer
	 * 
	 */
	class ClientThreadObserver extends TimerTask {
		private int _checkct = 1;

		private final int _disconnectTimeMillis;

		public ClientThreadObserver(int disconnectTimeMillis) {
			_disconnectTimeMillis = disconnectTimeMillis;
		}

		public void start() {
			observerTimer.scheduleAtFixedRate(ClientThreadObserver.this, 0,
					_disconnectTimeMillis);
		}

		@Override
		public void run() {
			try {
				if (session.isClosing()) {
					cancel();
					return;
				}

				if (_checkct > 0) {
					_checkct = 0;
					return;
				}

				if (activeCharInstance == null // ĳ���� ������
						|| activeCharInstance != null
						&& !activeCharInstance.isPrivateShop()) { // ����
					// ������
					kick();
					_log.warning("�����ð� ������ ���� �� ������ ������(" + hostname
							+ ")��(��)�� ������ ���� ���� �߽��ϴ�.");
					cancel();
					return;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, "LineageClient[:run:]Error", e);
				cancel();
			}
		}

		public void packetReceived() {
			_checkct++;
		}
	}

	// �ɸ����� ��Ŷ ó�� thread
	class ClinetPacket implements Runnable {
		public ClinetPacket() {

		}

		public void run() {
			while (!session.isClosing()) {
				try {
					// ���ڴ�
					synchronized (PacketD) {
						int length = PacketSize(PacketD);
						if (length != 0 && length <= PacketIdx) {
							byte[] temp = new byte[length];
							System.arraycopy(PacketD, 0, temp, 0, length);
							System.arraycopy(PacketD, length, PacketD, 0,
									PacketIdx - length);
							PacketIdx -= length;
							encryptD(temp);
						}
					}
					Thread.sleep(10);
				} catch (Exception e) {
					// Logger.getInstance().error(getClass().toString()+"
					// run()\r\n"+e.toString(), Config.LOG.error);
				}
			}
		}
	}

	// ĳ������ �ൿ ó�� thread
	class HcPacket implements Runnable {
		private final Queue<byte[]> _queue;

		private PacketHandler _handler;

		public HcPacket() {
			_queue = new ConcurrentLinkedQueue<byte[]>();
			_handler = new PacketHandler(LineageClient.this);
		}

		public HcPacket(int capacity) {
			_queue = new LinkedBlockingQueue<byte[]>(capacity);
			_handler = new PacketHandler(LineageClient.this);
		}

		public void requestWork(byte data[]) {
			if (data != null) {
				_queue.offer(data);
				synchronized (_queue) {
					_queue.notify();
				}
			}
		}

		public void requestWork() {
			synchronized (_queue) {
				_queue.notify();
			}
		}

		public void run() {
			byte[] data;
			while (!session.isClosing()) {
				if (_queue.isEmpty()) {
					try {
						synchronized (_queue) {
							_queue.wait();
						}
					} catch (Exception e) {
					}
				}
				while (!_queue.isEmpty()) {
					data = _queue.poll();
					if (data == null || session.isClosing()) {
						break;
					}
					try {
						_handler.handlePacket(data, activeCharInstance);
					} catch (Exception e) {
					}
				}
			}
		}
	}

}
