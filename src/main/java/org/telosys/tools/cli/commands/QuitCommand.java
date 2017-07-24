package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class QuitCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public QuitCommand(ConsoleReader consoleReader) {
		super(consoleReader);
	}
	
	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public String getShortName() {
		return "q" ;
	}
	
	@Override
	public String getDescription() {
		return "Quit";
	}
	

	@Override
	public String execute(Environment environment, String[] args) {
		print("bye...");
		System.exit(0);
		return "";
	}

}
