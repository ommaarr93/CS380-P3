// Omar Rodriguez
// CS 380
// Professor Nima Davarpanah

import java.io.*;
import java.net.*;
import java.util.*;

public class Ipv4Client
{
	public static void main(String args[]) throws IOException
	{
		Socket socket = new Socket("codebank.xyz", 38003);
		OutputStream out = socket.getOutputStream();
    InputStream is = socket.getInputStream();
    InputStreamReader isr = new InputStreamReader(is,"UTF-8");
		BufferedReader br = new BufferedReader(isr);

		for(int i = 0; i < 12; i++) {
      boolean good = true;

			if(good == true) {
				int sizeOfData = (int)Math.pow(2, i + 1);
				byte[] packet = new byte[sizeOfData + 20];

				System.out.println("data length: " + (sizeOfData));

				packet[0] = 0b01000101;
				packet[1] = 0x0;

				int len = sizeOfData + 20;

				packet[2] = (byte)(len >>> 8);
				packet[3] = (byte)(len);
				packet[4] = 0;
				packet[5] = 0;
				packet[6] = (byte)0b01000000;
				packet[7] = 0;
				packet[8] = (byte)50;
				packet[9] = (byte)6;
				packet[10] = 0;
				packet[11] = 0;
				packet[12] = 0b0;
				packet[13] = 0b0;
				packet[14] = 0b0;
				packet[15] = 0b0;
				packet[16] = (byte)0x34;
				packet[17] = (byte)0x25;
				packet[18] = (byte)0x58;
				packet[19] = (byte)0x9A;
				short sum = checksum(packet);
				packet[10] = (byte)(sum >>> 8);
				packet[11] =(byte)(sum);

				out.write(packet);

				String serverMes = br.readLine();

				if(serverMes.equals("good")) {
					System.out.println("good");
				} else {
          good = false;
					break;
        }
			}
		}
	}

	public static short checksum(byte[] b) {
		long sum = 0;
		int i = 0;
		int length = b.length;
		while(length > 1) {
			sum += ((b[i] << 8) & 0xFF00 | ((b[i + 1]) & 0xFF));

			if((sum & 0xFFFF0000) > 0) {
					sum = sum & 0xFFFF;
					sum++;
			}
			i += 2;
			length -= 2;
		}

		if(length > 0) {
			sum += (b[i] << 8 & 0xFF00);
			if((sum & 0xFFFF0000) > 0) {
				sum = sum & 0xFFFF;
				sum++;
			}
		}
		return (short)(~sum & 0xFFFF);
	}
}
