package com.dc.ConnectFourGame.shared;

import java.util.Random;

public class C4Logic {
	int[ ][ ] gameBoard;
	
	public C4Logic(){
		gameBoard = new int[6][7];
	}
	
	
	public int setChoice(int col, int player){
		if(col < 0 || col > gameBoard[0].length)
			return -1;
		
		int colChosen = -1;
		
		for(int row = gameBoard.length - 1; row >= 0; row--){
			if(gameBoard[row][col] != 0){
				gameBoard[row][col] = player;
				colChosen = col;
			}
		}
		
		return col;	
	}
	
	public boolean checkWinOrLose(){
		return false;
	}
	
	public boolean decideMove(){
		return false;
	}
	
	public int randomMove(int player){
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
