package idlerpg.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/** 
 * settings import based on tutorial examples by DeadEd ( http://www.deaded.com ) @ 2006
 * bot built on PircBot by www.jibble.org
 * @author Juhan Trink
 * 
 */
public class IdleBotMain {
	public static void main(String[] args) throws Exception {
		Properties config = new Properties();
		try {
			config.load(new FileInputStream("idlerpg.properties"));
		} catch (IOException ioex) {
			System.err.println("Error loading config file: idlerpg.properties");
			System.exit(0);
		}
		IdleBot bot = new IdleBot();
		bot.setVerbose(true);
		bot.setMessageDelay(10);
		bot.setEncoding("UTF-8");
		bot.connect(config.getProperty("server"), 6667);
		bot.joinChannel("#\u00f5lidnd");
		new Character(bot);
	}

}
