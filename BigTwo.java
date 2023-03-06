import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big
 * Two card
 * game
 * 
 * @author Agrim Somani
 */
public class BigTwo implements CardGame {

    private int numOfPlayers;
    private Deck deck;
    private ArrayList<CardGamePlayer> playerList;
    private ArrayList<Hand> handsOnTable;
    private int currentPlayerIdx;
    private BigTwoGUI ui;
    private BigTwoClient client;

    /**
     * a constructor for creating a Big Two card game.
     * 
     */
    public BigTwo() {
        this.numOfPlayers = 4;
        this.playerList = new ArrayList<CardGamePlayer>();
        for (int i = 0; i < 4; i++) {
            CardGamePlayer player = new CardGamePlayer();
            player.setName(null);
            playerList.add(player);
        }
        this.handsOnTable = new ArrayList<Hand>();
        this.ui = new BigTwoGUI(this);
        this.client = new BigTwoClient(this, this.ui);
    }

    /**
     * – a method for starting a Big Two card game. It should (i)
     * create a Big Two card game, (ii) create and shuffle a deck of cards, and
     * (iii) start the
     * game with the deck of cards.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog(null, "Please Enter Your name:");
        if (name == null) {
            System.exit(0);
        }
        while (name.length() == 0) {
            name = JOptionPane.showInputDialog(null, "Invalid Name. Please Enter Your name again:");
            if (name == null) {
                System.exit(0);
            }
        }
        BigTwo game = new BigTwo();
        try {
            game.client.setPlayerName(name);
            game.client.setServerIP("192.168.1.213");
            game.client.setServerPort(2396);
            game.client.connect();
        } catch (Exception e) {
            System.exit(0);
        }

        game.deck = new BigTwoDeck();
        game.deck.shuffle();
        game.start(game.deck);
    }

    /**
     * a method for
     * returning a valid hand from the specified list of cards of the player.
     * Returns null if no
     * valid hand can be composed from the specified list of cards.
     * 
     * @param player an instance of the CardGamePlayer class
     * @param cards  this the an instance of the CardList class
     * 
     * @return A Hand Object representing any valid hand that can be made from the
     *         cards param
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards) {
        if (cards.size() == 5) {
            Flush flush = new Flush(player, cards);
            Straight straight = new Straight(player, cards);
            Quad quad = new Quad(player, cards);
            FullHouse fullHouse = new FullHouse(player, cards);
            StraightFlush straightFlush = new StraightFlush(player, cards);

            if (straightFlush.isValid()) {
                return straightFlush;
            } else if (quad.isValid()) {
                return quad;
            } else if (fullHouse.isValid()) {
                return fullHouse;
            } else if (flush.isValid()) {
                return flush;
            } else if (straight.isValid()) {
                return straight;
            } else {
                return null;
            }
        }

        else if (cards.size() == 3) {
            Triple triple = new Triple(player, cards);
            if (triple.isValid()) {
                return triple;
            } else {
                return null;
            }
        }

        else if (cards.size() == 2) {
            Pair pair = new Pair(player, cards);
            if (pair.isValid()) {
                return pair;
            } else {
                return null;
            }
        }

        else if (cards.size() == 1) {
            Single single = new Single(player, cards);
            return single;
        }

        else {
            return null;
        }

    }

    /**
     * a method for getting the number of players
     * 
     * @return the number of players
     */
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    /**
     * a method for retrieving the deck of cards being used
     * 
     * @return the deck
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * a method for retrieving the list of
     * players
     * 
     * @return the array list of the players playing the card game
     */
    public ArrayList<CardGamePlayer> getPlayerList() {
        return this.playerList;
    }

    /**
     * a method for retrieving the list of hands played
     * on the table
     * 
     * @return the array list of the hands played on the table
     */
    public ArrayList<Hand> getHandsOnTable() {
        return this.handsOnTable;
    }

    /**
     * a method for retrieving the index of the current player
     * 
     * @return the index number of the current player in the player array list
     */
    public int getCurrentPlayerIdx() {
        return this.currentPlayerIdx;
    }

    /**
     * a method for starting/restarting the game with a given
     * shuffled deck of cards.
     * 
     * @param deck a deck object which is shuffled to be distributed to the card
     *             game players
     * 
     */
    public void start(Deck deck) {

        this.handsOnTable.clear();
        this.deck = deck;
        int indexOfStartingPlayer = 0;

        for (int i = 0; i < 4; i++) {
            System.out.println(this.getPlayerList().get(i).getName());
            if (this.getPlayerList().get(i).getName() == null) {
                return;
            }
        }

        ui.enable();

        for (int i = 0; i < 4; i++) {
            this.playerList.get(i).removeAllCards();
            for (int j = 0; j < 13; j++) {
                if ((this.deck.getCard(((i * 13) + j)).getRank() == 2)
                        && (this.deck.getCard(((i * 13) + j)).getSuit() == 0)) {
                    indexOfStartingPlayer = i;
                }
                this.playerList.get(i).addCard(this.deck.getCard(((i * 13) + j)));
            }
            this.playerList.get(i).sortCardsInHand();
        }
        this.currentPlayerIdx = indexOfStartingPlayer;
        ui.setActivePlayer(indexOfStartingPlayer);
        ui.repaint();
        ui.promptActivePlayer();
    }

    /**
     * a method for checking a move
     * made by a player
     * 
     * @param playerIdx the index of the player who made the move
     * @param cardIdx   array of indexes of the cards played by the player in their
     *                  respective hands
     */
    public void checkMove(int playerIdx, int[] cardIdx) {
        if (cardIdx == null) {
            if (this.getHandsOnTable().size() == 0) {
                ui.printMsg("Not a legal move!!!");
                ui.promptActivePlayer();
            } else if (this.getHandsOnTable().get(handsOnTable.size() - 1).getPlayer() != this.getPlayerList()
                    .get(playerIdx)) {
                ui.printMsg("{Pass}");
                if (playerIdx == 3) {
                    this.currentPlayerIdx = 0;
                } else {
                    this.currentPlayerIdx = playerIdx + 1;
                }
                ui.setActivePlayer(this.currentPlayerIdx);
                ui.repaint();
                ui.promptActivePlayer();
            } else {
                ui.printMsg("Not a legal move!!!");
                ui.promptActivePlayer();
            }

            return;
        }

        CardList cards = new CardList();

        for (int cardIndex : cardIdx) {
            cards.addCard(this.playerList.get(playerIdx).getCardsInHand().getCard(cardIndex));
        }

        Hand composedHand = composeHand(this.playerList.get(playerIdx), cards);

        if (composedHand != null) {
            if (this.getHandsOnTable().size() == 0) {
                if (!composedHand.contains(new Card(0, 2))) {
                    ui.printMsg("Not a legal move!!!");
                    ui.promptActivePlayer();
                    return;
                }

                if (playerIdx == 3) {
                    this.currentPlayerIdx = 0;
                } else {
                    this.currentPlayerIdx = playerIdx + 1;
                }

                this.handsOnTable.add(composedHand);
                this.playerList.get(playerIdx).removeCards(cards);

            } else if (composedHand.beats(this.getHandsOnTable().get(handsOnTable.size() - 1))
                    || this.getHandsOnTable().get(handsOnTable.size() - 1).getPlayer() == this.getPlayerList()
                            .get(playerIdx)) {
                if (playerIdx == 3) {
                    this.currentPlayerIdx = 0;
                } else {
                    this.currentPlayerIdx = playerIdx + 1;
                }

                this.handsOnTable.add(composedHand);
                this.playerList.get(playerIdx).removeCards(cards);

                if (this.endOfGame()) {

                    ui.repaint();
                    ui.clearMsgArea();
                    ui.disable();

                    String toShowInMessageDialog = "";

                    for (int i = 0; i < 4; i++) {
                        if (this.playerList.get(i).getCardsInHand().size() == 0) {
                            toShowInMessageDialog += this.getPlayerList().get(i).getName() + " wins the game.\n";
                        } else {
                            toShowInMessageDialog += this.getPlayerList().get(i).getName() + " has "
                                    + this.playerList.get(i).getCardsInHand().size()
                                    + " left.\n";
                        }
                    }

                    JOptionPane.showMessageDialog(null, toShowInMessageDialog);
                    client.sendMessage(new CardGameMessage(4, -1, null));
                    return;

                }
            } else {
                ui.printMsg("Not a legal move!!!");
                ui.promptActivePlayer();
                return;
            }

            this.ui.printMsg(this.getHandsOnTable().get(handsOnTable.size() - 1).getType() + " " +
                    this.getHandsOnTable().get(handsOnTable.size() - 1));

            ui.setActivePlayer(this.currentPlayerIdx);
            ui.repaint();
            ui.promptActivePlayer();
            return;
        }

        else {
            ui.printMsg("Not a legal move!!!");
            ui.promptActivePlayer();
        }

    }

    /**
     * a method for making a move by a
     * player with the specified index using the cards specified by the list of
     * indices.
     * 
     * @param playerIdx the index of the player who made the move
     * @param cardIdx   array of indexes of the cards played by the player in their
     *                  respective hands
     */
    public void makeMove(int playerIdx, int[] cardIdx) {
        checkMove(playerIdx, cardIdx);
        this.client.sendMessage(new CardGameMessage(6, -1, cardIdx));
    }

    /**
     * a method for checking if the game ends
     * 
     * @return a boolean which specifies whether the game is over or not
     */
    public boolean endOfGame() {
        for (CardGamePlayer player : this.playerList) {
            if (player.getCardsInHand().size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * a method for sending message to the server
     * 
     * @param message the message to send
     */
    public void sendMessageToServer(GameMessage message) {
        this.client.sendMessage(message);
    }

    /**
     * a method connecting to the server
     * 
     */
    public void connectToServer() {
        this.client.connect();
    }

    /**
     * a method for getting the local players name
     * 
     * @return The string of the local players name
     */
    public String getLocalPlayerName() {
        return client.getPlayerName();
    }

    /**
     * a method for checking if local player is connected to the server or not
     * 
     * @return boolean which specifies whether the local player is connected to the
     *         server or not
     */
    public Boolean checkIfConnected() {
        return client.checkIfConnected();
    }

}
