package com.dc.ConnectFourGame.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;

public class C4Client extends C4Logic {
	
	Socket connection;
	OutputStream send;
	InputStream  receive;
	C4Packet converser;
	
	public C4Client() throws IOException
	{
		converser = new C4Packet();
	}
	
	public void handleClick(){
		
	}
	
	
	public boolean startConnection(String serverIp,int serverPort) throws IOException
	{
		try{
		connection = new Socket(serverIp, serverPort);
		receive = connection.getInputStream();
		byte[] b =converser.receivePacket(receive);
		System.out.println(b[0]);
		System.out.println(b[1]);
		System.out.println(b[2]);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public void sendPacket(byte[] packet) throws IOException
	{
		send = connection.getOutputStream();
		converser.sendPacket(packet, send);
	}

}
