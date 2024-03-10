package qqServer.server;

import java.io.IOException;  
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import qqCommon.Message;
import qqCommon.MessageType;
import qqServer.Utils.MyObjectOutputStream;
import qqServer.view.ServerFrame;

/**
 * 
 * @author sky
 * @version 2.0
 * 
 * 	����� ����(Ⱥ��)��Ϣ��
 * 		Ⱥ����Ϣ���ͻ��� Ⱥ�Ĵ���
 */

public class SendToAllService {

	private ServerFrame serverFrame = null;   // ����
	
	public SendToAllService(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}

		
	/*
	 * 	����� ���û�������Ϣ ����
	 * 	��ServerFrame ����, �����"����" ��ťʱ����
	 */
	public void pushNews(String news) {
		
		// 1.����һ��message Ⱥ��������Ϣ
		Message message = new Message();
		message.setSender("\n===== ������ ��ܰ��ʾ =====");
		message.setContent(news + "\n\n=========================\n");
		
		// ��������Ϊ Ⱥ����Ϣ(ͬ�ͻ��˵�Ⱥ��)   �Ͳ��������� �ͻ��� �ӽ��մ���
		message.setMessType(MessageType.MESSAGE_ToAll_MES);     
		message.setSendTime(new Date().toString());   // ����ʱ��
		
		println("�������������� ������Ϣ: " + news);
		
		// 2.ֱ���ڴ� ѭ������ͨ���߳�   �õ�socket,������Ϣ
		// �����̹߳����� �ķ����õ� ����
		HashMap<String, HashMap<String, ServerConnectThread>> map 
				= ServerConnectThreadManage.getMap();
		
		// �������� ������Ϣ
		Iterator<String> iterator = map.keySet().iterator();
	
		while(iterator.hasNext()) {
			// �����û�Id  
			String onlineUserId = iterator.next().toString();
			
			try {
				// get���� ״̬Ϊ"Ⱥ��" �����߳�  
				ServerConnectThread thread = map.get(onlineUserId).get("Ⱥ��");
				
				if(thread != null) {
					MyObjectOutputStream oos =         
							new MyObjectOutputStream(thread.getSocket().getOutputStream());
					
					message.setGetter(onlineUserId);
					oos.writeObject(message);
				} else {
					JOptionPane.showMessageDialog(null, "����ʧ�� !!");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		serverFrame.getTxt_Send().setText("");
	}
	
	
	// println��� ȷ����������������
    public void println(String s) {
        if (s != null) {
           serverFrame.getTaShow().setText(serverFrame.getTaShow().getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
}



