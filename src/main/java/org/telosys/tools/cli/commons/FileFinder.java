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

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;

public class FileFinder  {

	private final File folder ;
	private final String fileExt;
	
	/**
	 * Constructor
	 * @param folder
	 * @param fileExt
	 */
	public FileFinder(File folder, String fileExt) {
		if ( folder == null ) {
			throw new IllegalArgumentException("folder is null");
		}
		if ( ! folder.exists() ) {
			throw new IllegalArgumentException("folder doesn't exist : " + folder.getAbsolutePath() );
		}
		if ( ! folder.isDirectory() ) {
			throw new IllegalArgumentException("not a directory : " + folder.getAbsolutePath() );
		}
		this.folder = folder;
		this.fileExt = fileExt;
	}
	
	/**
	 * Try to find one or more files matching the given name or part of name
	 * @param fileNameAbbreviation
	 * @return
	 */
	public List<File> find(String fileNameAbbreviation) {
		// 1) try to find a unique file with the given name
		File file = findUniqueFile(fileNameAbbreviation);
		if ( file != null ) {
			List<File> filesFound = new LinkedList<>();
			filesFound.add(file);
			return filesFound;
		}
		else {
			// 2) try to find a unique file matching the given abbreviation
			return findFiles(fileNameAbbreviation);
		}
	}
	
	/**
	 * Try to find a unique file with the given file name (in folder with expected ext)
	 * @param fileName file name with or without extension ( eg "aaa", "aaa." or "aaa.ext" )
	 * @return
	 */
	public File findUniqueFile(String fileName) {
		File file = buildFile(fileName);
		if ( file.exists() && file.isFile() ) { // Check name for exact matching (case sensitive)
			// The given file name part is an existing file => use it
			return file ;
		}
		else {
			// No unique file
			return null;
		}
	}

	/**
	 * Builds a File with the expected extension if any
	 * @param fileName
	 * @return
	 */
	private File buildFile(String fileName) {
		String fileNameWithExt = "";
		if ( ! StrUtil.nullOrVoid(fileExt) ) {
			if ( fileName.endsWith(fileExt) ) {
				// "xxxx.ext"
				fileNameWithExt = fileName ;
			}
			else if ( fileNameWithExt.endsWith(".") ) {
				// "xxxx."
				fileNameWithExt = StrUtil.removeEnd(fileName, ".")  + fileExt ;
			}
			else {
				// "xxx"
				fileNameWithExt = fileName + fileExt ;
			}
		}
		else {
			fileNameWithExt = fileName ;
		}
		return new File( FileUtil.buildFilePath(folder.getAbsolutePath(), fileNameWithExt) );
	}

	/**
	 * Finds the files with a name containing the given abbreviation (in folder with expected ext)
	 * @param fileNameAbbreviation
	 * @return
	 */
	public List<File> findFiles(String fileNameAbbreviation) {
		List<File> filesFound = new LinkedList<>();
		File[] files = folder.listFiles() ;
		if ( files != null ) {
			for ( File f : files ) {
				if ( fileNameMatchesCriteria(f, fileNameAbbreviation) ) {
					filesFound.add(f);
				}
			}
		}
		return filesFound ;
	}
	
	private boolean fileNameMatchesCriteria(File file, String abbreviation) {
		String fileName = file.getName();
		if ( fileName.contains(abbreviation) ) {
			if ( fileExt != null ) { // if restriction on the file extension
				if ( fileName.endsWith(fileExt) ) {
					return true;
				}
			}
			else { // no file extension restriction
				return true;
			}
		}
		return false;
	}
}
