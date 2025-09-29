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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.telosys.tools.commons.StrUtil;

public class GitPush {

	private GitPush() {
	}

	/**
	 * Pushes all branches to the given remote-name
	 * @param gitWorkingTreeDir
	 * @param remote remote alias name or URL
	 * @param credentialsProvider user name + personal access token (for GitHub or GitLab with 2FA)
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static List<String> pushAllBranches(File gitWorkingTreeDir, String remote, CredentialsProvider credentialsProvider) throws IOException, GitAPIException {
		List<String> resultList = new LinkedList<>();
		// "/aaa/bbb/ccc/.git" directory 
		File gitRepoDir = GitUtil.getRepositoryDir(gitWorkingTreeDir);
		
		try (Git git = Git.open(gitRepoDir)) {
			// Setup push command
			PushCommand pushCommand = git.push();
			// Define the 'remote', it can be 
			//  . a remote alias name ( eg "origin", "depot", etc)
			//  . a URL instead of a remote name
			pushCommand.setRemote(remote); 
			
			// If you use 2FA on GitHub or GitLab, you must use a personal access token, not your password
			pushCommand.setCredentialsProvider(credentialsProvider);

			// define branche(s) to push 
			pushCommand.setPushAll(); // pushes all branches
			// pushCommand.setRefSpecs(specs); // pushes specific branches
			
			Iterable<PushResult> results = pushCommand.call();
			
			for (PushResult result : results) {
				String messages = result.getMessages();
				if ( ! StrUtil.nullOrVoid(messages) ) {
					resultList.add(messages);
				}
				for (RemoteRefUpdate update : result.getRemoteUpdates()) {
					resultList.add(update.getRemoteName() + " -> " + update.getStatus());
				}
			}
		}
		return resultList;
	}
}
