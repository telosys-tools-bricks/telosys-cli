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

import java.io.File;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;

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
		return "New model for the current/given database";
	}
	
	@Override
	public String getUsage() {
		return "ndbm [database-id]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				// database-id
				String argId = args[1];
				Integer id = StrUtil.getIntegerObject(argId);
				if ( id != null ) {
					newDatabaseModel(id);
				}
				else {
					print("Invalid database id '" + argId + "'");					
				}
			}
			else {
				// no database-id
				newDatabaseModel(null);
			}			
		}
		return null;
	}
		
	private String newDatabaseModel(Integer id) {
		try {
			TelosysProject telosysProject = getTelosysProject();
			File dbModelFile = telosysProject.getDbModelFile(id);
			if ( dbModelFile != null && dbModelFile.exists() ) {
				print("WARNING : Model file '" + dbModelFile.getName() + "' already exists");
				if ( ! confirm("Do you really want to overwrite it ?") ) {
					return null ;
				}
			}
			print("Generating new model...");
			telosysProject.createNewDbModel(id) ;
			print("New model created.");
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}
}
