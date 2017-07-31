package org.telosys.tools.cli;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;

public abstract class Command {

	private final ConsoleReader consoleReader ;
	private final PrintWriter out ;
	private final Environment environment ;
	
	public Command(ConsoleReader consoleReader, Environment environment ) {
		super();
		//this.out = out;
		this.consoleReader = consoleReader ;
		this.out = new PrintWriter(consoleReader.getOutput()) ;
		this.environment = environment ;
	}
	
	public abstract String getName();
	
	public abstract String getShortDescription();
	
	public abstract String getDescription();
	
//	public abstract String execute(Environment environment, String[] args);
	public abstract String execute(String[] args);
	
	protected Environment getEnvironment() {
		return environment ;
	}

	protected void appendLine(StringBuffer sb, String s) {
		sb.append(s);
		sb.append(Environment.LINE_SEPARATOR);
	}
	
	protected void appendEndOfLine(StringBuffer sb) {
		sb.append(Environment.LINE_SEPARATOR);
	}

//	protected String getLastErrorMessage() {
//		return lastErrorMessage;
//	}

	protected String invalidUsage(String message) {
		return "Invalid usage : " + message ;
	}

	protected void print(String message) {
		out.println(message);
		out.flush();
	}
	protected void printError(String message) {
		out.println("[ERROR] : " + message );
		out.flush();
	}
	protected void printError(Exception ex) {
		LastError.setError(ex);
		out.println("[ERROR] Exception class   : " + ex.getClass().getSimpleName() );
		out.println("[ERROR] Exception message : " + ex.getMessage() );
		out.flush();
	}
	protected void printDebug(String message) {
		if ( Trace.DEBUG ) {
			out.println("[DEBUG] "+message);
			out.flush();
		}
	}

	protected String getCurrentDirectory() {
		return environment.getCurrentDirectory();
	}

	//-------------------------------------------------------------------------
	// Home directory 
	//-------------------------------------------------------------------------
	protected boolean checkHomeDirectoryDefined() {
		if ( environment.getHomeDirectory() != null ) {
			return true ;
		}
		else {
			print( "Home directory must be set before using this command!" ) ;
			return false ;
		}
	}	

	protected void setCurrentHome() {
		environment.setHomeDirectory();
		updatePrompt();
	}
	
	/**
	 * @param directory
	 */
	protected void setCurrentHome(String directory) {
		environment.setHomeDirectory(directory);
		updatePrompt();
	}
	
	/**
	 * Returns the current home directory
	 * @return
	 */
	protected String getCurrentHome() {
		return environment.getHomeDirectory();
	}
	
//	/**
//	 * Returns the current home directory
//	 * @return
//	 */
//	protected String getHomeDirectory() {
//		return environment.getHomeDirectory();
//	}

	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// Model
	//-------------------------------------------------------------------------
	protected boolean checkModelDefined() {
		if ( environment.getCurrentModel() != null ) {
			return true ;
		}
		else {
			print( "Model-name must be set before using this command!" ) ;
			return false ;
		}
	}	

	/**
	 * Set the current model name in the current environment
	 * @param modelName
	 */
	protected void setCurrentModel(String modelName) {
		environment.setCurrentModel(modelName);
		updatePrompt();
	}
	
	/**
	 * Returns the current model in the environment
	 */
	protected String getCurrentModel() {
		return environment.getCurrentModel();
	}
	
	//-------------------------------------------------------------------------
	// GitHub store
	//-------------------------------------------------------------------------
	protected boolean checkGitHubStoreDefined() {
		if ( environment.getCurrentGitHubStore() != null ) {
			return true ;
		}
		else {
			print( "GitHub store must be set before using this command!" ) ;
			return false ;
		}
	}	
	
	protected void setCurrentGitHubStore(String storeName) {
		environment.setCurrentGitHubStore(storeName);
	}

	protected String getCurrentGitHubStore() {
		return environment.getCurrentGitHubStore();
	}
	
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------
	
	protected void updatePrompt() {
		String prompt = Const.PROMPT_TEXT ;
		if ( environment.getHomeDirectory() != null ) {
			prompt = prompt + "#" ;
		}
		if ( environment.getCurrentModel() != null ) {
			prompt = prompt + "(" + environment.getCurrentModel() + ")" ;
		}
		prompt = prompt + Const.PROMPT_CHAR ;
		consoleReader.setPrompt(Color.colorize(prompt, Const.PROMPT_COLOR));
	}
	
	protected String launchEditor(String fileFullPath) {
		String editorCommand = environment.getEditorCommand();
		String fullCommand = editorCommand ;
		if ( ! StrUtil.nullOrVoid(fileFullPath) ) {
			fullCommand = editorCommand + " " + fileFullPath;
		}
		launchSystemCommand(fullCommand);
		return fullCommand ;
	}
	
	protected void launchSystemCommand(String fullCommand) {
		try {
			Runtime.getRuntime().exec(fullCommand);
		} catch (IOException e) {
			print( "ERROR : IOException : " + e.getMessage() );
		}
	}

	/**
	 * Returns the current Telosys project, or null if HOME is not defined
	 * @param environment
	 * @return
	 */
	protected TelosysProject getTelosysProject() {
		if ( checkHomeDirectoryDefined() ) {
			String projectFullPath = environment.getHomeDirectory();
			TelosysProject telosysProject = new TelosysProject(projectFullPath);
			return telosysProject ;
		}
		return null ;
	}
	
	/**
	 * Returns the file full path for 'telosys-tools.cfg' if the file exists, else returns null
	 * @return
	 */
	protected String getTelosysToolsCfgFullPath() {
		return getFileFullPathIfExists(Const.TELOSYS_TOOLS_CFG, Const.TELOSYS_TOOLS_FOLDER);
	}

	/**
	 * Returns the file full path for 'databases.dbcfg' if the file exists, else returns null
	 * @return
	 */
	protected String getTelosysDbCfgFullPath() {
		return getFileFullPathIfExists(Const.DATABASES_DBCFG, Const.TELOSYS_TOOLS_FOLDER);
	}
	
//	/**
//	 * Returns the File corresponding to the given model name ( .model / .dbrep / .dbmodel file )
//	 * @param modelName
//	 * @return
//	 * @throws TelosysToolsException if more than 1 file is found for the given name
//	 */
//	protected File getModelFile(String modelName) throws TelosysToolsException {
//		File file1 = getFileInHomeDir( modelName + ApiUtil.MODEL_SUFFIX,   Const.TELOSYS_TOOLS_FOLDER) ;
//		File file2 = getFileInHomeDir( modelName + ApiUtil.DBREP_SUFFIX,   Const.TELOSYS_TOOLS_FOLDER) ;
//		File file3 = getFileInHomeDir( modelName + ApiUtil.DBMODEL_SUFFIX, Const.TELOSYS_TOOLS_FOLDER) ;
//		int n = 0 ;
//		File file = null ;
//		if ( file1.exists() ) {
//			file = file1 ;
//			n++ ;
//		}
//		if ( file2.exists() ) {
//			file = file2 ;
//			n++ ;
//		}
//		if ( file3.exists() ) {
//			file = file3 ;
//			n++ ;
//		}
//		if ( n == 0 ) {
//			// no model (not found)
//			return null ;
//		}
//		else if ( n == 1 ) {
//			// unique model found 
//			return file ;
//		}
//		else {
//			// more than one !
//			throw new TelosysToolsException("Model name is not unique");
//		}
//	}
	
	/**
	 * Returns the file full path or null is the file doesn't exist
	 * @param fileName
	 * @param subDirectory
	 * @return
	 */
	private String getFileFullPathIfExists(String fileName, String subDirectory) {

//		// Try to get the file in the 'home' directory
//		String shortPath = fileName; // e.g. 'telosys-tools.cfg' or 'databases.dbcfg'
//		if (subDirectory != null) {
//			shortPath = FileUtil.buildFilePath(subDirectory, fileName); 
//			// e.g. 'TelosysTools/databases.dbcfg'
//		}
//		String fileFullPath = FileUtil.buildFilePath(environment.getHomeDirectory(), shortPath);
//		File file = new File(fileFullPath);
		
		File file = getFileInHomeDir(fileName, subDirectory);
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		// Not found
		return null;
	}

	private File getFileInHomeDir(String fileName, String subDirectory) {

		// Try to get the file in the 'home' directory
		String shortPath = fileName; // e.g. 'telosys-tools.cfg' or 'databases.dbcfg'
		if (subDirectory != null) {
			shortPath = FileUtil.buildFilePath(subDirectory, fileName); 
			// e.g. 'TelosysTools/databases.dbcfg'
		}
		String fileFullPath = FileUtil.buildFilePath(environment.getHomeDirectory(), shortPath);
		return new File(fileFullPath);
	}

	/**
	 * Returns the string as is if not null, or "(undefined)" if null 
	 * @param s
	 * @return
	 */
	protected String undefinedIfNull(String s) {
		if ( s != null ) {
			return s ;
		}
		else {
			return "(undefined)";
		}
	}	

}   
