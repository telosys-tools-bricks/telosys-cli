/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
	public String getUsage() {
		return "err";
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
	private String getExceptionInfo(Throwable e) {
		StringBuffer sb = new StringBuffer();			
		appendLine(sb, " class   : " + e.getClass().getSimpleName() );
		appendLine(sb, " message : " + e.getMessage() );
		appendLine(sb, " stack trace : " );
		appendLine(sb, getStackTraceInfo(e) );
		Throwable cause = e.getCause();
		if ( cause != null ) {
			appendEndOfLine(sb);
			appendLine(sb, "Cause : " );
			//appendLine(sb, getExceptionInfo(cause) );
			sb.append( getExceptionInfo(cause) );
		}
		return sb.toString();
	}
	
	private String getStackTraceInfo(Throwable e) {
		StringBuffer sb = new StringBuffer();			
		StackTraceElement[] stack = e.getStackTrace() ;
		for ( StackTraceElement ste : stack ) {
			//appendLine(sb, "  " + ste.getFileName() + "[" + ste.getLineNumber()+"] "+ ste.getMethodName() );
			appendLine(sb, "  " + ste.getClassName() + "[" + ste.getLineNumber()+"] "+ ste.getMethodName() );
		}
		return sb.toString();
	}	
}
