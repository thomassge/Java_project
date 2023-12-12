package tuto;

public class CardDeck extends Card{
	
	public static void main(String[] args) {
		
		String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
		String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Ace", "Bube", "Dame", "Koenig"};
		
		int k=0;
		Card[] deck = new Card[52];
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<13; j++) {
				
				deck [k] = new Card(suits[i], values[j]);
				k++;
			}
		}
		
		
	}	
	
}
