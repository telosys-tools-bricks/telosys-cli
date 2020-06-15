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

public class FilesComparisonResult {

	private final File file1;
	private final File file2;

	private boolean differenceFound;
	private int differenceLineNum;
	private String differenceLine1;
	private String differenceLine2;

	//private Exception exception;
	private String errorMessage;

	public FilesComparisonResult(File file1, File file2) {
		super();
		this.file1 = file1;
		this.file2 = file2;
		this.differenceFound = false;
		this.differenceLineNum = 0;
	}

	public File getFile1() {
		return file1;
	}

	public File getFile2() {
		return file2;
	}

//	public void setError ( Exception ex) {
//		this.exception = ex;
//	}
	public void setErrorMessage ( String msg) {
		this.errorMessage = msg;
	}


	public void setDifference ( int differenceLineNum, String differenceLine1, String differenceLine2) {
		this.differenceFound = true;
		this.differenceLineNum = differenceLineNum;
		this.differenceLine1 = differenceLine1;
		this.differenceLine2 = differenceLine2;
	}

	public boolean differenceFound() {
		return differenceFound;
	}

	public int getDifferenceLineNum() {
		return differenceLineNum;
	}

//	public String getDifferenceMessage() {
//		return differenceMessage != null ? differenceMessage : "" ;
//	}
	
	public String getDifferenceLine1() {
		return differenceLine1 != null ? differenceLine1 : "" ;
	}
	public String getDifferenceLine2() {
		return differenceLine2 != null ? differenceLine2 : "" ;
	}
	
//	public Exception getError() {
//		return exception;
//	}
	
	public boolean errorOccurred() {
		return errorMessage != null;
	}
	public String getErrorMessage() {
		return errorMessage != null ? errorMessage : "" ;
	}
}
