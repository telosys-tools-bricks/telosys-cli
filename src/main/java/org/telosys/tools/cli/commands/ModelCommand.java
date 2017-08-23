package org.telosys.tools.cli.commands;

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class ModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public ModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "m";
	}

	@Override
	public String getShortDescription() {
		return "Model" ;
	}

	@Override
	public String getDescription() {
		return "Set/print the current model";
	}
	
	@Override
	public String getUsage() {
		return "m [model-name]";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( args.length > 1 ) {
			return tryToSetCurrentModel(args[1]);
		}
		else {
			//return invalidUsage("model-name argument expected");
			return undefinedIfNull(getCurrentModel());
		}
	}
	
//	private String tryToSetCurrentModel(String modelName) {
//		TelosysProject telosysProject = getTelosysProject(); 
//		if ( telosysProject != null ) {
//			try {
//				File file = telosysProject.getDslModelFile(modelName);
//				if (file.exists()) {
//					setCurrentModel(modelName);
//					return "Current model is now '" + getCurrentModel() + "'";
//				}
//				else {
//					return "Model '" + modelName + "' not found";
//				}
//			} catch (TelosysToolsException e) {
//				printError(e);
//			}
//		}
//		return null ;
//	}

	private String tryToSetCurrentModel(String modelName) {
		File modelFile = getModelFile(modelName);
		if ( modelFile != null ) {
			setCurrentModel(modelFile.getName());
			return "Current model is now '" + getCurrentModel() + "'";
		}
		else {
			return "Model '" + modelName + "' not found";
		}		
	}
}
