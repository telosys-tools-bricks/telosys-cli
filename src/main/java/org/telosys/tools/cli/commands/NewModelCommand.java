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

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.LastError;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

public class NewModelCommand extends Command {
	
	public static final String COMMAND_NAME = "nm";	
	
	/**
	 * Constructor
	 * @param out
	 */
	public NewModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader,environment);
	}
	
	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getShortDescription() {
		return "New Model" ;
	}
	
	@Override
	public String getDescription() {
		return "Create a new Telosys model (optionally from a database)";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " model-name [database-id]";
	}
	
	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		
		if ( checkHomeDirectoryDefined() && checkArguments(commandArguments, 1, 2) ) {
			if ( commandArguments.size() == 1 ) {
				newModel(commandArguments.get(0));
			}
			else if ( commandArguments.size() == 2 ) {
				newModelFromDatabase(commandArguments.get(0), commandArguments.get(1));
			}
		}
		return null ;
	}

	private void newModel(String modelName) {
		getTelosysProject().createNewDslModel(modelName);
		afterCreationOK(modelName);
	}

	private void newModelFromDatabase(String modelName, String databaseId) {
		try {
			getTelosysProject().createNewDslModelFromDatabase(modelName, databaseId);
			afterCreationOK(modelName);
		} catch (TelosysToolsException e) {
			LastError.setError(e);
			print("Cannot create model '" + modelName + "'" );
			print(e.getMessage());
		}
	}
	
	private void afterCreationOK(String modelName) {
		setCurrentModel(modelName);
		print( "Model '" + modelName + "' created. Current model is now '"+ modelName + "'" );
	}
}
