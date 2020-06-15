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

public class FoldersComparisonResult {

	private final String folder1;
	private final String folder2;
	private int numberOfComparisons;
	private int numberOfDifferences;
	private int numberOfErrors;

	public FoldersComparisonResult(String folder1, String folder2) {
		super();
		this.folder1 = folder1;
		this.folder2 = folder2;
		this.numberOfComparisons = 0;
		this.numberOfDifferences = 0;
		this.numberOfErrors = 0;
	}

	public void register(FilesComparisonResult r) {
		numberOfComparisons++;
		if ( r.differenceFound() ) {
			numberOfDifferences++;
		}
		if ( r.errorOccurred() ) {
			numberOfErrors++;
		}
	}

	public String getFolder1() {
		return folder1;
	}

	public String getFolder2() {
		return folder2;
	}

	public int getNumberOfComparisons() {
		return numberOfComparisons;
	}

	public int getNumberOfDifferences() {
		return numberOfDifferences;
	}

	public int getNumberOfErrors() {
		return numberOfErrors;
	}

}
