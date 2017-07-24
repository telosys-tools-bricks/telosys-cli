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
	public ListBundlesCommand(ConsoleReader consoleReader) {
		super(consoleReader);
	}

	@Override
	public String getName() {
		return "lb";
	}

	@Override
	public String getShortName() {
		return null ;
	}

	@Override
	public String getDescription() {
		return "List the installed bundles";
	}
	
	@Override
	public String execute(Environment environment, String[] args) {
		return listBundles(environment);
	}

	private String listBundles(Environment environment) {
		TelosysProject telosysProject = getTelosysProject(environment);
		try {
			List<String> bundles = telosysProject.getInstalledBundles();
			StringBuffer sb = new StringBuffer();
			for ( String b : bundles ) {
				appendLine(sb, " . " + b);
			}
			return sb.toString();
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

}
