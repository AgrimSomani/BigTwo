import java.util.ArrayList;

/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a
 * card used in a
 * Big Two card game
 * 
 * @author Agrim Somani
 */
public class BigTwoCardClass extends Card {

    /**
     * a constructor for building a card with the specified
     * suit and rank
     * 
     * @param suit the suit of the card
     * @param rank the rank of the card
     * 
     * 
     */
    BigTwoCardClass(int suit, int rank) {
        super(suit, rank);
    }

    /**
     * a method for comparing the order of this card with the
     * specified card.
     * 
     * @param card the specified card to be compared to
     * 
     * @return a negative integer, zero, or a positive integer when this card is
     *         less than, equal to, or greater than the specified card
     */
    public int compareTo(Card card) {
        ArrayList<Integer> bigTwoCardClassRank = new ArrayList<Integer>();

        for (int i = 0; i <= 12; i++) {
            if (i == 0) {
                bigTwoCardClassRank.add(11);
            } else if (i == 1) {
                bigTwoCardClassRank.add(12);
            } else {
                bigTwoCardClassRank.add(i - 2);
            }
        }

        if (bigTwoCardClassRank.get(this.rank) > bigTwoCardClassRank.get(card.rank)) {
            return 1;
        } else if (bigTwoCardClassRank.get(this.rank) < bigTwoCardClassRank.get(card.rank)) {
            return -1;
        } else if (this.suit > card.suit) {
            return 1;
        } else if (this.suit < card.suit) {
            return -1;
        } else {
            return 0;
        }
    }
}
