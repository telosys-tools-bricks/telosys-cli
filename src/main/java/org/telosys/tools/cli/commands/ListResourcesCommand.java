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

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;

/**
 * Lists the resources for the current bundle
 * 
 * @author Laurent GUERIN
 *
 */
public class ListResourcesCommand extends Command {
	
	/**
	 * Constructor
	 * @param consoleReader
	 * @param environment
	 */
	public ListResourcesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "lr";
	}

	@Override
	public String getShortDescription() {
		return "List Resources" ;
	}

	@Override
	public String getDescription() {
		return "List the resources defined for the current bundle";
	}
	
	@Override
	public String getUsage() {
		return "lr";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() && checkBundleDefined() ) {
			listResources();
		}
		return null ;
	}
	
	private void listResources() {
		TargetsDefinitions targetDefinitions = getCurrentTargetsDefinitions();
		List<TargetDefinition> resources = targetDefinitions.getResourcesTargets();
		if ( resources.isEmpty() ) {
			print( "No resource" ) ; 
		}
		else {
			for ( TargetDefinition td : resources ) {
				String dest = "" ;
				String folder = td.getFolder() ;
				String file = td.getFile();
				if ( ! StrUtil.nullOrVoid( folder ) && ! StrUtil.nullOrVoid(file) ) {
					dest = folder + " / " + file ;
				}
				else {
					if ( ! StrUtil.nullOrVoid( folder ) ) {
						dest = folder ;
					}
					if ( ! StrUtil.nullOrVoid( file ) ) {
						dest = file ;
					}
				}
				print( " . " + td.getTemplate() + " -> " + dest  ) ; 
			}
		}
	}
}
