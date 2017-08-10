package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.BundlesFilter;
import org.telosys.tools.commons.TelosysToolsException;

public class DeleteBundleCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public DeleteBundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "db";
	}

	@Override
	public String getShortDescription() {
		return "Delete Bundle" ;
	}

	@Override
	public String getDescription() {
		return "Deletes the given bundle";
	}
	
	@Override
	public String getUsage() {
		return "db bundle-part1 [bundle-part2 bundle-part3 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				deleteBundles(args);
			}
			else {
				return invalidUsage("argument expected");
			}
		}
		return null ;
	}

	private void deleteBundles(String[] commandArgs) {
		try {
			List<String> bundleNames = BundlesFilter.getExistingBundles(getTelosysProject(), commandArgs);	
			if ( bundleNames.size() > 0 ) {
				print("You are about to delete the following bundles :") ;
				printList(bundleNames) ;
				if ( confirm("Are you sure you want to delete these bundles ?") ) {
					for ( String bundleName : bundleNames ) {
						deleteBundle(bundleName);
					}
				}
			}
			else {
				print("No bundle found.") ;
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}


	private void deleteBundle(String bundleName) {

		TelosysProject telosysProject = getTelosysProject();
		try {
			if ( telosysProject.deleteBundle(bundleName) ) {
				print("Bundle '"+ bundleName + "' deleted.");
			}
			else {
				print("Bundle '"+ bundleName + "' not found.");
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}

}
