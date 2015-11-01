package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
import com.dc.ConnectFourGame.shared.Identifier;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

public class C4ServerSession extends C4Logic {

	private boolean playAgain;
	private boolean gameOver;
	private Socket connection;
	private C4Packet converser;

	public C4ServerSession(Socket connection) throws IOException {
		this.connection = connection;
		converser = new C4Packet();
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
	public int decideMove() {
		int[] ranks = new int[7];
		for(int column = 0; column < 7; column++)
		{
			ranks[column] = getRank(column, Identifier.Server);
		}
		int choice = 0;
		for(int i = 1; i < 7; i++)
		{
			if(ranks[i]>ranks[choice])
				choice = i;
		}
			return choice;
	}

	private void getResponce() {
		try {
			byte[] packet = converser.receivePacket(connection);
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case RESET_GAME:
				playAgain = true;
				gameOver = false;
				newGame();
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
		col = setChoice(col, Identifier.Client);

		if (col != -1) { // means it was a valid move
			int row = getNextEmptyRow(col);

			if (checkWin(col, Identifier.Client)) {
				gameOver = true;
				sendWinPacket(row, col);
			} else if (checkDraw()) {
				gameOver = true;
				sendTiePacket(row, col, Identifier.Client);
			} else { // server turn to make a move
				setServerChoice(row, col);
			}
		} else { // means it was an invalid move
			sendBadMovePacket();
		}
	}

	private void setServerChoice(int rowClient, int colClient) {
		int colServer = decideMove();

		if (colServer != -1) { // means it was a valid move
			int rowServer = getNextEmptyRow(colServer);

			if (checkWin(colServer, Identifier.Server)) {
				gameOver = true;
				sendLosePacket(rowServer, colServer);
			} else if (checkDraw()) {
				gameOver = true;
				sendTiePacket(rowServer, colServer, Identifier.Server);
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

	private void sendTiePacket(int row, int col,Identifier player) {
		try {
			byte[] packet = null;
			// to display the last move, on the client GUI and who was move made
			// by
			if (player == Identifier.Client) // CLIENT
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