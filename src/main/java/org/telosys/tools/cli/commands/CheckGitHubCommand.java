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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.telosys.tools.cli.Color;
import org.telosys.tools.cli.CommandWithGitHub;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.github.GitHubRateLimitResponse;

import jline.console.ConsoleReader;

public class CheckGitHubCommand extends CommandWithGitHub {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public CheckGitHubCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "cgh";
	}

	@Override
	public String getShortDescription() {
		return "Check GitHub" ;
	}
	
	@Override
	public String getDescription() {
		return "Check GitHub accessibility and get API rate limit";
	}
	
	@Override
	public String getUsage() {
		return "cgh";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			try {
				GitHubRateLimitResponse response = getTelosysProject().getGitHubRateLimit();
				printResponse(response);
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}
	
	
	/**
	 * @param response
	 */
	private void printResponse(GitHubRateLimitResponse response) {
		String remaining = response.getRemaining();
		int remainingValue = StrUtil.getInt(response.getRemaining(), 9999);
		if ( remainingValue > 0 && remainingValue < 10 ) {
			remaining = Color.colorize(response.getRemaining(), Color.YELLOW_BRIGHT);
		}		
		else if ( remainingValue == 0 ) {
			remaining = Color.colorize(response.getRemaining(), Color.RED_BRIGHT );
		}
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		String hhmmss = fmt.format(new Date());
		print( "GitHub is responding." ); 
		print( "Current API rate limit (at " + hhmmss + ") : " ); 
		print( " . remaining  : " + remaining ); 
		print( " . limit      : " + response.getLimit() ); 
		print( " . reset      : " + response.getReset() ); 
		print( " . reset date : " + response.getResetDate() ); 
		if ( remainingValue == 0 ) {
			print( Color.colorize("API rate limit has been reached!", Color.RED_BRIGHT ) );
		}
	}
}
