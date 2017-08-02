package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Const;
import org.telosys.tools.cli.Environment;

public class EditProjectCfgCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditProjectCfgCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}


	@Override
	public String getName() {
		return "epc";
	}

	@Override
	public String getShortDescription() {
		return "Edit Project Configuration" ;
	}

	@Override
	public String getDescription() {
		return "Open an editor to edit the '.cfg' file ";
	}

	@Override
	public String getUsage() {
		return "epc";
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
