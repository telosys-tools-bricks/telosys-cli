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
package org.telosys.tools.cli;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.cli.commands.GenBatchCommand;

public class CommandsGroups {

	
	private final List<CommandsGroup> all ;
		
	public CommandsGroups() {
		super();
		all = new LinkedList<>();
		
		all.add(getGlobalCommands()) ;
		all.add(getProjectCommands()) ;
		all.add(getDatabaseCommands()) ;
		all.add(getModelCommands()) ;
		all.add(getEntityCommands()) ;
		all.add(getBundleCommands()) ;
		all.add(getTemplateCommands()) ;
		all.add(getGenerationCommands()) ;
		all.add(getGitHubCommands()) ;
	}

	public final List<CommandsGroup> getAll() {
		return all ;
	}
	
	private final CommandsGroup getGlobalCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "General commands" ;
		commands.add("?");
		commands.add("cd");
		commands.add("ls");
		commands.add("mkdir");
		commands.add("pwd");
		
		commands.add("e");
		commands.add("fx");
		commands.add("err");
		commands.add("env");
		commands.add("proxy");
		commands.add("ver");
		commands.add("q");
		commands.add("exit");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getProjectCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Project commands" ;
		commands.add("h");
		commands.add("init");
		commands.add("cfg");
		commands.add("ecfg");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getModelCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Model commands" ;
		commands.add("m");
		commands.add("nm"); 
		commands.add("lm");
		commands.add("lmd");
		commands.add("em");
		commands.add("cm");
		commands.add("dm");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getDatabaseCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Database commands" ;
		commands.add("ldb");
		commands.add("edb");
		commands.add("cdb");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getEntityCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Entity commands" ;
		commands.add("ne");
		commands.add("le");
		commands.add("ee");
		commands.add("de");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getGitHubCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "GitHub commands" ;
		commands.add("gh");
		commands.add("ghu");
		// commands.add("lgh"); // replaced by "lbd" in v 4.2.0
		commands.add("cgh");
		
		return new CommandsGroup(name, commands);
	}
	
	private final CommandsGroup getBundleCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Bundle of templates commands" ;
		commands.add("b");
		commands.add("lb");
		commands.add("eb");
		commands.add("db");
		commands.add("lbd");
		commands.add("ib");

		return new CommandsGroup(name, commands);
	}
	
	private final CommandsGroup getTemplateCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Template commands" ;
		commands.add("lt");
		commands.add("et");
		// Command "dt" for "Delete Template" ???
		
		return new CommandsGroup(name, commands);
	}
	
	private final CommandsGroup getGenerationCommands() {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Generation commands" ;
		commands.add("gen");
		commands.add(GenBatchCommand.COMMAND_NAME);
		
		return new CommandsGroup(name, commands);
	}
	
}
