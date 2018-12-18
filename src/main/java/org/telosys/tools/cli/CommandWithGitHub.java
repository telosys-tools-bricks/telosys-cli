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
package org.telosys.tools.cli;

import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.commons.BundlesFilter;
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
	 * Returns a list of bundles matching the given arguments
	 * @param githubStoreName
	 * @param args
	 * @return
	 */
	protected final BundlesFromGitHub getGitHubBundles(String githubStoreName, String[] args) throws TelosysToolsException {
		List<String> criteria = BundlesFilter.buildCriteriaFromArgs(args);
		TelosysProject telosysProject = getTelosysProject();
		return getGitHubBundles(telosysProject, githubStoreName, criteria);
	}
	
	/**
	 * Returns a list of bundles matching the given criteria
	 * @param telosysProject
	 * @param githubStoreName
	 * @param criteria
	 * @return
	 * @throws TelosysToolsException
	 */
	private BundlesFromGitHub getGitHubBundles(TelosysProject telosysProject, String githubStoreName, List<String> criteria) throws TelosysToolsException {
		
		// Get all the bundles available in the GitHub store
		BundlesFromGitHub bundles = telosysProject.getGitHubBundlesList(githubStoreName);
	
		// Filter with criteria
		List<String> filteredList = BundlesFilter.filter(bundles.getBundlesNames(), criteria);
		return new BundlesFromGitHub(filteredList, bundles.getRateLimitMessage());
	}	

}
