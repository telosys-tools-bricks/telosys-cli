package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

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
		return "newentity";
	}

	@Override
	public String getShortName() {
		return "ne" ;
	}

	@Override
	public String getDescription() {
		return "Creates a new entity in the current model ( 'ne [entity-name]' ) ";
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			if ( getCurrentModel() != null ) {
				return newEntity(args[1]);
			}
			else {
				print("No current model!");
				return null ;
			}
		}
		else {
			return invalidUsage("entity-name expected");
		}
	}

	private String newEntity(String entityName) {

		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
			
// TODO			
//			telosysProject.createNewDslEntity(xxxx);
//			File file;
//			try {
//				file = telosysProject.getDslEntityFile(environment.getCurrentModel(), entityName);
//				return launchEditor(environment, file.getAbsolutePath() );
//			} catch (TelosysToolsException e) {
//				printError(e);
//			}
		}
		return null ;
	}

}
