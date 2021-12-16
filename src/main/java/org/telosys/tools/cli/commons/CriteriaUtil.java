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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;

public class CriteriaUtil {

	/**
	 * No constructor ! 
	 */
	private CriteriaUtil() {
	}
	
	/**
	 * Builds a list of criteria from the given argument <br>
	 * Returns a list of criteria or null if no criteria <br>
	 * 
	 * @param arg argument containing the criteria ( eg 'pattern1,pattern2,pattern3' )
	 * @return
	 */
	public static final List<String> buildCriteriaFromArg( String arg ) {
		if ( arg == null ) {
			return null ; // No criteria
		}
		else if ( "*".equals(arg) || "".equals(arg) ) {
			return null ; 
		}
		else {
			List<String> list = new LinkedList<>();
			if ( arg.contains(",") ) {
				// Many template patterns (eg 'template1,template2,template3')				
				String[] array = StrUtil.split(arg, ',' );
				for ( String s : array ) {
					String templateName = s.trim();
					if ( templateName.length() > 0 ) {
						list.add(templateName);
					}
				}
			}
			else {
				// Only 1 template pattern 
				list.add(arg.trim());
			}
			return list;
		}
	}

	/**
	 * Select the strings matching the given criteria
	 * @param strings 
	 * @param criteria
	 * @return
	 */
	public static List<String> select( List<String> strings, List<String> criteria) {
		if ( criteria != null ) {
			List<String> list = new LinkedList<>();
			for ( String criterion : criteria ) {
				for ( String s : strings ) {
					if ( s.contains(criterion) ) {
						list.add(s); 
					}
				}				
			}
			return list;	
		}
		else {
			return strings ; // No critera => ALL
		}
	}

	/**
	 * Select and sort the given strings according with the given criteria
	 * @param strings
	 * @param criteria
	 * @return
	 */
	public static List<String> selectAndSort( List<String> strings, List<String> criteria ) {
		List<String> list = select(strings, criteria);
		Collections.sort(list);
		return list;
	}

}
