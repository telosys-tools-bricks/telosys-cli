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

import java.util.Arrays;
import java.util.HashSet;
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

		if ( confirmLaunch(models, bundles, flagResources) ) {
			try {
				printStart();
				// Launch code generation for selected models and bundles
				BatchGenResult batchResult = launchGeneration(models, bundles, flagResources);
				// Print normal end with result 
				printEnd();
				printBatchResult(modelNameFilter, bundleNameFilter, flagResources, batchResult);
			} catch (TelosysModelException e) {
				printError(e);
			} catch (TelosysToolsException e) {
				printError(e);
			} catch (Exception e) {
				printUnexpectedError(e);
			} 
		}
	}

	private boolean confirmLaunch(List<String> models, List<String> bundles, boolean flagResources) {
		String startOfLine = "  . ";
		print("You're about to launch a bulk generation with the following parameters:");
		print(models.size() + " model(s) ");
		printList(models, startOfLine);
		print(bundles.size() + " bundles(s) of templates ");
		printList(bundles, startOfLine);
		print("Copy resources : " + flagResources);
		print("");
		return confirm("Do you want to launch the generation");
	}
	
	private void printStart() {
		print(LINE);
		print("| Start of batch generation | ");
		print(LINE);
	}

	private void printEnd() {
		print("");
		print(LINE);
		print("|  End of batch generation  | ");
		print(LINE);
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
				// Register generation task result
				batchGenResult.update(model, bundle, generationTaskResult);
			}
		}
		return batchGenResult;
	}
	
	private void printBatchResult(String modelNameFilter, String bundleNameFilter, boolean flagResources, BatchGenResult r) {
		print("Batch parameters : " );
		print(" . model names    : '" + modelNameFilter  + "' ("+ r.getNumberOfModelsUsed()  + " models used)");
		print(" . bundle names   : '" + bundleNameFilter + "' ("+ r.getNumberOfBundlesUsed() + " bundles used)");
		print(" . copy resources : " + flagResources);
		print("Batch generation result : " );
		print(" . number of files generated   : " + r.getNumberOfFilesGenerated() );
		print(" . number of generation errors : " + r.getNumberOfGenerationErrors() );
		print(" . number of resources copied  : " + r.getNumberOfResourcesCopied() );
		print("Results by model/bundle : " );
		for ( String s : r.getBundlesStatus() ) {
			print(s);
		}
	}
	
	/**
	 * @param modelNameFilter
	 * @return
	 */
	private List<String> getModels(String modelNameFilter) {
		List<String> allModels = getTelosysProject().getModelNames();
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
		List<String> allBundles = getTelosysProject().getBundleNames();
		List<String> bundles = Filter.filter(allBundles, bundleNameFilter);
		if ( bundles.isEmpty() ) {
			print("No bundle for '" + bundleNameFilter + "'") ;
		}
		return bundles;
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
