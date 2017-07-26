package org.telosys.tools.cli.commands;

import java.util.LinkedList;
import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.BundlesFilter;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
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
	public String getShortName() {
		return null ;
	}
	
	@Override
	public String getDescription() {
		return "Install templates bundles from GitHub ";
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			// ib aaa aaa  
			return install(args);
		}
		else {
			// ib 
			return "Invalid usage";
		}
	}
		
	private String install(String[] args) {
		
		//TelosysProject telosysProject = getTelosysProject(environment);
		TelosysProject telosysProject = getTelosysProject();
		//List<String> bundles = getAllBundles(telosysProject, environment);
		List<String> bundles = getAllBundles(telosysProject);
		if ( bundles != null ) {
			if ( bundles.size() > 0 ) {
				List<String> criteria = buildCriteria(args);
				bundles = BundlesFilter.filter(bundles, criteria);
				if ( bundles.size() > 0 ) {
					print( "Installing " + bundles.size() + " bundle(s) from GitHub... ");
					for ( String bundleName : bundles ) {
						try {
//							telosysProject.downloadAndInstallBundle(environment.getCurrentGitHubStore(), bundleName);
							telosysProject.downloadAndInstallBundle(getCurrentGitHubStore(), bundleName);
							print( " . '" + bundleName + "' : installed. ");
						} catch (TelosysToolsException e) {
							print( " . '" + bundleName + "' : ERROR (cannot install) : "+ e.getMessage() );
						}
					}
				}
				else {
					print("No bundle matching the given criteria.") ;
				}
			}
			else {
				print("No bundle available on GitHub.") ;
			}
		}
		return null ;
	}
	
	private List<String> getAllBundles(TelosysProject telosysProject) {
		try {
			//return telosysProject.getBundlesList(environment.getCurrentGitHubStore());
			return telosysProject.getBundlesList(getCurrentGitHubStore());
		} catch (TelosysToolsException e) {
			printError(e);
			return null ;
		}
	}
	private List<String> buildCriteria( String[] args) {
		List<String> tokens = new LinkedList<>();
		for ( int i = 1 ; i < args.length ; i++ ) {
			tokens.add(args[i]);
		}
		return tokens ;
	}
}
