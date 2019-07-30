/**
 *  Copyright (C) 2015-2019 Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.cli.args;

import java.io.File;
import java.io.PrintWriter;

import static org.telosys.tools.cli.args.Arguments.*;

import org.telosys.tools.cli.Trace;

/**
 * Application entry point for CLI
 * 
 * @author Laurent GUERIN
 *
 */
public class ArgumentsParser {
		
	private final PrintWriter out ;

	/**
	 * Constructor
	 * @param out output where to print if necessary
	 */
	public ArgumentsParser(PrintWriter out) {
		this.out = out ;
	}
	
	private void print(String message) {
		out.println(message);
		out.flush();
	}

	private void debug(String message) {
		if ( Trace.DEBUG ) {
			print("[DEBUG] " + message);
		}
	}
	private void debug(String[] args) {
		if ( Trace.DEBUG ) {
			print("args (length = " + args.length + ") : ");
			int i = 0 ;
			for (String s : args ) {
				print("  " + i + " : " + s);
				i++;
			}
		}
	}
	
	/**
	 * Parse the OS command arguments
	 * e.g. : -h homedir -i inputfile
	 * @param args
	 * @return 
	 */
	public Arguments parseArguments(String[] args) {
		debug(args);
		try {
			return processArguments(args);
		} catch (Exception e) {
			print("ERROR : Unexpected exception " + e.getMessage() );
			return new Arguments();
		}
	}

	/**
	 * Processes the arguments if any <br>
	 * @param args
	 * @return
	 */
	private Arguments processArguments(String[] args) {
		Arguments arguments = new Arguments();
		// args do not containts the initial command ( eg "java -jar xxx" )  // TODO : check it !
		// so the usefull arguments starts at ZERO 
		for ( int i = 0 ; i < args.length ; i++ ) {
			String arg = args[i];
			if ( H_ARG.equals(arg) ) {
				debug(H_ARG) ;
				String home = getNextArg(args, i);
				File homeFile = getFile( home );
				if ( homeFile != null && homeFile.exists() && homeFile.isDirectory() ) {
					arguments.put(new Argument(H_ARG, homeFile) );
				}
				else {
					print("Invalid 'home dir' argument : '" + home + "'");
				}
			}
			if ( I_ARG.equals(arg) ) {
				debug(I_ARG) ;
				String fileName = getNextArg(args, i);
				File file = getFile( fileName );
				if ( file != null && file.exists() && file.isFile() ) {
					arguments.put(new Argument(I_ARG, file) );
				}
				else {
					print("Invalid 'input file' argument : '" + fileName + "'");
				}
			}
			if ( O_ARG.equals(arg) ) {
				debug(O_ARG) ;
				String fileName = getNextArg(args, i);
				File file = getFile( fileName );
				if ( file != null ) {
					arguments.put(new Argument(O_ARG, file) );
				}
				else {
					print("Invalid 'output file' argument : '" + fileName + "'");
				}
			}
		}
		return arguments;
	}
		
	private String getNextArg(String[] args, int i) {
		int j = i+1;
		if ( j < args.length ) {
			String arg = args[j] ;
			if ( ! arg.startsWith("-") ) {
				return arg ;
			}
			print("Unexpected argument '" + arg + "', file or folder expected (not supposed to stars with '-')");
		}
		return null ;
	}

	private File getFile(String fileName) {
		if ( fileName != null ) {
			return new File(fileName);
		}
		return null ;
	}	
}
