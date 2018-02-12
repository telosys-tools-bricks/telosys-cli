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

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.PropertiesManager;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;


public class Environment {
	
	private static final String TELOSYS_CLI_CFG      = "telosys-cli.cfg" ;
	private static final String TELOSYS_TERM_SH      = "telosys-term.sh" ;
	
	private static final String EDITOR_COMMAND       = "EditorCommand" ;
	private static final String DEFAULT_GITHUB_STORE = "telosys-templates-v3" ;
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	// unchangeable attributes
	private final CommandProvider commandProvider ;
	private final CommandsGroups  commandsGroups ;
	
	private final String jarFileFullPath ;
	private final String jarFolderFullPath ;
	private final String osName ;
	private final OSType osType ;
	private final String editorCommand ;
	private final String originalDirectory ;

	// alterable attributes
	private       String homeDirectory ;
	private       String currentDirectory ;
	private       String currentGitHubStore = DEFAULT_GITHUB_STORE ;
	private       String currentModel ;
	private       String currentBundle ;
	
	/**
	 * Constructor
	 * @param commandProvider
	 */
	public Environment(CommandProvider commandProvider) {
		super();
		
		// unchangeable attributes 
		this.commandProvider = commandProvider ;
		this.commandsGroups  = new CommandsGroups();
		this.jarFileFullPath   = findJarFileFullPath();
		this.jarFolderFullPath = findJarFolderFullPath();
		this.originalDirectory = System.getProperty("user.dir"); 
		this.osName = System.getProperty("os.name");
		this.osType = findOSType(this.osName); 
		this.editorCommand = findEditorCommand(this.osType);

		// alterable attributes
		this.currentDirectory   = originalDirectory ;
		this.homeDirectory      = null ;
		this.currentModel       = null ;
		this.currentBundle      = null ;
		this.currentGitHubStore = DEFAULT_GITHUB_STORE ;
	}
	
	/**
	 * Returns the editor command to be used <br>
	 * The specific command if defined in the '.cfg' file <br>
	 * or the default command for the current OS<br>
	 * @return
	 */
	private String findEditorCommand(OSType osType) {
		// Try to find a specific command (defined by the user)
		String specificEditorCommand = findSpecificEditorCommand();
		if ( specificEditorCommand != null ) {
			// A specific editor command is defined in ".cfg" file => use it
			return specificEditorCommand ;
		}
		else {
			// No specific editor command => get the default edito command
			return determineDefaultEditorCommand(osType);
		}
	}
	
	/**
	 * Returns the '.jar' file location (full path) 
	 * @return
	 */
	private String findJarFileFullPath() {
		File file = findJarFile();
		return file.toString();
	}

	/**
	 * Returns the '.jar' folder full path
	 * @return
	 */
	private String findJarFolderFullPath() {
		File file = findJarFile();
		if ( file.exists() ) {
			File parent = file.getParentFile();
			return parent.toString();
		}
		else {
			throw new RuntimeException("Cannot find jar folder (no jar file)");
		}
	}

	/**
	 * Returns the '.jar' file 
	 * @return
	 */
	private File findJarFile() {
		URL url = TelosysCLI.class.getProtectionDomain().getCodeSource().getLocation();
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
		File configFile = findTelosysCliConfigFile();
		if ( configFile != null ) {
			PropertiesManager pm = new PropertiesManager(configFile);
			Properties p = pm.load();
			cmd = p.getProperty(EDITOR_COMMAND);
		}
		return cmd ;
	}

	/**
	 * Tries to find the 'telosys-cli.cfg' configuration file <br>
	 * located in the same folder as the '.jar' file
	 * @return the file or null if not found
	 */
	private File findTelosysCliConfigFile() {
		return findFileInJarFolder(TELOSYS_CLI_CFG) ;
	}

	/**
	 * Tries to find the 'telosys-term.sh' shell file <br>
	 * located in the same folder as the '.jar' file
	 * @return the file or null if not found
	 */
	private File findTelosysTerminalShellFile() {
		return findFileInJarFolder(TELOSYS_TERM_SH) ;
	}

	private File findFileInJarFolder(String fileName) {
		if ( this.jarFolderFullPath != null ) {
			File file = new File( FileUtil.buildFilePath(this.jarFolderFullPath, fileName) ) ;
			if ( file.exists() && file.isFile() ) {
				return file ;
			}
		}
		return null ;
	}

	private OSType findOSType(String osName) {
		if ( osName.contains("windows") || osName.contains("Windows") ) {
			return OSType.WINDOWS ;
		}
		else if ( osName.contains("mac") || osName.contains("Mac") ) {
			return OSType.MACOS ;
		}
		else {
			// Other => Linux
			return OSType.LINUX ;
		}
	}
	
	/**
	 * Determine the suitable command according with the current OS
	 * @return
	 */
	private String determineDefaultEditorCommand(OSType osType) {
		
		switch(osType) {
		case WINDOWS :
			return "notepad.exe $FILE" ;
		case MACOS :
			return "open -t $FILE" ;
		case LINUX :
			return getDefaultEditorCommandForLinux() ;
		case UNKNOWN :
		default :
			return "ERROR-unknown-operating-system" ;
		}
	}
	
	/**
	 * Return the command to open 'vi' in a new terminal <br>
	 * The shell 'telosys-term.sh' is used to open a new terminal using the adaquate command
	 * @return
	 */
	private String getDefaultEditorCommandForLinux() {
		// If Linux use the shell script to open a new terminal
		File shFile = findTelosysTerminalShellFile();
		if ( shFile != null ) {
			return "/bin/sh " + shFile.getAbsolutePath() + " vi $FILE" ;
		}
		else {
			return "ERROR-shell-file-not-found-(" + TELOSYS_TERM_SH + ")";
		}
	}
	
//	/**
//	 * Customize the given command if necessary in order to open a new terminal/window
//	 * when the command will be launched
//	 * @param fullCommand
//	 * @return
//	 */
//	protected String customizeSystemCommandIfNecessary(String fullCommand) {
//		// If Linux use the shell script to open a new terminal
//		if ( this.osType == OSType.LINUX ) {
//			File shFile = findTelosysTerminalShellFile();
//			if ( shFile != null ) {
//				return "/bin/sh " + shFile.getAbsolutePath() + " " + fullCommand ;
//			}
//		}
//		return fullCommand;
//	}

	//---------------------------------------------------------------------------------
	public String getJarLocation() {
		return jarFileFullPath;
	}

	//---------------------------------------------------------------------------------
	public String getOperatingSystemName() {
		return osName;
	}

	//---------------------------------------------------------------------------------
	public OSType getOperatingSystemType() {
		return osType;
	}
	//---------------------------------------------------------------------------------
	public String getJavaVersion() {
		// property "java.version" doesn't return the buikd ( eg : "1.6.0_45" )
		return System.getProperty("java.runtime.version"); // eg : 1.6.0_45-b06
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
	 * Set the "HOME" directory with the current directory
	 */
	public void setHomeDirectory() {
		setHomeDirectory(this.currentDirectory);
	}

	/**
	 * Set the "HOME" directory with the given directory
	 * @param homeDirectory
	 */
	public void setHomeDirectory(String directory) {
		this.homeDirectory = directory;
		// Reset the current environment according with the new HOME
		resetCurrentEnvironment() ; 
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
	 * Returns the default GitHub store name ( eg "telosys-templates-v3" )
	 * @return
	 */
	public String getDefaultGitHubStore() {
		return DEFAULT_GITHUB_STORE ;
	}

	/**
	 * Returns the current GitHub store name
	 * @return
	 */
	public String getCurrentGitHubStore() {
		return currentGitHubStore;
	}

	/**
	 * Set the current GitHub store name
	 * @param github
	 */
	public void setCurrentGitHubStore(String github) {
		this.currentGitHubStore = github;
		saveCurrentEnvironment();
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
		saveCurrentEnvironment();
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
		saveCurrentEnvironment();
	}

	//---------------------------------------------------------------------------------
	// Environment persistence
	//---------------------------------------------------------------------------------
	private static final String ENV_FILE_NAME  = "telosys.env" ;
	private static final String MODEL  = "model" ;
	private static final String BUNDLE = "bundle" ;
	private static final String GITHUBSTORE = "githubstore" ;
	
	/**
	 * Returns the current environment properties file <br>
	 * e.g. HOME/TelosysTools/telosys.env
	 * @return a File instance or null if HOME is not defined 
	 */
	private File getEnvironmentPropertiesFile() {
		if ( homeDirectory != null ) {
			String dir = null ;
			try {
				TelosysToolsCfg telosysToolsCfg = (new TelosysProject(homeDirectory)).getTelosysToolsCfg();
				dir = telosysToolsCfg.getTelosysToolsFolderAbsolutePath();
			} catch (TelosysToolsException e) {
				// Cannot get Telosys configuration
				return null ;
			}
			// Check 'TelosysTools' directory existence ( if not yet init => no TelosysTools directory )
			File telosysToolsDir = new File(dir);
			if ( telosysToolsDir.exists() && telosysToolsDir.isDirectory() ) {
				// Build the file
				return new File( FileUtil.buildFilePath(dir, ENV_FILE_NAME) );
			}
		}
		return null ;
	}
	
	/**
	 * Saves the current environment in a file
	 */
	private void saveCurrentEnvironment() {
		File file = getEnvironmentPropertiesFile() ;
		if ( file != null ) {
			Properties properties = new Properties();
			putIfNotNull(properties, MODEL, this.currentModel);
			putIfNotNull(properties, BUNDLE, this.currentBundle);
			putIfNotNull(properties, GITHUBSTORE, this.currentGitHubStore);
			
			PropertiesManager pm = new PropertiesManager(file);
			pm.save(properties);
		}
	}
	
	private void putIfNotNull(Properties properties, String key, String value ) {
		if ( value != null ) {
			properties.put(key, value);
		}
	}
	
	private void resetCurrentEnvironment() {
		// Reset all the default values 
		this.currentModel = null ;
		this.currentBundle = null ;
		this.currentGitHubStore = DEFAULT_GITHUB_STORE ;
		// Try to restore if possible 
		restoreCurrentEnvironment();
	}
	
	/**
	 * Restores the environment from a file if any
	 */
	private void restoreCurrentEnvironment() {
		File file = getEnvironmentPropertiesFile() ;
		if ( file != null && file.exists() && file.isFile() ) {
			
			PropertiesManager pm = new PropertiesManager(file);
			Properties properties = pm.load();
			
			this.currentModel = properties.getProperty(MODEL); // null if not found
			this.currentBundle = properties.getProperty(BUNDLE); // null if not found
			this.currentGitHubStore = properties.getProperty(GITHUBSTORE); // null if not found
		}
	}
}
