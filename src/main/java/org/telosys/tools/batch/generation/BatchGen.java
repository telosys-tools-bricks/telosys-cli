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
package org.telosys.tools.batch.generation;

import org.telosys.tools.api.TelosysApiVersion;
import org.telosys.tools.generator.GeneratorVersion;
import org.telosys.tools.generic.model.GenericModelVersion;

public class BatchGen {

	private static void print(String s) {
		System.out.println(s);
	}
	
	private static void printVersions() {
		print("Versions : " );
		print(". Telosys API   : " + TelosysApiVersion.VERSION );
		print(". Generator     : " + GeneratorVersion.GENERATOR_VERSION );
		print(". Generic Model : " + GenericModelVersion.VERSION);
	}
	
	private static void printContext(String projectFullPath, String modelFileName, String bundlePattern, String destFolder) {
		print("Context : " );
		print(". project path   : '" + projectFullPath + "'" );
		print(". model file     : '" + modelFileName + "'" );
		print(". bundle pattern : '" + bundlePattern + "'" );
		print(". dest folder    : '" + destFolder + "' " );
	}
	
	private static final String LINE = "+------------------------+" ;
	
	public static void run(String projectFullPath, String modelFileName, String bundlePattern, String destFolder) {

		printVersions();
		printContext(projectFullPath, modelFileName, bundlePattern, destFolder);
		print("");
					
		print(LINE);
		print("| Starting Telosys-Batch | ");
		print(LINE);
		BatchGenResult r ;
		try {
			print("Creating launcher..." );
			BatchGenLauncher launcher = new BatchGenLauncher(projectFullPath, modelFileName);

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
		print(LINE);
		print("|  End of Telosys-Batch  | ");
		print(LINE);
		printContext(projectFullPath, modelFileName, bundlePattern, destFolder);
		print("Global result : " );
		print(" . number of bundles used      : " + r.getNumberOfBundlesUsed() );
		print(" . number of files generated   : " + r.getNumberOfFilesGenerated() );
		print(" . number of generation errors : " + r.getNumberOfGenerationErrors() );
		print(" . number of resources copied  : " + r.getNumberOfResourcesCopied() );
		print("Results by bundle : " );
		for ( String s : r.getBundlesStatus() ) {
			print(s);
		}
	}
	
}
