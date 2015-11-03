package com.dc.ConnectFourGame.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class deals with everything packet related. It is able to create packets
 * given you have the provided fields It is also able to send and receive
 * packets based on the socket provided.
 * 
 * @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public class C4Packet {

	private OutputStream send;
	private InputStream receive;
	private Socket socket;
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	/**
	 * Constructor
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public C4Packet(Socket socket) throws IOException {
		this.socket = socket;
		receive = socket.getInputStream();
		send = socket.getOutputStream();
	}

	/**
	 * Creates a packet which is in the form of a byte array
	 * 
	 * @param type
	 * @param rowClient
	 * @param colClient
	 * @param rowServer
	 * @param colServer
	 * @return
	 * @throws IOException
	 */
	public byte[] createPacket(int type, int rowClient, int colClient, int rowServer, int colServer)
			throws IOException {
		byte[] packet = new byte[5];
		packet[0] = (byte) type;
		packet[1] = (byte) rowClient;
		packet[2] = (byte) colClient;
		packet[3] = (byte) rowServer;
		packet[4] = (byte) colServer;

		return packet;
	}

	/**
	 * Attempts to sends the packet to its destination
	 * @param packet
	 * @throws IOException
	 */
	public void sendPacket(byte[] packet) throws IOException {
		send.write(packet);
		log.info("\nSENT\nTYPE: " + packet[0] + "\nROW CLIENT: " + packet[1] + "\nCOL CLIENT: " + packet[2]
				+ "\nROW SERVER: " + packet[3] + "\nCOL SERVER: " + packet[4]);
		send.flush();
	}

	/**
	 * Attempts to receive a packet from its target
	 * @return packet
	 * @throws IOException
	 */
	public byte[] receivePacket() throws IOException {

		byte[] packet = new byte[5];
		int totalBytes = 0;
		int bytesReceived;

		while (totalBytes < 5) {
			if ((bytesReceived = receive.read(packet, totalBytes, 5 - totalBytes)) == -1)
				throw new SocketException("Connection closed prematurely.");
			totalBytes += bytesReceived;
		}
		log.info("\nRECEIVED\nTYPE: " + packet[0] + "\nROW CLIENT: " + packet[1] + "\nCOL CLIENT: " + packet[2]
				+ "\nROW SERVER: " + packet[3] + "\nCOL SERVER: " + packet[4]);
		
		return packet;
	}

	/**
	 * Closes the socket
	 * @throws IOException
	 */
	public void connectionClose() throws IOException {
		socket.close();
	}
}
