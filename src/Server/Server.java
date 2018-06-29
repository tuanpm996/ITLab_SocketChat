package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
	private int port;
	private List<User> clients;
	private ServerSocket server;

	public List<User> getClients() {
		return this.clients;
	}

	public static void main(String[] args) throws IOException {
		new Server(12345).run();
	}

	public Server(int port) {
		this.port = port;
		this.clients = new ArrayList<User>();
	}

	public void run() throws IOException {
		try {
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Port 12345 is now open");
		while (true) {
			Socket client = server.accept();
			Scanner sc = new Scanner(client.getInputStream());
			String nickname = sc.nextLine();
			
			User newUser = new User(client, nickname);
			this.clients.add(newUser);
			
			StringBuilder sb = new StringBuilder();
			sb.append("Hello ");
			sb.append( newUser.getNickname());
			sb.append("\nList users:\n");
			for (User user : clients) {
				sb.append(user.getNickname());
				sb.append("-");
				sb.append(user.getUserId());
				sb.append("\n");
			}
			sb.append("Type @ + id of person you want to chat with: \n");
			sb.append("Type & to know users list: \n");
			newUser.getOutStream().println(sb.toString());
			
			new Thread(new UserHandler(this, newUser)).start();
		}
	}

	public void sendMessage(User sender, String msg) {
		sender.getInteractingUser().getOutStream().println(sender.getNickname() + " messaged you: " + msg);
	}

	public void removeUser(User user) {
		this.clients.remove(user);
	}

}
