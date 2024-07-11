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
 * Specialization of 'Command' providing methods to work with models, bundles, etc
 * 
 * @author Laurent GUERIN 
 *
 */
public abstract class CommandLevel2 extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	protected CommandLevel2(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	//-----------------------------------------------------------------------------------------------
	// MODELS 
	//-----------------------------------------------------------------------------------------------
	
//	/**
//	 * Try to found a unique model folder according with the first argument 
//	 * or with the current model if no arg provided
//	 * @param args
//	 * @return
//	 */
//	protected File findModelFolder(String[] args) {
//		File modelFolder = null ;
//		if ( args.length > 1 ) {
//			modelFolder = findModelFolder(args[1]);
//		}
//		else {
//			if ( checkModelDefined() ) {
//				return getCurrentModelFolder();
//			}
//		}
//		return modelFolder ;
//	}
	
	/**
	 * Try to found a unique model folder according with the first argument 
	 * or with the current model if no arg provided
	 * @param args
	 * @return
	 */
	protected File findModelFolder(List<String> args) {
		File modelFolder = null ;
		if ( ! args.isEmpty() ) {
			modelFolder = findModelFolder(args.get(0));
		}
		else {
			if ( checkModelDefined() ) {
				return getCurrentModelFolder();
			}
		}
		return modelFolder ;
	}
	
//	/**
//	 * Try to find the current model folder 
//	 * @return
//	 */
//	protected File findModelFolder() {
//		if ( checkModelDefined() ) {
//			return getCurrentModelFolder();
//		}
//		return null ;
//	}
	
	/**
	 * Try to found a unique model folder matching the given model name pattern
	 * @param modelNamePattern
	 * @return the model folder (or null if not found or not unique)
	 */
	protected File findModelFolder(String modelNamePattern) {
//		List<File> allModelsInProject = getTelosysProject().getModels();
//		// filter using pattern
//		List<File> modelsFound = new LinkedList<>();
//		for ( File f : allModelsInProject ) {
//			if ( f.getName().equals(modelNamePattern) ) {
//				// strict equality
//				return f;
//			}
//			else {
//				// check if contains a part of the given string
//				if ( f.getName().contains(modelNamePattern) ) {
//					modelsFound.add(f);
//				}
//			}
//		}
//		// filter result 
//		if ( modelsFound.isEmpty() ) {
//			print("No model for '" + modelNamePattern + "'") ;
//		}
//		else if ( modelsFound.size() > 1 ) {
//			print("Ambiguous : " + modelsFound.size() + " models found") ;
//		}
//		else {
//			// OK : only 1 model found
//			return modelsFound.get(0);
//		}
//		return null ;
		List<File> allModels = getTelosysProject().getModels();
		return findFile(allModels, modelNamePattern, "model");		
	}

	/**
	 * @param modelNamePatterns
	 * @return
	 */
	protected List<File> findModelFolders(List<String> modelNamePatterns) {
		List<File> allModels = getTelosysProject().getModels();
		if ( modelNamePatterns.isEmpty() ) {
			return allModels;
		}
		else {
			List<File> found = new LinkedList<>();
			for ( File f : allModels ) {
				for ( String pattern : modelNamePatterns ) {
					if ( matching(f, pattern)) {
						found.add(f);
					}
				}
			}
			return found;
		}
	}

	/**
	 * Try to find the model file for the given model name (print errors if any)
	 * @param modelName the model name 
	 * @return the model file (or null if not found) 
	 */
	private File getModelInfoFile(String modelName) {
		TelosysProject telosysProject = getTelosysProject();		
		if ( telosysProject.modelFolderExists(modelName) ) {
			File modelFile = telosysProject.getModelInfoFile(modelName);
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
	
	protected File getCurrentModelInfoFile() {
		String modelName = getCurrentModel();
		if ( modelName != null ) {
			return getModelInfoFile(modelName);
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
	
	private File getModelFolder(String modelName) {
		File modelFolder = getTelosysProject().getModelFolder(modelName); 
		if ( modelFolder.exists() ) {
			return modelFolder ;
		}
		else {
			print("Model folder '" + modelFolder.getName() + "' not found.");
			return null;
		}
	}

	/**
	 * Loads the current model (print model errors if any)
	 * @return
	 */
	protected Model loadCurrentModel() {
		return loadModel(getCurrentModel());
	}

	/**
	 * Loads the model identified by the given name (print model errors if any)
	 * @param modelName
	 * @return
	 */
	protected Model loadModel(String modelName) {
		// 1) try to get the file 
		File modelFile = getModelFolder(modelName);
		if ( modelFile != null ) {
			// 2) try to load the model 
			return loadModel(modelFile);
		}
		return null ;
	}

	/**
	 * Loads the model located in the given folder (print model errors if any)
	 * @param modelFolder
	 * @return the model loaded (or null if cannot be loaded)
	 */
	protected Model loadModel(File modelFolder) {
		try {
			return getTelosysProject().loadModel(modelFolder);
		} catch (TelosysModelException tme) {
			printModelError(tme);
			return null ; // Model cannot be loaded
		}
	}

	protected void printModelError(TelosysModelException tme) {
		printError("Invalid model '" + tme.getModelName() + "'");
		// Print parsing errors
		print(tme.getMessage());
		DslModelErrors errors = tme.getDslModelErrors();
		if ( errors != null ) {
			for ( DslModelError e : errors.getErrors() ) {
				print( " . " + e.getReportMessage() );
			}
		}
	}

	//-----------------------------------------------------------------------------------------------
	// BUNDLES 
	//-----------------------------------------------------------------------------------------------
	protected File getCurrentBundleConfigFile() {
		String bundleName = getCurrentBundle();
		if ( bundleName != null ) {
			return getBundleConfigFile(bundleName);
		}
		return null;
	}
	
	/**
	 * Try to find the bundle config file for the given bundle name (print errors if any)
	 * @param bundleName the model name 
	 * @return the file (or null if not found) 
	 */
	private File getBundleConfigFile(String bundleName) {
		TelosysProject telosysProject = getTelosysProject();		
		if ( telosysProject.bundleFolderExists(bundleName) ) {
			File file = telosysProject.getBundleConfigFile(bundleName);
			if ( file.exists() ) {
				return file ;
			}
			else {
				print("Bundle configuration file '" + file.getName() + "' not found.") ;
			}
		}
		else {
			print("Bundle '" + bundleName + "' doesn't exist.") ;
		}
		return null;
	}

	protected File getCurrentBundleFolder() {
		String bundleName = getCurrentBundle();
		if ( bundleName != null ) {
			return getBundleFolder(bundleName);
		}
		return null;
	}
	
	private File getBundleFolder(String bundleName) {
		File bundleFolder = getTelosysProject().getBundleFolder(bundleName); 
		if ( bundleFolder.exists() ) {
			return bundleFolder ;
		}
		else {
			print("Bundle folder '" + bundleFolder.getName() + "' not found.");
			return null;
		}
	}

	
//	/**
//	 * Returns bundles installed in the current project and matching the given criteria
//	 * @param criteria
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	protected final List<String> getInstalledBundles(List<String> criteria) throws TelosysToolsException {
//		TelosysProject telosysProject = getTelosysProject();
//		// get all installed bundles
//		List<String> allBundles = telosysProject.getInstalledBundles();
//		// filter bundles according with criteria
//		return Filter.filter(allBundles, criteria);
//	}
	
	/**
	 * Try to found a unique bundle folder matching the given bundle name pattern
	 * @param bundleNamePattern
	 * @return the bundle folder (or null if not found or not unique)
	 */
	protected File findBundleFolder(String bundleNamePattern) {
		List<File> allBundles = getTelosysProject().getBundles();
		return findFile(allBundles, bundleNamePattern, "bundle");
	}
	
	/**
	 * @param bundleNamePatterns
	 * @return
	 */
	protected List<File> findBundleFolders(List<String> bundleNamePatterns) {
		List<File> allBundles = getTelosysProject().getBundles();
		if ( bundleNamePatterns.isEmpty() ) {
			return allBundles;
		}
		else {
			List<File> found = new LinkedList<>();
			for ( File f : allBundles ) {
				for ( String pattern : bundleNamePatterns ) {
					if ( matching(f, pattern)) {
						found.add(f);
					}
				}
			}
			return found;
		}
	}

	//-----------------------------------------------------------------------------------------------
	// GITHUB 
	//-----------------------------------------------------------------------------------------------

//	/**
//	 * @param githubStoreName
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	protected final BundlesFromGitHub getGitHubBundles(String githubStoreName) throws TelosysToolsException {
//		TelosysProject telosysProject = getTelosysProject();
////		return telosysProject.getGitHubBundlesList(githubStoreName);
//		return getTelosysProject().getBundlesAvailableInDepot(githubStoreName);
//	}
	
	//-----------------------------------------------------------------------------------------------
	// COMMONS 
	//-----------------------------------------------------------------------------------------------

	private File findFile(List<File> allFiles, String namePattern, String objectName) {
		// filter using pattern
		List<File> found = new LinkedList<>();
		for ( File f : allFiles ) {
			if ( f.getName().equals(namePattern) ) {
				// strict equality
				return f;
			}
			else {
				// check if contains a part of the given string
				if ( f.getName().contains(namePattern) ) {
					found.add(f);
				}
			}
		}
		// filter result 
		if ( found.isEmpty() ) {
			print("No " + objectName + " for '" + namePattern + "'") ;
			return null ;
		}
		else if ( found.size() > 1 ) {
			print("Ambiguous : " + found.size() + " " + objectName + "s found") ;
			return null ;
		}
		else {
			// OK : only 1 found => get it
			return found.get(0);
		}
	}
	
	private boolean matching(File file, String namePattern) {
		if ( file.getName().equals(namePattern) ) {
			// strict equality
			return true;
		}
		else {
			// check if contains a part of the given string
			if ( file.getName().contains(namePattern) ) {
				return true;
			}
		}
		return false;
	}	

}
