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
package org.telosys.tools.cli;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.dsl.DslModelError;
import org.telosys.tools.dsl.DslModelErrors;
import org.telosys.tools.generic.model.Model;

import jline.console.ConsoleReader;

/**
 * Specialization of 'Command' providing methods to work with models
 * 
 * @author Laurent GUERIN 
 *
 */
public abstract class CommandWithModel extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	protected CommandWithModel(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	/**
	 * Try to found a unique model folder according with the first argument 
	 * or with the current model if no arg provided
	 * @param args
	 * @return
	 */
	protected File findModelFolder(String[] args) {
		File modelFolder = null ;
		if ( args.length > 1 ) {
			modelFolder = findModelFolder(args[1]);
		}
		else {
			if ( checkModelDefined() ) {
				return getCurrentModelFolder();
			}
		}
		return modelFolder ;
	}
	
	/**
	 * Try to found a unique model folder matching the given model name pattern
	 * @param modelNamePattern
	 * @return the model folder (or null if not found or not unique)
	 */
	protected File findModelFolder(String modelNamePattern) {
		List<File> allModelsInProject = getTelosysProject().getModels();
		// filter using pattern
		List<File> modelsFound = new LinkedList<>();
		for ( File f : allModelsInProject ) {
			if ( f.getName().equals(modelNamePattern) ) {
				// strict equality
				return f;
			}
			else {
				// check if contains a part of the given string
				if ( f.getName().contains(modelNamePattern) ) {
					modelsFound.add(f);
				}
			}
		}
		// filter result 
		if ( modelsFound.isEmpty() ) {
			print("No model for '" + modelNamePattern + "'") ;
		}
		else if ( modelsFound.size() > 1 ) {
			print("Ambiguous : " + modelsFound.size() + " models found") ;
		}
		else {
			// OK : only 1 model found
			return modelsFound.get(0);
		}
		return null ;
	}
	
	/**
	 * Try to find the model file for the given model name (print errors if any)
	 * @param modelName the model name 
	 * @return the model file (or null if not found) 
	 */
	protected File getModelFile(String modelName) {
		TelosysProject telosysProject = getTelosysProject();		
		if ( telosysProject.dslModelFolderExists(modelName) ) {
			File modelFile = telosysProject.getDslModelFile(modelName);
			if ( modelFile.exists() ) {
				return modelFile ;
			}
			else {
				print("Model file '" + modelFile.getName() + "' not found.") ;
			}
		}
		else {
			print("Model '" + modelName + "' doesn't exist.") ;
		}
		return null;
	}
	
	protected File getCurrentModelFile() {
		String modelName = getCurrentModel();
		if ( modelName != null ) {
			return getModelFile(modelName);
		}
		return null;
	}

	protected File getCurrentModelFolder() {
		String modelName = getCurrentModel();
		if ( modelName != null ) {
			return getModelFolder(modelName);
		}
		return null;
	}
	
	protected File getModelFolder(String modelName) {
		File modelFolder =  getTelosysProject().getModelFolder(modelName); // v 3.4.0
		if ( modelFolder.exists() ) {
			return modelFolder ;
		}
		else {
			print("Model folder '" + modelFolder.getName() + "' not found.");
			return null;
		}
	}

	/**
	 * Loads the model located in the given folder (print errors if any)
	 * @param modelFolder
	 * @return the model loaded (or null if cannot be loaded)
	 */
	protected Model loadModel(File modelFolder) {
		try {
			return getTelosysProject().loadModel(modelFolder);
		} catch (TelosysModelException tme) {
			printError("Invalid model !");
			// Print parsing errors
			print(tme.getMessage());
			DslModelErrors errors = tme.getDslModelErrors();
			if ( errors != null ) {
				for ( DslModelError e : errors.getErrors() ) {
					print( " . " + e.getReportMessage() );
				}
			}
		}
		return null ; // Model cannot be loaded
	}

	protected Model loadModel(String modelName) {
		// 1) try to get the file 
		File modelFile = getModelFolder(modelName); // v 3.4.0
		if ( modelFile != null ) {
			// 2) try to load the model 
			return loadModel(modelFile);
		}
		return null ;
	}

	protected Model loadCurrentModel() {
		return loadModel(getCurrentModel());
	}

}
