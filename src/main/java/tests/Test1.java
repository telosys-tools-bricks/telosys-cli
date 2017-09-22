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

import java.awt.Frame;
import java.awt.List;

public class Test1 {
 
	public static void main(String args[])  
	{  
        
        List list = new List(10);  
        list.setBounds(10,10, 200, 300);  
        list.add("Item 1");  
        list.add("Item 2");  
        list.add("Item 3");  
        list.add("Item 4");  
        list.add("Item 5");  
        
        list.setMultipleMode(true);
        
        Frame f= new Frame();  
        f.add(list);  
        f.setSize(400,400);
        //f.setLocation(200, 200);
        f.setLayout(null);  
        f.setVisible(true);  
	}  
}