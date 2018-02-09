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

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class EnvCommand extends Command {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public EnvCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "env";
	}

	@Override
	public String getShortDescription() {
		return "Environment" ;
	}
	
	@Override
	public String getDescription() {
		return "Environment state";
	}
	
	@Override
	public String getUsage() {
		return "env";
	}
	
	@Override
	public String execute(String[] args) {
		return env();
	}
	
	private String env() {
		Environment environment = getEnvironment();
		
		StringBuilder sb = new StringBuilder();
		
		appendLine(sb, "ENVIRONMENT : ");

		appendLine(sb, ". Operating System name : " + environment.getOperatingSystemName() );
		appendLine(sb, ". Operating System type : " + environment.getOperatingSystemType() );
		appendLine(sb, ". Java version          : " + environment.getJavaVersion() );
		appendLine(sb, ". Editor command        : " + environment.getEditorCommand() );
		appendLine(sb, ". '.jar' file           : " + environment.getJarLocation() );
		appendLine(sb, ". Current directory     : " + environment.getCurrentDirectory() );
		appendLine(sb, ". Current GitHub store  : " + undefinedIfNull(environment.getCurrentGitHubStore()) );
		appendLine(sb, ". Home directory        : " + undefinedIfNull(environment.getHomeDirectory()) );
		appendLine(sb, ". Current model         : " + undefinedIfNull(environment.getCurrentModel()) );
		appendLine(sb, ". Current bundle        : " + undefinedIfNull(environment.getCurrentBundle()) );
		
		return sb.toString();
	}
	
}
