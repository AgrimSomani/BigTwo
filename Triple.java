/**
 * The Triple class is a subclass of the Hand class and is used to model
 * a
 * Triple
 * 
 * @author Agrim Somani
 */
public class Triple extends Hand {

    /**
     * a constructor for building a Triple
     * with the specified player and list of cards
     * 
     * @param player the player who plays the hand
     * @param cards  the cards played by the player to construct the hand
     */
    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    public String getType() {
        return "Triple";
    }

    public boolean isValid() {
        if (this.size() == 3) {
            if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
                if (this.getCard(0).getRank() == this.getCard(2).getRank()) {
                    if (this.getCard(1).getRank() == this.getCard(2).getRank()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean beats(Hand hand) {
        if (hand.size() == 3) {
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
