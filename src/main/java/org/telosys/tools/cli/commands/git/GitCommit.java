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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.telosys.tools.commons.TelosysToolsException;

public class GitCommit {

	private GitCommit() {
	}

	/**
	 * Commit the given working tree with default author and message
	 * @param workingTree
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws TelosysToolsException
	 */
	public static String commit(File workingTree) throws IOException, GitAPIException, TelosysToolsException  {
		return commit(workingTree, null, null);
	}
	
	/**
	 * Commit the given working tree with the given author and message
	 * @param workingTree
	 * @param author
	 * @param commitMessage
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws TelosysToolsException
	 */
	public static String commit(File workingTree, PersonIdent author, String commitMessage) throws IOException, GitAPIException, TelosysToolsException {
		if (workingTree == null) {
			throw new TelosysToolsException("workingTree argument is null");
		}

		try ( Repository repository = GitUtil.buildRepository(workingTree) ) {
			// Check if something to commit (via Git Status) 
			int stagedCount = GitStatus.getStagedCount(repository);
			if ( stagedCount > 0 ) {
				// Check commit author
				if ( author == null ) {
					author = buildDefaultAuthor();
				}
				// Check commit message
				if ( commitMessage == null ) {
					commitMessage = buildDefaultMessage(stagedCount);
				}
				// COMMIT
				return executeCommit(workingTree, author, commitMessage);
			}
			else {
				return "" ; // No commit => no commit id
			}
		} // close Repository
	}
	
	private static String executeCommit(File workingTree, PersonIdent author, String commitMessage) throws IOException, GitAPIException {
		try (Git git = Git.open(workingTree)) { // param = the repository to open. May be either the GIT_DIR, or the working tree directory that contains .git.
				CommitCommand commitCommand = git.commit()
						.setAuthor(author)    // or .setAuthor("Name", "email@example.com")  // Who originally wrote the changes
						.setCommitter(author) // or .setCommitter("Name", "email@example.com") //  Who actually created the commit
						.setMessage(commitMessage)
    			   ;    	   
			// commits do always contain both fields internally: Author and Committer
			//  - Author    : shown by default in git log
			//  - Committer : hidden unless you ask for it, visible with "git log --pretty=full"
			// if only set the author: Author is overridden, committer remains the current Git identity.
			// if only set the committer: Committer is overridden, author defaults to the committer.
			// if no "setAuthor" or "setCommitter"
			//  JGit falls back to the same resolution logic Git uses :
			//   1) Repository config (.git/config) -> usable for each repo 
			//   2) Global Git config ('~/.gitconfig' on Linux/macOS) ('C:/Users/user-name/.gitconfig' on Windows)
			//   3) System properties / defaults : try to use the system user (System.getProperty("user.name")) and may leave the email blank.
			//  This can lead to commits with "unknown" as committer
    	   
			RevCommit revCommit = commitCommand.call();
    	   
			return revCommit.getId().getName(); // SHA-1 long version    (use getId().abbreviate(7).name() for short version)
		}
	}
	
	private static String buildDefaultMessage(int changeCount) {
	    // Format timestamp
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String timestamp = LocalDateTime.now().format(formatter);
	    // Build message
	    return String.format("Telosys commit (%s): %d change(s)", timestamp, changeCount);
	}
	private static PersonIdent buildDefaultAuthor() {
	    return new PersonIdent("Telosys Agent", "contact@telosys.org");
	}

}
