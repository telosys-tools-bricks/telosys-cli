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

import org.telosys.tools.cli.Color;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.Filter;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;

import jline.console.ConsoleReader;

public abstract class ListInDepotAbstractCommand extends CommandLevel2 {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	protected ListInDepotAbstractCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	/**
	 * Filter elements in DepotResponse and print the resultin elements
	 * @param elementsTypeName
	 * @param depotName
	 * @param depotResponse
	 * @param criteria
	 */
	protected void filterAndPrintSearchResult(String elementsTypeName, String depotName, DepotResponse depotResponse, List<String> criteria) {
		DepotRateLimit rateLimit = depotResponse.getRateLimit();
		if ( depotResponse.getHttpStatusCode() == 200 ) {
			// Filter bundles with args if necessary
			List<String> elements = Filter.filter(depotResponse.getElementNames(), criteria);
			// Print the result
			printElements(elementsTypeName, depotName, elements);
			// Print current API rate limit returned by GitHub 
			print("API rate limit : "+ rateLimit.getRemaining() + "/" + rateLimit.getLimit() ) ; 
		}
		else if ( depotResponse.getHttpStatusCode() == 403 ) {
			String msg = "API request refused : http status '" + depotResponse.getHttpStatusCode() + "'" ;
			print(Color.colorize(msg, Color.RED_BRIGHT));
			print("API rate limit status : " ) ; 
			print(" . remaining : " + rateLimit.getRemaining() ) ; 
			print(" . limit     : " + rateLimit.getLimit() ) ; 
			print(" . reset     : " + rateLimit.getReset() ) ; 
		}
		else {
			String msg = "Unexpected http status '" + depotResponse.getHttpStatusCode() + "'" ;
			print(Color.colorize(msg, Color.RED_BRIGHT));
			print("Depot: " + depotResponse.getDepotName());
			print("URL: " + depotResponse.getDepotURL());
		}		
	}
	
	/**
	 * Prints elements found in the depot
	 * @param elementsTypeName
	 * @param depotName
	 * @param elements
	 */
	private void printElements(String elementsTypeName, String depotName, List<String> elements ) {
		if ( elements != null && ! elements.isEmpty() ) {
			print( elementsTypeName + " found in depot '" + depotName + "' : ");
			for ( String s : elements ) {
				print( " . " + s);
			}
		}
		else {
			print( "Nothing found in depot '" + depotName + "'.");
		}
	}
}
