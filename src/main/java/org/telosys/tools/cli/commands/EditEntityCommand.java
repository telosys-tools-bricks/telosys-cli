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
		return "Edit an entity file";
	}
	
	@Override
	public String getUsage() {
		return "ee entity-name";
	}

	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			if ( checkModelDefined() ) {
				if ( getCurrentModel().endsWith(".model") ) {
					return editEntityDSL(args[1]);
				}
				else {
					editDBModelFile();
				}
			}
		}
		else {
			return invalidUsage("entity-name expected");
		}
		return null ;
	}

	/**
	 * Edit the ENTITY FILE  ( eg Car.entity ) 
	 * @param entityName
	 * @return
	 */
	private String editEntityDSL(String entityName) {
		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
			try {
				File file = telosysProject.buildDslEntityFile(getCurrentModel(), entityName);
				return launchEditor(file.getAbsolutePath() );
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}

	/**
	 * Edit the DBMODEL FILE ( eg bookstore.dbmodel / .dbrep )
	 * @return
	 */
	private String editDBModelFile() {
		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
			try {
				File file = telosysProject.getModelFile(getCurrentModel());
				return launchEditor(file.getAbsolutePath() );
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}


}
