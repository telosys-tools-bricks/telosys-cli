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

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitRemote {

	private GitRemote() {
	}

	public static void setRemote(File gitRepoDir, String remoteName, String remoteUrl) throws IOException {
		// Path to .git directory ( eg "/path/to/your/repo/.git" )
		File gitSubDir = new File(gitRepoDir, ".git"); // add "/.git" at the end of the repo dir 
		if (!gitSubDir.exists()) {
		    throw new IllegalArgumentException("No .git directory found in " + gitRepoDir.getAbsolutePath()) ;
		}
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		
		Repository repository = builder.setGitDir(gitSubDir)
                .readEnvironment()
                .findGitDir()
                .build();		
		
		try {
			StoredConfig storedConfig = repository.getConfig();
			// Set "remote" (overwrite existing value if any)
			storedConfig.setString("remote", remoteName, "url",   remoteUrl);
			storedConfig.setString("remote", remoteName, "fetch", "+refs/heads/*:refs/remotes/" + remoteName + "/*");
			storedConfig.save();
		}
		finally {
			repository.close();
		}
	}
}
