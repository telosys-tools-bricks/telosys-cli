package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.CommandProvider;
import org.telosys.tools.cli.CommandsGroup;
import org.telosys.tools.cli.CommandsGroups;
import org.telosys.tools.cli.Environment;

public class HelpCommand extends Command {

	private final static int PADDING = 4 ;
	
	private final CommandProvider commandProvider ;
	
	/**
	 * Constructor
	 * @param out
	 */
	public HelpCommand(ConsoleReader consoleReader, Environment environment, CommandProvider commandProvider) {
		super(consoleReader, environment);
		this.commandProvider = commandProvider ;
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
	
//	@Override
//	public String execute(String[] args) {
//		Environment environment = getEnvironment();
//		List<Command> commands = environment.getCommandProvider().getAllCommands();
//		StringBuffer sb = new StringBuffer();
//		for ( Command c : commands ) {
//			sb.append(" . " + c.getName() ) ; 
//			if ( c.getShortName() != null ) {
//				sb.append(" / " + c.getShortName() ) ;
//			}
//			// sb.append(" (" + c.getClass().getSimpleName() +")") ;
//			sb.append(" : " + c.getDescription() ) ;
//			//sb.append("\n") ;
//			appendEndOfLine(sb);
//		}
//		return sb.toString();
//	}
	
	@Override
	public String execute(String[] args) {
		printHelp();
		return null ;
	}
	
	private void printHelp() {
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
				print(". " + padding(commandName, PADDING) + " Unknown !!!") ;
			}
		}
	}

	private String padding(String s, int length) {
		if ( s.length() >= length ) {
			return s ;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(s);
		for ( int i = s.length() ; i < length ; i++ ) {
			sb.append(' ');
		}
		return sb.toString();
	}
	
	private String formatCommand(Command c) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(". " + padding(c.getName(), PADDING)  ) ; 
//		if ( c.getShortName() != null ) {
//			sb.append(" / " + c.getShortName() ) ;
//		}
		sb.append( " " + c.getShortDescription() ) ;
		// sb.append(" (" + c.getClass().getSimpleName() +")") ;
		sb.append(" : " + c.getDescription() ) ;
		return sb.toString();
	}
}
