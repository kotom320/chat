package testg03.chat2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame implements ActionListener, Runnable {
	Button btn_exit;// 종료버튼
	Button btn_send;// 전송버튼
	Button btn_connect;// 서버에 접속 버튼

	TextArea txt_list;// 챝이 내용
	TextField txt_server_ip;// 서버 아이피 입력 필드
	TextField txt_name; // 접속자 이름
	TextField txt_input; // 채팅 입력창

	Socket client; // client 소켓
	BufferedReader br;// 입력 버퍼
	PrintWriter pw;// 출력
	String server_ip;// 서버아이피 주소
	final int port = 6005;//
	CardLayout cl;// 카드 레이아웃

	public ChatClient() {
		setTitle("채팅 클라이언트");
		// closing
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		cl = new CardLayout();
		setLayout(cl);
		Panel connect = new Panel();
		connect.setBackground(Color.LIGHT_GRAY);
		connect.setLayout(new BorderLayout());// connect패널 레이아웃 구성
		btn_connect = new Button("서버 접속");
		btn_connect.addActionListener(this);
		txt_server_ip = new TextField("192.168.0.13", 15); // 자신의 ip , 길이
		txt_name = new TextField("홍길동", 15);// 접속자 이름을 입력
		Panel connect_sub = new Panel();
		connect_sub.add(new Label("서버아이피(IP) : "));
		connect_sub.add(txt_server_ip);
		connect_sub.add(new Label("대화명 : "));
		connect_sub.add(txt_name);

		// 채팅 화면 구성
		Panel chat = new Panel();
		chat.setLayout(new BorderLayout());
		Label lblChat = new Label("채팅 접속화면", Label.CENTER);
		connect.add(lblChat, BorderLayout.NORTH);
		connect.add(connect_sub, BorderLayout.CENTER);
		connect.add(btn_connect, BorderLayout.SOUTH);

		// 채팅창 화면 구성
		txt_list = new TextArea();// 채팅 내용 보여주기
		
		txt_input = new TextField("", 25);// 채팅 입력
		btn_exit = new Button("종료");// 종료버튼
		btn_send = new Button("전송");// 전송버튼
		btn_exit.addActionListener(this);// 종료수신기 부착
		btn_send.addActionListener(this);// 전송수신기 부착
		txt_input.addActionListener(this);// 채팅 입력수신기 부착

		Panel chat_sub = new Panel();// 채팅창 sub패널
		chat_sub.add(txt_input);
		chat_sub.add(btn_send);
		chat_sub.add(btn_exit);

		Label lblChatTitle = new Label("채팅 프로그램 v 1.1", Label.CENTER);

		chat.add(lblChatTitle, BorderLayout.NORTH);
		chat.add(txt_list, BorderLayout.CENTER);
		chat.add(chat_sub, BorderLayout.SOUTH);

		// 프레임에 추가
		add(connect, "접속창");
		add(chat, "채팅창");
		cl.show(this, "접속창"); // card layout 자신은 사라지고 다른게 나오게 됨 접속창, 채팅창을 교체
		setBounds(250, 250, 300, 300);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println("수신");
		Object obj = e.getSource();
		try {
			if (obj == btn_connect) {
				server_ip = txt_server_ip.getText();
				Thread th = new Thread(this);
				th.start();
				cl.show(this, "채팅창");// 카드레이아웃의 화면 전환(접속창 -> 채팅창)
			}
			else if(obj == btn_exit) {
				System.exit(0); //프로그램 종료
			}
			else if (obj == btn_send || obj == txt_input) { //send버튼을 누르거나 엔터를 쳤을때
			String msg = txt_input.getText();
			pw.println(msg);
			pw.flush();
			txt_input.setText(""); // 내용 지우기
			txt_input.requestFocus(); // 커서를 이곳에 두기
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}

	}

	@Override
	public void run() {
		// System.out.println("수신2"); // th.start()메소드를 받는다.
		// 소켓 만들기
		// client = new Socket("192.168.0.90", 5005);
		try {
			client = new Socket(server_ip, port);
			InputStream is = client.getInputStream();
			OutputStream os = client.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new OutputStreamWriter(os));
			String str = txt_name.getText();// 대화명 얻기
			pw.println(str); // 대화명 전송
			pw.flush();// 내용 완전 비우기
			txt_input.requestFocus();
			while(true) {
				str= br.readLine();
				txt_list.append(str+"\n"); //대화명에 줄바꿈 추가
				
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ChatClient();
	}
}
