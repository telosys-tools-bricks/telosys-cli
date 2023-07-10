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

public class ListModelsCommand extends CommandLevel2 {
	
	public static final String COMMAND_NAME = "lm";

	/**
	 * Constructor
	 * @param out
	 */
	public ListModelsCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getShortDescription() {
		return "List Models" ;
	}

	@Override
	public String getDescription() {
		return "List the project models";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " [name-part1 name-part2 ...]";
	}

	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		if ( checkHomeDirectoryDefined() ) {
			listModels(commandArguments);
		}
		return null ;
	}
	
	private void listModels(List<String> commandArguments) {
		List<File> models = findModelFolders(commandArguments);
		if ( models.isEmpty() ) {
			print("No model found.") ;
		}
		else {
			print( models.size() + " model(s) :") ;
			printListOfFiles(models) ;
		}
	}

//	private void listModels() {
//		TelosysProject telosysProject = getTelosysProject();
//		List<File> files = telosysProject.getModels();
//		if ( files.isEmpty() ) {
//			print("No model found.") ;
//		}
//		else {
//			// Convert files to file names (strings)
//			LinkedList<String> names = new LinkedList<>();
//			for ( File f : files ) {
//				names.add(f.getName());
//			}
//			// Print file names 
//			print( names.size() + " model(s) :") ;
//			printList(names) ;
//		}
//	}
//
}
