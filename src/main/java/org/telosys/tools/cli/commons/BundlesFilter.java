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
package org.telosys.tools.cli.commons;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.TelosysToolsException;

public class BundlesFilter {
	
	/**
	 * Private constructor
	 */
	private BundlesFilter() {	
	}
	
	/**
	 * Builds a list of criteria from the given command line arguments <br>
	 * taking all the arguments from 1 to N (except 0)
	 * @param commandArgs 
	 * @return
	 */
	public static final List<String> buildCriteriaFromArgs( String[] commandArgs) {
		List<String> tokens = new LinkedList<>();
		for ( int i = 1 ; i < commandArgs.length ; i++ ) {
				tokens.add(commandArgs[i]);
		}
		return tokens ;
	}

	/**
	 * Filter the given list with the given criteria
	 * @param bundles
	 * @param criteria
	 * @return
	 */
	public static List<String> filter(List<String> bundles, List<String> criteria) {

		if ( criteria == null )  {
			// No criteria => no filter => return the same list
			return bundles ;
		}
		else if ( criteria.isEmpty() )  {
			// No criteria => no filter => return the same list
			return bundles ;
		}
		else if ( criteria.size() == 1 && "*".equals(criteria.get(0)) )  {
			// 1 criteria = "*" => no filter => return the same list
			return bundles ;
		}
		else {
			// Filter : check bundle name contains at least 1 of the given criteria
			List<String> result = new LinkedList<>();
			for ( String bundle : bundles ) {
				for ( String s : criteria ) {
					if ( bundle.contains(s) ) {
						if ( ! result.contains(bundle) ) {
							result.add(bundle);
						}
					}
				}
			}
			return result;
		}
	}

	/**
	 * Return a list with all the existing bundles matching the given command arguments 
	 * @param telosysProject
	 * @param commandArgs
	 * @return
	 * @throws TelosysToolsException
	 */
	public static List<String> getExistingBundles(TelosysProject telosysProject, String[] commandArgs) throws TelosysToolsException {
		List<String> criteria = BundlesFilter.buildCriteriaFromArgs(commandArgs);
		// get all installed bundles
		List<String> bundles = telosysProject.getInstalledBundles();
		// filter with criteria if any
		return filter(bundles, criteria);
	}

}
