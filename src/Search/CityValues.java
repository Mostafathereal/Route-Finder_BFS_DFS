package Search;

public enum CityValues {
	
	Boston(0),
	NewYorkCity(1),
	Philadelphia(2),
	Baltimore(3),
	Washington(4),
	Charlotte(5),
	Pittsburgh(6),
	Columbus(7),
	Indianapolis(8),
	StLouis(9),
	KansasCity(10),
	Denver(11),
	OklahomaCity(12),
	Atlanta(13),
	Houston(14),
	Memphis(15),
	Jacksonville(16),
	Miami(17),
	Nashville(18),
	Chicago(19),
	SaltLakeCity(20),
	LasVegas(21),
	LosAngeles(22),
	SanFrancisco(23),
	Portland(24),
	Seattle(25),
	Phoenix(26),
	Albuquerque(27),
	SanAntonio(28),
	NewOrleans(30),
	Dallas(31),
	Minneapolis(32);
	
	
	private final int hash;
	
	CityValues(int h){
		this.hash = h; 
	}
	
	public int getValue() {
		return this.hash;
	}
	

}
