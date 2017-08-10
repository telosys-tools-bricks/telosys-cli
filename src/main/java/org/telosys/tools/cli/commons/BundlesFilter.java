package org.telosys.tools.cli.commons;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.TelosysToolsException;

public class BundlesFilter {
	
	/**
	 * Builds a list of criteria from the given command line arguments <br>
	 * taking all the arguments from 1 to N (except 0)
	 * @param commandArgs 
	 * @return
	 */
	public final static List<String> buildCriteriaFromArgs( String[] commandArgs) {
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
		else if ( criteria.size() == 0 )  {
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
