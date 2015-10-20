package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import shared.C4Logic;
import shared.C4Packet;

public class C4ServerSession extends C4Logic {
	
	private boolean playAgain;
	private boolean gameOver;
	OutputStream send;
	InputStream  receive;
	C4Packet converser;
	
	public C4ServerSession(Socket clntSocket) throws IOException{
		
		playAgain = true;
		gameOver = false;
		
		startSession(clntSocket);
	}
	
	public void startSession(Socket clntSocket) throws IOException{
		
		System.out.println(converser.receivePacket(clntSocket.getInputStream()));
		
		/*		
		
		while (false)
		{
			
			//a session can be 0 or more games. check if the user wants to play
		}*/
		
		while(playAgain)
		{
			while(!gameOver)
			{
				//AI logic
			}
	
		}
	}
}