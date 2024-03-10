package qqServer.server;

import java.io.IOException;  
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;
import qqServer.Utils.MyObjectInputStream;
import qqServer.Utils.MyObjectOutputStream;
import qqServer.view.ServerFrame;

/**
 * 
 * @author sky
 * @version 1.0
 * 
 * 	������ ���ӷ�����
 * 		1. ���������, ���ӿͻ���socket
 * 		2. �洢�û����� ����
 * 		3. ��֤�û���¼
 * 		4. �û�ע��
 * 		5. �û�������¼����
 * 		6. �����߳�ά��socket, ���뼯��
 */
public class QQServer {

	private ServerSocket server = null;
	private ServerFrame serverFrame = null;    // ���ս������ ��������ʼ��  ����println����,�ɽ��������ӡ�ڽ�����
	
	/*
	 *  ʹ�ü��� HashMap���� ģ�����ݿ�
	 *  		��Ŷ���û�
	 *  		<�û�id, User>
	 */
	private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();	
	
	// ʹ�þ�̬�����  ��ʼ�� validUsers
	static {
		validUsers.put("100", new User("100", "123456"));
		validUsers.put("101", new User("101", "123456"));
		validUsers.put("102", new User("102", "123456"));
		validUsers.put("¬����", new User("¬����", "123456"));
		validUsers.put("�ֳ�", new User("�ֳ�", "123456"));
		validUsers.put("�ν�", new User("�ν�", "123456"));
		validUsers.put("³����", new User("³����", "123456"));
		validUsers.put("����", new User("����", "123456"));
	}
		
	/**	����������socket ����User 
	 * 
	 * @param serverFrame
	 */
	public QQServer(ServerFrame serverFrame) {
		
		// ��ʼ�� �������
		this.serverFrame = serverFrame;
		
		
		// 1. �ڹ��������� �����Socket
		try {
			println("��������9999�˿ڼ���~");
			server = new ServerSocket(9999);
			
			
		// 2. ѭ������, �ɼ�������ͻ���socket
		// 	����ĳ���ͻ��˽������Ӻ�, Ҳ���������, �ȴ���һ������
		while(true) {
			
			// ��û�пͻ�������, ����
			Socket socket = server.accept();   
			
			// 3. �������Ӻ�, ���տͻ��˴�����User����
			MyObjectInputStream ois = 
					new MyObjectInputStream(socket.getInputStream());
			User user = (User)ois.readObject();   
			
			
			// 4.׼�����ظ��ͻ����жϽ��
			MyObjectOutputStream oos = 
					new MyObjectOutputStream(socket.getOutputStream());
			Message message = new Message();
			
			
			// 5.�� ��Ϣ����Ϊ MESSAGE_REGIST_REQUEST �ͻ�������ע��
			if(user.getRegistMessageType() != null && user.getRegistMessageType().equals(MessageType.MESSAGE_REGIST_REQUEST) )  {
				
				if(registUser(user.getUserId(), user)) {   // �˺�δ��ע��, �ҽ����˺ż��뼯��
					
					println("�û� " + user.getUserId() + " ע��ɹ�");
					
					// ��messageType��Ϣ�������� ע��ɹ� 
					message.setMessType(MessageType.MESSAGE_REGIST_SUCCEED);
					oos.writeObject(message);   // ����
					
				} else {
					// �˺��Ѵ���
					println("�û� " + user.getUserId() + " �Ѵ���, ע��ʧ��");
					
					// ��messageType��Ϣ�������� ע��ʧ��
					message.setMessType(qqCommon.MessageType.MESSAGE_REGIST_FAIL);
					oos.writeObject(message);   // ����
				}
				user = null;
				// ע�������Ҫ �ر�, �����۳ɹ����
				socket.close();
				
				
			// �ͻ��� �������촰��
			} else if (user.getState() != null) {
				
				// �����߳� ά��
				ServerConnectThread thread = 
						new ServerConnectThread(socket, user.getUserId(), serverFrame);
				thread.start();
				
				// ���뼯��                                �û���             ״̬             �߳�
				new ServerConnectThreadManage().addThread(user.getUserId(), user.getState(), thread);		
				
				if(user.getState() == "Ⱥ��") {
					println(user.getUserId() + " ����Ⱥ�Ĵ���");
				} else {
					println(user.getUserId() + " ������ " + user.getState() + " �����촰��");
				}
				
			// �ͻ��������¼
			} else {
				// ��֤ userId������  ���ñ����checkUser()���� 
				if( checkUser(user.getUserId(), user.getPassword()) ) {
					// ��֤ͨ��
					//   ��Ϣ��ʽ����Ϊ:    MESSAGE_LOGIN_SUCCEED ��¼�ɹ�
					message.setMessType(qqCommon.MessageType.MESSAGE_LOGIN_SUCCEED);
					oos.writeObject(message);   // ����
					
				
					// ����һ�� �߳�, ά����socket����  
					ServerConnectThread thread = 
							new ServerConnectThread(socket, user.getUserId(), serverFrame);
					thread.start();  // ����
					
					// �����߳� ������뼯���� ���й���
					/*
					 * 	����ServerConnectClientThreadManage �̹߳��������  
					 * 	addThread���� �����߳� �����Ӧ��userId, ״̬Ϊ"����"
					 */
					 new ServerConnectThreadManage().addThread(user.getUserId(), "����", thread);
					
					
				} else {  // ��¼ʧ��
					println("�û� " + user.getUserId() + " ��¼��֤ʧ��" );
					
					// ��messageType��Ϣ�������� ��¼ʧ��
					message.setMessType(qqCommon.MessageType.MESSAGE_LOGIN_FAIL);
					oos.writeObject(message);   // ����
					
					// �ر�
					socket.close();
				}
			}
					
		}
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}  catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// ��������˳�ʱ, ��whileѭ������
			// �ر� ServerSocket����
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**  ��¼��֤����:
	 *   ��֤ �û�id�Ƿ� �����ڼ��ϵķ���
	 * 
	 * @param userId   �û���
	 * @param password ����
	 * @return boolean trueΪ��֤ͨ��
	 */
	private boolean checkUser(String userId, String password) {
		// ͨ��key�� userId ȡ�� user����
		User user = validUsers.get(userId);
		
		// �������� û�д����userId, ��ȡ����userΪ��
		if(user == null) {
			return false;
		}
		// ������������
		if( ! (user.getPassword().equals(password))) {
			return false;
		}
		
		return true;
	}
	
	
	/**  ע�᷽��:
	 * 	1. ��֤�˺��Ƿ��Ѵ���, ���ڷ���fail   
	 *  2. �����ڷ���true ����user������� ConcurrentHashMap
	 * 
	 * @param userId   �û���
	 * @param user     ��Ӧuser
	 * @return boolean trueע��ɹ�, false�û��Ѵ���
	 */
	private boolean registUser(String userId, User user) {
		// ��ȡuser����
		User userVerify = validUsers.get(userId);
		// �˺���ע�� ����ʧ��
		if(userVerify != null) {
			return false;
		} else {
		
			// δ��ע��, ���û�����
			validUsers.put(userId, user);
			return true;   
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



