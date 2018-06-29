package Client;

import java.io.InputStream;
import java.util.Scanner;

public class ReceivedMessagesHandler implements Runnable {
	private InputStream serverInputStream;
	private Client client;

	public ReceivedMessagesHandler(InputStream serverInputStream, Client client) {
		this.serverInputStream = serverInputStream;
		this.client = client;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(serverInputStream);
		String message = "";
		while (sc.hasNextLine()) {
			message = sc.nextLine();
			if (message.length() > 0) {
				// if server send username
				if (message.charAt(0) == '@') {
					this.client.setUserChatWith(message.substring(1));
				} else {
					System.out.println(message);
				}
			}
		}
		sc.close();

	}

}
