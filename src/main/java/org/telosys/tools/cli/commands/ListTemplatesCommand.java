package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.CriteriaUtil;
import org.telosys.tools.cli.commons.TargetUtil;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;

/**
 * Lists the templates for the current bundle
 * 
 * @author Laurent GUERIN
 *
 */
public class ListTemplatesCommand extends Command {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
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
		return "lt [*|pattern|pattern1,pattern2,...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkArguments(args, 0, 1) && checkHomeDirectoryDefined() && checkBundleDefined() ) {
			return listTemplates(args);
		}
		return null ;
	}
	
	private String listTemplates(String[] args) {
		TargetsDefinitions targetDefinitions = getCurrentTargetsDefinitions();
		//List<String> criteria = TargetUtil.buildCriteriaFromArg(args.length > 1 ? args[1] : null) ;
		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(args.length > 1 ? args[1] : null) ;
		List<TargetDefinition> selectedTargets = TargetUtil.filter(targetDefinitions.getTemplatesTargets(), criteria);
		print ( TargetUtil.buildListAsString(selectedTargets) );
		return null ;
	}
}
