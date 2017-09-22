/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
