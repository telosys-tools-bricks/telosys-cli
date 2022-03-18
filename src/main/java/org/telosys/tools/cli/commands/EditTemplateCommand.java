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
	
	/**
	 * Edit the TEMPLATE FILE  ( eg xxxx.vm ) 
	 * @param templateName
	 * @return
	 */
	private String editTemplate(String templateName) {
		TelosysProject telosysProject = getTelosysProject();
		if ( telosysProject != null ) {
			File bundleFolder = telosysProject.getBundleFolder(getCurrentBundle());
			// Try to find one or more files matching the given name or part of name
			FileFinder fileFinder = new FileFinder(bundleFolder, Const.DOT_VM);
			List<File> files = fileFinder.find(templateName);
			if ( files.isEmpty() ) {
				print("No template matching '" + templateName + "' in the current bundle.");
			}
			else if ( files.size() == 1 ) {
				return launchEditor(files.get(0).getAbsolutePath() ) ;
			}
			else {
				print("Ambiguous name '" + templateName + "' (" + files.size() + " templates found).");
			}
		}
		return null ;
	}
	
}
