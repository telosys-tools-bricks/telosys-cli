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

import java.util.Collection;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;

public class GitLsRemote {

	private GitLsRemote() {
	}

	private static Collection<Ref> lsRemote(String remote, CredentialsProvider credentialsProvider) throws GitAPIException {
        // 'ls-remote' CLI command equivalent
		LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository().setRemote(remote);
		// optional authentication
		if ( credentialsProvider != null ) {
			lsRemoteCommand.setCredentialsProvider(credentialsProvider);
		}
		// call
		return lsRemoteCommand.call();
	}
	
	public static boolean exists(String remote, CredentialsProvider credentialsProvider) {
		try {
			lsRemote(remote, credentialsProvider);
			// Repository exists and is accessible
			return true;
		} catch (Exception e) {
			// Repository does not exist or is not accessible
			return false;
		}
	}

	public static Collection<Ref> getRefs(String remote, CredentialsProvider credentialsProvider) throws GitAPIException {
		return lsRemote(remote, credentialsProvider);
	}

}
