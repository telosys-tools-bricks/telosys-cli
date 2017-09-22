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

/**
 * Install Bundle(s) 
 * Examples :
 *    ib * 				--> install all bundles from GitHub
 *    ib java rest 		--> install all the bundles containing "java" or "rest"
 *    
 * @author Laurent GUERIN
 *
 */
public class InstallBundlesCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public InstallBundlesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "ib";
	}

	@Override
	public String getShortDescription() {
		return "Install Bundle" ;
	}
	
	@Override
	public String getDescription() {
		return "Install templates bundles from GitHub ";
	}
	
	@Override
	public String getUsage() {
		return "ib [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( args.length > 1 ) {
			// ib aaa bbb  
			if ( checkHomeDirectoryDefined() ) {
				if ( checkGitHubStoreDefined() ) {
					return install(args);
				}
			}
			return null ;
		}
		else {
			// ib 
			return "Invalid usage";
		}
	}
		
	private String install(String[] args) {
		//   TODO if already exists : prompt "overwrite ? [y/n] : "
		TelosysProject telosysProject = getTelosysProject();
		List<String> bundles = getBundles(getCurrentGitHubStore(), args);
		if ( bundles != null && bundles.size() > 0 ) {
			print( "Installing " + bundles.size() + " bundle(s) from GitHub... ");
			for ( String bundleName : bundles ) {
				try {
					telosysProject.downloadAndInstallBundle(getCurrentGitHubStore(), bundleName);
					print( " . '" + bundleName + "' : installed. ");
				} catch (TelosysToolsException e) {
					print( " . '" + bundleName + "' : ERROR (cannot install) : "+ e.getMessage() );
				}
			}
		}
		else {
			print("No bundle available on GitHub.") ;
		}
		return null ;
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

}
