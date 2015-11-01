package com.dc.ConnectFourGame.client;

import java.io.IOException;
import java.net.Socket;

import com.dc.ConnectFourGame.controllers.BoardController;
import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
import com.dc.ConnectFourGame.shared.Identifier;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

public class C4Client extends C4Logic {

	private Socket connection;
	private C4Packet converser;
	private String address;
	private boolean connected;

	private int port = 62366;
	private BoardController controller;

	public C4Client() {
		converser = new C4Packet();
	}
	
	public C4Packet getC4Packet(){
		return converser;	
	}
	
	public Socket getSocket(){
		return connection;	
	}

	public void readIp() throws IOException {
		controller = new BoardController(this);
		address = controller.getConnectingIP();
		startConnection(address, port);
	}

	public boolean startConnection(String serverIp, int serverPort) throws IOException {
		try {
			connection = new Socket(serverIp, serverPort);
			byte [] packet = converser.createPacket(PACKET_TYPE.CONNECT.getValue(), -1, -1);
			converser.sendPacket(packet, connection);
			getResponce();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public void getResponce() {
		System.out.println("response\n");
		try {
			byte[] packet = converser.receivePacket(connection);
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case CONNECT:
				controller.setStatusMessage("Prepare to lose. MUHAHA");
				controller.setIsConnected(true);
				connected = true;
				break;
			case RESET_GAME:
				controller.resetBoard();
				controller.setIsConnected(true);
				connected = true;
				break;
			case MOVE:
				controller.setMovesOnBoard(packet);
				break;
			case BAD_MOVE:
				// DO NOTHING OR DISPLAY A MESSAGE SAYING IT WAS A BAD MOVE?
				controller.setStatusMessage("Bad move");
				break;
			case WIN:
				controller.setStatusMessage("YOU WON!");
				controller.setIsConnected(false);
				connected = false;
				break;
			case LOSE:
				controller.setStatusMessage("YOU LOSE...");
				controller.setIsConnected(false);
				connected = false;
				break;
			case TIE:
				controller.setStatusMessage("IT'S A DRAW.");
				controller.setIsConnected(false);
				connected = false;
				break;
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendResetPacket() {
		byte[] packet;
		try {
			packet = converser.createPacket(PACKET_TYPE.RESET_GAME.getValue(), -1, -1);
			converser.sendPacket(packet, connection);
			getResponce();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendDisconnectPacket() {
		// TODO Auto-generated method stub
		byte[] packet;
		try {
			packet = converser.createPacket(PACKET_TYPE.DISCONNECT.getValue(), -1, -1);
			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeMove(int column) throws Exception
	{
		int row =getNextEmptyRow(column);
		if(setChoice(row,column, Identifier.Client))
		{
			try {
				sendMovePacket(column,row);
			} catch (IOException e) {
				//rollback play
				getGameBoard()[row][column] = null;
				throw new Exception("Error encountered when attempting to play move");
			}
		}
		else
			throw new Exception("Error encountered when attempting to play move");
	}
	public void sendMovePacket(int column,int row) throws IOException {
			byte[] packet = converser.createPacket(PACKET_TYPE.MOVE.getValue(), row, column);
			converser.sendPacket(packet, connection);
			getResponce();
		
	}

}
