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

public class GuideCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public GuideCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "guide";
	}

	@Override
	public String getShortDescription() {
		return "Guide" ;
	}
	
	@Override
	public String getDescription() {
		return "Guide ";
	}
	
	@Override
	public String getUsage() {
		return "guide";
	}
		

	@Override
	public String execute(String[] args) {
		if ( confirm("Do you confirm") ) {
			print("YES");
		}
		else {
			print("NO");
		}
		return null ;
	}
	
	protected String guide() {
		StringBuilder sb = new StringBuilder();
		
		appendLine(sb, "1) Initialize the project environment");
		appendLine(sb, " . cd [project-directory]");
		appendLine(sb, " . init");
		
		appendLine(sb, "2) Configure the project (edit the properties)");
		appendLine(sb, " . cd [project-directory]");
		appendLine(sb, " . edit telosys-tools.cfg");

		appendLine(sb, "3) Copy JDBC jar files in [project-directory/TelosysTools/lib]");

		appendLine(sb, "4) Configure the databases ");
		appendLine(sb, " . cd [project-directory/TelosysTools]");
		appendLine(sb, " . edit databases.dbcfg");
		
		appendLine(sb, "5) Check the database configuration");
		appendLine(sb, " . dbtest [db-id]");

		appendLine(sb, "6) Generate the database model");
		
		
		return sb.toString();
	}

}
