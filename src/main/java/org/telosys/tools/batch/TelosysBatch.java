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

import org.telosys.tools.api.TelosysApiVersion;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorVersion;
import org.telosys.tools.generic.model.GenericModelVersion;

public class TelosysBatch {

	public static void main(String[] args) {
		
		print("Telosys-Batch" );
		if ( args.length < 3 ) {
			print("Invalid usage : 3 or 4 arguments expected");
			print("Usage : ") ;
			print("  arg #1 : project path " ) ;
			print("  arg #2 : model file in project  " ) ;
			print("  arg #3 : bundle name pattern (unique name, part of names, '*' for all) " ) ;
			print("  arg #4 : destination folder (this argument is optional) " ) ;
			print("           (full path expected, it can contain ${VER} and ${BUNDLE}) " ) ;
			return;
		}

		String projectFullPath = args[0] ;
		String modelFileName = args[1] ;
		String bundlePattern = args[2] ;
		String destFolder = null;
		if ( args.length > 3 ) {
			destFolder = args[3];
		}
		
		// Force values for tests 
		projectFullPath = "D:/workspaces-TELOSYS-TOOLS/wks-46-telosys-tools-bricks/test-telosys-cli/project";
		modelFileName = "foo.model" ;
		bundlePattern = "*";
//		bundlePattern = "java-domain-T300";
//		bundlePattern = "python";
//		bundlePattern = "java";
//		bundlePattern = "csharp";
		destFolder = "D:/TMP/TMPTMP/TELOSYS-BATCH-${VER}/BUNDLE-${BUNDLE}" ;
		
		if ( destFolder != null ) {
			destFolder = StrUtil.replaceVar(destFolder, "${VER}", GeneratorVersion.GENERATOR_VERSION );
		}
		run(projectFullPath, modelFileName, bundlePattern, destFolder);
	}
	
	public static void print(String s) {
		System.out.println(s);
	}
	
	public static void printStatus(String projectFullPath, String modelFileName, String bundlePattern, String destFolder) {
		print("Versions : " );
		print(" . Telosys API   : " + TelosysApiVersion.VERSION );
		print(" . Generator     : " + GeneratorVersion.GENERATOR_VERSION );
		print(" . Generic Model : " + GenericModelVersion.VERSION);
		print("Context : " );
		print(" . project path   : '" + projectFullPath + "'" );
		print(" . model file     : '" + modelFileName + "'" );
		print(" . bundle pattern : '" + bundlePattern + "'" );
		print(" . dest folder    : '" + destFolder + "' " );
	}
	
	public static void run(String projectFullPath, String modelFileName, String bundlePattern, String destFolder) {

		printStatus(projectFullPath, modelFileName, bundlePattern, destFolder);
		print("");
					
		print("+------------------------+ ");
		print("| Starting Telosys-Batch | ");
		print("+------------------------+ ");
		TelosysBatchResult r ;
		try {
			print("Creating launcher..." );
			TelosysLauncher launcher = new TelosysLauncher(projectFullPath, modelFileName);

			print("Initializing launcher..." );
			launcher.init(destFolder);
			
			print("Launching code generation..." );
			print("");
			r = launcher.launchGeneration(bundlePattern);
		} catch (Exception e) {
			print("ERROR :");
			print(" " + e.getMessage());
			return;
		}
		print("");
		print("+------------------------+ ");
		print("|  End of Telosys-Batch  | ");
		print("+------------------------+ ");
		printStatus(projectFullPath, modelFileName, bundlePattern, destFolder);
		print("Result : " );
		print(" . number of bundles used      : " + r.getNumberOfBundlesUsed() );
		print(" . number of files generated   : " + r.getNumberOfFilesGenerated() );
		print(" . number of generation errors : " + r.getNumberOfGenerationErrors() );
		print(" . number of resources copied  : " + r.getNumberOfResourcesCopied() );
	}
	
}
