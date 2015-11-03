package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
import com.dc.ConnectFourGame.shared.Identifier;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

public class C4ServerSession extends C4Logic {

	private boolean playAgain;
	private boolean gameOver;
	private C4Packet converser;

	public C4ServerSession(Socket connection) throws IOException {
		converser = new C4Packet(connection);
		start();

	}

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

	public void startGameSession() throws IOException {
		while (playAgain) {
			while (!gameOver) {
				getResponce();
			}

		}

		converser.connectionClose();
	}

	public void sendPacket(byte[] packet) throws IOException {
		converser.sendPacket(packet);
	}

	// Moves are set onto the gameboard in getWinOrBlockMove or getRandomMove
	public int decideMove() {

		System.out.println("DECIDING CRAP");
		boolean identicalValues = true;
		int[] ranks = new int[7];
		int i = 0;
		for(int column = 3; column < 10; column++)
		{
			ranks[i] = getRank(column, Identifier.Server);
			i++;
		}
		int choice = 0;
		for(i = 1; i < 6; i++)
		{
			if(ranks[i] != ranks[i+1])
			{
				identicalValues = false;
			}
		}
		
		if(!identicalValues)
		{
			for(i = 0; i < 7; i++)
			{
				if(ranks[i]>ranks[choice])
					choice = i;
			}
		}
		else
		{
			choice = (int) (Math.random()*7);
		}
		
		if(ranks[choice] < 0)
		{
			List<Integer> list = new ArrayList<Integer>();
			for(i = 0;i<ranks.length;i++)
			{
				if(ranks[i] >=0)
					list.add(ranks[i]);
			}
			choice = (int) (Math.random()*list.size());
		}
			System.out.println(" CHOICE   : " + choice);
			return choice;
	}

	private void getResponce() {
		try {
			byte[] packet = converser.receivePacket();
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case RESET_GAME:
				playAgain = true;
				gameOver = false;
				newGame();
				sendResetGamePacket();
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
		int column = (int) packet[2];
		int row =(int) packet[1];
		
		if( setChoice(column, Identifier.Client) == row)
		{
			if (checkWin(column, row, Identifier.Client)) {
				gameOver = true;
				sendWinPacket(row, column);
			} else { // server turn to make a move
				setServerChoice(row, column);
			}
		} else { // means it was an invalid move
			sendBadMovePacket();
		}
	}

	private void setServerChoice(int rowClient, int colClient) {
		int colServer = decideMove();
		int rowServer = getNextEmptyRow(colServer);

		
		//When you fill up several rows this causes a hang!!@#!#@#$ TODO FIX
		while(!isValidMove(rowServer,colServer))
		{
			System.out.println("ROW   : " + rowServer + "   COLUMN : " +colServer);
			colServer = decideMove();
			rowServer = getNextEmptyRow(colServer);
		}
			setChoice(colServer,Identifier.Server);
			if (checkWin(colServer, rowServer, Identifier.Server)) {
				gameOver = true;
				sendLosePacket(rowClient, colClient,rowServer, colServer);
			} else if (checkDraw()) {
				gameOver = true;
				sendTiePacket(rowClient, colClient,rowServer, colServer, Identifier.Server);
			} else {
				sendBackClientAndServerMove(rowClient, colClient, rowServer, colServer);
			}
		}
	

	private void sendBackClientAndServerMove(int rowClient, int colClient, int rowServer, int colServer) {
		try {

			byte[] packet = converser.createPacket(PACKET_TYPE.MOVE.getValue(), rowClient, colClient, rowServer,
					colServer);
			converser.sendPacket(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendBadMovePacket() {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.BAD_MOVE.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendConnectPacket() {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.CONNECT.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendLosePacket(int rowClient, int colClient, int rowServer, int colServer) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.LOSE.getValue(), rowClient, colClient, rowServer, colServer);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendTiePacket(int rowClient, int colClient, int rowServer, int colServer,Identifier player) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.TIE.getValue(), rowClient, colClient, rowServer, colServer);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendWinPacket(int row, int col) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.WIN.getValue(), row, col, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendResetGamePacket() {
		// TODO Auto-generated method stub
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.RESET_GAME.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}