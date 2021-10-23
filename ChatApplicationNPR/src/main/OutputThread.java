package main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JTextArea;

public class OutputThread extends Thread {
	Socket socket;
	JTextArea txt;
	BufferedReader bf;
	String sender;
	String receiver;

	public OutputThread(Socket socket, JTextArea txt, String sender, String receiver) {
		super();
		this.socket = socket;
		this.txt = txt;
		this.sender = sender;
		this.receiver = receiver;

		try {
			bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while (true) {
			try {
				if (socket != null) {
					String message = "";
					if ((message = bf.readLine()) != null && message.length() > 0) 
						txt.append("\n"+ receiver+": " + message);
					
				}
				sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
