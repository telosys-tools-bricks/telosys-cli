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

import java.io.IOException;
import java.io.PrintWriter;

import org.telosys.tools.cli.observer.DbMetadataObserver;
import org.telosys.tools.cli.observer.DbModelObserver;
import org.telosys.tools.db.observer.DatabaseObserverProvider;

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
	private final CommandProvider commandProvider ;
	private final CommandLineProcessor commandLineProcessor;
	
	/**
	 * Constructor
	 * @throws IOException
	 */
	public TelosysCLI() throws IOException {
		super();
		this.consoleReader = new ConsoleReader() ;
		this.out = new PrintWriter(consoleReader.getOutput());
		this.commandProvider = new CommandProvider(consoleReader);
		this.commandLineProcessor = new CommandLineProcessor( consoleReader, commandProvider ) ;
	}
	
	/**
	 * Strats Telosys CLI
	 * @param args
	 */
	public void start(String[] args) {
		
		// Initialize the CLI
		init();		
		
		// Process launch arguments if any
		LaunchArgumentsProcessor argsProcessor = new LaunchArgumentsProcessor(commandProvider, out);
		argsProcessor.processLaunchArguments(args);
		
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
		
		DatabaseObserverProvider.setModelObserverClass(DbModelObserver.class);
		DatabaseObserverProvider.setMetadataObserverClass(DbMetadataObserver.class);
		DbMetadataObserver.setActive(false);
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

}
