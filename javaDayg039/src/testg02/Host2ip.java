package testg02;

import java.io.IOException;
import java.net.InetAddress;

import org.omg.CORBA.portable.UnknownException;

public class Host2ip {
	public static void main(String[] args) throws IOException {
		String hostname = "www.cwd.go.kr";

		try {
			InetAddress address = InetAddress.getByName(hostname);
			System.out.println("ip 주소 : " + address.getHostAddress());
		} catch (UnknownException e) {
			// TODO Auto-generated catch block
			System.out.println(hostname + "의 ip주소를 찾을 수 없습니다.");
		}

	}
}
