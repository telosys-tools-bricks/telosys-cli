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
