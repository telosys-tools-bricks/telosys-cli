package org.telosys.tools.cli;

import java.io.PrintWriter;

import jline.console.ConsoleReader;

public class CommandLineProcessor {
	
	private final PrintWriter     out ;
	private final CommandProvider commandProvider ;
	
	/**
	 * Constructor
	 * @param consoleReader
	 */
	public CommandLineProcessor(ConsoleReader consoleReader, CommandProvider commandProvider) {
		super();
		this.out = new PrintWriter(consoleReader.getOutput());
		this.commandProvider = commandProvider;
	}

	/**
	 * Processes the given line (try to interpret and execute the command) 
	 * @param line
	 */
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
			Command command = commandProvider.getCommand(commandName);
			if ( command != null ) {
				try {
					result = command.execute(args);
					print(result);
				} catch (CancelCommandException cancelCommandEx ) {
					// The command has been canceled, for example due to invalid arguments 
					// => just print the messsage and continue
					print(cancelCommandEx.getMessage());
				}
			}
			else {
				print("Invalid command '" + commandName + "'");
			}
		} catch (Exception e) {
			printException(e);
		}
	}
	
	private void debug(String message) {
		if ( Trace.DEBUG ) {
			out.println("[DEBUG] "+message);
			out.flush();
		}
	}
	private void print(String message) {
		if ( message != null ) {
			out.println(message);
			out.flush();
		}
	}
	private void printException(Exception e) {
		out.println("ERROR - Unexpected exception : " + e.getClass().getSimpleName() );
		out.println("  Message : " + e.getMessage() );
		e.printStackTrace(out);
		out.flush();
	}
}
