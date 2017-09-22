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
//			out.println( getJarLocation() );
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
	
//	private static String getJarLocation() {
//		URL url = ApplicationCLI.class.getProtectionDomain().getCodeSource().getLocation();
//		try {
//			URI uri = url.toURI();
//			File file = new File( uri.getPath() ) ;
//			return file.toString();
//		} catch (URISyntaxException e) {
//			throw new RuntimeException("Cannot get exe location (URISyntaxException)", e);
//		}		
//	}
}
