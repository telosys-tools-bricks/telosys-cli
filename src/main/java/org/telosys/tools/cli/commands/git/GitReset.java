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
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.telosys.tools.commons.TelosysToolsException;

public class GitReset {

	private GitReset() {
	}

	public static void reset(File workingTree, ResetType resetType, ObjectId commitId) throws TelosysToolsException, IOException, GitAPIException {
		if (workingTree == null) {
			throw new TelosysToolsException("workingTree argument is null");
		}

		try ( Repository repository = GitUtil.buildRepository(workingTree) ) {

			try (Git git = new Git(repository)) {

				// Hard reset local branch to fetched commit
				ResetCommand resetCommand = git.reset();
				
				resetCommand.setMode(resetType); // Set reset type: SOFT, MIXED, HARD
				
				resetCommand.setRef(commitId.getName()); // Set the name of the Ref to reset to
				
				resetCommand.call();
			}
		}

	}
}
