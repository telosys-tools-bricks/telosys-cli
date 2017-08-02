package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.BundlesFilter;
import org.telosys.tools.cli.commons.GitHubBundlesUtil;
import org.telosys.tools.commons.TelosysToolsException;

public class InstallBundlesCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public InstallBundlesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "ib";
	}

	@Override
	public String getShortDescription() {
		return "Install Bundle" ;
	}
	
	@Override
	public String getDescription() {
		return "Install templates bundles from GitHub ";
	}
	
	@Override
	public String getUsage() {
		return "ib [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( args.length > 1 ) {
			// ib aaa aaa  
			if ( checkHomeDirectoryDefined() ) {
				if ( checkGitHubStoreDefined() ) {
					return install(args);
				}
			}
			return null ;
		}
		else {
			// ib 
			return "Invalid usage";
		}
	}
		
	private String install(String[] args) {
		TelosysProject telosysProject = getTelosysProject();
		List<String> bundles = getBundles(getCurrentGitHubStore(), args);
		if ( bundles != null && bundles.size() > 0 ) {
			print( "Installing " + bundles.size() + " bundle(s) from GitHub... ");
			for ( String bundleName : bundles ) {
				try {
					telosysProject.downloadAndInstallBundle(getCurrentGitHubStore(), bundleName);
					print( " . '" + bundleName + "' : installed. ");
				} catch (TelosysToolsException e) {
					print( " . '" + bundleName + "' : ERROR (cannot install) : "+ e.getMessage() );
				}
			}
		}
		else {
			print("No bundle available on GitHub.") ;
		}
		return null ;
	}
	
	private List<String> getBundles(String githubStoreName, String[] args) {
		//List<String> criteria = GitHubBundlesUtil.buildCriteria(args);
		List<String> criteria = BundlesFilter.buildCriteriaFromArgs(args);
		TelosysProject telosysProject = getTelosysProject();
		try {
			return GitHubBundlesUtil.getBundles(telosysProject, githubStoreName, criteria);
		} catch (TelosysToolsException e) {
			printError(e);
			return null ;
		}
	}

}
