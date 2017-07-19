package org.telosys.tools.cli.commands;

import java.io.PrintWriter;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class InitCommand  extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public InitCommand(PrintWriter out) {
		super(out);
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
	public String execute(Environment environment, String[] args) {
		
		if ( environment.getHomeDirectory() == null ) {
			return "Home directory must be set before" ;
		}

		String projectFullPath = environment.getHomeDirectory();
		TelosysProject telosysProject = new TelosysProject(projectFullPath);
		String result = telosysProject.initProject();
		return result ;
	}
}
