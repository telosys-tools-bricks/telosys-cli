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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FilesComparator {
	
	private final List<Pattern> patterns ;
	
	public FilesComparator() {
		super();
		this.patterns = new ArrayList<>();
	}
	
	public FilesComparator(List<String> ignorePatternStrings) {
		super();
		this.patterns = new ArrayList<>();
		if ( ignorePatternStrings != null ) {
			for (String s : ignorePatternStrings ) {
				Pattern pattern = Pattern.compile(s);
				this.patterns.add(pattern);
			}
		}
	}

	public FilesComparisonResult compareFiles(File file1, File file2) {
		// Prepare comparison result
		FilesComparisonResult result = new FilesComparisonResult(file1, file2) ;
		// Check file existence
		if ( fileExist(file1, result) && fileExist(file2, result) ) {
			// Compare files
			try (BufferedReader reader1 = new BufferedReader(new FileReader(file1)) ;
				 BufferedReader reader2 = new BufferedReader(new FileReader(file2)) ) {
					compareFiles(reader1, reader2, result);
				} 
				catch ( IOException e) {
					result.setErrorMessage("IOException : " + e.getMessage() );
				}
		}
		return result ;
	}

	private boolean fileExist(File file, FilesComparisonResult result) {
		if ( ! file.exists() ) {
			result.setErrorMessage("'" + file.getAbsolutePath() + "' doesn't exist");
			return false;
		}
		if ( ! file.isFile() ) {
			result.setErrorMessage("'" + file.getAbsolutePath()  + "' is not a file");
			return false;
		}			
		return true;
	}
	
	private void compareFiles(BufferedReader reader1, BufferedReader reader2, FilesComparisonResult result) throws IOException {

		// Read first lines pair
		int lineNum = 1;
		String line1 = reader1.readLine();
		String line2 = reader2.readLine();
		while (line1 != null || line2 != null) {
			// Check if one of the files has less lines
			if (line1 == null) {
				result.setDifference(lineNum, "(no line)", line2);
				return ;
			}
			if (line2 == null ) {
				result.setDifference(lineNum, line1, "(no line)");
				return ;
			} 
			// Compare the 2 lines 
			compareLines(lineNum, line1, line2, result);
			if ( result.differenceFound() ) {
				// Stop at the first lines difference found 
				return;
			}
			// Read next lines pair
			lineNum++;
			line1 = reader1.readLine();
			line2 = reader2.readLine();
		}
	}
	
	private void compareLines(int lineNum, String line1, String line2, FilesComparisonResult result) {
		
		if ( ignoreLine(line1) ) {
			return ;
		} 
		else {
			// Basic comparison 
			if ( ! line1.equals(line2) ) {
				result.setDifference(lineNum, line1, line2);
			}
		}
	}
	
	/**
	 * Check if the given line must be ignored (true if it matches one the patterns)
	 * @param line
	 * @return
	 */
	private boolean ignoreLine(String line) {
		for ( Pattern pattern : patterns ) {
			if ( pattern.matcher(line).matches() ) {
				return true ;
			}
		}
		return false ;
	}
}
