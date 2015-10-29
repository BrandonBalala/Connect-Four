package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

public class C4ServerSession extends C4Logic {

	private boolean playAgain;
	private boolean gameOver;
	private Socket connection;
	private C4Packet converser;
	private C4Logic c4logic;

	public C4ServerSession(Socket connection) throws IOException {
		this.connection = connection;
		converser = new C4Packet();
		c4logic = new C4Logic();
		start();

	}

	private void start() throws IOException {
		byte[] packet = converser.receivePacket(connection);
		int type = (int) packet[0];
		if (type == PACKET_TYPE.CONNECT.getValue()) {
			sendConnectPacket();
			playAgain = true;
			gameOver = false;
			startGameSession();
		}
	}

	public void startGameSession() throws IOException {
		while (playAgain) {
			while (!gameOver) {
				getResponce();
			}

		}

		connection.close();
	}

	public void sendPacket(byte[] packet) throws IOException {
		converser.sendPacket(packet, connection);
	}

	// Moves are set onto the gameboard in getWinOrBlockMove or getRandomMove
	public int decideMove(int serverToken, int clientToken) {
		// to win
		int column = getWinOrBlockMove(serverToken, serverToken);

		// to block
		if (column == -1)
			column = getWinOrBlockMove(clientToken, serverToken);

		// random
		if (column == -1)
			column = getRandomMove(serverToken);

		return column;
	}

	// target indicates which marker you are looking for in the board
	// player says that its the server who is playing and its marker will be
	// added to the board
	private int getWinOrBlockMove(int target, int player) {
		int[][] gameBoard = c4logic.getGameBoard();
		int rowLength = gameBoard.length;
		int colLength = gameBoard[0].length;

		int column;
		// Horizontal check
		column = horizontalCheck(target, player, gameBoard, rowLength, colLength);

		if (column != -1)
			return column;

		// Vertical check
		column = verticalCheck(target, player, gameBoard, rowLength, colLength);

		if (column != -1)
			return column;

		// Diagonal Upwards check
		column = diagonalUpwardsCheck(target, player, gameBoard, rowLength, colLength);

		if (column != -1)
			return column;

		// Diagonal Upwards check
		column = diagonalDownwardsCheck(target, player, gameBoard, rowLength, colLength);

		return column;
	}

	private int diagonalDownwardsCheck(int target, int player, int[][] gameBoard, int rowLength, int colLength) {
		int colChoice = -1;
		outerLoop: for (int row = 0; row <= rowLength - 4; row++) {
			for (int col = 0; col <= colLength - 4; col++) {
				int token1 = gameBoard[row][col];
				int token2 = gameBoard[row + 1][col + 1];
				int token3 = gameBoard[row + 2][col + 2];
				int token4 = gameBoard[row + 3][col + 3];

				if (token1 == 0 && token2 == target && token3 == target && token4 == target) {
					colChoice = col;
					break outerLoop;
				}
				if (token2 == 0 && token1 == target && token3 == target && token4 == target) {
					colChoice = col + 1;
					break outerLoop;
				}
				if (token3 == 0 && token1 == target && token2 == target && token4 == target) {
					colChoice = col + 2;
					break outerLoop;
				}
				if (token4 == 0 && token1 == target && token2 == target && token3 == target) {
					colChoice = col + 3;
					break outerLoop;
				}
			}
		}
		if (colChoice != -1) {
			setChoice(colChoice, player);
		}
		return colChoice;
	}

	private int diagonalUpwardsCheck(int target, int player, int[][] gameBoard, int rowLength, int colLength) {

		int colChoice = -1;
		outerLoop: for (int row = rowLength - 1; row > rowLength - 4; row--) {
			for (int col = 0; col <= colLength - 4; col++) {
				int token1 = gameBoard[row][col];
				int token2 = gameBoard[row - 1][col + 1];
				int token3 = gameBoard[row - 2][col + 2];
				int token4 = gameBoard[row - 3][col + 3];

				if (token1 == 0 && token2 == target && token3 == target && token4 == target) {
					colChoice = col;
					break outerLoop;
				}
				if (token2 == 0 && token1 == target && token3 == target && token4 == target) {
					colChoice = col + 1;
					break outerLoop;
				}
				if (token3 == 0 && token1 == target && token2 == target && token4 == target) {
					colChoice = col + 2;
					break outerLoop;
				}
				if (token4 == 0 && token1 == target && token2 == target && token3 == target) {
					colChoice = col + 3;
					break outerLoop;
				}
			}
		}
		if (colChoice != -1) {
			setChoice(colChoice, player);
		}
		return colChoice;
	}

	private int verticalCheck(int target, int player, int[][] gameBoard, int rowLength, int colLength) {

		int colChoice = -1;
		outerLoop: for (int col = 0; col < colLength; col++) {
			for (int row = 0; col <= rowLength - 4; col++) {
				int token1 = gameBoard[row][col];
				int token2 = gameBoard[row + 1][col];
				int token3 = gameBoard[row + 2][col];
				int token4 = gameBoard[row + 3][col];

				if (token1 == 0 && token2 == target && token3 == target && token4 == target) {
					colChoice = col;
					break outerLoop;
				}
				if (token2 == 0 && token1 == target && token3 == target && token4 == target) {
					colChoice = col;
					break outerLoop;
				}
				if (token3 == 0 && token1 == target && token2 == target && token4 == target) {
					colChoice = col;
					break outerLoop;
				}
				if (token4 == 0 && token1 == target && token2 == target && token3 == target) {
					colChoice = col;
					break outerLoop;
				}
			}
		}
		if (colChoice != -1) {
			setChoice(colChoice, player);
		}

		return colChoice;
	}

	private int horizontalCheck(int target, int player, int[][] gameBoard, int rowLength, int colLength) {

		int colChoice = -1;
		outerLoop: for (int row = 0; row < rowLength; row++) {
			for (int col = 0; col <= colLength - 4; col++) {
				int token1 = gameBoard[row][col];
				int token2 = gameBoard[row][col + 1];
				int token3 = gameBoard[row][col + 2];
				int token4 = gameBoard[row][col + 3];

				if (token1 == 0 && token2 == target && token3 == target && token4 == target) {
					colChoice = col;
					break outerLoop;
				}
				if (token2 == 0 && token1 == target && token3 == target && token4 == target) {
					colChoice = col + 1;
					break outerLoop;
				}
				if (token3 == 0 && token1 == target && token2 == target && token4 == target) {
					colChoice = col + 2;
					break outerLoop;
				}
				if (token4 == 0 && token1 == target && token2 == target && token3 == target) {
					colChoice = col + 3;
					break outerLoop;
				}
			}
		}
		if (colChoice != -1) {
			setChoice(colChoice, player);
		}

		return colChoice;
	}

	public int getRandomMove(int player) {
		int col = -1;

		while (col == -1) {
			Random rand = new Random();
			int max = c4logic.getGameBoard()[0].length - 1;
			int min = 0;
			int randomCol = rand.nextInt((max - min) + 1) + min;

			col = setChoice(randomCol, player);
		}

		return col;
	}

	private void getResponce() {
		try {
			byte[] packet = converser.receivePacket(connection);
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case RESET_GAME:
				playAgain = true;
				gameOver = false;
				c4logic.resetGame();
				break;
			case DISCONNECT:
				playAgain = false;
				gameOver = true;
				break;
			case MOVE: // RECEIVING A MOVE FROM CLIENT
				setPlayerChoice(packet);
				break;
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setPlayerChoice(byte[] packet) {
		int col = (int) packet[2];
		int clientToken = 1;
		col = c4logic.setChoice(col, clientToken);

		if (col != -1) { // means it was a valid move
			int row = c4logic.getUpmostRow(col);

			if (c4logic.checkWin(col, clientToken)) {
				gameOver = true;
				sendWinPacket(row, col);
			} else if (c4logic.checkDraw()) {
				gameOver = true;
				sendTiePacket(row, col, clientToken);
			} else { // server turn to make a move
				setServerChoice(row, col);
			}
		} else { // means it was an invalid move
			sendBadMovePacket();
		}
	}

	private void setServerChoice(int rowClient, int colClient) {
		int serverToken = 2;
		int clientToken = 1;
		int colServer = decideMove(serverToken, clientToken);

		if (colServer != -1) { // means it was a valid move
			int rowServer = c4logic.getUpmostRow(colServer);

			if (c4logic.checkWin(colServer, serverToken)) {
				gameOver = true;
				sendLosePacket(rowServer, colServer);
			} else if (c4logic.checkDraw()) {
				gameOver = true;
				sendTiePacket(rowServer, colServer, serverToken);
			} else {
				sendBackClientAndServerMove(rowClient, colClient, rowServer, colServer);
			}
		}
	}

	private void sendBackClientAndServerMove(int rowClient, int colClient, int rowServer, int colServer) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.MOVE.getValue(), rowClient, colClient, rowServer,
					colServer);
			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendBadMovePacket() {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.BAD_MOVE.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendConnectPacket() {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.CONNECT.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendLosePacket(int row, int col) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.LOSE.getValue(), -1, -1, row, col);
			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendTiePacket(int row, int col, int player) {
		try {
			byte[] packet = null;
			// to display the last move, on the client GUI and who was move made
			// by
			if (player == 1) // CLIENT
				packet = converser.createPacket(PACKET_TYPE.TIE.getValue(), row, col, -1, -1);
			else // SERVER
				packet = converser.createPacket(PACKET_TYPE.TIE.getValue(), -1, -1, row, col);

			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendWinPacket(int row, int col) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.WIN.getValue(), row, col, -1, -1);
			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}