package l1j.server.Warehouse;

// Ư��â��
public class SpecialWarehouseList extends WarehouseList {
	@Override
	protected SpecialWarehouse createWarehouse(String name) {
		return new SpecialWarehouse(name);
	}
}
