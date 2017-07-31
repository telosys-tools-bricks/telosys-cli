package org.telosys.tools.cli.commands;

import java.io.File;
import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

public class ListEntitiesCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public ListEntitiesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "le";
	}

	@Override
	public String getShortDescription() {
		return "List Entities" ;
	}

	@Override
	public String getDescription() {
		return "List the entities for the current/given model ( 'le [model-name]' )";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				// le model-name
				return listEntities(args[1]);
			}
			else {
				// le => current model if defined
				if ( checkModelDefined() ) {
					return listEntities(getCurrentModel());
				}				
			}			
		}
		return null;
	}
	
	private String listEntities(String modelName) {

		// 1) try to get the file 
		File modelFile = getModelFile(modelName); 
		if ( modelFile != null ) {
			// 2) try to load the model 
			Model model = loadModel(modelFile);
			if ( model != null ) {
				printEntities(modelFile, model.getEntities());
			}
		}
		return null ;
	}
	
	private void printEntities(File modelFile, List<Entity> entities) {
		if ( entities != null && entities.size() > 0 ) {
			print("Model '" + modelFile.getName() + "' contains " + entities.size() + " entities : " ) ;
			for ( Entity e : entities ) {
				print(" . " + e.getClassName() + " ( " + e.getFullName() + " )" ) ;
			}
		}
		else {
			print("Model '" + modelFile.getName() + "' is void (no entity)" ) ;
		}
	}
}
