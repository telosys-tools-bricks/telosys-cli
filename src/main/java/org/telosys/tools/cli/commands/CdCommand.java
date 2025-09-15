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
		final String EOL = "\n  ";
		return "cd sub-directory-name " + EOL
			 + "cd .." + EOL
			 + "cd ../.." + EOL
			 + "cd -m [model-name]   (direct change to 'models' directory)" + EOL
			 + "cd -b [bundle-name]  (direct change to 'bundles/templates' directory)" + EOL
			 + "cd -l                (direct change to 'lib' directory)" 
			 ;
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
				// cd without arg => back to home
				setCurrentDirectoryToHomeIfDefined();
			}
			else if ( ! argsWithoutOptions.isEmpty()  &&  options.isEmpty() ) {
				// cd <dir-name>
				cd(argsWithoutOptions.get(0));
			}
			else if ( ! options.isEmpty() ) { // cd with options (new in v 4.3.0)
				if ( options.size() == 1 ) {
					// cd -x  | cd -x <name>
					cdWithOption(options.iterator().next(), argsWithoutOptions );
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

	private void cd(String destination) {
		printDebug("cd : '" + destination + "'");
		if (destination != null) {
			if ("..".equals(destination)) {
				cdParent();
			}
			else if ("../..".equals(destination)) {
				cdParentParent();
			}
			else if (".".equals(destination)) {
				// No change (stay in the current directory )
			}
			else if (destination.contains("..")) {
				print("Invalid destination ('..' in the path) !");
			}
			else {
				cdPath(destination);
			}
		} else {
			print("Invalid destination (null) !");
		}
	}
	
	private File getCurrentDirAsFile() {
		return new File(getCurrentDirectory());
	}
	
	private void cdParent() {
		File parent = getCurrentDirAsFile().getParentFile();
		if (parent != null) {
			tryToChangeCurrentDirectory(parent);
		} else {
			print("No '..' directory.");
		}
	}
	private void cdParentParent() {
		File parent = getCurrentDirAsFile().getParentFile();
		if (parent != null) {
			File parentParent = parent.getParentFile();
			if (parentParent != null) {
				tryToChangeCurrentDirectory(parentParent);
				return;
			}
		}
		print("No '../..' directory.");
	}
	
	private void cdPath(String destination) {
		if ( destination.startsWith("/") ) {
			tryToChangeCurrentDirectory(new File(destination));
		}
		else {
			// Can be an absolute path and not starts with "/", eg "C:\tmp" with Windows )
			File file = new File(destination);
			if ( file.isAbsolute() ) { 
				// Build the full path from the current directory
				tryToChangeCurrentDirectory(file);
			}
			else {
				// Build the full path from the current directory
				String destPath = FileUtil.buildFilePath(getCurrentDirAsFile().getAbsolutePath(), destination);
				tryToChangeCurrentDirectory(new File(destPath));
			}
		}
	}
	
	/**
	 * Try to change the current directory to the given file 
	 * @param file
	 * @return
	 */
	private void tryToChangeCurrentDirectory(File file) {
		if ( checkDirectory(file) ) { // if valid directory
			setCurrentDirectory(file.getAbsolutePath());
		}
	}

	/**
	 * Cd with options like "-m", "-t", ... with or without name argument
	 * Example : cd -m, cd -m cars, cd -t, cd -t python-mvc  
	 * @param option
	 * @param argsWithoutOptions
	 * @return
	 * @since 4.3.0
	 */
	private void cdWithOption(String option, List<String> argsWithoutOptions) {
		File destination = getDestination(option, argsWithoutOptions);
		if ( destination != null ) {
			tryToChangeCurrentDirectory(destination);
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
