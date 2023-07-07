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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class DeleteModelCommand extends CommandLevel2 {

	public static final String COMMAND_NAME = "dm";
	
	private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList(YES_OPTION)); // -y 

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
		return COMMAND_NAME;
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
		return COMMAND_NAME + " [model-part-name] [-y]";
	}

	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		if ( checkHomeDirectoryDefined() ) {
			// Check arguments :
			// 0 : dm 
			// 1 : dm -y | dm model-name
			// 2 : dm model-name -y
			if ( checkArguments(commandArguments, 0, 1, 2 ) && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
//				String[] newArgs = registerAndRemoveYesOption(args);
//				// newArgs = args without '-y' if any
//				execute2(newArgs);
				Set<String> activeOptions = getOptions(commandArguments);
				registerYesOptionIfAny(activeOptions);
				List<String> argsWithoutOptions = removeOptions(commandArguments);
				executeDeleteModel(argsWithoutOptions);
				
			}
		}
		return null ;
	}

//	private void execute2(String[] args) {
//		File modelFolder = findModelFolder(args);
//		if ( modelFolder != null) {
//			deleteModel(modelFolder);
//		}
//	}
	private void executeDeleteModel(List<String> args) {
		File modelFolder = null;
		if ( args.size() == 0 ) {
			// dm  (no arg) => delete current model 
			modelFolder = findModelFolder();
		}
		else if ( args.size() == 1 ) {
			// dm model-name => find model
			modelFolder = findModelFolder(args.get(0));
		}
		else {
			print("invalid arguments"); // not supposed to happen
			return;
		}
		if ( modelFolder != null ) {
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
