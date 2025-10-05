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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;

public class GitAdd {

	private GitAdd() {
	}

	public static void addAll(File workingTree) throws GitAPIException, IOException {
       try (Git git = Git.open(workingTree)) {
            // Stage new and modified files
            git.add()
               .addFilepattern(".")
               .setUpdate(false) // include untracked files
               .call();

            // Stage deletions (like `git add -u .`)
            DirCache dirCache = git.add()
               .addFilepattern(".")
               .setUpdate(true) // stages deletions and modifications of tracked files
               .call();
            
            // Returned "DirCache" (DirCache models the Git index/staging area)
            // The DirCache from the first call  --> index state after adding untracked + modified files.
            // The DirCache from the second call --> index state after also staging deletions (final result)
            // To get final staging state => just use the DirCache returned from the SECOND CALL
            
            // Removed because return 1 even if noting in index (known problem with JGit)
            // return dirCache.getEntryCount(); // Total number of file entries stored in the index
            // Sometimes that single entry is the HEAD or .gitignore entry.
            
        } 
	}
}
