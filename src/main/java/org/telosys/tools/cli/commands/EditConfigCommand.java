package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Const;
import org.telosys.tools.cli.Environment;

public class EditConfigCommand extends Command {

	public final static String COMMAND_NAME = "ecfg";
	
	/**
	 * Constructor
	 * @param out
	 */
	public EditConfigCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME ;
	}

	@Override
	public String getShortDescription() {
		return "Edit Configuration" ;
	}

	@Override
	public String getDescription() {
		return "Open an editor to edit '" + Const.TELOSYS_TOOLS_CFG + "'";
	}

	@Override
	public String getUsage() {
		return COMMAND_NAME ;
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			String fileToBeEdited = getTelosysToolsCfgFullPath();
			if (fileToBeEdited != null) {
				return launchEditor(fileToBeEdited);
			} else {
				return "ERROR: file '" + Const.TELOSYS_TOOLS_CFG + "' not found";
			}			
		}
		return null ;
	}

}
