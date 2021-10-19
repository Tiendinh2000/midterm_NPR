package main;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.border.MatteBorder;

public class ClientChatter extends JFrame {

	private JPanel contentPane;
	private JTextField txt_Student1;
	private JTextField txt_ip;
	private JTextField txt_port;

	Socket mngSocket = null;
	String mngIp = null;
	int mngPort = 0;
	String studentName = "";
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread t = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatter frame = new ClientChatter();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public ClientChatter() {
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 651, 517);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(32770, 32767));
		panel.setBorder(
				new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Server-Client infomation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 7, 0, 0));

		JLabel lblNewLabel = new JLabel("Student:");
		lblNewLabel.setFont(new Font("Segoe Print", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);

		txt_Student1 = new JTextField();
		txt_Student1.setFont(new Font("Segoe Print", Font.BOLD, 15));
		panel.add(txt_Student1);
		txt_Student1.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("IP:");
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_1.setFont(new Font("Segoe Print", Font.BOLD, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);

		txt_ip = new JTextField();
		txt_ip.setFont(new Font("Segoe Print", Font.BOLD, 15));
		panel.add(txt_ip);
		txt_ip.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Port:");
		lblNewLabel_2.setFont(new Font("Segoe Print", Font.BOLD, 15));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_2);

		txt_port = new JTextField();
		txt_port.setFont(new Font("Segoe Print", Font.BOLD, 15));
		panel.add(txt_port);
		txt_port.setColumns(10);

		JFrame thisFrame = this;

		JButton btn_Connect = new JButton("Connect");
		btn_Connect.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		ImageIcon icon=new ImageIcon(ClientChatter.class.getResource("/assets/connect.png"));
		btn_Connect.setIcon(resizeIcon(icon,20,20));
		
		btn_Connect.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		btn_Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mngIp = txt_ip.getText();
				System.out.println("IP" + mngIp);
				mngPort = Integer.parseInt(txt_port.getText());
				System.out.println("IP" + mngPort);
				studentName = txt_Student1.getText();
				System.out.println("IP" + studentName);
				try {
					mngSocket = new Socket(mngIp, mngPort);
		
					if (mngSocket != null) {

						ChatPanel p = new ChatPanel(mngSocket, "You", "Admin");
						thisFrame.getContentPane().add(p);
						p.getTxtMessges().append("Application is running");
						p.updateUI();

						bf = new BufferedReader(new InputStreamReader(mngSocket.getInputStream()));
		
						if(bf!=null) {
							System.out.println(bf.toString());
							String s;

						}
						os = new DataOutputStream(mngSocket.getOutputStream());

						
						os.writeBytes("Student: " + studentName);
						os.write(13);// line break
						os.write(10);
						os.flush();
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		panel.add(btn_Connect);
	}
	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}
}
