package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Const;
import org.telosys.tools.cli.Environment;

public class EditProjectDbCfgCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditProjectDbCfgCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}


	@Override
	public String getName() {
		return "epdb";
	}

	@Override
	public String getShortDescription() {
		return "Edit Project Databases" ;
	}

	@Override
	public String getDescription() {
		return "Open an editor to edit the '.dbcfg' file ";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			String fileToBeEdited = getTelosysDbCfgFullPath();
			if (fileToBeEdited != null) {
				return launchEditor(fileToBeEdited);
			} else {
				return "ERROR: file '" + Const.DATABASES_DBCFG + "' not found";
			}			
		}
		return null ;
	}

}
