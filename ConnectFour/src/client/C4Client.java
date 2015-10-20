package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import shared.C4Logic;
import shared.C4Packet;

public class C4Client extends C4Logic {
	
	Socket connection;
	OutputStream send;
	InputStream  receive;
	C4Packet converser;
	
	public C4Client()
	{
		converser = new C4Packet();
	}
	public void handleClick(){
		
	}
	
	
	public boolean startConnection(String serverIp,int serverPort)
	{
		try{
		connection = new Socket(serverIp, serverPort);
		send = connection.getOutputStream();
		receive = connection.getInputStream();
		byte[] b = new byte[3];
		b[0]=10;
		b[1]=20;
		b[2]=30;
		converser.sendPacket(b, send);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	

}
