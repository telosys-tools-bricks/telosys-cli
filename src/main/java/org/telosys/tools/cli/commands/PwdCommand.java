package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class PwdCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public PwdCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "pwd";
	}

	@Override
	public String getShortDescription() {
		return "Print Working Directory" ;
	}
	
	@Override
	public String getDescription() {
		return "Print the current working directory";
	}
	
	@Override
	public String execute(String[] args) {
		return getCurrentDirectory();
	}

}
