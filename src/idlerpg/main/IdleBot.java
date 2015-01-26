package idlerpg.main;

import org.jibble.pircbot.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * 
 * @author Juhan Trink
 * 
 *         timer system based on demo by DeadEd ( http://www.deaded.com ) @ 2006
 *
 */

public class IdleBot extends PircBot {

	Timer timer;
	int numQuests;

	public String mainChannel = null;
	public IdleBot bot = this;
	ArrayList<String> questers = new ArrayList<String>();

	public IdleBot() {
		Properties config = new Properties();
		try {
			config.load(new FileInputStream("idlerpg.properties"));
		} catch (IOException ioex) {
			System.err.println("Error loading config file: idlerpg.properties");
			System.exit(0);
		}

		final String configuredChannel = "#\u00f5lidnd";
		mainChannel = configuredChannel;
		this.setName("jarjuut");
		this.setLogin(config.getProperty("login"));
		this.setVersion(config.getProperty("version"));
		int timescale = Integer.parseInt(config.getProperty("timescale"));
		EventTask event = new EventTask();
		ExperienceTask xp = new ExperienceTask();
		QuestTask quest = new QuestTask();

		timer = new Timer();
		timer.schedule(event, 60 * 1000, 22 * 60 * 100 * timescale); // events happen 22 minutes
		timer.schedule(xp, 0, 10 * 100 * timescale); // xp is given out every 10 seconds
		timer.schedule(quest, 180 * 1000, 180 * 60 * 100 * timescale); // quests happen every 3 hours
	}

	class EventTask extends TimerTask {

		public void run() {
			try {
				sendMessage(mainChannel, Character.event());
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	class QuestTask extends TimerTask {

		public void run() {
			try {
				new Quest(bot);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ExperienceTask extends TimerTask {

		public void run() {
			String reply = null;
			try {
				reply = Character.experience(bot);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (reply != null) {
				sendMessage(mainChannel, reply);
			}
		}
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.equalsIgnoreCase("!online")) {
			try {
				sendMessage(channel, Character.onlineChars());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.equals("!quit") && hostname.equals("ffy.users.quakenet.org")) { // admin hostname
			quitServer("shutdown ordered by " + sender);
			System.exit(0);
		} else if (message.equals("!quest")) {
			if (questers.size() == 0) {
				sendMessage(channel, "There are currently no quests underway");
			} else {
				String questerList = questers.get(0);
				for (int i = 1; i < questers.size(); i++) {
					questerList = questerList + ", " + questers.get(i);
				}
				sendMessage(channel, "Currently on quest (ordered from old to new): " + questerList);
			}
		} else {
			String[] splitMessage;
			splitMessage = message.split(" ");
			if (splitMessage[0].equalsIgnoreCase("!top")) {
				if (splitMessage.length < 2) {
					try {
						sendMessage(channel, Character.topChars("3"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						sendMessage(channel, Character.topChars(splitMessage[1]));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		String reply = null;
		try {
			reply = Commands.checkForCommand(sender, login, hostname, message);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (reply != null) {
			sendMessage(sender, reply);
		}

	}

	public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
		try {
			Online.findCharacterLogOut(recipientNick);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onJoin(String channel, String sender, String login, String hostname) {
		if (sender.equalsIgnoreCase(getNick()) == true) {
			// TODO: try to autologin everyone
		} else {
			try {
				System.out.println("trying to log in");
				sendMessage(sender, Online.findCharacterLogIn(hostname, sender));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onPart(String channel, String sender, String login, String hostname) {
		if (sender.equalsIgnoreCase(getNick()) == true) {
			// TODO: rejoin
		} else {
			try {
				Online.findCharacterLogOut(sender);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
		if (sourceNick.equalsIgnoreCase(getNick()) == true) {
			// TODO: log everyone out?
		} else {
			try {
				Online.findCharacterLogOut(sourceNick);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void onNickChange(String oldNick, String login, String hostname, String newNick) {
		try {
			Online.findCharacterLogIn(hostname, newNick); // have to do essentially the same things as when logging in
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addQuester(String quester) {
		questers.add(quester);
	}

	public void removeQuester() {
		questers.remove(0);
	}

}
