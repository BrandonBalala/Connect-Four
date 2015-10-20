package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import shared.C4Logic;

public class C4ServerSession extends C4Logic {
	
	private boolean playAgain;
	private boolean gameOver;
	
	public C4ServerSession(Socket clntSocket){
		
		playAgain = true;
		gameOver = false;
		
		startSession(clntSocket);
	}
	
	public void startSession(Socket clntSocket){
		
		byte[] byteBuffer = new byte[32];
		int recvMsgSize;
		
		try {
			
		InputStream in = clntSocket.getInputStream();
		OutputStream out = clntSocket.getOutputStream();
		
		while ((recvMsgSize = in.read(byteBuffer)) != -1)
		{
			
			//a session can be 0 or more games. check if the user wants to play
		}
		
		while(playAgain)
		{
			while(!gameOver)
			{
				//AI logic
			}
	
		}
		
		clntSocket.close();
		
	} catch (IOException e) {
		System.out.println("Streams couldnt get set up from client socket.");
		}// end of catch
	}
}