package qqClient.view;

import java.awt.BorderLayout;  
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import qqClient.Service.UserClientService;


/**
 * @author sky
 * @version 4.0
 * 
 * 	�ͻ��� �����û��б� ����  [������]
 * 		1. ��ȡ�����û�
 * 		2. �������촰��(˽��/Ⱥ��)
 */
public class OnlineUserFrame extends JFrame{
 
	private static final long serialVersionUID = 1L;
	private JButton btn_gain, btn_startChat;
	private JTextArea taShow;
	private JTextField txt_inputUser;
	private JLabel label_user;
	
	private UserClientService userClientService;
	
	public OnlineUserFrame(String userId, UserClientService userClientServices) {
		
		this.userClientService = userClientServices;
		
		/*  ����UserClientService�� starThread����
		 * 	�����������, �ڸ÷����������߳�  
		 */
		userClientService.startThread(userId, OnlineUserFrame.this);
		
		// ��ȡ�û�ģ��
		btn_gain = new JButton("��ȡ�����û�");
		label_user = new JLabel("�����û��б�    ");
		label_user.setFont(new Font("����", Font.PLAIN, 16));
		label_user.setForeground(new Color(250, 0, 0));
		
		// �����������
		btn_startChat = new JButton("��������");
		txt_inputUser = new JTextField(10);
		
		// ��ʾ���
		taShow = new JTextArea();
		taShow.setFont(new Font("����", Font.PLAIN, 16));
		taShow.setText("\n�ȵ�� '��ȡ�����û�' ��ť \n��ȡ�û��б�\n");
		
		
		JPanel top = new JPanel(new FlowLayout());
		top.add(label_user);
		top.add(btn_gain);
		this.add(top, BorderLayout.NORTH);
		
		// ��ʽ������
		JPanel buttom = new JPanel(new FlowLayout());
		buttom.add(txt_inputUser, FlowLayout.LEFT);
		buttom.add(btn_startChat);
		this.add(buttom, BorderLayout.SOUTH);
		
		// �ı���߿� ����
		final JScrollPane sp = new JScrollPane();
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setViewportView(this.taShow);
		this.taShow.setEditable(false);
		this.add(sp, BorderLayout.CENTER);
		
		this.setTitle(userId + " �����û�����");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 500);
		this.setLocation(100, 200);
		this.setVisible(true);
		this.setResizable(false);
		
		// ��ȡ�û� ��ť��Ӧ
		btn_gain.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				/*	���� ��½ע����� ��ȡ�����û�����
				 */
				userClientService.onlineFriendList(userId);
			}
		});
		
		// ����������Ӧ
		btn_startChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// �� ��������� ��Ϊ��
				if(txt_inputUser != null) {
					/*  ��new һ��UserClientService����
					 * 		�����߳�, �������촰�� chatFrame
					 */
					new ChatFrame(userId, txt_inputUser.getText());
				
					
					// ��������� �� ��ʾ��� �ÿ�
					txt_inputUser.setText(null);
					taShow.setText(null);
					 
				}
				
			}
		});
		
		// �ͻ��� �˳�
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				// ������ʾ
				JOptionPane.showMessageDialog(null, "�Ƿ��˳�?", 
						"ȷ��", JOptionPane.QUESTION_MESSAGE);
					/*	����userClientService�� 
					 * 		�� logout()���������˷����˳���ʾ  ���쳣�˳�
					*/
				userClientService.logout(userId, "����");
				System.exit(0); // �ر�
				
			}
		});
		
		
	}
/* get set */
	public JTextArea getTaShow() {
		return taShow;
	}

	public void setTaShow(JTextArea taShow) {
		this.taShow = taShow;
	}

	public JTextField getTxt_inputUser() {
		return txt_inputUser;
	}

	public void setTxt_inputUser(JTextField txt_inputUser) {
		this.txt_inputUser = txt_inputUser;
	}

	
//	public static void main(String[] args) {
//		new OnlineUserFrame("123" ,new UserClientService());
//	}
}
