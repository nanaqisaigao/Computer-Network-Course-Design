package qqServer.server;

import java.io.IOException; 
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import qqCommon.Message;
import qqCommon.MessageType;
import qqServer.Utils.MyObjectInputStream;
import qqServer.Utils.MyObjectOutputStream;
import qqServer.view.ServerFrame;

/**
 * 
 * @author sky
 * @version 1.0 
 * 
 * ������߳���
 * 		1.������ȡ�����û�����, ����message��ԭ�û�
 * 		2.����˽������, ������Ϣ����ָ���û�
 * 		3.����Ⱥ������, ������Ϣ�������������û�
 * 		4.����˽���ļ�����
 * 		5.����Ⱥ���ļ�����
 * 		6.����(�߳�)�˳�����, ����Ϣ���͸����߳���ʾ�˳�, ��break�˳����߳� �ر�socket
 * 				���� ���촰��(�߳�)�˳� �� �ͻ����˳�
 * 	
 */

public class ServerConnectThread extends Thread{

	private ServerFrame serverFrame = null;    // ����˽������
	private Socket socket;   // �༴�ͻ��� ��socket
	private String userId;   // ���ӵ��ķ���� �Ķ�Ӧ�û�id, ����ʶ��
	
	
	// ������ ��ʼ������
	public ServerConnectThread(Socket socket, String userId, ServerFrame serverFrame) {
		super();
		this.socket = socket;
		this.userId = userId;
		this.serverFrame = serverFrame;
	}
	
	
	// get����
	public Socket getSocket() {
		return socket;
	}
	
	
	// ��ͣ ���ն�ȡ �ͻ��˷��� ������
	@Override
	public void run() {
		// �̴߳���run״̬, ����ѭ�� ��ʽ/���� �ͻ�����Ϣ, ����ͨ��
		while(true) {
			println("����˺� " + userId + " ����ͨ��, ��ȡ����");
		
			try {
				MyObjectInputStream ois = 
						new MyObjectInputStream(socket.getInputStream());
				// �� �ͻ���û�з��Ͷ���, ����д�ڴ�����(��ͣ)
				Message message = (Message) ois.readObject();   
				
				//	�Խ��յ�messageType ���ͽ����ж�,������Ӧ����
				if(message.getMessType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
				// һ. �ӵ��ͻ��� ��ȡ�����û��б� ������
					
					/*
					 *  �̹߳�����ServerConnectClientThreadManage �洢����userId
					 * 	  ������ getOnlineUsers()����  �õ�userId��ɵ��ַ���
					 */
					String onlineUsers = ServerConnectThreadManage.getOnlineUsers();
					
					Message message2 = new Message(); 
					message2.setContent(onlineUsers);     // ���÷�������  �����û�
					message2.setSendTime(message.getSendTime());
					message2.setMessType(MessageType.MESSAGE_RET_ONLINE_FRIEND);    
					
					message2.setSender(message.getSender());    // ���÷����� (Ϊ����ȥ�Ŀͻ���)
					
					println("�ͻ��� " + userId + " ������ȡ�����û��б�....");
					
					MyObjectOutputStream oos = 
							new MyObjectOutputStream(socket.getOutputStream());
					oos.writeObject(message2);    // д��    
					
					   
				} else if (message.getMessType().equals(MessageType.MESSAGE_COMM_MES)) {
				// ��. ��ͨ��Ϣ  �ͻ���˽��
					
					
					// ���� �̹߳������ getThread()���� ��ȡ--������-- ���߳�
					ServerConnectThread thread 
						= ServerConnectThreadManage.getThread( message.getGetter(), message.getSender());
					
					if(thread != null) {   // �̴߳���
						MyObjectOutputStream oos = 
								new MyObjectOutputStream(thread.getSocket().getOutputStream());
						oos.writeObject(message);   // д��
						
						println(message.getSender() + " �� " 
								+ message.getGetter() + " ������Ϣ."	);
					} else {
						// �Է�δ���� ��Ӧ�߳�
						println(message.getSender() + " �� " + message.getGetter() + " ������Ϣʧ��!");
					}
					
					
					
				} else if (message.getMessType().equals(MessageType.MESSAGE_ToAll_MES)) {
				// ��. �ͻ���Ⱥ��
					
					/*
					 *  ��Ҫ���� �̹߳�����ļ��� �õ� �����߳�socket
					 *  ���� ��getThread()���� ȡ�� ���û����̼߳���
					 */
					HashMap<String, HashMap<String, ServerConnectThread>> map = 
							ServerConnectThreadManage.getMap();
					// ��������
					Iterator<String> iterator = map.keySet().iterator();
					String onlineUserId;
					while(iterator.hasNext()) {
						
						onlineUserId = iterator.next().toString();  // ȡ�������û���id ��Ϊiterator
						
						// �����û����� �ͻ��˷��ͷ�
						if(! onlineUserId.equals(message.getSender())) {
							
							message.setGetter(onlineUserId);
							// ȡ��Ⱥ���߳�
							ServerConnectThread thread = map.get(onlineUserId).get("Ⱥ��");
							
							if(thread != null) {
								MyObjectOutputStream oos =     //    ȡ�ø��û��̼߳���   ȡȺ���߳�    ȡio��     
										new MyObjectOutputStream(thread.getSocket().getOutputStream());
								oos.writeObject(message);
								
								println("�û� " + message.getSender() + " ��������Ⱥ����Ϣ, �û�"
										+ onlineUserId + " ���ճɹ�.");
								
							} else {
								println(message.getSender() + " �������� Ⱥ����Ϣ, �û� "
										+ onlineUserId + " ����ʧ��!");
							}
						}
					}
					
					
				} else if (message.getMessType().equals(MessageType.MESSAGE_File_MES)) {
				// ��. �ͻ��� ˽�Ĵ����ļ�  
					
					String getterId = message.getGetter();
					/*	�̹߳������getThread()���� ��ȡ�߳�
					 */
					ServerConnectThread thread =       //      ���շ�    ���շ��������(���ͷ�)
							ServerConnectThreadManage.getThread(getterId, userId);
					
					if(thread != null) {   // �̴߳���
						MyObjectOutputStream oos = 
								new MyObjectOutputStream(thread.getSocket().getOutputStream());
						oos.writeObject(message);
						
						println(message.getSender() + " �� " + message.getGetter() + " �����ļ�...");
					} else {
						// �Է�δ���� ��Ӧ�߳�
						println(message.getSender() + " �� " + message.getGetter() + " �����ļ�ʧ��!");
					}
					
				} else if (message.getMessType().equals(MessageType.MESSAGE_File_MES_TOALL)) {
				// ��. �ͻ���Ⱥ���ļ�	
					
					// ������Ϣ���� Ϊ�ļ����� (�ͻ��˽��ղ��� Ⱥ��/˽��)
					message.setMessType(MessageType.MESSAGE_File_MES);
					/*
					 *  ��Ҫ���� �̹߳�����ļ��� �õ� �����߳�socket
					 *  ���� getThread()���� ȡ��hashmap����
					 */
					HashMap<String, HashMap<String, ServerConnectThread>> map = 
							ServerConnectThreadManage.getMap();
					
					// ��������
					Iterator<String> iterator = map.keySet().iterator();
					String onlineUserId;
					while(iterator.hasNext()) {
						
						// ȡ�������û���id ��Ϊiterator
						onlineUserId = iterator.next().toString();
						
						// �����û����� �ͻ��˷��ͷ�, ����
						if(! onlineUserId.equals(message.getSender())) {
							
							message.setGetter(onlineUserId);
							
							ServerConnectThread thread = map.get(onlineUserId).get("Ⱥ��");
							
							if(thread != null) {
								MyObjectOutputStream oos =     //    ȡ�ø��û��̼߳���   ȡȺ���߳�    ȡio��     
										new MyObjectOutputStream(thread.getSocket().getOutputStream());
								oos.writeObject(message);
							
								// ��ӡ�ļ���
								println("�û� " + message.getSender() + " ��������Ⱥ���ļ�:\n" 
											+ message.getFileName() + " �����û�: " + onlineUserId);
							} else {
								println(message.getSender() + " �������� Ⱥ���ļ�, �û� " 
										+ onlineUserId + " ����ʧ��!");
							}
						}
					}
					
					
				} else if (message.getMessType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
				// ��. �ӵ� �ͻ���׼���˳� ϵͳ, ��message���ظ��ͻ����߳�, �ٹر�socket
					
					/*	�ͻ��� ����userClientService�� �� logout()���������˷����˳���ʾ  ���쳣�˳�
					 * 
					 * 	���� ����˻ᱨ��: java.net.SocketException:Connection reset
					 * 			����λ��: at qqServer.server.ServerConnectClientThread.run(ServerConnectClientThread.java:60)
					 * 		(����˽��յ���Ὣmessage���ظ� ���ͻ����߳�-�������˳�, �ٹرշ���˶�Ӧsocket)
					 */
					
					ServerConnectThread thread = ServerConnectThreadManage.
							getThread(message.getSender(), message.getGetter());
					
					MyObjectOutputStream oos = 
							new MyObjectOutputStream(thread.getSocket().getOutputStream());
					oos.writeObject(message);
					
					// ���ͻ��˶�Ӧ���̴߳Ӽ����� ɾ��
					if(message.getGetter().equals("����")) {
						
						// �ͻ��� �˳�, ���ɾ�� ���û����� �̼߳���
						ServerConnectThreadManage.removeUserId(userId);
						println("�ͻ��� " + message.getSender() + " �˳� !!");
						
					} else if (message.getGetter().equals("Ⱥ��")) {
						
						// 4.0 �ͻ��˽� ״̬����getter   ��ȡ��Ӧ�̲߳��Ӽ���ɾ��
						ServerConnectThreadManage.removeThread(message.getSender(), message.getGetter());
						println("�ͻ��� " + message.getSender() + " ����Ⱥ��..");
					} else {
						
						ServerConnectThreadManage.removeThread(message.getSender(), message.getGetter());
						println("�ͻ��� " + message.getSender() + 
								" ������ " + message.getGetter() + " ������..");
					}
					
					socket.close();   // ���̵߳�socket ��Ϊ ���˳��ͻ���socket, ֱ�ӹر�
					break;   // �˳�whileѭ��, run����������, ���߳̽���!!!
					
				} else {
					
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	// println��� ȷ����������������
    public void println(String s) {
        if (s != null) {
           serverFrame.getTaShow().setText(serverFrame.getTaShow().getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
}




