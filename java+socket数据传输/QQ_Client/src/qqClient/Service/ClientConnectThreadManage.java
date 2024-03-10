package qqClient.Service;

import java.util.HashMap;


/**
 * 
 * @author sky
 * @version 1.0
 * 
 * 	����  �ͻ������ӵ�����˵��߳�  ����
 * 
 *  �ͻ��� �̹߳�����
 */
//           �ͻ���   ����  ����  �߳�  ����
public class ClientConnectThreadManage {

	// 4.0 �̹߳������
	// ������߳� ���� HashMap����, keyΪ�û�id  value Ϊ�߳� 
	private static HashMap<String, HashMap<String, ClientConnectThread>> map = new HashMap<>();
	// һ���û����̼߳���    ״̬      ��Ӧ�߳�
	private static HashMap<String, ClientConnectThread> stateMap = new HashMap<>();
	
	
	public static HashMap<String, HashMap<String, ClientConnectThread>> getMap() {
		return map;
	}

	public static HashMap<String, ClientConnectThread> getStateMap() {
		return stateMap;
	}

	// ����߳� ���뼯�ϵķ���
	public static void addThread(String userId, String state, ClientConnectThread thread) {
		stateMap.put(state, thread);
		map.put(userId, stateMap);
	}
	
	
	// ����userId �����߳�
	public static ClientConnectThread getThread(String userId, String state) {
		
		return map.get(userId).get(state);
	}
	
	
	// �Ӽ�����ɾ�� ĳ���̶߳���
	public static void removeThread(String userId) {
		map.remove(userId);
	}
}
