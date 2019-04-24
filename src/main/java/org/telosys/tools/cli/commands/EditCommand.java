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

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;

public class EditCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}


	@Override
	public String getName() {
		return "e";
	}

	@Override
	public String getShortDescription() {
		return "Edit" ;
	}

	@Override
	public String getDescription() {
		return "Open an external editor";
	}
	
	@Override
	public String getUsage() {
		return "e [file-name]";
	}

	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			return edit(args[1]);
		}
		else {
			return edit("");
		}
	}

	private String edit(String shortFileName) {
		if (!StrUtil.nullOrVoid(shortFileName)) {
			String fileToBeEdited = FileUtil.buildFilePath(getCurrentDirectory(), shortFileName);
			return launchEditor(fileToBeEdited);
		} else {
			return launchEditor("");
		}
	}

}
