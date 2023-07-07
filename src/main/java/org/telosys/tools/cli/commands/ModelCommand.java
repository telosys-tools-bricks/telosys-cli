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

import jline.console.ConsoleReader;

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;

/**
 * 'm' command
 * 
 * @author Laurent GUERIN
 *
 */
public class ModelCommand extends CommandLevel2 {
	
	private static final String NO_CURRENT_MODEL = "No current model";

	public static final String COMMAND_NAME = "m";
	
	private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList("-none")); 

	/**
	 * Constructor
	 * @param out
	 */
	public ModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getShortDescription() {
		return "Model" ;
	}

	@Override
	public String getDescription() {
		return "Set/print the current model";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " [model-name] [-none]";
	}
	
	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		
		if ( checkArguments(commandArguments, 0, 1 ) && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
			Set<String> activeOptions = getOptions(commandArguments);
			List<String> argsWithoutOptions = removeOptions(commandArguments);
			if ( ! argsWithoutOptions.isEmpty() ) {
				if ( checkHomeDirectoryDefined() ) {
					tryToSetCurrentModel(argsWithoutOptions.get(0));
				}
			}
			else if ( isOptionActive("-none", activeOptions) ) {
				unsetCurrentModel();
				print( NO_CURRENT_MODEL );
			}
			else {
				String modelName = getCurrentModel() ;
				print( modelName != null ? modelName : NO_CURRENT_MODEL );
			}
		}
		return null ;
	}
	
	private String tryToSetCurrentModel(String modelNamePattern) {
		File modelFolder = findModelFolder(modelNamePattern) ;
		// if found 
		if ( modelFolder != null ) {
			setCurrentModel(modelFolder.getName());
			print( "Current model is now '" + getCurrentModel() + "'" );
		}
		return null ;
	}
}
