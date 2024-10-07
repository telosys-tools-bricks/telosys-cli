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

import org.telosys.tools.api.TelosysGlobal;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

public class GitHubTokenCommand extends Command {
	
	private static final String INVALID_USAGE = "Invalid usage";
	/**
	 * Constructor
	 * @param out
	 */
	public GitHubTokenCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "ght";
	}

	@Override
	public String getShortDescription() {
		return "GitHub Token" ;
	}
	
	@Override
	public String getDescription() {
		return "Set or remove the current GitHub personal access token (PAT) ";
	}
	
	@Override
	public String getUsage() {
		return "ght [-set or -none]";
	}
		
	@Override
	public String execute(String[] args) {
		if ( args.length == 1 ) {
			// ght
			executeWithoutArg();
		}
		else if ( args.length == 2 ) {
			// 'ght -set' or 'ght -none' 
			executeWithOneArg(args[1]);
		}
		else {
			return INVALID_USAGE;
		}
		return null;
	}
	
	private void executeWithoutArg() {
		try {
			if ( TelosysGlobal.isGitHubTokenSet() ) {
				print("A GitHub token is defined for the current user");
				print("(you can remove it with the 'ght -none' command)");
			}
			else {
				print("No GitHub token");
				print("(you can set it with the 'ght -set' command)");
			}
		} catch (Exception e) {
			printError(e);
		}		
	}
	private void executeWithOneArg(String arg) {
		try {
			if ( "-set".equals(arg) ) {
				setGitHubToken();
			}
			else if ( "-none".equals(arg) ) {
				removeGitHubToken();
			}
			else {
				print(INVALID_USAGE);
			}
		} catch (Exception e) {
			printError(e);
		}
	}
	
	private void setGitHubToken() throws TelosysToolsException {
		// Input token in secret mode
		String token = readSecret("GitHub personal access token (PAT):") ;
		// Set token
		TelosysGlobal.setGitHubToken(token);
		print("GitHub token is set.");
	}

	private void removeGitHubToken() throws TelosysToolsException {
		if ( TelosysGlobal.isGitHubTokenSet() ) {
			// Remove token
			TelosysGlobal.removeGitHubToken();
			print("The token has been removed");
		}
		else {
			print("No GitHub token");
		}
	}

}