package com.dc.ConnectFourGame.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class C4Packet {

	private OutputStream send;
	private InputStream receive;
	private Socket socket;
	
	//WHAT OUR PACKETS SHOULD LOOK LIKE
	//byte[] b = { THE TYPE OF PACKAGE, row client, col client, row server, col server };
	//ROW AND COL CAN BE SET TO -1 IF THEY  ARE NOT NEEDED
	
	public C4Packet(Socket socket) throws IOException
	{
		this.socket = socket;
		receive = socket.getInputStream();
		send = socket.getOutputStream();
	}
	/**
	 * Creates a packet
	 * @param type
	 * @param row
	 * @param col
	 * @return byte[]	the packet
	 * @throws IOException
	 */
	public byte[] createPacket(int type, int row, int column) throws IOException {
		byte[] packet = new byte[3];
		packet[0] = (byte) type;
		packet[1] = (byte) row;
		packet[2] = (byte) column;

		return packet;
	}

	public void sendPacket(byte[] packet) throws IOException {
		send.write(packet);
		System.out.println("SENT\nSender: " + new Exception().getStackTrace()[1].getClassName()
						+ "\nTYPE:" + packet[0] 
						+ "\nRow: " + packet[1] 
						+ "\nColumn: " + packet[2])
						;
		send.flush();
	}

	public byte[] receivePacket() throws IOException {
		
		byte[] packet = new byte[3];
		int totalBytes = 0;
		int bytesReceived;

		while (totalBytes < 3) {
			if ((bytesReceived = receive.read(packet, totalBytes, 3 - totalBytes)) == -1)
				throw new SocketException("Connection closed prematurely.");
			totalBytes += bytesReceived;
		}
		System.out.println("RECEIVED\nSender: " + new Exception().getStackTrace()[1].getClassName()
						+ "\nTYPE:" + packet[0] 
						+ "\nRow: " + packet[1] 
						+ "\nColumn: " + packet[2]);
		return packet;

	}
	
	public void connectionClose() throws IOException
	{
		socket.close();
	}
	
	public boolean isConnected() throws IOException
	{
		return socket.isConnected();
	}

}
