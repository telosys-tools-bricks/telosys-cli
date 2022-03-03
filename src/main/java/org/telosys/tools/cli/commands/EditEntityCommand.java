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
import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Const;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.FileFinder;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

/**
 * 'ee' command
 * 
 * @author Laurent GUERIN
 *
 */
public class EditEntityCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditEntityCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "ee";
	}

	@Override
	public String getShortDescription() {
		return "Edit Entity" ;
	}

	@Override
	public String getDescription() {
		return "Edit an entity file";
	}
	
	@Override
	public String getUsage() {
		return "ee entity-name";
	}

	@Override
	public String execute(String[] args) {
		if ( checkDslModelDefined() ) {
			if ( args.length > 1 ) {
				return editEntityDSL(args[1]);
			}
			else {
				return invalidUsage("entity-name expected");
			}
		}
		return null ;
	}

	/**
	 * Edit the ENTITY FILE  ( eg Car.entity ) 
	 * @param entityName
	 * @return
	 */
	private String editEntityDSL(String entityName) {
		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
//			try {
				//File file = telosysProject.buildDslEntityFile(getCurrentModel(), entityName);
				File file = telosysProject.getDslEntityFile(getCurrentModel(), entityName);// v 3.4.0
				if ( file.exists() && file.getName().equals(entityName) ) { // Check entity name for exact matching (case sensitive)
					return launchEditor(file.getAbsolutePath() );
				}
				else {
					// Try to find the file with abbreviation
					File dir = file.getParentFile() ;
					List<File> files = FileFinder.find(entityName, dir, Const.DSL_ENTITY_FILE_SUFFIX);
					if ( files.isEmpty() ) {
						print("No entity '" + entityName + "' in the current model.");
					}
					else if ( files.size() == 1 ) {
						return launchEditor(files.get(0).getAbsolutePath() ) ;
					}
					else {
						print("Ambiguous name '" + entityName + "' (" + files.size() + " entities found).");
					}
				}
//			} catch (TelosysToolsException e) {
//				printError(e);
//			}
		}
		return null ;
	}

}
