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
	public HelpCommand(ConsoleReader consoleReader) {
		super(consoleReader);
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
	public String execute(Environment environment, String[] args) {
		StringBuffer sb = new StringBuffer();
		List<Command> commands = environment.getCommandProvider().getAllCommands();
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
