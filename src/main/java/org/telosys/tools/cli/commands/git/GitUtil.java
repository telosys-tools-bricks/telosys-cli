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

public class GitUtil {

	private GitUtil() {
	}

	/**
	 * Returns true if the given file is a Git repository (a directory having a '.git' subdirectory)
	 * @param file
	 * @return
	 */
	public static boolean isGitRepository(File file) {
        if (file == null) return false;
		if ( file.exists() && file.isDirectory() ) {
			File gitSubFolder = new File(file, ".git");
			return gitSubFolder.exists() && gitSubFolder.isDirectory() ;
		}
		return false;
	}
	
	/**
	 * Returns true if the given string is a valid Git URL
	 * @param arg
	 * @return
	 */
	public static boolean isGitUrl(String arg) {
        if (arg == null) return false;
        String s = arg.trim();
        if (s.isEmpty() ) return false;

        return s.startsWith("http://")
            || s.startsWith("https://")
            || s.startsWith("git://")
            || s.startsWith("ssh://")
            || s.startsWith("file://")            
            || isLocalPath(s)            
            || isGitSCPlikeSyntax(s); // SCP-like syntax: "user@host:repo.git"		
	}
	
	private static boolean isLocalPath(String s) {
		// "/path/to/repo.git" (bare path)
		return s.startsWith("/")      // absolute path "/home/user/projects/myrepo.git"
				|| s.startsWith("./") // relative path "./myrepo.git"
				|| s.contains(":/")   // Windows absolute path "C:/Users/me/projects/myrepo.git"
				|| s.contains(":\\")  // Windows absolute path "C:\Users\me\projects\myrepo.git"
				|| s.startsWith("//") // Network shares (UNC paths) "//SERVER/Share/repo.git"
				;
	}
	
	private static boolean isGitSCPlikeSyntax(String s) {
		// The so-called SCP-like syntax is the form you most often see in Git SSH remotes, for example:
		// git@github.com:user/repo.git
		// user@server:project.git
		int iArobase = s.indexOf('@');
		int iColon   = s.indexOf(':');
		// "x@x:x"
		return iArobase > 0  &&  iColon > iArobase+1 ;
	}
}
