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

import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.TelosysToolsException;

public class GitHubBundlesUtil {
	
	/**
	 * Returns a list of bundles matching the given criteria
	 * @param telosysProject
	 * @param githubStoreName
	 * @param criteria
	 * @return
	 * @throws TelosysToolsException
	 */
	public final static List<String> getBundles(TelosysProject telosysProject, String githubStoreName, List<String> criteria) throws TelosysToolsException {
		
		// Get all the bundles available in the GitHub store
		List<String> bundles = telosysProject.getGitHubBundlesList(githubStoreName);
		if ( bundles != null ) {
			// Filter with criteria
			return BundlesFilter.filter(bundles, criteria);
		}
		else {
			return null ;
		}
	}	
}
