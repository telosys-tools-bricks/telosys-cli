/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
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

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.TelosysToolsException;

public class ListModelsCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListModelsCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "lm";
	}

	@Override
	public String getShortDescription() {
		return "List Models" ;
	}

	@Override
	public String getDescription() {
		return "List the models";
	}
	
	@Override
	public String getUsage() {
		return "lm";
	}

	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			return listModels();
		}
		return null ;
	}

	private String listModels() {
		TelosysProject telosysProject = getTelosysProject();
		try {
			List<File> files = telosysProject.getModels();
			StringBuilder sb = new StringBuilder();
			for ( File f : files ) {
				appendLine(sb, " . " + f.getName() );
			}
			return sb.toString();
		} catch (TelosysToolsException e) {
			printError(e);
		}
		return null ;
	}

}
