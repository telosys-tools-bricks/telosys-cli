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

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.github.GitHubClient;
import org.telosys.tools.commons.http.HttpClient;

import jline.console.ConsoleReader;

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
		return "env [-s] [-http] \n"
		  + "   -s    : system properties \n" 	
		  + "   -http : http components version and https.protocols system property " ; 	
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			// env arg1
			String arg = args[1] ;
			if ( "-s".equals(arg) ) {
				return systemProperties();
			}
			else if ( "-http".equals(arg) ) {
				return httpConfig();
			}
			else {
				return "Invalid argument '" + arg + "'" ;
			}
		}
		else {
			// env
			return env();
		}
	}
	
	private String env() {
		Environment environment = getEnvironment();
		
		String telosysCliCfgAbsolutePath = environment.getTelosysCliConfigFileAbsolutePath() ;
		if ( telosysCliCfgAbsolutePath == null ) {
			telosysCliCfgAbsolutePath = "(not found)";
		}
		
		StringBuilder sb = new StringBuilder();
		
		appendLine(sb, "Telosys-CLI : ");
		appendLine(sb, ". Home directory        : " + undefinedIfNull(environment.getHomeDirectory()) );
		appendLine(sb, ". Current directory     : " + environment.getCurrentDirectory() );
		appendLine(sb, ". Current model         : " + undefinedIfNull(environment.getCurrentModel()) );
		appendLine(sb, ". Current bundle        : " + undefinedIfNull(environment.getCurrentBundle()) );
		appendLine(sb, ". Current GitHub store  : " + undefinedIfNull(environment.getCurrentGitHubStore()) );
		
		appendLine(sb, ". CLI config file       : " + telosysCliCfgAbsolutePath );
		appendLine(sb, ". Editor command        : " + undefinedIfNull(environment.getEditorCommand()) );
		appendLine(sb, ". File Explorer command : " + undefinedIfNull(environment.getFileExplorerCommand()) );
		
		appendLine(sb, "Operating System : ");
		appendLine(sb, ". Operating System name : " + environment.getOperatingSystemName() );
		appendLine(sb, ". Operating System type : " + environment.getOperatingSystemType() );

		appendLine(sb, "Java : ");
		appendLine(sb, ". Java version          : " + environment.getJavaVersion() );
		appendLine(sb, ". '.jar' file           : " + environment.getJarLocation() );		
		appendLine(sb, ". Java classpath : " );
		classPath(sb);
		return sb.toString();
	}
	
	private void classPath(StringBuilder sb) {
		String classpath = System.getProperty("java.class.path");
		String pathSeparator = System.getProperty("path.separator");
		String[] classpathEntries = classpath.split(pathSeparator);
		for ( String s : classpathEntries ) {
			appendLine(sb, "    " + s);
		}
	}
	
	private String systemProperties() {
		List<String> lines = new LinkedList<>();
		Properties p = System.getProperties();
		Enumeration<?> keys = p.keys();
		while (keys.hasMoreElements()) {
		    String key = (String)keys.nextElement();
		    String value = (String)p.get(key);
		    lines.add(". " + key + " = " + value);
		}
		java.util.Collections.sort(lines);
		
		print("SYSTEM PROPERTIES : ");
		for ( String l : lines ) {
			print(l);
		}
		return null ;
	}

	private static final String HTTPS_PROTOCOLS = "https.protocols" ;
	private String httpConfig() {
		print("HTTP CONFIGURATION : ");
		print(". HttpClient version   : " + HttpClient.VERSION );
		print(". GitHubClient version : " + GitHubClient.VERSION );
		print(". System property '" + HTTPS_PROTOCOLS + "' : " + System.getProperty(HTTPS_PROTOCOLS) );
		return null ;
	}
}
