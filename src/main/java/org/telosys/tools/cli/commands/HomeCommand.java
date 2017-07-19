package org.telosys.tools.cli.commands;

import java.io.PrintWriter;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class HomeCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public HomeCommand(PrintWriter out) {
		super(out);
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
	public String execute(Environment environment, String[] args) {
		environment.setHomeDirectory();
		return "Home set ('" + environment.getHomeDirectory() + "')" ;	
	}
	
}
