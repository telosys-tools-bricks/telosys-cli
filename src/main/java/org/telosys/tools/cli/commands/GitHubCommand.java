package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class GitHubCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public GitHubCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "gh";
	}

	@Override
	public String getShortDescription() {
		return "GitHub" ;
	}
	
	@Override
	public String getDescription() {
		return "Set/print the GitHub store name ";
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length == 1 ) {
			// gh 
			return getCurrentGitHubStore();
		}
		else if ( args.length == 2 ) {
			// gh xxxx 
			return setNewValue(args[1]);
		}
		else {
			// gh xxx yyy 
			return "Invalid usage";
		}
	}
	
//	private String getCurrentGitHub() {
//		Environment environment = getEnvironment();
//		return environment.getCurrentGitHubStore();
//	}
	
	private String setNewValue(String newValue) {
		setCurrentGitHubStore(newValue);
		return "GitHub store is now '" + newValue + "'";
	}

}
