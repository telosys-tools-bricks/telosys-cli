package org.telosys.tools.cli.commons;

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
	public final static List<String> buildCriteriaFromArg( String arg ) {
		if ( arg == null ) {
			return null ; // No criteria
		}
		else if ( "*".equals(arg) || "".equals(arg) ) {
			return null ; 
		}
		else {
			List<String> list = new LinkedList<String>();
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
	
}
