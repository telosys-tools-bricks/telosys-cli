package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

/**
 * Lists the templates for the current bundle
 * 
 * @author Laurent GUERIN
 *
 */
public class ListTemplatesCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListTemplatesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "lt";
	}

	@Override
	public String getShortDescription() {
		return "List Templates" ;
	}

	@Override
	public String getDescription() {
		return "List the templates for the current bundle";
	}
	
	@Override
	public String getUsage() {
		return "lt [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() && checkBundleDefined() ) {
			return listTemplates(args);
		}
		return null ;
	}

	private String listTemplates(String[] args) {
		TelosysProject telosysProject = getTelosysProject();
		try {
			List<String> templates = telosysProject.getTemplates( getCurrentBundle() );
			return printTemplates(templates);
		} catch (TelosysToolsException e) {
			printError(e);
		}

//		List<String> criteria = BundlesFilter.buildCriteriaFromArgs(args);
//		try {
//			// get all installed bundles
//			List<String> bundles = telosysProject.getInstalledBundles();
//			// filter with criteria if any
//			List<String> filteredBundles = BundlesFilter.filter(bundles, criteria);
//			return printBundles(filteredBundles);
//			
//		} catch (TelosysToolsException e) {
//			printError(e);
//		}
		return null ;
	}
	
//	private List<String> getTemplates(String bundleName) throws TelosysToolsException {
//		TelosysProject telosysProject = getTelosysProject();
//		TargetsDefinitions targetDef = telosysProject.getTargetDefinitions(bundleName);
//		List<TargetDefinition> templates = targetDef.getTemplatesTargets();
//		List<String> list = new LinkedList<String>();
//		for ( TargetDefinition t : templates ) {
//			list.add( t.getFile() );
//		}
//		return list ;
//	}
	
	/**
	 * Prints the given templates
	 * @param templates
	 * @return
	 */
	private String printTemplates(List<String> templates) {
		StringBuffer sb = new StringBuffer();
		if ( templates != null && templates.size() > 0 ) {
			for ( String s : templates ) {
				appendLine(sb, " . " + s);
			}
		}
		else {
			appendLine(sb, "No template.");
		}
		return sb.toString();
	}

}
