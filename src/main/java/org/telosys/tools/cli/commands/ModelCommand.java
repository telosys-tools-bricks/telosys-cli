package org.telosys.tools.cli.commands;

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class ModelCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public ModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "model";
	}

	@Override
	public String getShortName() {
		return "m" ;
	}

	@Override
	public String getDescription() {
		return "Set the current model ( 'm [model-name]' )";
	}
	
//	protected boolean checkArgs(String[] args, int n, String message) {
//		if ( ! ( args.length > n ) ) {
//			print(message);
//			return false;
//		}
//		return true;
//	}
	
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
	
	private String tryToSetCurrentModel(String modelName) {
//		TelosysProject telosysProject = getTelosysProject(environment); 
		TelosysProject telosysProject = getTelosysProject(); 
		if ( telosysProject != null ) {
			try {
				File file = telosysProject.getDslModelFile(modelName);
				if (file.exists()) {
					setCurrentModel(modelName);
					return "Current model is now '" + getCurrentModel() + "'";
				}
				else {
					return "Model '" + modelName + "' not found";
				}
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}
}
