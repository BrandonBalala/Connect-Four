package com.dc.ConnectFourGame.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class C4Server {

	private ServerSocket servSock;
	private InetAddress serverInfo;
	private int port = 62366;

	// constructor
	public C4Server() throws IOException {
		serverInfo = InetAddress.getLocalHost();
		System.out.println(serverInfo.getHostAddress());
		setUp();
	}

	public void setUp() throws IOException {

		// infinite loop to accept game proposals from client
		while (true) {
			// port we should listen on?
			servSock = new ServerSocket(port);

			// Get client connection
			Socket clntSock = servSock.accept();

			// send client socket to session
			new C4ServerSession(clntSock);

		}

	}// end of setUp

	public InetAddress getServerInfo() {
		return serverInfo;
	}

}// end of class
