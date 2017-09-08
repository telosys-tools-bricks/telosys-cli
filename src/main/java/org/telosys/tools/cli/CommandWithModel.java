package org.telosys.tools.cli;

import java.io.File;
import java.util.Map;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.generic.model.Model;

public abstract class CommandWithModel extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public CommandWithModel(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	/**
	 * Try to find the model file for the given model name (print errors if any)
	 * @param modelName the model name with or without its suffix ( eg 'foo', 'foo.model', 'foo.dbmodel', etc )
	 * @return the model file (or null if not found or ambiguous) 
	 */
	protected File getModelFile(String modelName) {
		TelosysProject telosysProject = getTelosysProject();
		
		try {
			File modelFile = telosysProject.getModelFile(modelName);
			if ( modelFile == null ) {
				print("Model '" + modelName + "' not found.") ;
			}
			return modelFile ;
		} catch (TelosysToolsException e) {
			print(e.getMessage()); // Ambiguous model name
			return null ;
		}
	}
	
	/**
	 * Loads the given model file (and print errors if any)
	 * @param modelFile
	 * @return the model loaded (or null if cannot be loaded)
	 */
	protected Model loadModel(File modelFile) {
		TelosysProject telosysProject = getTelosysProject();
		try {
			return telosysProject.loadModel(modelFile);
		} catch (TelosysToolsException ex) {
			if ( ex instanceof TelosysModelException ) {
				printError("Invalid model !");
				// Print parsing errors
				TelosysModelException tme = (TelosysModelException) ex ;
				Map<String,String> parsingErrors = tme.getParsingErrors();
				if ( parsingErrors != null ) {
					print( parsingErrors.size() + " parsing error(s)" );
					for ( Map.Entry<String,String> entry : parsingErrors.entrySet() ) {
						print( "'" + entry.getKey() + "' : " + entry.getValue() );
					}					
				}
			}
			else {
				printError(ex);					
			}
		}
		return null ; // Model cannot be loaded
	}
	
	protected Model loadModel(String modelName) {
		// 1) try to get the file 
		File modelFile = getModelFile(modelName); 
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
