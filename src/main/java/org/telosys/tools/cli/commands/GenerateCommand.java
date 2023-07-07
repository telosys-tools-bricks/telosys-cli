/**
 *  Copyright (C) 2015-2019 Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.cli.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.CriteriaUtil;
import org.telosys.tools.cli.commons.EntityUtil;
import org.telosys.tools.cli.commons.TargetUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

import jline.console.ConsoleReader;

/**
 * Generates targets using the given entities and the given templates 
 * 
 * @author Laurent GUERIN
 *
 */
public class GenerateCommand extends CommandLevel2 {

	public static final String COMMAND_NAME = "gen";
	
	private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList(YES_OPTION, "-r")); // -y -r
	

	/**
	 * Constructor
	 * @param out
	 */
	public GenerateCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME ;
	}

	@Override
	public String getShortDescription() {
		return "Generate" ;
	}

	@Override
	public String getDescription() {
		return "Generate the given targets for the given entities";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " *|entity-name *|template-name [-r] [-y]";
	}

	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		if ( checkModelDefined() && checkBundleDefined() ) {
			// Check arguments :
			// 1 : gen -r
			// 2 : gen * *     |  gen -r -y
			// 3 : gen * * -r  |  gen * * -y
			// 4 : gen * * -r  -y
//			if ( checkArguments(args, 1, 2, 3, 4 ) && checkOptions(args, "-r", "-y") ) {
//				String[] newArgs = registerAndRemoveYesOption(args);
//				// newArgs = args without '-y' if any
//				generate(newArgs);
//			}
			if ( checkArguments(commandArguments, 1, 2, 3, 4 ) && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
				Set<String> activeOptions = getOptions(commandArguments);
				registerYesOptionIfAny(activeOptions);			
				List<String> argsWithoutOptions = removeOptions(commandArguments);
				generate(argsWithoutOptions, activeOptions );
			}
		}
		return null ;
	}

//	/**
//	 * Generation entry point
//	 * @param args all the arguments as provided by the command line (0 to N)
//	 */
//	private GenerationTaskResult generate(String[] args)  {
//		int nbArgs = args.length - 1 ;
//		try {
//			if ( nbArgs == 2 ) {
//				// gen * * 
//				return generate(args[1], args[2], false);
//			}
//			else if ( nbArgs == 3 ) {
//				// gen * * -r 
//				if ( checkResourcesOption(args[3]) ) {
//					return generate(args[1], args[2], true);
//				}
//			}
//			else if ( nbArgs == 1 ) {
//				// gen -r 
//				if ( checkResourcesOption(args[1]) ) {
//					return generateResources();
//				}
//			}
//			else {
//				print("invalid arguments");
//			}
//		} catch (TelosysToolsException e) {
//			printError(e);
//		}
//		return null;
//	}
	private GenerationTaskResult generate(List<String> argsWithoutOptions, Set<String> activeOptions )  {
		boolean flagResources = isOptionActive("-r", activeOptions);
		try {
			if ( argsWithoutOptions.size() == 2 ) {
				// gen * * / gen * * -r
				return generate(argsWithoutOptions.get(0), argsWithoutOptions.get(1), flagResources);
			}
			else if ( argsWithoutOptions.isEmpty() && flagResources) {
				// gen -r
				return generateResources();
			}
			else {
				print("invalid arguments"); // not supposed to happen
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null;
	}
	
//	private boolean checkResourcesOption(String option) {
//		if ( "-r".equals(option) ) {
//			return true ;
//		}
//		else {
//			print("Invalid argument '" + option + "' ( '-r' expected ) ");
//			return false ;
//		}
//	}
	
	/**
	 * @param argEntityNames argument for entities ( eg '*', 'Car', 'Car,Driver', etc )
	 * @param argTemplateNames argument for templates ( eg '*', 'CacheFilter_java.vm', '_java,_xml', etc )
	 * @param flagResources resources generation flag : true = generate resources
	 * @return 
	 * @throws TelosysToolsException
	 */
	private GenerationTaskResult generate(String argEntityNames, String argTemplateNames, 
			boolean flagResources) throws TelosysToolsException {
		
		TelosysProject telosysProject = getTelosysProject();
		// Loads the model for the current model name
		Model model = loadCurrentModel();
		if ( model == null) {
			print("Cannot load model => stopping code generation.");
			return null ;
		}
		List<Entity> entities = buildEntitiesList(argEntityNames, model);
		
		String bundleName = getCurrentBundle() ;
		List<TargetDefinition> targetDefinitions = buildTargetsList(argTemplateNames);
		
		print("Entities ( model = '"+getCurrentModel()+"' ) : ");
		print ( EntityUtil.buildListAsString(entities) );
		List<String> entityNames = EntityUtil.toEntityNames(entities);

		print("Templates ( bundle = '"+bundleName+"' ) : ");
		print ( TargetUtil.buildListAsString(targetDefinitions) );

		print("Copy resources : " + ( flagResources ? "yes" : "no" ));

		if ( entityNames.isEmpty() || targetDefinitions.isEmpty() ) {
			print("No entity or no templates => nothing to generate ");
			return null ;
		}
		else {
			if ( confirm("Do you want to launch the generation") ) {
				print("Generation in progress...");
				return telosysProject.launchGeneration(model, entityNames, bundleName, targetDefinitions, flagResources);			
			}
			else {
				print("Generation canceled.");
				return null ;
			}
		}
	}

	private GenerationTaskResult generateResources() throws TelosysToolsException {
		
		TelosysProject telosysProject = getTelosysProject();
		
		Model model = loadCurrentModel(); // Loads the model for the current model name
		List<String> entityNames = new LinkedList<>(); // Void list
		
		String bundleName = getCurrentBundle() ;
		TargetsDefinitions targetsDefinitions = getCurrentTargetsDefinitions();
		List<TargetDefinition> targetDefinitions = new LinkedList<>(); // Void list
		List<TargetDefinition> resources = targetsDefinitions.getResourcesTargets();
		if ( resources.isEmpty() ) {
			print("No resource in bundle '" + bundleName + "'");
		}
		else {
			if ( confirm("Do you want to copy the resources from '" + bundleName + "'" ) ) {
				print("Generation in progress...");
				return telosysProject.launchGeneration(model, entityNames, bundleName, targetDefinitions, true);			
			}
			else {
				print("Generation canceled.");
			}
		}
		return null ;
	}

	/**
	 * Builds a list of entities using the given argument ( eg : '*', 'Car', 'Car,Dog', 'Dog,Driver,Car' )
	 * @param arg
	 * @param model
	 * @return
	 */
	private List<Entity> buildEntitiesList(String arg, Model model) {
		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(arg) ;
		return EntityUtil.filter(model.getEntities(), criteria);
	}
	
	/**
	 * Returns a list of TargetDefinitions for the given argument <br>
	 * 
	 * @param arg can be '*' or a single 'pattern' or a list of 'patterns' ( eg '*' or 'record' or 'record,resource' )
	 * @return
	 */
	private List<TargetDefinition> buildTargetsList(String arg) {
		TargetsDefinitions targetDefinitions = getCurrentTargetsDefinitions();
		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(arg) ;
		return TargetUtil.filter(targetDefinitions.getTemplatesTargets(), criteria);
	}
	
}
