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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.telosys.tools.commons.TelosysToolsException;

public class GitPublish {

	private GitPublish() {
	}

	/**
	 * Publish the given working tree to the given remote
	 */
	public static List<String> publish(File gitWorkingTreeDir, String remoteName, CredentialsProvider credentialsProvider) throws IOException, GitAPIException, TelosysToolsException {
		if (gitWorkingTreeDir == null) {
			throw new TelosysToolsException("gitWorkingTreeDir argument is null");
		}

		List<String> resultList = new LinkedList<>();

		//--- Step 1 : add all changes to index (staging)
		resultList.add("Adding all changes to index...");
		GitAdd.addAll(gitWorkingTreeDir);
		resultList.add("  changes added.");
		
		//--- Step 2 : commit 
		resultList.add("Committing changes to repository...");
		String commitResult = GitCommit.commit(gitWorkingTreeDir);
		if ( commitResult != null && !commitResult.trim().isEmpty() ) {
			resultList.add("  commit OK, id (sha) = " + commitResult);
		}
		else {
			resultList.add("  nothing to commit (staged changes = 0)" );
		}
		
		//--- Step 3 : push to remote 
		resultList.add("Pushing to remote '" + remoteName + "' ...");
		List<String> pushResult = GitPush.pushAllBranches(gitWorkingTreeDir, remoteName, credentialsProvider);
		for( String s : pushResult ) {
			resultList.add("  " + s);
		}
		
		return resultList;
	}
	
}
