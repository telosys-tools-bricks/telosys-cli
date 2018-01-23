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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Application entry point for CLI
 * 
 * @author Laurent GUERIN
 *
 */
public class LaunchArgumentsProcessor {
	
	private final PrintWriter out ;

	private final CommandProvider commandProvider ;
	
	/**
	 * Constructor
	 * @throws IOException
	 */
	public LaunchArgumentsProcessor(CommandProvider commandProvider, PrintWriter out) {
		this.commandProvider = commandProvider ;
		this.out = out ;
	}
	
	/**
	 * Processes the launch arguments if any <br>
	 * eg : -h homedir 
	 * @param args
	 */
	protected void processLaunchArguments(String[] args) {
		try {
			processArguments(args);
		} catch (Exception e) {
			print("ERROR : Unexpected exception " + e.getMessage() );
		}
	}
	
	private void print(String message) {
		out.println(message);
		out.flush();
	}

	/**
	 * Processes the arguments if any <br>
	 * eg : -h homedir 
	 *  
	 * @param args
	 */
	private void processArguments(String[] args) {
		if ( args.length > 0 ) {
			if ( Trace.DEBUG ) {
				print("");
				printArguments(args);
			}
		}
		// Is there a "-h homedir" argument ?
		File home = getHomeArg(args);
		if ( home != null ) {
			if ( Trace.DEBUG ) {
				print("Setting 'home' to '" + home.getAbsolutePath() + "'") ;
			}

			// Set current working directory : command "cd path" 
			Command cdCommand = commandProvider.getCommand("cd");
			String[] cdArgs = buildArgs(cdCommand, home.getAbsolutePath());
			cdCommand.execute(cdArgs);
			
			// Set 'HOME' : command "h path"
			Command homeCommand = commandProvider.getCommand("h");
			String[] hArgs = buildArgs(cdCommand, home.getAbsolutePath());
			String r = homeCommand.execute( hArgs );
			
			print(r) ; // Prints the "h" command result : Home set ('xxx')			
		}
	}
	
	private String[] buildArgs(Command command, String homePath) {
		String[] args = new String[2] ;
		args[0] = command.getName();
		args[1] = homePath;
		return args;
	}
	
	private void printArguments(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("args : ");
		for (String s : args ) {
			sb.append(s);
			sb.append(" ");
		}
		print(sb.toString());
	}
	
	/**
	 * Returns a File pointing on the "-h" given folder (or null if none)
	 * @param args
	 * @return
	 */
	private File getHomeArg(String[] args) {
		// args do not containts the initial command ( eg "java -jar xxx" ) 
		// so the usefull arguments starts at ZERO 
		for ( int i = 0 ; i < args.length ; i++ ) {
			String arg = args[i];
			if ( "-h".equals(arg) ) {
				String home = getNextArg(args, i);
				if ( home != null ) {
					File file = new File(home);
					if ( file.exists() && file.isDirectory() ) {
						// OK, home argument is valid
						return file ;
					}
					else {
						print("Invalid 'home' argument : '" + home + "'");
					}
				}
			}
		}
		return null ;
	}

	private String getNextArg(String[] args, int i) {
		if ( i+1 < args.length ) {
			return args[i+1] ;
		}
		else {
			return null ;
		}
	}

}
