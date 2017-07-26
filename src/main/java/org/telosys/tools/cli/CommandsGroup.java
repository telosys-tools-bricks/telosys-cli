package org.telosys.tools.cli;

import java.util.List;

public class CommandsGroup {

	private final String       name ;
	private final List<String> commands ;
	
	
	public CommandsGroup(String name, List<String> commands) {
		super();
		this.name = name;
		this.commands = commands;
	}


	public String getName() {
		return name;
	}

	public List<String> getCommands() {
		return commands;
	}
	
}
