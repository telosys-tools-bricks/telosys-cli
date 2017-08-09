package org.telosys.tools.cli;

import java.io.IOException;
import java.io.PrintWriter;

import jline.console.ConsoleReader;

/**
 * Application entry point for CLI
 * 
 * @author Laurent GUERIN
 *
 */
public class ApplicationCLI {
	
	/**
	 * Main
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		try {

			ConsoleReader consoleReader = new ConsoleReader();
			CommandProvider commandProvider = new CommandProvider(consoleReader);
			consoleReader.setPrompt(Color.colorize(Const.INITIAL_PROMPT, Const.PROMPT_COLOR) );
			
			PrintWriter out = new PrintWriter(consoleReader.getOutput());
			//out.println(new Banner().banner2());
			String banner = new Banner().bannerSlant();
			//out.println( Color.colorize(banner, Color.MAGENTA_BRIGHT));
			//out.println( Color.colorize(banner, Color.YELLOW_BRIGHT));
			//out.println( Color.colorize(banner, Color.BLUE_BRIGHT));
			out.println( Color.colorize(banner, Color.CYAN_BRIGHT));
			out.println("Enter ? for help");

			CommandLineProcessor commandLineProcessor = new CommandLineProcessor( consoleReader, commandProvider ) ;
			
			while (true) {
				String line = consoleReader.readLine() ;
				commandLineProcessor.processLine(line);
			}
		} catch (Exception e) {
			System.out.println("ERROR : Exception " + e.getMessage() );
			System.out.flush();
		}
	}
}
