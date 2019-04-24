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
package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Color;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.CommandProvider;
import org.telosys.tools.cli.CommandsGroup;
import org.telosys.tools.cli.CommandsGroups;
import org.telosys.tools.cli.Environment;

public class HelpCommand extends Command {

	private static final int PADDING = 4 ;
	
	private final CommandProvider commandProvider ;
	
	/**
	 * Constructor
	 * @param out
	 */
	public HelpCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
		this.commandProvider = environment.getCommandProvider();
	}
	
	@Override
	public String getName() {
		return "?";
	}

	@Override
	public String getShortDescription() {
		return "Help" ;
	}
	
	@Override
	public String getDescription() {
		return "Print help (list of all available commands)";
	}
	
	@Override
	public String getUsage() {
		return "?";
	}

	@Override
	public String execute(String[] args) {
		printHelp(args);
		return null ;
	}
	
	private void printHelp(String[] args) {
		if ( args.length > 1 ) {
			for ( int i = 1 ; i < args.length ; i++ ) {
				printHelp(args[i]);
			}
		}
		else {
			printHelpForAllCommands();
		}
	}
	
	private void printHelp(String commandName) {
		Command command = commandProvider.getCommand(commandName);
		if ( command != null ) {
			printHelp(command);
		}
		else {
			print("Invalid command name '" + commandName + "'") ;
		}
	}

	private void printHelp(Command command) {
		print(Color.colorize(command.getName(), Color.CYAN_BRIGHT) + " : " + command.getShortDescription());
		print("Description :");
		print("  " + command.getDescription());
		print("Usage :");
		print("  " + command.getUsage());
		print("");
	}
	
	
	private void printHelpForAllCommands() {
		Environment environment = getEnvironment();
		CommandsGroups commandsGroups = environment.getCommandsGroups();
		for ( CommandsGroup cg : commandsGroups.getAll() ) {
			printHelp(cg);
		}
	}
	
	private void printHelp(CommandsGroup commandsGroup) {
		print( commandsGroup.getName() + " :" );
		for ( String commandName : commandsGroup.getCommands() ) {
			Command c = commandProvider.getCommand(commandName);
			if ( c != null ) {
				print(formatCommand(c)) ;
			}
			else {
				print(". " + padding(commandName, PADDING) + " ( TODO ) not yet implemented") ;
			}
		}
	}

	private String padding(String s, int length) {
		if ( s.length() >= length ) {
			return s ;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(s);
		for ( int i = s.length() ; i < length ; i++ ) {
			sb.append(' ');
		}
		return sb.toString();
	}
	
	private String formatCommand(Command c) {
		StringBuilder sb = new StringBuilder();
		sb.append(". " + padding(c.getName(), PADDING)  ) ; 
		sb.append( " " + c.getShortDescription() ) ;
		sb.append(" : " + c.getDescription() ) ;
		return sb.toString();
	}
}
