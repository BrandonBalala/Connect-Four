package com.dc.ConnectFourGame.shared;


public class C4Logic {
	Identifier[ ][ ] gameBoard;
	private int playerMarkers;
	private int serverMarkers;

	public C4Logic() {
		newGame();
	}

	public Identifier[][] getGameBoard() {
		return gameBoard;
	}

	public boolean setChoice(int row,int column, Identifier player){;
	    row+=2;
	    column+=2;
		if(isValidMove(row,column))
		{
			gameBoard[row][column] = player;
		
			if (player == Identifier.Client)
				playerMarkers--;
			else if (player == Identifier.Server)
				serverMarkers--;
			
			return true;
		}
		else
			return false;
	}
	

	/**
	 * This method checks whether the last move invoked a win
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean checkWin(int column,int row, Identifier player){
		int checkType = 0;
		int repeatNum = 0;
		column+=3;
		row=+3;
		int r=row,c=column;
		boolean repeated=true;
					
						while(checkType < 7)
						{
							while(repeated && repeatNum < 3)
							{
								switch(checkType)
								{
									case(0):  c++;
											  break;
									case(1):  c--;
											  break;
									case(2):  r++;
									          break;
									case(3):  c--;
											  r--;
									          break;
									case(4):  c++;
									  		  r++;
									  		  break;
									case(5):  c++;
									 		  r--;
									 		  break;
									case(6):  c--;
											  r++;
											  break;
								}
								
								if(player.equals(gameBoard[r][c]))
								{
									repeatNum++;
								}
								else
								{
									repeated = false;
									repeatNum = 0;
								}
								
							}
							if(repeated)
								return repeated;
							else
							{
								checkType++;
								r=row;
								c=column;
							}
						}
		return repeated;
	}


	public int getRank(int column,Identifier player)
	{
		Identifier opponent =(player!=Identifier.Client)?Identifier.Client:Identifier.Server;
		int rank=0;
		int checkType = 0;
		int opponentCount,playerCount,repeatNum;
		boolean repeated=true;
		int row=getNextEmptyRow(column);
		int r=row,c=column;
		
		while(checkType < 7)
		{
			opponentCount = 0;
			playerCount = 0;
			repeatNum = 0;
			while(repeated && repeatNum < 3)
			{
				switch(checkType)
				{
					case(0):  c++;
							  break;
					case(1):  c--;
							  break;
					case(2):  r++;
					          break;
					case(3):  c--;
							  r--;
					          break;
					case(4):  c++;
					  		  r++;
					  		  break;
					case(5):  c++;
					 		  r--;
					 		  break;
					case(6):  c--;
							  r++;
							  break;
				}
				
				
				if(player.equals(gameBoard[r][c]))
				{
					if(opponentCount > 0)
						repeated = false;
					else
						playerCount++;
				}
				else if (opponent.equals(gameBoard[r][c]))
				{
					if(playerCount > 0)
						repeated = false;
					else
						opponentCount++;
				}
				repeatNum++;
			}
			
			//The values (value of last * 7) make sure that just because there are 6 options
			// of one move, the rank does not end up summing to higher then a more important move 
			//( example 6 options to block opponent with 3 tokens does not take priority over placing a 4th and winning)
			if(opponentCount > 0 && playerCount == 0 )
			{
				switch(opponentCount)
				{
				case 2: rank += 7;
						break;
				case 3: rank += 343;
						break;
				default: rank += 1;
				}
			}
			else if(playerCount> 0 && opponentCount == 0 )
			{
				switch(playerCount)
				{
				case 2: rank += 49;
						break;
				case 3: rank += 2401;
						break;
				default: rank += 1;
				}
			}
			checkType++;
			r=row;
			c=column;
		}
		
		return rank;
	}
	
	public boolean checkDraw() {
		if (playerMarkers == 0 & serverMarkers == 0)
			return true;

		return false;
	}

	public boolean isValidMove(int row,int column)
	{
		if(		
			column > 2 &&
			column < gameBoard[0].length-3 &&
			row >2 && row<gameBoard.length-3 &&
			gameBoard[row][column]==null &&
			row == getNextEmptyRow(column)){
			
			return true;
		}
		else
			return false;
		
	}
	public int getNextEmptyRow(int column) {
		int row = -1;

		for (int cntr = 3; cntr < gameBoard.length-2; cntr++) 
		{
			if (gameBoard[cntr][column] != null) {
				row = cntr + 1;
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
