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
package org.telosys.tools.cli;

import java.io.IOException;
import java.util.Properties;

/**
 * Telosys-CLI version provider
 *  
 * New: since ver 4.1.0 version and build id are provided via a properties file
 * updated at each Maven build using resources filtering
 * 
 * @author Laurent Guerin
 *
 */
public class Version {

	/**
	 * Property name to get the VERSION from properties file
	 */
	private static final String PROJECT_VERSION = "project.version" ;
	
	/**
	 * Property name to get the BUILD-ID from properties file
	 */
	private static final String BUILD_ID = "project.build" ;

	private static String error = null ;
	
	
	/**
	 * Private constructor
	 */
	private Version() {}
	
	/**
	 * Returns the Maven 'project.version' and 'build.id' <br>
	 * @return
	 */
	public static final String getVersionWithBuilId() {
		String version = getVersion() ;
		if ( error != null ) { // test error on first access
			return error;
		}
		else {
			return version + " (build: " + getBuildId() + ")";
		}
	}
	
	/**
	 * Returns the Maven 'project.version' 
	 * @return
	 */
	public static final String getVersion() {
		return getProperty(PROJECT_VERSION, "unknown version");
	}

	/**
	 * Returns the Maven 'build.id' 
	 * @return
	 */
	public static final String getBuildId() {
		return getProperty(BUILD_ID, "unknown build");
	}

	public static final String getProperty(String propertyName, String defaultValue) {
	    Properties properties = getBuildProperties();
	    if ( error != null ) {
	    	// error detected => return error message instead
		    return error;
	    }
	    else {
	    	// ok: properties found and loaded
		    return properties.getProperty(propertyName, defaultValue);	
	    }
	}

	private static Properties properties = null ;

	public static final Properties getBuildProperties() {
		if ( properties != null ) {
			// already loaded
			return properties ;
		}
		else {
			// not loaded yet
			properties = loadBuildProperties();
			return properties ;
		}
	}
	
	public static final Properties loadBuildProperties() {
	    Properties properties = new Properties();
	    try {
	    	error = null;
	    	properties.load(Version.class.getResourceAsStream("/telosys-cli-build.properties"));
	    } 
	    catch (IOException e) {	    	
	    	error = "ERROR: cannot load build properties, error: " + e.getMessage() ;
	    }
    	return properties;
	}
}
