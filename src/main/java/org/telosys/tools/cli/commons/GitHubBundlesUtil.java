package org.telosys.tools.cli.commons;

import java.util.LinkedList;
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
		List<String> bundles = telosysProject.getBundlesList(githubStoreName);
		if ( bundles != null ) {
			// Filter with criteria
			return BundlesFilter.filter(bundles, criteria);
		}
		else {
			return null ;
		}
	}	
	
//	/**
//	 * Returns all the bundles available in the given store
//	 * @param githubStoreName
//	 * @return
//	 */
//	private final static List<String> getAllBundles(TelosysProject telosysProject, String githubStoreName) throws TelosysToolsException {
//		return telosysProject.getBundlesList(githubStoreName);
//	}
	
	/**
	 * Builds a list of criteria to filter the bundles
	 * @param commandArgs 
	 * @return
	 */
	public final static List<String> buildCriteria( String[] commandArgs) {
		List<String> tokens = new LinkedList<>();
		for ( int i = 1 ; i < commandArgs.length ; i++ ) {
//			if ( ! ( "-l".equals(args[i]) ) ) {
				tokens.add(commandArgs[i]);
//			}
		}
		return tokens ;
	}
	
}
