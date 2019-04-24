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

import org.telosys.tools.api.TelosysGlobalEnv;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class GitHubUserCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public GitHubUserCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "ghu";
	}

	@Override
	public String getShortDescription() {
		return "GitHub user" ;
	}
	
	@Override
	public String getDescription() {
		return "Print/Set/Clear the current GitHub user ";
	}
	
	@Override
	public String getUsage() {
		return "ghu [user-name] ( or [-c] for clear )";
	}
		
	@Override
	public String execute(String[] args) {
		if ( args.length == 1 ) {
			// ghu
			String user = TelosysGlobalEnv.getGitHubUser();
			if ( user != null ) {
				return user ;
			}
			else {
				return "GitHub user is not yet defined";
			}
		}
		else if ( args.length == 2 ) {
			// ghu xxxx 
			String arg = args[1] ;
			if ( "-c".equals(arg) ) {
				// Clear the current GitHub user
				TelosysGlobalEnv.clearGitHubUser();
				return "GitHub user has been cleared";
			}
			else {
				// Set a new GitHub user
				return setGitHubUser(arg);
			}
		}
		else {
			// gh xxx yyy 
			return "Invalid usage";
		}
	}
	
	private String setGitHubUser(String user) {
		// Input password
		String password = readSecret("Password:") ;
		// Set user/password couple
		TelosysGlobalEnv.setGitHubUser(user, password);
		return "GitHub user is now '" + user + "'";
	}

}
