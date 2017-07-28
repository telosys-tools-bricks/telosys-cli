package tests;


public class ThrowException {
	
	public static void main(String args[])  { 
		err(args);
	}
	
	public static void err(String args[])  {  
		System.out.println("Throw exception");
		System.out.println( args[456] );
	}  
	
	
}