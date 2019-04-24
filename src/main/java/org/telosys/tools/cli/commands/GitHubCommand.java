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

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class GitHubCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public GitHubCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "gh";
	}

	@Override
	public String getShortDescription() {
		return "GitHub" ;
	}
	
	@Override
	public String getDescription() {
		return "Print/Set/Reset the GitHub store name ";
	}
	
	@Override
	public String getUsage() {
		return "gh [github-store-name] ( or [-r] for reset )";
	}
		
	@Override
	public String execute(String[] args) {
		if ( args.length == 1 ) {
			// gh 
			return getCurrentGitHubStore();
		}
		else if ( args.length == 2 ) {
			// gh xxxx 
			String arg = args[1] ;
			if ( "-r".equals(arg) ) {
				// Reset the default GitHub store
				return setGitHubStore(getDefaultGitHubStore());
			}
			else {
				// Reset a specific GitHub store
				return setGitHubStore(arg);
			}
		}
		else {
			// gh xxx yyy 
			return "Invalid usage";
		}
	}
	
	private String setGitHubStore(String newValue) {
		setCurrentGitHubStore(newValue);
		return "GitHub store is now '" + newValue + "'";
	}

}
