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
	    this.connection = connection;
		receive = connection.getInputStream();
	    converser = new C4Packet();
	    start();
		
	}
	private void start() throws IOException {
		byte[] packet = converser.receivePacket(receive);
		if(packet[0] == 2)
			playAgain = true;
		else if(packet[0] == 3)
		{
			playAgain = false;
			gameOver = true;
			
		}
	    startGameSession();
	}

	public void startGameSession() throws IOException{
	
		byte[] b = {0,0,0};
		sendPacket(b);
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