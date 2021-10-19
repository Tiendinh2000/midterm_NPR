package main;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.border.TitledBorder;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class ChatPanel extends JPanel {

	Socket socket = null;
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread t = null;
	String sender;
	String receiver;
	JTextArea txtMessages;

	/**
	 * Create the panel.
	 */
	public ChatPanel(Socket socket, String sender, String receiver) {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();

		JTextPane txtMessage = new JTextPane();
		scrollPane.setViewportView(txtMessage);

		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, BorderLayout.CENTER);

		txtMessages = new JTextArea();
		scrollPane_1.setViewportView(txtMessages);

		txtMessages.setForeground(Color.BLUE);
		txtMessages.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
		txtMessage.setFont(new Font("Monaco", Font.ITALIC, 13));

		JButton btnSend = new JButton("Send");
		ImageIcon icon = new ImageIcon(ChatPanel.class.getResource("/assets/sent.png"));

		btnSend.setIcon(resizeIcon(icon, 30, 30));

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("message" + txtMessage.getText());
				if (txtMessage.getText().trim().length() == 0) {
					return;
				}
				try {
					os.writeBytes(txtMessage.getText());
					os.write(13);// line break
					os.write(10);
					os.flush();
					txtMessage.setForeground(Color.BLUE);
					txtMessages.append("\n" + sender + ": " + txtMessage.getText());

					txtMessage.setText("");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});

		/// Set layout for message Panel
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE));
		panel.setLayout(gl_panel);
         ///

		this.socket = socket;
		this.sender = sender;
		this.receiver = receiver;
		try {
			bf = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			os = new DataOutputStream(this.socket.getOutputStream());
			t = new OutputThread(socket, txtMessages, sender, receiver);

			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JTextArea getTxtMessges() {
		return this.txtMessages;
	}

	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}
}
