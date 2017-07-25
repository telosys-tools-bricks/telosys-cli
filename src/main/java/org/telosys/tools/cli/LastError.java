package org.telosys.tools.cli;

public class LastError {

	private static String message ;

	private static Exception exception ;
	
	public final static void setError(String msg, Exception ex ) {
		message = msg ;
		exception = ex ;
	}
	public final static void setError(Exception ex ) {
		message = null ;
		exception = ex ;
	}
	public final static void setError(String msg ) {
		message = msg ;
		exception = null ;
	}
	
	public final static boolean hasError() {
		return message != null || exception != null ;
	}
	public final static String getMessage() {
		return message ;
	}
	public final static Exception getException() {
		return exception;
	}
}
