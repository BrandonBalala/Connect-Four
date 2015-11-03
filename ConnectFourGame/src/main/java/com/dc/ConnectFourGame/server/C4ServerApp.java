package com.dc.ConnectFourGame.server;

import java.io.IOException;

/**
 * Basic class to start the server application. 
 * Must be run on server machine.
 * Only calls the C4Server class.
 * 
 * @author Irina Patrocinio-Frazao, Ofer Nitka-Nakash, Brandon Yvan Balala
 */
public class C4ServerApp {

	public static void main(String args[]) throws IOException {
		//calls the C4Server constructor
		new C4Server();
	}
}
