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
	public String getShortName() {
		return null ;
	}
	
	@Override
	public String getDescription() {
		return "Init a Telosys project directory with all required files";
	}
	
	@Override
	public String execute(String[] args) {
		
//		if ( environment.getHomeDirectory() == null ) {
//			return "Home directory must be set before" ;
//		}

//		String projectFullPath = environment.getHomeDirectory();
//		TelosysProject telosysProject = new TelosysProject(projectFullPath);
		TelosysProject telosysProject = getTelosysProject();
		String result = telosysProject.initProject();
		return result ;
	}
}
