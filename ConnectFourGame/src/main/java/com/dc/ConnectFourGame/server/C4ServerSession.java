package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
import com.dc.ConnectFourGame.shared.Identifier;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

/**
 * Class that receives the different packets from the client and decides what to
 * do with them. It can send all sorts of packets to the client: win, tie, lose,
 * connect, reset game, move, bad move. It also decides which moves the ai will
 * play
 * 
 * @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public class C4ServerSession extends C4Logic {

	private boolean playAgain;
	private boolean gameOver;
	private C4Packet converser;

	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	/**
	 * C4ServerSession constructor. Creates the C4Packet object that will be
	 * needed.
	 * 
	 * @param connection
	 *            The client socket to which packet will be send
	 * @throws IOException
	 *             if there is any problem with the sockets
	 */
	public C4ServerSession(Socket connection) throws IOException {
		converser = new C4Packet(connection);
		log.info("Session connected to " + connection.getInetAddress().getHostAddress());
		start();

	}

	/**
	 * Class that sends the connecting packet to the client and starts the game
	 * session.
	 * 
	 * @throws IOException
	 *             if there is any problem with the sockets
	 */
	private void start() throws IOException {
		byte[] packet = converser.receivePacket();
		int type = (int) packet[0];
		if (type == PACKET_TYPE.CONNECT.getValue()) {
			sendConnectPacket();
			playAgain = true;
			gameOver = false;
			startGameSession();
		}
	}

	/**
	 * Handles the total number of games the client wants to play. It will
	 * examine all the packet it receives until the game ends. Closes the
	 * connection when the client doesn't want to play anymore.
	 * 
	 * @throws IOException
	 *             if there is any problem with the sockets
	 */
	public void startGameSession() throws IOException {
		log.info("Session Started");
		while (playAgain) {
			while (!gameOver) {
				getResponce();
			} // the single game is over
		} // the client does not want to play anymore

		converser.connectionClose();
	}

	/**
	 * Decides which column the server should play to make the most intelligent
	 * move.
	 * 
	 * @return the column the server decided to play
	 */
	public int decideMove() {

		// there's one possible move at each column
		int[] ranks = new int[7];
		boolean identicalValues = true;
		int i = 0;

		// get a score for each possible move
		for (int column = 3; column < 10; column++) {
			ranks[i] = getRank(column, Identifier.Server);
			i++;
		}

		// determines if all the moves have the same priority
		int choice = 0;
		for (i = 1; i < 6; i++) {
			if (ranks[i] != ranks[i + 1]) {
				identicalValues = false;
			}
		}

		// if the moves have different scores, get the highest ranked move.
		if (!identicalValues) {
			for (i = 0; i < 7; i++) {
				if (ranks[i] > ranks[choice])
					choice = i;
			}
		} else {
			// choose a random move from the list since they all have the same
			// priority
			choice = (int) (Math.random() * 7);
		}

		// the move could have a score of -1 if its a bad move
		if (ranks[choice] < 0) {
			List<Integer> list = new ArrayList<Integer>();
			for (i = 0; i < ranks.length; i++) {
				// only add the valid moves
				if (ranks[i] >= 0)
					list.add(ranks[i]);
			}
			// choose a random VALID move
			choice = (int) (Math.random() * list.size());
		}

		/*
		 * the number representing the column the server choose to play. it has
		 * an offset of 3 because the 6 * 7 board has 3 empty space on all sides
		 * to simplify null checks.
		 */
		return choice + 3;
	}

	/**
	 * Checks the first byte of the received packet and decides what methods
	 * should be called.
	 */
	private void getResponce() {
		try {
			byte[] packet = converser.receivePacket();
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case RESET_GAME:
				playAgain = true;
				gameOver = false;
				// resets board and markers
				newGame();
				sendResetGamePacket();
				log.info("Game restarted");
				break;
			case DISCONNECT:
				playAgain = false;
				gameOver = true;
				log.info("DISCONNECTED");
				break;
			case MOVE:
				// receiving a move from the client
				setPlayerChoice(packet);
				break;
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the players move is valid and if it was a winning move. If not
	 * the server plays his move or the move was invalid.
	 * 
	 * @param packet
	 *            the clients packets with his move in it
	 */
	private void setPlayerChoice(byte[] packet) {
		int column = (int) packet[2];
		int row = (int) packet[1];

		// checks if its a valid move
		if (setChoice(column, Identifier.Client) == row) {
			// did the client win?
			if (checkWin(column, row, Identifier.Client)) {
				gameOver = true;
				sendWinPacket(row, column);
			} else {
				// server turn to make a move
				setServerChoice(row, column);
			}
		} else {
			// it was an invalid move
			sendBadMovePacket();
		}
	}

	/**
	 * Decides a column and row for the server taking the user's move.
	 * If the move is valid, it checks if it won or tied the game. 
	 * If not it returns a complete back with the client's and server's move 
	 * to the client
	 * 
	 * @param rowClient
	 * 			the row the client choose to play
	 * @param colClient
	 * 			the column the client choose to play
	 */
	private void setServerChoice(int rowClient, int colClient) {
		// decides which column the server should play
		int colServer = decideMove();

		// returns the most upper empty row in a column
		int rowServer = getNextEmptyRow(colServer);

		//check if valid move and updates internal board and markers
		setChoice(colServer, Identifier.Server);
		
		//check if server won with his move and send lose packet if so
		if (checkWin(colServer, rowServer, Identifier.Server)) {
			gameOver = true;
			sendLosePacket(rowClient, colClient, rowServer, colServer);
		} else if (checkDraw()) {
			//check if server created a draw
			gameOver = true;
			sendTiePacket(rowClient, colClient, rowServer, colServer);
		} else {
			/*the status didnt change so the server's move is send back 
			to the client to update the gui*/
			sendBackClientAndServerMove(rowClient, colClient, rowServer, colServer);
		}
	}

	/**
	 * Creates a complete 5 bytes packet and sends it back to the client.
	 * it indicates that both their moves were valid,
	 * to update the gui and continue playing.
	 * 
	 * @param rowClient
	 * 				the row the client choose to play
	 * @param colClient
	 * 				the column the client choose to play
	 * @param rowServer
	 * 				the row the server choose to play
	 * @param colServer
	 * 				the column the server choose to play
	 */
	private void sendBackClientAndServerMove(int rowClient, int colClient, int rowServer, int colServer) {
		try {

			byte[] packet = converser.createPacket(PACKET_TYPE.MOVE.getValue(), rowClient, colClient, rowServer,
					colServer);
			converser.sendPacket(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a bad move packet and sends it to the client. 
	 * It then waits for the next packet the client will send it.
	 */
	private void sendBadMovePacket() {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.BAD_MOVE.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a packet meaning the connection was made succesfully
	 * and sends it to the client.
	 */
	private void sendConnectPacket() {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.CONNECT.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a lose packet and sends it to the client. 
	 * It then waits for the next packet the client will send it.
	 * 
	 * @param rowClient
	 * 				the row the client choose to play
	 * @param colClient
	 * 				the column the client choose to play
	 * @param rowServer
	 * 				the row the server choose to play
	 * @param colServer
	 * 				the column the server choose to play
	 */
	private void sendLosePacket(int rowClient, int colClient, int rowServer, int colServer) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.LOSE.getValue(), rowClient, colClient, rowServer,
					colServer);
			converser.sendPacket(packet);
			getResponce();
			log.info("Client Lost");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a tie packet and sends it to the client. 
	 * It then waits for the next packet the client will send it.
	 * 
	 * @param rowClient
	 * 				the row the client choose to play
	 * @param colClient
	 * 				the column the client choose to play
	 * @param rowServer
	 * 				the row the server choose to play
	 * @param colServer
	 * 				the column the server choose to play
	 */
	private void sendTiePacket(int rowClient, int colClient, int rowServer, int colServer) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.TIE.getValue(), rowClient, colClient, rowServer,
					colServer);
			converser.sendPacket(packet);
			getResponce();
			log.info("Game Tied");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a win packet and sends it to the client. 
	 * It then waits for the next packet the client will send it.
	 * 
	 * @param row
	 * 			the row the client choose to play to win
	 * @param col
	 * 			the column the client choose to play to win
	 */
	private void sendWinPacket(int row, int col) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.WIN.getValue(), row, col, -1, -1);
			converser.sendPacket(packet);
			getResponce();
			log.info("Client Won");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a reset game packet and sends it to the client. 
	 * It then waits for the next packet the client will send it.
	 */
	private void sendResetGamePacket() {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.RESET_GAME.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}