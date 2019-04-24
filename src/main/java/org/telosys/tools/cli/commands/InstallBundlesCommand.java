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
import org.telosys.tools.cli.CommandWithGitHub;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.BundlesFromGitHub;

import jline.console.ConsoleReader;

/**
 * Install Bundle(s) 
 * Examples :
 *    ib * 				--> install all bundles from GitHub
 *    ib java rest 		--> install all the bundles containing "java" or "rest"
 *    
 * @author Laurent GUERIN
 *
 */
public class InstallBundlesCommand extends CommandWithGitHub {
	
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
			if ( checkHomeDirectoryDefined() && checkGitHubStoreDefined() ) {
				installBundles(args);
			}
			return null ;
		}
		else {
			// ib 
			return "Invalid usage";
		}
	}
		
	private void installBundles(String[] args) {
		//   TODO if already exists : prompt "overwrite ? [y/n] : "
		TelosysProject telosysProject = getTelosysProject();
		
		String githubStoreName = getCurrentGitHubStore() ;
		
		// Get bundles from GitHub 
		BundlesFromGitHub githubBundles;
		try {
			githubBundles = super.getGitHubBundles(githubStoreName);
		} catch (TelosysToolsException e) {
			printError(e);
			return ;
		}
		// Filter bundles names if args		
		List<String> bundlesNames = githubBundles.getBundlesNames().filter(args);

		// Install  bundles		
		if ( bundlesNames != null && bundlesNames.size() > 0 ) {
			print( "Installing " + bundlesNames.size() + " bundle(s) from GitHub... ");
			for ( String bundleName : bundlesNames ) {
				try {
					telosysProject.downloadAndInstallBundle(githubStoreName, bundleName);
					print( " . '" + bundleName + "' : installed. ");
				} catch (TelosysToolsException e) {
					print( " . '" + bundleName + "' : ERROR (cannot install) : "+ e.getMessage() );
				}
			}
		}
		else {
			print("No bundle found on GitHub.") ;
		}
	}
	
}
