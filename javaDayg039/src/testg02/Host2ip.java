package testg02;

import java.io.IOException;
import java.net.InetAddress;

import org.omg.CORBA.portable.UnknownException;

public class Host2ip {
	public static void main(String[] args) throws IOException {
		String hostname = "www.cwd.go.kr";

		try {
			InetAddress address = InetAddress.getByName(hostname);
			System.out.println("ip �ּ� : " + address.getHostAddress());
		} catch (UnknownException e) {
			// TODO Auto-generated catch block
			System.out.println(hostname + "�� ip�ּҸ� ã�� �� �����ϴ�.");
		}

	}
}
