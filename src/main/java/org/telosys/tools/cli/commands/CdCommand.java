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
		Environment environment = getEnvironment();
		if (destination != null) {
			if ("..".equals(destination)) {
				File current = new File(environment.getCurrentDirectory());
				File parent = current.getParentFile();
				if (parent != null) {
					return tryToChangeCurrentDirectory(environment, current.getParentFile());
				} else {
					return "No parent directory.";
				}
			}
			if (".".equals(destination)) {
				// No change (stay in the current directory )
				return environment.getCurrentDirectory();
			} else {
				if (destination.contains("..")) {
					return "Invalid syntax!";
				} else if (destination.startsWith("/")) {
					return tryToChangeCurrentDirectory(environment, new File(destination));
				} else {
					File current = new File(environment.getCurrentDirectory());
					String destPath = FileUtil.buildFilePath(current.getAbsolutePath(), destination);
					return tryToChangeCurrentDirectory(environment, new File(destPath));
				}
			}
		} else {
			environment.resetCurrentDirectoryToHomeIfDefined();
			return environment.getCurrentDirectory();
		}

	}

	private String tryToChangeCurrentDirectory(Environment environment, File file) {
		if (file == null) {
			return "Destination is null!";
		}
		if (file.exists()) {
			if (file.isDirectory()) {
				environment.setCurrentDirectory(file.getAbsolutePath());
				return environment.getCurrentDirectory();
			} else {
				return "'" + file.getAbsolutePath() + "' is not a directory!";
			}
		} else {
			return "Invalid directory!";
		}
	}

}
