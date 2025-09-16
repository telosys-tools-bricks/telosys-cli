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

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.LastError;
import org.telosys.tools.cli.commands.commons.DepotContent;
import org.telosys.tools.cli.commands.git.GitAdd;
import org.telosys.tools.cli.commands.git.GitClone;
import org.telosys.tools.cli.commands.git.GitCommit;
import org.telosys.tools.cli.commands.git.GitInit;
import org.telosys.tools.cli.commands.git.GitPush;
import org.telosys.tools.cli.commands.git.GitRemote;
import org.telosys.tools.cli.commands.git.GitStatus;
import org.telosys.tools.cli.commands.git.GitUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.Depot;
import org.telosys.tools.commons.depot.DepotResponse;
import org.telosys.tools.commons.exception.TelosysRuntimeException;

import jline.console.ConsoleReader;

/**
 * 'git' command
 * 
 * @author Laurent GUERIN
 *
 */
public class GitCommand extends CommandLevel2 {
	
	public static final String GIT = "git";
	public static final String CLONE_ARG_EXPECTED = "argument expected (repo_url or bundle/model name)";
	
	private static final String CLONEM = "clonem" ;
	private static final String CLONEB = "cloneb" ;
	private static final Set<String> CLONE_COMMANDS  = new HashSet<>(Arrays.asList(CLONEM,  CLONEB  )); 
	
	private static final String INITM = "initm" ;
	private static final String INITB = "initb" ;
	private static final Set<String> INIT_COMMANDS   = new HashSet<>(Arrays.asList(INITM,   INITB   )); 
	
	private static final String REMOTEM = "remotem" ;
	private static final String REMOTEB = "remoteb" ;
	private static final Set<String> REMOTE_COMMANDS = new HashSet<>(Arrays.asList(REMOTEM, REMOTEB )); 
	
	private static final String STATUSM = "statusm" ;
	private static final String STATUSB = "statusb" ;
	private static final Set<String> STATUS_COMMANDS = new HashSet<>(Arrays.asList(STATUSM, STATUSB )); 
	
	public static final String DEPOT = "depot";
	
    private enum ArgType {
        MODEL,
        BUNDLE
    }

	/**
	 * Constructor
	 * @param out
	 */
	public GitCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return GIT;
	}

	@Override
	public String getShortDescription() {
		return "Git commands " ;
	}

	@Override
	public String getDescription() {
		return "Git commands for managing model and bundle repositories ";
	}
	
	@Override
	public String getUsage() {
		final String EOL = "\n  ";
		return "Git clone model or bundle and add 'depot' remote " + EOL
			 + " " + GIT + " clonem  model-name-in-depot  |or| any-repo-url  (clone a model)" + EOL
			 + " " + GIT + " cloneb  bundle-name-in-depot |or| any-repo-url  (clone a bundle)" + EOL
			 + "Git init model or bundle and add 'depot' remote " + EOL
			 + " " + GIT + " initm  [model-name]  (init a model, current model by default) " + EOL
			 + " " + GIT + " initb  [bundle-name] (init a bundle, current bundle by default)" + EOL
			 + "Git remote (print remotes) " + EOL
			 + " " + GIT + " remotem  [model-name]  (model remotes, current model by default) " + EOL
			 + " " + GIT + " remoteb  [bundle-name] (bundle remotes, current bundle by default)" + EOL
			 + "Git status (print status) " + EOL
			 + " " + GIT + " statusm  [model-name]  (model status, current model by default) " + EOL
			 + " " + GIT + " statusb  [bundle-name] (bundle status, current bundle by default)" + EOL
			 ;
	}
	
	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		
		if ( checkArguments(commandArguments, 1, 2 ) ) {
			List<String> argsWithoutOptions = removeOptions(commandArguments);
			if ( ! argsWithoutOptions.isEmpty() ) {
				String gitCommand = argsWithoutOptions.get(0);
				if (CLONE_COMMANDS.contains(gitCommand) ) {
					executeCloneCommand(gitCommand, argsWithoutOptions);
				}
				else if (INIT_COMMANDS.contains(gitCommand) ) { 
					executeInitCommand(gitCommand, argsWithoutOptions);
				}
				else if (REMOTE_COMMANDS.contains(gitCommand) ) { 
					executeRemoteCommand(gitCommand, argsWithoutOptions);
				}
				else if (STATUS_COMMANDS.contains(gitCommand) ) { 
					executeStatusCommand(gitCommand, argsWithoutOptions);
				}
				// hidden commands (just for test in current dir)
				else if ("remote".equals(gitCommand) ) { 
					executeRemoteCommand(getCurrentDirAsGitWorkingTree());
				}
				else if ("status".equals(gitCommand) ) { 
					executeStatusCommand(getCurrentDirAsGitWorkingTree());
				}
				else if ("add".equals(gitCommand) ) { 
					executeAddCommand(getCurrentDirAsGitWorkingTree());
				}
				else if ("commit".equals(gitCommand) ) { 
					executeCommitCommand(getCurrentDirAsGitWorkingTree());
				}
				else if ("push".equals(gitCommand) ) { 
					executePushCommand(getCurrentDirAsGitWorkingTree());
				}
				else {
					printInvalidGitCommand(gitCommand);
				}
			}
		}
		return null ;
	}

	private void printInvalidGitCommand(String gitCommand) {
		print("Invalid git command '" + gitCommand + "'");
	}
	
	private File getCurrentDirAsGitWorkingTree() {
		File directory = new File( getCurrentDirectory() ); 
		if ( GitUtil.isGitWorkingTree(directory) ) {
			return directory;
		}
		else {
			printError("Current directory is not a Git working tree");
			return null;
		}
	}

	private String getModelOrBundleArg(List<String> argsWithoutOptions) {
		String arg = null;
		if ( argsWithoutOptions.size() >= 2 ) { // (0)command[m/b] [ (1)arg ] 
			arg = argsWithoutOptions.get(1);
		}
		return arg;
	}
	private String getDefaultArgIfNone(String arg, ArgType argType) {
		if ( arg == null ) {
			if ( argType == ArgType.MODEL ) {
				// default is current model
				String currentModel = getCurrentModel();
				if ( currentModel == null ) {
					print("No current model");
				}
				return currentModel;
			}
			else if ( argType == ArgType.BUNDLE ) {
				// default is current bundle
				String currentBundle = getCurrentBundle();
				if ( currentBundle == null ) {
					print("No current bundle");
				}
				return currentBundle;
			}
			else {
				printError("Unexpected arument type");
				return null;
			}
		}
		else {
			return arg; // OK, not null
		}
	}
	
	private String getDepotDefinition(ArgType argType) {
		if ( argType == ArgType.MODEL ) {
			return getDepotDefinition(DepotContent.MODELS);
		}
		else if ( argType == ArgType.BUNDLE ) {
			return getDepotDefinition(DepotContent.BUNDLES);
		}
		else {
			throw new TelosysRuntimeException("Unexpected ArgType");
		}
	}
	private void executeCloneCommand(String gitCommand, List<String> argsWithoutOptions) {
		if ( argsWithoutOptions.size() >= 2 ) { // (0)clone[m/b] (1)arg 
			String arg = argsWithoutOptions.get(1);
			if ( CLONEM.equals(gitCommand) ) {
				tryToClone(arg, ArgType.MODEL);
			}
			else if ( CLONEB.equals(gitCommand) ) {
				tryToClone(arg, ArgType.BUNDLE);
			}
			else {
				printInvalidGitCommand(gitCommand); // not supposed to happen 
			}
		}
		else {
			print( CLONE_ARG_EXPECTED );
		}
	}

	private void executeInitCommand(String gitCommand, List<String> argsWithoutOptions) {
		String arg = null;
		if ( argsWithoutOptions.size() >= 2 ) { // (0)init[m/b] [ (1)arg ] 
			arg = argsWithoutOptions.get(1);
		}
		if ( INITM.equals(gitCommand) ) {
			tryToInit(arg, ArgType.MODEL);
		}
		else if ( INITB.equals(gitCommand) ) {
			tryToInit(arg, ArgType.BUNDLE);
		}
		else {
			printInvalidGitCommand(gitCommand); // not supposed to happen 
		}
	}

	private void executeRemoteCommand(String gitCommand, List<String> argsWithoutOptions) {
		if ( REMOTEM.equals(gitCommand) ) {
			tryToPrintRemotes( getModelOrBundleArg(argsWithoutOptions), ArgType.MODEL);
		}
		else if ( REMOTEB.equals(gitCommand) ) {
			tryToPrintRemotes( getModelOrBundleArg(argsWithoutOptions), ArgType.BUNDLE);
		}
		else {
			printInvalidGitCommand(gitCommand); // not supposed to happen 
		}
	}
	
	private void executeStatusCommand(String gitCommand, List<String> argsWithoutOptions) {
		if ( STATUSM.equals(gitCommand) ) {
			tryToPrintStatus( getModelOrBundleArg(argsWithoutOptions), ArgType.MODEL);
		}
		else if ( STATUSB.equals(gitCommand) ) {
			tryToPrintStatus( getModelOrBundleArg(argsWithoutOptions), ArgType.BUNDLE);
		}
		else {
			printInvalidGitCommand(gitCommand); // not supposed to happen 
		}
	}
	
	private void executeRemoteCommand(File workingTreeDirectory) {
		if ( workingTreeDirectory != null ) {
			try {
				print("git remotes for '" + workingTreeDirectory.getName() + "' ");
				List<String> remotes = GitRemote.getRemotes(workingTreeDirectory);
				if ( remotes.isEmpty() ) {
					print("No remote");
				}
				else {
					for ( String s : remotes ) {
						print(s);
					}
				}
			} catch (Exception e) {
				LastError.setError(e);
				printError(e);
			}
		}
	}

	private void executeStatusCommand(File workingTreeDirectory) {
		if ( workingTreeDirectory != null ) {
			try {
				print("git status for '" + workingTreeDirectory.getName() +"' ");
				List<String> report = GitStatus.getStatusReport(workingTreeDirectory);
				if ( report != null && !report.isEmpty() ) {
					for ( String s : report ) {
						print(s);
					}
				}
				else {
					printError("No status report ");
				}
			} catch (Exception e) {
				LastError.setError(e);
				printError(e);
			}
		}
	}

	private void executeAddCommand(File workingTreeDirectory) {
		if ( workingTreeDirectory != null ) {
			try {
				print("git add for '" + workingTreeDirectory.getName() + "' ");
				GitAdd.addAll(workingTreeDirectory);
				print("Add OK, all changes added in stage");
			} catch (Exception e) {
				LastError.setError(e);
				printError(e);
			}
		}
	}
	
	private void executeCommitCommand(File workingTreeDirectory) {
		if ( workingTreeDirectory != null ) {
			try {
				print("git commit for '" + workingTreeDirectory.getName() + "' ");
				String result = GitCommit.commit(workingTreeDirectory);
				if ( result != null && !result.trim().isEmpty() ) {
					print("Commit OK, id (sha) = " + result);
				}
				else {
					print("Nothing to commit (staged changes = 0)" );
				}
			} catch (Exception e) {
				LastError.setError(e);
				printError(e);
			}
		}
	}
	private void executePushCommand(File workingTreeDirectory) {
		if ( workingTreeDirectory != null ) {
			
			// TODO xxxxxxxxxxxxxxxxxxxx
			String username = "";
			String tokenOrPassword = "";
			// xxxxxxxxxxxxxxxxxxxx
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, tokenOrPassword );
			try {
				print("git push for '" + workingTreeDirectory.getName() +"' ");
				List<String> resultList = GitPush.pushAllBranches(workingTreeDirectory, DEPOT, credentialsProvider );
				if ( resultList != null && !resultList.isEmpty() ) {
					for ( String s : resultList ) {
						print(s);
					}
				}
				else {
					printError("Push done. No result. ");
				}
			} catch (Exception e) {
				LastError.setError(e);
				printError(e);
			}
		}
	}
	
	private void tryToPrintRemotes(String arg, ArgType argType) {
		String modelOrBundleName = getDefaultArgIfNone(arg, argType);
		if ( modelOrBundleName != null ) {
			try {
				File workingTreeDirectory = getWorkingTreeDirectory(modelOrBundleName, argType); 
				if ( workingTreeDirectory != null ) {
					executeRemoteCommand(workingTreeDirectory);				
				}
			} catch (Exception e) {
				LastError.setError(e);
				printError(e); 
			}
		}
	}

	private void tryToPrintStatus(String arg, ArgType argType) {
		String modelOrBundleName = getDefaultArgIfNone(arg, argType);
		if ( modelOrBundleName != null ) {
			try {
				File workingTreeDirectory = getWorkingTreeDirectory(modelOrBundleName, argType); 
				if ( workingTreeDirectory != null ) {
					executeStatusCommand(workingTreeDirectory);
				}
			} catch (Exception e) {
				LastError.setError(e);
				printError(e); 
			}
		}
	}

	private void tryToInit(String arg, ArgType argType) {
		String modelOrBundleName = getDefaultArgIfNone(arg, argType);
		if ( modelOrBundleName != null ) {
			try {
				File directory = getDirectory(modelOrBundleName, argType); 
				if ( directory != null ) {
					if ( GitUtil.isGitWorkingTree(directory) ) {
						print("'" + directory.getName() + "' is already a Git repository");
					}
					else {
						// Step #1 - Init
						if ( gitInit(directory) ) {
							// Step #2 - Add remote 'depot'
							gitAddRemoteDepot(directory, getDepotDefinition(argType));
						}
					}
				}
			} catch (Exception e) {
				LastError.setError(e);
				printError(e); 
			}
		}
	}
	private boolean gitInit(File directory) {
		try {
			print("Git init '" + directory.getName() + "' ...");
			GitInit.init(directory);
			print("Git repository successfully initialized." );
			return true;
		} catch (Exception e) { // All exceptions (including GitAPIException)
			printError("Cannot init '" + directory.getName() + "'");
			printError(e);
			return false;
		}
	}
	
	private File getWorkingTreeDirectory(String modelOrBundleName, ArgType argType) {
		File directory = getDirectory(modelOrBundleName, argType); 
		if ( directory != null ) {
			if ( GitUtil.isGitWorkingTree(directory) ) {
				return directory;
			}
			else {
				print("'" + modelOrBundleName + "' directory is not a git working tree");
			}
		}
		return null;		
	}
	
	/**
	 * Returns the directory for given model/bundle name
	 * @param modelOrBundleName  model/bundle name or 'null' for current model/bundle
	 * @param argType
	 * @return
	 */
	private File getDirectory(String modelOrBundleName, ArgType argType) {
		File dir = null;
		if ( argType == ArgType.MODEL) {
			if ( modelOrBundleName != null ) {
				dir = getTelosysProject().getModelFolder(modelOrBundleName);
			}
			else {
				dir = getCurrentModelFolder(); // if doesn't exist return null
				if (dir == null) print("No current model");
			}
		}
		else if ( argType == ArgType.BUNDLE) {
			if ( modelOrBundleName != null ) {
				dir = getTelosysProject().getBundleFolder(modelOrBundleName);
			}
			else {
				dir = getCurrentBundleFolder(); // if doesn't exist return null
				if (dir == null) print("No current bundle");
			}
		}
		if ( dir != null ) {
			if ( dir.exists() && dir.isDirectory() ) {
				return dir;
			}
			else {
				print("Invalid directory: " + dir.getAbsolutePath() ) ;
			}
		}
		return null;
	}
	
	private void tryToClone(String from, ArgType argType) {
		String depotDefinition = getDepotDefinition(argType);
		try {
			String url = getUsableURL(from, depotDefinition);
			if ( url != null ) {
				if ( argType == ArgType.MODEL) {
					cloneModelFromUrl( url, depotDefinition );
				}
				else if ( argType == ArgType.BUNDLE) {
					cloneBundleFromUrl( url, depotDefinition );
				}
			}
		} catch (Exception e) {
			LastError.setError(e);
			printError(e); 
		}
	}
	private String getUsableURL(String from, String depotDefinition) throws TelosysToolsException {
		if ( GitUtil.isGitUrl(from) ) {
			// Valid Git URL => usable as is => keep it
			return from; 
		}
		else {
			// just the repo-name => build URL for this repo in the given depot
			DepotResponse depotResponse = getTelosysProject().getElementsAvailableInDepot(depotDefinition);
			if ( depotResponse.contains(from) ) {
				// OK this repo exists in the depot
				Depot depot = new Depot(depotDefinition);
				return depot.buildGitRepositoryURL(from);
			}
			else {
				// this repo doesn't exist in the depot 
				print("Repository '" + from + "' not found in depot '" + depotDefinition + "'");
				return null ;
			}
		}
	}
	
	private void cloneModelFromUrl(String fromRepoUrl, String depotDefinition) {
		TelosysProject telosysProject = getTelosysProject();
		String modelName = getRepoNameFromUrl(fromRepoUrl);
		if ( ! telosysProject.modelFolderExists(modelName) ) {
			File modelFolder = telosysProject.getModelFolder(modelName);
			gitCloneAndAddRemote(fromRepoUrl, modelFolder, depotDefinition);
		}
		else {
			print("Model '" + modelName + "' already exists.");
		}
	}
	
	private void cloneBundleFromUrl(String fromRepoUrl, String depotDefinition) {
		TelosysProject telosysProject = getTelosysProject();
		String bundleName = getRepoNameFromUrl(fromRepoUrl);
		if ( ! telosysProject.bundleFolderExists(bundleName) ) {
			File bundleFolder = telosysProject.getBundleFolder(bundleName);
			gitCloneAndAddRemote(fromRepoUrl, bundleFolder, depotDefinition);
		}
		else {
			print("Bundle '" + bundleName + "' already exists.");
		}
	}
	
	public static String getRepoNameFromUrl(String repoUrl) {
	    if (repoUrl == null || repoUrl.isEmpty()) {
	        return null;
	    }
	    // Remove trailing slash if present
	    String cleanedUrl = repoUrl.endsWith("/") 
	            ? repoUrl.substring(0, repoUrl.length() - 1) 
	            : repoUrl;
	    // Get last path segment
	    String name = cleanedUrl.substring(cleanedUrl.lastIndexOf('/') + 1);
	    // Strip ".git" suffix if present
	    if (name.endsWith(".git")) {
	        name = name.substring(0, name.length() - 4);
	    }
	    return name;
	}

	
	private void gitCloneAndAddRemote(String fromRepoUrl, File localRepoDir, String depotDefinition) {
		// Step 1 - Clone
		if ( gitClone(fromRepoUrl, localRepoDir) ) {
			// Step 2 - Add remote
			gitAddRemoteDepot(localRepoDir, depotDefinition);
		}
	}
	private void gitAddRemoteDepot(File localRepoDir, String depotDefinition) {
		try {
			Depot depot = new Depot(depotDefinition);
			String localRepoName = localRepoDir.getName(); 
			String depotURL = depot.buildGitRepositoryURL(localRepoName);
			setGitRemoteDepot(localRepoDir, depotURL);
		} catch (TelosysToolsException e) {
			printError("Cannot add remote 'depot'");
			printError(e);
		}
	}
	private boolean gitClone(String fromRepoUrl, File toFolder) {
		try {
			print("Git clone from " + fromRepoUrl );
			print("to " + toFolder.getAbsolutePath() );
			GitClone.cloneRepository(fromRepoUrl, toFolder.getAbsolutePath() );
			print("Git repository successfully cloned." );
			return true;
		} catch (Exception e) { // All exceptions (including GitAPIException)
			LastError.setError(e);
			printError("Cannot clone from '" + fromRepoUrl + "'");
			printError(e);
			return false;
		}
	}
	
	private void setGitRemoteDepot(File gitRepoDir, String depotRemoteUrl) {
		try {
			print("Git add remote 'depot' (" + depotRemoteUrl + ")" );
			GitRemote.setRemote(gitRepoDir, DEPOT, depotRemoteUrl);
			print("Git remote 'depot' added." );
		} catch (Exception e) { // All exceptions
			printError("Cannot add remote in  '" + gitRepoDir.getAbsolutePath() + "'");
			printError(e);
		}
	}
}
