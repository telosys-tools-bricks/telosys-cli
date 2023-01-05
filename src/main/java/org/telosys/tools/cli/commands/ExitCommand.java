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

public class ExitCommand extends Command {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public ExitCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "exit";
	}

	@Override
	public String getShortDescription() {
		return "Exit" ;
	}
	
	@Override
	public String getDescription() {
		return "Exit Telosys command line interface";
	}

	@Override
	public String getUsage() {
		return "exit";
	}

	@Override
	public String execute(String[] args) {
		print("bye...");
		System.exit(0);
		return "";
	}

}
