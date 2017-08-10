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
