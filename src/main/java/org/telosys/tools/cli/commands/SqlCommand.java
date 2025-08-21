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
import java.nio.file.Path;
import java.sql.Connection;

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.jdbc.SqlScriptRunner;

import jline.console.ConsoleReader;

public class SqlCommand extends CommandLevel2 {

	/**
	 * Constructor
	 * @param out
	 */
	public SqlCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "sql";
	}

	@Override
	public String getShortDescription() {
		return "SQL script execution" ;
	}

	@Override
	public String getDescription() {
		return "Execute SQL script (absolute or relative file path) on the given database";
	}
	
	@Override
	public String getUsage() {
		return "sql database-id sql-file" ;
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			// Check arguments :
			// 0 : sql  
			// 1 : db-id
			// 2 : sql-file
			if ( args.length == 3 ) {
				// args OK => launch command
				launchSqlCommand(args[1], args[2]);
			}
			else {
				return invalidUsage("syntax is '" + getUsage() + "'");
			}
		}
		return null;
	}

	private void launchSqlCommand(String databaseId, String sqlFileName) {
		if ( databaseIsDefined(databaseId) ) {
			File sqlFile = findSqlFile(sqlFileName) ;
			if ( sqlFile != null ) {
				try (Connection conn = getTelosysProject().getDatabaseConnection(databaseId) ) {
					executeSqlScript(conn, sqlFile);
				}
				catch(Exception e) { // catch all exceptions: SQLException, TelosysToolsException, any other RuntimeException
					printError(e);
				}
			}
			else {
				printError("File '" + sqlFileName + "' not found"); 
			}
		}
	}
	
	private boolean databaseIsDefined(String databaseId) {
		try {
			boolean exists = getTelosysProject().databaseIsDefined(databaseId);
			if ( ! exists ) {
				printError("Database '" + databaseId + "' is not defined"); 
			}
			return exists ;
		} catch (TelosysToolsException e) {
			printError(e);
			return false;
		}
	}
	
//	private boolean fileExists(String fileName) {
//		File file = new File(fileName);
//		return file.exists();
//	}

	private File findSqlFile(String sqlFileName) {
//		File file = null;
//		Path path = Paths.get(sqlFileName);
//		if (path.isAbsolute()) {
//			file = path.toFile();
//		}
//		else {
//			String currentDir = "xxx";
//			Path cwd = Paths.get(currentDir);
//			// Resolve the filename against the given current dir
//            Path path2 = cwd.resolve(sqlFileName).normalize();
//            file = path2.toFile();
//			//file = getFileInHome(sqlFileName);
//		}
		// Current directory in Telosys 
		String currentDir = getCurrentDirectory();
		// Build absolute path in case of relative path (with or without '.' or '..')
		Path absolutePath = FileUtil.getAbsolutePath(sqlFileName, currentDir);
		File file = absolutePath.toFile();
		if ( file != null && file.exists() ) {
			return file;
		}
		else {
			return null;
		}
	}

	public void executeSqlScript(Connection conn, File sqlFile) {
		SqlScriptRunner sqlScriptRunner = new SqlScriptRunner(conn);
		try {
			print("Running SQL script : " + sqlFile);
			sqlScriptRunner.runScript(sqlFile);
			print("SQL script executed successfully.");
		} catch (Exception e) {
			printError(e.getMessage());
		}
	}
	
}
