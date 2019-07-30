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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CommandsFileProcessor {

	private final CommandLineProcessor commandLineProcessor ;
	
	/**
	 * Constructor
	 * @param commandLineProcessor
	 */
	public CommandsFileProcessor(CommandLineProcessor commandLineProcessor) {
		this.commandLineProcessor = commandLineProcessor ;
	}
	
	private void print(String msg) {
		System.out.println(msg);
	}
	/**
	 * Process the given file name
	 * @param fileName
	 * @throws IOException
	 */
	public void processFile(String fileName) throws IOException {
		File file = new File(fileName);
		processFile(file);
	}
	
	/**
	 * Process the given file 
	 * @param file
	 * @throws IOException
	 */
	public void processFile(File file) throws IOException {
//		if ( ! file.isFile() ) {
//			throw new RuntimeException( fileName + " is not a file");
//		}
		print("commands execution from file : " + file.getAbsolutePath() ) ;
		try ( FileReader fileReader = new FileReader(file) ) {
			try ( BufferedReader br = new BufferedReader(fileReader) ) {
				String line = "";
				while ( ( line = br.readLine() ) != null) {
				    print("command> " + line);
				    commandLineProcessor.processLine(line);
				}
			}
		}
		
		
//		try {
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			
//			try {
//				br.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}

}
