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

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.OSType;
import org.telosys.tools.cli.SystemCommand;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;

import jline.console.ConsoleReader;

public class FileExplorerCommand extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public FileExplorerCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "fx";
	}

	@Override
	public String getShortDescription() {
		return "File Explorer" ;
	}

	@Override
	public String getDescription() {
		return "Open a file explorer in the current directory";
	}
	
	@Override
	public String getUsage() {
		return "fx";
	}

	@Override
	public String execute(String[] args) {
		openFileExplorer(getCurrentDirectory());
		return null ;
	}

	private void openFileExplorer(String directory) {

		Environment environment = getEnvironment();
		// Check if the 'File Explorer' command is defined
		String fileExplorerCommand = getFileExplorerCommand(environment);
		if ( fileExplorerCommand != null ) {
			launchFileExplorer(fileExplorerCommand, directory, environment);
		}
		else {
			print("'File Explorer' command is undefined, check your configuration.");
		}
		
		
//		if (!StrUtil.nullOrVoid(shortFileName)) {
//			String fileToBeEdited = FileUtil.buildFilePath(getCurrentDirectory(), shortFileName);
//			return launchEditor(fileToBeEdited);
//		} else {
//			return launchEditor("");
//		}
	}
	
	private String getFileExplorerCommand(Environment environment) {
		// Check if the 'File Explorer' command is defined
		String fileExplorerCommand = null ;
		// TODO : String fileExplorerCommand = environment.getFileExplorerCommand();
		if ( fileExplorerCommand == null ) {
			// No specific command defined => try to determine the defaut File Explorer for the current OS
			print("No 'File Explorer' command configuration, trying to get default command");
			OSType osType = environment.getOperatingSystemType();
			switch ( osType ) {
			case WINDOWS :
				return "explorer ${DIR}";
			case MACOS :
				return "open ${DIR}";
			default :
				return null;
			}
		}
		else {
			return fileExplorerCommand;
		}
	}
	
	private void launchFileExplorer(String fileExplorerCommand, String directory, Environment environment) {
		// Replace $DIR or ${DIR} if present
		String fullCommand = "" ;
		if ( fileExplorerCommand.contains("$DIR") ) {
			fullCommand = StrUtil.replaceVar(fileExplorerCommand, "$DIR", directory);
		}
		else if ( fileExplorerCommand.contains("${DIR}") ) {
			fullCommand = StrUtil.replaceVar(fileExplorerCommand, "${DIR}", directory);
		}
		else {
			fullCommand = fileExplorerCommand;
		}

		print("Launching '" + fullCommand + "'");
		SystemCommand.run(fullCommand, environment);
	}
}
