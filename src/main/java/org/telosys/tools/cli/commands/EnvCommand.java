package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class EnvCommand extends Command {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public EnvCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "env";
	}

	@Override
	public String getShortDescription() {
		return "Environment" ;
	}
	
	@Override
	public String getDescription() {
		return "Environment state";
	}
	
	@Override
	public String getUsage() {
		return "env";
	}
	
	@Override
	public String execute(String[] args) {
		return env();
	}
	
	private String env() {
		Environment environment = getEnvironment();
		
		StringBuffer sb = new StringBuffer();
		
		appendLine(sb, "ENVIRONMENT : ");
//		buf.append( ". Original directory : " + environment.getOriginalDirectory() );
//		buf.append(OsUtils.LINE_SEPARATOR);

		appendLine(sb, ". Operating System     : " + environment.getOperatingSystem() );		
		appendLine(sb, ". Editor command       : " + environment.getEditorCommand() );
		appendLine(sb, ". '.jar' file          : " + environment.getJarLocation() );
		appendLine(sb, ". Current directory    : " + environment.getCurrentDirectory() );
		appendLine(sb, ". Home directory       : " + undefinedIfNull(environment.getHomeDirectory()) );
		appendLine(sb, ". Current GitHub store : " + undefinedIfNull(environment.getCurrentGitHubStore()) );
		appendLine(sb, ". Current model        : " + undefinedIfNull(environment.getCurrentModel()) );
		appendLine(sb, ". Current bundle       : " + undefinedIfNull(environment.getCurrentBundle()) );
		
		return sb.toString();
	}
	
}
