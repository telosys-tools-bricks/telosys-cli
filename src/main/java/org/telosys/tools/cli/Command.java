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
package org.telosys.tools.cli;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.commands.git.GitUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.TargetsDefinitions;
import org.telosys.tools.editor.TextEditorManager;

import jline.console.ConsoleReader;

/**
 * Command abstract class
 * 
 * @author Laurent GUERIN
 *
 */
public abstract class Command {

	protected static final String YES_OPTION = "-y" ;

	private final ConsoleReader consoleReader ;
	private final PrintWriter out ;
	private final Environment environment ;
	
	private boolean sayYes ; // Flag to say 'yes' if '-y' option is set
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	protected Command(ConsoleReader consoleReader, Environment environment ) {
		super();
		this.consoleReader = consoleReader ;
		this.out = new PrintWriter(consoleReader.getOutput()) ;
		this.environment = environment ;
	}
	
	//----------------------------------------------------------------
	/**
	 * Returns the command name 
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * Returns a short description
	 * @return
	 */
	public abstract String getShortDescription();
	
	/**
	 * Returns the command description
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * Returns the usage 
	 * @return
	 */
	public abstract String getUsage();
	
	/**
	 * Executes the command with the given arguments
	 * @param args
	 * @return
	 */
	public abstract String execute(String[] args);
	
	//----------------------------------------------------------------
	protected Environment getEnvironment() {
		return environment ;
	}

	protected void appendLine(StringBuilder sb, String s) {
		sb.append(s);
		sb.append(Environment.LINE_SEPARATOR);
	}
	
	protected void appendEndOfLine(StringBuilder sb) {
		sb.append(Environment.LINE_SEPARATOR);
	}

	protected String invalidUsage(String message) {
		return "Invalid usage : " + message ;
	}

	protected int readChar() {
		try {
			return consoleReader.readCharacter();
		} catch (IOException e) {
			printError(e);
			throw new CancelCommandException("IOException : readCharacter()");
		}
	}
	protected void backspace() {
		try {
			consoleReader.backspace();
		} catch (IOException e) {
			printError(e);
			throw new CancelCommandException("IOException : backspace()");
		}
	}
	
	/**
	 * Read user response 
	 * @return
	 */
	protected String readResponse() {
		return readString(true);
	}
	
	/**
	 * Read user response in 'secret' mode
	 * @return
	 */
	protected String readSecret(String message) {
		if ( message != null ) {
			out.print( message );
			out.flush();			
		}
		return readString(false);
	}
	
	private String readString(boolean showInputChar) {
		StringBuilder sb = new StringBuilder();
		while ( true ) {
			char c = (char) readChar();
			if ( c >= ' ' ) {
				// Standard character --> keep it in the buffer
				sb.append(c);
				if ( showInputChar ) {
					out.print(c);
				}
				else {
					out.print('*');
				}
				out.flush();
			}
			else {
				switch(c) {
				// CR or LF : end of input --> return all chars as string
				case '\n' :
				case '\r' :
					out.println("");
					out.flush();
					return sb.toString();

				// BACKSPACE
				case '\b' :
					backspace();
					if ( sb.length() > 0 ) {
						sb.setLength(sb.length() - 1); // back (remove last char)
						out.print('\b');
						out.print(' ');
						out.print('\b');
						out.flush();
					}
					break ;
				}
			}			
		}
	}
	
	protected boolean confirm(String message) {
		if ( sayYes ) {
			return true ;
		}
		else {
			out.print( message + " [y/n] ? " );
			out.flush();
			String r = readResponse() ;
			return "Y".equalsIgnoreCase(r) ;
		}
	}

	protected void print(String message) {
		out.println(message);
		out.flush();
	}
	protected void printError(String message) {
		out.println("[ERROR] " + message );
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

	/**
	 * Prints the given list of strings with a ' . ' at the beginning 
	 * @param strings
	 * @return
	 */
	protected void printList(List<String> strings) {
		printList(strings, " . " );
	}
	
	/**
	 * Prints the given list of strings with a ' . ' at the beginning 
	 * @param strings
	 * @return
	 */
	protected void printList(List<String> strings, String startOfLine) {
		if ( strings != null ) {
			for ( String s : strings ) {
				out.println(startOfLine + s);
			}
		}
		out.flush();
	}	

	/**
	 * Prints the given list of files with a ' . ' at the beginning 
	 * @param files
	 * @return
	 */
	protected void printListOfFiles(List<File> files) {
		if ( files != null ) {
			for ( File file : files ) {
				String info = GitUtil.isGitRepository(file) ? " (GIT)" : "";
				out.println(" . " + file.getName() + info);
			}
		}
		out.flush();
	}	

	protected String getCurrentDirectory() {
		return environment.getCurrentDirectory();
	}

	//-------------------------------------------------------------------------
	// Home directory 
	//-------------------------------------------------------------------------
	protected boolean checkDirectory(String dir) {
		if (dir == null) {
			print("Directory is null !");
			return false;
		}
		return checkDirectory(new File(dir) );
	}
	
	protected boolean checkDirectory(File file) {
		if (file == null) {
			print("Directory is null !");
			return false;
		}
		if ( ! file.exists() ) {
			print("'" + file.getAbsolutePath() + "' doesn't exist !");
			return false;
		}
		if ( ! file.isDirectory()) {
			print( "'" + file.getAbsolutePath() + "' is not a directory !" );
			return false;
		} 
		return true ; // OK
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
		// Propagation to the Text Editor if any
		TextEditorManager.setHomeDirectory(environment.getHomeDirectory() );
	}
	
	/**
	 * @param directory
	 */
	protected void setCurrentHome(String directory) {
		environment.setHomeDirectory(directory);
		updatePrompt();
		// Propagation to the Text Editor if any
		TextEditorManager.setHomeDirectory(environment.getHomeDirectory() );
	}
	
	/**
	 * Returns the current home directory
	 * @return
	 */
	protected String getCurrentHome() {
		return environment.getHomeDirectory();
	}
	
	//-------------------------------------------------------------------------
	/**
	 * Builds a list containing the command arguments (index 1 to LAST)
	 * @param args
	 * @return
	 */
	protected List<String> getArgumentsAsList(String[] args) {
//		List<String> list = new LinkedList<>();
//		if ( args != null ) {
//			for ( int i = 1 ; i < args.length ; i++ ) {
//				list.add(args[i]);
//			}
//		}
//		return list;
	    if (args == null || args.length <= 1) {
	        return Collections.emptyList();
	    }
	    return Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
	}
	
	/**
	 * Check the number of arguments
	 * @param commandArguments 
	 * @param acceptableNumbers list of acceptable number of arguments without the command itself (without args[0])
	 * @return
	 */
	protected boolean checkArguments(List<String> commandArguments, int ... acceptableNumbers) {
		for ( int i : acceptableNumbers ) {
			if ( commandArguments.size() == i ) { 
				// args count is one of acceptable numbers
				return true;
			}
		}
		print("Invalid usage : unexpected number of arguments");
		return false;
	}

	/**
	 * Check validity of options (for all args starting with '-') 
	 * @param commandArguments 
	 * @param options list of acceptable options
	 * @return
	 */
	protected boolean checkOptions(List<String> commandArguments, Collection<String> options) {
		boolean status = true ;
		for ( String arg : commandArguments ) {
			if ( arg.startsWith("-") && ( ! isValidOption(arg, options) ) ) {
				print("Invalid option '" + arg + "'");
				status = false ;
			}
		}
		return status;
	}
	
	/**
	 * Returns true if the given option is present in the given list of options
	 * @param option
	 * @param optionsList
	 * @return
	 */
	protected boolean isOptionActive(String option, Collection<String> optionsList) {
		for ( String s : optionsList ) {
			if ( s.equals(option) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a Set containing all options in the given command arguments (each arg starting by "-")
	 * @param commandArguments
	 * @return
	 */
	protected Set<String> getOptions(Collection<String> commandArguments) {
		Set<String> activeOptions = new HashSet<>() ;
		for ( String arg : commandArguments ) {
			if ( arg.startsWith("-") ) {
				activeOptions.add(arg);
			}
		}
		return activeOptions;
	}
	
	/**
	 * Returns a new list of arguments without all options 
	 * @param commandArguments
	 * @return
	 */
	protected List<String> removeOptions(Collection<String> commandArguments) {
		List<String> argsWithoutOptions = new LinkedList<>() ;
		for ( String arg : commandArguments ) {
			if ( ! arg.startsWith("-") ) {
				argsWithoutOptions.add(arg);
			}
		}
		return argsWithoutOptions;
	}
	
	private boolean isValidOption(String arg, Collection<String> options) {
		for ( String o : options ) {
			if ( arg.equals(o) ) {
				return true ;
			}
		}
		return false ;
	}
	
	protected void sayYes(boolean value) {
		sayYes = value ;
	}
	
	protected void registerYesOptionIfAny(Collection<String> arguments) {
		sayYes(false); // Reset "yes" option 
		for ( String a : arguments ) {
			if ( a.equals(YES_OPTION) ) {
				sayYes(true); // Set "yes" option
			}
		}
	}
	
	//-------------------------------------------------------------------------
	// Model
	//-------------------------------------------------------------------------
	/**
	 * Checks if current model is defined
	 * @return
	 */
	protected boolean checkModelDefined() {
		if ( environment.getCurrentModel() != null ) {
			return true ;
		}
		else {
			print( "No current model (can be set with 'm' command)" ) ;
			return false ;
		}
	}	

	protected void setCurrentModel(String modelName) {
		if ( getTelosysProject().modelFolderExists(modelName) ) {
			environment.setCurrentModel(modelName);
			updatePrompt();
		}
		else {
			printError("Model '" + modelName + "' does not exist");
		}
	}
	
	/**
	 * Returns true if the given model name is the current model 
	 * @param modelName
	 * @return
	 */
	protected boolean isCurrentModel(String modelName) {
		if ( modelName != null ) {
			return modelName.equals(environment.getCurrentModel() ) ;
		}
		return false ;
	}
	
	/**
	 * Unset the current model in the current environment
	 */
	protected void unsetCurrentModel() {
		environment.setCurrentModel(null);
		updatePrompt();
	}
	
	/**
	 * Returns the current model in the environment
	 * @return the current model (or null if none)
	 */
	protected String getCurrentModel() {
		return environment.getCurrentModel();
	}

	//-------------------------------------------------------------------------
	// Bundle
	//-------------------------------------------------------------------------
	protected boolean checkBundleDefined() {
		if ( environment.getCurrentBundle() != null ) {
			return true ;
		}
		else {
			print( "No current bundle (can be set with 'b' command)" ) ;
			return false ;
		}
	}	

	/**
	 * Set the current bundle name in the current environment
	 * @param bundleName
	 */
	protected void setCurrentBundle(String bundleName) {
		environment.setCurrentBundle(bundleName);
		updatePrompt();
	}

	/**
	 * Returns true if the given bundle name is the current bundle 
	 * @param bundleName
	 * @return
	 */
	protected boolean isCurrentBundle(String bundleName) {
		if ( bundleName != null ) {
			return bundleName.equals(environment.getCurrentBundle() ) ;
		}
		return false ;
	}
		
	/**
	 * Unset the current bundle name in the current environment
	 */
	protected void unsetCurrentBundle() {
		environment.setCurrentBundle(null);
		updatePrompt();
	}

	/**
	 * Returns the current bundle in the environment
	 */
	protected String getCurrentBundle() {
		return environment.getCurrentBundle();
	}

	/**
	 * Returns the TargetsDefinitions for the current bundle
	 * @return
	 */
	protected TargetsDefinitions getCurrentTargetsDefinitions() {
		TelosysProject telosysProject = getTelosysProject();
		try {
			return telosysProject.getTargetDefinitions( getCurrentBundle() );
		} catch (TelosysToolsException e) {
			printError(e);
			throw new CancelCommandException("Cannot get TargetsDefinitions");
		}
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
		if ( environment.getCurrentBundle() != null ) {
			prompt = prompt + "[" + environment.getCurrentBundle() + "]" ;
		}
		prompt = prompt + Const.PROMPT_CHAR ;
		consoleReader.setPrompt(Color.colorize(prompt, Const.PROMPT_COLOR));
	}
	
	/**
	 * Launches the external editor with the given file as argument 
	 * @param fileFullPath
	 * @return the full command line launched
	 */
	protected String launchEditor(String fileFullPath) {
		
		// Check if the editor command is defined
		String editorCommand = environment.getEditorCommand();
		if ( editorCommand == null ) {
			// No specific editor command defined => use the default embedded editor
			if ( StrUtil.nullOrVoid(fileFullPath) ) {
				// No file to edit
				TextEditorManager.openEditor();
				return "Embedded editor (no file to edit)";
			}
			else {
				TextEditorManager.editFile( new File(fileFullPath) ) ;
				return "Embedded editor : edit file '" + fileFullPath + "'";
			}
		}
		
		// Is there a file to be edited ?
		String fileToEdit = fileFullPath ;
		if ( StrUtil.nullOrVoid(fileToEdit) ) {
			fileToEdit = "" ; // for replacement in "xxx $FILE"
		}

		// Replace $FILE or ${FILE} if present
		String fullCommand ;
		if ( editorCommand.contains("$FILE") ) {
			fullCommand = StrUtil.replaceVar(editorCommand, "$FILE", fileToEdit);
		}
		else if ( editorCommand.contains("${FILE}") ) {
			fullCommand = StrUtil.replaceVar(editorCommand, "${FILE}", fileToEdit);
		}
		else {
			fullCommand = editorCommand + " " + fileToEdit;
		}
		// At this step the full comman is like "notepad foo.txt" 
		// or "/bin/sh /xx/xx/telosys-term.sh vi foo.txt"
		// or "specific-command foo.txt"

		// Run the OS command 
		SystemCommand.run(fullCommand, environment.getOperatingSystemType());
		return fullCommand ;
	}
	
	/**
	 * Returns the current Telosys project, or null if HOME is not defined
	 * @param environment
	 * @return
	 */
	protected TelosysProject getTelosysProject() {
		if ( checkHomeDirectoryDefined() ) {
			String projectFullPath = environment.getHomeDirectory();
			return new TelosysProject(projectFullPath);
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
	 * Returns the file full path for 'databases.yaml' if the file exists, else returns null
	 * @return
	 */
	protected String getDatabasesFileFullPath() {
		return getFileFullPathIfExists(Const.DATABASES_YAML, Const.TELOSYS_TOOLS_FOLDER);
	}
	
	
	/**
	 * Returns the file full path or null is the file doesn't exist
	 * @param fileName
	 * @param subDirectory
	 * @return
	 */
	private String getFileFullPathIfExists(String fileName, String subDirectory) {
		File file = getFileInHomeDir(fileName, subDirectory);
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		// Not found
		return null;
	}

	private File getFileInHomeDir(String fileName, String subDirectory) {

		// Try to get the file in the 'home' directory
		String shortPath = fileName; // e.g. 'telosys-tools.cfg' or 'databases.yaml'
		if (subDirectory != null) {
			shortPath = FileUtil.buildFilePath(subDirectory, fileName); 
			// e.g. 'TelosysTools/databases.yaml'
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
	
	/**
	 * Builds a list of criteria from the given command line arguments <br>
	 * taking all the arguments from 1 to N (except 0)
	 * @param commandArgs 
	 * @return
	 */
	protected List<String> buildCriteriaFromArgs( String[] commandArgs) {
//		List<String> criteria = new LinkedList<>();
//		for ( int i = 1 ; i < commandArgs.length ; i++ ) {
//				criteria.add(commandArgs[i]);
//		}
//		return criteria ;
	    if (commandArgs == null || commandArgs.length <= 1) {
	        return Collections.emptyList();
	    }
	    return Arrays.asList(Arrays.copyOfRange(commandArgs, 1, commandArgs.length)); // fixed-size list backed by the array.
	}

}   
