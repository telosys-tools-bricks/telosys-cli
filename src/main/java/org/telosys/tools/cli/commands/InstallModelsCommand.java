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

import java.util.List;

import org.telosys.tools.api.InstallationType;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commands.commons.DepotAbstractCommand;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.DepotResponse;

import jline.console.ConsoleReader;

/**
 * Install Model(s) 
 * Examples : 
 *   im *    --> install all models from the depot 
 *   im car  --> install all the models containing "car"
 * 
 * @author Laurent GUERIN
 *
 */
public class InstallModelsCommand extends DepotAbstractCommand {

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public InstallModelsCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "im";
	}

	@Override
	public String getShortDescription() {
		return "Install Model(s)";
	}

	@Override
	public String getDescription() {
		return "Install model(s) from the depot ";
	}

	@Override
	public String getUsage() {
		return "im [name-part1 name-part2 ...]";
	}

	@Override
	public String execute(String[] args) {

		if (args.length > 1) {
			// im aaa bbb
			if ( checkHomeDirectoryDefined() ) {
				install(args);
			}
			return null;
		}
		else {
			// no argument 
			return "Invalid usage";
		}
	}

	private void install(String[] args) {
		// SUGGESTION: if already exists : prompt "overwrite ? [y/n] : "
		String depot = getTelosysProject().getTelosysToolsCfg().getDepotForModels(); 
		try {
			DepotResponse depotResponse = getTelosysProject().getModelsAvailableInDepot(depot); 
			if (isDepotResponseOK(depotResponse)) {
				List<String> criteria = buildCriteriaFromArgs(args);
				filterAndInstallSearchResult("model", depot, depotResponse, criteria, InstallationType.MODEL);
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}		
	}

}
