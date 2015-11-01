package com.dc.ConnectFourGame.client;

import java.io.IOException;

public class C4App {

	public static void main(String[] args) throws IOException {
		new C4Client().startConnection("localhost", 62366);
	}

}
