package idlerpg.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @author Juhan Trink
 *
 */

public class Registration {

	public static String makeChar(String sender, String charname, String chclass, String password, String hostname) throws IOException {

		PrintWriter out = new PrintWriter("./characters/" + charname);
		out.println(charname);
		out.println(chclass);
		out.println(password);
		out.println(hostname);
		out.println(sender);
		out.close();
		PrintWriter statsfile = new PrintWriter("./characters/" + charname + ".stats");
		for (int i = 0; i < 10; i++) { // make a new charname.stats file with 1 in every slot, resulting in a level 1 character with 1 xp in noob gear
			statsfile.println(1);
		}
		statsfile.close();

		try (PrintWriter output = new PrintWriter(new FileWriter("charlist", true))) {
			output.printf("%s\r\n", charname);
		} catch (Exception e) {
		}
		Online.makeOnline(charname, hostname, sender);
		return "Character " + charname + " the " + chclass + " with password " + password + " registered.";

	}

}
