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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitInit {

	private GitInit() {
	}

	public static boolean init(File directory) throws GitAPIException {
		boolean done = false;
		
		// Define command
		InitCommand initCommand = Git.init();
		initCommand.setDirectory(directory); // throws IllegalStateException (RuntimeException)
		
		// Execute command and close resources 
        try ( Git git = initCommand.call() ) { 
        	done = true;  // just to avoid Sonar warning if void block
        } 	

        return done;
	}
}
