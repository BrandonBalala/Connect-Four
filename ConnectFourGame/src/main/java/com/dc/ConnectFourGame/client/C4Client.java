package com.dc.ConnectFourGame.client;

import java.io.IOException;
import java.net.Socket;

import com.dc.ConnectFourGame.controllers.BoardController;
import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
import com.dc.ConnectFourGame.shared.Identifier;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

/**
 * This class extends the C4Logic. It's the class that handles receiving 
 * moves from the server to make changes to the GUI. It also sends packets
 *  to the server depending on the action made on the GUI.
 * 
 * @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public class C4Client extends C4Logic {

	private Socket connection;
	private C4Packet converser;
	private String address;
	private boolean canPlay;
	private int port = 50000;
	private BoardController controller;
	private final String CONNECTED_MSG = "Prepare to lose. MUHAHA";
	private final String RESET_MSG = "New game. Prepare to lose again. MUHAHA";
	private final String MOVE_MSG = "Make Your Move";
	private final String BAD_MOVE_MSG = "Bad move";
	private final String WIN_MSG = "YOU WON!";
	private final String LOSE_MSG = "YOU LOSE...";
	private final String TIE_MSG = "IT'S A DRAW.";
	private final String SERVER_NOT_FOUND_MSG = "Server not found";

	/**
	 * Get the C4Packet
	 * 
	 * @return converser
	 */
	public C4Packet getC4Packet() {
		return converser;
	}

	/**
	 * Get the socket
	 * 
	 * @return connection
	 */
	public Socket getSocket() {
		return connection;
	}

	/**
	 * This methhod receives the BoardController and gets from it the ip address
	 * of the server. It then tries to establish a connection with the client by
	 * sending in ip and port number
	 * 
	 * @param controller
	 * @throws IOException
	 */
	public void readIp(BoardController controller) throws IOException{
		this.controller = controller;
		address = controller.getConnectingIP();
			startConnection(address, port);
	}

	/**
	 * Tries to establish connection with the server by sending a connect
	 * packet. If it does connect correctly, it's suppose to receive a response
	 * from the server.
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @return connected
	 * @throws IOException
	 */
	public boolean startConnection(String serverIp, int serverPort) throws IOException {
		boolean connected = false;
		try {
			connection = new Socket(serverIp, serverPort);
			converser = new C4Packet(connection);

			// Creating the packet
			byte[] packet = converser.createPacket(PACKET_TYPE.CONNECT.getValue(), -1, -1, -1, -1);

			// Sending the packet
			converser.sendPacket(packet);

			// Wait for responce
			getResponce();
			connected = true;
		} catch (Exception e) {
			controller.setStatusMessage(SERVER_NOT_FOUND_MSG);
		}
		return connected;
	}

	/**
	 * Converts the packets received which are in terms of a 12x13 board used in
	 * C4Logic into a 6x7 board used in our GameController.
	 * 
	 * @param packet
	 * @return
	 */
	public byte[] convertPacketToSmallBoard(byte[] packet) {
		packet[1] -= 3;
		packet[2] -= 3;
		packet[3] -= 3;
		packet[4] -= 3;
		return packet;
	}

	/**
	 * This method waits for an incoming packet and then performs specified
	 * actions according to the type of packet you are receiving
	 */
	public void getResponce() {
		try {
			byte[] packet = converser.receivePacket();
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case CONNECT:
				controller.setStatusMessage(CONNECTED_MSG);
				controller.setIsConnected(true);
				controller.disableConnectButton();
				canPlay = true;
				break;
			case RESET_GAME:
				controller.setStatusMessage(RESET_MSG);
				controller.resetBoard(); //Reset the GUI's labels
				controller.setIsConnected(true);
				controller.setNotWaiting(true);
				newGame(); //Resets the actual client site game board
				canPlay = true;
				break;
			case MOVE:
				controller.setStatusMessage(MOVE_MSG);
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet)); //Sets the move on the GUI
				setChoice(packet[2] + 3, Identifier.Client); //Sets the client move on the client's gameBoard
				setChoice(packet[4] + 3, Identifier.Server); //Sets the server move on the client's gameBoard
				break;
			case BAD_MOVE:
				controller.setStatusMessage(BAD_MOVE_MSG);
				canPlay = false;
				break;
			case WIN:
				controller.setStatusMessage(WIN_MSG);
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet)); //Sets the client move on the GUI
				canPlay = false;
				break;
			case LOSE:
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet)); //Sets the client and server move on the GUI
				controller.setStatusMessage(LOSE_MSG);
				canPlay = false;
				break;
			case TIE:
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet)); //Sets the client and server move on the GUI
				controller.setStatusMessage(TIE_MSG);
				canPlay = false;
				break;
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This is invoked when client clicks on the column he wants in the GUI.
	 * We are validating here in the client whether it is a valid move before actually sending,
	 * to the server with the soul purpose of not sending useless moves to the server
	 * @param column
	 * @throws Exception
	 */
	public void makeMove(int column) throws Exception {
		if (canPlay) { //check if user can play
			column += 3;
			
			int row = getNextEmptyRow(column); //gets the upmost empty row
			if (row != -1) { //-1 means that the row is already full
				try {
					//Sending a move packet to server
					sendMovePacket(column, row);
				} catch (IOException e) {
					// Rollback play
					getGameBoard()[row][column] = null;
				}
			}
		}
	}
	
	/**
	 * Sends a reset packet to the server and then expects for an incoming packet in return
	 */
	public void sendResetPacket() {
		byte[] packet;
		try {
			packet = converser.createPacket(PACKET_TYPE.RESET_GAME.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a disconnect packet to the server
	 */
	public void sendDisconnectPacket() {
		// TODO Auto-generated method stub
		byte[] packet;
		try {
			packet = converser.createPacket(PACKET_TYPE.DISCONNECT.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a move packet to the server, which contains the clients row and column of
	 * choice and then expects for an incoming packet in return
	 */
	public void sendMovePacket(int column, int row) throws IOException {
		byte[] packet = converser.createPacket(PACKET_TYPE.MOVE.getValue(), row, column, -1, -1);
		converser.sendPacket(packet);
		getResponce();

	}

}
