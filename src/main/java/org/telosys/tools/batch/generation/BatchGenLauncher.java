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

import java.io.File;
import java.util.List;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.BundlesNames;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generic.model.Model;

public class BatchGenLauncher {

	private final String projectFullPath ;
	private final String modelFileName ;
	
	private TelosysProject telosysProject ;
	private Model model ;
	private String specificDestinationFolder ; 
	
	/**
	 * Constructor 
	 * @param projectFullPath
	 * @param modelFileName
	 */
	public BatchGenLauncher(String projectFullPath, String modelFileName ) {
		this.projectFullPath = projectFullPath ;
		this.modelFileName = modelFileName ;
	}

	/**
	 * Initialize the launcher
	 */
	public void init() {
		init(null);
	}

	/**
	 * Initialize the launcher with a specific destination folder
	 * @param specificDestinationFolder
	 */
	public void init(String specificDestinationFolder) {
		this.telosysProject = initTelosysProject();
		this.model = initModel();
		this.specificDestinationFolder = specificDestinationFolder;
	}

	private TelosysProject initTelosysProject() {
		File file = new File(projectFullPath);
		if ( file.exists() && file.isDirectory() ) {
			return new TelosysProject(projectFullPath);
		}
		else {
			throw new RuntimeException("Invalid project folder : " + projectFullPath );
		}
		
		//telosysProject.getTelosysToolsCfg().
	}

	private Model initModel() {
		try {
			return telosysProject.loadModel(modelFileName);
		} catch (TelosysModelException tme) {
			throw new RuntimeException("Cannot load model (TelosysModelException): " + tme.getMessage() );
		} catch (TelosysToolsException ex) {
			throw new RuntimeException("Cannot load model (TelosysToolsException): " + ex.getMessage() );
		}		
	}
	
	private void checkInit() {
		if ( telosysProject == null || model == null ) {
			throw new RuntimeException("Launcher is not initialized");
		}
	}
	
	/**
	 * Launch generation for the bundles matching the given pattern 
	 * @param bundlePattern : can be a single bundle name, '*' for all, a part of bundles names 
	 * @return
	 */
	public BatchGenResult launchGeneration(String bundlePattern) {
		checkInit();
		BundlesNames bundlesNames ;
		try {
			bundlesNames = telosysProject.getInstalledBundles();
		} catch (TelosysToolsException e) {
			throw new RuntimeException("Cannot get installed bundles : " + e.getMessage() );
		}
		
		// get bundles matching the given pattern
		String[] parts = new String[2];
		parts[0] = "" ;
		parts[1] = bundlePattern ;
		List<String> selectedBundleNames = bundlesNames.filter(parts);

		// Launch generation for each bundle
		BatchGenResult batchResult = new BatchGenResult();
//		int n = 0 ;
		for (String bundleName : selectedBundleNames ) {
			//n = n + launchBundleGeneration(bundleName); 
			GenerationTaskResult generationTaskResult = launchBundleGeneration(bundleName); 
			batchResult.update(bundleName, generationTaskResult);
		}
		return batchResult ;
	}
	
	/**
	 * Launch generation for the given bundle name
	 * @param bundleName
	 */
	private GenerationTaskResult launchBundleGeneration(String bundleName) {
		checkInit();
		
		// Set specific destination folder if any
		if ( specificDestinationFolder != null ) {
			// Replace ${BUNDLE} variable if any 
			String newDestinationFolder = StrUtil.replaceVar(specificDestinationFolder, "${BUNDLE}", bundleName);
			// Set the new destination folder for this bundle generation 
			try {
				telosysProject.getTelosysToolsCfg().setSpecificDestinationFolder(newDestinationFolder);
			} catch (TelosysToolsException e) {
				throw new RuntimeException("Cannot set destination folder (TelosysToolsException): " + e.getMessage() );
			}
		}
		
		// Launch code generation 
		try {
			return telosysProject.launchGeneration(model, bundleName);
		} catch (TelosysToolsException e) {
			throw new RuntimeException("Generation error (TelosysToolsException): " + e.getMessage() );
		}
	}
	
}
