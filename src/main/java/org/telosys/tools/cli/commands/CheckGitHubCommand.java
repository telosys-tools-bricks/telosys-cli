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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telosys.tools.cli.Color;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.depot.Depot;
import org.telosys.tools.commons.github.GitHubRateLimitResponse;

import jline.console.ConsoleReader;

public class CheckGitHubCommand extends CommandLevel2 {
	
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
		return "cgh [-v]";
	}
	
	@Override
	public String execute(String[] args) {
		List<String> commandArguments = getArgumentsAsList(args);
		if ( checkHomeDirectoryDefined() && checkArguments(commandArguments, 0, 1) ) {
			boolean verbose =  !commandArguments.isEmpty() && "-v".equals(commandArguments.get(0)) ;
			TelosysToolsCfg telosysToolsCfg = getTelosysProject().getTelosysToolsCfg();
			checkGitHub(telosysToolsCfg.getDepotNameForBundles(), telosysToolsCfg.getDepotNameForModels(), verbose);
		}
		return null ;
	}
	
	private void checkGitHub(String depotForBundles, String depotForModels, boolean verbose) {
		try {
			Map<String,Depot> map = new HashMap<>(); // Unicity based on API Rate Limit URL
			Depot depot1 = new Depot(depotForBundles);
			if ( depot1.isGitHubDepot() ) {
				map.put(depot1.getApiRateLimitUrl(), depot1);
			}
			Depot depot2 = new Depot(depotForModels);
			if ( depot2.isGitHubDepot() ) {
				map.put(depot2.getApiRateLimitUrl(), depot2);
			}
			if ( map.isEmpty() ) {
				print("No GitHub depot, nothing to check.");
			}
			else {
				int n = 0 ;
				for (Depot d : map.values()) {
					n++;
					if ( n > 1 ) print("");
					checkGitHubDepot(d, verbose);
				}
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}
	private void checkGitHubDepot(Depot depot, boolean verbose) {
		try {
			print("Calling " + depot.getApiRateLimitUrl() ); 
			GitHubRateLimitResponse response = getTelosysProject().checkGitHub(depot.getDefinition());
			printResponse(response, verbose);
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}
	
	/**
	 * @param response
	 */
	private void printResponse(GitHubRateLimitResponse response, boolean verbose) {
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
		print( "Response from " + response.getUrl() ); 
		print( httpStatusWithColor(response) ); 
		print( "Current API rate limit (at " + hhmmss + ") : " ); 
		print( " . remaining  : " + remaining ); 
		print( " . limit      : " + response.getLimit() ); 
		print( " . reset      : " + response.getReset() ); 
		print( " . reset date : " + response.getResetDate() ); 
		if ( remainingValue == 0 ) {
			print( Color.colorize("API rate limit has been reached!", Color.RED_BRIGHT ) );
		}
		if ( verbose ) {
			print( "Response body:" ); 
			print( response.getResponseBody() ); 
		}
	}
	private String httpStatusWithColor(GitHubRateLimitResponse response) {
		String s = "HTTP status : " + response.getHttpStatusCode();
		if ( response.getHttpStatusCode() != 200 ) {
			return Color.colorize(s, Color.RED_BRIGHT );
		}
		return s ;
	}
}
