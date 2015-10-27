package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;

public class C4ServerSession extends C4Logic {

	private boolean playAgain;
	private boolean gameOver;
	Socket connection;

	C4Packet converser;
	C4Logic c4logic;

	private int[][] gameBoard = c4logic.getGameBoard();
	private int rowLength = gameBoard.length;
	private int colLength = gameBoard[0].length;

	public C4ServerSession(Socket connection) throws IOException {

		playAgain = true;
		gameOver = false;
		this.connection = connection;

		converser = new C4Packet();
		c4logic = new C4Logic();
		start();

	}

	private void start() throws IOException {
		byte[] packet = converser.receivePacket(connection);
		if (packet[0] == 2)
			playAgain = true;
		else if (packet[0] == 3) {
			playAgain = false;
			gameOver = true;
		}
		startGameSession();
	}

	public void startGameSession() throws IOException {

		byte[] b = { 0, 0, 0 };
		sendPacket(b);

		// player can play more than one game
		while (playAgain) {
			while (!gameOver) {
				int column = decideMove(2, 1);
				int row = c4logic.getUpmostRow(column);

				if (c4logic.checkDraw())	
					gameOver = true;

				if (c4logic.checkWin(column, 2))
					gameOver = true;
				// WTFFFFFFFFFFFFFFFFFFFFFFFFF CHECK WIN CHECK
				// DRAW????????????????
				// resend packet with column and row that was already put
				// internally
			}

		}

		connection.close();
	}

	public void sendPacket(byte[] packet) throws IOException {
		converser.sendPacket(packet, connection);
	}

	public int decideMove(int serverToken, int clientToken) {
		// to win
		int column = getWinOrBlockMove(serverToken, serverToken);

		// to lose
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
		int column;
		// Horizontal check
		column = horizontalCheck(target, player);

		if (column != -1)
			return column;

		// Vertical check
		column = verticalCheck(target, player);

		if (column != -1)
			return column;

		// Diagonal check
		column = diagonalCheck(target, player);

		return column;
	}

	private int diagonalCheck(int target, int player) {

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
					colChoice = col + 1;
					break outerLoop;
				}
			}
		}
		if (colChoice != -1) {
			setChoice(colChoice, player);
		}
		return colChoice;
	}

	private int verticalCheck(int target, int player) {

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
					colChoice = col + 1;
					break outerLoop;
				}
				if (token3 == 0 && token1 == target && token2 == target && token4 == target) {
					colChoice = col + 2;
					break outerLoop;
				}
				if (token4 == 0 && token1 == target && token2 == target && token3 == target) {
					colChoice = col + 1;
					break outerLoop;
				}
			}
		}
		if (colChoice != -1) {
			setChoice(colChoice, player);
		}

		return colChoice;
	}

	private int horizontalCheck(int target, int player) {

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
					colChoice = col + 1;
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
}