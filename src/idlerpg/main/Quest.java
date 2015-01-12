package idlerpg.main;

import idlerpg.lib.Methods;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Quest {

	Timer questTimer;
	String quester;

	public Quest(IdleBot bot) throws IOException {
		questTimer = new Timer();
		ArrayList<String> onlineChars = Character.findOnline();
		quester = onlineChars.get((int) (Math.random() * onlineChars.size()));
		System.out.println(quester + " on quest");

		List<String> stats = Files.readAllLines(Paths.get("./characters/" + quester + ".stats"), StandardCharsets.UTF_8);
		int questLength = (Integer.parseInt(stats.get(1)) * Integer.parseInt(stats.get(1))) * 60 * 1000; // quest takes level^2 minutes to complete
		System.out.println(questLength);

		if (Math.random() <= 0.8) {
			questTimer.schedule(new RegularQuest(bot), questLength, questLength * 4);
			bot.sendMessage(bot.getChannels()[0], quester + " has just gone on a quest to gain experience. He should be done in " + Methods.readableTime(questLength) + ".");
		} else {
			questTimer.schedule(new ItemQuest(bot), questLength, questLength * 4);
			bot.sendMessage(bot.getChannels()[0], quester + " has just gone on a quest to find a new item. He should be done in " + Methods.readableTime(questLength) + ".");
		}
	}

	class RegularQuest extends TimerTask {

		IdleBot bot;
		
		public RegularQuest(IdleBot bot) {
			this.bot = bot;
		}

		public void run() {
			try {
				xpQuest(quester, bot);
			} catch (IOException e) {
				e.printStackTrace();
			}
			questTimer.cancel();
		}
	}

	class ItemQuest extends TimerTask {
		IdleBot bot;

		public ItemQuest(IdleBot bot) {
			this.bot = bot;
		}

		public void run() {
			try {
				itemQuest(quester, bot);
			} catch (IOException e) {
				e.printStackTrace();
			}
			questTimer.cancel();
		}
	}

	private static void xpQuest(String quester, IdleBot bot) throws IOException {
		if (!Character.onlineChars().contains(quester)) {
			bot.sendMessage(bot.getChannels()[0], quester + " has failed his quest. He is now sad.");
		} else {
			List<String> stats = Files.readAllLines(Paths.get("./characters/" + quester + ".stats"), StandardCharsets.UTF_8);
			List<String> quests = Files.readAllLines(Paths.get("quests"), StandardCharsets.UTF_8);
			int[] charStats = new int[10];
			for (int i = 0; i < charStats.length; i++) {
				charStats[i] = Integer.parseInt(stats.get(i));
			}
			int xp = (int) Math.round(((Math.random() * 0.4) + 0.1) * (400 * (charStats[1] * charStats[1]) + 360 * charStats[1]));
			charStats[2] = charStats[2] + xp;
			PrintWriter out = new PrintWriter("./characters/" + quester + ".stats");
			for (int j = 0; j < charStats.length; j++) { // rewrite the file with new information
				out.println(charStats[j]);
			}
			out.close();
			bot.sendMessage(bot.getChannels()[0], quester + " has managed to " + quests.get((int) (Math.random() * quests.size())) + "! This task gave him " + xp + " experience."); 
		}
	}

	private static void itemQuest(String quester, IdleBot bot) throws IOException {
		if (!Character.onlineChars().contains(quester)) {
			bot.sendMessage(bot.getChannels()[0], quester + " has failed his quest. He is now sad.");
		} else {
			List<String> quests = Files.readAllLines(Paths.get("quests"), StandardCharsets.UTF_8);
			List<String> stats = Files.readAllLines(Paths.get("./characters/" + quester + ".stats"), StandardCharsets.UTF_8);
			int[] charStats = new int[10];
			for (int i = 0; i < charStats.length; i++) {
				charStats[i] = Integer.parseInt(stats.get(i));
			}
			Random addedValue = new Random();
			int itemToReplace = (int) (Math.round((Math.random() * 6) + 3));
			charStats[itemToReplace] = charStats[itemToReplace] + addedValue.nextInt((int) (charStats[1]/2)) + 1;
			
			PrintWriter out = new PrintWriter("./characters/" + quester + ".stats");
			for (int j = 0; j < charStats.length; j++) { // rewrite the file with new information
				out.println(charStats[j]);
			}
			out.close();
			
			Map<Integer, String> itemSlots = new HashMap<Integer, String>();
			itemSlots.put(3, "helmet");
			itemSlots.put(4, "chest armor");
			itemSlots.put(5, "gloves");
			itemSlots.put(6, "pants");
			itemSlots.put(7, "boots");
			itemSlots.put(8, "weapon");
			itemSlots.put(9, "oar");
			
			bot.sendMessage(bot.getChannels()[0], quester + " has managed to " + quests.get((int) (Math.random() * quests.size())) + "! This task gave him a level " + charStats[itemToReplace] + " "  + itemSlots.get(itemToReplace) + "!"); 
		}	
	}

}
