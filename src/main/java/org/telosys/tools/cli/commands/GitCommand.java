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

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commands.git.GitClone;
import org.telosys.tools.cli.commands.git.GitRemote;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.Depot;

import jline.console.ConsoleReader;

/**
 * 'm' command
 * 
 * @author Laurent GUERIN
 *
 */
public class GitCommand extends CommandLevel2 {
	
	public static final String GIT = "git";
	public static final String REPO_URL_EXPECTED = "repository URL expected";
	
	//private static final Set<String> COMMAND_OPTIONS = new HashSet<>(Arrays.asList("-none")); 
	private static final Set<String> CLONE_COMMANDS = new HashSet<>(Arrays.asList("clonem", "cloneb" )); 

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
		return "Git like commands " ;
	}

	@Override
	public String getDescription() {
		return "Execute git commands for models and bundles";
	}
	
	@Override
	public String getUsage() {
		return GIT + " clonem|cloneb fromRepoURL";
	}
	
	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		
		if ( checkArguments(commandArguments, 2 ) ) { // && checkOptions(commandArguments, COMMAND_OPTIONS) ) {
			List<String> argsWithoutOptions = removeOptions(commandArguments);
			if ( ! argsWithoutOptions.isEmpty() ) {
				String gitCommand = argsWithoutOptions.get(0);
				if (CLONE_COMMANDS.contains(gitCommand) ) { // gitCommand.startsWith("clone")) {
					clone(gitCommand, argsWithoutOptions);
				}
				else {
					print("Unknown command '" + gitCommand + "'");
				}
				
//				switch ( gitCommand ) {
//				case "clonem" :
//					if ( argsWithoutOptions.size() >= 2 ) {
//						// fromRepoURL
//						cloneModel(argsWithoutOptions.get(1));
//					}
//					else {
//						print( REPO_URL_EXPECTED );
//					}
//					break;
//				case "cloneb" :
//					if ( argsWithoutOptions.size() >= 2 ) {
//						// fromRepoURL
//						cloneBundle(argsWithoutOptions.get(1));
//					}
//					else {
//						print( REPO_URL_EXPECTED );
//					}
//					break;
//				default:
//					print("Unknown command '" + gitCommand + "'");
//					break;
//				}
			}
//			Set<String> activeOptions = getOptions(commandArguments);
//			if ( ! argsWithoutOptions.isEmpty() ) {
//				if ( checkHomeDirectoryDefined() ) {
//					// 
//				}
//			}
////			else if ( isOptionActive("-none", activeOptions) ) {
////				unsetCurrentModel();
////				print( NO_CURRENT_MODEL );
////			}
		}
		return null ;
	}
	
	private void clone(String gitCommand, List<String> argsWithoutOptions) {
		if ( argsWithoutOptions.size() >= 2 ) {
			String fromRepoURL = argsWithoutOptions.get(1);
			if ( "clonem".equals(gitCommand) ) {
				cloneModel(fromRepoURL);
			}
			else if ( "cloneb".equals(gitCommand) ) {
				cloneBundle(fromRepoURL);
			}
			else {
				// not supposed to happen 
				print("Unknown command '" + gitCommand + "'");
			}
		}
		else {
			print( REPO_URL_EXPECTED );
		}
	}
	
	private void cloneModel(String fromRepoUrl) {
		TelosysProject telosysProject = getTelosysProject();
		String modelName = getRepoNameFromUrl(fromRepoUrl);
		if ( ! telosysProject.modelFolderExists(modelName) ) {
			File modelFolder = telosysProject.getModelFolder(modelName);
//			print("Cloning model '" + modelName + "' ");
//			gitClone(fromRepoUrl, modelFolder);
			String depot = telosysProject.getTelosysToolsCfg().getDepotForModels();
			gitCloneAndAddRemote(fromRepoUrl, modelFolder, depot);
		}
		else {
			print("Model '" + modelName + "' already exists.");
		}
	}
	
	private void cloneBundle(String fromRepoUrl) {
		TelosysProject telosysProject = getTelosysProject();
		String bundleName = getRepoNameFromUrl(fromRepoUrl);
		if ( ! telosysProject.bundleFolderExists(bundleName) ) {
			File bundleFolder = telosysProject.getBundleFolder(bundleName);
			print("Cloning bundle '" + bundleName + "' ");
			gitClone(fromRepoUrl, bundleFolder);
		}
		else {
			print("Bundle '" + bundleName + "' already exists.");
		}
	}
	
	private void gitCloneAndAddRemote(String fromRepoUrl, File localRepoDir, String depotDefinition) {
		// Step 1 - Clone
		gitClone(fromRepoUrl, localRepoDir);
		// Step 2 - Add remote
		try {
			Depot depot = new Depot(depotDefinition);
			//String depotURL = buildDepotURL(depot, xxx);
			String localRepoName = localRepoDir.getName(); 
			String depotURL = depot.buildGitRepositoryURL(localRepoName);
			setGitRemoteDepot(localRepoDir, depotURL);
		} catch (TelosysToolsException e) {
			printError("Cannot set depot");
			printError(e);
		}
	}
	private void gitClone(String fromRepoUrl, File toFolder) {
		try {
			print("Git clone from " + fromRepoUrl );
			print("to " + toFolder.getAbsolutePath() );
			GitClone.cloneRepository(fromRepoUrl, toFolder.getAbsolutePath() );
			print("Git repository successfully cloned." );
		} catch (Exception e) { // All exceptions (including GitAPIException)
			printError("Cannot clone from '" + fromRepoUrl + "'");
			printError(e);
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

	private void setGitRemoteDepot(File gitRepoDir, String depotRemoteUrl) {
		try {
			print("Git add remote 'depot' (" + depotRemoteUrl + ")" );
			GitRemote.setRemote(gitRepoDir, "depot", depotRemoteUrl);
			print("Git remote added." );
		} catch (Exception e) { // All exceptions
			printError("Cannot add remote in  '" + gitRepoDir.getAbsolutePath() + "'");
			printError(e);
		}
	}
}
