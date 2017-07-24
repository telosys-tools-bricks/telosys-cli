package org.telosys.tools.cli;


public class Environment {
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private final CommandProvider commandProvider ;
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
		this.originalDirectory = System.getProperty("user.dir"); 
		this.currentDirectory = originalDirectory ;
		this.homeDirectory    = null ;
		this.currentModel     = null ;
		this.currentBundle    = null ;
		osName = System.getProperty("os.name");
		if ( osName.contains("windows") || osName.contains("Windows") ) {
			editorCommand = "notepad.exe" ;
		}
		else {
			editorCommand = "vi" ;
		}
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
