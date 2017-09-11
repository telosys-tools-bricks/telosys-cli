package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Const;
import org.telosys.tools.cli.Environment;

public class EditDatabasesCommand extends Command {

	public final static String COMMAND_NAME = "edb";
			
	/**
	 * Constructor
	 * @param out
	 */
	public EditDatabasesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME ;
	}

	@Override
	public String getShortDescription() {
		return "Edit Databases" ;
	}

	@Override
	public String getDescription() {
		return "Open an editor to edit the '" + Const.DATABASES_DBCFG + "' file ";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME ;
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
