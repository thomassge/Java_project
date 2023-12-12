package Card_Deck_3;

public class Card {

	Card_values value;
	Card_suits suit;
	public Card() {
		
	}
	public Card(Card_values value, Card_suits suit) {
		this.value = value;
		this.suit = suit;
		System.out.println("Card created: " + this.value + ", " + this.suit);
	}
	
}
