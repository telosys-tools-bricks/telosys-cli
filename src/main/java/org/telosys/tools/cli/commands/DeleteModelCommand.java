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

import org.telosys.tools.api.ApiUtil;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;

import jline.console.ConsoleReader;

public class DeleteModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
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
		return "dm [model-name]";
	}

	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {			
				return deleteModel(args[1]);
			}
			else {
				if ( checkModelDefined() ) {
					return deleteModel(getCurrentModel());
				}
			}
		}
		return null ;
	}

	private String deleteModel(String modelName) {
		printDebug("deleteModel('" + modelName + "')");
		File modelFile = getModelFile(modelName);
		if ( modelFile != null ) {
			printDebug("modelFile = " + modelFile.getAbsolutePath() );
			if ( confirm("Do you realy want to delete model '" + modelName + "'") ) {
				deleteModel(modelFile );
				// If the current model has been deleted => update env & prompt
				if ( modelName.equals(getCurrentModel())) {
					setCurrentModel(null);
				}
			}
		}
		return null ;
	}
	
	private void deleteModel(File modelFile ) {
		
		printDebug("deleteModel('" + modelFile + "')");
		if ( ApiUtil.isDslModelFile(modelFile) ) {
			// Delete DSL MODEL
			print("deleting DSL model '" + modelFile.getName() + "' ..." );
			TelosysProject telosysProject = getTelosysProject();			
			try {
				telosysProject.deleteDslModel(modelFile);
				print("Model '" + modelFile.getName() + "' deleted." );
			} catch (Exception e) {
				print("Cannot delete model '" + modelFile.getName() + "' " );
				printError(e);
			}
		}
		else {
			// Delete DB MODEL (a single file )
			print("deleting DB model '" + modelFile.getName() + "' ..." );
			if ( ! modelFile.delete() ) {
				print("Cannot delete model '" + modelFile.getName() + "' " );
			}
			else {
				print("Model '" + modelFile.getName() + "' deleted." );
			}
		}
	}

}
