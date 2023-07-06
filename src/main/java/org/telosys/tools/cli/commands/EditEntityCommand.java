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
		if ( checkModelDefined() ) {
			if ( args.length > 1 ) {
				return editEntity(args[1]);
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
	private String editEntity(String entityName) {
		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
			File modelFolder = telosysProject.getModelFolder(getCurrentModel());
			// Try to find one or more files matching the given name or part of name
			FileFinder fileFinder = new FileFinder(modelFolder, Const.DSL_ENTITY_FILE_SUFFIX);
			List<File> files = fileFinder.find(entityName);
			if ( files.isEmpty() ) {
				print("No entity matching '" + entityName + "' in the current model.");
			}
			else if ( files.size() == 1 ) {
				return launchEditor(files.get(0).getAbsolutePath() ) ;
			}
			else {
				print("Ambiguous name '" + entityName + "' (" + files.size() + " entities found).");
			}
		}
		return null ;
	}

}
