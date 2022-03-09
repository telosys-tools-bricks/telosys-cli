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

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.yaml.DatabaseDefinition;

import jline.console.ConsoleReader;

public class NewDbModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public NewDbModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "ndbm";
	}

	@Override
	public String getShortDescription() {
		return "New Database Model" ;
	}

	@Override
	public String getDescription() {
		return "New database model from the given database";
	}
	
	@Override
	public String getUsage() {
		return "ndbm model-name database-id";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
//			// Check arguments :
//			// 0 : ndbm 
//			// 1 : ndbm -y | ndbm DbId
//			// 2 : ndbm DbId -y
//			if ( checkArguments(args, 0, 1, 2 ) && checkOptions(args, "-y") ) {
//				String[] newArgs = registerAndRemoveYesOption(args);
//				// newArgs = args without '-y' if any
//				execute2(newArgs);				
//			}
//			print("This command is temporarily unusable");					
			if ( checkArguments(args, 2 ) ) {
				execute(args[1], args[2]);				
			}
		}
		return null;
	}
		
	private void execute(String modelName, String databaseId) {
		TelosysProject telosysProject = getTelosysProject();
		try {
			DatabaseDefinition databaseDefinition = telosysProject.getDatabaseDefinition(databaseId);
			if ( databaseDefinition != null ) {
				newDatabaseModel(modelName, databaseDefinition);
			}
			else {
				print("No database with id '" + databaseId + "'");					
			}
		} catch (Exception e) {
			print("Cannot get database definition (id '" + databaseId + "')" );
			print(e.getMessage());
		}
	}
	
	private void newDatabaseModel(String modelName, DatabaseDefinition databaseDefinition) {
		TelosysProject telosysProject = getTelosysProject();
		
		// TODO
		//telosysProject.createNewDatabaseModel(modelName);
	}
	
//	private void newDatabaseModel(Integer id) {
//		try {
//			TelosysProject telosysProject = getTelosysProject();
////			File dbModelFile = telosysProject.getDbModelFile(id);
//			String existingModelFile = null ;
//			if ( dbModelFile != null && dbModelFile.exists() ) {
//				existingModelFile = dbModelFile.getName() ;
//			}
//
//			print("WARNING : ");
//			print("  You're about to create a new model from a database");
//			print("  This operation can take a lot of time (especially for Oracle databases)");
//			if ( existingModelFile != null ) {
//				print("  The model file '" + dbModelFile.getName() + "' already exists.");
//				print("  The existing file will be overridden !");
//			}
//			if ( confirm("Do you really want to launch the model creation ? ") ) {
//				print("");
//				print("Generating the new model...");
////				telosysProject.createNewDbModel(id) ;
//				print("New model created.");
//			}
//		} catch (TelosysToolsException e) {
//			printError(e);
//		}
//	}
}
