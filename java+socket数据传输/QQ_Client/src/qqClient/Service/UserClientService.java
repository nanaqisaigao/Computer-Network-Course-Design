package qqClient.Service;


import java.io.IOException;   
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.JOptionPane;

import qqClient.Utils.MyObjectInputStream;
import qqClient.Utils.MyObjectOutputStream;
import qqClient.view.ChatFrame;
import qqClient.view.OnlineUserFrame;
import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;

/**
 * 
 * @author sky
 * @version 1.0
 *	
 *	�ͻ��� �û�������
 * 		����: 1. �û���¼��֤
 * 			  2. �û�ע��  
 * 			  3. ��ȡ�����û� 
 * 			  4. ���쳣�˳�   
 * 			  5. �����߳� ���뼯��
 * 
 *  <�ڵ�¼��֤ʱ���ӷ����� ��¼�ɹ������߳�>
 *  <ע��ʱ���ӷ�����, ��ɺ�ر�>
 */

public class UserClientService {

	// ʹ��qqCommon�� ��User����, ���д���
	private User user = new User(); 
	
	private Socket socket;
	
	/*	��֤��¼����
	 *  1. ��userId ��password ���͵������
	 *  2. ���շ���˷���message
	 *  3. ����boolean
	 */
	public boolean checkUser(String userId, String password) {
		
		// ��ʱ����, ���ڷ���
		boolean loop = false;
		
		try {
			// �����ݷ�װ������
			user.setUserId(userId);
			user.setPassward(password);
			 
			// ����Socket ���ӵ������            InetAddress.getLocalHost()
				socket = new Socket(InetAddress.getLocalHost(), 9999);	
			
			// ����user����������
			MyObjectOutputStream oos = 
					new MyObjectOutputStream(socket.getOutputStream());
			oos.writeObject(user);  
		
			// ���շ���� ���͵�message ����, �û���Ϣ��֤
			MyObjectInputStream ois = 
					new MyObjectInputStream(socket.getInputStream());
			Message message = (Message)ois.readObject();
			
			// �Խ��յ���message ��Ϣ���������֤
			if( message.getMessType().equals(qqCommon.MessageType.MESSAGE_LOGIN_SUCCEED) ) {
				
				loop = true;
			} else {     
				// ������ʾ
				JOptionPane.showMessageDialog(null, "�˺Ż��������!");
				
				// ��½ʧ��
				// �ر� ��������socket
				socket.close();
			}
			
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(null, "������δ����");
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "������δ����");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loop;   // ����
	}
	
	
	/*  �ͻ���ע�� ����
	 * 	1. �����˺�����������, ���ж��˺��Ƿ��ѱ�ע��
	 *  2. ���շ���˷��ص� ��Ϣ����
	 */
	public boolean registUser(String userId, String password) {
		// ��ʱ����, ���ڷ���
		boolean loop = false;
		
		try {
			// �����ݷ�װ������
			user.setUserId(userId);
			user.setPassward(password);
			// ����Ϊ����ע��
			user.setRegistMessageType(MessageType.MESSAGE_REGIST_REQUEST);   
			 
			// ����Socket ���ӵ������            InetAddress.getLocalHost()
				socket = new Socket(InetAddress.getLocalHost(), 9999);
			
			// ����user����������
			MyObjectOutputStream oos = 
					new MyObjectOutputStream(socket.getOutputStream());
			oos.writeObject(user);    // ���͸������
			
			// ���շ���� ���͵�message ����, �û���Ϣ��֤
			MyObjectInputStream ois = 
					new MyObjectInputStream(socket.getInputStream());
			
			Message message = (Message)ois.readObject();
		
			
			// �Խ��յ���message ��Ϣ���������֤
			if( message.getMessType().equals(qqCommon.MessageType.MESSAGE_REGIST_SUCCEED) ) {
				
				loop = true;   // ��������ֵ
			} 
			// ���۳ɹ����, �ر�socket
			// �ر� ��������socket
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		// ����
		return loop;
	}	
	

	/**	�����û����� �߳���������
	 * 
	 * @param userId  �û���
	 * @param onlineUserFrame  �����û�����(���������û��б�)
	 */
	public void startThread(String userId, OnlineUserFrame onlineUserFrame) {
		/*
		 *  �����߳�, ��װά��socket  
		 *  ʹ�� ClientServerThread  �߳���
		 */
		ClientConnectThread thread = new ClientConnectThread(socket, onlineUserFrame);
		
		thread.start();   // ����

		/*	���̷߳��� �����̵߳ļ�����
		 *  ͨ��ClientConnentServerThreadManage �̹߳�����
		 *  	addClientThread ��������߳�
		 */
		// ״̬���Ϊ ����
		ClientConnectThreadManage.addThread(userId, "����", thread);
	}
	
	
	
	/**	4.0�޸� 
	 *  ���촰��  �����߳� ����
	 * 	1. ����һ���µ� socket
	 *  2. ����һ�� �µ��߳�
	 *  	��֤ һ�����촰��(����) ��Ӧ һ��socket���߳�
	 *  	ʹ һ���û����� ��ʾ������� ����
	 * 
	 * @param userId   �û���
	 * @param getterId �������(״̬)  "Ⱥ��",�Է��û��� 
	 * @param chatFrame	���촰��
	 */
	public void startThreadChat(String userId, String getterId, ChatFrame chatFrame) {
		
		/* 4.0
		 *  ����User���� 
		 *  	getterId ������� �� ״̬
		 *  	��������� ʶ��ʹ��
		 */
		user.setUserId(userId);
		user.setState(getterId);   // ����״̬Ϊ �������
		 
		// ����Socket ���ӵ������           
		try {
			socket = new Socket(InetAddress.getLocalHost(), 9999);

		
		// ����user����������
		MyObjectOutputStream oos = 
				new MyObjectOutputStream(socket.getOutputStream());
		
		oos.writeObject(user);    // ����
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		/*	�����߳�, ��װά��socket  
		 */
		ClientConnectThread thread = new ClientConnectThread(socket, chatFrame);
		
		thread.start();  // ����

		/* 4.0
		 * ���̷߳��� �����̵߳ļ�����
		 *  	���� �̹߳����� ��Ź��� 
		 */
		// ״̬ Ϊ �������("Ⱥ��" "�Է��û���")
		ClientConnectThreadManage.addThread(userId, getterId, thread);
	}
	
	
	
	
	/**	��ȡ�����û����� 
	 * 	������� �������������û��б�
	 * 
	 * @param senderId �û���
	 */
	public void onlineFriendList(String senderId) {
		
		// ����һ��message����
		// messageType ����ΪMESSAGE_GET_ONLINE_FRIEND �ͻ�������
		Message message = new Message();
		message.setSendTime(new Date().toString());  // ����ʱ��
		message.setMessType(MessageType.MESSAGE_GET_ONLINE_FRIEND);   
		message.setSender(senderId);         // ���÷�����
		
		try {
			// �̹߳����� �Ļ�ȡ�̷߳���, ����userId �� ״̬"����" 
			ClientConnectThread thread = 
					ClientConnectThreadManage.getThread(senderId, "����");

			// ͨ���߳��� ��getScoket���� �õ�socket  ���͸������
			MyObjectOutputStream ois = 
					new MyObjectOutputStream( thread.getSocket().getOutputStream());
			ois.writeObject(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	/**	���쳣�˳�����
	 * 	1. �÷��� �����˷��� �˳���message, ��ʾ��ر�socket �˳��߳� 
	 *  2. ����˷��ظ����߳�, ��ʾ���߳��˳�
	 * 
	 * @param userId  �û���
	 * @param state  ״̬(��λ����Ӧ���߳�)
	 */
	public void logout(String userId, String state) {
		Message message = new Message();
		message.setSender(userId);    // һ��Ҫָ�������� �ͻ���id
		message.setMessType(MessageType.MESSAGE_CLIENT_EXIT);   // ������Ϣ����Ϊ �˳�
		
		// 4.0
		message.setGetter(state);   // �����߶���Ϊ״̬
		
		try {
			
			// �����̹߳������ ������ȡ��Ӧ�߳�
			ClientConnectThread thread = 
					ClientConnectThreadManage.getThread(userId, state);
			
			// �����˷��� �˳�����message
			MyObjectOutputStream oos = 
					new MyObjectOutputStream(thread.getSocket().getOutputStream());
			oos.writeObject(message);
			
			System.out.println(userId + "�˳�ϵͳ!");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}




