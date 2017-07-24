package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class PwdCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public PwdCommand(ConsoleReader consoleReader) {
		super(consoleReader);
	}
	
	@Override
	public String getName() {
		return "pwd";
	}

	@Override
	public String getShortName() {
		return null ;
	}
	
	@Override
	public String getDescription() {
		return "Print working directory (show the current directory)";
	}
	
	@Override
	public String execute(Environment environment, String[] args) {
		return environment.getCurrentDirectory();
	}

}
