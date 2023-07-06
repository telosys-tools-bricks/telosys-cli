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

import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.yaml.DatabaseDefinition;
import org.telosys.tools.commons.dbcfg.yaml.DatabaseDefinitions;

import jline.console.ConsoleReader;

public class ListDatabasesCommand extends Command {

	public static final String COMMAND_NAME = "ldb";
	
	private static final String SEPARATOR = "---------------------------------------------" ;
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListDatabasesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getShortDescription() {
		return "List Databases" ;
	}

	@Override
	public String getDescription() {
		return "List the databases configurations";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " [database-id]";
	}

	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		
		if ( checkHomeDirectoryDefined() && checkArguments(commandArguments, 0, 1 ) ) {
			if ( commandArguments.isEmpty()) {
				// no database-id
				listDatabases(null);
			}
			else {
				// database-id
				listDatabases(commandArguments.get(0));
			}
		}
		return null ;
	}

	private void listDatabases(String id) {
		TelosysProject telosysProject = getTelosysProject();
		try {
			if ( id != null ) {
				DatabaseDefinition dbConfig = telosysProject.getDatabaseDefinition(id);
				if ( dbConfig != null ) {
					printDbConfig(dbConfig);
				}
				else {
					print("Database " + id + " is not defined."  );
				}
			}
			else {
				DatabaseDefinitions databasesConfigurations = telosysProject.getDatabaseDefinitions();
				List<DatabaseDefinition> databases = databasesConfigurations.getDatabases();
				if ( databases.isEmpty() ) {
					print("No database defined." );
				}
				else {
					print(databasesConfigurations.getDatabases().size()+ " database(s) defined" );
					for ( DatabaseDefinition dbConfig : databases ) {
						print(SEPARATOR);
						printDbConfig(dbConfig);
					}
					print(SEPARATOR);
				}
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
	}

	private void printDbConfig(DatabaseDefinition db) {

		print( " . Database id   : '" + db.getId() + "' ");
		print( " . Name          : " + db.getName() );
		print( " . Type          : " + db.getType() );
		
		print( " . Connection : " );
		print( "   - JDBC URL      : " + db.getUrl()  );
		print( "   - Driver class  : " + db.getDriver() );
		print( "   - User          : " + db.getUser() );
		print( "   - Password      : " + db.getPassword() );
		
		print( " . Metadata : " );
		print( "   - Catalog            : " + db.getCatalog() );
		print( "   - Schema             : " + db.getSchema() );
		print( "   - Table types        : " + db.getTableTypes() );
		print( "   - Table name pattern : " + db.getTableNamePattern() );
		print( "   - Table name exclude : " + db.getTableNameExclude() );
		print( "   - Table name include : " + db.getTableNameInclude() );

		print( " . Model creation options : " );
		print( "   - links ManyToOne  : " + db.isLinksManyToOne() );
		print( "   - links OneToMany  : " + db.isLinksOneToMany() );
		print( "   - links ManyToMany : " + db.isLinksManyToMany() );		
		print( "   - db comment       : " + db.isDbComment() );
		print( "   - db catalog       : " + db.isDbCatalog() );
		print( "   - db schema        : " + db.isDbSchema() );
		print( "   - db table         : " + db.isDbTable() );
		print( "   - db view          : " + db.isDbView() );
		print( "   - db name          : " + db.isDbName() );
		print( "   - db type          : " + db.isDbType() );
		print( "   - db default value : " + db.isDbDefaultValue() );
	}
}
