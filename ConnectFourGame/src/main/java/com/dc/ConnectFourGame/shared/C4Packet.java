package com.dc.ConnectFourGame.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class C4Packet {

	private OutputStream send;
	private InputStream receive;
	
	//WHAT OUR PACKETS SHOULD LOOK LIKE
	//byte[] b = { THE TYPE OF PACKAGE, row client, col client, row server, col server };
	//ROW AND COL CAN BE SET TO -1 IF THEY  ARE NOT NEEDED
	
	/**
	 * Creates a packet
	 * @param type
	 * @param row
	 * @param col
	 * @return byte[]	the packet
	 * @throws IOException
	 */
	public byte[] createPacket(int type, int rowClient, int colClient, int rowServer, int colServer) throws IOException {
		byte[] packet = new byte[5];
		packet[0] = (byte) type;
		packet[1] = (byte) rowClient;
		packet[2] = (byte) colServer;
		packet[3] = (byte) rowServer;
		packet[4] = (byte) colServer;

		return packet;
	}

	public void sendPacket(byte[] packet, Socket socket) throws IOException {

		send = socket.getOutputStream();

		send.write(packet);
		System.out.println("SENT\nTYPE: " + packet[0] 
						+ "\nROW CLIENT: " + packet[1] 
						+ "\nCOL CLIENT: " + packet[2]
						+ "\nROW SERVER: " + packet[3]
						+ "\nCOL SERVER: " + packet[4]);
		send.close();
	}

	public byte[] receivePacket(Socket socket) throws IOException {

		receive = socket.getInputStream();

		byte[] packet = new byte[5];
		int totalBytes = 0;
		int bytesReceived;

		while (totalBytes < 5) {
			if ((bytesReceived = receive.read(packet, totalBytes, 5 - totalBytes)) == -1)
				throw new SocketException("Connection closed prematurely.");
			totalBytes += bytesReceived;
		}
		System.out.println("RECEIVED\nTYPE: " + packet[0] 
				+ "\nROW CLIENT: " + packet[1] 
				+ "\nCOL CLIENT: " + packet[2]
				+ "\nROW SERVER: " + packet[3]
				+ "\nCOL SERVER: " + packet[4]);
		return packet;

	}

}
