/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
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
import org.telosys.tools.cli.CommandWithBundles;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

public class DeleteBundleCommand extends CommandWithBundles {

	/**
	 * Constructor
	 * @param out
	 */
	public DeleteBundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "db";
	}

	@Override
	public String getShortDescription() {
		return "Delete Bundle" ;
	}

	@Override
	public String getDescription() {
		return "Deletes the given bundle";
	}
	
	@Override
	public String getUsage() {
		return "db bundle-part1 [bundle-part2 bundle-part3 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				deleteBundles(args);
			}
			else {
				return invalidUsage("argument expected");
			}
		}
		return null ;
	}

	private void deleteBundles(String[] commandArgs) {
		try {
			//List<String> bundleNames = BundlesUtil.getExistingBundles(getTelosysProject(), commandArgs);	
			List<String> bundleNames = getInstalledBundles(commandArgs);
			if ( bundleNames.isEmpty() ) {
				print("No bundle found.") ;
			}
			else {
				print("You are about to delete the following bundles :") ;
				printList(bundleNames) ;
				if ( confirm("Are you sure you want to delete these bundles ?") ) {
					for ( String bundleName : bundleNames ) {
						deleteBundle(bundleName);
					}
				}
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}


	private void deleteBundle(String bundleName) {

		TelosysProject telosysProject = getTelosysProject();
		try {
			if ( telosysProject.deleteBundle(bundleName) ) {
				print("Bundle '"+ bundleName + "' deleted.");
			}
			else {
				print("Bundle '"+ bundleName + "' not found.");
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}

}
