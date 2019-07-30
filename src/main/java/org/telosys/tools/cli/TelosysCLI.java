/**
 *  Copyright (C) 2015-2019 Telosys project org. ( http://www.telosys.org/ )
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

import org.telosys.tools.cli.args.Argument;
import org.telosys.tools.cli.args.Arguments;
import org.telosys.tools.cli.args.ArgumentsParser;
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

//	private final PrintWriter out ;
	private final PrintWriter stdout = new PrintWriter(System.out, true);
	
	private final ConsoleReader consoleReader ;
	private final CommandProvider commandProvider ;
//	private final CommandLineProcessor commandLineProcessor;
	
	/**
	 * Constructor
	 * @throws IOException
	 */
	public TelosysCLI() throws IOException {
		super();
		this.consoleReader = new ConsoleReader() ;
//		this.out = new PrintWriter(consoleReader.getOutput());
		this.commandProvider = new CommandProvider(consoleReader);
		
//		this.commandLineProcessor = new CommandLineProcessor( out, commandProvider ) ;
		// Set prompt initial status
		consoleReader.setPrompt(Color.colorize(Const.INITIAL_PROMPT, Const.PROMPT_COLOR) );

	}
	
	/**
	 * Start Telosys CLI
	 * @param args
	 */
	public void start(String[] args) {
		
//		printStdOut("Starting Telosys-CLI...");
		
		// Initialize Telosys CLI
		init();		
		
		// Process launch arguments if any
		ArgumentsParser argumentsParser = new ArgumentsParser(stdout);
//		ArgumentsParser argumentsParser = new ArgumentsParser(new PrintWriter(System.out, true));
		Arguments arguments = argumentsParser.parseArguments(args);
		
		// "-h home-directory"
		Argument hArg = arguments.getArgument(Arguments.H_ARG);
		if ( hArg != null ) {
			setHomeDirectoryFromArg(hArg);
		}
		
		// "-i input-file"
		Argument iArg = arguments.getArgument(Arguments.I_ARG);
		if ( iArg != null ) {
			File inputFile = (File) iArg.getValue();
			processCommandsFromFile(inputFile);
		}
		else {
			// Launch the line processor...
			processCommandsFromConsole();
		}
	}
	
	private void printStdOut(String message) {
		stdout.println(message);
		stdout.flush();
	}

	private void printBanner(PrintWriter printWriter, String color) {
		String banner = new Banner().bannerSlant();
//		print(Color.colorize(banner, Color.CYAN_BRIGHT));
		if ( color != null ) {
			printWriter.println(Color.colorize(banner, color));
		}
		else {
			printWriter.println(banner);
		}
		printWriter.flush();
	}

	/**
	 * Initializes the CLI 
	 */
	private void init() {
//		// set the initial prompt
//		consoleReader.setPrompt(Color.colorize(Const.INITIAL_PROMPT, Const.PROMPT_COLOR) );
//		// print the banner
//		String banner = new Banner().bannerSlant();
//		print(Color.colorize(banner, Color.CYAN_BRIGHT));
		
		DatabaseObserverProvider.setModelObserverClass(DbModelObserver.class);
		DatabaseObserverProvider.setMetadataObserverClass(DbMetadataObserver.class);
		DbMetadataObserver.setActive(false);
	}

	/**
	 * Execute commands from the console 
	 */
	private void processCommandsFromConsole() {
		// Use Console output instead of stdout
		PrintWriter consoleOutput = new PrintWriter(consoleReader.getOutput());
		// Print banner with color
		printBanner(consoleOutput, Color.CYAN_BRIGHT);
		// print few messages
		//consoleOutput.println("Enter ? for help"); consoleOutput.flush();
		printStdOut("Enter ? for help");

		// start command processing
		CommandLineProcessor commandLineProcessor = new CommandLineProcessor( consoleOutput, commandProvider ) ;
		try {
			while (true) {
				String line = consoleReader.readLine() ;
				commandLineProcessor.processLine(line);
			}
		} catch (Exception e) {
			printStdOut("ERROR : Unexpected exception " + e.getMessage() );
		}
	}

	/**
	 * Execute commands from the given file 
	 * @param fileName
	 */
	private void processCommandsFromFile(File file) {
		
		printBanner(stdout, null);
		
		CommandLineProcessor commandLineProcessor = new CommandLineProcessor( stdout, commandProvider ) ;
		CommandsFileProcessor commandsFileProcessor = new CommandsFileProcessor(commandLineProcessor);
		try {
			commandsFileProcessor.processFile(file);
		} catch ( Exception e) {
			printStdOut("ERROR : Unexpected exception " + e.getMessage() );
		}
	}

	private void setHomeDirectoryFromArg(Argument hArgument) {
		File homeFile = (File) hArgument.getValue();
		//--- Set current working directory : command "cd path" 
		executeCommand(commandProvider.getCommand("cd"), homeFile);
		//--- Set 'HOME' : command "h path"
		String r = executeCommand(commandProvider.getCommand("h"), homeFile);
		printStdOut(r) ; // Prints the "h" command result : Home set ('xxx')
	}
	
	private String executeCommand(Command command, File homeFile) {
		String[] args = new String[2] ;
		args[0] = command.getName();
		args[1] = homeFile.getAbsolutePath();
		return command.execute( args );
	}
}
