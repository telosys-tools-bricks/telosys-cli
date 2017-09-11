package org.telosys.tools.cli;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.cli.commands.EditDatabasesCommand;

public class CommandsGroups {

	
	private final List<CommandsGroup> all ;
		
	public CommandsGroups(CommandProvider commandProvider) {
		super();
		all = new LinkedList<>();
		
		all.add(getGlobalCommands(commandProvider)) ;
		all.add(getProjectCommands(commandProvider)) ;
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
		commands.add("epc"); // Edit Project Config 
		commands.add(EditDatabasesCommand.COMMAND_NAME);
		// commands.add("lpc"); // List Project config 
		
		return new CommandsGroup(name, commands);
	}

	private final CommandsGroup getModelCommands(CommandProvider commandProvider) {
		List<String> commands = new LinkedList<>() ;
		
		String name = "Model commands" ;
		commands.add("m");
		commands.add("nm");
		commands.add("lm");
		commands.add("em");
		commands.add("dm");
		commands.add("cm");
		
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
