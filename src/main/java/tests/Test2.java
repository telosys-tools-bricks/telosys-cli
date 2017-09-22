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
package tests;

import java.util.LinkedList;
import java.util.List;


public class Test2 {
 
	public static void main( String args[] ) {
		
		List<String> items = new LinkedList<>();
		for ( int i = 0 ; i < 20 ; i++ ) {
			items.add("Item #" + i + " aazezaazz");
		}
		SelectionList window = new SelectionList(items);
		//list.showListDemo(items);
		window.show();
	}  
}