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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.PropertiesManager;


public class Environment {
	
	private static final String TELOSYS_CLI_CFG = "telosys-cli.cfg" ;
	private static final String EDITOR_COMMAND  = "EditorCommand" ;
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private final CommandProvider commandProvider ;
	private final CommandsGroups  commandsGroups ;
	
	private final String jarLocation ;
	private final String osName ;
	private final String editorCommand ;
	private final String originalDirectory ;

	private       String homeDirectory ;
	private       String currentDirectory ;
	private       String currentGitHubStore = "telosys-templates-v3";
	private       String currentModel ;
	private       String currentBundle ;

	
	/**
	 * Constructor
	 * @param commandProvider
	 */
	public Environment(CommandProvider commandProvider) {
		super();
		this.commandProvider = commandProvider ;
		this.commandsGroups  = new CommandsGroups();
		this.jarLocation = findJarFullPath();
		this.originalDirectory = System.getProperty("user.dir"); 
		this.currentDirectory = originalDirectory ;
		this.homeDirectory    = null ;
		this.currentModel     = null ;
		this.currentBundle    = null ;
		this.osName = System.getProperty("os.name");
		this.editorCommand = findEditorCommand();
	}
	
	/**
	 * Returns the editor command to be used <br>
	 * The specific command if defined in the '.cfg' file or the standard default command <br>
	 * @return
	 */
	private String findEditorCommand() {
		String specificEditorCommand = findSpecificEditorCommand();
		if ( specificEditorCommand != null ) {
			return specificEditorCommand ;
		}
		else {
			return determineDefaultEditorCommand();
		}
	}
	
	/**
	 * Returns the '.jar' file location (full path) 
	 * @return
	 */
	private String findJarFullPath() {
		File file = findJarFile();
		return file.toString();
	}

	/**
	 * Returns the '.jar' file 
	 * @return
	 */
	private File findJarFile() {
		URL url = ApplicationCLI.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			URI uri = url.toURI();
			return new File( uri.getPath() ) ;
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot get jar location (URISyntaxException)", e);
		}		
	}

	/**
	 * Tries to find a specific editor command defined in the configuration file
	 * @return the specific command or null if nout found
	 */
	private String findSpecificEditorCommand() {
		String cmd = null ;
		File configFile = findConfigFile();
		if ( configFile != null ) {
			PropertiesManager pm = new PropertiesManager(configFile);
			Properties p = pm.load();
			cmd = p.getProperty(EDITOR_COMMAND);
		}
		return cmd ;
	}

	/**
	 * Tries to find a configuration file 
	 * @return the file or null if not found
	 */
	private File findConfigFile() {
		File file = findJarFile();
		if ( file.exists() ) {
			File parent = file.getParentFile();
			String folderPath = parent.toString();
			File cfgFile = new File( FileUtil.buildFilePath(folderPath, TELOSYS_CLI_CFG) ) ;
			if ( cfgFile.exists() && cfgFile.isFile() ) {
				return cfgFile ;
			}
		}
		return null ;
	}

	/**
	 * Determine the suitable command according with the current OS
	 * @return
	 */
	private String determineDefaultEditorCommand() {
		String osName = System.getProperty("os.name");
		if ( osName.contains("windows") || osName.contains("Windows") ) {
			// Windows
			return "notepad.exe $FILE" ;
		}
		else if ( osName.contains("mac") || osName.contains("Mac") ) {
			// Mac OS
			return "open -t $FILE" ;
		}
		else {
			// Other => Linux
			return "vi $FILE" ;
		}
	}

	//---------------------------------------------------------------------------------
	public String getJarLocation() {
		return jarLocation;
	}

	//---------------------------------------------------------------------------------
	public String getOperatingSystem() {
		return osName;
	}

	//---------------------------------------------------------------------------------
	public String getEditorCommand() {
		return editorCommand;
	}

	//---------------------------------------------------------------------------------
	public String getOriginalDirectory() {
		return originalDirectory;
	}

	//---------------------------------------------------------------------------------
	public CommandProvider getCommandProvider() {
		return commandProvider;
	}
	//---------------------------------------------------------------------------------
	public CommandsGroups getCommandsGroups() {
		return commandsGroups;
	}


	//---------------------------------------------------------------------------------
	// HOME directory
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current "HOME" directory or null if not defined
	 * @return
	 */
	public String getHomeDirectory() {
		return homeDirectory;
	}

	/**
	 * Set the "HOME" directory with the given directory
	 * @param homeDirectory
	 */
	public void setHomeDirectory(String directory) {
		this.homeDirectory = directory;
	}

	/**
	 * Set the "HOME" directory with the current directory
	 */
	public void setHomeDirectory() {
		this.homeDirectory = this.currentDirectory ;
	}

	//---------------------------------------------------------------------------------
	// Current directory
	//---------------------------------------------------------------------------------
	public String getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(String directory) {
		this.currentDirectory = directory;
	}
	
	public void resetCurrentDirectoryToHomeIfDefined() {
		if ( this.homeDirectory != null ) {
			this.currentDirectory = this.homeDirectory;
		}
		// else unchanged
	}

	//---------------------------------------------------------------------------------
	// Current GITHUB STORE
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current model name
	 * @return
	 */
	public String getCurrentGitHubStore() {
		return currentGitHubStore;
	}

	/**
	 * Set the current model name
	 * @param github
	 */
	public void setCurrentGitHubStore(String github) {
		this.currentGitHubStore = github;
	}

	//---------------------------------------------------------------------------------
	// Current MODEL
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current model name
	 * @return
	 */
	public String getCurrentModel() {
		return currentModel;
	}

	/**
	 * Set the current model name
	 * @param modelName
	 */
	public void setCurrentModel(String modelName) {
		this.currentModel = modelName;
	}

	//---------------------------------------------------------------------------------
	// Current BUNDLE
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current bundle name
	 * @return
	 */
	public String getCurrentBundle() {
		return currentBundle ;
	}

	/**
	 * Set the current bundle name
	 * @param bundleName
	 */
	public void setCurrentBundle(String bundleName) {
		this.currentBundle = bundleName;
	}

	
}
