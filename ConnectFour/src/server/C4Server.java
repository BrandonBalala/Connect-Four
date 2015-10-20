package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class C4Server {
	
	private ServerSocket servSock;
	private InetAddress serverInfo;
	
	//constructor
	public C4Server() throws IOException{
		establishConnection();
		setUp();
	}
	
	public void setUp() throws IOException{
		//infinite loop to accept game proposals from client
		while(true)
		{
				//port we should listen on?
				servSock = new ServerSocket(50000);
				
				// Get client connection
				Socket clntSock = servSock.accept();
				
				//send client socket to session
				new C4ServerSession(clntSock);

		}
	}// end of setUp
	
	public void establishConnection() throws UnknownHostException{
		serverInfo = InetAddress.getLocalHost();
		System.out.println("SERVER ADDRESS :" + serverInfo.getHostAddress());//TODO DELETE LATER
		
	}
	
	public InetAddress getServerInfo()
	{
		return serverInfo;
	}
}//end of class
