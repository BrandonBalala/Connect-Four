package com.dc.ConnectFourGame.server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dc.ConnectFourGame.controllers.IPController;

public class C4Server {
	
	private ServerSocket servSock;
	private InetAddress serverInfo;
	private boolean connectionEstablished = false; 
	private IPController IpController;
	
	//constructor
	public C4Server() throws IOException{
		IpController = new IPController();
		printIP();
		establishConnection();
	}
	
	public void setUp() throws IOException{
		
		if(connectionEstablished)
		{
			//infinite loop to accept game proposals from client
			while(true)
			{
				//port we should listen on?
				servSock = new ServerSocket(62366);
				
				// Get client connection
				Socket clntSock = servSock.accept();
				
				//send client socket to session
				new C4ServerSession(clntSock);

			}
		}
	}// end of setUp
	
	public void printIP() throws UnknownHostException{
		serverInfo = InetAddress.getLocalHost();
		String address = serverInfo.getHostAddress();
		IpController.setServerIp(address);
	}
	
	public void establishConnection() throws IOException{
		if(IpController.getServerIp().equals(IpController.getConnectingIP()))
		{
			IpController.setErrorLabel("");
			connectionEstablished = true;
			setUp();
		}
		else
			IpController.setErrorLabel("Wrong IP!");
	}
	

	public InetAddress getServerInfo()
	{
		return serverInfo;
	}
}//end of class
