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
package org.telosys.tools.batch.comparison;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;

public class FoldersComparator {
	
	private String currentFolder = "";
	
	public FoldersComparator() {
		super();
	}
	
	private void print(String msg) {
		System.out.println(msg);
	}
	
	private void report(FilesComparisonResult r) {
		String folder1 = r.getFile1().getParent();
		if ( ! currentFolder.equals(folder1) ) {
			currentFolder = folder1 ;
			print("[DIR] " + currentFolder);
		}
		if ( r.differenceFound() ) {
			print(r.getFile1().getAbsolutePath());
			print(" > " + r.getDifferenceLine1());
			print(r.getFile2().getAbsolutePath());
			print(" > " + r.getDifferenceLine2());
			print("" );
		}
		if ( r.errorOccurred() ) {
			print("ERROR : " + r.getErrorMessage());
		}
	}
	
	public FoldersComparisonResult compareFolders(String dir1, String dir2, List<String> patterns) {
		FoldersComparisonResult foldersComparisonResult = new FoldersComparisonResult(dir1, dir2);
		if ( checkFolder(dir1) && checkFolder(dir2) ) {
			File folder1 = new File(dir1);
			File folder2 = new File(dir2);
			// get the list of files to be compared
			List<FilesPair> pairs = getFilesToCompare(folder1, folder2);
			// compare all the pairs of files 
			FilesComparator fileComparator = new FilesComparator(patterns);
			for ( FilesPair pair : pairs) {
				FilesComparisonResult filesComparisonResult = fileComparator.compareFiles(pair.getFile1(), pair.getFile2());
				foldersComparisonResult.register(filesComparisonResult);
				report(filesComparisonResult);
			}
		}
		return foldersComparisonResult ;
	}
	
	private boolean checkFolder(String folder) {
		File file = new File(folder);
		if ( ! file.exists() ) {
			throw new RuntimeException("Folder '" + folder + "' not found");
		}
		if ( ! file.isDirectory() ) {
			throw new RuntimeException("'" + folder + "' is not a directory");
		}			
		return true;
	}

	/**
	 * Builds a list of files to be compared <br>
	 * The list is based on the files contained in the first folder (and supposed to exist in folder 2)
	 * @param folder1
	 * @param folder2
	 * @return
	 */
	private List<FilesPair> getFilesToCompare(File folder1, File folder2) {
		// get list of all files recursively (full paths) from first folder
		List<String> filesInFolder1 = DirUtil.getDirectoryFiles(folder1, true);
		// build list of files pairs
		List<FilesPair> pairs = new LinkedList<>();
		String folder1FullPath = folder1.getAbsolutePath();
		String folder2FullPath = folder2.getAbsolutePath();
		for ( String file1FullPath : filesInFolder1 ) {
			String fileRelativePathInFolders = file1FullPath.substring( folder1FullPath.length() + 1 );
			String file2FullPath = FileUtil.buildFilePath(folder2FullPath, fileRelativePathInFolders);
			FilesPair pair = new FilesPair( file1FullPath, file2FullPath);
			pairs.add(pair);
		}
		return pairs;
	}
	
}
