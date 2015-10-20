package shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class C4Packet {
	

	public void sendPacket(byte[] move, OutputStream send) throws IOException
	{
			send.write(move);
			System.out.println("WORK WORK  :" + move[0] + " " + move[1] + " " + move[2]);
			send.close();
	}
	
	public byte[] receivePacket(InputStream receive) throws IOException
	{
			byte[] bytes =	new byte[3];
			int totalBytes = 0;
			int bytesReceived;
			while (totalBytes < 3)
		    {
		      if ((bytesReceived = receive.read(bytes, totalBytes,
		                        3 - totalBytes)) == -1)
		        throw new SocketException("Connection closed prematurely.");
		      totalBytes += bytesReceived;
		    }
			return bytes;
			
	}

}
