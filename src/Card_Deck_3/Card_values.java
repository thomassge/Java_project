package Card_Deck_3;

public enum Card_values {
	 
	TWO (2), 
	THREE (3), 
	FOUR (4), 
	FIVE (5), 
	SIX (6),
	SEVEN (7),
	EIGHT (8),
	NINE (9),
	TEN (10),
	ACE (11),
	BUBE (12),
	DAME(13),
	KOENIG (14);
	
	private int value;

	Card_values(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
