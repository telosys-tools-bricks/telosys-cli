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

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.telosys.tools.commons.TelosysToolsException;

public class GitClone {

	private GitClone() {
	}

	/**
	 * Clone a repository into a new working directory using the given credentials
	 * @param fromRepoUrl
	 * @param toLocalDir
	 * @param credentialsProvider (can be null)
	 * @return
	 * @throws GitAPIException
	 * @throws TelosysToolsException 
	 */
	public static boolean cloneRepository(String fromRepoUrl, String toLocalDir, CredentialsProvider credentialsProvider) throws GitAPIException, TelosysToolsException {
		if (fromRepoUrl == null) {
			throw new TelosysToolsException("fromRepoUrl argument is null");
		}
		if (toLocalDir == null) {
			throw new TelosysToolsException("toLocalDir argument is null");
		}
		boolean cloned = false;
        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI(fromRepoUrl);
        cloneCommand.setDirectory(new File(toLocalDir)); // throws IllegalStateException (RuntimeException)
        if (credentialsProvider != null) {
        	cloneCommand.setCredentialsProvider(credentialsProvider);
        }
        // Call command and close resources 
        try ( Git git = cloneCommand.call() ) { // throws GitAPIException, InvalidRemoteException (GitAPIException), TransportException (GitAPIException)
        	cloned = true;  // just to avoid Sonar warning if void block
        } 
        return cloned;
	}
}
