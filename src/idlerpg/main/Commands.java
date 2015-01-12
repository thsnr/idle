package idlerpg.main;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Juhan Trink
 *
 */
public class Commands {

	public static String checkForCommand(String sender, String login, String hostname, String message) throws IOException {
		//		IdleBot cmd = new IdleBot();
		//		if (message.equalsIgnoreCase(cmd.getName() + " quit") // quit if admin says so
		//				&& sender.equals("ffy")) {
		//			cmd.shutdown(sender);
		//		}
		//
		//		else {
		String[] splitmessage; // always split up the message
		splitmessage = message.split(" ");
		if (splitmessage[0].equalsIgnoreCase("register")) { // register a new character
			if (splitmessage.length != 4) { // check if command syntax is correct
				return "To register, enter 'register <character name> <character class> <password>'";
			} else {
				String charname = splitmessage[1];
				String chclass = splitmessage[2];
				String password = splitmessage[3];
				File f = new File("./characters/" + charname);
				if (f.exists()) {
					return "Character with that name already exists.";
				} else {
					return Registration.makeChar(sender, charname, chclass, password, hostname);
				}
			}
		} else if (splitmessage[0].equalsIgnoreCase("login")) {
			if (splitmessage.length != 3) { // check if command syntax is correct
				System.out.println(splitmessage.length);
				return "To log in, enter 'login <character name> <password>'";
			} else {
				String charname = splitmessage[1];
				String password = splitmessage[2];
				return Online.checkLogin(sender, charname, password, hostname);
			}
		} else if (splitmessage[0].equalsIgnoreCase("charinfo")) {
			if (splitmessage.length != 2) {
				return "To see character information, use 'charinfo <character name>'";
			} else {
				String charname = splitmessage[1];
				return Character.charInfo(charname);
			}
		}

		//		}
		return null;
	}

}
// else if (splitmessage[0].equalsIgnoreCase("login")) { // log in with old

// character
// List<String> lines = Files.readAllLines(
// Paths.get(splitmessage[1]), StandardCharsets.UTF_8);
// if (lines.get(1) == splitmessage[2]) {
// reply = login(splitmessage[1], sender, hostname);
// }
// else {
// reply = "Incorrect character name or password";
// }
// }
// }
// return reply;
// }

// private static String login(String charname, String sender, String
// hostname) {
//
// return "Logged in as " + charname;
// }

// public void onMessage(String channel, String sender, String login,
// String hostname, String message) {
//
// if (message.equalsIgnoreCase("tsau")) {
// sendMessage(channel, "tsau " + sender);
// }
// if (message.equalsIgnoreCase(getName() + " quit")
// && sender.equals("ffy")) {
// shutdown(sender);
// }
// }

// public void check(String channel, String sender, String login,
// String hostname, String message) {
// MyBot msg = new MyBot();
// msg.sendMessage(channel, "commanded");
// if (message.equalsIgnoreCase("tsau")) {
// msg.sendMessage(channel, "tsau " + sender);
// }
//
// }

