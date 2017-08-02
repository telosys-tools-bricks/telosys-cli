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
	public NewModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader,environment);
	}
	
	@Override
	public String getName() {
		return "nm";
	}

	@Override
	public String getShortDescription() {
		return "New Model" ;
	}
	
	@Override
	public String getDescription() {
		return "New Telosys DSL model";
	}
	
	@Override
	public String getUsage() {
		return "nm [model-name]";
	}
	
	@Override
	public String execute(String[] args) {
				
		if ( args.length > 1 ) {
			if ( checkHomeDirectoryDefined() ) {
				return newModel(args[1]);
			}
			else {
				return null ;
			}
		}
		else {
			return "No model name";
		}
	}

	private String newModel(String modelName) {

		String projectFullPath = getCurrentHome();
		TelosysProject telosysProject = new TelosysProject(projectFullPath);
		try {
			File file = telosysProject.createNewDslModel(modelName);
			setCurrentModel(modelName);
			return "Model '" + modelName + "' created (" + file.getName() + "), current model is now '" 
					+ modelName + "'" ;
		} catch (TelosysToolsException e) {
			return "Cannot create model '" + modelName + "'"
					+ " Exception : " + e.getMessage() ;
		}
	}
}
