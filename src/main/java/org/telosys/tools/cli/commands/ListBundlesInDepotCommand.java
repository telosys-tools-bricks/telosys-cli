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

import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commands.commons.ListInDepotAbstractCommand;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.depot.DepotResponse;

import jline.console.ConsoleReader;

public class ListBundlesInDepotCommand extends ListInDepotAbstractCommand {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public ListBundlesInDepotCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "lbd";
	}

	@Override
	public String getShortDescription() {
		return "List Bundles in Depot" ;
	}
	
	@Override
	public String getDescription() {
		return "List bundles of templates available in the depot";
	}
	
	@Override
	public String getUsage() {
		return "lbd [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			TelosysToolsCfg telosysToolsCfg = getTelosysProject().getTelosysToolsCfg();
			String depotName = telosysToolsCfg.getDepotNameForBundles(); 
			try {
				// get all bundles using depot API (GitHub API)
				DepotResponse depotResponse = getTelosysProject().getBundlesAvailableInDepot(depotName); 
				// filter and print the models found in depot 
				filterAndPrintSearchResult("Bundles", depotName, depotResponse, buildCriteriaFromArgs(args));
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}

}
