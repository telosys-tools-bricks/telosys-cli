package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class HelpCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public HelpCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getShortName() {
		return "?" ;
	}
	
	@Override
	public String getDescription() {
		return "Print help (list of all available commands)";
	}
	
	@Override
	public String execute(String[] args) {
		Environment environment = getEnvironment();
		List<Command> commands = environment.getCommandProvider().getAllCommands();
		StringBuffer sb = new StringBuffer();
		for ( Command c : commands ) {
			sb.append(" . " + c.getName() ) ; 
			if ( c.getShortName() != null ) {
				sb.append(" / " + c.getShortName() ) ;
			}
			// sb.append(" (" + c.getClass().getSimpleName() +")") ;
			sb.append(" : " + c.getDescription() ) ;
			//sb.append("\n") ;
			appendEndOfLine(sb);
		}
		return sb.toString();
	}

}
