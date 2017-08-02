package org.telosys.tools.cli.commands;

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class EditEntityCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditEntityCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "ee";
	}

	@Override
	public String getShortDescription() {
		return "Edit Entity" ;
	}

	@Override
	public String getDescription() {
		return "Edit an entity file ( 'ee [entity-name]' ) ";
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			if ( checkModelDefined() ) {
				return editEntity(args[1]);
			}
		}
		else {
			return invalidUsage("entity-name expected");
		}
		return null ;
	}

	private String editEntity(String entityName) {
		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
			File file;
			try {
				file = telosysProject.buildDslEntityFile(getCurrentModel(), entityName);
				return launchEditor(file.getAbsolutePath() );
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}

}
