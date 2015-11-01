package shared;

import java.util.Random;

public class C4Logic {
	Identifier[ ][ ] gameBoard;
	
	public C4Logic(){
		gameBoard = new Identifier[12][13];
	}
	
	
	public int setChoice(int col, Identifier player){
		col+=3;
		if(col < 0 || col > gameBoard[0].length-4)
			return -1;
		
		int colChosen = -1;
		
		for(int row = gameBoard.length - 1; row >= 0; row--){
			if(gameBoard[row][col] != null){
				gameBoard[row][col] = player;
				colChosen = col;
			}
		}
		
		return colChosen;	
	}
	
	public boolean checkWinOrLose(){
		return false;
	}
	/*
	 * Return enum of winner or a null if no win found.
	 */
	public Identifier checkRepeated()
	{
		int checkType = 0;
		int repeatNum = 0;
		Identifier currentTile;
		int row = gameBoard.length-4;
		int column = 3;
		int r,c;
		boolean repeated=true;
			while(column <= gameBoard[0].length-4)
			{
				while(row>=3)
				{		
					currentTile= gameBoard[row][column];
					r=row;
					c=column;
					
					 if(currentTile == null)
						 break;
					
						while(checkType < 4)
						{
							while(repeated && repeatNum < 3)
							{
								switch(checkType)
								{
									case(0):  c++;
											  break;
									case(1):  r--;
											  break;
									case(2):  c--;
											  r--;
											  break;
									case(3):  c++;
											  r--;
											  break;
								}
								
								if(currentTile.equals(gameBoard[r][c]))
								{
									repeatNum++;
								}
								else
								{
									repeated=false;
									repeatNum = 0;
								}
								
							}
						
						if(!repeated)
						{
							checkType++;
							repeated=true;
							r=row;
							c=column;
						}
						else
							return currentTile;
						
						}
					
					checkType=0;
					row--;
				}
				row = gameBoard.length-4;
				column++;
			}
			return null;
	}
	
	public boolean decideMove(){
		return false;
	}
	
	public void AIPlay()
	{
		
	}
	
	public int randomMove(Identifier player){
		int col = -1;
		
		while(col != -1){
			Random rand = new Random();
			int max = gameBoard[0].length - 1;
			int min = 0;
			int randomCol = rand.nextInt((max - min) + 1) + min;
			
			col  = setChoice(randomCol, player);
		}
		
		return col;
	}
}
