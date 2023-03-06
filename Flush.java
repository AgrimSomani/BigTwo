/**
 * The Flush class is a subclass of the Hand class and is used to model a
 * Flush
 * 
 * @author Agrim Somani
 */
public class Flush extends Hand {

    /**
     * a constructor for building a Flush
     * with the specified player and list of cards
     * 
     * @param player the player who plays the hand
     * @param cards  the cards played by the player to construct the hand
     */
    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    public Card getTopCard() {
        this.sort();
        Card topCard = this.getCard(4);
        return topCard;
    }

    public String getType() {
        return "Flush";
    }

    public boolean isValid() {
        if (this.size() == 5) {
            int firstCardSuit = this.getCard(0).getSuit();
            for (int i = 1; i < 5; i++) {
                if (this.getCard(i).getSuit() != firstCardSuit) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean beats(Hand hand) {
        if (hand.size() == 5 && hand.getType() == "Flush") {
            if (hand.getCard(0).getSuit() == this.getCard(0).getSuit()) {
                if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (hand.getCard(0).getSuit() > this.getCard(0).getSuit()) {
                    return false;
                } else {
                    return true;
                }
            }
        } else if (hand.size() == 5 && hand.getType() == "Straight") {
            return true;
        } else {
            return false;
        }
    }
}
