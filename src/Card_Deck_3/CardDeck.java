package Card_Deck_3;

public class CardDeck extends Card {
	
	public static void CardFill() {
		int k=0;
		Card[] deck = new Card[52];
		for(Card_suits suits : Card_suits.values()) {
			for(Card_values values : Card_values.values()) {
				deck[k] = new Card(values, suits);
				k++;
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		CardFill();
		
		
	}
}
