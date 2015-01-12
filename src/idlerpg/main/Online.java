package idlerpg.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Juhan Trink
 * 
 */


public class Online {
	
	static String checkLogin(String sender, String charname, String password, String hostname) throws IOException {
		List<String> logInInfo;
		try {
			logInInfo = Files.readAllLines(Paths.get("./characters/" + charname), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return "Invalid character name or password";
		}
		if (password.equals(logInInfo.get(2)) == true) {
			return makeOnline(charname, hostname, sender);
		} else {
			return "Invalid character name or password";
		}
	}

	public static String makeOnline(String charname, String hostname, String sender) throws IOException {
		List<String> stats = Files.readAllLines(Paths.get("./characters/" + charname + ".stats"), StandardCharsets.UTF_8);
		int[] charstats = new int[10];
		for (int i = 0; i < charstats.length; i++) {
			charstats[i] = Integer.parseInt(stats.get(i));
		}
		charstats[0] = 1; // first number in stats file determines online status, 1 = online
		PrintWriter out = new PrintWriter("./characters/" + charname + ".stats");
		for (int i = 0; i < charstats.length; i++) {
			out.println(charstats[i]);
		}
		out.close();
		List<String> charFile = Files.readAllLines(Paths.get("./characters/" + charname), StandardCharsets.UTF_8); // read character file
		PrintWriter reWrite = new PrintWriter("./characters/" + charname); // rewrite the character file with last line including current username
		for (int i = 0; i < 3; i++) { // TODO: onNickChange rewrites it again to new username
			reWrite.println(charFile.get(i));
		}
		reWrite.println(hostname);
		reWrite.println(sender);
		reWrite.close();
		return "Logged in as " + charname;
	}

	public static String makeOffline(String charname) throws IOException {
		List<String> stats = Files.readAllLines(Paths.get("./characters/" + charname + ".stats"), StandardCharsets.UTF_8);
		int[] charStats = new int[10];
		for (int i = 0; i < charStats.length; i++) {
			charStats[i] = Integer.parseInt(stats.get(i));
		}
		System.out.println(Arrays.toString(charStats) + " enne muutust"); // TEST
		charStats[0] = 0; // set online status to 0
		System.out.println(Arrays.toString(charStats)); // TEST
		PrintWriter out = new PrintWriter("./characters/" + charname + ".stats");
		for (int i = 0; i < charStats.length; i++) {
			out.println(charStats[i]);
		}
		out.close();
		return "You have been logged out of idle rpg!";
	}

	static String findCharacterLogOut(String sender) throws IOException {
		List<String> allChars = Files.readAllLines(Paths.get("charlist"), StandardCharsets.UTF_8);
		String[] charList = new String[allChars.size()];
		for (int i = 0; i < charList.length; i++) {
			charList[i] = allChars.get(i);
			List<String> currentChar = Files.readAllLines(Paths.get("./characters/" + charList[i]), StandardCharsets.UTF_8);
			if (currentChar.get(4).equals(sender) == true) {
				return makeOffline(charList[i]);
			}
		}
		return null;
	}

	static String findCharacterLogIn(String hostname, String sender) throws IOException {
		List<String> allChars = Files.readAllLines(Paths.get("charlist"), StandardCharsets.UTF_8);
		String[] charList = new String[allChars.size()];
		for (int i = 0; i < charList.length; i++) {
			charList[i] = allChars.get(i);
			List<String> currentChar = Files.readAllLines(Paths.get("./characters/" + charList[i]), StandardCharsets.UTF_8);
			if (currentChar.get(3).equals(hostname) == true) {
				return makeOnline(charList[i], hostname, sender);
			}
		}
		return "Automatic login failed. Log in or register manually.";

	}


}
