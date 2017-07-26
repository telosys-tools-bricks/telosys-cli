package org.telosys.tools.cli.commands;

import java.util.LinkedList;
import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.BundlesFilter;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

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
		if ( islistOption(args) ) {
			// gh -l 
			return listContent(args);
		}
		else {
			if ( args.length == 1 ) {
				// gh 
				return getCurrentGitHub();
			}
			else if ( args.length == 2 ) {
				// gh xxxx 
				return setCurrentGitHub(args[1]);
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
	
	private String getCurrentGitHub() {
		Environment environment = getEnvironment();
		return environment.getCurrentGitHubStore();
	}
	
	private String setCurrentGitHub(String newValue) {
		Environment environment = getEnvironment();
		environment.setCurrentGitHubStore(newValue);
		return "GitHub store is now '" + newValue + "'";
	}

	private String listContent(String[] args) {
		
		Environment environment = getEnvironment();
		printDebug("Args : " + args.length );
		List<String> bundles = getAllBundles();
		if ( bundles != null ) {
			List<String> criteria = buildCriteria(args);
			printDebug("Criteria : " + criteria );
			bundles = BundlesFilter.filter(bundles, criteria);
			return printBundles(environment.getCurrentGitHubStore(), bundles);
		}
		return null ;
	}
	
	private List<String> getAllBundles() {
		TelosysProject telosysProject = getTelosysProject();
		Environment environment = getEnvironment();
		try {
			return telosysProject.getBundlesList(environment.getCurrentGitHubStore());
		} catch (TelosysToolsException e) {
			printError(e);
			return null ;
		}
	}
	private List<String> buildCriteria( String[] args) {
		List<String> tokens = new LinkedList<>();
		for ( int i = 1 ; i < args.length ; i++ ) {
			if ( ! ( "-l".equals(args[i]) ) ) {
				tokens.add(args[i]);
			}
		}
		return tokens ;
	}
	
	private String printBundles(String githubStore, List<String> bundles) {
		StringBuffer sb = new StringBuffer();
		appendLine(sb, "Bundles found in GitHub store '" + githubStore + "' : ");
		for ( String s : bundles ) {
			appendLine(sb, " . " + s);
		}
		return sb.toString();
	}
}
