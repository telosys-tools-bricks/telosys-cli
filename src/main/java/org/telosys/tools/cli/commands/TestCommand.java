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

public class TestCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public TestCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String getShortDescription() {
		return "Test" ;
	}
	
	@Override
	public String getDescription() {
		return "Special command just for tests";
	}
	
	@Override
	public String getUsage() {
		return "guide";
	}
		

	@Override
	public String execute(String[] args) {
		if ( confirm("Do you confirm") ) {
			print("YES");
			printGuide();
		}
		else {
			print("NO");
		}
		return null ;
	}
	
	private void printGuide() {
		print( "1) Initialize the project environment");
		print( " . cd [project-directory]");
		print( " . init");
		
		print( "2) Configure the project (edit the properties)");
		print( " . cd [project-directory]");
		print( " . edit telosys-tools.cfg");

		print( "3) Copy JDBC jar files in [project-directory/TelosysTools/lib]");

		print( "4) Configure the databases ");
		print( " . cd [project-directory/TelosysTools]");
		print( " . edit databases.dbcfg");
		
		print( "5) Check the database configuration");
		print( " . dbtest [db-id]");

		print( "6) Generate the database model");
	}

}
