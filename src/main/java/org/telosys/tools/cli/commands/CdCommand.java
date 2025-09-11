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
package org.telosys.tools.cli.commands;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.FileUtil;

/**
 * 'cd' command
 * 
 * @author Laurent GUERIN
 *
 */
public class CdCommand extends Command {

	private static final String M_OPTION = "-m" ;
	private static final String B_OPTION = "-b" ;
	private static final String L_OPTION = "-l" ;
	
	private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList(M_OPTION, B_OPTION, L_OPTION )); 

	/**
	 * Constructor
	 * @param out
	 */
	public CdCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader,environment);
	}

	@Override
	public String getName() {
		return "cd";
	}

	@Override
	public String getShortDescription() {
		return "Change Directory" ;
	}

	@Override
	public String getDescription() {
		return "Change the current directory";
	}
	
	@Override
	public String getUsage() {
		return "cd [directory]";
	}
	
	@Override
	public String execute(String[] argsArray) {
		
		List<String> commandArguments = getArgumentsAsList(argsArray);
		// Check arguments :
		// 0 : cd 
		// 1 : cd <dest-dir> | cd -m | cd -t | cd -l
		// 2 : cd -m <model-name> | cd -b <bundle-name>
		if ( checkArguments(commandArguments, 0, 1, 2 ) && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
			Set<String> options = getOptions(commandArguments);
			List<String> argsWithoutOptions = removeOptions(commandArguments);
			if ( argsWithoutOptions.isEmpty() && options.isEmpty() ) {
				// cd
				return cdHome();
			}
			else if ( ! argsWithoutOptions.isEmpty()  &&  options.isEmpty() ) {
				// cd <dir-name>
				return cd(argsWithoutOptions.get(0));
			}
			else if ( ! options.isEmpty() ) { // cd with options (new in v 4.3.0)
				if ( options.size() == 1 ) {
					// cd -x  | cd -x <name>
					return cdWithOption(options.iterator().next(), argsWithoutOptions );
				}
				else {
					print("Use only one option (-m, -t, ..) ");
				}
			}
			else {
				print("Invalid 'cd' usage");
			}
		}
		return null;
	}

	private String cdHome() {
		Environment environment = getEnvironment();
		environment.resetCurrentDirectoryToHomeIfDefined();
		return environment.getCurrentDirectory();
	}
	
	private String cd(String destination) {
		printDebug("cd : '" + destination + "'");
		Environment environment = getEnvironment();
		if (destination != null) {
			if ("..".equals(destination)) {
				printDebug("cd : '..' => change to parent dir ");
				return cdParent();
			}
			else if (".".equals(destination)) {
				printDebug("cd : '.' => stay in the current dir ");
				// No change (stay in the current directory )
				return environment.getCurrentDirectory();
			}
			else if (destination.contains("..")) {
				printDebug("cd : contains '..' => invalid directory ");
				return "Invalid directory ('..' in the path) !";
			}
			else {
				return cdPath(destination);
			}
		} else {
			return "Invalid destination (null) !";
		}
	}
	
	private File getCurrentDir() {
		Environment environment = getEnvironment();
		return new File(environment.getCurrentDirectory());
	}
	
	private String cdParent() {
		File current = getCurrentDir();
		File parent = current.getParentFile();
		if (parent != null) {
			return tryToChangeCurrentDirectory(current.getParentFile());
		} else {
			return "No parent directory.";
		}
	}
	
	private String cdPath(String destination) {
		if ( destination.startsWith("/") ) {
			printDebug("cd : absolute path");
			return tryToChangeCurrentDirectory(new File(destination));
		}
		else {
			// Can be an absolute path and not starts with "/", eg "C:\tmp" with Windows )
			File file = new File(destination);
			if ( file.isAbsolute() ) { 
				printDebug("cd : absolute path");
				// Build the full path from the current directory
				return tryToChangeCurrentDirectory(file);
			}
			else {
				printDebug("cd : subfolder");
				// Build the full path from the current directory
				String destPath = FileUtil.buildFilePath(getCurrentDir().getAbsolutePath(), destination);
				return tryToChangeCurrentDirectory(new File(destPath));
			}
		}
	}
	
	/**
	 * Try to change the current directory to the given file 
	 * @param file
	 * @return
	 */
	private String tryToChangeCurrentDirectory(File file) {
		if ( checkDirectory(file) ) { // if valid directory
			Environment environment = getEnvironment();
			environment.setCurrentDirectory(file.getAbsolutePath());
			return environment.getCurrentDirectory();
		}
		return null;
	}

	/**
	 * Cd with options like "-m", "-t", ... with or without name argument
	 * Example : cd -m, cd -m cars, cd -t, cd -t python-mvc  
	 * @param option
	 * @param argsWithoutOptions
	 * @return
	 * @since 4.3.0
	 */
	private String cdWithOption(String option, List<String> argsWithoutOptions) {
		File destination = getDestination(option, argsWithoutOptions);
		if ( destination != null ) {
			return tryToChangeCurrentDirectory(destination);
		}
		else {
			return null;
		}
	}

	private File getDestination(String option, List<String> argsWithoutOptions) {
		TelosysProject telosysProject = getTelosysProject();
		String name = null ;
		if ( ! argsWithoutOptions.isEmpty() ) {
			name = argsWithoutOptions.get(0);
		}
		if ( M_OPTION.equals(option) ) {
			// go to "models" dir => "(home)/TelosysTools/models" 
			if ( name != null ) {
				return telosysProject.getModelFolder(name);
			} 
			else {
				return telosysProject.getModelsFolder();
			}
		}
		else if ( B_OPTION.equals(option) ) {
			// go to "bundles" dir => "(home)/TelosysTools/templates" 
			if ( name != null ) {
				return telosysProject.getBundleFolder(name);
			} 
			else {
				return telosysProject.getBundlesFolder();
			}
		}
		else if ( L_OPTION.equals(option) ) {
			// go to "lib" dir => "(home)/TelosysTools/lib" 
			return telosysProject.getLibFolder();
		}
		else {
			printError("Invalid option '" + option + "'");
			return null;
		}
	}
}
