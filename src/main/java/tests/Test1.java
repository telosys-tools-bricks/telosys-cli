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