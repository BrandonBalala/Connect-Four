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
	private boolean canPlay;
	private int port = 62366;
	private BoardController controller;

	
	public C4Packet getC4Packet(){
		return converser;	
	}
	
	public Socket getSocket(){
		return connection;	
	}

	public void readIp(BoardController controller) throws IOException {
		this.controller = controller;
		address = controller.getConnectingIP();
		startConnection(address, port);
	}

	public boolean startConnection(String serverIp, int serverPort) throws IOException {
		try {
			connection = new Socket(serverIp, serverPort);
			converser = new C4Packet(connection);
			byte [] packet = converser.createPacket(PACKET_TYPE.CONNECT.getValue(), -1, -1, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public byte[] convertPacketToSmallBoard(byte[] packet)
	{
		packet[1]-=3;
		packet[2]-=3;
		packet[3]-=3;
		packet[4]-=3;
		return packet;
	}
	public void getResponce() {
		System.out.println("response\n");
		try {
			byte[] packet = converser.receivePacket();
			switch (PACKET_TYPE.values()[(int) packet[0]]) {
			case CONNECT:
				controller.setStatusMessage("Prepare to lose. MUHAHA");
				controller.setIsConnected(true);
				canPlay=true;
				break;
			case RESET_GAME:
				controller.resetBoard();
				controller.setIsConnected(true);
				controller.setNotWaiting(true);
				newGame();
				canPlay=true;
				break;
			case MOVE:
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet));
				setChoice(packet[2]+3,Identifier.Client);
				setChoice(packet[4]+3,Identifier.Server);
				break;
			case BAD_MOVE:
				// DO NOTHING OR DISPLAY A MESSAGE SAYING IT WAS A BAD MOVE?
				controller.setStatusMessage("Bad move");
				canPlay=false;
				break;
			case WIN:
				controller.setStatusMessage("YOU WON!");
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet));
				canPlay=false;
				break;
			case LOSE:
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet));
				controller.setStatusMessage("YOU LOSE...");
				canPlay=false;
				break;
			case TIE:
				controller.setMovesOnBoard(convertPacketToSmallBoard(packet));
				controller.setStatusMessage("IT'S A DRAW.");
				canPlay=false;
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
			converser.sendPacket(packet);
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
			converser.sendPacket(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeMove(int column) throws Exception
	{
		if(canPlay){
	    column+=3;
		int row = getNextEmptyRow(column);
	    if(row != -1)
		{
			try {
				sendMovePacket(column,row);
			} catch (IOException e) {
				//rollback play
				getGameBoard()[row][column] = null;
			}
		}
		}
	}
	public void sendMovePacket(int column,int row) throws IOException {
		byte[] packet = converser.createPacket(PACKET_TYPE.MOVE.getValue(), row, column, -1, -1);
			converser.sendPacket(packet);
			getResponce();
		
	}

}
