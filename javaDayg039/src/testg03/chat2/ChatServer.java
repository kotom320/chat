package testg03.chat2;

//���� �����
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
		setTitle("ä�� ����");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		vChatList = new Vector();
		btnExit = new Button("��������");
		btnExit.addActionListener(this);
		ta = new TextArea();
		add(ta, BorderLayout.CENTER);
		add(btnExit, BorderLayout.SOUTH);
		setBounds(250, 250, 200, 200);
		setVisible(true);

		// ä�� �޼ҵ�(chatStart()) ȣ��
		chatStart();

	}

	public void chatStart() {
		// ���� ����
		try {
			ss = new ServerSocket(6005); // ������������ ����
			// ���ϵ��� �޾� �� ���ִ� ���ϵ� ����
			// sockClient = ss.accept(); //�ϳ��� �޴°��� �ƴϰ� �������� �ޱ����� while ���� ��
			while (true) {
				sockClient = ss.accept();
				// �������� ip�� �����´� ~~�� �����ߴ�. ip�� ����
				ta.append(sockClient.getInetAddress().getHostAddress() + "������ \n");

				// Thread�� ��ӹ޴� ���� Ŭ���� ChatHandle�� �����
				ChatHandle threadChat = new ChatHandle();
				vChatList.add(threadChat); // vector������ ����Ʈ ���� �����ؼ� ���������� ������

				// ������ ����
				threadChat.start(); // chatHandle�� �ִ� run() �޼ҵ� ����.

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
		BufferedReader br = null; // �Է´��
		PrintWriter pw = null; // ��� ���

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
			int size = vChatList.size();// ����Ʈ�� ����
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
				String name = br.readLine();// ������ �����Ҷ� �̸� ���� ���� ��ư�� ������ �̸��� ����ǰ� �ϱ� ���� ���ڿ�
				sendAllClient(name + "�Բ��� ����");
				while (true) { // ä�� ���� �ޱ�
					String msg = br.readLine();//
					String str = sockClient.getInetAddress().getHostName();
					ta.append(msg + "\n");// ä�ó����� ta�� �߰�
					if (msg.equals("@@Exit")) {
						break;
					} else {
						sendAllClient(name + " : " + msg);// ������ ��ο��� �޼��� ����.
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
