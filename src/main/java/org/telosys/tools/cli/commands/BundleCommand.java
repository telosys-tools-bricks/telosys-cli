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

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class BundleCommand extends CommandLevel2 {

	private static final String NO_CURRENT_BUNDLE = "No current bundle";

	public static final String COMMAND_NAME = "b";
	
	private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList("-none")); 


	/**
	 * Constructor
	 * @param out
	 */
	public BundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
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
		return COMMAND_NAME + " [bundle-name-part] [-none]";
	}
	
//	@Override
//	public String execute(String[] args) {
//		if ( args.length > 1 ) {
//			if ( checkHomeDirectoryDefined() ) {
//				return setBundle(args);
//			}
//		}
//		else {
//			String bundleName = getCurrentBundle() ;
//			if ( bundleName != null ) {
//				TargetsDefinitions targetsDefinitions = getCurrentTargetsDefinitions();
//				int templatesCount = targetsDefinitions.getTemplatesTargets().size();
//				String resources = "no resource" ;
//				if ( ! targetsDefinitions.getResourcesTargets().isEmpty()) {
//					resources = "contains resource(s)" ;
//				}
//				return bundleName + " : " + templatesCount + " template(s), " + resources ;
//			}
//			else {
//				return "Undefined (no bundle selected)";
//			}
//		}
//		return null ;
//	}
	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		
		if ( checkArguments(commandArguments, 0, 1 ) && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
			Set<String> activeOptions = getOptions(commandArguments);
			List<String> argsWithoutOptions = removeOptions(commandArguments);
			if ( ! argsWithoutOptions.isEmpty() ) {
				// b bundle-name
				if ( checkHomeDirectoryDefined() ) {
					tryToSetCurrentBundle(argsWithoutOptions.get(0));
				}
			}
			else if ( isOptionActive("-none", activeOptions) ) {
				// b -none
				unsetCurrentBundle();
				print( NO_CURRENT_BUNDLE );
			}
			else {
				// b  (without arg)
				String currentBundle = getCurrentBundle() ;
				print( currentBundle != null ? currentBundle : NO_CURRENT_BUNDLE );
			}
		}
		return null ;
	}

	private String findExactMatching(String name, List<String> names ) {
		for ( String s : names ) {
			if ( s.equals(name) ) {
				return s;
			}
		}
		return null ;
	}
	
	private String selectBundle(String bundleName ) {
		setCurrentBundle(bundleName);
		return "Current bundle is now '" + getCurrentBundle() + "'";
	}
	
//	private String setBundle(String[] args) {
//		TelosysProject telosysProject = getTelosysProject();
//		try {
//			// get all installed bundles
////			List<String> bundles  = telosysProject.getInstalledBundles();
//			List<String> bundles  = telosysProject.getBundleNames();
//			List<String> criteria = buildCriteriaFromArgs(args);
//			// filter with criteria if any
//			List<String> filteredBundles = Filter.filter(bundles, criteria);
//			
//			if ( filteredBundles.isEmpty() ) {
//				return "No bundle found!" ;
//			}
//			else if ( filteredBundles.size() == 1 ) {
//				// Only 1 bundle matching arg
//				return selectBundle(filteredBundles.get(0));
//			}
//			else {
//				if ( args.length == 1 ) {
//					// Only 1 arg matching exactly one of the filtered bundles  
//					// e.g. : command 'b foo' with 3 filtered names : 'foo', 'foo1', 'foo2' => return 'foo'
//					String name = findExactMatching(args[0], filteredBundles ) ;
//					if ( name != null ) {
//						return selectBundle(name);
//					}
//				}
//				return "Ambiguous : " + filteredBundles.size() + " bundles found" ;
//			}
//			
//		} catch (TelosysToolsException e) {
//			printError(e);
//		}
//		return null ;
//	}
	
	private String tryToSetCurrentBundle(String bundleNamePattern) {
		File bundleFolder = findBundleFolder(bundleNamePattern) ;
		// if found 
		if ( bundleFolder != null ) {
			setCurrentBundle(bundleFolder.getName());
			print( "Current bundle is now '" + getCurrentBundle() + "'" );
		}
		return null ;
	}	
}
