package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class InitCommand  extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public InitCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "init";
	}

	@Override
	public String getShortDescription() {
		return "Initialization" ;
	}
	
	@Override
	public String getDescription() {
		return "Init the Telosys directory with all required files";
	}
	
	@Override
	public String getUsage() {
		return "init";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			TelosysProject telosysProject = getTelosysProject();
			return telosysProject.initProject();
		}
		return null ;
	}
}
