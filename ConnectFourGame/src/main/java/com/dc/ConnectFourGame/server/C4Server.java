package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that creates a connection between the client and
 * server applications and starts a connect four game session.
 * 
 * @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public class C4Server {

	private ServerSocket servSock;
	private InetAddress serverInfo;
	private int port = 50000;

	/**
	 * C4Server Constructor. 
	 * Prints out the server's ip and calls the method to set up it's connection.
	 * 
	 * @throws IOException
	 * 			if the localhost is unreachable
	 */
	public C4Server() throws IOException {
		serverInfo = InetAddress.getLocalHost();
		
		// Displays the servers IP address
		System.out.println(serverInfo.getHostAddress());
		
		setUp();
	}

	/**
	 * Sets up the connection for the server socket, at the specific port.
	 * After the server socket is created, accepts the client connections 
	 * coming to it and starts a server session with it.
	 * 
	 * @throws IOException
	 * 				if theres any problem with the sockets
	 */
	public void setUp() throws IOException {

		//create server socket
		servSock = new ServerSocket(port);
		
		// infinite loop to accept game proposals from client
		for(;;){
			// Get client connection
			Socket clntSock = servSock.accept();;
			// send client socket to session
				new C4ServerSession(clntSock);	
		}

	}
}// end of class
