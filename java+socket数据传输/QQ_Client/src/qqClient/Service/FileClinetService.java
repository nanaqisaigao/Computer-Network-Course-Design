package qqClient.Service;

import java.io.File;  
import java.io.FileInputStream;
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
 * 	�ͻ��� -- �ļ�������
 * 
 * 	���������ļ�����
 */

public class FileClinetService {
	
	private ChatFrame chatFrame = null;
	
	/**  Ⱥ���ļ�����
	 * 
	 * @param senderId  ���ͷ�
	 * @param srcPath   �����ļ�·��
	 * @param fileName  �ļ���
	 * @param chatFrame �������
	 */
	public void sendFileToAll(String senderId, String srcPath, String fileName, ChatFrame chatFrame) {
		
		this.chatFrame = chatFrame;
		
		// ���ļ���Ϊ��, ˵��û��ѡ���ļ�
		if(fileName == null) {
			return;
		}
		
		// ����message
		Message message = new Message();
		message.setMessType(MessageType.MESSAGE_File_MES_TOALL);   // ��������
		message.setSender(senderId);
		message.setFileName(fileName);
		message.setSendTime(new Date().toString());  // ����ʱ��
		
		// ��ȡ Դ�ļ� ·��srcPath
		FileInputStream fis = null;
		                              // ��ȡ�ļ����� תΪint��
		byte []fileBytes = new byte[(int)new File(srcPath).length()];
		
		try {
			fis = new FileInputStream(srcPath);
			fis.read(fileBytes);   // ��srcԴ�ļ� ��ȡ����������
			
			// ���ֽ����� ����message
			message.setFileBytes(fileBytes);
		
			// ����message ������� 
			ClientConnectThread thread = ClientConnectThreadManage.getThread(senderId, "Ⱥ��");
			
				MyObjectOutputStream oos = 
						new MyObjectOutputStream(thread.getSocket().getOutputStream());
				
				oos.writeObject(message);
				
			// ��ʾ��Ϣ
			println(senderId + "(��)  �����ļ�:\t\t   " + new Date().toString());
			println("   ·��Ϊ:  " + srcPath + "\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �ر���
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
	}
	
	
	/**  �����ļ�����
	 * 
	 * @param senderId ������id
	 * @param getterId ������id
	 * @param srcPath  Դ�ļ�·��
	 * @param destPath �����ļ�����·��
	 */
	public void sendFileToOne(String senderId, String getterId, String srcPath, String fileName, ChatFrame chatFrame) {
		
		this.chatFrame = chatFrame;
		
		// ���ļ���Ϊ��, ˵��û��ѡ���ļ�
		if(fileName == null) {
			return;
		}
		
		// ����message
		Message message = new Message();
		message.setMessType(MessageType.MESSAGE_File_MES);   // ��������
		message.setSender(senderId);
		message.setGetter(getterId);
		message.setFileName(fileName);
		message.setSendTime(new Date().toString());  // ����ʱ��
		
		// ��ȡ Դ�ļ� ·��srcPath
		FileInputStream fis = null;
		
		                           // ��ȡ�ļ����� תΪint��
		byte []fileBytes = new byte[(int)new File(srcPath).length()];
		
		try {
			fis = new FileInputStream(srcPath);
			
			fis.read(fileBytes);   // ��srcԴ�ļ� ��ȡ����������
			// ���ֽ����� ����message
			message.setFileBytes(fileBytes);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �ر���
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		try {
		// ����message ������� 
		ClientConnectThread thread = ClientConnectThreadManage.getThread(senderId, getterId);
		
			MyObjectOutputStream oos = 
					new MyObjectOutputStream(thread.getSocket().getOutputStream());
			
			oos.writeObject(message);
			
			// ���ͳɹ� ��ӡ
			println(senderId + "(��)  �����ļ�:\t\t   " + new Date().toString());
			println("   ·��Ϊ:  " + srcPath + "\n");
			
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




