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
package org.telosys.tools.cli;

import java.util.LinkedList;
import java.util.List;

public class CommandsGroups {

	
	private final List<CommandsGroup> all ;
		
	public CommandsGroups(CommandProvider commandProvider) {
		super();
		all = new LinkedList<>();
		
		all.add(getGlobalCommands(commandProvider)) ;
		all.add(getProjectCommands(commandProvider)) ;
		all.add(getDatabaseCommands(commandProvider)) ;
		all.add(getModelCommands(commandProvider)) ;
		all.add(getEntityCommands(commandProvider)) ;
		all.add(getGitHubCommands(commandProvider)) ;
		all.add(getBundleCommands(commandProvider)) ;
		all.add(getTemplateCommands(commandProvider)) ;
		all.add(getGenerationCommands(commandProvider)) ;
		all.add(getLauncherCommands(commandProvider)) ;
		
	}

	public final List<CommandsGroup> getAll() {
		return all ;
	}
	
	private final CommandsGroup getGlobalCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "General commands" ;
		commands.add("?");
		commands.add("pwd");
		commands.add("cd");
		commands.add("ls");
		commands.add("e");
		commands.add("err");
		commands.add("env");
		commands.add("q");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getProjectCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Project commands" ;
		commands.add("h");		
		commands.add("init");
		commands.add("ecfg"); 
		// commands.add("lpc"); // List Project config 
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getModelCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Model commands" ;
		commands.add("m");
		commands.add("nm");
		commands.add("ndbm");
		commands.add("udbm");
		commands.add("lm");
		commands.add("em");
		commands.add("cm");
		commands.add("dm");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getDatabaseCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Database commands" ;
		commands.add("ldb");
		commands.add("edb");
		commands.add("cdb");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getEntityCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Entity commands" ;
		commands.add("ne");
		commands.add("le");
		commands.add("ee");
		commands.add("de");
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getGitHubCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "GitHub commands" ;
		commands.add("gh");
		commands.add("lgh");
		
		return new CommandsGroup(name, commands);
	}
	
	private final CommandsGroup getBundleCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Bundle commands" ;
		commands.add("ib");
		commands.add("b");
		commands.add("lb");
		commands.add("eb");
		commands.add("db");

		return new CommandsGroup(name, commands);
	}
	
	private final CommandsGroup getTemplateCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Template commands" ;
		commands.add("t");
		commands.add("lt");
		commands.add("et");
		commands.add("dt");
		
		return new CommandsGroup(name, commands);
	}
	
	private final CommandsGroup getGenerationCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Generation commands" ;
		commands.add("gen");
		
		return new CommandsGroup(name, commands);
	}
	
	private final CommandsGroup getLauncherCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Launcher commands" ;
		commands.add("ll");
		commands.add("l");
		commands.add("nl");
		commands.add("el");
		commands.add("ele");
		commands.add("elt");
		commands.add("dl");

		commands.add("lg");

		// Launcher management
		// ll (list launchers )
		// l (set/print current launcher : "l" 
		// nl (new launcher : nl launcher-name model-name or current-model --> .properties + .entities + .templates )
		// el (edit launcher : el launcher-name --> .properties )
		// ele ( edit launcher entities --> .entities )
		// elt ( edit launcher templates --> .templates )
		// dl (delete launcher : dl launcher-name )
		// lg (launch generation : lg launcher-name )
		return new CommandsGroup(name, commands);
	}
	
}
