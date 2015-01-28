package idlerpg.main;

import idlerpg.lib.Methods;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author Juhan Trink
 * 
 */

public class Character {
	
	public Character(IdleBot bot) {
	}
	
	public static String event() throws IOException { // for one online player, either gives an event or makes two players fight
		ArrayList<String> onlineChars = findOnline();
		String char1 = onlineChars.get((int) (Math.random() * onlineChars.size()));
		List<String> stats1 = Files.readAllLines(Paths.get("./characters/" + char1 + ".stats"), StandardCharsets.UTF_8);
		int[] charStats1 = new int[10];
		for (int i = 0; i < charStats1.length; i++) {
			charStats1[i] = Integer.parseInt(stats1.get(i));
		}
		if (Math.random() >= 0.5) { // >=.5 event, <.5 fight
			if (Math.random() >= 0.5) {
				List<String> goodEvents = Files.readAllLines(Paths.get("goodevents"), StandardCharsets.UTF_8); // good event
				int xp = (int) Math.round(0.1 * (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1]));
				charStats1[2] = charStats1[2] + xp;

				PrintWriter out = new PrintWriter("./characters/" + char1 + ".stats");
				for (int i = 0; i < charStats1.length; i++) { // rewrite the file with new information
					out.println(charStats1[i]);
				}
				out.close();

				return char1 + goodEvents.get((int) (Math.random() * goodEvents.size())) + "! This godsend gave him " + xp + " experience, leaving him " + timeToLevel(charStats1[2], (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1])) + " from leveling up.";
			} else {
				List<String> badEvents = Files.readAllLines(Paths.get("badevents"), StandardCharsets.UTF_8); // bad event
				int xp = (int) Math.round(0.1 * (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1]));
				charStats1[2] = charStats1[2] - xp;

				if (charStats1[2] < 0) { // xp cant be negative
					charStats1[2] = 0;
				}

				PrintWriter out = new PrintWriter("./characters/" + char1 + ".stats");
				for (int i = 0; i < charStats1.length; i++) { // rewrite the file with new information
					out.println(charStats1[i]);
				}
				out.close();
				return char1 + badEvents.get((int) (Math.random() * badEvents.size())) + ". This setback cost him " + xp + " experience, leaving him " + timeToLevel(charStats1[2], (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1])) + " from leveling up.";
			}
		} else { // FIGHT
			onlineChars.remove(char1); // remove char1 from online to perpare to fight
			if (onlineChars.isEmpty()) {
				return char1 + " tried to find someone to fight but he was all alone.";
			}
			String char2 = onlineChars.get((int) (Math.random() * onlineChars.size()));

			List<String> stats2 = Files.readAllLines(Paths.get("./characters/" + char2 + ".stats"), StandardCharsets.UTF_8);
			int itemSum1 = 0;
			int itemSum2 = 0;

			for (int i = 3; i < 10; i++) {
				itemSum1 = itemSum1 + Integer.parseInt(stats1.get(i));
			}
			for (int i = 3; i < 10; i++) {
				itemSum2 = itemSum2 + Integer.parseInt(stats2.get(i));
			}
			int roll1 = (int) (Math.random() * itemSum1);
			int roll2 = (int) (Math.random() * itemSum2);
			if (roll1 >= roll2) {
				int xp = (int) Math.round(0.1 * (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1])) + 25 * Integer.parseInt(stats2.get(1));
				charStats1[2] = charStats1[2] + xp;

				PrintWriter out = new PrintWriter("./characters/" + char1 + ".stats");
				for (int i = 0; i < charStats1.length; i++) { // rewrite the file with new information
					out.println(charStats1[i]);
				}
				out.close();
				if (roll1 >= itemSum1 * 0.85) { // if you roll really well, you critical your opponent
					int[] charStats2 = new int[10];
					for (int j = 0; j < charStats2.length; j++) {
						charStats2[j] = Integer.parseInt(stats2.get(j));
					}
					int lostxp = (int) Math.round(0.1 * (400 * (charStats2[1] * charStats2[1]) + 360 * charStats2[1])) + 25 * charStats1[1];
					charStats2[2] = charStats2[2] - lostxp;

					if (charStats2[2] < 0) { // xp cant be negative
						charStats2[2] = 0;
					}

					PrintWriter loserOut = new PrintWriter("./characters/" + char2 + ".stats");
					for (int i = 0; i < charStats2.length; i++) { // rewrite the file with new information
						loserOut.println(charStats2[i]);
					}
					loserOut.close();
					return char1 + " [" + roll1 + "/" + itemSum1 + "] fought " + char2 + " [" + roll2 + "/" + itemSum2 + "] in battle and won, gaining " + xp + " experience, leaving him " + timeToLevel(charStats1[2], (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1]))
							+ " from leveling up. " + char2 + " also suffered a critical hit, costing them " + lostxp + " experience.";
				} else {
					return char1 + " [" + roll1 + "/" + itemSum1 + "] fought " + char2 + " [" + roll2 + "/" + itemSum2 + "] in battle and won, gaining " + xp + " experience, leaving him " + timeToLevel(charStats1[2], (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1]))
							+ " from leveling up.";

				}
			} else {
				int xp = (int) Math.round(0.1 * (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1])) + 25 * Integer.parseInt(stats2.get(1));
				charStats1[2] = charStats1[2] - xp;

				if (charStats1[2] < 0) { // xp cant be negative
					charStats1[2] = 0;
				}

				PrintWriter out = new PrintWriter("./characters/" + char1 + ".stats");
				for (int i = 0; i < charStats1.length; i++) { // rewrite the file with new information
					out.println(charStats1[i]);
				}
				out.close();
				return char1 + " [" + roll1 + "/" + itemSum1 + "] fought " + char2 + " [" + roll2 + "/" + itemSum2 + "] in battle and lost, costing him " + xp + " experience, leaving him "
						+ timeToLevel(charStats1[2], (400 * (charStats1[1] * charStats1[1]) + 360 * charStats1[1])) + " from leveling up.";
			}
		}

	}

	public static String experience(IdleBot bot) throws IOException { // gives out experience, checks for levelups
		String msg = null;

		ArrayList<String> onlineChars = findOnline();
		for (int i = 0; i < onlineChars.size(); i++) {
			List<String> stats = Files.readAllLines(Paths.get("./characters/" + onlineChars.get(i) + ".stats"), StandardCharsets.UTF_8);
			int[] charStats = new int[10];
			for (int j = 0; j < charStats.length; j++) {
				charStats[j] = Integer.parseInt(stats.get(j));
			}
			charStats[2] = charStats[2] + 10; // add 10 experience every timescale, default 1 xp per second
			if (charStats[2] >= (int) ((400 * (charStats[1] * charStats[1]) + 360 * charStats[1]))) { // compare current xp to levelup xp
				charStats[2] = charStats[2] - (400 * (charStats[1] * charStats[1]) + 360 * charStats[1]); // remove xp to level from total xp
				charStats[1]++; // increase level by one
				msg = levelUpMessage(onlineChars.get(i), charStats[1]);
				int itemToReplace = (int) (Math.round((Math.random() * 6) + 3)); // replace an item in character.stats slot 3 to 9
				int compare = charStats[itemToReplace]; // remember the old item
				charStats[itemToReplace] = findItem(charStats[itemToReplace], charStats[1]); // possibly replace an item in a random slot
				if (compare < charStats[itemToReplace]) {
					foundItem(onlineChars.get(i), itemToReplace, charStats[itemToReplace], bot); // send a PM if a new item was found
				}
			}

			PrintWriter out = new PrintWriter("./characters/" + onlineChars.get(i) + ".stats");
			for (int j = 0; j < charStats.length; j++) { // rewrite the file with new information
				out.println(charStats[j]);
			}
			out.close();
		}
		return msg;

	}

	public static ArrayList<String> findOnline() throws IOException { // finds all the online players for other Character methods
		List<String> allChars = Files.readAllLines(Paths.get("charlist"), StandardCharsets.UTF_8);
		ArrayList<String> onlineChars = new ArrayList<String>();
		for (int i = 0; i < allChars.size(); i++) {
			Scanner scan = new Scanner(Paths.get("./characters/" + allChars.get(i) + ".stats"));
			int onlineStatus = scan.nextInt();
			scan.close();
			if (onlineStatus == 1) {
				onlineChars.add(allChars.get(i));
			}
		}
		return (onlineChars);

	}

	private static String levelUpMessage(String charname, int newLevel) throws IOException { // levelup message
		List<String> info = Files.readAllLines(Paths.get("./characters/" + charname), StandardCharsets.UTF_8);
		return charname + " is now a level " + newLevel + " " + info.get(1) + "!";

	}

	private static int findItem(int itemLevel, int charLevel) {
		int item = itemLevel;
		int newItem = (int) Math.round((Math.random() * (charLevel * 1.3 - charLevel * 0.8) + charLevel * 0.8));
		if (newItem > item) {
			item = newItem;
		}
		return item;

	}

	public static String charInfo(String charname) throws IOException { // send all info about a character (name, class, ttl, itemscores), to be called from Commands
		List<String> info = Files.readAllLines(Paths.get("./characters/" + charname), StandardCharsets.UTF_8);
		List<String> stats = Files.readAllLines(Paths.get("./characters/" + charname + ".stats"), StandardCharsets.UTF_8);
		int level = Integer.parseInt(stats.get(1));
		int xpToLevel = (400 * (level * level) + 360 * level);
		int itemSum = 0;
		for (int i = 3; i < 10; i++) {
			itemSum = itemSum + Integer.parseInt(stats.get(i));
		}
		return charname + ", the level " + stats.get(1) + " " + info.get(1) + ". [" + stats.get(2) + "/" + xpToLevel + "], " + timeToLevel(Integer.parseInt(stats.get(2)), xpToLevel) + " to next level. Helmet: " + stats.get(3) + " Chest: " + stats.get(4) + " Gloves: "
				+ stats.get(5) + " Pants: " + stats.get(6) + " Boots: " + stats.get(7) + " Weapon: " + stats.get(8) + " Oar: " + stats.get(9) + " Total: " + itemSum;
	}

	private static String timeToLevel(int currentXp, int xpRequired) {
		long ttl = 1000 * (xpRequired - currentXp);
		if (ttl <= 0) {
			return "a few seconds";
		} else {
		return Methods.readableTime(ttl);
		}

	}
	
	public static String onlineChars() throws IOException {
		ArrayList<String> chars = findOnline();
		String message = chars.get(0);
		for (int i = 1; i < chars.size(); i++) {
			message = message + ", " + chars.get(i);
		}

		return message;
	}
	
	public static void foundItem(String charname, int itemSlot, int itemLevel, IdleBot bot) throws IOException { // send a private message upon finding a new item
		String message = "Your character "+ charname + " has found a level ";
		Map<Integer, String> itemSlots = new HashMap<Integer, String>();
		itemSlots.put(3, "helmet");
		itemSlots.put(4, "chest armor");
		itemSlots.put(5, "gloves");
		itemSlots.put(6, "pants");
		itemSlots.put(7, "boots");
		itemSlots.put(8, "weapon");
		itemSlots.put(9, "oar");
		message = message + itemLevel + " " + itemSlots.get(itemSlot) + "!"; 
		List<String> info = Files.readAllLines(Paths.get("./characters/" + charname), StandardCharsets.UTF_8);
		bot.sendMessage(info.get(4), message);
	}

	public static String topChars(String howMany) throws IOException { // returns the 3 highest level characters
		int topSpots = 0;
		List<String> allChars = Files.readAllLines(Paths.get("charlist"), StandardCharsets.UTF_8);
		
		if (!Methods.isParsable(howMany) || Integer.parseInt(howMany) <= 0) {
			topSpots = 3;
		} else {
			topSpots = Integer.parseInt(howMany);
		}		
		if (topSpots > allChars.size()) {
			topSpots = allChars.size();
		}
		
		String message = "The top " + topSpots + ": ";
		ArrayList<Long> totalXpList = new ArrayList<Long>();
		Map<Long, String> xpMap = new HashMap<Long, String>();
		
		for (int i = 0; i < allChars.size(); i++) { // add all total experience values to the list
			List<String> currentChar = Files.readAllLines(Paths.get("./characters/" + allChars.get(i) + ".stats"), StandardCharsets.UTF_8);
			long totalXp = Long.parseLong(currentChar.get(2));
			for (int x = Integer.parseInt(currentChar.get(1)) - 1; x > 0; x--) {
				totalXp = totalXp + 400 * (x * x) + 360 * x;
			}
			totalXpList.add(totalXp);
			xpMap.put(totalXp, allChars.get(i)); // tie total xp with a character name
		}
		
		Collections.sort(totalXpList, Collections.reverseOrder()); // descending sort on the list

		for (int i = 0; i < topSpots; i++) { // find and return the top 3
			long xpToFormat = totalXpList.get(i);
			String formattedXp = String.format("%,d", xpToFormat);
			List<String> stats = Files.readAllLines(Paths.get("./characters/" + xpMap.get(totalXpList.get(i)) + ".stats"), StandardCharsets.UTF_8);
			int itemSum = 0;
			for (int j = 3; j < 10; j++) {
				itemSum = itemSum + Integer.parseInt(stats.get(j));
			}
			message = message + (i + 1) + ". " + xpMap.get(totalXpList.get(i)) + " [" + formattedXp + " xp] [" + itemSum + " ilvl] ";	
		}
		
		return message;
	}

}
