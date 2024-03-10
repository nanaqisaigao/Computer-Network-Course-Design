package qqClient.Service;

import java.io.IOException;  
import java.util.Date;

import qqClient.Utils.MyObjectOutputStream;
import qqClient.view.ChatFrame;
import qqCommon.Message;
import qqCommon.MessageType;

/**
 * 
 * @author sky
 * @version 1.0
 * 
 *  �ͻ��� ������
 *  	ʵ���û� ˽�� / Ⱥ��
 */

public class MessageClientService {
	
	private ChatFrame chatFrame;   // ���촰���� println���� 
	
	// ��������ʼ��
	public MessageClientService(ChatFrame chatFrame) {
		super();
		this.chatFrame = chatFrame;
	}


	/** ˽�ķ���
	 * 
	 *  �÷��������˷���message����, �з���˷��͸�ָ���û�
	 * 
	 * @param sendId   ������Id
	 * @param getterId ������Id
	 * @param content  ��������
	 */
	public void sendMessageToOne(String senderId, String getterId, String content) {
		Message message = new Message();
		
		message.setMessType(MessageType.MESSAGE_COMM_MES);    // ������Ϣ���� ��ͨ��Ϣ��
		message.setSender(senderId);
		message.setGetter(getterId);
		message.setContent(content);
		message.setSendTime(new Date().toString());    // ���÷���ʱ��, ����util���ķ���
		
		println(senderId + "(��):\t\t   " + new Date().toString());
		println(content + "\n");
		
		
		// �����̹߳����� ���� ���뷢����id,�������(״̬)  ��ȡ�߳�
		ClientConnectThread thread =
				ClientConnectThreadManage.getThread(senderId, getterId);
		// ���͸��ͻ���
		try {
			MyObjectOutputStream oos = 
					new MyObjectOutputStream(thread.getSocket().getOutputStream());
			oos.writeObject(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**  Ⱥ�ķ���
	 *  
	 *  ��˽�Ĳ�ͬ: û�н����� ��Ϣ���Ͳ�ͬ
	 * 
	 * @param senderId  ������
	 * @param content   ��������
	 */
	public void sendMessageToAll(String senderId, String content) {
		Message message = new Message();
		
		message.setMessType(MessageType.MESSAGE_ToAll_MES);    // ������Ϣ���� Ⱥ��
		message.setSender(senderId);
		message.setContent(content);
		message.setSendTime(new Date().toString());    // ���÷���ʱ��
		
		println(senderId + "(��):\t\t   " + new Date().toString());
		println(content + "\n");
		
		// �����̹߳����� ���� ���뷢����id  ��ȡ�߳�
		ClientConnectThread thread =
				ClientConnectThreadManage.getThread(senderId, "Ⱥ��");
		// ���͸��ͻ���
		try {
			MyObjectOutputStream oos = 
					new MyObjectOutputStream(thread.getSocket().getOutputStream());
			oos.writeObject(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// ����������� chatFrame�������
    public void println(String s) {
        if (s != null) {
           chatFrame.getTxt_Chat().setText(chatFrame.getTxt_Chat().getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
}




