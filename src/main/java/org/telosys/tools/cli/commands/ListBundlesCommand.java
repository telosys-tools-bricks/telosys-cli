package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.BundlesFilter;
import org.telosys.tools.commons.TelosysToolsException;

public class ListBundlesCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListBundlesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "lb";
	}

	@Override
	public String getShortDescription() {
		return "List Bundles" ;
	}

	@Override
	public String getDescription() {
		return "List the installed bundles";
	}
	
	@Override
	public String getUsage() {
		return "lb [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			return listBundles(args);
		}
		return null ;
	}

	private String listBundles(String[] args) {
		List<String> criteria = BundlesFilter.buildCriteriaFromArgs(args);
		TelosysProject telosysProject = getTelosysProject();
		try {
			// get all installed bundles
			List<String> bundles = telosysProject.getInstalledBundles();
			// filter with criteria if any
			List<String> filteredBundles = BundlesFilter.filter(bundles, criteria);
			return printBundles(filteredBundles);
			
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}
	
	/**
	 * Prints the given bundles
	 * @param bundles
	 * @return
	 */
	private String printBundles(List<String> bundles) {
		StringBuffer sb = new StringBuffer();
		if ( bundles != null && bundles.size() > 0 ) {
			//appendLine(sb, "Bundles installed in the current project : ");
			for ( String s : bundles ) {
				appendLine(sb, " . " + s);
			}
		}
		else {
			appendLine(sb, "No bundle found.");
		}
		return sb.toString();
	}

}
