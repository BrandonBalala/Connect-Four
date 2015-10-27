package com.dc.ConnectFourGame.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.dc.ConnectFourGame.controllers.BoardController;
import com.dc.ConnectFourGame.shared.C4Logic;
import com.dc.ConnectFourGame.shared.C4Packet;

public class C4Client extends C4Logic {
	
	private Socket connection;
	private OutputStream send;
	private InputStream  receive;
	private C4Packet converser;
	private String address;
	
	private int port = 50000;
	private BoardController controller;
	
	public C4Client(){
		converser = new C4Packet();
	}
	
	public void readIp() throws IOException{
		controller = new BoardController();
		address = controller.getConnectingIP();
		startConnection(address, port);
	}
	
	public boolean startConnection(String serverIp,int serverPort) throws IOException
	{
		try{
		connection = new Socket(serverIp, serverPort);
		receive = connection.getInputStream();
		byte[] b = converser.receivePacket(receive);
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

