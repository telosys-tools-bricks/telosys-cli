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
package org.telosys.tools.cli.commands;

import java.io.File;
import java.util.List;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Const;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.FileFinder;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;

import jline.console.ConsoleReader;

/**
 * 'et' command
 * 
 * @author Laurent GUERIN
 *
 */
public class EditTemplateCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditTemplateCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "et";
	}

	@Override
	public String getShortDescription() {
		return "Edit Template" ;
	}

	@Override
	public String getDescription() {
		return "Edit a template (.vm) file";
	}
	
	@Override
	public String getUsage() {
		return "et template-name";
	}

	@Override
	public String execute(String[] args) {
		if ( checkBundleDefined() ) {
			if ( args.length > 1 ) {
				return editTemplate(args[1]);
			}
			else {
				return invalidUsage("template-name expected");
			}
		}
		return null ;
	}
	
	private File getBundleDirectory(String bundleName) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();

		File bundleConfigFile = telosysProject.getBundleConfigFile(bundleName);
		return bundleConfigFile.getParentFile();
	}
	
	private File buildTemplateFile(String bundleName, String templateName) throws TelosysToolsException {
		File bundleDir = getBundleDirectory(bundleName);
		String fileName = templateName ;
		if ( ! fileName.endsWith(Const.DOT_VM) ) {
			fileName = templateName + Const.DOT_VM ;
		}
		return new File( FileUtil.buildFilePath(bundleDir.getAbsolutePath(), fileName) );
	}
	
	/**
	 * Edit the TEMPLATE FILE  ( eg xxxx.vm ) 
	 * @param templateName
	 * @return
	 */
	private String editTemplate(String templateName) {
		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
			try {
//				String bundleName = getCurrentBundle();
//				File bundleConfigFile = telosysProject.getBundleConfigFile(bundleName);
//				File bundleDir = bundleConfigFile.getParentFile();
//				
////				File file = telosysProject.buildDslEntityFile(getCurrentModel(), templateName);
//				String fileName = templateName ;
//				if ( ! fileName.endsWith(Const.DOT_VM) ) {
//					fileName = templateName + Const.DOT_VM ;
//				}
//				File file = new File( FileUtil.buildFilePath(bundleDir.getAbsolutePath(), fileName) );
				File file = buildTemplateFile(getCurrentBundle(), templateName);
				
				if ( file.exists() && file.getName().equals(templateName) ) { // Check name for exact matching (case sensitive)
					return launchEditor(file.getAbsolutePath() );
				}
				else {
					// Try to find the file with abbreviation
					File dir = file.getParentFile() ;
					List<File> files = FileFinder.find(templateName, dir, Const.DOT_VM);
					if ( files.isEmpty() ) {
						print("No template '" + templateName + "' in the current bundle.");
					}
					else if ( files.size() == 1 ) {
						return launchEditor(files.get(0).getAbsolutePath() ) ;
					}
					else {
						print("Ambiguous name '" + templateName + "' (" + files.size() + " files found).");
					}
				}
			} catch (TelosysToolsException e) {
				printError(e);
			}
		}
		return null ;
	}

}
