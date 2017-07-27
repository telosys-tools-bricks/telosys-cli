package org.telosys.tools.cli;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.commands.CdCommand;
import org.telosys.tools.cli.commands.DeleteModelCommand;
import org.telosys.tools.cli.commands.EditCommand;
import org.telosys.tools.cli.commands.EditEntityCommand;
import org.telosys.tools.cli.commands.EditProjectCfgCommand;
import org.telosys.tools.cli.commands.EditProjectDbCfgCommand;
import org.telosys.tools.cli.commands.EnvCommand;
import org.telosys.tools.cli.commands.ErrorCommand;
import org.telosys.tools.cli.commands.GitHubCommand;
import org.telosys.tools.cli.commands.GuideCommand;
import org.telosys.tools.cli.commands.HelpCommand;
import org.telosys.tools.cli.commands.HomeCommand;
import org.telosys.tools.cli.commands.InitCommand;
import org.telosys.tools.cli.commands.InstallBundlesCommand;
import org.telosys.tools.cli.commands.ListBundlesCommand;
import org.telosys.tools.cli.commands.ListModelsCommand;
import org.telosys.tools.cli.commands.LsCommand;
import org.telosys.tools.cli.commands.ModelCommand;
import org.telosys.tools.cli.commands.NewModelCommand;
import org.telosys.tools.cli.commands.PwdCommand;
import org.telosys.tools.cli.commands.QuitCommand;
import org.telosys.tools.cli.commands.ThrowExceptionCommand;

public class CommandProvider {

	private final Map<String, Command> commands = new Hashtable<>();
	
	private final void register(Command command) {
		commands.put(command.getName(), command);
//		if ( command.getShortName() != null ) {
//			commands.put(command.getShortName(), command);
//		}
	}
	private final void init(ConsoleReader consoleReader, Environment environment) {
		
		register(new ThrowExceptionCommand(consoleReader, environment)); // tex : FOR TESTS ONLY !
		
		register(new CdCommand(consoleReader, environment)); // cd
		register(new EditCommand(consoleReader, environment)); // e
		register(new EnvCommand(consoleReader, environment)); // env
		register(new GuideCommand(consoleReader, environment)); // guide
		register(new HelpCommand(consoleReader, environment, this)); // ? - help
		register(new LsCommand(consoleReader, environment)); // ls
		register(new PwdCommand(consoleReader, environment)); // pwd
		register(new QuitCommand(consoleReader, environment)); // q
		register(new ErrorCommand(consoleReader, environment)); // err
		
		// Project commands
		register(new HomeCommand(consoleReader, environment)); // h 
		register(new InitCommand(consoleReader, environment)); // init
		register(new EditProjectCfgCommand(consoleReader, environment)); // epc
		register(new EditProjectDbCfgCommand(consoleReader, environment)); // epdb
		

		// Model management
		register(new ModelCommand(consoleReader, environment)); // m : m [model-name] // TODO : "m" --> print 
		register(new NewModelCommand(consoleReader, environment)); // nm
		register(new ListModelsCommand(consoleReader, environment)); // lm : list models
		register(new DeleteModelCommand(consoleReader, environment)); // dm : delete model
		// TODO : em (edit model)
		

		// Entity management
		// TODO : le (list entities)
		// TODO : ne (new entity)
		register(new EditEntityCommand(consoleReader, environment)); // ee 		
		// TODO : de (delete entity)
		
		// GitHub store management
		register(new GitHubCommand(consoleReader, environment)); // gh : gh [store-name] 
		// gh : set/print current github repo ( "gh store-name" --> set , "gh" --> print )
		// gh -l : list github bundles : print bundles available in the store
		//         > gh -l   --> all
		//         > gh -l java rest --> repos with "java" and "rest" in the name 
		//        result :
		//         1) java-persistence-jpa-T300 
		//         2) java-web-rest-jaxrs1-T300
		//         3) java-web-rest-jaxrs1-T300
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
//		 Templates/Bundles management
/***
//		   ib (install bundle) with a SWING/AWT SELECTION LIST to choose the bundle(s)
//		   https://www.javatpoint.com/java-awt-list
//		 or 
//		   ib : install all the selected bundles
//		   ib * : install all bundles from the current list
//		   ib 1 2 
//		 or 
**/
		register(new InstallBundlesCommand(consoleReader, environment)); // install  bundles from GitHub 		
		//   ib * : install all bundles from GitHub
		//   ib java rest : install all the bundles containing "java" or "rest"
		//   if already exists : prompt "overwrite ? [y/n] : "
		
		register(new ListBundlesCommand(consoleReader, environment)); // lb (list installed bundles)	
		// b : set/print current bundle ( "b bundle-name" --> set , "b" --> print )
		// eb (edit bundle --> edit templates.cfg )
		// et (edit template --> .vm )
		// db (delete bundle)
		// dt (delete template)
		

		// Launcher management
		// ll (list launchers )
		// l (set/print current launcher : "l" 
		// nl (new launcher : nl launcher-name model-name or current-model --> .properties + .entities + .templates )
		// el (edit launcher : el launcher-name --> .properties )
		// ele ( edit launcher entities --> .entities )
		// elt ( edit launcher templates --> .templates )
		// dl (delete launcher : dl launcher-name )
		
		// Generation management
		// lg (launch generation : lg launcher-name )
		
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
//		Command command = commands.get(commandName);
//		if ( command != null ) {
//			return command;
//		}
//		else {
//			throw new RuntimeException("Invalid command name '" + commandName + "'");
//		}
		return commands.get(commandName);
	}
	
	public final List<Command> getAllCommands() {
		List<Command> list = new LinkedList<>();
		for ( Map.Entry<String, Command> entry : commands.entrySet() ) {
		    //String name = entry.getKey() ;
			Command a = entry.getValue() ;
			if ( ! list.contains(a)) {
			    list.add(a);
			}
		}
		return list ;
	}
}
