package com.dc.ConnectFourGame.shared;

/**
 * Class that contains all the logic to update the board internally,
 * check for win, draws, the ranks of moves for the ai and if a move is valid.
 * 
 *  @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public class C4Logic {
	//2d array of enums: client or server
	Identifier[][] gameBoard;
	private int playerMarkers;
	private int serverMarkers;

	/**
	 * Constructor that calls the intialization method.
	 */
	public C4Logic() {
		newGame();
	}

	/**
	 * Gets the board of the game
	 * 
	 * @return the 2d array representing the board
	 */
	public Identifier[][] getGameBoard() {
		return gameBoard;
	}

	/**
	 * Validates the move, sets it in the board
	 * and updates the total number of markers.
	 * 
	 * @param column
	 * 			the column that was choosen 
	 * @param player
	 * 			the enum of the user playing to put in board
	 * 
	 * @return the row the move was put on
	 */
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
	 * 			the row played
	 * @param col
	 * 			the column played
	 * @param identity
	 * 			the enum of the player it is checking for
	 * 
	 * @return if the play was a win or not
	 */
	public boolean checkWin(int column, int row, Identifier identity) {
		int direction = 0;
		int checkType = 0;
		int repeatNum = 0;
		int r = row, c = column;
		boolean repeated = true;

		// Horizontal Check (twice)
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
			//re-set values for next CASE
			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}
		// if theres at least 4 markers in a row = win
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

		// Vertical Check (twice)
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
			
			//re-set values for next CASE
			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}
		// if theres at least 4 markers in a row = win
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

		// Diagonal-Upwards (twice)
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
			//re-set values for next CASE
			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}
		// if theres at least 4 markers in a row = win
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

		// Diagonal-Downwards (twice)
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
			//re-set values for next CASE
			r = row;
			c = column;
			repeated = true;
			direction++;
			checkType++;
		}
		// if theres at least 4 markers in a row = win
		if (repeatNum >= 3) {
			return true;
		}

		//no 4 or more markers in a row were found
		return false;
	}

	/**
	 * Gives a rank to a move for the ai to be able to 
	 * decide which move is the best to play.
	 * 
	 * @param column
	 * 			the row choosen to play
	 * @param player
	 * 			the enum of who is playing
	 * 
	 * @return the number(rank) a certain move got qualified as
	 */
	public int getRank(int column, Identifier player) {
		Identifier opponent = 
				(player != Identifier.Client) ? Identifier.Client : Identifier.Server;
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
			
			//checking  horizontal 
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
							//found one marker of the play
							playerCount++;
					} else if (opponent.equals(gameBoard[r][c])) {
						if (playerCount > 0)
							repeated = false;
						else
							//found one marker of the opponent
							opponentCount++;
					} else {
						//the sequence stopped
						repeated = false;
					}
				}
				r = row;
				c = column;
				direction++;
				checkType++;
				repeated = true;
			}
			//winning move
			if (playerCount > 2) {
				totalRank += 341;
				//make a line of 3 of your own markers
			} else if (playerCount > 1) {
				totalRank += 21;
			} else
				//random move
				totalRank += 1;

			//blocking move of a line of 3
			if (opponentCount > 2) {
				totalRank += 85;
				//block a line of 2
			} else if (opponentCount > 1) {
				totalRank += 5;
			} else
				//random move
				totalRank += 1;

			//set back to original value
			opponentCount = 0;
			playerCount = 0;
			direction = 0;
			checkType = 0;

			//checking diagonal downwards
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
							//found a player marker
							playerCount++;
					} else if (opponent.equals(gameBoard[r][c])) {
						if (playerCount > 0)
							repeated = false;
						else
							//found an opponent marker
							opponentCount++;
					} else {
						//sequence stopped
						repeated = false;
					}
				}
				r = row;
				c = column;
				direction++;
				checkType++;
				repeated = true;
			}
			//winning move
			if (playerCount > 2) {
				totalRank += 341;
				//continue a line of 2
			} else if (playerCount > 1) {
				totalRank += 21;
			} else
				//random move
				totalRank += 1;

			//block line of 3
			if (opponentCount > 2) {
				totalRank += 85;
				//block line of 2
			} else if (opponentCount > 1) {
				totalRank += 5;
			} else
				//random move
				totalRank += 1;

			opponentCount = 0;
			playerCount = 0;
			direction = 0;
			checkType = 0;

			//diagonal upwards
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
							//found one of our own markers
							playerCount++;
					} else if (opponent.equals(gameBoard[r][c])) {
						if (playerCount > 0)
							repeated = false;
						else
							//found an opponent marker
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
			//winning move
			if (playerCount > 2) {
				totalRank += 341;
			} else if (playerCount > 1) {
				//continue line of 2
				totalRank += 21;
			} else
				//random move
				totalRank += 1;
			if (opponentCount > 2) {
				//block winning move
				totalRank += 85;
			} else if (opponentCount > 1) {
				//block line of 2
				totalRank += 5;
			} else
				//random
				totalRank += 1;

			//check vertical
			while (repeated)
			{	
				r++;

				if (player.equals(gameBoard[r][c])) {
					if (opponentCount > 0)
						repeated = false;
					else
						//found own marker
						playerCount++;
				} else if (opponent.equals(gameBoard[r][c])) {
					if (playerCount > 0)
						repeated = false;
					else
						//found oppponent marker
						opponentCount++;
				} else {
					repeated = false;
				}
			}

			//winning move
			if (playerCount > 2) {
				totalRank += 341;
			} else if (playerCount > 1) {
				//continue line of 2
				totalRank += 21;
			} else
				//random move
				totalRank += 1;

			if (opponentCount > 2) {
				//block winning move
				totalRank += 85;
			} else if (opponentCount > 1) {
				//block line of 2
				totalRank += 5;
			} else
				//random move
				totalRank += 1;

			return totalRank;
		} else
			//invalid move. row sent in was not valid 
			return -1;

	}

	/**
	 * Checks if the game is ended as a draw 
	 * if both players dont have anymore markers
	 * 
	 * @return if the game is in a state of draw
	 */
	public boolean checkDraw() {
		if (playerMarkers == 0 & serverMarkers == 0)
			return true;

		return false;
	}

	/**
	 * Checks if the move is within the four borders of the board
	 * and if there isnt any move already in that spot. 
	 * 
	 * @param row 
	 * 			the row of the move to check
	 * @param column
	 * 			the column of the move to check
	 * 
	 * @return whether or not the move is valid
	 */
	public boolean isValidMove(int row, int column) {
		if (column > 2 && column < gameBoard[0].length - 3 &&
				row > 2 && row < gameBoard.length - 3
				&& gameBoard[row][column] == null) {

			return true;
		} else
			return false;

	}

	/**
	 * Checks the board for the next empty row.
	 * If the row is full, -1 will be returned.
	 * 
	 * @param column
	 * 			the column for which to get the empty row
	 * 
	 * @return	the number of the row which is empty
	 */
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

	/**
	 * Resets the game variables, the enums in the board and
	 * the markers, to play a new game.
	 */
	public void newGame() {
		gameBoard = new Identifier[12][13];
		playerMarkers = 21;
		serverMarkers = 21;
	}
}
