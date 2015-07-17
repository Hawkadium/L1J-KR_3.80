package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import server.LineageClient;

import l1j.server.server.model.L1World;
import l1j.server.server.datatables.ReportTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_Reports extends ClientBasePacket {

	private static final String C_REPORTS = "[C] C_Reports";
	private static Logger _log = Logger.getLogger(C_Reports.class.getName());
	

	public C_Reports(byte abyte0[], LineageClient client)
			throws Exception {
		super(abyte0);		
		int type = readC(); // Ÿ��
		int objid = readD(); // �ɸ��� ������Ʈ	
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		L1PcInstance target = (L1PcInstance) L1World.getInstance().findObject(objid);
		
		if (!pc.isReport()) {
			pc.sendPackets(new S_ServerMessage(1021)); // ����� �ٽ� �Ű� ���ּ���.
			return;
		}
		
		if (target != null) { // objid �� null �� �ƴ϶��  
			if (!ReportTable.getInstance().name.contains(target.getName())) { 
				ReportTable.getInstance().name.add(target.getName());
				ReportTable.getInstance().report(target.getName(), pc.getName()); // DB�� ���
				pc.sendPackets(new S_ServerMessage(1019)); // ��ϵǾ����ϴ�.
				pc.startReportDeley();
			} else {
				pc.sendPackets(new S_ServerMessage(1020)); // �̹� ��� �Ǿ����ϴ�.
			}
		} 
	}

	@Override
	public String getType() {
		return C_REPORTS;
	}
}







