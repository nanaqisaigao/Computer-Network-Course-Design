package qqServer.server;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author sky
 * @version 1.0 
 * 
 * 	 ����� �����û��߳���
 * 	
 * 	1.�����û��߳� �ļ���:
 * 			  �û���id        ���û����̼߳���
 * 		HashMap<String, HashMap<String, ServerConnectThread>>
 * 
 * 	2.�û��� �̼߳���:
 * 			    ״̬(�������)     �߳�
 * 		HashMap<String, ServerConnectThread>     ���û���һ�����촰�� ���� һ���߳�
 */

public class ServerConnectThreadManage {


	// �����û��̵߳ļ��� 
	// ע����Ҫ��̬
	// ���û��̼߳���          userId      ���û����̼߳���
	private static HashMap<String, HashMap<String, ServerConnectThread>> map = new HashMap<>();

	
	public static HashMap<String, HashMap<String, ServerConnectThread>> getMap() {
		return map;
	}


	// ����߳� ���뼯�ϵķ���
	public void addThread(String userId, String state, ServerConnectThread thread) {
		
		if(map.get(userId) != null) {
			// �����Ѵ���, �õ��û��̼߳��� ����� 
			map.get(userId).put(state, thread);
			
		} else {  // �û�������
			
			//     �������û����̼߳���
			HashMap<String, ServerConnectThread> stateMaps = new HashMap<>();
			stateMaps.put(state, thread);
			// ���û����̼߳��� ���� ���û�����
			map.put(userId, stateMaps);
		}
		
	}
	
	
	// ����userId �����߳�
	public static ServerConnectThread getThread(String userId, String state) {
		
		return map.get(userId).get(state);
	}
	
	
	// �Ӽ�����ɾ�� ĳ���̶߳���
	public static void removeThread(String userId, String state) {
		//  ��ȡ���û��̼߳���   ɾ����Ӧ�߳�
		map.get(userId).remove(state);
	}
	
	
	// �û��˳�, ֱ��ɾ�� �û���������
	public static void removeUserId(String userId) {
		
		map.remove(userId);
	}
	
	
	/*
	 *  ���������û��б�ķ���, ������userId
	 */
	public static String getOnlineUsers() {
		
		// �������� 
		// ��ȡ�����е�key ͨ��keyȡ����Ӧ��value(�˴�ֻ��ȡkey)
		Iterator<String> iterator = map.keySet().iterator();
		String onlineUserList = "  ";
		
		while(iterator.hasNext()) {
			// ȡ��ÿһ��userIdתΪ�ַ���
			// ���ڿͻ��˽��յ��ַ���ʱ���ܽ����
			// ���� "\n  " ���з���2���ո� �ָ�� �ͻ������
			onlineUserList += iterator.next().toString() + "\n  ";
		}
		
		return onlineUserList;
	}
	
}
