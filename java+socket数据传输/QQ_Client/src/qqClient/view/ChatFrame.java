package qqClient.view;

import javax.swing.*;  

import qqClient.Service.FileClinetService;
import qqClient.Service.MessageClientService;
import qqClient.Service.UserClientService;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * @author sky
 * @version 3.0
 * 
 * 	�ͻ��� ������洰��
 * 		1. ����
 * 		2. �ļ�����
 */
public class ChatFrame extends JFrame {
    
	private static final long serialVersionUID = 1L;
	
    //��Ϣ���ؼ�
    private JTextArea txt_Chat = new JTextArea();    // ��������ı�
    private JTextArea txt_Send = new JTextArea();    // �������ı�
    private JButton btn_Send = new JButton();        // ���� ��Ϣ��ť
    
    //�����ļ��ؼ�
    private JTextField txt_SendFile = new JTextField();     // �ļ�·�� �ı�
    private JButton btn_SendFile = new JButton();           // �����ļ���ť
    private JButton btn_ChooseFile = new JButton();         // �ļ�ѡ��ť
    private  JProgressBar progressBar = new JProgressBar(); // ������
    
    private String sendFileName = null;       // �����ļ� ���ļ���                
    private MessageClientService messageClientService = null;   // ������
    private FileClinetService fileClinetService = null;         // �ļ�������

    public ChatFrame(String userId, String getterId) {
        System.out.println(Thread.currentThread().getName());
        
        // ��ʼ���ļ�������
        this.fileClinetService = new FileClinetService();
        // ��ʼ�� ������
        this.messageClientService = new MessageClientService(ChatFrame.this);
       
        // ��ʼ�� �����߳�
		UserClientService userClientService = new UserClientService();	
		userClientService.startThreadChat(userId, getterId, ChatFrame.this);
		
		
     /* ��Ϣ��� */
        // �������������� ������
		JScrollPane spChat = new JScrollPane();
		spChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spChat.setViewportView(this.txt_Chat);
		spChat.setBounds(1, 1, 660, 350);
		this.add(spChat);
		this.txt_Chat.setEditable(false);    // ���������ʾ���  ���������ı�
		txt_Chat.setFont(new Font("����", Font.PLAIN, 16));
        
		
		txt_Chat.setText("\n----------------------------- ��ӭ��¼���������� ------------------------------\n");
		
		
	/* ��Ϣ���� */
        txt_Send.setBounds(1, 350, 660, 100);      
        this.add(txt_Send);                   // �����������
        
        
    /* ��Ϣ���� */
        // ������Ϣ ��ťͼƬ
		ImageIcon ico_login = new ImageIcon("image/sendBtn.png");
		ico_login.setImage(ico_login.getImage().getScaledInstance(140, 35,
				Image.SCALE_DEFAULT));	
		// ���� ������Ϣ��ť
		btn_Send.setIcon(ico_login);
		btn_Send.setBorderPainted(false);
		btn_Send.setBorder(null);
		btn_Send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn_Send.setBounds(10, 460, 140, 35); // ��� ������Ϣ ��ť
        this.add(btn_Send);
        btn_Send.setFont(new Font("����", Font.BOLD, 16));
        
    /* �����ļ� */
        // �����ļ� ���ݿ� 
        txt_SendFile.setBounds(10, 520, 400, 32);
        this.add(txt_SendFile);

        // �ļ�ѡ��ť  �����ļ�ѡ����
        btn_ChooseFile.setBounds(410, 520, 100, 30);
        btn_ChooseFile.setText("ѡ���ļ�");
        this.add(btn_ChooseFile);
        btn_ChooseFile.setFont(new Font("����", Font.BOLD, 14));

        // �����ļ� ��ť
        btn_SendFile.setBounds(520, 520, 100, 30);
        btn_SendFile.setText("�����ļ�");
        this.add(btn_SendFile);
        btn_SendFile.setFont(new Font("����", Font.BOLD, 14));
        
        
    /* ������ */
        progressBar.setBounds(320, 520, 200, 30);
        this.add(progressBar);
        progressBar.setMaximum(100);
        progressBar.setValue(0);

        
    /* ���ڲ��� */
        setBounds(500, 100, 675, 600);
        setVisible(true);
        setResizable(false);
        
        if(getterId.equals("Ⱥ��")) {
            setTitle(userId + "    ��Ⱥ�������Ҵ���");
        } else {
			setTitle(userId + "  =>  " + getterId + "  �����촰��");
		}
        

        // ������Ϣ�����¼�
        btn_Send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                	 
                if(getterId.equals("Ⱥ��")) {

	                /* ����������messageClientService 
	                 * 		��sendMessageToAll()Ⱥ�ķ���  �����˷������� 
	                 */
	                messageClientService.sendMessageToAll(userId, txt_Send.getText());
	                txt_Send.setText("");   // ����
	                
                } else {
					messageClientService.sendMessageToOne(userId, getterId, txt_Send.getText());
					txt_Send.setText("");
				}
			
            }
        });

        
        
		// �ͻ����˳�
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				// ������ʾ
				JOptionPane.showMessageDialog(null, "�Ƿ��˳�?", 
						"ȷ��", JOptionPane.QUESTION_MESSAGE);
					/*	����userClientService�� 
					 * 		�� logout()���������˷����˳���ʾ  ���쳣�˳�
					*/
					userClientService.logout(userId, getterId);
					
					this.setVisible(false);  // ����رպ� ����
				
			}

			private void setVisible(boolean b) {}
		});
        
        
        
        // ѡ���ļ� �����¼�
        btn_ChooseFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //�ļ�ѡ����
                JFileChooser chooser = new JFileChooser();
                
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//�ļ������ļ�
                
                /* JFileChooser �ļ�ѡ����
                 * ����: 
                 *     parent: �ļ�ѡȡ���Ի���ĸ����, �Ի��򽫻ᾡ����ʾ�ڿ��� parent ������; ����� null, ����ʾ����Ļ���ġ�
                 * 
                 * ����ֵ:
                 *     JFileChooser.CANCEL_OPTION: �����ȡ����ر�
                 *     JFileChooser.APPROVE_OPTION: �����ȷ�ϻ򱣴�
                 *     JFileChooser.ERROR_OPTION: ���ִ���
                 */
                // �ڻ�ȡ�û�ѡ����ļ�֮ǰ��ͨ������֤����ֵ�Ƿ�Ϊ APPROVE_OPTION.
                int num = chooser.showOpenDialog(null);
                
                // ��ѡ�����ļ������ӡѡ����ļ�·��
                if(num == JFileChooser.APPROVE_OPTION)
                {
                    File file = chooser.getSelectedFile();         // ��ȡ�ļ�
                    sendFileName = file.getName();                 // �����ļ���
                    txt_SendFile.setText(file.getAbsolutePath());  // ����ļ�·����txt_SendFile
                }
            }
        });
        
        
        // �����ļ� �����¼�
        btn_SendFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                
               /*  �����ļ������� fileClinetService
                * 		sendFileToAll() Ⱥ���ļ�����
                */
                if(getterId.equals("Ⱥ��")) {

                    fileClinetService.sendFileToAll(userId, txt_SendFile.getText(), 
                    		sendFileName, ChatFrame.this);
                } else {
					
                	fileClinetService.sendFileToOne(userId, getterId, txt_SendFile.getText(), 
                			sendFileName, ChatFrame.this);
				}
                
                txt_SendFile.setText("");
                sendFileName = null;
            }
        	
		});

    }
    
    
/*   get  set ����   */
	public JTextArea getTxt_Chat() {
		return txt_Chat;
	}

	public void setTxt_Chat(JTextArea txt_Chat) {
		this.txt_Chat = txt_Chat;
	}

	public JTextArea getTxt_Send() {
		return txt_Send;
	}

	public void setTxt_Send(JTextArea txt_Send) {
		this.txt_Send = txt_Send;
	}

	public JTextField getTxt_SendFile() {
		return txt_SendFile;
	}

	public void setTxt_SendFile(JTextField txt_SendFile) {
		this.txt_SendFile = txt_SendFile;
	}
	
	public static void main(String[] args) {
		new ChatFrame("123", "123");
	}
}


