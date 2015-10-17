package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class C4Server {
	
	private ServerSocket servSock;
	
	//constructor
	public C4Server(){
		setUp();
	}
	
	public void setUp(){
		
		//infinite loop to accept game proposals from client
		while(true)
		{
			try {
				//port we should listen on?
				servSock = new ServerSocket(50000);
				
				// Get client connection
				Socket clntSock = servSock.accept();
				
				//send client socket to session
				new C4ServerSession(clntSock);
				 
			} catch (IOException e) {
				System.out.println("Server Socket was not initialized correctly.");
			}
			
		}
	}// end of setUp
}//end of class
