package org.telosys.tools.cli.commands;

import java.io.File;
import java.io.PrintWriter;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class EditEntityCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditEntityCommand(PrintWriter out) {
		super(out);
	}

	@Override
	public String getName() {
		return "editentity";
	}

	@Override
	public String getShortName() {
		return "ee" ;
	}

	@Override
	public String getDescription() {
		return "Edit an entity file ( 'ee [entity-name]' ) ";
	}
	
	@Override
	public String execute(Environment environment, String[] args) {
		if ( args.length > 1 ) {
			if ( environment.getCurrentModel() != null ) {
				return editEntity(environment, args[1]);
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

	private String editEntity(Environment environment, String entityName) {

		TelosysProject telosysProject = getTelosysProject(environment);
		File file;
		try {
			file = telosysProject.getDslEntityFile(environment.getCurrentModel(), entityName);
			return launchEditor(environment, file.getAbsolutePath() );
		} catch (TelosysToolsException e) {
			printError(e);
			return null ;
		}
	}

}
