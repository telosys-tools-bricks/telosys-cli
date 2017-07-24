package org.telosys.tools.cli.commands;

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class NewModelCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public NewModelCommand(ConsoleReader consoleReader) {
		super(consoleReader);
	}
	
	@Override
	public String getName() {
		return "newmodel";
	}

	@Override
	public String getShortName() {
		return "nm" ;
	}
	
	@Override
	public String getDescription() {
		return "New Telosys model ( 'nm [model-name]' ) ";
	}
	
	@Override
	public String execute(Environment environment, String[] args) {
				
		if ( args.length > 1 ) {
			return newModel(environment, args[1]);
		}
		else {
			return "No model name";
		}
	}

	private String newModel(Environment environment, String modelName) {

		if ( environment.getHomeDirectory() == null ) {
			return "Home directory must be set before creating a new model" ;
		}
		
		String projectFullPath = environment.getHomeDirectory();
		TelosysProject telosysProject = new TelosysProject(projectFullPath);
		try {
			File file = telosysProject.createNewDslModel(modelName);
			environment.setCurrentModel(modelName);
			return "Model '" + modelName + "' created (" + file.getName() + "), current model is now '" 
					+ modelName + "'" ;
		} catch (TelosysToolsException e) {
			return "Cannot create model '" + modelName + "'"
					+ " Exception : " + e.getMessage() ;
		}
	}
}
