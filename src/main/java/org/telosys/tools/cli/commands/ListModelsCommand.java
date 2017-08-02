package org.telosys.tools.cli.commands;

import java.io.File;
import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class ListModelsCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListModelsCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "lm";
	}

	@Override
	public String getShortDescription() {
		return "List Models" ;
	}

	@Override
	public String getDescription() {
		return "List the models";
	}
	
	@Override
	public String getUsage() {
		return "lm";
	}

	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			return listModels();
		}
		return null ;
	}

	private String listModels() {
		TelosysProject telosysProject = getTelosysProject();
		try {
			List<File> files = telosysProject.getModels();
			StringBuffer sb = new StringBuffer();
			for ( File f : files ) {
				appendLine(sb, " . " + f.getName() );
			}
			return sb.toString();
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

}
