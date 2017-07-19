package org.telosys.tools.cli.commands;

import java.io.File;
import java.io.PrintWriter;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class ModelCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public ModelCommand(PrintWriter out) {
		super(out);
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
	public String execute(Environment environment, String[] args) {
		
		if ( args.length > 1 ) {
			return setCurrentModel(environment, args[1]);
		}
		else {
			return invalidUsage("model-name argument expected");
		}
	}
	
	private String setCurrentModel(Environment environment, String modelName) {
		TelosysProject telosysProject = getTelosysProject(environment); 
		if ( telosysProject != null ) {
			try {
				File file = telosysProject.getDslModelFile(modelName);
				if (file.exists()) {
					environment.setCurrentModel(modelName);
					return "Current model is now '" + environment.getCurrentModel() + "'";
				}
				else {
					return "Model '" + modelName + "' not found";
				}
			} catch (TelosysToolsException e) {
				return "Error: " + e.getMessage();
			}
		}
		else {
			return getLastErrorMessage();
		}
		

	}
}
