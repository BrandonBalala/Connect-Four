package com.dc.ConnectFourGame.shared;

public class C4Logic {
	Identifier[][] gameBoard;
	private int playerMarkers;
	private int serverMarkers;

	public C4Logic() {
		newGame();
	}

	public Identifier[][] getGameBoard() {
		return gameBoard;
	}

	public int setChoice(int column, Identifier player) {
		int result = -1;
		int row = getNextEmptyRow(column);

		if (isValidMove(row, column)) {
			gameBoard[row][column] = player;

			if (player == Identifier.Client)
				playerMarkers--;
			else if (player == Identifier.Server)
				serverMarkers--;
			result = row;
		}
		return result;

	}

	/**
	 * This method checks whether the last move invoked a win
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean checkWin(int column, int row, Identifier identity) {
		int direction = 0;
		int checkType = 0;
		int repeatNum = 0;
		int r = row, c = column;
		boolean repeated = true;

		// Horizontal Check
		while (checkType < 2) {
			while (repeated) {
				switch (direction) {
				case 0: // Right
					c++;
					break;

				case 1: // Left
					c--;
					break;
				}

				if (identity == gameBoard[r][c])
					repeatNum++;
				else
					repeated = false;
			}
			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}
		if (repeatNum >= 3) {
			return true;
		}

		// Reset
		direction = 0;
		checkType = 0;
		repeatNum = 0;
		r = row;
		c = column;
		repeated = true;

		// Vertical Check
		while (checkType < 2) {
			while (repeated) {
				switch (direction) {
				case 0: // Down
					r++;
					break;

				case 1: // Up
					r--;
					break;
				}

				if (identity == gameBoard[r][c])
					repeatNum++;
				else
					repeated = false;
			}
			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}
		if (repeatNum >= 3) {
			return true;
		}

		// Reset
		direction = 0;
		checkType = 0;
		repeatNum = 0;
		r = row;
		c = column;
		repeated = true;

		// Diagonal-Upwards
		while (checkType < 2) {
			while (repeated) {
				switch (direction) {
				case 0: // Nort-East
					c++;
					r--;
					break;

				case 1: // South-West
					c--;
					r++;
					break;
				}

				if (identity == gameBoard[r][c])
					repeatNum++;
				else
					repeated = false;
			}

			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}
		if (repeatNum >= 3) {
			return true;
		}

		// Reset
		direction = 0;
		checkType = 0;
		repeatNum = 0;
		r = row;
		c = column;
		repeated = true;

		// Diagonal-Downwards
		while (checkType < 2) {
			while (repeated) {
				switch (direction) {
				case 0: // South-East
					c++;
					r++;
					break;
				case 1: // North-West
					c--;
					r--;
					break;
				}

				if (identity == gameBoard[r][c])
					repeatNum++;
				else
					repeated = false;
			}

			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}

		if (repeatNum >= 3) {
			return true;
		}

		return false;
	}

	public int getRank(int column, Identifier player) {
		Identifier opponent = (player != Identifier.Client) ? Identifier.Client : Identifier.Server;
		int totalRank = 0;
		int direction = 0;
		int checkType = 0;
		int row = getNextEmptyRow(column);
		int r = row, c = column;
		boolean repeated = true;
		int opponentCount = 0;
		int playerCount = 0;
		if (row != -1) {
			
			while (checkType < 2) {
				switch (direction) {
				case 0:
					c++;
					break;

				case 1:
					c--;
					break;
				}
				while (repeated) {
					
					switch (direction)
					{
					case 0:
						c++;
						r++;
						break;

					case 1:
						c--;
						r--;
						break;
					}

					if (player.equals(gameBoard[r][c])) {
						if (opponentCount > 0)
							repeated = false;
						else
							playerCount++;
					} else if (opponent.equals(gameBoard[r][c])) {
						if (playerCount > 0)
							repeated = false;
						else
							opponentCount++;
					} else {
						repeated = false;
					}
				}
				r = row;
				c = column;
				direction++;
				checkType++;
				repeated = true;
			}
			// MAKES SURE YOU DO NOT BUILD THEM UP FOR A WIN!
			if (opponentCount > 2)
				return 0;
			
			opponentCount = 0;
			playerCount = 0;
			direction = 0;
			checkType = 0;
			
			while (checkType < 2) {
				while (repeated) {
					switch (direction) {
					case 0:
						c++;
						break;

					case 1:
						c--;
						break;
					}

					if (player.equals(gameBoard[r][c])) {
						if (opponentCount > 0)
							repeated = false;
						else
							playerCount++;
					} else if (opponent.equals(gameBoard[r][c])) {
						if (playerCount > 0)
							repeated = false;
						else
							opponentCount++;
					} else {
						repeated = false;
					}
				}
				r = row;
				c = column;
				direction++;
				checkType++;
				repeated = true;
			}
			if (playerCount > 2) {
				totalRank += 341;
			} else if (playerCount > 1) {
				totalRank += 21;
			} else
				totalRank += 1;

			if (opponentCount > 2) {
				totalRank += 85;
			} else if (opponentCount > 1) {
				totalRank += 5;
			} else
				totalRank += 1;

			opponentCount = 0;
			playerCount = 0;
			direction = 0;
			checkType = 0;

			while (checkType < 2) {
				while (repeated) {
					switch (direction) {
					case 0:
						c++;
						r--;
						break;

					case 1:
						c--;
						r++;
						break;
					}

					if (player.equals(gameBoard[r][c])) {
						if (opponentCount > 0)
							repeated = false;
						else
							playerCount++;
					} else if (opponent.equals(gameBoard[r][c])) {
						if (playerCount > 0)
							repeated = false;
						else
							opponentCount++;
					} else {
						repeated = false;
					}
				}
				r = row;
				c = column;
				direction++;
				checkType++;
				repeated = true;
			}
			if (playerCount > 2) {
				totalRank += 341;
			} else if (playerCount > 1) {
				totalRank += 21;
			} else
				totalRank += 1;

			if (opponentCount > 2) {
				totalRank += 85;
			} else if (opponentCount > 1) {
				totalRank += 5;
			} else
				totalRank += 1;

			opponentCount = 0;
			playerCount = 0;
			direction = 0;
			checkType = 0;

			while (checkType < 2) {
				while (repeated) {
					switch (direction) {
					case 0:
						c++;
						r++;
						break;

					case 1:
						c--;
						r--;
						break;
					}

					if (player.equals(gameBoard[r][c])) {
						if (opponentCount > 0)
							repeated = false;
						else
							playerCount++;
					} else if (opponent.equals(gameBoard[r][c])) {
						if (playerCount > 0)
							repeated = false;
						else
							opponentCount++;
					} else {
						repeated = false;
					}
				}
				r = row;
				c = column;
				direction++;
				checkType++;
				repeated = true;
			}
			if (playerCount > 2) {
				totalRank += 341;
			} else if (playerCount > 1) {
				totalRank += 21;
			} else
				totalRank += 1;
			if (opponentCount > 2) {
				totalRank += 85;
			} else if (opponentCount > 1) {
				totalRank += 5;
			} else
				totalRank += 1;

			while (repeated)
			{	
				r++;

				if (player.equals(gameBoard[r][c])) {
					if (opponentCount > 0)
						repeated = false;
					else
						playerCount++;
				} else if (opponent.equals(gameBoard[r][c])) {
					if (playerCount > 0)
						repeated = false;
					else
						opponentCount++;
				} else {
					repeated = false;
				}
			}

			if (playerCount > 2) {
				totalRank += 341;
			} else if (playerCount > 1) {
				totalRank += 21;
			} else
				totalRank += 1;

			if (opponentCount > 2) {
				totalRank += 85;
			} else if (opponentCount > 1) {
				totalRank += 5;
			} else
				totalRank += 1;

			System.out.println(totalRank);
			return totalRank;
		} else
			return -1;

	}

	public boolean checkDraw() {
		if (playerMarkers == 0 & serverMarkers == 0)
			return true;

		return false;
	}

	public boolean isValidMove(int row, int column) {
		if (column > 2 && column < gameBoard[0].length - 3 && row > 2 && row < gameBoard.length - 3
				&& gameBoard[row][column] == null) {

			return true;
		} else
			return false;

	}

	public int getNextEmptyRow(int column) {
		int row = -1;

		for (int cntr = gameBoard.length - 4; cntr > 2; cntr--) {
			if (gameBoard[cntr][column] == null) {
				row = cntr;
				break;
			}
		}
		return row;
	}

	public void newGame() {
		gameBoard = new Identifier[12][13];
		playerMarkers = 21;
		serverMarkers = 21;
	}
}
