/**
 * The Hand class is a subclass of the CardList class and is used to model a
 * hand of cards
 * 
 * @author Agrim Somani
 */
abstract class Hand extends CardList {

    private CardGamePlayer player;

    /**
     * a constructor for building a hand
     * with the specified player and list of cards
     * 
     * @param player the player who plays the hand
     * @param cards  the cards played by the player to construct the hand
     */
    public Hand(CardGamePlayer player, CardList cards) {
        this.player = player;
        for (int i = 0; i < cards.size(); i++) {
            this.addCard(cards.getCard(i));
        }
    }

    /**
     * a method for retrieving the player of this hand
     * 
     * @return the CardGamePlayer object who played this hand
     */
    public CardGamePlayer getPlayer() {
        return this.player;
    }

    /**
     * a method for retrieving the top card of this hand
     * 
     * @return the Card object representing the top card
     */
    public Card getTopCard() {
        Card topCard = this.getCard(0);
        for (int i = 1; i < this.size(); i++) {
            if (this.getCard(i).getSuit() > topCard.getSuit()) {
                topCard = this.getCard(i);
            }
        }
        return topCard;
    }

    /**
     * a method for checking if this hand beats a specified hand
     * 
     * @param hand the specified hand object to compare to
     * 
     * @return a boolean specifying wheter this hand beats the specified hand in the
     *         params
     */
    public boolean beats(Hand hand) {
        return true;
    }

    /**
     * a method for checking if this is a valid hand
     * 
     * @return a boolean specifying whether its a valid hand or not
     */
    public abstract boolean isValid();

    /**
     * a method for returning a string specifying the type of this hand
     * 
     * @return a string representing the type of the hand
     */
    public abstract String getType();
}
