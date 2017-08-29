package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.BundlesFilter;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.TargetsDefinitions;

public class BundleCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public BundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "b";
	}

	@Override
	public String getShortDescription() {
		return "Bundle" ;
	}

	@Override
	public String getDescription() {
		return "Print or set the current bundle";
	}
	
	@Override
	public String getUsage() {
		return "b [name-part]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			if ( checkHomeDirectoryDefined() ) {
				return setBundle(args);
			}
		}
		else {
			String bundleName = getCurrentBundle() ;
			if ( bundleName != null ) {
				TargetsDefinitions targetsDefinitions = getCurrentTargetsDefinitions();
				int templatesCount = targetsDefinitions.getTemplatesTargets().size();
				//int resourcesCount = targetsDefinitions.getResourcesTargets().size();
				String resources = "no resource" ;
				if ( targetsDefinitions.getResourcesTargets().size() > 0 ) {
					resources = "contains resource(s)" ;
				}
				return bundleName + " : " + templatesCount + " template(s), " + resources ;
			}
			else {
				return "Undefined (no current bundle)";
			}
		}
		return null ;
	}

	private String setBundle(String[] args) {
		List<String> criteria = BundlesFilter.buildCriteriaFromArgs(args);
		TelosysProject telosysProject = getTelosysProject();
		try {
			// get all installed bundles
			List<String> bundles = telosysProject.getInstalledBundles();
			// filter with criteria if any
			List<String> filteredBundles = BundlesFilter.filter(bundles, criteria);
			if ( filteredBundles.size() < 0 ) {
				return "No bundle found!" ;
			}
			else if ( filteredBundles.size() == 1 ) {
				setCurrentBundle(filteredBundles.get(0));
				return "Current bundle is now '" + getCurrentBundle() + "'";
			}
			else {
				return filteredBundles.size() + " bundles found!" ;
			}
			
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}
	
}
