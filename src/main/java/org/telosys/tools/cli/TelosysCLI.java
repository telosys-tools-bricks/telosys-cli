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

import jline.console.ConsoleReader;

/**
 * Application entry point for CLI
 * 
 * @author Laurent GUERIN
 *
 */
public class TelosysCLI {

	private final PrintWriter out ;
	private final ConsoleReader consoleReader ;
	private final CommandLineProcessor commandLineProcessor;
	
	/**
	 * Constructor
	 * @throws IOException
	 */
	public TelosysCLI() throws IOException {
		super();
		this.consoleReader = new ConsoleReader() ;
		this.out = new PrintWriter(consoleReader.getOutput());
		CommandProvider commandProvider = new CommandProvider(consoleReader);
		this.commandLineProcessor = new CommandLineProcessor( consoleReader, commandProvider ) ;
	}
	
	/**
	 * Strats Telosys CLI
	 * @param args
	 */
	public void start(String[] args) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("args : ");
		for (String s : args ) {
			sb.append(s);
			sb.append(" ");
		}
		print(sb.toString());
		
		// Initialize the CLI
		init();		
		// Process launch arguments if any
		processArguments(args);
		// Launch the line processor...
		launch();
	}
	
	private void print(String message) {
		out.println(message);
		out.flush();
	}

	/**
	 * Initializes the CLI 
	 */
	private void init() {
		// set the initial prompt
		consoleReader.setPrompt(Color.colorize(Const.INITIAL_PROMPT, Const.PROMPT_COLOR) );
		// print the banner
		String banner = new Banner().bannerSlant();
		print(Color.colorize(banner, Color.CYAN_BRIGHT));
		// print few messages
		print("Enter ? for help");
	}

	/**
	 * Processes the arguments if any <br>
	 * eg : -h homedir 
	 *  
	 * @param args
	 */
	private void processArguments(String[] args) {
		// Is there a "-h homedir" argument ?
		File home = getHomeArg(args);
		if ( home != null ) {
			print("Setting 'home' to " + home.getAbsolutePath() ) ;
			if ( commandLineProcessor.processLine("cd " + home.getAbsolutePath() ) ) {
				commandLineProcessor.processLine("h " + home.getAbsolutePath() );
			}
		}
	}

	/**
	 * Launches the command line processor
	 */
	private void launch() {
		try {
			while (true) {
				String line = consoleReader.readLine() ;
				commandLineProcessor.processLine(line);
			}
		} catch (Exception e) {
			print("ERROR : Unexpected exception " + e.getMessage() );
		}
	}

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
						print("Invalid 'home' argument : " + home);
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
