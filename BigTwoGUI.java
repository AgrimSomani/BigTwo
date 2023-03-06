import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build
 * a GUI for
 * the Big Two card game and handle all user actions.
 * 
 * @author Agrim Somani
 */
public class BigTwoGUI implements CardGameUI {

    private Border borderGray = BorderFactory.createLineBorder(Color.GRAY);
    private BigTwo game;
    private boolean[] selected;
    private int activePlayer;
    private JFrame frame;
    private JPanel main;
    private JPanel bigTwoPanel;
    private JButton playButton;
    private JButton passButton;
    private JButton quitButton;
    private JButton connectButton;
    private JTextArea msgArea;
    private JTextArea chatArea;
    private JScrollPane msgAreaScroll;
    private JScrollPane chatAreaScroll;
    private JTextField chatInput;
    private boolean disableCardSelection = false;
    private int localPlayerIdx;

    /**
     * a constructor for creating a Big Two GUI
     * 
     */
    BigTwoGUI(BigTwo game) {
        this.game = game;
        this.go();
    }

    /**
     * a method for adding components to the frame, and making the frame visible
     * 
     */
    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.selected = new boolean[13];

        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }

        this.playButton = new JButton("Play");
        this.playButton.addActionListener(new PlayButtonListener());

        this.passButton = new JButton("Pass");
        this.passButton.addActionListener(new PassButtonListener());

        this.quitButton = new JButton("Quit");
        this.quitButton.addActionListener(new QuitMenuItemListener());

        this.connectButton = new JButton("Connect");
        this.connectButton.addActionListener(new ConnectMenuItemListener());

        this.msgArea = new JTextArea();
        this.msgArea.setEditable(false);
        msgArea.setBorder(borderGray);
        this.msgAreaScroll = new JScrollPane(msgArea);

        this.chatArea = new JTextArea();
        this.chatArea.setEditable(false);
        this.chatArea.setWrapStyleWord(true);
        chatArea.setBorder(borderGray);
        this.chatAreaScroll = new JScrollPane(chatArea);

        this.chatInput = new JTextField();
        this.chatInput.addActionListener(new EnterMessageItemListener());

        JPanel header = new JPanel();
        header.add(connectButton);
        header.add(quitButton);
        frame.add(header, BorderLayout.NORTH);

        this.main = new JPanel();
        main.setLayout(new GridLayout(1, 2));

        frame.add(main);

        this.bigTwoPanel = new BigTwoPanel();
        main.add(this.bigTwoPanel);

        JPanel messagesAndMovesContainer = new JPanel();
        messagesAndMovesContainer.setLayout(new GridLayout(2, 1));
        messagesAndMovesContainer.add(msgAreaScroll);
        messagesAndMovesContainer.add(chatAreaScroll);
        main.add(messagesAndMovesContainer);

        JPanel footer = new JPanel();
        footer.setLayout(new GridLayout(1, 2));
        footer.setBackground(new Color(240, 240, 240));
        JPanel buttonContainer = new JPanel();
        buttonContainer.add(this.playButton);
        buttonContainer.add(new JLabel("   "));
        buttonContainer.add(this.passButton);
        footer.add(buttonContainer);
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BorderLayout());
        messageContainer.add(new JLabel("Message: "), BorderLayout.WEST);
        messageContainer.add(this.chatInput, BorderLayout.CENTER);
        messageContainer.setBorder(new EmptyBorder(5, 10, 5, 10));
        footer.add(messageContainer);
        frame.add(footer, BorderLayout.SOUTH);

        for (int i = 0; i < 4; i++) {
            if (this.game.getPlayerList().get(i).getName() == null) {
                disable();
            }
        }

        frame.pack();
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    /**
     * a method for setting the index of the
     * active player (i.e., the player having control of the GUI).
     * 
     * @param activePlayer the index of the activePlayer in the card players array
     *                     list
     */
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;

    }

    /**
     * a method for repainting the GUI
     */
    public void repaint() {
        bigTwoPanel.repaint();

        if (game.endOfGame()) {
            disable();
            return;
        }

        if (game.getCurrentPlayerIdx() != localPlayerIdx) {
            disable();
        } else {
            enable();
        }

        if (game.checkIfConnected()) {
            setConnectedButton(false);
        } else {
            setConnectedButton(true);
        }

    }

    /**
     * a method for clearing the message area of the GUI
     */
    public void clearMsgArea() {
        this.msgArea.selectAll();
        this.msgArea.replaceSelection("");
    }

    /**
     * a method for resetting the GUI. You should (i) reset the list of selected
     * cards; (ii) clear the message area; and (iii) enable user interactions
     */
    public void reset() {
        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }
        this.clearMsgArea();
    }

    /**
     * a method for enabling user interactions with the GUI
     */
    public void enable() {
        this.playButton.setEnabled(true);
        this.passButton.setEnabled(true);
        this.disableCardSelection = false;
    }

    /**
     * a method for disabling user interactions with the GUI
     */
    public void disable() {
        this.playButton.setEnabled(false);
        this.passButton.setEnabled(false);
        this.disableCardSelection = true;
    }

    /**
     * a method for prompting the active player to select cards
     * and make his/her move. A message should be displayed in the message area
     * showing it
     * is the active player’s turn.
     */
    public void promptActivePlayer() {
        this.msgArea.append(game.getPlayerList().get(this.activePlayer).getName() + "'s turn:\n");
    }

    /**
     * a method to convert the selected boolean array to an array of integers
     * containing the indexes of the cards selected
     * 
     * @param selected array of boolean of size 13 representing the selected cards
     *                 from the current players hand, selected by the current player
     * @return an array of integers containing the indexes of the cards selected
     *         from the current users hand
     */
    public int[] convertSelectedToIdx(boolean[] selected) {
        int[] cardIdx = null;
        int count = 0;

        for (int j = 0; j < selected.length; j++) {
            if (selected[j]) {
                count++;
            }
        }

        if (count != 0) {
            cardIdx = new int[count];
            count = 0;
            for (int j = 0; j < selected.length; j++) {
                if (selected[j]) {
                    cardIdx[count] = j;
                    count++;
                }
            }
        }

        return cardIdx;
    }

    /**
     * a method for printing the specified string to the message
     * area of the GUI
     */
    public void printMsg(String msg) {
        this.msgArea.append(msg + "\n");
        this.msgArea.setCaretPosition(this.msgArea.getDocument().getLength());
    }

    /**
     * a method for setting the local player index in the GUI
     * 
     * @param Idx the local player index
     */
    public void setLocalPlayerIdx(int Idx) {
        this.localPlayerIdx = Idx;
    }

    /**
     * a method appending a chat message to the chat input area
     * 
     * @param chat chat message to append
     */
    public void printChat(String chat) {
        chatArea.append(chat + "\n");
        chatInput.setText("");
    }

    /**
     * a method for changing the state of the connect button
     * 
     * @param connect a boolean specifying whether to enable or disable the connect
     *                button
     */
    public void setConnectedButton(Boolean connect) {
        connectButton.setEnabled(connect);
    }

    /**
     * @author Agrim Somani
     *         an inner class that extends the JPanel class and implements the
     *         MouseListener interface.
     */
    public class BigTwoPanel extends JPanel implements MouseListener {

        /**
         * a constructor for creating a Big Two Panel
         * 
         */
        BigTwoPanel() {
            this.addMouseListener(this);
        }

        Image avatar0 = new ImageIcon("res/0.png").getImage();
        Image avatar1 = new ImageIcon("res/1.png").getImage();
        Image avatar2 = new ImageIcon("res/2.png").getImage();
        Image avatar3 = new ImageIcon("res/3.png").getImage();
        Image backCard = new ImageIcon("cards/b.gif").getImage();

        /**
         * a method which will add components to the graphics object of this panel
         * 
         * @param g the Graphics context in which to paint
         */
        public void paint(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;

            g2D.setColor(new Color(0, 153, 76));
            g2D.fillRect(0, 0, this.getWidth(), this.getHeight());

            g2D.setColor(Color.BLACK);
            for (int i = 1; i < 5; i++) {
                g2D.drawLine(0, (int) Math.round(this.getHeight() * 0.2 * i), this.getWidth(),
                        (int) Math.round(this.getHeight() * 0.2 * i));
            }

            g2D.setFont(new Font("TimesRoman", Font.BOLD, (int) Math.round(this.getHeight() * 0.02)));

            for (int i = 0; i <= 4; i++) {
                if (i == 4) {
                    if (game.getHandsOnTable().size() != 0) {
                        g2D.drawString(
                                "Played by "
                                        + game.getHandsOnTable().get(game.getHandsOnTable().size() - 1)
                                                .getPlayer().getName(),
                                Math.round(this.getWidth() * 0.02),
                                (int) Math.round((this.getHeight() * 0.2 * i) + (this.getHeight() * 0.025)));
                    }
                } else {
                    if (game.getPlayerList().get(i).getName() != null) {
                        g2D.drawString(localPlayerIdx != i ? game.getPlayerList().get(i).getName() : "You",
                                Math.round(this.getWidth() * 0.02),
                                (int) Math.round((this.getHeight() * 0.2 * i) + (this.getHeight() * 0.025)));
                    }
                }
            }

            if (game.getPlayerList().get(0).getName() != null) {
                g2D.drawImage(avatar0, 0, (int) Math.round((this.getHeight() * 0.038)),
                        (int) Math.round((this.getWidth() * 0.1)),
                        (int) Math.round((this.getHeight() * 0.16)), null);
            }

            if (game.getPlayerList().get(1).getName() != null) {
                g2D.drawImage(avatar1, 0, (int) Math.round(((this.getHeight() * 0.038) + this.getHeight() * 0.2)),
                        (int) Math.round((this.getWidth() * 0.1)),
                        (int) Math.round((this.getHeight() * 0.16)), null);
            }

            if (game.getPlayerList().get(2).getName() != null) {
                g2D.drawImage(avatar2, 0, (int) Math.round(((this.getHeight() * 0.038) + this.getHeight() * 0.4)),
                        (int) Math.round((this.getWidth() * 0.1)),
                        (int) Math.round((this.getHeight() * 0.16)), null);
            }

            if (game.getPlayerList().get(3).getName() != null) {
                g2D.drawImage(avatar3, 0, (int) Math.round(((this.getHeight() * 0.038) + this.getHeight() * 0.6)),
                        (int) Math.round((this.getWidth() * 0.1)),
                        (int) Math.round((this.getHeight() * 0.16)), null);
            }

            for (int i = 0; i < 5; i++) {

                char[] suit = { 'd', 'c', 'h', 's' };
                char[] rank = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };

                if (i == 4) {

                    if (game.getHandsOnTable().size() == 0) {
                        break;
                    }

                    game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).sort();
                    CardList lastHandOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);

                    for (int j = 0; j < lastHandOnTable.size(); j++) {

                        Image addCard = new ImageIcon("cards/"
                                + (rank[lastHandOnTable.getCard(j).getRank()])
                                + (suit[lastHandOnTable.getCard(j).getSuit()]) + ".gif")
                                .getImage();

                        g2D.drawImage(addCard,
                                (int) Math.round((this.getWidth() * 0.02) + (j * this.getWidth() * 0.025)),
                                (int) Math.round((this.getHeight() * 0.038) + (this.getHeight() * 0.2 * i)),
                                (int) Math.round((this.getWidth() * 0.1)),
                                (int) Math.round((this.getHeight() * 0.15)), null);
                    }
                } else if (game.getCurrentPlayerIdx() != i) {
                    for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
                        g2D.drawImage(backCard,
                                (int) Math.round((this.getWidth() * 0.12) + (j * this.getWidth() * 0.025)),
                                (int) Math.round((this.getHeight() * 0.038) + (this.getHeight() * 0.2 * i)),
                                (int) Math.round((this.getWidth() * 0.1)),
                                (int) Math.round((this.getHeight() * 0.15)), null);
                    }
                } else {
                    for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {

                        game.getPlayerList().get(i).getCardsInHand().sort();

                        CardList currentPlayerCards = game.getPlayerList().get(i).getCardsInHand();

                        Image addCard = new ImageIcon("cards/"
                                + (rank[currentPlayerCards.getCard(j).getRank()])
                                + (suit[currentPlayerCards.getCard(j).getSuit()]) + ".gif")
                                .getImage();
                        if (!selected[j]) {
                            g2D.drawImage(addCard,
                                    (int) Math.round((this.getWidth() * 0.12) + (j * this.getWidth() * 0.025)),
                                    (int) Math.round((this.getHeight() * 0.038) + (this.getHeight() * 0.2 * i)),
                                    (int) Math.round((this.getWidth() * 0.1)),
                                    (int) Math.round((this.getHeight() * 0.15)), null);
                        } else {
                            g2D.drawImage(addCard,
                                    (int) Math.round((this.getWidth() * 0.12) + (j * this.getWidth() * 0.025)),
                                    (int) Math.round((this.getHeight() * 0.038) + (this.getHeight() * 0.2 * i)
                                            - (this.getHeight() * 0.02)),
                                    (int) Math.round((this.getWidth() * 0.1)),
                                    (int) Math.round((this.getHeight() * 0.15)), null);
                        }
                    }
                }
            }

        }

        /**
         * a method which will select and deselect the cards based on which cards the
         * active user presses from his hand. It will update the selected boolean array
         * accordingly, and update the UI so that the selected and not selected cards
         * are rendered in their correct respective ways
         * 
         * @param e the event to be processed
         */
        public void mouseReleased(MouseEvent e) {

            if (disableCardSelection) {
                return;
            }

            int currentPlayerIdx = game.getCurrentPlayerIdx();
            CardGamePlayer currentPlayer = game.getPlayerList().get(currentPlayerIdx);

            int lowerBoundY = (int) Math
                    .round((this.getHeight() * 0.038) + (this.getHeight() * 0.2 * currentPlayerIdx)
                            - (this.getHeight() * 0.02));
            int upperBoundY = (int) Math.round(lowerBoundY + (this.getHeight() * 0.15) + (this.getHeight() * 0.02));

            int lowerBoundX = (int) Math.round((this.getWidth() * 0.12));
            int upperBoundX = (int) Math
                    .round((this.getWidth() * 0.12) + ((currentPlayer.getNumOfCards() - 1) * this.getWidth() * 0.025)
                            + this.getWidth() * 0.1);

            int x = e.getX();
            int y = e.getY();

            if (((y >= lowerBoundY) && (y <= upperBoundY))) {
                if ((x >= lowerBoundX) && (x <= upperBoundX)) {
                    for (int j = 0; j < currentPlayer.getNumOfCards(); j++) {

                        int lowerBoundXForSelected = (int) Math
                                .round((this.getWidth() * 0.12) + (j * this.getWidth() * 0.025));
                        int upperBoundXForSelected = (int) Math.round(
                                (this.getWidth() * 0.12) + ((j + 1) * this.getWidth() * 0.025));

                        // int lowerBoundXForDeselected = (int) Math
                        // .round((this.getWidth() * 0.12) + (j * this.getWidth() * 0.025));
                        // int upperBoundXForDeselected = (int) Math
                        // .round(lowerBoundXForDeselected + this.getWidth() * 0.1);

                        if (j == (currentPlayer.getNumOfCards() - 1)) {
                            upperBoundXForSelected = (int) Math.round(
                                    lowerBoundXForSelected + (this.getWidth() * 0.1));
                        }

                        if (x >= lowerBoundXForSelected && x <= upperBoundXForSelected) {

                            int lowerBoundYForDeselecting = (int) Math
                                    .round((this.getHeight() * 0.038) + (this.getHeight() * 0.2 * currentPlayerIdx)
                                            - (this.getHeight() * 0.02));
                            int upperBoundYForDeSelecting = (int) Math
                                    .round(lowerBoundYForDeselecting
                                            + (this.getHeight() * 0.15));

                            int lowerBoundYForSelecting = (int) Math
                                    .round((this.getHeight() * 0.038) + (this.getHeight() * 0.2 * currentPlayerIdx));
                            int upperBoundYForSelecting = (int) Math
                                    .round(lowerBoundYForSelecting + (this.getHeight() * 0.15));

                            if (y >= lowerBoundYForDeselecting && y <= upperBoundYForDeSelecting) {
                                if (selected[j]) {
                                    selected[j] = false;
                                    this.repaint();
                                    return;
                                }
                            }

                            if (y >= lowerBoundYForSelecting && y <= upperBoundYForSelecting) {
                                selected[j] = true;
                                this.repaint();
                                return;
                            }
                        }
                    }
                }
            }
        }

        /**
         * not used
         * 
         * @param e the event to be processed
         */
        public void mouseClicked(MouseEvent e) {

        }

        /**
         * not used
         * 
         * @param e the event to be processed
         */
        public void mousePressed(MouseEvent e) {

        }

        /**
         * not used
         * 
         * @param e the event to be processed
         */
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * not used
         * 
         * @param e the event to be processed
         */
        public void mouseExited(MouseEvent e) {

        }
    }

    private class PlayButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.sendMessageToServer(new CardGameMessage(6, -1, convertSelectedToIdx(selected)));
            for (int i = 0; i < selected.length; i++) {
                selected[i] = false;
            }
            repaint();
        }
    }

    private class PassButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.sendMessageToServer(new CardGameMessage(6, -1, null));
            for (int i = 0; i < selected.length; i++) {
                selected[i] = false;
            }
            repaint();
        }
    }

    private class ConnectMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent action) {
            game.connectToServer();
        }
    }

    private class QuitMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent action) {
            System.exit(0);
        }
    }

    private class EnterMessageItemListener implements ActionListener {
        public void actionPerformed(ActionEvent action) {
            game.sendMessageToServer(new CardGameMessage(7, -1, chatInput.getText()));
        }
    }

}
