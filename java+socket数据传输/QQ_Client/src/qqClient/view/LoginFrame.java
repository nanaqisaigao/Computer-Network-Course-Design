package qqClient.view;

import javax.swing.*;    

import qqClient.Service.UserClientService;

import java.awt.*;
import java.awt.event.*;


/**
 * @author sky
 * @version 3.0
 * 
 * 	�ͻ��� ��¼����
 * 		1. ���������û�����OnlineUserFrame [������]
 * 		2. ���� ע�ᴰ��
 */
public class LoginFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	// ����logo��
	private JLabel bq_North;
	
	// �в���
	private JTabbedPane choose;
	private JPanel jp_Center;
	
	// �в����ͷ����
	private JLabel bq_head;
	
	// �в��Ҳ� ���������
	private JPanel jp_input;
	private JLabel label_user, label_pwd, label_empty;  // ��ʾ
	private JTextField txt_user;             // �û�������
	private JPasswordField txt_pwd;          // ��������
	private JButton btn_sweep, btn_regist;   // ��� �� ע�� ��ť
	
	// �ײ���¼��
	private JButton btn_login;
	private JPanel  jp_South;

	private UserClientService userClientService = null;  // �û���¼ע����

	public LoginFrame() {
	/* ����logo�� */
		// ���붥��logo
		ImageIcon logo = new ImageIcon("image/QQlogo.png");
		logo.setImage(logo.getImage().getScaledInstance(516, 170,
				Image.SCALE_DEFAULT));
		bq_North = new JLabel(logo);   // ����Ϸ�logo
		
				
	/* �ײ���¼�� */
		jp_South = new JPanel();    // ����·����
		btn_login = new JButton();         // ��ŵ�¼��ť��ͼƬ
		// �·� ��¼��ť ����ͼƬ
		ImageIcon ico_login = new ImageIcon("image/loginBtn.png");
		ico_login.setImage(ico_login.getImage().getScaledInstance(350, 45,
				Image.SCALE_DEFAULT));
		
		// ���� ��¼��ť
		btn_login.setIcon(ico_login);
		btn_login.setBorderPainted(false);
		btn_login.setBorder(null);
		btn_login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jp_South.add(btn_login);
		
		
	/* �в���� ͷ���� */
		// �м��Ҳ� ͷ��ͼƬ
		ImageIcon headIcon = new ImageIcon("image/headshot.png");
		headIcon.setImage(headIcon.getImage().getScaledInstance(120, 144,
				Image.SCALE_DEFAULT));
		bq_head = new JLabel(headIcon);
		
		label_empty = new JLabel("              ");
		
		
	/* �в� ������ */	
		// �м��� �������ͷ��  �Ҳ��������
		jp_input = new JPanel();
		jp_Center = new JPanel();
		jp_Center.add(bq_head);
		jp_Center.add(label_empty);
		jp_Center.add(jp_input);
		jp_input.setSize(200, 300);
		
		choose = new JTabbedPane();
		choose.add("�û���¼", jp_Center);
		
		
	/* �����м��Ҳ� ��������� */
		label_user = new JLabel("�û���", JLabel.CENTER);
		label_user.setFont(new Font("����", Font.PLAIN, 16));
		label_pwd = new JLabel("��  ��", JLabel.CENTER);
		label_pwd.setFont(new Font("����", Font.PLAIN, 16));
		txt_user = new JTextField();
		txt_user.setFont(new Font("����", Font.PLAIN, 14));
		txt_pwd = new JPasswordField();
		
		// ������� �� ע���˺Ű�ť
		btn_sweep = new JButton("�������");
		btn_sweep.setForeground(Color.red);
		btn_regist = new JButton("ע���˺�");
		btn_regist.setPreferredSize(new Dimension(20,20));
		btn_regist.setForeground(Color.blue);
	
		// ������ �������
		jp_input.setLayout(new GridLayout(3, 3));
		jp_input.add(label_user);
		jp_input.add(txt_user);
		jp_input.add(label_pwd);
		jp_input.add(txt_pwd);
		jp_input.add(btn_sweep);
		jp_input.add(btn_regist);
		
	
	/* ���� ����ҳ�� ��λ���� */
		add(choose, BorderLayout.CENTER);
		add(bq_North, BorderLayout.NORTH);
		add(jp_South, BorderLayout.SOUTH);
		
		btn_login.addActionListener(this);   // ��¼��ť ��Ӧ
		btn_sweep.addActionListener(this);   // ������� ��Ӧ
		btn_regist.addActionListener(this);  // ע���¼� ��Ӧ
		
		// ���ô���
		setVisible(true);
		setBounds(500, 150, 500, 460);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("�ͻ��˵�½");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * �������� "��¼" ��ť 
		 * 	 1. �ж��ʺŻ��������Ƿ�Ϊ��, ������ȷ�������Ƿ���ͬ
		 * 	 2. ����UserClientService�� �� checkUser()���� ���˺���������message�����͸� �����
		 *   3. checkUser()���� ����˷��ص�message, �ж���Ϣ���Ƿ�ɹ�, ����boolean
		 *   4. booleanΪtrue, �Ե��ø����startThread()���� �����߳�, ��socket�����߳�, ����socket���뼯����
		 *   5. �������촰��chatFrame
		 */
		if (e.getSource() == btn_login) {
			String userId = txt_user.getText().trim();
			String password = new String(txt_pwd.getPassword()).trim();
			
			if ("".equals(userId) || userId == null) {
				JOptionPane.showMessageDialog(null, "�������ʺţ���");
				return;
			}
			if ("".equals(password) || password == null) {
				JOptionPane.showMessageDialog(null, "���������룡��");
				return;
			}
			
			/* ���˺ź����� ���͵� ����� ��֤
			 *    
			 *    ͨ��qqClient.Service���µ�UserClientService �ͻ��˵�¼������
			 *    ������ �Ѵ����� ����, 
			 *    ����checkUser()���� ��֤�˺� ���� �Ƿ���ȷ
			 *    ͬʱͨ���÷��� ���ӷ����� �������߳� ����ڼ���
			 */
			
			userClientService = new UserClientService();
			if(userClientService.checkUser(userId, password)) {
				
				// ������¼�ɹ�
				this.dispose();
				JOptionPane.showMessageDialog(null, "��½�ɹ���");
				
				// ���������û�����
				new OnlineUserFrame(userId, userClientService);
			
			} else {
				
			}
			

			// ��� "ע��" ��ť, ����ע�����
		} else if(e.getSource() == btn_regist) {

			this.setVisible(false);
			new RegisterFrame();
			
			
		} else if (e.getSource() == btn_sweep) {
			// ����������
			txt_pwd.setText("");
		}
	}
	

}






