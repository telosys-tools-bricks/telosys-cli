package org.telosys.tools.cli;

public class Color {

	// see : http://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html
	public final static String BALCK = "30" ;
	
	public final static String BLUE         = "34" ;
	public final static String BLUE_BRIGHT  = "34;1" ;
	
	public final static String CYAN         = "36" ;
	public final static String CYAN_BRIGHT  = "36;1" ;
	
	public final static String GREEN        = "32" ;
	public final static String GREEN_BRIGHT = "32;1" ;
	
	public final static String MAGENTA        = "35" ;
	public final static String MAGENTA_BRIGHT = "35;1" ;
	
	public final static String RED            = "31" ;
	public final static String RED_BRIGHT     = "31;1" ;
			
	public final static String YELLOW         = "33" ;
	public final static String YELLOW_BRIGHT  = "33;1" ;
			
	public final static String colorize(String s, String color) {
		return "\u001b[" + color + "m" 
				+ s
				+ "\u001b[" + 0 + "m" ;  // Reset
	}
}
