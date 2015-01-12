package idlerpg.lib;

/** 
 * 
 * @author Juhan Trink
 *	based on a stackoverflow answer
 *
 */

public class Methods {
	
	public static String readableTime (long time) {
		
		final long SECOND = 1000;
		final long NUM_SECONDS = 60;
//		final long MINUTE = NUM_SECONDS * SECOND;
		final long NUM_MINUTES = 60;
//		final long HOUR = NUM_MINUTES * MINUTE;
		final long NUM_HOURS = 24;
//		final long DAY = NUM_HOURS * HOUR;
		
		String humanTime = "";
		
		time /= SECOND;
		int seconds = (int) (time % NUM_SECONDS);
		time /= NUM_SECONDS;
		int minutes = (int) (time % NUM_MINUTES);
		time /= NUM_MINUTES;
		int hours = (int) (time % NUM_HOURS);
		int days = (int) (time / NUM_HOURS);
		
		if (days == 0) {
		      humanTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		    } else {
		      humanTime = String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds);
		    }
		
		
		return humanTime;
		
	}
	
	public static boolean isParsable(String input){
	    boolean parsable = true;
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        parsable = false;
	    }
	    return parsable;
	}

}
