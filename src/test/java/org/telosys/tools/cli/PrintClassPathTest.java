package org.telosys.tools.cli;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;

public class PrintClassPathTest {

	private void printClassPath(ClassLoader classLoader ) {
		System.out.println("----------" );
		// NB : Class cast exception with Java > 9
		URLClassLoader urlClassLoader = (URLClassLoader) classLoader; 
		int n = 0;
		for ( URL url : urlClassLoader.getURLs() ) {
			System.out.println(" . " + url.getFile() );
			n++;
		}
		System.out.println(" n =  " + n);
	}
	
	@Test
	public void test1() {
		//String classpath = System.getProperties().get("java.class.path");
		
		String classpath = System.getProperty("java.class.path");
		System.out.println(classpath);
		String pathSeparator = System.getProperty("path.separator");
		System.out.println(pathSeparator);
		
		String[] classpathEntries = classpath.split(pathSeparator);
		int n = 0;
		for ( String s : classpathEntries ) {
			System.out.println(" . " + s );
			n++;
		}
		System.out.println(" n =  " + n);
	}
	
	@Test
	public void test2() {
		printClassPath(this.getClass().getClassLoader());
	}
	
	@Test
	public void test3() {
		printClassPath(ClassLoader.getSystemClassLoader());
	}
	
}
