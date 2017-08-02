package org.telosys.tools.cli.commands;


import java.util.LinkedList;
import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

import tests.SelectionList;

public class GuideCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public GuideCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "guide";
	}

	@Override
	public String getShortDescription() {
		return "Guide" ;
	}
	
	@Override
	public String getDescription() {
		return "Guide ";
	}
	
	@Override
	public String getUsage() {
		return "guide";
	}
		

	@Override
	public String execute(String[] args) {
//		return guide(environment, (String) args[0]);

		List<String> items = new LinkedList<>();
		for ( int i = 0 ; i < 20 ; i++ ) {
			items.add("Item #" + i);
		}
		SelectionList list = new SelectionList(items);
		list.show();
		return null ;
	}
	
	private String guide(final String parameter) {
		StringBuffer sb = new StringBuffer();
		
		appendLine(sb, "1) Initialize the project environment");
		appendLine(sb, " . cd [project-directory]");
		appendLine(sb, " . init");
		
		appendLine(sb, "2) Configure the project (edit the properties)");
		appendLine(sb, " . cd [project-directory]");
		appendLine(sb, " . edit telosys-tools.cfg");

		appendLine(sb, "3) Copy JDBC jar files in [project-directory/TelosysTools/lib]");

		appendLine(sb, "4) Configure the databases ");
		appendLine(sb, " . cd [project-directory/TelosysTools]");
		appendLine(sb, " . edit databases.dbcfg");
		
		appendLine(sb, "5) Check the database configuration");
		appendLine(sb, " . dbtest [db-id]");

		appendLine(sb, "6) Generate the database model");
		
		
		return sb.toString();
	}

//	private void append(StringBuffer sb, String s) {
//		sb.append(s);
//		sb.append(OsUtils.LINE_SEPARATOR);
//	}
	
}
