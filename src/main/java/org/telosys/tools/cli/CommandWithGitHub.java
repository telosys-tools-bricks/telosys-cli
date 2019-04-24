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

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.BundlesFromGitHub;

import jline.console.ConsoleReader;

/**
 * Specialization of 'Command' providing methods to work with GitHub 
 * 
 * @author Laurent GUERIN 
 *
 */
public abstract class CommandWithGitHub extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public CommandWithGitHub(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	/**
	 * @param githubStoreName
	 * @return
	 * @throws TelosysToolsException
	 */
	protected final BundlesFromGitHub getGitHubBundles(String githubStoreName) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		return telosysProject.getGitHubBundlesList(githubStoreName);
	}
	
}
