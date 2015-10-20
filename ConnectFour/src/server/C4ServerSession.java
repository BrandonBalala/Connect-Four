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
	Socket connection;
	
	public C4ServerSession(Socket connection) throws IOException{
		
		playAgain = true;
		gameOver = false;
		
		startSession(connection);
	}
	
	public void startSession(Socket connection) throws IOException{
		
		System.out.println(converser.receivePacket(connection.getInputStream()));
		
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
		
		connection.close();
	}
	
	public void sendPacket(byte[] packet) throws IOException
	{
		send = connection.getOutputStream();
		converser.sendPacket(packet, send);
	}
}