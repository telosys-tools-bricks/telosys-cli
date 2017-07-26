package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.LastError;

public class ErrorCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ErrorCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "err";
	}

	@Override
	public String getShortDescription() {
		return "Error" ;
	}
	
	@Override
	public String getDescription() {
		return "Print details about the last error";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( LastError.hasError() ) {
			StringBuffer sb = new StringBuffer();			
			appendLine(sb, "Last error :");
			
			appendLine(sb, "Message :");
			if ( LastError.getMessage() != null ) {
				appendLine(sb, " " + LastError.getMessage());
			}
			else {
				appendLine(sb, " (no message)" );
			}

			appendLine(sb, "Exception :");
			if ( LastError.getException() != null ) {
				appendLine(sb, getExceptionInfo(LastError.getException()) );
			}
			else {
				appendLine(sb, " (no exception)" );
			}
			
			return sb.toString();
		}
		else {
			return "No error" ;
		}
		
	}
	private String getExceptionInfo(Exception e) {
		StringBuffer sb = new StringBuffer();			
		appendLine(sb, " class   : " + e.getClass().getSimpleName() );
		appendLine(sb, " message : " + e.getMessage() );
		appendLine(sb, " stack trace : " );
		appendLine(sb, getStackTraceInfo(e) );
		if ( e.getCause() != null ) {
			appendLine(sb, "Cause : " );
			appendLine(sb, getExceptionInfo(e) );
		}
		return sb.toString();
	}
	
	private String getStackTraceInfo(Exception e) {
		StringBuffer sb = new StringBuffer();			
		StackTraceElement[] stack = e.getStackTrace() ;
		for ( StackTraceElement ste : stack ) {
			appendLine(sb, "  " + ste.getFileName() + "[" + ste.getLineNumber()+"] "+ ste.getMethodName() );
		}
		return sb.toString();
	}	
}
