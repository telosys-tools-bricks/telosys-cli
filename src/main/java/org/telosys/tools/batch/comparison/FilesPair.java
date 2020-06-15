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

public class FilesPair {

	private final File file1 ;
	private final File file2 ;

	public FilesPair(String file1FullPath, String file2FullPath) {
		super();
		this.file1 = new File(file1FullPath);
		this.file2 = new File(file2FullPath);
	}
	public FilesPair(File file1, File file2) {
		super();
		this.file1 = file1;
		this.file2 = file2;
	}
	
	public File getFile1() {
		return file1;
	}

	public File getFile2() {
		return file2;
	}

}
