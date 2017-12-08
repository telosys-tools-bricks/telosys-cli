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

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.FileUtil;

public class CdCommand extends Command {

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
	public String execute(String[] args) {
		
		if ( args.length > 1 ) {
			return cd(args[1]);
		}
		else {
			return cd(null);
		}
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
			environment.resetCurrentDirectoryToHomeIfDefined();
			return environment.getCurrentDirectory();
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
		File file = new File(destination);
		if ( file.isAbsolute() ) {
			printDebug("cd : absolute path");
			return tryToChangeCurrentDirectory(file);
		}
		else {
			printDebug("cd : subfolder");
			File current = getCurrentDir();
			String destPath = FileUtil.buildFilePath(current.getAbsolutePath(), destination);
			return tryToChangeCurrentDirectory(new File(destPath));
		}
	}
	
	/**
	 * Try to change the current directory to the given file 
	 * @param file
	 * @return
	 */
	private String tryToChangeCurrentDirectory(File file) {
		if ( checkDirectory(file) ) {
			Environment environment = getEnvironment();
			environment.setCurrentDirectory(file.getAbsolutePath());
			return environment.getCurrentDirectory();
		}
		return null;
	}
}
