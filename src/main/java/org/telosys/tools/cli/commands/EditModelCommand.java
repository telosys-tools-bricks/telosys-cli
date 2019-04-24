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

import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

/**
 * 'em' command
 * 
 * @author Laurent GUERIN
 *
 */
public class EditModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public EditModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "em";
	}

	@Override
	public String getShortDescription() {
		return "Edit Model" ;
	}

	@Override
	public String getDescription() {
		return "Edit the current/given model";
	}
	
	@Override
	public String getUsage() {
		return "em [model-name]";
	}

	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			File modelFile = findModelFile(args) ;
			// if found => launch the editor
			if ( modelFile != null ) {
				return launchEditor(modelFile.getAbsolutePath());
			}
		}
		return null ;
	}

//	/**
//	 * Edit the model file for the given model name
//	 * @param modelName ( eg 'car.model', 'bookstore.dbmodel', 'bookstore.dbrep' )
//	 * @return
//	 */
//	private String editModel(String modelName) {
//		TelosysProject telosysProject = getTelosysProject();
//		if ( telosysProject != null ) {
//			try {
//				File file = telosysProject.getModelFile(modelName);
//				return launchEditor(file.getAbsolutePath() );
//			} catch (TelosysToolsException e) {
//				printError(e);
//			}
//		}
//		return null ;
//	}

}
