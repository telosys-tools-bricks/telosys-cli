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

import org.telosys.tools.api.MetaDataOptions;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commands.util.CheckDatabaseCommandOptions;
import org.telosys.tools.cli.observer.DbMetadataObserver;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DbConnectionStatus;
import org.telosys.tools.commons.dbcfg.yaml.DatabaseDefinition;

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
		return "cdb database-id [-v] [-i -t -c -pk -fk -s -cat]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) { // at least 2 args expected
				// build command options if any
				CheckDatabaseCommandOptions options = new CheckDatabaseCommandOptions(args);
				if ( options.hasErrors() ) {
					// Invalid options(s)
					for ( String s : options.getErrors() ) {
						print(s);
					}
				}
				else {
					// Option(s) OK => check database with options
					String databaseId = args[1] ;
					if ( databaseIsDefined(databaseId) ) {
						DbMetadataObserver.setActive(true);
						checkDatabase(args[1], options );
						DbMetadataObserver.setActive(false);
					}
				}
			}
			else {
				return invalidUsage("database-id expected");
			}
		}
		return null;
	}

	private boolean databaseIsDefined(String databaseId) {
		try {
			boolean exists = getTelosysProject().databaseIsDefined(databaseId);
			if ( ! exists ) {
				print("Database '" + databaseId + "' is not defined"); 
			}
			return exists ;
		} catch (TelosysToolsException e) {
			printError(e);
			return false;
		}
	}
	private String checkDatabase(String databaseId, CheckDatabaseCommandOptions arguments) {
		try {
			print("Checking database '" + databaseId + "'..."); 
			if ( arguments.hasMetaDataOptions() ) {
				getMetaData(databaseId, arguments.getMetaDataOptions() ) ;
			}
			else if ( arguments.hasVerboseOption() ) {
				checkDatabaseConnectionVerbose(databaseId);
			}
			else {
				checkDatabaseConnection(databaseId);
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}
	
	/**
	 * Try to connect and get metadata
	 * @param databaseId
	 * @param options
	 * @throws TelosysToolsException
	 */
	private void getMetaData(String databaseId, MetaDataOptions options) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		DatabaseDefinition databaseDefinition = telosysProject.getDatabaseDefinition(databaseId);
		print("Database definition : ");
		print(". id :" + databaseDefinition.getId());
		print(". table pattern : " + databaseDefinition.getTableNamePattern());
		// Meta-data required
		String metadata = telosysProject.getMetaData(databaseId, options);
		print(metadata); 
	}
	
	/**
	 * Just try to connect (get a connection and close it) not verbose, no metadata
	 * @param databaseId
	 * @throws TelosysToolsException
	 */
	private void checkDatabaseConnection(String databaseId) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		// Just try to connect to database (no meta-data)
		telosysProject.checkDatabaseConnection(databaseId) ;
		print("OK, connection test is successful.");
	}

	/**
	 * Try to connect and get status (get information from connexion) 
	 * @param databaseId
	 * @throws TelosysToolsException
	 */
	private void checkDatabaseConnectionVerbose(String databaseId) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		// Just try to connect to database (no meta-data)
		DbConnectionStatus conStatus = telosysProject.checkDatabaseConnectionWithStatus(databaseId) ;
		print("OK, connection test is successful.");
		print(" . product name    : " + conStatus.getProductName() );
		print(" . product version : " + conStatus.getProductVersion() );
		print(" . catalog         : " + conStatus.getCatalog() );
		print(" . schema          : " + conStatus.getSchema() );
		print(" . autocommit      : " + conStatus.isAutocommit() );
	}
}
