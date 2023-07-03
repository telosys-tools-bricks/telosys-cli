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
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.batch.generation.BatchGenResult;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.Filter;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generic.model.Model;

import jline.console.ConsoleReader;

/**
 * Generation in batch mode (model level and bundle level)
 * 
 * @author Laurent GUERIN
 *
 */
public class GenBatchCommand extends Command {
	
	public static final String COMMAND = "genb";
	
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
		return COMMAND;
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
		return COMMAND + " *|model-name-filter *|bundle-name-filter [-y]";
	}

	@Override
	public String execute(String[] args) {
			// Check arguments :
			// 2 : gen * *    
			// 3 : gen * * -y  
			if ( checkArguments(args, 2, 3 ) && checkOptions(args, "-y") ) {
				String[] newArgs = registerAndRemoveYesOption(args);
				// newArgs = args without '-y' if any
				generateBatch(newArgs);
			}
		return null ;
	}

	/**
	 * Generation entry point
	 * @param args all the arguments as provided by the command line (0 to N)
	 */
	private GenerationTaskResult generateBatch(String[] args)  {
		int nbArgs = args.length - 1 ;
		if ( nbArgs == 2 ) {
			// gen * * 
			generateBatch(args[1], args[2]);
		}
		else {
			print("invalid arguments");
		}
		return null;
	}
	
	private boolean checkResourcesOption(String option) {
		if ( "-r".equals(option) ) {
			return true ;
		}
		else {
			print("Invalid argument '" + option + "' ( '-r' expected ) ");
			return false ;
		}
	}
	
	private void generateBatch(String modelNameFilter, String bundleNameFilter) {
		List<String> models = getModels(modelNameFilter); // in the future add filter like 'bundles'
		List<String> bundles = getBundles(bundleNameFilter);
		if ( models.isEmpty() ) {
			print("No model.") ;
			return;
		}
		if ( bundles.isEmpty() ) {
			print("No bundle.") ;
			return;
		}
		print(LINE);
		print("| Start of batch generation | ");
		print(LINE);
		
		BatchGenResult batchResult = launchGeneration(models, bundles); 

		print("");
		print(LINE);
		print("|  End of batch generation  | ");
		print(LINE);		
		
		printBatchResult(modelNameFilter, bundleNameFilter, batchResult);
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
	
	/**
	 * Launches a generation for all entities in all given models with all templates in all the given bundles
	 * @param models list of models
	 * @param bundles list of bundles
	 * @return
	 */
	private BatchGenResult launchGeneration(List<String> models, List<String> bundles) {
		BatchGenResult batchGenResult = new BatchGenResult();
		for ( String model : models ) {
			batchGenResult.updateCurrentModel(model);
			for ( String bundle : bundles ) {
				batchGenResult.updateCurrentBundle(bundle);
				GenerationTaskResult generationTaskResult = launchGeneration(model, bundle);
				batchGenResult.update(model, bundle, generationTaskResult);
			}
		}
		return batchGenResult;
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
		//List<File> allModels = getTelosysProject().getModels();
		List<String> allModels = getModelNames();
		List<String> models = Filter.filter(allModels, modelNameFilter);
		if ( models.isEmpty() ) {
			print("No model.") ;
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
				print("No bundle.") ;
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
	 * @return
	 */
	private GenerationTaskResult launchGeneration(String modelName, String bundleName) {
		Model model = loadModel(modelName);
		return launchGeneration(model, bundleName);
	}
	
	/**
	 * @param modelName
	 * @return
	 */
	private Model loadModel(String modelName) {
		try {
			return getTelosysProject().loadModel(modelName);
		} catch (TelosysModelException e) {
			String msg = "Model error: cannot load model '" + modelName + "'" ;
			throw new TelosysRuntimeException(msg, e);
		}
	}

	/**
	 * Launches a generation for all entities in the given model with all templates in the given bundle
	 * @param model
	 * @param bundleName
	 * @return
	 */
	private GenerationTaskResult launchGeneration(Model model, String bundleName) {
		try {
			return getTelosysProject().launchGeneration(model, bundleName);
		} catch (TelosysToolsException e) {
			String msg = "Generation error: model '" + model.getName() + "' - bundle '" + bundleName + "'" ;
			throw new TelosysRuntimeException(msg, e);
		}
	}
}
