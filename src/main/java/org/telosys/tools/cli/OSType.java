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
package org.telosys.tools.cli;

public enum OSType {

	UNKNOWN(0, "Unknown"),
	
	LINUX(1, "Linux"),
	
	MACOS(2, "Mac-OS"),
	
	WINDOWS(3, "Windows");
	
	//---------------------------------------------------
	private final int    value ;
	private final String text  ;
	
	private OSType(int value, String text) {
		this.value = value ;
		this.text  = text ;
	}
	
	/**
	 * Returns the int value (0 to N) <br>
	 * @return
	 */
	public int getValue() {
		return this.value ;
	}
	
	/**
	 * Returns the text<br>
	 * e.g. : 'Linux', 'Windows', etc
	 * @return
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Returns true if the OS has a shell command
	 * @return
	 */
	public boolean hasShell() {
		if ( this == MACOS || this == LINUX ) return true ;
		return false ;
	}
	
	@Override
	public String toString() {
		return text ;
	}
}
