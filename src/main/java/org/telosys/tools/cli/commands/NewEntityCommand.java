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
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class NewEntityCommand extends CommandLevel2 {

	/**
	 * Constructor
	 * @param out
	 */
	public NewEntityCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "ne";
	}

	@Override
	public String getShortDescription() {
		return "New Entity" ;
	}

	@Override
	public String getDescription() {
		return "Create a new entity in the current model";
	}
	
	@Override
	public String getUsage() {
		return "ne [entity-name]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() && checkCurrentModelDefinedAndExists() ) {
			if ( args.length > 1 ) {
				return newEntity(args[1]);
			}
			else {
				return invalidUsage("entity-name expected");
			}
		}
		return null ;
	}

	private String newEntity(String entityName) {

		TelosysProject telosysProject = getTelosysProject();
		String modelName = getCurrentModel();
		telosysProject.createNewDslEntity(modelName, entityName);
		print("Entity '"+ entityName + "' created.");		
		return null ;
	}

}
