package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class GitHubCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public GitHubCommand(ConsoleReader consoleReader) {
		super(consoleReader);
	}
	
	@Override
	public String getName() {
		return "gh";
	}

	@Override
	public String getShortName() {
		return null ;
	}
	
	@Override
	public String getDescription() {
		return "Set the GitHub store name, or list its content ";
	}
	
	@Override
	public String execute(Environment environment, String[] args) {
		if ( islistOption(args) ) {
			// gh -l 
			return listContent(environment, args);
		}
		else {
			if ( args.length == 1 ) {
				// gh 
				return getCurrentGitHub(environment);
			}
			else if ( args.length == 2 ) {
				// gh xxxx 
				return setCurrentGitHub(environment, args[1]);
			}
			else {
				// gh xxx yyy 
				return "Invalid usage";
			}
		}
	}
	
	private boolean islistOption(String[] args) {
		if ( args.length >= 2 ) {
			return "-l".equals(args[1]);
		}
		return false ;
	}
	
	private String getCurrentGitHub(Environment environment) {
		return environment.getCurrentGitHubStore();
	}
	
	private String setCurrentGitHub(Environment environment, String newValue) {
		environment.setCurrentGitHubStore(newValue);
		return "GitHub store is now '" + newValue + "'";
	}

	private String listContent(Environment environment, String[] args) {
		
		TelosysProject telosysProject = getTelosysProject(environment);
		try {
			List<String> bundles = telosysProject.getBundlesList(environment.getCurrentGitHubStore());
			StringBuffer sb = new StringBuffer();
			appendLine(sb, "Bundles found in GitHub store '" + environment.getCurrentGitHubStore() + "' : ");
			for ( String s : bundles ) {
				appendLine(sb, " . " + s);
			}
			return sb.toString();
		} catch (TelosysToolsException e) {
			e.printStackTrace();
			printError(e);
			return null ;
		}
	}
}
