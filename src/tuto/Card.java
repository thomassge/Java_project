package tuto;

public class Card {
	private String suit;
	private String value;
	
	
	public Card() {
	}
	
	public Card(String suit, String value) {
		this.suit = suit;
		this.value = value;
		
		System.out.println("Karte erstellt: " + this.suit + ", " + this.value);
		
	}
	
	public String getSuit() {
		return this.suit;
	}
	
	public String getValue() {
		return this.value;
	}
	
	/*public static void main(String[] args) {
		
		Card c1 = new Card("Heart", "5");
	}*/
	
}
