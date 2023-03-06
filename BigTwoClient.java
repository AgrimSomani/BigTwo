import java.io.*;
import java.net.*;

/**
 * The BigTwoClient class implements the NetworkGame interface. It is used to
 * model a Big
 * Two game client that is responsible for establishing a connection and
 * communicating with
 * the Big Two game server.
 * 
 * @author Agrim Somani
 */
public class BigTwoClient implements NetworkGame {

    private BigTwo game;
    private BigTwoGUI gui;
    private Socket sock;
    private ObjectOutputStream oos;
    private int playerID;
    private String playerName;
    private String serverIP;
    private int serverPort;
    private boolean connected;

    /**
     * a constructor for creating a Big Two
     * client. The first parameter is a reference to a BigTwo object associated with
     * this client
     * and the second parameter is a reference to a BigTwoGUI object associated the
     * BigTwo
     * object.
     * 
     */
    BigTwoClient(BigTwo game, BigTwoGUI gui) {
        this.game = game;
        this.gui = gui;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(int PlayerID) {
        this.playerID = PlayerID;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void connect() {
        try {
            sock = new Socket(this.getServerIP(), this.getServerPort());
            this.connected = true;
            gui.setConnectedButton(false);
            oos = new ObjectOutputStream(sock.getOutputStream());
            Thread receiveMessage = new Thread(new ServerHandler());
            receiveMessage.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void parseMessage(GameMessage message) {
        switch (message.getType()) {
            case 0:
                gui.printMsg("Connected to the server at /192.168.1.213:2396");
                setPlayerID(message.getPlayerID());
                String[] names = (String[]) message.getData();
                for (int i = 0; i < 4; i++) {
                    game.getPlayerList().get(i).setName(names[i]);
                }
                sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
                break;
            case 1:
                String name = (String) message.getData();
                game.getPlayerList().get(message.getPlayerID()).setName(name);
                if (message.getPlayerID() == getPlayerID()) {
                    sendMessage(new CardGameMessage(4, -1, null));
                    gui.setLocalPlayerIdx(playerID);
                }
                gui.repaint();
                break;
            case 2:
                gui.printMsg("Server is full. You cannot join the game now.");
                try {
                    sock.close();
                    connected = false;
                    gui.setConnectedButton(true);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 3:
                gui.printMsg(
                        game.getPlayerList().get(message.getPlayerID()).getName() + " (" + (String) message.getData()
                                + ") leaves the game");
                game.getPlayerList().get(message.getPlayerID()).setName(null);
                gui.repaint();
                if (!game.endOfGame()) {
                    sendMessage(new CardGameMessage(4, -1, null));
                    gui.disable();
                }
                break;
            case 4:
                gui.printMsg(this.game.getPlayerList().get(message.getPlayerID()).getName() + " is ready");
                break;
            case 5:
                game.start((Deck) message.getData());
                break;
            case 6:
                game.checkMove(message.getPlayerID(), (int[]) message.getData());
                break;
            case 7:
                gui.printChat((String) message.getData());
                break;
        }

    }

    public void sendMessage(GameMessage message) {
        try {
            oos.writeObject(message);
        } catch (Exception e) {
            System.out.println("There was an error sending data to the server.");
        }
    }

    public String getServerIP() {
        return this.serverIP;
    }

    /**
     * Checks and returns whether the current local player is connected to the
     * server or not
     * 
     * @return a boolean potraying the connection status of the local player
     */
    public boolean checkIfConnected() {
        return this.connected;
    }

    /**
     * an inner class that implements the Runnable interface
     * 
     * @author Agrim Somani
     */
    public class ServerHandler implements Runnable {

        /**
         * Parses the input stream message from the server, by first converting the
         * input stream in bytes to an object. The object is parsed into a message using
         * the parseMessage method.
         * 
         */
        public void run() {
            if (sock.isConnected()) {
                try {
                    ObjectInputStream oistream = new ObjectInputStream(sock.getInputStream());
                    CardGameMessage message;
                    while ((message = (CardGameMessage) oistream.readObject()) != null) {
                        parseMessage(message);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}
