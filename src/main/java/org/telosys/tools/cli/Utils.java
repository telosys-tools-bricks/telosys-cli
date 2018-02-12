/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
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

import java.util.LinkedList;

public class Utils {

	/**
	 * Private constructor
	 */
	private Utils() {
	}

	public static String[] getWords(String s) {
        if (s != null) {
        	LinkedList<String> words = new LinkedList<>();
    		String[] tokens = s.split("[ \t\r\n]");
    		for ( String t : tokens ) {
    			if ( t.length() > 0 ) {
    				words.add(t);
    			}
    		}
    		return words.toArray(new String[0]);
        }
        return new String[0]; 
	}
}
