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

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.CriteriaUtil;
import org.telosys.tools.cli.commons.TargetUtil;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;

import jline.console.ConsoleReader;

/**
 * Lists the templates for the current bundle
 * 
 * @author Laurent GUERIN
 *
 */
public class ListTemplatesCommand extends CommandLevel2 {
	
	public static final String COMMAND_NAME = "lt";

	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public ListTemplatesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return COMMAND_NAME ;
	}

	@Override
	public String getShortDescription() {
		return "List Templates" ;
	}

	@Override
	public String getDescription() {
		return "List the templates for the current bundle";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " [*|pattern|pattern1,pattern2,...]";
	}
	
	@Override
	public String execute(String[] argsArray) {
		if ( checkHomeDirectoryDefined() && checkCurrentBundleDefinedAndExists() ) {
			List<String> commandArguments = getArgumentsAsList(argsArray);
			if ( checkArguments(commandArguments, 0, 1) && checkHomeDirectoryDefined() && checkBundleDefined() ) {
				String patterns = commandArguments.isEmpty() ? null : commandArguments.get(0) ;
				listTemplates(patterns);
			}
		}
		return null ;
	}
	
	private void listTemplates(String patterns) {
		// Get all templates for the current bundle
		TargetsDefinitions targetDefinitions = getCurrentTargetsDefinitions();
		// Apply filter with criteria
		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(patterns) ;
		List<TargetDefinition> selectedTargets = TargetUtil.filter(targetDefinitions.getTemplatesTargets(), criteria);
		print ( TargetUtil.buildListAsString(selectedTargets) );
	}
}
