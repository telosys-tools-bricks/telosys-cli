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

import java.io.File;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.http.HttpSystemConfig;

import jline.console.ConsoleReader;

public class ProxyCommand extends Command {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public ProxyCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "proxy";
	}

	@Override
	public String getShortDescription() {
		return "Proxy configuration" ;
	}
	
	@Override
	public String getDescription() {
		return "Get current proxy configuration ";
	}
	
	@Override
	public String getUsage() {
		return "proxy";
	}
	
	@Override
	public String execute(String[] args) {
		// Reset configuration from config file
		String configFile = getTelosysToolsCfgFullPath();
		if ( configFile != null ) {
			HttpSystemConfig.init(new File(configFile));
		}
		// Get and print configuration
		for ( String s : HttpSystemConfig.getCurrentHttpConfig() ) {
			print(s);
		}
		for ( String s : HttpSystemConfig.getCurrentProxyConfig() ) {
			print(s);
		}
		return null ;
	}
	
}
