package client;

import java.io.IOException;

public class C4App {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		C4Client client = new C4Client();
		client.startConnection("10.172.12.5", 62366);
	}

}
