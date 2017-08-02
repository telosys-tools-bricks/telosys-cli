package org.telosys.tools.cli.commons;

import java.util.LinkedList;
import java.util.List;

public class BundlesFilter {
	
	/**
	 * Builds a list of criteria from the given command line arguments
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

}
