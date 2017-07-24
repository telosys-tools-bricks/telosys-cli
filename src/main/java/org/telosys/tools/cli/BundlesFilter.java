package org.telosys.tools.cli;

import java.util.LinkedList;
import java.util.List;

public class BundlesFilter {
	
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
