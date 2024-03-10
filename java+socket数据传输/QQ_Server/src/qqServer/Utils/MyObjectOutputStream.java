package qqServer.Utils;

import java.io.IOException; 
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author sky
 * @version 4.0
 * 	 ������������
 */
public class MyObjectOutputStream extends ObjectOutputStream{

	public MyObjectOutputStream(OutputStream out) throws IOException {
		super(out);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void writeStreamHeader() throws IOException {
		// TODO Auto-generated method stub
		return;
	}
	
}

/*
 *	������������ �ᵥ������һ��socket �� �߳�
 *		���߳̽��շ���˷��͵���Ϣ��ʱ, ����:
 *  ����: java.io.StreamCorruptedException: invalid type code: AC
 *	λ��: ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
 *	
 *
 *	ԭ��:
 *	1.ÿ�����Ǵ�һ���ļ�������ʹ�� ObjectOutputStream �� FileOutputStream �������л����󸽼ӵ��ļ�ĩβʱ��
 *	  ObjectOutputStream ���Ὣ��ͷд���ļ�ĩβ��д��������ݡ�
 *	2.ÿ�δ��ļ���д���һ������ʱ��ObjectOutputStream ������д���������֮ǰ����ͷд���ļ�ĩβ. 
 *	��ˣ������ַ�ʽ��ÿ����׷��ģʽ���ļ���ʹ�� FileOutputStream �� ObjectOutputStream д�����ʱ, ��ͷ���ᱻ���д�롣
 *	
 *	���:
 *	�������Լ���Object Output Stream�࣬
 *	����MyObjectOutputStream�࣬ͨ����չObjectOutputStream���̳У��͸��Ƿ�������protected void writeStreamHeader�����׳�IOException��
 *	�������У��˷�����Ӧִ���κβ���
 *
 *	��ϸ�ο�: 
 *	https://www.geeksforgeeks.org/how-to-fix-java-io-streamcorruptedexception-invalid-type-code-in-java/
*/





