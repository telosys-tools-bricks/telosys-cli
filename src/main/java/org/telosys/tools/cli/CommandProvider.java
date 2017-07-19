package org.telosys.tools.cli;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.cli.commands.CdCommand;
import org.telosys.tools.cli.commands.EditCommand;
import org.telosys.tools.cli.commands.EditEntityCommand;
import org.telosys.tools.cli.commands.EnvCommand;
import org.telosys.tools.cli.commands.GuideCommand;
import org.telosys.tools.cli.commands.HelpCommand;
import org.telosys.tools.cli.commands.HomeCommand;
import org.telosys.tools.cli.commands.InitCommand;
import org.telosys.tools.cli.commands.LsCommand;
import org.telosys.tools.cli.commands.ModelCommand;
import org.telosys.tools.cli.commands.NewModelCommand;
import org.telosys.tools.cli.commands.PwdCommand;
import org.telosys.tools.cli.commands.QuitCommand;

public class CommandProvider {

	private final Map<String, Command> commands = new Hashtable<>();
	
	private final void register(Command command) {
		commands.put(command.getName(), command);
		if ( command.getShortName() != null ) {
			commands.put(command.getShortName(), command);
		}
	}
	private final void init(PrintWriter out) {
		register(new CdCommand(out));
		register(new EditCommand(out));
		register(new EditEntityCommand(out));
		register(new EnvCommand(out));
		register(new GuideCommand(out));
		register(new HelpCommand(out));
		register(new HomeCommand(out));
		register(new InitCommand(out));
		register(new LsCommand(out));
		register(new ModelCommand(out));
		register(new NewModelCommand(out));
		register(new PwdCommand(out));
		register(new QuitCommand(out));
	}
	
	/**
	 * Constructor
	 */
	public CommandProvider(PrintWriter out) {
		super();
		init(out);
	}

	public final Command getCommand(String commandName) {
		Command command = commands.get(commandName);
		if ( command != null ) {
			return command;
		}
		else {
			throw new RuntimeException("Invalid command name '" + commandName + "'");
		}
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
