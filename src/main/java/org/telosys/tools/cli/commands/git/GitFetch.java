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
package org.telosys.tools.cli.commands.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.telosys.tools.commons.TelosysToolsException;

public class GitFetch {

	private GitFetch() {
	}
	
	/**
	 * Fetch the current branch from the given remote repository
	 * @param workingTree
	 * @param remote
	 * @param credentialsProvider
	 * @return
	 * @throws GitAPIException
	 * @throws IOException
	 * @throws TelosysToolsException
	 */
	public static ObjectId fetch(File workingTree, String remote, CredentialsProvider credentialsProvider) throws GitAPIException, IOException, TelosysToolsException {
		return fetch(workingTree, remote, null, credentialsProvider);
	}
	
	/**
	 * Fetch the given branch from the given remote repository
	 * @param workingTree
	 * @param remote
	 * @param branchParam
	 * @param credentialsProvider (can be null)
	 * @return
	 * @throws GitAPIException
	 * @throws IOException
	 * @throws TelosysToolsException
	 */
	public static ObjectId fetch(File workingTree, String remote, String branchParam, CredentialsProvider credentialsProvider) throws GitAPIException, IOException, TelosysToolsException {
		if (workingTree == null) {
			throw new TelosysToolsException("workingTree argument is null");
		}
		if (remote == null) {
			throw new TelosysToolsException("remote argument is null");
		}

		try ( Repository repository = GitUtil.buildRepository(workingTree) ) {
			
			try ( Git git = new Git(repository) ) {
				
				//------------------------------------------------------------
				// STEP 1 - Fetch from remote into a temporary tracking branch
				//------------------------------------------------------------
				FetchCommand fetchCommand = git.fetch();
				
				// set the remote name or the remote URL
				fetchCommand.setRemote(remote);
				
				// if authentication is required
		        if (credentialsProvider != null) {
		        	fetchCommand.setCredentialsProvider(credentialsProvider);
		        }
				
		        // Get current branch if not provided
		        String branch ;
		        if ( branchParam != null ) {
		        	branch = branchParam ;
		        }
		        else {
		        	// Get the short name of the current branch that "HEAD" points to
		        	branch = repository.getBranch();
			        // Still null ?
			        if ( branch == null ) {
			        	throw new TelosysToolsException("Cannot get current branch from repository");
			        }
		        }
		        // In JGit, when you fetch from a URL (instead of a named remote), you cannot directly reset to the remote branch 
		        // because JGit fetch requires a refspec to know where to store the fetched commits locally.
		        // Fetching only a single branch
		        // "+refs/heads/main"       => source branch on the remote. Only this branch is fetched.
		        // "refs/remotes/temp/main" => local temporary branch. JGit now has a reference to the fetched commit.
		        // "+" => forces the update even if it’s not a fast-forward. (ensures a forced update of the temporary ref. This avoids conflicts if your local refs diverge)
		        String temporaryRefBranch = "refs/remotes/temp/" + branch ;
		        fetchCommand.setRefSpecs("+refs/heads/" + branch + ":" + temporaryRefBranch ); // local_branch : temporary_fetched_branch
		        
				// execute "fetch" 
		        fetchCommand.call();
	
				//------------------------------------------------------------
				// STEP 2 - Get the ObjectId of the fetched commit
				//------------------------------------------------------------
	            Ref tempRef = git.getRepository().findRef(temporaryRefBranch);
	            if (tempRef == null) {
	                throw new TelosysToolsException("Fetched branch '" + temporaryRefBranch + "'not found");
	            }
	            return tempRef.getObjectId(); // ObjectId of the fetched commit
			}
		}
	}
}
