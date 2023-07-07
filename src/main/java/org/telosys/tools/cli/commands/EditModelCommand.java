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

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

/**
 * 'em' command
 * 
 * @author Laurent GUERIN
 *
 */
public class EditModelCommand extends CommandLevel2 {

	public static final String COMMAND_NAME = "em";

	/**
	 * Constructor
	 * @param out
	 */
	public EditModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
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
		return COMMAND_NAME + " [model-name]";
	}

	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);

		if ( checkHomeDirectoryDefined() && checkArguments(commandArguments, 0, 1 ) ) {
			File modelInfoFile = null;
			if ( commandArguments.isEmpty() ) {
				modelInfoFile = getCurrentModelInfoFile();
			}
			else {
				modelInfoFile = findModelInfoFile(commandArguments.get(0));
			}
			//File modelFile = findModelFile(commandArguments);
			// if found => launch the editor
			if ( modelInfoFile != null ) {
				return launchEditor(modelInfoFile.getAbsolutePath());
			}
		}
		return null ;
	}

	private File findModelInfoFile(String modelNamePattern) {
		// 1) Find model folder
		File modelFolder = findModelFolder(modelNamePattern);
		if (modelFolder != null) {
			// 2) Search model info file in folder
			File modelInfoFile = getTelosysProject().getModelInfoFile(modelFolder);
			if ( modelInfoFile.exists() ) {
				return modelInfoFile;
			}
			else {
				print("No model file in model '" + modelFolder.getName() + "'") ;
				return null;
			}
		}
		return null;
	}
}
