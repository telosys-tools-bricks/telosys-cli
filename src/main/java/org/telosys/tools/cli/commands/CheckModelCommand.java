package org.telosys.tools.cli.commands;

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.generic.model.Model;

public class CheckModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public CheckModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "cm";
	}

	@Override
	public String getShortDescription() {
		return "Check Model" ;
	}

	@Override
	public String getDescription() {
		return "Check the current/given model ( 'cm [model-name]' )";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				// cm model-name
				return checkModel(args[1]);
			}
			else {
				// cm => current model if defined
				if ( checkModelDefined() ) {
					return checkModel(getCurrentModel());
				}				
			}			
		}
		return null;
	}
		
	private String checkModel(String modelName) {
		
		// 1) try to get the file 
		File modelFile = getModelFile(modelName); 
		if ( modelFile != null ) {
			
			// 2) try to load the model 
			Model model = loadModel(modelFile);
			if ( model != null ) {
				int n = model.getEntities() != null ? model.getEntities().size() : 0 ; 
				print( "Model OK (file '" + modelFile.getName() + "' loaded : " + n + " entities)" );
			}
		}
		return null ;
	}
}
