/**
 * The Single class is a subclass of the Hand class and is used to model a
 * Single
 * 
 * @author Agrim Somani
 */
public class Single extends Hand {

    /**
     * a constructor for building a Single
     * with the specified player and list of cards
     * 
     * @param player the player who plays the hand
     * @param cards  the cards played by the player to construct the hand
     */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    public String getType() {
        return "Single";
    }

    public boolean isValid() {
        if (this.size() == 1) {
            return true;
        }
        return false;
    }

    public boolean beats(Hand hand) {
        if (hand.size() == 1) {
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
