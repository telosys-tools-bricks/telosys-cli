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
package org.telosys.tools.cli;

import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.Filter;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

/**
 * Specialization of 'Command' providing methods to work with project bundles 
 * 
 * @author Laurent GUERIN 
 *
 */
public abstract class CommandWithBundles extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	protected CommandWithBundles(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	/**
	 * Returns bundles installed in the current project and matching the given arguments
	 * @param args
	 * @return
	 * @throws TelosysToolsException
	 */
	protected final List<String> getInstalledBundles(String[] args) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		// get all installed bundles
//		BundlesNames bundlesNames = telosysProject.getInstalledBundles();
//		return bundlesNames.filter(args);
		List<String> allBundles = telosysProject.getInstalledBundles();
		return Filter.filter(allBundles, buildCriteriaFromArgs(args));
	}

}
