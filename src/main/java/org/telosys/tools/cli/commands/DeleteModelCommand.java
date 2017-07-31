package org.telosys.tools.cli.commands;

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.api.ApiUtil;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

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
		return "Delete the current/given model ( 'dm [model-name]' ) ";
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {			
			// delete the given model name
			printDebug("delete args[1] ... ");
			return deleteModel(args[1]);
		}
		else {
			// no model name => current model ?
			printDebug("delete current model ... ");
			if ( checkModelDefined() ) {
				printDebug("OK current model is defined ");
				String result = deleteModel(getCurrentModel());
				return result ;
			}
			else {
				printDebug("PB current model is not defined ");
			}
		}
		return null ;
	}

	private String deleteModel(String modelName) {
		printDebug("deleteModel('" + modelName + "')");
//		File modelFile = null ;
//		try {
//			modelFile = getModelFile(modelName);
//		} catch (TelosysToolsException e) {
//			printError(e);
//		} 

		File modelFile = getModelFile(modelName);
		if ( modelFile != null ) {
			printDebug("modelFile = " + modelFile.getAbsolutePath() );
			deleteModel(modelFile );
			// If the current model has been deleted => update env & prompt
			if ( modelName.equals(getCurrentModel())) {
				setCurrentModel(null);
			}
		}
//		else {
//			print("Model " + modelName + " not found");
//		}
		return null ;
	}
	
	private String deleteModel(File modelFile ) {
		
		printDebug("deleteModel('" + modelFile + "')");
		// TODO : confirm y/n ? 
		
		if ( ApiUtil.isDslModelFile(modelFile) ) {
			// Delete DSL MODEL
			print("deleting DSL model '" + modelFile.getName() + "' ..." );
			TelosysProject telosysProject = getTelosysProject();			
			try {
				telosysProject.deleteDslModel(modelFile);
			} catch (TelosysToolsException e) {
				printError(e);
				return null ;
			}
		}
		else {
			// Delete DB MODEL (a single file )
			print("deleting DB model '" + modelFile.getName() + "' ..." );
			modelFile.delete();
		}
		print("model '" + modelFile.getName() + "' deleted." );
		return null ;
	}

}
