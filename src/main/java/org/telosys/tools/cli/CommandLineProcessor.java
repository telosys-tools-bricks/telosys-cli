package org.telosys.tools.cli;

import java.io.PrintWriter;

import jline.console.ConsoleReader;

public class CommandLineProcessor {
	
	private final PrintWriter     out ;
	private final CommandProvider commandProvider ;
	private final Environment     environment ;
	
	/**
	 * Constructor
	 * @param consoleReader
	 */
	public CommandLineProcessor(ConsoleReader consoleReader) {
		super();
		this.out = new PrintWriter(consoleReader.getOutput());
		this.commandProvider = new CommandProvider(this.out);
		this.environment = new Environment(this.commandProvider);
	}

	public void processLine( String line ) {
		debug("(line='"+line+"')");
		if ( line.trim().length() == 0 ) {
			return ;
		}
		String result ;
		String[] args = CommandLineUtil.toArgs(line);
		try {
			debug("(args length = " + args.length +")");
			String commandName = args[0];
			debug("(commandName = '" + commandName +"')");
			if ( "q".equalsIgnoreCase(commandName)) {
				print("bye...");
				System.exit(0);
			}
			Command command = commandProvider.getCommand(commandName);
			if ( command != null ) {
				result = command.execute(environment, args);
				print(result);
			}
			else {
				print("Invalid command '" + commandName + "'");
			}
		} catch (Exception e) {
			print(e.getMessage());
		}
	}
	
	private void debug(String message) {
		if ( Trace.DEBUG ) {
			out.println("[DEBUG] "+message);
			out.flush();
		}
	}
	private void print(String message) {
		out.println(message);
		out.flush();
	}
}
