package qqServer.view;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import qqServer.server.QQServer;
import qqServer.server.SendToAllService;

/**
 * @author sky
 * @version 2.0
 *
 * 	����˽���
 *  ����: 1. �������������
 *		  2. ��ʾ ��ͻ��˵���Ϣ
 *		  3. ��ͻ���������Ϣ
 */
public class ServerFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private JButton btn_Start;      // ����������
	private JButton btn_Send;       // ������Ϣ��ť
	private JTextField txt_Send;    // ��Ҫ���͵��ı���Ϣ
	private JTextArea taShow;       // ��Ϣչʾ
	public QQServer server;        // ���������ͻ�������
	
	private SendToAllService toAllService = null;   // ������������� �߳��� 

	public ServerFrame() {
		super("��������");
		btn_Start = new JButton("����������");
		btn_Start.setForeground(new Color(200, 0, 0));
		btn_Send = new JButton("������Ϣ");
		
		txt_Send = new JTextField(10);
		taShow = new JTextArea();
		taShow.setFont(new Font("����", Font.PLAIN, 16));
		
		// ��ʽ������
		JPanel top = new JPanel(new FlowLayout());
		top.add(txt_Send);
		top.add(btn_Send);
		top.add(btn_Start);
		this.add(top, BorderLayout.SOUTH);
		
		// �ı���߿� ����
		final JScrollPane sp = new JScrollPane();
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setViewportView(this.taShow);
		this.taShow.setEditable(false);
		this.add(sp, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 300);
		this.setLocation(100, 200);
		this.setVisible(true);
		
		
	/* �����¼� */
		
		// ��� "���������" ��ť, ��ʼ��QQServer
		btn_Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*
				 *  �½��߳�, �����������ͻ���(�߳�socket)
				 *  ��ֹ��Ϊ��ѭ�� ���½��濨��
				 */
				new Thread() {
					@Override
					public void run() {
						// ��� �������� ʱ, ��ʼ��QQServer ���������socket ���߳�
						server = new QQServer(ServerFrame.this);
							
					}
				}.start();
				
			}
		});
		
		// �� �ͻ���������Ϣ
		btn_Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// ��ʼ��Ⱥ����Ϣ��
				toAllService = new SendToAllService(ServerFrame.this);
				
				// ���������� pushNews���� Ⱥ����Ϣ
				toAllService.pushNews(txt_Send.getText());
				txt_Send.setText("");
			}
		});
	
		// �ͻ����˳�
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// ������ʾ
				int a = JOptionPane.showConfirmDialog(null, "ȷ���ر���", "��ܰ��ʾ",
						JOptionPane.YES_NO_OPTION);
				if (a == 1) {
					
					System.exit(0); // �ر�
				}
			}
		});
	}
	
//	// ���в���
//	public static void main(String[] args) {
//		new ServerFrame();
//	}
	
    public void println(String s) {
        if (s != null) {
            this.taShow.setText(this.taShow.getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }

  /* get set */
	public JTextArea getTaShow() {
		return taShow;
	}

	public void setTaShow(JTextArea taShow) {
		this.taShow = taShow;
	}

	public JTextField getTxt_Send() {
		return txt_Send;
	}

	public void setTxt_Send(JTextField txt_Send) {
		this.txt_Send = txt_Send;
	}
    
    
}
