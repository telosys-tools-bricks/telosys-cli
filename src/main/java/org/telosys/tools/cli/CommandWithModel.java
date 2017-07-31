package org.telosys.tools.cli;

import java.io.File;
import java.util.List;
import java.util.Map;

import jline.console.ConsoleReader;

import org.telosys.tools.api.ApiUtil;
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
		File modelFile = null ;
		int n = 0 ;
		List<File> models;
		try {
			models = telosysProject.getModels();
		} catch (TelosysToolsException ex) {
			printError(ex);
			return null ;
		} // All the models files in the current project
		if ( modelName.contains(".") ) {
			// Suffix is in the name 
			for ( File file : models ) {
				if ( file.getName().equals(modelName) ) {
					modelFile = file ;
					n++;				
				}
			}
		}
		else {
			// No suffix in the name => try to add all the suffixes 
			for ( File file : models ) {
				if ( file.getName().equals(modelName + ApiUtil.MODEL_SUFFIX) ) {
					modelFile = file ;
					n++;				
				}
				if ( file.getName().equals(modelName + ApiUtil.DBMODEL_SUFFIX) ) {
					modelFile = file ;
					n++;				
				}
				if ( file.getName().equals(modelName + ApiUtil.DBREP_SUFFIX) ) {
					modelFile = file ;
					n++;				
				}
			}
		}
		if ( n == 0 ) {
			print("Model '" + modelName + "' not found.") ;
			return null ; // Not found
		}
		else if ( n == 1 ) {
			return modelFile ; // Found 1 matching file
		}
		else {
			print("Ambiguous model name (" + n + " files found)");
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
	

}
