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

import java.io.File;
import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithBundles;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

public class EditBundleCommand extends CommandWithBundles {

	/**
	 * Constructor
	 * @param out
	 */
	public EditBundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "eb";
	}

	@Override
	public String getShortDescription() {
		return "Edit Bundle" ;
	}

	@Override
	public String getDescription() {
		return "Edit the 'templates.cfg' file of the given bundle";
	}
	
	@Override
	public String getUsage() {
		return "eb [bundle-name|bundle-partial-name]";
	}

	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				editBundle(args);
			}
			else {
				if ( checkBundleDefined() ) {
					editBundle( getCurrentBundle() );
				}
			}
		}
		return null ;
	}

	private String editBundle(String[] commandArgs) {
		try {
			List<String> bundleNames = getInstalledBundles(commandArgs);
			if ( bundleNames.size() > 1 ) {
				print( "Too much bundles found (" + bundleNames.size() + " bundles)") ;
			}
			else if ( bundleNames.size() == 1 ) {
				editBundle(bundleNames.get(0));
			}
			else {
				print("No bundle found.") ;
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}
	
	private String editBundle(String bundleName) {
		TelosysProject telosysProject = getTelosysProject();
		try {
			File file = telosysProject.getBundleConfigFile(bundleName);
			if ( file.exists() ) {
				return launchEditor(file.getAbsolutePath() );
			}
			else {
				print("File '" + file.getAbsolutePath() + "' not found");
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

}
