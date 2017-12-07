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

import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;

import jline.console.ConsoleReader;

public class ListDatabasesCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListDatabasesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "ldb";
	}

	@Override
	public String getShortDescription() {
		return "List Databases" ;
	}

	@Override
	public String getDescription() {
		return "List the databases";
	}
	
	@Override
	public String getUsage() {
		return "ldb [id]";
	}

	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				// database-id
				String argId = args[1];
				Integer id = StrUtil.getIntegerObject(argId);
				if ( id != null ) {
					return listDatabases(id);
				}
				else {
					print("Invalid database id '" + argId + "'");					
				}
			}
			else {
				// no database-id
				return listDatabases(null);
			}			
		}
		return null ;
	}

	private String listDatabases(Integer id) {
		TelosysProject telosysProject = getTelosysProject();
		try {
			StringBuilder sb = new StringBuilder();
			if ( id != null ) {
				DatabaseConfiguration dbConfig = telosysProject.getDatabaseConfiguration(id);
				if ( dbConfig != null ) {
					printDbConfig(sb, dbConfig);
				}
				else {
					appendLine(sb, "Database " + id + " is not defined."  );
				}
			}
			else {
				DatabasesConfigurations databasesConfigurations = telosysProject.getDatabasesConfigurations();
				appendLine(sb, databasesConfigurations.getNumberOfDatabases() + " database(s) defined" );
				appendLine(sb, "Default database is '" + databasesConfigurations.getDatabaseDefaultId() + "'" );
				List<DatabaseConfiguration> databases = databasesConfigurations.getDatabaseConfigurationsList();
				if ( databases.size() > 0 ) {
					for ( DatabaseConfiguration dbConfig : databases ) {
						printDbConfig(sb, dbConfig);
					}
				}
				else {
					appendLine(sb, "No database defined." );
				}
			}
			return sb.toString();
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

	private void printDbConfig(StringBuilder sb, DatabaseConfiguration dbConfig) {
		appendLine(sb, " ");
		appendLine(sb, "Database '" + dbConfig.getDatabaseId() + "' : " + dbConfig.getDatabaseName() );
		appendLine(sb, " . Driver class  : " + dbConfig.getDriverClass() );
		appendLine(sb, " . JDBC URL      : " + dbConfig.getJdbcUrl()  );
		appendLine(sb, " . User          : " + dbConfig.getUser() );
		appendLine(sb, " . Password      : " + dbConfig.getPassword() );
		appendLine(sb, " . Type name     : " + dbConfig.getTypeName() );
		appendLine(sb, " . Dialect       : " + dbConfig.getDialect() );
		
		appendLine(sb, " . Catalog       : " + dbConfig.getMetadataCatalog() );
		appendLine(sb, " . Schema        : " + dbConfig.getMetadataSchema() );
		appendLine(sb, " . Table filters : " );
		appendLine(sb, "   - pattern : " + dbConfig.getMetadataTableNamePattern() );
		appendLine(sb, "   - types   : " + dbConfig.getMetadataTableTypes() );
		appendLine(sb, "   - exclude : " + dbConfig.getMetadataTableNameExclude() );
		appendLine(sb, "   - include : " + dbConfig.getMetadataTableNameInclude() );
	}
}
