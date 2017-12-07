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

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.BundlesFilter;
import org.telosys.tools.cli.commons.GitHubBundlesUtil;
import org.telosys.tools.commons.TelosysToolsException;

public class ListGitHubCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListGitHubCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "lgh";
	}

	@Override
	public String getShortDescription() {
		return "List GitHub" ;
	}
	
	@Override
	public String getDescription() {
		return "List the content of the GitHub store";
	}
	
	@Override
	public String getUsage() {
		return "lgh [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			if ( checkGitHubStoreDefined() ) {
				return listContent(getCurrentGitHubStore(), args);
			}
		}
		return null ;
	}
	
	private String listContent(String githubStoreName, String[] args) {
		
		List<String> bundles = getBundles(githubStoreName, args) ;
		return printBundles(githubStoreName, bundles);
	}
	
	private List<String> getBundles(String githubStoreName, String[] args) {
		List<String> criteria = BundlesFilter.buildCriteriaFromArgs(args);
		TelosysProject telosysProject = getTelosysProject();
		try {
			return GitHubBundlesUtil.getBundles(telosysProject, githubStoreName, criteria);
		} catch (TelosysToolsException e) {
			printError(e);
			return null ;
		}
	}
	
	/**
	 * Prints the given bundles
	 * @param githubStore
	 * @param bundles
	 * @return
	 */
	private String printBundles(String githubStore, List<String> bundles) {
		StringBuilder sb = new StringBuilder();
		if ( bundles != null && bundles.size() > 0 ) {
			appendLine(sb, "Bundles found in GitHub store '" + githubStore + "' : ");
			for ( String s : bundles ) {
				appendLine(sb, " . " + s);
			}
		}
		else {
			appendLine(sb, "No bundle found in GitHub store '" + githubStore + "'.");
		}
		return sb.toString();
	}
}
