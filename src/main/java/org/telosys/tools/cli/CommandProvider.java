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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.cli.commands.BundleCommand;
import org.telosys.tools.cli.commands.CdCommand;
import org.telosys.tools.cli.commands.CfgCommand;
import org.telosys.tools.cli.commands.CheckDatabaseCommand;
import org.telosys.tools.cli.commands.CheckGitHubCommand;
import org.telosys.tools.cli.commands.CheckModelCommand;
import org.telosys.tools.cli.commands.DeleteBundleCommand;
import org.telosys.tools.cli.commands.DeleteEntityCommand;
import org.telosys.tools.cli.commands.DeleteModelCommand;
import org.telosys.tools.cli.commands.EditBundleCommand;
import org.telosys.tools.cli.commands.EditCommand;
import org.telosys.tools.cli.commands.EditConfigCommand;
import org.telosys.tools.cli.commands.EditDatabasesCommand;
import org.telosys.tools.cli.commands.EditEntityCommand;
import org.telosys.tools.cli.commands.EditModelCommand;
import org.telosys.tools.cli.commands.EditTemplateCommand;
import org.telosys.tools.cli.commands.EnvCommand;
import org.telosys.tools.cli.commands.ErrorCommand;
import org.telosys.tools.cli.commands.ExitCommand;
import org.telosys.tools.cli.commands.FileExplorerCommand;
import org.telosys.tools.cli.commands.GenBatchCommand;
import org.telosys.tools.cli.commands.GenerateCommand;
import org.telosys.tools.cli.commands.HelpCommand;
import org.telosys.tools.cli.commands.HomeCommand;
import org.telosys.tools.cli.commands.InitCommand;
import org.telosys.tools.cli.commands.InstallBundlesCommand;
import org.telosys.tools.cli.commands.InstallModelsCommand;
import org.telosys.tools.cli.commands.ListBundlesCommand;
import org.telosys.tools.cli.commands.ListBundlesInDepotCommand;
import org.telosys.tools.cli.commands.ListDatabasesCommand;
import org.telosys.tools.cli.commands.ListEntitiesCommand;
import org.telosys.tools.cli.commands.ListModelsCommand;
import org.telosys.tools.cli.commands.ListModelsInDepotCommand;
import org.telosys.tools.cli.commands.ListResourcesCommand;
import org.telosys.tools.cli.commands.ListTemplatesCommand;
import org.telosys.tools.cli.commands.LsCommand;
import org.telosys.tools.cli.commands.MkdirCommand;
import org.telosys.tools.cli.commands.ModelCommand;
import org.telosys.tools.cli.commands.NewEntityCommand;
import org.telosys.tools.cli.commands.NewModelCommand;
import org.telosys.tools.cli.commands.ProxyCommand;
import org.telosys.tools.cli.commands.PwdCommand;
import org.telosys.tools.cli.commands.QuitCommand;
import org.telosys.tools.cli.commands.TestCommand;
import org.telosys.tools.cli.commands.ThrowExceptionCommand;
import org.telosys.tools.cli.commands.VerCommand;

import jline.console.ConsoleReader;

/**
 * Class for command instance providing <br>
 * Based on a Map : 'command name' --> 'command instance'
 * 
 * @author Laurent GUERIN 
 *
 */
public class CommandProvider {

	private final Map<String, Command> commands = new HashMap<>();
	
	private final void register(Command command) {
		commands.put(command.getName(), command);
	}
	
	private final void init(ConsoleReader consoleReader, Environment environment) {
		
		register(new ThrowExceptionCommand(consoleReader, environment)); // tex : FOR TESTS ONLY !
		
		// O.S. commands
		register(new CdCommand(consoleReader, environment)); // cd
		register(new LsCommand(consoleReader, environment)); // ls
		register(new MkdirCommand(consoleReader, environment)); // mkdir (ver 3.1.2 CLI-002 )
		register(new PwdCommand(consoleReader, environment)); // pwd

		// Telosys global commands
		register(new CfgCommand(consoleReader, environment)); // cfg (ver 3.1.2 CLI-002 )
		register(new EditCommand(consoleReader, environment)); // e
		register(new FileExplorerCommand(consoleReader, environment)); // fx ( ver 4.1.0 )
		register(new EnvCommand(consoleReader, environment)); // env
		register(new HelpCommand(consoleReader, environment)); // ? - help
		register(new QuitCommand(consoleReader, environment)); // q
		register(new ExitCommand(consoleReader, environment)); // exit  ( ver 4.1.0 )
		register(new ErrorCommand(consoleReader, environment)); // err
		register(new VerCommand(consoleReader, environment)); // ver
		register(new ProxyCommand(consoleReader, environment)); // proxy ( ver 4.1.1 )

		register(new TestCommand(consoleReader, environment)); // Special command (just for tests)
		
		// Project commands
		register(new HomeCommand(consoleReader, environment)); // h 
		register(new InitCommand(consoleReader, environment)); // init
		register(new EditConfigCommand(consoleReader, environment)); // ecfg
		register(new EditDatabasesCommand(consoleReader, environment)); // edb

		// Database commands
		register(new ListDatabasesCommand(consoleReader, environment)); // ldb
		register(new CheckDatabaseCommand(consoleReader, environment)); // cdb
		// register(new NewDbModelCommand(consoleReader, environment));    // ndbm // removed in v 4.0.0
		// register(new UpdateDbModelCommand(consoleReader, environment)); // udbm // removed in v 4.0.0
		
		// Model commands
		register(new ModelCommand(consoleReader, environment));       // m 
		register(new NewModelCommand(consoleReader, environment));    // nm
		register(new ListModelsCommand(consoleReader, environment));  // lm 
		register(new ListModelsInDepotCommand(consoleReader, environment));  // lmd (v 4.2.0)
		register(new InstallModelsCommand(consoleReader, environment));      // im (v 4.2.0) 
		register(new DeleteModelCommand(consoleReader, environment)); // dm 
		register(new CheckModelCommand(consoleReader, environment));  // cm
		register(new EditModelCommand(consoleReader, environment));   // em
		
		// Entity commands
		register(new ListEntitiesCommand(consoleReader, environment)); // le : list entities [model-name]
		register(new NewEntityCommand(consoleReader, environment));    // ne : new entity
		register(new EditEntityCommand(consoleReader, environment));   // ee : edit entity
		register(new DeleteEntityCommand(consoleReader, environment)); // de : delete entity
		
		// GitHub store management
//		register(new GitHubCommand(consoleReader, environment));     // gh  : gh [store-name]  // removed in ver 4.2.0
//		register(new GitHubUserCommand(consoleReader, environment)); // ghu : ghu user-name    // removed in ver 4.2.0
//		register(new ListGitHubCommand(consoleReader, environment)); // lgh [filter-criteria]  // replaced by "lbd" command - v 4.2.0
		register(new CheckGitHubCommand(consoleReader, environment)); // cgh 
		
/**
//		 or
//		   sb (select bundle from GitHub) :
//		   sb --> print the current bundles list with selected bundles ( 
//		        result :
//		         1) [ ] java-persistence-jpa-T300
//		         2) [x] java-web-rest-jaxrs1-T300
//		   sb * --> build a list with all the bundles (and prompt to select or not each bundle ?)
//		   sb java rest --> build a list with all the bundles containing "java" or "rest" (and prompt to select or not each bundle?)
//		   sb 1, 3 --> select items 1 and 3 
//		   sb -1 -3 -4 --> unselect items 1 3 and 4 
//		 
**/
		// Bundles commands
		register(new ListBundlesCommand(consoleReader, environment)); // lb (list installed bundles)
		register(new BundleCommand(consoleReader, environment)); // b set/print current bundle
		register(new EditBundleCommand(consoleReader, environment)); // eb : edit bundle
		register(new DeleteBundleCommand(consoleReader, environment)); // db : delete bundle
		// Bundles depot commands
		register(new ListBundlesInDepotCommand(consoleReader, environment)); // lbd [filter-criteria] 
		register(new InstallBundlesCommand(consoleReader, environment)); // ib : install  bundle(s) from depot (GitHub, ...) 				

		// Templates commands
		register(new ListTemplatesCommand(consoleReader, environment)); // lt : list templates				
		register(new ListResourcesCommand(consoleReader, environment)); // lr : list resources				
		register(new EditTemplateCommand(consoleReader, environment)); // et : edit template				
		// et (edit template --> .vm ) : ee foo --> edit foo.vm
		// dt (delete template)
		
		// Generation management
		register(new GenerateCommand(consoleReader, environment)); // gen : generates entities with templates				
		register(new GenBatchCommand(consoleReader, environment)); // genbatch : generation in batch mode				
		
		
		// Launcher management
		// ll (list launchers )
		// l (set/print current launcher : "l" 
		// nl (new launcher : nl launcher-name model-name or current-model --> .properties + .entities + .templates )
		// el (edit launcher : el launcher-name --> .properties )
		// ele ( edit launcher entities --> .entities )
		// elt ( edit launcher templates --> .templates )
		// dl (delete launcher : dl launcher-name )
		
		
	}
	
	/**
	 * Constructor
	 */
	public CommandProvider(ConsoleReader consoleReader) {
		super();
		Environment environment = new Environment(this); 
		init(consoleReader, environment);
	}

	public final Command getCommand(String commandName) {
		return commands.get(commandName);
	}
	
	public final List<Command> getAllCommands() {
		List<Command> list = new LinkedList<>();
		for ( Map.Entry<String, Command> entry : commands.entrySet() ) {
			Command a = entry.getValue() ;
			if ( ! list.contains(a)) {
			    list.add(a);
			}
		}
		return list ;
	}
}
