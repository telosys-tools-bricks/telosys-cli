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

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;

public class InitCommand  extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public InitCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "init";
	}

	@Override
	public String getShortDescription() {
		return "Initialization" ;
	}
	
	@Override
	public String getDescription() {
		return "Init the Telosys directory with all required files";
	}
	
	@Override
	public String getUsage() {
		return "init";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			TelosysProject telosysProject = getTelosysProject();
			return telosysProject.initProject();
		}
		return null ;
	}
}
