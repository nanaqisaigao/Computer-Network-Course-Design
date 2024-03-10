package qqClient.view;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import qqClient.Service.UserClientService;


/**
 * 
 * @author sky
 * @version 2.0
 * 
 * 	�û� ע�����	
 * 		�û�ע��
 */
public class RegisterFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel bq_North;                                 // ����logo  ģ��
	private JLabel label_user, label_pwd, label_confim_pwd;  // �û���, ����, ȷ������  ��ʾ
	private JButton btn_regist;                              // ע�ᰴť
	private JTextField txt_user;                             // �û��� �����
	private JPasswordField txt_pwd, txt_confim_pwd;          // ����,ȷ�����������
	private JTabbedPane choose;                              // ��ɫ�߿�
	private JPanel jp_Center, jp_South;                      

	
	public RegisterFrame() {
		
	/* ����logo�� */
		// ���붥��logo
		ImageIcon logo = new ImageIcon("image/QQlogo4.png");
		logo.setImage(logo.getImage().getScaledInstance(350, 285,
				Image.SCALE_DEFAULT));
		bq_North = new JLabel(logo);   // ����Ϸ�logo
		
		
	/* �ײ�ע���� */
		jp_South = new JPanel();    // ����·����
		btn_regist = new JButton();         // ���ע�ᰴť��ͼƬ
		// �·� ע�ᰴť ����ͼƬ
		ImageIcon ico_login = new ImageIcon("image/registBtn.png");
		ico_login.setImage(ico_login.getImage().getScaledInstance(220, 40,
				Image.SCALE_DEFAULT));	
		// ���� ע�ᰴť
		btn_regist.setIcon(ico_login);
		btn_regist.setBorderPainted(false);
		btn_regist.setBorder(null);
		btn_regist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jp_South.add(btn_regist);

		
     /* �м� ��������� */
		label_user = new JLabel("�û���", JLabel.CENTER);
		label_user.setFont(new Font("����", Font.PLAIN, 14));
		txt_user = new JTextField();
		
		label_pwd = new JLabel("��   ��", JLabel.CENTER);
		label_pwd.setFont(new Font("����", Font.PLAIN, 14));
		txt_pwd = new JPasswordField();
		
		label_confim_pwd = new JLabel("ȷ������", JLabel.CENTER);
		label_confim_pwd.setFont(new Font("����", Font.PLAIN, 14));
		txt_confim_pwd = new JPasswordField();
		
		jp_Center = new JPanel();
		choose = new JTabbedPane();
		choose.add("�û�ע��", jp_Center);
	
		// ������
		jp_Center.setLayout(new GridLayout(7, 3));
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(label_user);
		jp_Center.add(txt_user);
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(label_pwd);
		jp_Center.add(txt_pwd);
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(label_confim_pwd);
		jp_Center.add(txt_confim_pwd);
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		jp_Center.add(new JLabel());
		
			
	/* ���� ����ҳ�� ��λ���� */
		add(choose, BorderLayout.CENTER);
		add(bq_North, BorderLayout.NORTH);
		add(jp_South, BorderLayout.SOUTH);
		
		btn_regist.addActionListener(this);   // ��¼��ť ��Ӧ
		
		// ���ô���
		setVisible(true);
		setBounds(200, 50, 350, 600);
		setResizable(false);
		getContentPane().setBackground(Color.white);
		setTitle("�ͻ��� �û�ע��");
	}
	
	
// ��������
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * �������� "ע��" ��ť 
		 * 	 1. �ж��ʺŻ��������Ƿ�Ϊ��, ������ȷ�������Ƿ���ͬ
		 * 	 2. ����UserClientService�� �� registUser()����
		 */
		if (e.getSource() == btn_regist) {
			String userId = txt_user.getText().trim();
			String password = new String(txt_pwd.getPassword()).trim();
			String confimPwd = new String(txt_confim_pwd.getPassword()).trim();
			
			if ("".equals(userId) || userId == null) {
				JOptionPane.showMessageDialog(null, "�������ʺ� ����");
				return;
			}
			if ("".equals(password) || password == null) {
				JOptionPane.showMessageDialog(null, "���������� ����");
				return;
			}
			if ("".equals(confimPwd) || confimPwd == null) {
				JOptionPane.showMessageDialog(null, "���ٴ�ȷ������ ����");
				return;
			}
			if(password.length() < 6 || password.length() > 20) {
				JOptionPane.showMessageDialog(this, "���볤��С��6�����20  ���������� ! !");
				return;
			}
			if(!confimPwd.equals(password)) {
				JOptionPane.showMessageDialog(null, "���벻һ��   ���������� ! !");
				return;
			}
			
			/* 	��ʼ�� UserClientService�����
			 * 		registUser()���� ע��
			 */
			UserClientService userClientService = new UserClientService();
			if(userClientService.registUser(userId, password)) {
				
				JOptionPane.showMessageDialog(this, "ע��ɹ�");
				this.setVisible(false);
				new LoginFrame();
				
			} else {
				JOptionPane.showMessageDialog(this, "�˺��Ѵ���, ����������!");
			}


		}
	}
	
}




