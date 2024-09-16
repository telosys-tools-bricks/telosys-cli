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

import org.telosys.tools.api.InstallationType;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commands.commons.DepotAbstractCommand;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.DepotResponse;

import jline.console.ConsoleReader;

/**
 * Install Bundle(s) 
 * Examples :
 *    ib * 				--> install all bundles from the depot
 *    ib java rest 		--> install all the bundles containing "java" or "rest"
 *    
 * @author Laurent GUERIN
 *
 */
public class InstallBundlesCommand extends DepotAbstractCommand {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public InstallBundlesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "ib";
	}

	@Override
	public String getShortDescription() {
		return "Install Bundle(s)" ;
	}
	
	@Override
	public String getDescription() {
		return "Install bundle(s) of templates available in the depot";
	}
	
	@Override
	public String getUsage() {
		return "ib [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( args.length > 1 ) {
			// ib aaa bbb  
			if ( checkHomeDirectoryDefined() ) {
				installBundles(args);
			}
			return null ;
		} 
		else {
			// no argument 
			return "Invalid usage";
		}
	}
		
	private void installBundles(String[] args) {
		String depotName = getTelosysProject().getTelosysToolsCfg().getDepotNameForBundles(); 
		try {
			DepotResponse depotResponse = getTelosysProject().getBundlesAvailableInDepot(depotName); 
			if (isDepotResponseOK(depotResponse)) {
				List<String> criteria = buildCriteriaFromArgs(args);
				filterAndInstallSearchResult("bundle", depotName, depotResponse, criteria, InstallationType.BUNDLE);
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}		
	}
	
}
