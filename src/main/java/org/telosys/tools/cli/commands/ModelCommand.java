/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
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

import jline.console.ConsoleReader;

import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;

/**
 * 'm' command
 * 
 * @author Laurent GUERIN
 *
 */
public class ModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public ModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "m";
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
		return "m [model-name]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( args.length > 1 ) {
			if ( checkHomeDirectoryDefined() ) {
				return tryToSetCurrentModel(args[1]);
			}
		}
		else {
			return undefinedIfNull(getCurrentModel());
		}
		return null ;
	}
	
	private String tryToSetCurrentModel(String modelNamePattern) {
		File modelFile = findModelFile(modelNamePattern) ;
		// if found => launch the editor
		if ( modelFile != null ) {
			setCurrentModel(modelFile.getName());
			return "Current model is now '" + getCurrentModel() + "'";
		}
		return null ;
	}
}
