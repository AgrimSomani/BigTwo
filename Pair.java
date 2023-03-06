/**
 * The Pair class is a subclass of the Hand class and is used to model a
 * Pair
 * 
 * @author Agrim Somani
 */
public class Pair extends Hand {

    /**
     * a constructor for building a pair
     * with the specified player and list of cards
     * 
     * @param player the player who plays the hand
     * @param cards  the cards played by the player to construct the hand
     */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    public String getType() {
        return "Pair";
    }

    public boolean isValid() {
        if (this.size() == 2) {
            if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
                return true;
            }
        }
        return false;
    }

    public boolean beats(Hand hand) {
        if (hand.size() == 2) {
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
