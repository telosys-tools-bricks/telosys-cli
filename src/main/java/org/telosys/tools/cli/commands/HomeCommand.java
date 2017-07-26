package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class HomeCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public HomeCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "home";
	}

	@Override
	public String getShortName() {
		return "h";
	}
	
	@Override
	public String getDescription() {
		return "Set the HOME directory with the current directory";
	}
	
	@Override
	public String execute(String[] args) {
//		environment.setHomeDirectory();
//		updatePrompt(environment);
		if ( args.length > 1 ) {
			return tryToSetHome(args[1]);
		}
		else {
			//return invalidUsage("model-name argument expected");
			return undefinedIfNull(getCurrentHome());
		}
		
//		setCurrentHome();
////		return "Home set ('" + environment.getHomeDirectory() + "')" ;	
//		return "Home set ('" + getHomeDirectory() + "')" ;	
	}

	private String tryToSetHome(String dir) {
		if ( ".".equals(dir) ) {
			setCurrentHome();
			return "Home set ('" + getHomeDirectory() + "')" ;	
		}
		else {
			return "Invalid directory";
		}
	}
}
