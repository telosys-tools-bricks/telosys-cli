package org.telosys.tools.cli;

import java.io.IOException;
import java.io.PrintWriter;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.StrUtil;

public abstract class Command {

	private final PrintWriter out ;
	private String lastErrorMessage ;
	
	
	public Command(PrintWriter out) {
		super();
		this.out = out;
	}
	public abstract String getName();
	public abstract String getShortName();
	public abstract String execute(Environment environment, String[] args);
	public abstract String getDescription();
	
	protected void appendLine(StringBuffer sb, String s) {
		sb.append(s);
		sb.append(Environment.LINE_SEPARATOR);
	}
	
	protected void appendEndOfLine(StringBuffer sb) {
		sb.append(Environment.LINE_SEPARATOR);
	}

	protected String getLastErrorMessage() {
		return lastErrorMessage;
	}

	protected String invalidUsage(String message) {
		return "Invalid usage : " + message ;
	}

	protected void print(String message) {
		out.println(message);
		out.flush();
	}
	protected void printError(Exception ex) {
		out.println("[ERROR] Exception : "+ ex.getMessage());
		out.flush();
	}
	protected void printDebug(String message) {
		if ( Trace.DEBUG ) {
			out.println("[DEBUG] "+message);
			out.flush();
		}
	}
	
	protected String launchEditor(Environment environment, String fileFullPath) {
		String editorCommand = environment.getEditorCommand();
		String fullCommand = editorCommand ;
		if ( ! StrUtil.nullOrVoid(fileFullPath) ) {
			fullCommand = editorCommand + " " + fileFullPath;
		}
		launchSystemCommand(fullCommand);
		return fullCommand ;
	}
	
	protected void launchSystemCommand(String fullCommand) {
		try {
			Runtime.getRuntime().exec(fullCommand);
		} catch (IOException e) {
			print( "ERROR : IOException : " + e.getMessage() );
		}
	}

	protected TelosysProject getTelosysProject(Environment environment) {
		if ( environment.getHomeDirectory() == null ) {
			lastErrorMessage = "Home directory must be set before using this command!" ;
			return null ;
		}
		
		String projectFullPath = environment.getHomeDirectory();
		TelosysProject telosysProject = new TelosysProject(projectFullPath);
		return telosysProject ;
	}
}   
