package qqClient.Service;

import java.io.FileNotFoundException;  
import java.io.FileOutputStream; 
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import qqClient.Utils.MyObjectInputStream;
import qqClient.view.ChatFrame;
import qqClient.view.OnlineUserFrame;
import qqCommon.Message;
import qqCommon.MessageType;

/**
 * 
 * @author sky
 * @version 1.0
 *	
 *	�ͻ��� �߳���
 *		1. ���������û��б�
 *		2. ����˽������
 *  	3. ����Ⱥ������
 *  	4. �����ļ�
 *  	5. ���ձ��߳��˳����� -- �������߳�!!
 */
public class ClientConnectThread extends Thread{

	// �����߳���Ҫsocket
	private Socket socket;
	
	private ChatFrame chatFrame;
	private OnlineUserFrame onlineUserFrame = null;
	
	
	// ����������Socket
	public ClientConnectThread(Socket socket, ChatFrame chatFrame) {
		super();
		this.socket = socket;
		this.chatFrame = chatFrame;
	}

	// ����������  �����û���� ����
	public ClientConnectThread(Socket socket, OnlineUserFrame onlineUserFrame) {
		super();
		this.socket = socket;
		this.onlineUserFrame = onlineUserFrame;
	}
	
	// ͨ���ж�message�Ĳ�ͬ ���в�ͬ����
	@Override
	public void run() {
	
		// ���� �ͻ�����Ҫ ���������� ����ͨ��, ʹ��whileѭ��
		while(true) {
			try {

				MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
				// �������û�з���Messsage ����, ���������(��ͣ)
				Message message = (Message)ois.readObject();
				
				
				// �ж�messageType���ͽ��в�ͬ����
				if(message.getMessType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
				// ����һ. ����� ���������û��б�
					
					/* 4.0 
					 *   ���շ���˴����� �����û� �ַ���
					 *   �� "\n  " �ָ�, ֱ���������
					 */
					onlineUserPrintln("��ȡʱ��\n" + message.getSendTime() 
						+ "\n======= �����û� =======\n  Ⱥ��\n" + message.getContent() 
							+ "\n�����·����������û��� ��������\n<ע��: ��β��Ҫ�пո�!!>");
					
				}  else if (message.getMessType().equals(MessageType.MESSAGE_COMM_MES)) {
				// ���Ͷ�. �ͻ��� ����˽�� ����(�ͻ���1-->�����-->���ͻ���)    ��ͨ��Ϣ��
					
					// ֱ�Ӵ�ӡ���
					println(message.getSender() + ":\t\t\t" + message.getSendTime());
					println(message.getContent() + "\n");
					
				}  else if (message.getMessType().equals(MessageType.MESSAGE_ToAll_MES)) {
				// ������. �ͻ��� ����Ⱥ������   
					
					println(message.getSender() + ":\t\t\t" + message.getSendTime());
					println(message.getContent() + "\n");
					
				} else if (message.getMessType().equals(MessageType.MESSAGE_File_MES)) {
				// ������. �ͻ����ļ�����   �����ļ�(�ͻ���1-->�����-->���ͻ���)
					 
					println(message.getSender() + "\t�����ļ�:\t\t" + message.getSendTime());
					println("   " + message.getFileName() + "\n");			
					
					// ������ʾ���� �����ļ� ��·��
					// ���ñ��ඨ�巽��       SaveFileAddress(ʹ����, ������,���͵��ļ���)
					String srcPsth = saveFileAddress(message.getGetter(), message.getSender(),
							message.getFileName());
					
					if(srcPsth != null) {
						// ȡ��message �ֽ�����  �����д�����
						FileOutputStream fis = new FileOutputStream(srcPsth);
						try {
							fis.write(message.getFileBytes());
							fis.close();
							
						// ·������, �׳��쳣�� ������ʾ
						} catch (FileNotFoundException e) {
							// TODO: handle exception
							JOptionPane.showMessageDialog(null, "����ʧ�� !!", message.getGetter() + " �ĵ�����ʾ", JOptionPane.INFORMATION_MESSAGE);
						}
						// ������ʾ
						JOptionPane.showMessageDialog(null, "�ļ��ѱ���",  message.getGetter() + " �ĵ�����ʾ", JOptionPane.INFORMATION_MESSAGE);
					
					}
					
					
				}  else if (message.getMessType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
				// ������. ���շ���˷��� ���˷��͵� �˳�ϵͳmessage,  �ر�socket  break�˳��߳�
					
					// ���̵߳�socket ��Ϊ ���˳��ͻ���socket, ֱ�ӹر�
					socket.close();   
					
					// �˳�whileѭ��, run����������, ���߳̽���
					break;   
					
				} else {
					// ��ʱ��������
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



	/**  ������ʾ�û����� �����ļ���ַ
	 * 
	 * @param userId   ʹ����
	 * @param sender   ������
	 * @param fileName ���͹������ļ���
	 * @return srcPath �ļ������·��
	 */
	public String saveFileAddress(String userId, String sender, String fileName) {
		
		String srcPath = JOptionPane.showInputDialog(null, 
				sender + "�����ļ�: " + fileName + "\n�����뱣���ļ���·��:\n\t��ʽ: C:\\xxx.txt", 
				userId + "  �ĵ�������", JOptionPane.INFORMATION_MESSAGE);

		return srcPath;
	}
	
	
	
    public void println(String s) {
        if (s != null) {
            chatFrame.getTxt_Chat().setText(chatFrame.getTxt_Chat().getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
	
    public void onlineUserPrintln(String s) {
        if (s != null) {
            onlineUserFrame.getTaShow().setText(s + "\n");
            System.out.println(s + "\n");
         }
     }

	/* set get���� */
	public Socket getSocket() {
		return socket;
	}

	public ChatFrame getChatFrame() {
		return chatFrame;
	}

	public void setChatFrame(ChatFrame chatFrame) {
		this.chatFrame = chatFrame;
	}
	
}
