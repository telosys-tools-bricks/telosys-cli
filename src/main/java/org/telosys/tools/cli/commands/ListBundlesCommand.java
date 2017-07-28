package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
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
		return "List Bundle" ;
	}

	@Override
	public String getDescription() {
		return "List the installed bundles";
	}
	
	@Override
	public String execute(String[] args) {
		return listBundles();
	}

	private String listBundles() {
		//TelosysProject telosysProject = getTelosysProject(environment);
		TelosysProject telosysProject = getTelosysProject();
		try {
			List<String> bundles = telosysProject.getInstalledBundles();
//			StringBuffer sb = new StringBuffer();
//			for ( String b : bundles ) {
//				appendLine(sb, " . " + b);
//			}
//			return sb.toString();
			return printBundles(bundles);
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
			appendLine(sb, "Bundles installed in the current project : ");
			for ( String s : bundles ) {
				appendLine(sb, " . " + s);
			}
		}
		else {
			appendLine(sb, "No bundle found in the current project.");
		}
		return sb.toString();
	}

}
