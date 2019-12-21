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

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;

public class MkdirCommand extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public MkdirCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader,environment);
	}

	@Override
	public String getName() {
		return "mkdir";
	}

	@Override
	public String getShortDescription() {
		return "Make Directory" ;
	}

	@Override
	public String getDescription() {
		return "Create a new directory in the current location";
	}
	
	@Override
	public String getUsage() {
		return "mkdir directory-name";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( args.length > 1 ) {
			mkdir(args[1]);
		}
		else {
			print("Invalid usage : directory-name expected");
		}
		return null ;
	}

	private void mkdir(String directory) {
		printDebug("mkdir : '" + directory + "'");
		try {
			String currentDir = getEnvironment().getCurrentDirectory();
			String dirFullPath = FileUtil.buildFilePath(currentDir, directory);
			File dir = new File(dirFullPath);
			if ( dir.exists() ) {
				String type = "" ;
				if ( dir.isFile() ) {
					type = "file" + ( dir.isHidden() ? "/hidden" : "" ) ;
				}
				if ( dir.isDirectory() ) {
					type = "directory" + ( dir.isHidden() ? "/hidden" : "" ) ;
				}
				print("'"+ directory +"' already exists (" + type + ")"); 
			}
			else {
				DirUtil.createDirectory(dir);
				print("Directory '"+ directory +"' created"); 
			}
		} catch (Exception e) {
			print("Error: Cannot create '"+ directory +"' directory"); 
			print("Exception '" + e.getClass().getCanonicalName() + "' : " + e.getMessage()); 
		}
	}
	
}
