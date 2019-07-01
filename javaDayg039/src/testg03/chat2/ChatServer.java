package testg03.chat2;

//서버 만들기
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.border.Border;

public class ChatServer extends Frame implements ActionListener {
	Button btnExit;
	TextArea ta;
	Vector vChatList;
	ServerSocket ss;
	Socket sockClient;

	public ChatServer() {
		setTitle("채팅 서버");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		vChatList = new Vector();
		btnExit = new Button("서버종료");
		btnExit.addActionListener(this);
		ta = new TextArea();
		add(ta, BorderLayout.CENTER);
		add(btnExit, BorderLayout.SOUTH);
		setBounds(250, 250, 200, 200);
		setVisible(true);

		// 채팅 메소드(chatStart()) 호출
		chatStart();

	}

	public void chatStart() {
		// 소켓 생성
		try {
			ss = new ServerSocket(6005); // 소켓접속자의 역할
			// 소켓들을 받아 줄 수있는 소켓들 생성
			// sockClient = ss.accept(); //하나만 받는것이 아니고 무한으로 받기위해 while 문을 씀
			while (true) {
				sockClient = ss.accept();
				// 접속자의 ip를 가져온다 ~~가 접속했다. ip를 얻음
				ta.append(sockClient.getInetAddress().getHostAddress() + "접속함 \n");

				// Thread를 상속받는 내부 클래스 ChatHandle를 만든다
				ChatHandle threadChat = new ChatHandle();
				vChatList.add(threadChat); // vector형식의 리스트 누가 접속해서 서버쪽으로 오는지

				// 스레드 동작
				threadChat.start(); // chatHandle에 있는 run() 메소드 실행.

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		dispose();
	}

	public static void main(String[] args) {
		new ChatServer();
	}

	class ChatHandle extends Thread {
		BufferedReader br = null; // 입력담당
		PrintWriter pw = null; // 출력 담당

		public ChatHandle() {
			try {
				InputStream is = sockClient.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));

				OutputStream os = sockClient.getOutputStream();
				pw = new PrintWriter(new OutputStreamWriter(os));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void sendAllClient(String msg) {
			int size = vChatList.size();// 리스트의 개수
			try {
				for (int i = 0; i < size; i++) {
					ChatHandle chr = (ChatHandle) vChatList.elementAt(i);
					chr.pw.println(msg);
					chr.pw.flush();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void run() {
			try {
				String name = br.readLine();// 서버에 접속할때 이름 쓰고 접속 버튼을 누르면 이름이 저장되게 하기 위한 문자열
				sendAllClient(name + "님께서 입장");
				while (true) { // 채팅 내용 받기
					String msg = br.readLine();//
					String str = sockClient.getInetAddress().getHostName();
					ta.append(msg + "\n");// 채팅내용을 ta에 추가
					if (msg.equals("@@Exit")) {
						break;
					} else {
						sendAllClient(name + " : " + msg);// 접속자 모두에게 메세지 전달.
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				vChatList.remove(this);
				try {
					br.close();
					pw.close();
					sockClient.close();
				} catch (IOException e) {
				} // finally catch

			} // finally

		}// run
	}
}
