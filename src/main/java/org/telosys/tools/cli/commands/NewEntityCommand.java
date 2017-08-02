package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class NewEntityCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public NewEntityCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "ne";
	}

	@Override
	public String getShortDescription() {
		return "New Entity" ;
	}

	@Override
	public String getDescription() {
		return "Creates a new entity in the current model";
	}
	
	@Override
	public String getUsage() {
		return "ne [entity-name]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkModelDefined() ) {
			if ( args.length > 1 ) {
				return newEntity(args[1]);
			}
			else {
				return invalidUsage("entity-name expected");
			}
		}
		return null ;
	}

	private String newEntity(String entityName) {

		TelosysProject telosysProject = getTelosysProject();
		String modelName = getCurrentModel();
		if ( telosysProject != null ) {
			try {
				telosysProject.createNewDslEntity(modelName, entityName);
				print("Entity '"+ entityName + "' created.");
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}

}
