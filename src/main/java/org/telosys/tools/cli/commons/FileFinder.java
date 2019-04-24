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
package org.telosys.tools.cli.commons;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileFinder {

	/**
	 * Private constructor
	 */
	private FileFinder() {
	}

	/**
	 * Finds the files located in the given dir with a name containing the given abbreviation
	 * @param abbreviation
	 * @param dir the directory where to search the files
	 * @param ext the file extension (can be null if not applicable)
	 * @return
	 */
	public static List<File> find(String abbreviation, File dir, String ext) {
		List<File> filesFound = new LinkedList<>();
		if ( dir.isDirectory() ) {
			File[] files = dir.listFiles() ;
			if ( files != null ) {
				for ( File f : files ) {
					String fileName = f.getName();
					if ( fileName.contains(abbreviation) ) {
						if ( ext != null ) { // if restriction on the file extension
							if ( fileName.endsWith(ext) ) {
								filesFound.add(f);
							}
						}
						else { // no file extension restriction
							filesFound.add(f);
						}
					}
				}
			}
		}
		return filesFound ;
	}


}
