package testg03.chat;

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
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends Frame implements ActionListener, Runnable {
	Button btn_exit;// �����ư
	Button btn_send;// ���۹�ư
	Button btn_connect;// ������ ���� ��ư

	TextArea txt_list;// �j�� ����
	TextField txt_server_ip;// ���� ������ �Է� �ʵ�
	TextField txt_name; // ������ �̸�
	TextField txt_input; // ä�� �Է�â

	Socket client; // client ����
	BufferedReader br;// �Է� ����
	PrintWriter pw;// ���
	String server_ip;// ���������� �ּ�
	final int port = 5005;//
	CardLayout cl;// ī�� ���̾ƿ�

	public ChatClient() {
		setTitle("ä�� Ŭ���̾�Ʈ");
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
		connect.setLayout(new BorderLayout());// connect�г� ���̾ƿ� ����
		btn_connect = new Button("���� ����");
		btn_connect.addActionListener(this);
		txt_server_ip = new TextField("192.168.0.90", 15); // �ڽ��� ip , ����
		txt_name = new TextField("ȫ�浿", 15);// ������ �̸��� �Է�
		Panel connect_sub = new Panel();
		connect_sub.add(new Label("����������(IP) : "));
		connect_sub.add(txt_server_ip);
		connect_sub.add(new Label("��ȭ�� : "));
		connect_sub.add(txt_name);

		// ä�� ȭ�� ����
		Panel chat = new Panel();
		chat.setLayout(new BorderLayout());
		Label lblChat = new Label("ä�� ����ȭ��", Label.CENTER);
		connect.add(lblChat, BorderLayout.NORTH);
		connect.add(connect_sub, BorderLayout.CENTER);
		connect.add(btn_connect, BorderLayout.SOUTH);

		// ä��â ȭ�� ����
		txt_list = new TextArea();// ä�� ���� �����ֱ�
		txt_input = new TextField("", 25);// ä�� �Է�
		btn_exit = new Button("����");// �����ư
		btn_send = new Button("����");// ���۹�ư
		btn_exit.addActionListener(this);// ������ű� ����
		btn_send.addActionListener(this);// ���ۼ��ű� ����
		txt_input.addActionListener(this);// ä�� �Է¼��ű� ����

		Panel chat_sub = new Panel();// ä��â sub�г�
		chat_sub.add(txt_input);
		chat_sub.add(btn_send);
		chat_sub.add(btn_exit);

		Label lblChatTitle = new Label("ä�� ���α׷� v 1.1", Label.CENTER);

		chat.add(lblChatTitle, BorderLayout.NORTH);
		chat.add(txt_list, BorderLayout.CENTER);
		chat.add(chat_sub, BorderLayout.SOUTH);

		// �����ӿ� �߰�
		add(connect, "����â");
		add(chat, "ä��â");
		cl.show(this, "����â"); // card layout �ڽ��� ������� �ٸ��� ������ �� ����â, ä��â�� ��ü
		setBounds(250, 250, 300, 300);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println("����");
		Object obj = e.getSource();
		if (obj == btn_connect) {
			server_ip = txt_server_ip.getText();
			Thread th = new Thread(this);
			th.start();
			cl.show(this, "ä��â");// ī�巹�̾ƿ��� ȭ�� ��ȯ(����â -> ä��â)
		}

	}

	@Override
	public void run() {
		System.out.println("����2"); // th.start()�޼ҵ带 �޴´�.
	}

	public static void main(String[] args) {
		new ChatClient();
	}
}