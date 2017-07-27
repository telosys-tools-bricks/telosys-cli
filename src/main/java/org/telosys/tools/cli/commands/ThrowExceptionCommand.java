package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class ThrowExceptionCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ThrowExceptionCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "tex";
	}

	@Override
	public String getShortDescription() {
		return "Throw Exception" ;
	}
	
	@Override
	public String getDescription() {
		return "Throws an exception for error testing";
	}
	
	@Override
	public String execute(String[] args) {
		String s = null ;
		print("Throwing a Null Pointer Exception for test");
		s.length() ;
		return "Not supposed to be printed";
	}

}
