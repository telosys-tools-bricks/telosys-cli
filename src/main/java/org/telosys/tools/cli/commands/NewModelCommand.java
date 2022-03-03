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

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class NewModelCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public NewModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader,environment);
	}
	
	@Override
	public String getName() {
		return "nm";
	}

	@Override
	public String getShortDescription() {
		return "New Model" ;
	}
	
	@Override
	public String getDescription() {
		return "New Telosys DSL model";
	}
	
	@Override
	public String getUsage() {
		return "nm [model-name]";
	}
	
	@Override
	public String execute(String[] args) {
				
		if ( args.length > 1 ) {
			if ( checkHomeDirectoryDefined() ) {
				return newModel(args[1]);
			}
			else {
				return null ;
			}
		}
		else {
			return "No model name";
		}
	}

	private String newModel(String modelName) {
//		String projectFullPath = getCurrentHome();
//		TelosysProject telosysProject = new TelosysProject(projectFullPath);
		File modelFile = getTelosysProject().createNewDslModel(modelName);
//		setCurrentModel(modelFile);
		setCurrentModel(modelName);
		return "Model '" + modelName + "' created (" + modelFile.getName() + "), current model is now '" 
				+ modelName + "'" ;
	}
}
