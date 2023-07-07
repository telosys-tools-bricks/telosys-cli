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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.batch.generation.BatchGenResult;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.Filter;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generic.model.Model;

import jline.console.ConsoleReader;

/**
 * Generation in batch mode (model level and bundle level)
 * 
 * @author Laurent GUERIN
 *
 */
public class GenBatchCommand extends CommandLevel2 {
	
	public static final String COMMAND_NAME = "genb";
	
	private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList(YES_OPTION, "-r")); // -y -r
	
	private static final String LINE = "+---------------------------+" ;
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public GenBatchCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getShortDescription() {
		return "Generation in batch mode" ;
	}

	@Override
	public String getDescription() {
		return "Launch generation for many models and many bundles";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " *|model-name-filter *|bundle-name-filter [-y]";
	}

	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		// Check arguments :
		// 2 : gen * *    
		// 3 : gen * * -y  | gen * * -r
		// 4 : gen * * -y  -r
		if ( checkArguments(commandArguments, 2, 3, 4 ) && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
			Set<String> activeOptions = getOptions(commandArguments);
			registerYesOptionIfAny(activeOptions);			
			List<String> argsWithoutOptions = removeOptions(commandArguments);
			// newArgs = args without '-y' if any
			generateBatch(argsWithoutOptions, activeOptions);
		}
		return null ;
	}

	/**
	 * Batch generation for the given arguments and options
	 * @param argsWithoutOptions
	 * @param activeOptions
	 * @return
	 */
	private GenerationTaskResult generateBatch(List<String> argsWithoutOptions, Set<String> activeOptions)  {
		boolean flagResources = isOptionActive("-r", activeOptions);
		if ( argsWithoutOptions.size() == 2 ) {
			String modelNameFilter  = argsWithoutOptions.get(0);
			String bundleNameFilter = argsWithoutOptions.get(1);
			// gen * * 
			generateBatch(modelNameFilter, bundleNameFilter, flagResources);
		}
		else {
			print("invalid arguments");
		}
		return null;
	}
	
	/**
	 * Batch generation for the given models and bundles filters 
	 * @param modelNameFilter
	 * @param bundleNameFilter
	 * @param flagResources
	 */
	private void generateBatch(String modelNameFilter, String bundleNameFilter, boolean flagResources) {
		// Get models
		List<String> models = getModels(modelNameFilter); // in the future add filter like 'bundles'
		if ( models.isEmpty() ) {
			return;
		}
		// Get bundles
		List<String> bundles = getBundles(bundleNameFilter);
		if ( bundles.isEmpty() ) {
			return;
		}
		
		print(LINE);
		print("| Start of batch generation | ");
		print(LINE);
		
		try {
			// Launch code generation for selected models and bundles
			BatchGenResult batchResult = launchGeneration(models, bundles, flagResources);
			
			// Print normal end with result 
			print("");
			print(LINE);
			print("|  End of batch generation  | ");
			print(LINE);
			printBatchResult(modelNameFilter, bundleNameFilter, batchResult);
			
		} catch (TelosysModelException e) {
			printError(e);
		} catch (TelosysToolsException e) {
			printError(e);
		} catch (Exception e) {
			printUnexpectedError(e);
		} 
	}

	private void printError(TelosysModelException e) {
		print(LINE);
		print("|       MODEL   ERROR       | ");
		print(LINE);		
		printModelError(e);
	}

	private void printError(TelosysToolsException e) {
		print(LINE);
		print("|     GENERATION  ERROR     | ");
		print(LINE);		
		print("Generation error: " + e.getMessage() );
	}

	private void printUnexpectedError(Exception e) {
		print(LINE);
		print("|     UNEXPECTED  ERROR     | ");
		print(LINE);		
		print("Unexpected exception: " + e.getMessage() );
	}

	/**
	 * Launches a generation for all entities in all given models with all templates in all the given bundles
	 * @param models
	 * @param bundles
	 * @param flagResources
	 * @return
	 * @throws TelosysModelException
	 * @throws TelosysToolsException
	 */
	private BatchGenResult launchGeneration(List<String> models, List<String> bundles, boolean flagResources) throws TelosysModelException, TelosysToolsException {
		BatchGenResult batchGenResult = new BatchGenResult();
		for ( String model : models ) {
			batchGenResult.updateCurrentModel(model);
			for ( String bundle : bundles ) {
				batchGenResult.updateCurrentBundle(bundle);
				// Launch code generation for MODEL + BUNDLE
				GenerationTaskResult generationTaskResult = launchGeneration(model, bundle, flagResources);
				batchGenResult.update(model, bundle, generationTaskResult);
			}
		}
		return batchGenResult;
	}
	
	private void printBatchResult(String modelNameFilter, String bundleNameFilter, BatchGenResult r) {
		print("Batch parameters : " );
		print(" . model names  : " + modelNameFilter );
		print(" . bundle names : " + bundleNameFilter );
		print("Batch generation result : " );
		print(" . number of models used       : " + r.getNumberOfModelsUsed() );
		print(" . number of bundles used      : " + r.getNumberOfBundlesUsed() );
		print(" . number of files generated   : " + r.getNumberOfFilesGenerated() );
		print(" . number of generation errors : " + r.getNumberOfGenerationErrors() );
		print(" . number of resources copied  : " + r.getNumberOfResourcesCopied() );
		print("Results by bundle : " );
		for ( String s : r.getBundlesStatus() ) {
			print(s);
		}
	}
	
	private List<String> getModelNames() { // TODO : move in API TelosysProject 
		List<String> modelNames = new LinkedList<>();
		// Convert files to file names (strings)
		for ( File f : getTelosysProject().getModels() ) {
			modelNames.add(f.getName());
		}
		return modelNames;
	}
	
	/**
	 * @return
	 */
	private List<String> getModels(String modelNameFilter) {
		List<String> allModels = getModelNames();
		List<String> models = Filter.filter(allModels, modelNameFilter);
		if ( models.isEmpty() ) {
			print("No model for '" + modelNameFilter + "'") ;
		}
		return models;
	}
	
	/**
	 * @param bundleNameFilter
	 * @return
	 */
	private List<String> getBundles(String bundleNameFilter) {
		try {
			List<String> allBundles = getTelosysProject().getInstalledBundles();
			List<String> bundles = Filter.filter(allBundles, bundleNameFilter);
			if ( bundles.isEmpty() ) {
				print("No bundle for '" + bundleNameFilter + "'") ;
			}
			return bundles;
		} catch (TelosysToolsException e) {
			print("Error: cannot get bundles: " + e.getMessage()) ;
			return new LinkedList<>();
		}
	}

	/**
	 * Launches a generation for all entities in the given model name with all templates in the given bundle name
	 * @param modelName
	 * @param bundleName
	 * @param flagResources
	 * @return
	 * @throws TelosysModelException
	 * @throws TelosysToolsException
	 */
	private GenerationTaskResult launchGeneration(String modelName, String bundleName, boolean flagResources) throws TelosysModelException, TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		// Load given model 
		Model model = telosysProject.loadModel(modelName);
		// Launch generation with model and given bundle 
		return telosysProject.launchGeneration(model, bundleName, flagResources);
	}
	
}
