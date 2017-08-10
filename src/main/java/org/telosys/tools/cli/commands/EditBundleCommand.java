package org.telosys.tools.cli.commands;

import java.io.File;
import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.BundlesFilter;
import org.telosys.tools.commons.TelosysToolsException;

public class EditBundleCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditBundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "eb";
	}

	@Override
	public String getShortDescription() {
		return "Edit Bundle" ;
	}

	@Override
	public String getDescription() {
		return "Edit the 'templates.cfg' file of the given bundle";
	}
	
	@Override
	public String getUsage() {
		return "eb [bundle-name|bundle-partial-name]";
	}

	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			editBundle(args);
		}
		else {
			if ( checkBundleDefined() ) {
				editBundle( getCurrentBundle() );
			}
		}
		return null ;
	}

	private String editBundle(String[] commandArgs) {
		try {
			List<String> bundleNames = BundlesFilter.getExistingBundles(getTelosysProject(), commandArgs);	
			if ( bundleNames.size() > 1 ) {
				print( "Too much bundles found (" + bundleNames.size() + " bundles)") ;
			}
			else if ( bundleNames.size() == 1 ) {
				editBundle(bundleNames.get(0));
			}
			else {
				print("No bundle found.") ;
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}
	
	private String editBundle(String bundleName) {
		TelosysProject telosysProject = getTelosysProject();
		try {
			File file = telosysProject.getBundleConfigFile(bundleName);
			if ( file.exists() ) {
				return launchEditor(file.getAbsolutePath() );
			}
			else {
				print("File '" + file.getAbsolutePath() + "' not found");
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

}
