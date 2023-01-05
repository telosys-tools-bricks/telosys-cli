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

import jline.console.ConsoleReader;

import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.generic.model.Model;

/**
 * 'cm' command
 * 
 * @author Laurent GUERIN 
 *
 */
public class CheckModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public CheckModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "cm";
	}

	@Override
	public String getShortDescription() {
		return "Check Model" ;
	}

	@Override
	public String getDescription() {
		return "Check the current/given model";
	}
	
	@Override
	public String getUsage() {
		return "cm [model-name]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			File modelFolder = findModelFolder(args);
			if ( modelFolder != null ) {
				checkModel(modelFolder);
			}
		}
		return null;
	}
		
	private void checkModel(File modelFolder) {
		// Just try to load the model to check it 
		Model model = loadModel(modelFolder);
		if ( model != null ) {
			int n = model.getEntities() != null ? model.getEntities().size() : 0 ; 
			print( "Model OK ('" + modelFolder.getName() + "' loaded : " + n + " entities)" );
		}
	}
}
