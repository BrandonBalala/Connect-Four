package com.dc.ConnectFourGame.shared;

public class C4Logic {
	int[][] gameBoard;
	private int playerMarker;
	private int serverMarker;

	public C4Logic() {
		gameBoard = new int[6][7];
		playerMarker = 21;
		serverMarker = 21;
	}

	public int[][] getGameBoard() {
		return gameBoard;
	}

	public int setChoice(int col, int player) {
		if (col < 0 || col > gameBoard[0].length)
			return -1;

		int colChosen = -1;

		for (int row = gameBoard.length - 1; row >= 0; row--) {
			if (gameBoard[row][col] != 0) {
				gameBoard[row][col] = player;
				colChosen = col;
			}
		}

		if (colChosen != -1) {
			if (player == 1)
				playerMarker--;
			else if (player == 2)
				serverMarker--;
		}

		return colChosen;
	}

	/**
	 * This method checks whether the last move invoked a win
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean checkWin(int col, int player) {
		int row = getUpmostRow(col);
		int min = 0;
		int rowMax = gameBoard.length;
		int colMax = gameBoard[0].length;

		// Horizontal Check
		// Left
		int fourInARow = 1;
		for (int c = col - 1; c >= min; c--) {
			if (gameBoard[row][c] == player)
				fourInARow++;
			else
				break;
		}

		// Right
		for (int c = col + 1; c < colMax; c++) {
			if (gameBoard[row][c] == player)
				fourInARow++;
			else
				break;
		}

		if (fourInARow >= 4) {
			return true;
		} else
			fourInARow = 1;

		// Vertical Check
		// Up
		for (int r = row - 1; r >= min; r--) {
			if (gameBoard[r][row] == player)
				fourInARow++;
			else
				break;
		}

		// Down
		for (int r = row + 1; r < rowMax; r++) {
			if (gameBoard[r][col] == player)
				fourInARow++;
			else
				break;
		}

		if (fourInARow >= 4) {
			return true;
		} else
			fourInARow = 1;

		// Diagonal Upward Check
		// South-West
		for (int r = row + 1, c = col - 1; r < rowMax && c >= min; r++, c--) {
			if (gameBoard[r][c] == player)
				fourInARow++;
			else
				break;
		}
		// Nort-East
		for (int r = row - 1, c = col + 1; r >= 0 && min < colMax; r--, c++) {
			if (gameBoard[r][c] == player)
				fourInARow++;
			else
				break;
		}

		if (fourInARow >= 4) {
			return true;
		} else
			fourInARow = 1;

		// Diagonal Downward Check
		// North-West
		for (int r = row - 1, c = col - 1; r >= min && c >= min; r--, c--) {
			if (gameBoard[r][c] == player)
				fourInARow++;
			else
				break;
		}
		// Nort-East
		for (int r = row + 1, c = col + 1; r < rowMax && c < colMax; r++, c++) {
			if (gameBoard[r][c] == player)
				fourInARow++;
			else
				break;
		}

		if (fourInARow >= 4) {
			return true;
		} else
			fourInARow = 1;

		return false;
	}

	public boolean checkDraw() {
		if (playerMarker == 0 & serverMarker == 0)
			return true;

		return false;
	}

	public int getUpmostRow(int col) {
		int row = -1;

		for (int cntr = 0; cntr < gameBoard.length; cntr++) {
			if (gameBoard[cntr][col] != 0) {
				row = cntr;
			}
		}

		return row;
	}
}
