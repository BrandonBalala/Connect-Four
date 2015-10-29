package com.dc.ConnectFourGame.client;

import java.io.IOException;
import java.net.Socket;

import com.dc.ConnectFourGame.controllers.BoardController;
import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;
import com.dc.ConnectFourGame.shared.PACKET_TYPE;

public class C4Client extends C4Logic {

	private Socket connection;
	private C4Packet converser;
	private String address;

	private int port = 50000;
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
		controller = new BoardController();
		address = controller.getConnectingIP();
		startConnection(address, port);
	}

	public boolean startConnection(String serverIp, int serverPort) throws IOException {
		try {
			connection = new Socket(serverIp, serverPort);
			byte [] packet = converser.createPacket(PACKET_TYPE.CONNECT.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet, connection);
			getResponce();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public void getResponce() {
		try {
			byte[] packet = converser.receivePacket(connection);
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case CONNECT:
				controller.setStatusMessage("Prepare to lose. MUHAHA");
				break;
			case RESET_GAME:
				controller.resetBoard();
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
				break;
			case LOSE:
				controller.setStatusMessage("YOU LOSE...");
				break;
			case TIE:
				controller.setStatusMessage("IT'S A DRAW.");
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
			packet = converser.createPacket(PACKET_TYPE.RESET_GAME.getValue(), -1, -1, -1, -1);
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
			packet = converser.createPacket(PACKET_TYPE.DISCONNECT.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet, connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMovePacket(int colChosen) {
		try {
			byte[] packet = converser.createPacket(PACKET_TYPE.MOVE.getValue(), -1, colChosen, -1, -1);
			converser.sendPacket(packet, connection);
			getResponce();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
