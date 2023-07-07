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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

public class DeleteBundleCommand extends CommandLevel2 {

	public static final String COMMAND_NAME = "db";
	
	private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList(YES_OPTION)); // -y 

	/**
	 * Constructor
	 * @param out
	 */
	public DeleteBundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
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
		return COMMAND_NAME + " [bundle-part-name] [-y]";
	}
	
	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		if ( checkHomeDirectoryDefined() ) {
			// Check arguments :
			// 0 : db 
			// 1 : db -y | db bundle-name
			// 2 : db bundle-name -y
			if ( checkArguments(commandArguments, 0, 1, 2 ) && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
//				List<String> newArgs = registerAndRemoveYesOption(commandArguments);
//				// newArgs = args without '-y' if any
//				executeDeleteBundle(newArgs);
				Set<String> activeOptions = getOptions(commandArguments);
				registerYesOptionIfAny(activeOptions);
				List<String> argsWithoutOptions = removeOptions(commandArguments);
				executeDeleteBundle(argsWithoutOptions);
			}
		}
		return null ;
	}

	public void executeDeleteBundle(List<String> args) {
		if ( args.size() > 1 ) {
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

	private void getAndDeleteBundles(List<String> args) {
		try {
			// Get bundles matching the given name parts
			List<String> bundleNames = getInstalledBundles(args);
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
