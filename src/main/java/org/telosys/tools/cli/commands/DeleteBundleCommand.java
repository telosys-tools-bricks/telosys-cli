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
		return "Delete the current/given bundle";
	}
	
	@Override
	public String getUsage() {
		return "db [bundle-part-name] [-y]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			// Check arguments :
			// 0 : db 
			// 1 : db -y | db bundle-name
			// 2 : db bundle-name -y
			if ( checkArguments(args, 0, 1, 2 ) && checkOptions(args, "-y") ) {
				String[] newArgs = registerAndRemoveYesOption(args);
				// newArgs = args without '-y' if any
				execute2(newArgs);				
			}
		}
		return null ;
	}

	public void execute2(String[] args) {
		if ( args.length > 1 ) {
			getAndDeleteBundles(args);
		}
		else {
			if ( checkBundleDefined() ) {
				// Current bundle is defined => delete the current bundle
				String currentBundle = getCurrentBundle();
				print("You are about to delete the current bundle :") ;
				print(" . " + currentBundle) ;
				if ( confirm("Do you really want to delete this bundle ?") ) {
					deleteBundle(currentBundle);
				}
			}
		}
	}

	private void getAndDeleteBundles(String[] commandArgs) {
		try {
			// Get bundles matching the given name parts
			List<String> bundleNames = getInstalledBundles(commandArgs);
			if ( bundleNames.isEmpty() ) {
				print("No bundle found.") ;
			}
			else {
				print("You are about to delete the following bundles :") ;
				printList(bundleNames) ;
				if ( confirm("Do you really want to delete these bundles ?") ) {
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
				if ( bundleName.equals(getCurrentBundle()) ) {
					// If current bundle deleted => unset current bundle
					unsetCurrentBundle();
				}
			}
			else {
				print("Bundle '"+ bundleName + "' not found.");
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}

}
