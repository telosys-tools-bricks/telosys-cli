package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class DeleteEntityCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public DeleteEntityCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "de";
	}

	@Override
	public String getShortDescription() {
		return "Delete Entity" ;
	}

	@Override
	public String getDescription() {
		return "Deletes the given entity in the current model";
	}
	
	@Override
	public String getUsage() {
		return "de [entity-name]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkModelDefined() ) {
			if ( args.length > 1 ) {
				return deleteEntity(args[1]);
			}
			else {
				return invalidUsage("entity-name expected");
			}
		}
		return null ;
	}

	private String deleteEntity(String entityName) {

		TelosysProject telosysProject = getTelosysProject();
		String modelName = getCurrentModel();
		try {
			if ( telosysProject.deleteDslEntity(modelName, entityName) ) {
				print("Entity '"+ entityName + "' deleted.");
			}
			else {
				print("Entity '"+ entityName + "' not found.");
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

}
