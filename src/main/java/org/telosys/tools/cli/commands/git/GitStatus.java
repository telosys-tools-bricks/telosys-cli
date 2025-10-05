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
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;

public class GitStatus {

	private GitStatus() {
	}

	private static String getCurrentLocalBranch(Repository repository) throws IOException  {
		try (Git git = new Git(repository)) {
			// Get the current local branch
            return repository.getBranch();
		}
	}
	
    private static String getUpstreamBranch(Repository repository, String localBranchName) throws IOException {
        StoredConfig config = repository.getConfig();
        String remoteName = config.getString("branch", localBranchName, "remote");
        String mergeRef   = config.getString("branch", localBranchName, "merge");

        if (remoteName != null && mergeRef != null) {
            // Format: "refs/heads/<branch-name>" -> "<remote-name>/<branch-name>"
            String branchName = mergeRef.replace("refs/heads/", "");
            return remoteName + "/" + branchName;
        }
        return null;
    }
    
	private static Status getStatus(Repository repository) throws GitAPIException  {
		try (Git git = new Git(repository)) {
			return git.status().call();
		}
	}
	
	/**
	 * Get number of 'staged' elements that would be included in a 'commit'
	 * @param repository
	 * @return
	 * @throws GitAPIException
	 */
	public static int getStagedCount(Repository repository) throws GitAPIException {
		return getStagedCount( getStatus(repository) );
	}
	private static int getStagedCount(Status status) {
		// Check only the "staged" elements
		return    status.getAdded().size()  // files added to the index (not in HEAD, but present in index)
				+ status.getChanged().size() // changed files from HEAD to index
				+ status.getRemoved().size() // removed files (staged for deletion)
				;
	}
	
	/**
	 * Get number of 'unstaged' elements that would be added to 'index'
	 * @param status
	 * @return
	 */
	protected static int getUnstagedCount(Status status) {
		// Check only the "unstaged" elements
		return    status.getModified().size() // modified but not staged (changed in the working tree but not staged)
				+ status.getMissing().size()  // deleted from disk but not staged (git rm not called yet)
				+ status.getUntracked().size() // new files not tracked by Git 
				+ status.getUntrackedFolders().size() // new directories not tracked by Git
				;
	}

	public static List<String> getStatusReport(File workingTree) throws GitAPIException, IOException {
		List<String> report = new LinkedList<>();
		Repository repository = GitUtil.buildRepository(workingTree);
		// Current local branch
		String currentLocalBranch = getCurrentLocalBranch(repository);
		if ( currentLocalBranch != null ) {
			String branch = "Current local branch '" + currentLocalBranch + "'";
			String upstreamBranch = getUpstreamBranch(repository, currentLocalBranch);
			if (upstreamBranch != null) {
				branch = branch + " (tracks remote branch '" + upstreamBranch + "')";
			}
			else {
				branch = branch + " (no upstream branch)";
			}
			report.add(branch);
		}
		else {
			report.add("No current local branch");
		}
		
		// Status / changes 
		Status status = getStatus(repository);

		int stagedCount = getStagedCount(status);
		report.add("Staged changes (to be committed): " + stagedCount );
		add(report, status.getAdded(),   "new:      "); // not in HEAD 
		add(report, status.getChanged(), "modified: "); // changed from HEAD to index 
		add(report, status.getRemoved(), "deleted:  "); // removed from index, but in HEAD 
		
		int unstagedCount = getUnstagedCount(status);
		report.add("Unstaged changes: " + unstagedCount );
		add(report, status.getModified(),         "modified: ");  // modified on disk relative to the index 
		add(report, status.getMissing(),          "deleted:  ");  // in index, but not filesystem 
		add(report, status.getUntracked(),        "untracked:");  // files not ignored, and not in the index
		add(report, status.getUntrackedFolders(), "untracked:");  // directories not ignored, and not in the index
		
		report.add("Ignored (in .gitignore): " + status.getIgnoredNotInIndex().size() );
		add(report, status.getIgnoredNotInIndex(), "ignored:  ");  //  ignored files which are not in the index
		
		return report;
	}
	
	private static void add(List<String> report, Set<String> set, String type) {
		for (String s : set) {
		    report.add("  . " + type +  " " + s);
		}
	}
}
