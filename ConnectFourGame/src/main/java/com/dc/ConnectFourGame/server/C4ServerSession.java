package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;

public class C4ServerSession extends C4Logic {

	private boolean playAgain;
	private boolean gameOver;
	private Socket connection;
	private C4Packet converser;
	private C4Logic c4logic;

	public C4ServerSession(Socket connection) throws IOException {
		//playAgain = true;
		//gameOver = false;
		this.connection = connection;
		converser = new C4Packet();
		c4logic = new C4Logic();
		start();

	}

	private void start() throws IOException {
		byte[] packet = converser.receivePacket(connection);
		if (packet[0] == 1){  //START GAME
			playAgain = true;
			gameOver = false;
		}
		else if(packet[0] == 2){ //PLAY AGAIN OR RESET GAME
			//resets the gameBoard
			playAgain = true;
			gameOver = false;
			c4logic = new C4Logic();
		}
		else if (packet[0] == 3) { //END GAME
			playAgain = false;
			gameOver = true;
		}
		
		startGameSession();
	}
	
	public void startGameSession() throws IOException {
		//WHAT OUR PACKETS SHOULD LOOK LIKE ?
		//byte[] b = { THE TYPE OF PACKAGE, ROW CHOSEN, COL CHOSEN };
		//ROW AND COL CAN BE SET TO -1 IF THEY  ARE NOT NEEDED
		//TYPES suggestions!:
		//1 -> establish connection to server,then resend back 0 to client to let him know to start making moves 
		//2 -> PLAY AGAIN OR RESET GAME, client makes request to play again or rest game
		//3 -> END GAME, client informs server to end game
		//4 -> WIN sent to client to notify win
		//5 -> LOSE, sent to client to notify lose
		//6 -> TIE, sent to client to notify tie
		//7 -> MOVE, so if it is this, col and row are important.
		//8	-> ERROR, inform client that he sent invalid move
		//9 -> idk
		//etc...
		byte[] b = { 0, 0, 0 };
		sendPacket(b);

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
		int[][] gameBoard = c4logic.getGameBoard();
		int rowLength = gameBoard.length;
		int colLength = gameBoard[0].length;
		
		int column;
		// Horizontal check
		column = horizontalCheck(target, player, gameBoard,rowLength, colLength);

		if (column != -1)
			return column;

		// Vertical check
		column = verticalCheck(target, player, gameBoard,rowLength, colLength);

		if (column != -1)
			return column;

		// Diagonal Upwards check
		column = diagonalUpwardsCheck(target, player, gameBoard,rowLength, colLength);

		if (column != -1)
			return column;

		// Diagonal Upwards check
		column = diagonalDownwardsCheck(target, player, gameBoard,rowLength, colLength);

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
}