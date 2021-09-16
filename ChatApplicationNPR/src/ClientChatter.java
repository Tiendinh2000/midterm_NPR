import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		setBounds(100, 100, 925, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, "staff and server info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 7, 0, 0));

		JLabel lblNewLabel = new JLabel("Student");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);

		txt_Student1 = new JTextField();
		panel.add(txt_Student1);
		txt_Student1.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("IP:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);

		txt_ip = new JTextField();
		panel.add(txt_ip);
		txt_ip.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Port:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_2);

		txt_port = new JTextField();
		panel.add(txt_port);
		txt_port.setColumns(10);

		JFrame thisFrame = this;

		JButton btn_Connect = new JButton("Connect");
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

						ChatPanel p = new ChatPanel(mngSocket, studentName, studentName);
						thisFrame.getContentPane().add(p);
						p.getTxtMessges().append("Application is running");
						p.updateUI();


						bf = new BufferedReader(new InputStreamReader(mngSocket.getInputStream()));
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

}
