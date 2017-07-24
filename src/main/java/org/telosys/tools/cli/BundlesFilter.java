package org.telosys.tools.cli;

import java.util.LinkedList;
import java.util.List;

public class BundlesFilter {
	
	public static List<String> filter(List<String> bundles, List<String> criteria) {

		if ( criteria != null && criteria.size() > 0 )  {
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
		else {
			// No criteria => no filter => return the same list
			return bundles ;
		}
	}

}
