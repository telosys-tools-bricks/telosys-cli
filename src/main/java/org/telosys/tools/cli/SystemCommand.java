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
package org.telosys.tools.cli;

import java.io.File;
import java.io.IOException;

public class SystemCommand {

	/**
	 * Private constructor
	 */
	private SystemCommand() {
	}

	/**
	 * Runs the given command for the given OS type
	 * @param fullCommand
	 * @param osType
	 */
	public static void run(String fullCommand, OSType osType)  {
		try {
			runSystemCommand(fullCommand, osType);
		} catch (IOException e) {
			throw new RuntimeException("Cannot run system command", e);
		}
	}
	
	private static Process runSystemCommand(String fullCommand, OSType osType) throws IOException {
		switch ( osType ) {
		case LINUX :
			return runLinuxCommand(fullCommand);
		case WINDOWS :
			return runWindowsCommand(fullCommand);
		case MACOS :
			return runMacOSCommand(fullCommand);
		//case UNKNOWN :
		default:
			throw new RuntimeException("Unknown Operating System");
		}
	}

	private static Process runWindowsCommand(String fullCommand) throws IOException {
		// For WINDOWS launch the full command line "as is" 
		return Runtime.getRuntime().exec(fullCommand);
	}

	private static Process runMacOSCommand(String fullCommand) throws IOException {
		// For MAC OS launch the full command line "as is" 
		return Runtime.getRuntime().exec(fullCommand);
	}

	private static Process runLinuxCommand(String fullCommand ) throws IOException {
		// For LINUX split the full command in words tokens and execute 
		String[] words = Utils.getWords(fullCommand);
		// Executes the command with separated arguments
		return runLinuxCommand(words);
	}
	
	/**
	 * Runs the given command arguments 
	 * @param commandArray the arguments (eg : "/bin/sh", "-c", "myfile.sh" )
	 * @return
	 * @throws IOException
	 */
	private static Process runLinuxCommand(String[] commandArray ) throws IOException {
		
		// envp 
		// array of strings, each element of which has environment variable settings 
		// in the format name=value, or null if the subprocess should inherit the environment 
		// of the current process.
		String[] envp = null ; 
		
		// dir 
		// the working directory of the subprocess, 
		// or null if the subprocess should inherit the working directory of the current process.
		File dir = null ;
		
		return Runtime.getRuntime().exec(commandArray, envp, dir);
	}
}
