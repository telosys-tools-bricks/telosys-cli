package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class EnvCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public EnvCommand(ConsoleReader consoleReader) {
		super(consoleReader);
	}

	@Override
	public String getName() {
		return "env";
	}

	@Override
	public String getShortName() {
		return null ;
	}
	
	@Override
	public String getDescription() {
		return "Environment state";
	}
	
	@Override
	public String execute(Environment environment, String[] args) {
		return env(environment);
	}
	
	private String env(Environment environment) {
		StringBuffer sb = new StringBuffer();
		
		appendLine(sb, "ENVIRONMENT : ");
//		buf.append( ". Original directory : " + environment.getOriginalDirectory() );
//		buf.append(OsUtils.LINE_SEPARATOR);

		appendLine(sb, ". Operating System     : " + environment.getOperatingSystem() );		
		appendLine(sb, ". Editor command       : " + environment.getEditorCommand() );
		appendLine(sb, ". Current directory    : " + environment.getCurrentDirectory() );
		appendLine(sb, ". Home directory       : " + nullableString(environment.getHomeDirectory()) );
		appendLine(sb, ". Current GitHub store : " + nullableString(environment.getCurrentGitHubStore()) );
		appendLine(sb, ". Current model        : " + nullableString(environment.getCurrentModel()) );
		appendLine(sb, ". Current bundle       : " + nullableString(environment.getCurrentBundle()) );
		
		return sb.toString();
	}
	
	private String nullableString(String s) {
		if ( s != null ) {
			return s ;
		}
		else {
			return "(undefined)";
		}
	}	
}
