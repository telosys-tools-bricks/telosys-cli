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

import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.variables.Variable;

import jline.console.ConsoleReader;

/**
 * 'cfg' command 
 * 
 * @author Laurent GUERIN
 *
 */
public class CfgCommand extends CommandLevel2 {
	
	/**
	 * Constructor
	 * @param out
	 */
	public CfgCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "cfg";
	}

	@Override
	public String getShortDescription() {
		return "Configuration" ;
	}

	@Override
	public String getDescription() {
		return "Print project configuration (folders, variables, etc.)";
	}
	
	@Override
	public String getUsage() {
		return "cfg";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			printConfiguration(getTelosysProject().getTelosysToolsCfg());
		}
		return null ;
	}

	private void printConfiguration(TelosysToolsCfg cfg) {

		print("Current project configuration (from file 'telosys-tools.cfg')");
		
		print("FOLDERS (standard variables) : ");
		print(". Sources         ${SRC}      : " + cfg.getSRC() );
		print(". Resources       ${RES}      : " + cfg.getRES() );
		print(". Web content     ${WEB}      : " + cfg.getWEB());
		print(". Tests sources   ${TEST_SRC} : " + cfg.getTEST_SRC());
		print(". Tests resources ${TEST_RES} : " + cfg.getTEST_RES());
		print(". Documentation   ${DOC}      : " + cfg.getDOC());
		print(". Temporary files ${TMP}      : " + cfg.getTMP());
		
		print("PACKAGES (standard variables) : ");
		print(". Root package     ${ROOT_PKG}   : " + cfg.getRootPackage());
		print(". Entities package ${ENTITY_PKG} : " + cfg.getEntityPackage());
		
		// Project Specific Variables
		print("SPECIFIC PROJECT VARIABLES : ");
		for ( Variable var : cfg.getSpecificVariables() ) {
			print(". " +  var.getSymbolicName() + " : " + var.getValue());
		}
		
		// Advanced configuration
		print("SPECIFIC LOCATIONS : ");
		print(". Templates folder              : " + cfg.getSpecificTemplatesFolderAbsolutePath() );
		print(". Generation destination folder : " + cfg.getSpecificDestinationFolder() );
		
	}
}
