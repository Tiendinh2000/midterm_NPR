import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManagerChatter extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField txtPort;
	private JTabbedPane tabbedPane;

	ServerSocket serverSocket = null;
	BufferedReader bf = null;
	Thread t = null;
	private JLabel lblNewLabel;
	private JTextField txt_SName;
	String sName;
	int port;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerChatter frame = new ManagerChatter();
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
	public ManagerChatter() {

		try {
			JTextField SName = new JTextField();
			JTextField port = new JTextField();

			final JComponent[] inputs = new JComponent[] { new JLabel("Your name"), SName, new JLabel("port"), port };
			int result = JOptionPane.showConfirmDialog(null, inputs, "Yout information", JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				this.sName = SName.getText();
				this.port = Integer.parseInt(port.getText());
			} else {
				System.out.println("User canceled / closed the dialog, result = " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(2, 2, 0, 0));

		JLabel txtServerPort = new JLabel("Server Port:");
		panel.add(txtServerPort);

		txtPort = new JTextField();
		txtPort.setText(String.valueOf(this.port));
		panel.add(txtPort);
		txtPort.setColumns(10);

		lblNewLabel = new JLabel("Your name");
		panel.add(lblNewLabel);

		txt_SName = new JTextField();
		panel.add(txt_SName);
		txt_SName.setColumns(10);
		txt_SName.setText(this.sName);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		this.setSize(600, 300);

		try {
			serverSocket = new ServerSocket(port);

		} catch (Exception e) {
			e.printStackTrace();
		}
		t = new Thread(this);
		t.start();
	}

	public void run() {

		while (true) {
			try {
				Socket aSocket = serverSocket.accept();
				if (aSocket != null) {
					bf = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
					String s = bf.readLine();
					int post = s.indexOf(":");
					String sName = s.substring(post + 1);
					ChatPanel p = new ChatPanel(aSocket, this.sName, sName);
					tabbedPane.add(sName, p);
					p.updateUI();
				}
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
