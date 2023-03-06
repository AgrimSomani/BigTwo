import java.util.ArrayList;

/**
 * The StraightFlush class is a subclass of the Hand class and is used to model
 * a
 * StraightFlush
 * 
 * @author Agrim Somani
 */
public class StraightFlush extends Hand {

    /**
     * a constructor for building a StraightFlush
     * with the specified player and list of cards
     * 
     * @param player the player who plays the hand
     * @param cards  the cards played by the player to construct the hand
     */
    public StraightFlush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    public Card getTopCard() {
        this.sort();
        Card topCard = this.getCard(4);
        return topCard;
    }

    public String getType() {
        return "StraightFlush";
    }

    public boolean isValid() {
        if (this.size() == 5) {
            this.sort();

            ArrayList<Integer> bigTwoCardClassRank = new ArrayList<Integer>();

            for (int i = 0; i < 5; i++) {
                if (this.getCard(i).getRank() == 0 || this.getCard(i).getRank() == 1) {
                    bigTwoCardClassRank.add(this.getCard(i).getRank() + 13);
                } else {
                    bigTwoCardClassRank.add(this.getCard(i).getRank());
                }
            }

            int suit = this.getCard(0).getSuit();
            int compareRankCounter = bigTwoCardClassRank.get(0);

            for (int i = 1; i < 5; i++) {
                if (this.getCard(i).getSuit() != suit) {
                    return false;
                }
                if ((bigTwoCardClassRank.get(i) != compareRankCounter + 1)) {
                    return false;
                }
                compareRankCounter = bigTwoCardClassRank.get(i);
            }
            return true;
        }
        return false;
    }

    public boolean beats(Hand hand) {
        if (hand.size() == 5 && hand.getType() == "StraightFlush") {
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (hand.size() == 5 && hand.getType() != "StraightFlush") {
            return true;
        } else {
            return false;
        }
    }
}
