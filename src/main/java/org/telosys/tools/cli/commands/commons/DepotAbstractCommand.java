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
package org.telosys.tools.cli.commands.commons;

import java.util.List;

import org.telosys.tools.api.InstallationType;
import org.telosys.tools.cli.Color;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;

import jline.console.ConsoleReader;

public abstract class DepotAbstractCommand extends CommandLevel2 {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	protected DepotAbstractCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	/**
	 * Returns true if the HTTP reponse from the depot is OK (status code = 200)
	 * else prints problem explanantion
	 * @param depotResponse
	 * @return
	 */
	protected boolean isDepotResponseOK(DepotResponse depotResponse) {
		if (depotResponse.getHttpStatusCode() == 200) {
			return true; // OK
		} else {
			if (depotResponse.getHttpStatusCode() == 403) {
				String msg = "API request refused : http status '" + depotResponse.getHttpStatusCode() + "'";
				print(Color.colorize(msg, Color.RED_BRIGHT));
				DepotRateLimit rateLimit = depotResponse.getRateLimit();
				print("API rate limit status : ");
				print(" . remaining : " + rateLimit.getRemaining());
				print(" . limit     : " + rateLimit.getLimit());
				print(" . reset     : " + rateLimit.getReset());
			} else {
				String msg = "Unexpected http status '" + depotResponse.getHttpStatusCode() + "'";
				print(Color.colorize(msg, Color.RED_BRIGHT));
				print("Depot: " + depotResponse.getDepotName());
				print("URL: " + depotResponse.getDepotURL());
			}
			return false; // NOT OK
		}
	}
	
	/**
	 * Filter elements in DepotResponse and print the resulting elements
	 * @param elementsTypeName
	 * @param depotName
	 * @param depotResponse
	 * @param criteria
	 */
	protected void filterAndPrintSearchResult(String elementsTypeName, String depotName, DepotResponse depotResponse, List<String> criteria) {
		if ( isDepotResponseOK(depotResponse) ) {
			// Filter elements (models or bundles) with criteria if any
			List<DepotElement> elements = depotResponse.filterElementsByName(criteria);
			// Print the result
			printElements(elementsTypeName, depotName, elements);
			// Print current API rate limit returned by GitHub 
			DepotRateLimit rateLimit = depotResponse.getRateLimit();
			print("API rate limit : "+ rateLimit.getRemaining() + "/" + rateLimit.getLimit() + " (http status : " + depotResponse.getHttpStatusCode()+ ")") ; 
		}
	}
	
	private void printElements(String elementsTypeName, String depotName, List<DepotElement> elements) {
		if ( ! elements.isEmpty() ) {
			print( elementsTypeName + " found in depot '" + depotName + "' : ");
			for ( DepotElement e : elements ) {
				print( " . " + e.getName() + "  ("+e.getVisibility()+") (default branch '" + e.getDefaultBranch() + "')");
			}
		}
		else {
			print( "Nothing found in depot '" + depotName + "'.");
		}
	}
	
	/**
	 * Filter elements in DepotResponse and install the resulting elements
	 * @param elementTypeName
	 * @param depotName
	 * @param depotResponse
	 * @param criteria
	 * @param installationType
	 */
	protected void filterAndInstallSearchResult(String elementTypeName, String depotName, DepotResponse depotResponse, List<String> criteria, InstallationType installationType) {
		// Filter elements (models or bundles) with criteria if any
		List<DepotElement> elements = depotResponse.filterElementsByName(criteria);
		if ( ! elements.isEmpty()) {
			print("Installing " + elements.size() + " " + elementTypeName + "(s) from depot... ");
			for (DepotElement e : elements) {
				try {
					if ( getTelosysProject().downloadAndInstallBranch(depotName, e.getName(), e.getDefaultBranch(), installationType) ) {
						print(" . '" + e.getName() + "' : installed. ");
					} else {
						print(" . '" + e.getName() + "' : not installed (already exists). ");
					}
				} catch (TelosysToolsException ex) {
					print(" . '" + e.getName() + "' : ERROR : " + ex.getMessage());
				}
			}
		} else {
			print("No " + elementTypeName + " found in depot.");
		}
	}
}
