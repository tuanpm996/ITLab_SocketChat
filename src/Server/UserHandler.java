package Server;

import java.util.Scanner;

public class UserHandler implements Runnable {

	private Server server;
	private User user;

	public UserHandler(Server server, User user) {
		this.server = server;
		this.user = user;
	}

	@Override
	public void run() {
		String message;

		Scanner sc = new Scanner(this.user.getInputStream());
		while (sc.hasNextLine() && !(message = sc.nextLine()).equals("")) {
			switch (message.charAt(0)) {
			//choose person to chat with
			case '@':
				try {
					int userId = Integer.parseInt(message.substring(1));
					for (User client : this.server.getClients()) {
						if (client.getUserId() == userId) {
							this.user.setInteractingUser(client);
							this.user.getOutStream().println("@" + client.getNickname());
						}
					}
					if (this.user.getInteractingUser() == null) {
						this.user.getOutStream().println("User doesn't exist: ");
					}
				} catch (NumberFormatException e) {
					this.user.getOutStream().println("It's not a id, enter again: ");
				}
				break;
			//get users list
			case '&':
				StringBuilder sb = new StringBuilder();
				sb.append("List users:\n");
				for (User user : this.server.getClients()) {
					sb.append(user.getNickname());
					sb.append("-");
					sb.append(user.getUserId());
				}
				sb.append("\nType @ + id of person you want to chat with: \n");
				this.user.getOutStream().println(sb.toString());
				break;
			
			//send normal message
			default:
				if (this.user.getInteractingUser() != null) {
					this.server.sendMessage(this.user, message);
				} else {
					this.user.getOutStream().println("You must select people to chat with");
				}
				break;
			}
		}
		System.out.println("End user handler thread");
		User.nbUser -= 1;
		server.removeUser(user);
		sc.close();
	}

}
