package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;

/**
 * Lists the resources for the current bundle
 * 
 * @author Laurent GUERIN
 *
 */
public class ListResourcesCommand extends Command {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public ListResourcesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "lr";
	}

	@Override
	public String getShortDescription() {
		return "List Resources" ;
	}

	@Override
	public String getDescription() {
		return "List the resources defined for the current bundle";
	}
	
	@Override
	public String getUsage() {
		return "lr";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() && checkBundleDefined() ) {
			return listResources();
		}
		return null ;
	}
	
	private String listResources() {
		TargetsDefinitions targetDefinitions = getCurrentTargetsDefinitions();
		List<TargetDefinition> resources = targetDefinitions.getResourcesTargets();
		if ( resources.size() > 0 ) {
			for ( TargetDefinition td : resources ) {
				String dest = "" ;
				String folder = td.getFolder() ;
				String file = td.getFile();
				if ( ! StrUtil.nullOrVoid( folder ) && ! StrUtil.nullOrVoid(file) ) {
					dest = folder + " / " + file ;
				}
				else {
					if ( ! StrUtil.nullOrVoid( folder ) ) {
						dest = folder ;
					}
					if ( ! StrUtil.nullOrVoid( file ) ) {
						dest = file ;
					}
				}
				print( " . " + td.getTemplate() + " -> " + dest  ) ; 
			}
		}
		else {
			print( "No resource" ) ; 
		}
		return null ;
	}
}
