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

import org.telosys.tools.cli.Color;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.Filter;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;

import jline.console.ConsoleReader;

public class ListModelsInDepotCommand extends CommandLevel2 {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public ListModelsInDepotCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "lmd";
	}

	@Override
	public String getShortDescription() {
		return "List Models in Depot" ;
	}
	
	@Override
	public String getDescription() {
		return "List models available in the depot";
	}
	
	@Override
	public String getUsage() {
		return "lmd [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() && checkGitHubStoreDefined() ) {
			getAndPrintModels(getCurrentGitHubStore(), args);
		}
		return null ;
	}
	
	private void getAndPrintModels(String depotName, String[] args) {
		
		try {
			// Get all from depot 
			DepotResponse depotResponse = getTelosysProject().getModelsAvailableInDepot(depotName); // v 4.2.0
			DepotRateLimit rateLimit = depotResponse.getRateLimit();
			
			if ( depotResponse.getHttpStatusCode() == 200 ) {
				// Filter bundles with args if necessary
				List<String> bundles = Filter.filter(depotResponse.getElementNames(), buildCriteriaFromArgs(args));
				// Print the result
				printBundles(depotName, bundles);
				// Print current API rate limit returned by GitHub 
				print("GitHub API rate limit : "+ rateLimit.getRemaining() + "/" + rateLimit.getLimit() ) ; 
			}
			else if ( depotResponse.getHttpStatusCode() == 403 ) {
				String msg = "GitHub API request refused : http status '" + depotResponse.getHttpStatusCode() + "'" ;
				print(Color.colorize(msg, Color.RED_BRIGHT));
				print("GitHub API rate limit status : " ) ; 
				print(" . remaining : " + rateLimit.getRemaining() ) ; 
				print(" . limit     : " + rateLimit.getLimit() ) ; 
				print(" . reset     : " + rateLimit.getReset() ) ; 
			}
			else {
				String msg = "Unexpected http status '" + depotResponse.getHttpStatusCode() + "'" ;
				print(Color.colorize(msg, Color.RED_BRIGHT));
			}
			
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}
	
	/**
	 * Prints the given bundles
	 * @param githubStore
	 * @param bundles
	 * @return
	 */
	private void printBundles(String githubStore, List<String> bundles) {
		if ( bundles != null && ! bundles.isEmpty() ) {
			print("Models found in '" + githubStore + "' : ");
			for ( String s : bundles ) {
				print( " . " + s);
			}
		}
		else {
			print( "No model found in '" + githubStore + "'.");
		}
	}
}
