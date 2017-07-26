package org.telosys.tools.cli;

import java.io.IOException;
import java.io.PrintWriter;

import jline.console.ConsoleReader;

public class Main {
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		try {

			ConsoleReader consoleReader = new ConsoleReader();
			CommandProvider commandProvider = new CommandProvider(consoleReader);
			//consoleReader.setPrompt(PROMPT);
			consoleReader.setPrompt(Color.colorize(Const.INITIAL_PROMPT, Const.PROMPT_COLOR) );
			
			PrintWriter out = new PrintWriter(consoleReader.getOutput());
			//out.println(new Banner().banner2());
			String banner = new Banner().bannerSlant();
			//out.println( Color.colorize(banner, Color.MAGENTA_BRIGHT));
			//out.println( Color.colorize(banner, Color.YELLOW_BRIGHT));
			//out.println( Color.colorize(banner, Color.BLUE_BRIGHT));
			out.println( Color.colorize(banner, Color.CYAN_BRIGHT));

			CommandLineProcessor inputParser = new CommandLineProcessor( consoleReader, commandProvider ) ;
			
			//String line;
			//while ((line = consoleReader.readLine()) != null) {
			while (true) {
				String line = consoleReader.readLine() ;
				inputParser.processLine(line);
			}
		} catch (Exception e) {
			System.out.println("ERROR : Exception " + e.getMessage() );
			System.out.flush();
		}
	}

//	private static void processLine(String line, ConsoleReader consoleReader, PrintWriter out, CliEnvironment environment) {
//		System.out.println("(line="+line+")");
//		String result ;
//		LinkedList<String> args = commandLineProcess(line);
//		try {
//			System.out.println("(args size = " + args.size() +")");
//			if (!args.isEmpty()) {
//				String commandName = args.get(0);
//				System.out.println("(commandName = '" + commandName +"')");
//				if ( "q".equalsIgnoreCase(commandName)) {
//					out.println("bye...");
//					System.exit(0);
//				}
//				CliAction commande = commandProvider.getCommand(commandName);
//				if (args.size() > 1) {
//					result = commande.execute(environment, args.get(1));
//					if (commandName.equals("cd"))
//						if (StrUtil.isPath(result))
//							consoleReader.setPrompt(result + ">");
//					out.println(result);
//				} else {
//					result = commande.execute(environment, (Object) null);
//					out.println(result);
//				}
//			}
//		} catch (Exception e) {
//			out.println(e.getMessage());
//		}
//		out.flush();
//	}
//	
//	public static LinkedList<String> commandLineProcess(String commandLine) {
//		LinkedList<String> commandProcess = new LinkedList<>();
//		String[] p = commandLine.split(" ");
//		for (String str : p) {
//			if (!" ".equals(str)) {
//				commandProcess.add(str);
//			}
//		}
//		return commandProcess;
//	}
}
