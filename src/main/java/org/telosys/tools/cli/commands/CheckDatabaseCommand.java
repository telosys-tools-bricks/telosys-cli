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

import org.telosys.tools.api.MetaDataOptions;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commands.util.CheckDatabaseArguments;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

public class CheckDatabaseCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public CheckDatabaseCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "cdb";
	}

	@Override
	public String getShortDescription() {
		return "Check Database" ;
	}

	@Override
	public String getDescription() {
		return "Check the current/given database";
	}
	
	@Override
	public String getUsage() {
		return "cdb [database-id]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			CheckDatabaseArguments arguments = new CheckDatabaseArguments(args);
			if ( arguments.hasErrors() ) {
				for ( String s : arguments.getErrors() ) {
					print(s);
				}
			}
			else {
				checkDatabase(arguments.getDatabaseId(), arguments.getOptions() );
			}
		}
		return null;
	}
		
	private String checkDatabase(Integer id, MetaDataOptions options) {
		try {
			checkDatabaseConnection(id, options) ;
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}
	
	private void checkDatabaseConnection(Integer id, MetaDataOptions options) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		if ( options.hasOptions() ) {
			// Meta-data required
			print( telosysProject.getMetaData(id, options) );
		}
		else {
			// Just try to connect to database (no meta-data)
			if ( telosysProject.checkDatabaseConnection(id, options) ) {
				print("OK, connection test is successful.");
			}
			else {
				print("ERROR: cannot connect to databse.");
			}
		}
	}
}
