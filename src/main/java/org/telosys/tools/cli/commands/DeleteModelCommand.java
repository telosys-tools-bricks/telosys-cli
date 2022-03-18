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

public class DeleteModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public DeleteModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "dm";
	}

	@Override
	public String getShortDescription() {
		return "Delete Model" ;
	}

	@Override
	public String getDescription() {
		return "Delete the current/given model";
	}
	
	@Override
	public String getUsage() {
		return "dm [model-name] [-y]";
	}

	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			// Check arguments :
			// 0 : dm 
			// 1 : dm -y | dm model-name
			// 2 : dm model-name -y
			if ( checkArguments(args, 0, 1, 2 ) && checkOptions(args, "-y") ) {
				String[] newArgs = registerAndRemoveYesOption(args);
				// newArgs = args without '-y' if any
				execute2(newArgs);				
			}
		}
		return null ;
	}

	private void execute2(String[] args) {
		File modelFolder = findModelFolder(args);
		if ( modelFolder != null) {
			deleteModel(modelFolder);
		}
	}

	private void deleteModel(File modelFolder) {
		String modelName = modelFolder.getName();
		if ( confirm("Do you really want to delete model '" + modelName + "'") ) {
			// delete model
			getTelosysProject().deleteDslModel(modelFolder);
			// If the current model has been deleted => update env & prompt
			if ( isCurrentModel(modelName) ) {
				unsetCurrentModel();
			}
		}
	}

}
