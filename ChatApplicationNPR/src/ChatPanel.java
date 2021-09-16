import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
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
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		JTextPane txtMessage = new JTextPane();
		scrollPane.setViewportView(txtMessage);

		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, BorderLayout.CENTER);

		txtMessages = new JTextArea();
		scrollPane_1.setViewportView(txtMessages);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("messageL"+txtMessage.getText());
				if (txtMessage.getText().trim().length() == 0) {
					return;}
				try {
					os.writeBytes(txtMessage.getText());
					os.write(13);// line break
					os.write(10);
					os.flush();
				//	<html><font color='red'>rouge</font></html>
					txtMessages.append("\n" + sender + ": " + txtMessage.getText());
					txtMessage.setText("");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		panel.add(btnSend);

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

}
