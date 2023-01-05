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
package org.telosys.tools.cli.observer;

import org.telosys.tools.commons.StrUtil;

public class DbMetadataObserver extends AbstractObserver {

	private static boolean active = false ;
	
	/**
	 * Constructor
	 */
	public DbMetadataObserver() {
		super();
	}
		
	public static void setActive(boolean b) {
		active = b ;
	}

	public static boolean isActive() {
		return active ;
	}
	
	@Override
	public void notify(Integer level, String msg) {
		if ( ! active ) return ;
		
		int n = level < 10 ? level : 10 ;
		String s = StrUtil.repeat('.', n);
		print(s + msg );
	}
}
