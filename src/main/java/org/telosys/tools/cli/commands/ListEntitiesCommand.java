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
import org.telosys.tools.dsl.DslModelUtil;

import jline.console.ConsoleReader;

public class ListEntitiesCommand extends CommandLevel2 {

	public static final String COMMAND_NAME = "le";

	/**
	 * Constructor
	 * @param out
	 */
	public ListEntitiesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return COMMAND_NAME ;
	}

	@Override
	public String getShortDescription() {
		return "List Entities" ;
	}

	@Override
	public String getDescription() {
		return "List the entities defined in the current model";
	}
	
	@Override
	public String getUsage() {
		return COMMAND_NAME + " [*|pattern|pattern1,pattern2,...]";
	}
	
	@Override
	public String execute(String[] argsArray) {
		List<String> commandArguments = getArgumentsAsList(argsArray);
		
		if ( checkArguments(commandArguments, 0, 1) && checkHomeDirectoryDefined() && checkModelDefined() ) {
			//listEntities(args);
			String patterns = commandArguments.isEmpty() ? null : commandArguments.get(0) ;
			listEntities(patterns);
		}
		return null ;		
	}
	
//	private void listEntities(String[] args) {
//		// Get all entities for the current model
//		List<String> entities = DslModelUtil.getEntityNames(getCurrentModelFolder());
//		// Apply filter with criteria
//		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(args.length > 1 ? args[1] : null) ;
//		List<String> result = CriteriaUtil.selectAndSort(entities, criteria);
//		if ( result.isEmpty() ) {
//			print("No entity");
//		}
//		else {
//			printList( CriteriaUtil.selectAndSort(entities, criteria), " . " );
//		}
//	}
	private void listEntities(String patterns) {
		// Get all entities for the current model
		List<String> entities = DslModelUtil.getEntityNames(getCurrentModelFolder());
		// Apply filter with criteria
		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(patterns) ;
		List<String> result = CriteriaUtil.selectAndSort(entities, criteria);
		if ( result.isEmpty() ) {
			print("No entity");
		}
		else {
			printList(result);
		}
	}
	
//	protected void printList(List<String> list, String prefix) {
//		for ( String s : list) {
//			print(prefix + s );
//		}
//	}
}
