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
import org.telosys.tools.cli.commands.commons.DepotContent;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
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
	private void printModelDoesntExists(String modelName) {
		print("Model '" + modelName + "' doesn't exist!");
		File f = getTelosysProject().getModelsFolder();
		if ( f != null ) {
			print("(models dir : " + f.getAbsolutePath() + ")");
		}
		else {
			print("(models dir : undefined)");
		}
		
	}
	
	/**
	 * Check model existence
	 * @param modelName
	 * @return
	 * @since 4.2.0
	 */
	protected final boolean checkModelExists(String modelName) {
		if ( modelName != null ) {
			if ( getTelosysProject().modelFolderExists(modelName) ) {
				return true;
			}
			else {
				printModelDoesntExists(modelName);
				return false;
			}
		}
		else {
			// Not supposed to happen 
			print("Model is undefined!"); 
			return false;
		}
	}

	/**
	 * Check current model is defined and exists in the filesystem
	 * @return
	 * @since 4.2.0
	 */
	protected final boolean checkCurrentModelDefinedAndExists() {
		if ( checkModelDefined() ) {
			return checkModelExists(getCurrentModel()); 
		}
		else {
			return false;
		}
	}

	/**
	 * Try to found a unique model folder matching the given model name pattern
	 * @param modelNamePattern
	 * @return the model folder (or null if not found or not unique)
	 */
	protected final File findModelFolder(String modelNamePattern) {
		List<File> allModels = getTelosysProject().getModels();
		return findFile(allModels, modelNamePattern, "model");		
	}

	/**
	 * @param modelNamePatterns
	 * @return
	 */
	protected final List<File> findModelFolders(List<String> modelNamePatterns) {
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
			printModelDoesntExists(modelName);
		}
		return null;
	}
	
	protected final File getCurrentModelInfoFile() {
		String modelName = getCurrentModel();
		if ( modelName != null ) {
			return getModelInfoFile(modelName);
		}
		return null;
	}

	protected final File getCurrentModelFolder() {
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
			// No model folder => doesn't exist
			printModelDoesntExists(modelName);
			return null;
		}
	}

	/**
	 * Loads the current model (print model errors if any)
	 * @return
	 */
	protected final Model loadCurrentModel() {
		return loadModel(getCurrentModel());
	}

	/**
	 * Loads the model identified by the given name (print model errors if any)
	 * @param modelName
	 * @return
	 */
	protected final Model loadModel(String modelName) {
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
	protected final Model loadModel(File modelFolder) {
		try {
			return getTelosysProject().loadModel(modelFolder);
		} catch (TelosysModelException tme) {
			printModelError(tme);
			return null ; // Model cannot be loaded
		}
	}

	protected final void printModelError(TelosysModelException tme) {
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
	private void printBundleDoesntExists(String bundleName) {
		print("Bundle '" + bundleName + "' doesn't exist!");
		File f = getTelosysProject().getBundlesFolder();
		if ( f != null ) {
			print("(bundles dir : " + f.getAbsolutePath() + ")");
		}
		else {
			print("(bundles dir : undefined)");
		}
	}
	
	/**
	 * Check bundle existence
	 * @param bundleName
	 * @return
	 * @since 4.2.0
	 */
	protected final boolean checkBundleExists(String bundleName) {
		if ( bundleName != null ) {
			if ( getTelosysProject().bundleFolderExists(bundleName) ) {
				return true;
			}
			else {
				printBundleDoesntExists(bundleName);
				return false;
			}
		}
		else {
			// Not supposed to happen 
			print("Bundle is undefined!"); 
			return false;
		}
	}
	
	/**
	 * Check current bundle is defined and exists in the filesystem
	 * @return
	 * @since 4.2.0
	 */
	protected final boolean checkCurrentBundleDefinedAndExists() {
		if ( checkBundleDefined() ) {
			return checkBundleExists(getCurrentBundle()); 
		}
		else {
			return false;
		}
	}
	
	protected final File getCurrentBundleConfigFile() {
		String bundleName = getCurrentBundle();
		if ( bundleName != null ) {
			return getBundleConfigFile(bundleName);
		}
		return null;
	}
	
	/**
	 * Try to find the bundle config file for the given bundle name (print errors if any)
	 * @param bundleName the bundle name 
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
			printBundleDoesntExists(bundleName);
		}
		return null;
	}

	protected final File getCurrentBundleFolder() {
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
			printBundleDoesntExists(bundleName);
			return null;
		}
	}

	/**
	 * Try to found a unique bundle folder matching the given bundle name pattern
	 * @param bundleNamePattern
	 * @return the bundle folder (or null if not found or not unique)
	 */
	protected final File findBundleFolder(String bundleNamePattern) {
		List<File> allBundles = getTelosysProject().getBundles();
		return findFile(allBundles, bundleNamePattern, "bundle");
	}
	
	/**
	 * @param bundleNamePatterns
	 * @return
	 */
	protected final List<File> findBundleFolders(List<String> bundleNamePatterns) {
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
	// DEPOTS 
	//-----------------------------------------------------------------------------------------------

	protected final String getDepotDefinition(DepotContent depotContent) {
		TelosysToolsCfg config = getTelosysProject().getTelosysToolsCfg();
		if ( depotContent == DepotContent.MODELS ) {
			return config.getDepotForModels() ;
		}
		else if ( depotContent == DepotContent.BUNDLES ) {
			return config.getDepotForBundles() ;
		}
		else {
			// cannot happen (only 2 entries in enum)
			throw new TelosysRuntimeException("Unexpected DepotContent"); 
		}
	}
	
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
