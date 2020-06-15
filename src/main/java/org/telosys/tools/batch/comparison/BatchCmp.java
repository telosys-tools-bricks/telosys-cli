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

import java.util.List;

public class BatchCmp {

	private static void print(String s) {
		System.out.println(s);
	}
	
	private static void printContext(String dir1, String dir2, List<String> patterns) {
		print(". Folder #1   : '" + dir1 + "'" );
		print(". Folder #2   : '" + dir2 + "'" );
		print(". Ignore patterns : ");
		if ( patterns != null ) {
			for (String s : patterns ) {
				print("  . '" + s + "' " );
			}
			print("  " + patterns.size() + " pattern(s) defined" );
		} else {
			print("  no pattern defined" );
		}
	}
	
	private static final String LINE = "+---------------------+" ;
	
	public static void run(String dir1, String dir2, List<String> patterns) {

		printContext(dir1, dir2, patterns);
		print("");
		
		print(LINE);
		print("| Starting comparison | ");
		print(LINE);

		FoldersComparisonResult r ;
		try {
			FoldersComparator foldersComparator = new FoldersComparator();
			r = foldersComparator.compareFolders(dir1, dir2, patterns);
		} catch (Exception e) {
			print("ERROR :");
			print(" " + e.getMessage());
			return;
		}
		
		print("");
		print(LINE);
		print("| End of comparison   | ");
		print(LINE);

		print("");
		printContext(dir1, dir2, patterns);
		print("");
		print("Number of comparisons : " + r.getNumberOfComparisons() );
		print("Number of differences : " + r.getNumberOfDifferences() );
		print("Number of errors      : " + r.getNumberOfErrors() );
		print("");
	}
	
}
