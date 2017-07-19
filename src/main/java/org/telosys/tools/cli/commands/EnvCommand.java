package org.telosys.tools.cli.commands;

import java.io.PrintWriter;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class EnvCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public EnvCommand(PrintWriter out) {
		super(out);
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

		appendLine(sb, ". Operating System  : " + environment.getOperatingSystem() );		
		appendLine(sb, ". Editor command    : " + environment.getEditorCommand() );
		appendLine(sb, ". Current directory : " + environment.getCurrentDirectory() );
		appendLine(sb, ". Home directory    : " + nullableString(environment.getHomeDirectory()) );
		appendLine(sb, ". Current model     : " + nullableString(environment.getCurrentModel()) );
		
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
