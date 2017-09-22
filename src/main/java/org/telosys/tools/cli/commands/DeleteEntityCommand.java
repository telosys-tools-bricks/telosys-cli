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

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class DeleteEntityCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public DeleteEntityCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "de";
	}

	@Override
	public String getShortDescription() {
		return "Delete Entity" ;
	}

	@Override
	public String getDescription() {
		return "Deletes the given entity in the current model";
	}
	
	@Override
	public String getUsage() {
		return "de [entity-name]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkModelDefined() ) {
			if ( args.length > 1 ) {
				return deleteEntity(args[1]);
			}
			else {
				return invalidUsage("entity-name expected");
			}
		}
		return null ;
	}

	private String deleteEntity(String entityName) {

		TelosysProject telosysProject = getTelosysProject();
		String modelName = getCurrentModel();
		try {
			if ( telosysProject.deleteDslEntity(modelName, entityName) ) {
				print("Entity '"+ entityName + "' deleted.");
			}
			else {
				print("Entity '"+ entityName + "' not found.");
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

}
