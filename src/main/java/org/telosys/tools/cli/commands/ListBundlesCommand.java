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

import org.telosys.tools.cli.CommandWithBundles;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

public class ListBundlesCommand extends CommandWithBundles {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListBundlesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "lb";
	}

	@Override
	public String getShortDescription() {
		return "List Bundles" ;
	}

	@Override
	public String getDescription() {
		return "List the installed bundles";
	}
	
	@Override
	public String getUsage() {
		return "lb [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			listBundles(args);
		}
		return null ;
	}

	private void listBundles(String[] commandArgs) {
		
		try {
//			List<String> bundleNames = BundlesUtil.getExistingBundles(getTelosysProject(), commandArgs);	
			List<String> bundleNames = getInstalledBundles(commandArgs);
			if ( bundleNames.isEmpty() ) {
				print("No bundle found.") ;
			}
			else {
				print( bundleNames.size() + " bundle(s) :") ;
				printList(bundleNames) ;
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}
}
