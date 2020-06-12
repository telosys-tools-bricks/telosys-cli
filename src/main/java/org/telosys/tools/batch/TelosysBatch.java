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
package org.telosys.tools.batch;

import java.util.Properties;

import org.telosys.tools.batch.generation.BatchGen;
import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.PropertiesManager;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorVersion;

public class TelosysBatch {
	
	private static final String PROPERTIES_FILE = "telosys-batch.properties" ;
	
	private static int configErrors = 0 ;
	
	private static void print(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) {
		
		print("Telosys-Batch" );
		if ( args.length < 1 ) {
			print("Invalid usage : 'command' argument expected");
			print("usable commands : ") ;
			print("  'gen' : to launch code generation for multiple bundles " ) ;
			print("  'cmp' : to launch multiple files comparison " ) ;
			return;
		}

		// Current working directory 
		String workingDir = DirUtil.getUserWorkingDirectory() ;
		print("Working directory : " + workingDir );
		
		// Batch configuration in properties file
		String propertiesFile = FileUtil.buildFilePath(workingDir, PROPERTIES_FILE );
		PropertiesManager pm = new PropertiesManager(propertiesFile);
		Properties p = pm.load();
		if ( p == null ) {
			print("ERROR : Properties file '" + PROPERTIES_FILE + "' not found");
			return;
		}
		
		String command = args[0] ;
		if ( "gen".equals(command) ) {
			gen(p);
		}
		else if  ( "cmp".equals(command) ) {
			cmp(p);
		}
		else {
			print("ERROR : invalid command '" + command + "'");
		}
	}
	
	private static String getProperty(Properties p, String name, boolean mandatory) {
		String s = p.getProperty(name);
		if ( s != null ) {
			return s.trim();
		} else {
			if ( mandatory ) {
				configErrors++;
				print("ERROR: property '" + name +"' not defined in '" + PROPERTIES_FILE + "'");
			}
		}
		return s;
	}
	
	/**
	 * 'gen' command
	 * @param p
	 */
	private static void gen(Properties p) {
		String projectFullPath = getProperty(p, "gen.projectFullPath", true);
		String modelFileName = getProperty(p, "gen.modelFileName", true);
		String bundlesPattern = getProperty(p, "gen.bundlesPattern", true);
		String destDir1 = getProperty(p, "gen.destDir", false);
		String destDir2 = destDir1 ;
		if ( destDir1 != null ) {
			destDir2 = StrUtil.replaceVar(destDir1, "${VER}", GeneratorVersion.GENERATOR_VERSION );
		}
		if ( configErrors > 0 ) {
			return ;
		}
		print("Batch code generation properties : " );
		print(" . project path   : '" + projectFullPath + "'" );
		print(" . model file     : '" + modelFileName + "'" );
		print(" . bundle pattern : '" + bundlesPattern + "'" );
		print(" . dest directory : '" + destDir1 + "' " );
		print("                    '" + destDir2 + "' " );
		
		BatchGen.run(projectFullPath, modelFileName, bundlesPattern, destDir2);
	}	
	
	/**
	 * 'cmp' command
	 * @param p
	 */
	private static void cmp(Properties p) {
		print("'cmp' command : not yet implemented" );
	}
}
