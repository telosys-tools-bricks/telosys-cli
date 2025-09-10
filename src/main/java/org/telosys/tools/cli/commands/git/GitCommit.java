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

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitCommit {

	private GitCommit() {
	}

	public static String commit(File gitRepoDir, String commitMessage) throws IOException, GitAPIException  {
       try (Git git = Git.open(gitRepoDir)) {
    	   CommitCommand commitCommand = git.commit()
    			   .setMessage(commitMessage)
//    			   .setAuthor("Name", "email@example.com")
//    			   .setCommitter("Name", "email@example.com")
    			   ;    	   
    	   // Author    -> Who originally wrote the changes.
    	   // Committer -> Who actually created the commit object in the repository.
    	   // if only set the author: Author is overridden, committer remains the current Git identity.
    	   // if only set the committer: Committer is overridden, author defaults to the committer.
    	   // if no "setAuthor" or "setCommitter"
    	   //  JGit falls back to the same resolution logic Git uses :
    	   //   1) Repository config (.git/config) -> usable for each repo 
    	   //   2) Global Git config ('~/.gitconfig' on Linux/macOS) ('C:/Users/user-name/.gitconfig' on Windows)
    	   //   3) System properties / defaults : try to use the system user (System.getProperty("user.name")) and may leave the email blank.
    	   //  This can lead to commits with "unknown" as committer
    	   
    	   RevCommit revCommit = commitCommand.call();
    	   
//    	   revCommit.getId().getName(); // SHA-1 long version 
//    	   revCommit.getId().abbreviate(7).name(); // returns the short version (same as what GitHub shows in commit lists)
    	   
    	   return revCommit.getId().getName(); // SHA-1 long version 
        } 
	}
}
