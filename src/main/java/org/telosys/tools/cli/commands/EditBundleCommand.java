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

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class EditBundleCommand extends CommandLevel2 {

	public static final String COMMAND_NAME = "eb";

	/**
	 * Constructor
	 * @param out
	 */
	public EditBundleCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
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
		return COMMAND_NAME + " [bundle-name|bundle-partial-name]";
	}

	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		if ( checkHomeDirectoryDefined() && checkArguments(commandArguments, 0, 1 ) ) {
			List<String> argsWithoutOptions = removeOptions(commandArguments);
			executeEditBundle(argsWithoutOptions);
		}
		return null ;
	}
	
	private void executeEditBundle(List<String> argsWithoutOptions) {
		File file = null;
		if ( argsWithoutOptions.isEmpty() ) {
			// db  (no arg) => current bundle if any
			if ( checkBundleDefined() ) {
				file = getCurrentBundleConfigFile();
			}
		}
		else if ( argsWithoutOptions.size() == 1 ) {
			// db bundle-name => find bundle
			file = findBundleConfigFile(argsWithoutOptions.get(0));
		}
		else {
			print("invalid arguments"); // not supposed to happen
			return;
		}
		if ( file != null ) {
			editBundleConfigFile(file);
		}
	}

	private void editBundleConfigFile(File file) {
		if ( file.exists() && file.isFile() ) {
			String r = launchEditor( file.getAbsolutePath() );
			print(r);
		}
		else {
			print("No bundle file '" + file.getAbsolutePath() + "'") ;
		}
	}
	
	private File findBundleConfigFile(String namePattern) {
		File bundleFolder = findBundleFolder(namePattern);
		if (bundleFolder != null) {
			File file = getTelosysProject().getBundleConfigFile(bundleFolder);
			if ( file.exists() ) {
				return file;
			}
		}
		return null;
	}
}
