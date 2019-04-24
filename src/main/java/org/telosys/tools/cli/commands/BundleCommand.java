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

import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.BundlesNames;
import org.telosys.tools.commons.bundles.TargetsDefinitions;

import jline.console.ConsoleReader;

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
		TelosysProject telosysProject = getTelosysProject();
		try {
			// get all installed bundles
			//List<String> bundles = telosysProject.getInstalledBundles();
			BundlesNames bundlesNames = telosysProject.getInstalledBundles();
			// filter with criteria if any
//			List<String> criteria = BundlesUtil.buildCriteriaFromArgs(args);
//			List<String> filteredBundles = BundlesUtil.filter(bundles, criteria);
			
			List<String> filteredBundles = bundlesNames.filter(args);
			
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
