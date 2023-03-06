import java.util.ArrayList;

/**
 * The Quad class is a subclass of the Hand class and is used to model a
 * Quad
 * 
 * @author Agrim Somani
 */
public class Quad extends Hand {

    /**
     * a constructor for building a Quad
     * with the specified player and list of cards
     * 
     * @param player the player who plays the hand
     * @param cards  the cards played by the player to construct the hand
     */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    public Card getTopCard() {

        this.sort();

        int firstRank = this.getCard(0).getRank();
        int firstRankCounter = 1;

        for (int i = 1; i < 5; i++) {
            if (this.getCard(i).getRank() == firstRank) {
                firstRankCounter++;
            }
        }

        if (firstRankCounter == 4) {
            Card topCard = this.getCard(0);
            for (int i = 1; i < 4; i++) {
                if (this.getCard(i).getSuit() > topCard.getSuit()) {
                    topCard = this.getCard(i);
                }
            }
            return topCard;
        }

        else {
            Card topCard = this.getCard(1);
            for (int i = 2; i < 5; i++) {
                if (this.getCard(i).getSuit() > topCard.getSuit()) {
                    topCard = this.getCard(i);
                }
            }
            return topCard;
        }
    }

    public String getType() {
        return "Quad";
    }

    public boolean isValid() {
        if (this.size() == 5) {
            ArrayList<Integer> check = new ArrayList<Integer>();
            for (int i = 0; i < 5; i++) {
                if (!check.contains(this.getCard(i).getRank())) {
                    check.add(this.getCard(i).getRank());
                }
            }
            if (check.size() != 2) {
                return false;
            } else {
                int firstRank = check.get(0);
                int firstRankCounter = 1;
                for (int i = 1; i < 5; i++) {
                    if (this.getCard(i).getRank() == firstRank) {
                        firstRankCounter++;
                    }
                }
                if (firstRankCounter == 4 || firstRankCounter == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean beats(Hand hand) {
        if (hand.size() == 5 && hand.getType() == "Quad") {
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
