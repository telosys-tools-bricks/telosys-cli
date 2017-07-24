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