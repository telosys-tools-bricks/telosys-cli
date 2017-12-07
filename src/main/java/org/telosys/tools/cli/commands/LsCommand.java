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
package org.telosys.tools.cli.commands;

import java.io.File;
import java.io.FilenameFilter;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.FileUtil;

public class LsCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public LsCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "ls";
	}

	@Override
	public String getShortDescription() {
		return "List" ;
	}

	@Override
	public String getDescription() {
		return "List the content of a directory";
	}
	
	@Override
	public String getUsage() {
		return "ls [directory]";
	}

	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			return ls(args[1]);
		}
		else {
			return ls(null);
		}
	}

	private String ls(final String parameter) {
		File currentDir = new File(getCurrentDirectory());
		if (parameter == null) {
			// --- No parameter => list current directory
			return buildFilesList(currentDir, currentDir.listFiles());
		} else {
			// --- Parameter : is it a directory ?
			File argFile = new File(FileUtil.buildFilePath(getCurrentDirectory(), parameter));
			if (argFile.exists() && argFile.isDirectory()) {
				// --- List parameter directory
				return buildFilesList(argFile, argFile.listFiles());
			} else {
				// --- Parameter is not a directory => it's a RegExp
				FilenameFilter filenameFilter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return (name.matches(buildRegExp(parameter)));
					}
				};
				return buildFilesList(currentDir, currentDir.listFiles(filenameFilter));
			}
		}
	}

	private String buildFilesList(File dir, File[] files) {
		int n = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(dir.getPath() + " : ");
		appendEndOfLine(sb);
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					sb.append(" d");
				} else {
					sb.append(" -");
				}
				sb.append(" ");
				sb.append(file.getName());
				appendEndOfLine(sb);
				n++;
			}
		}
		sb.append("        " + n + " file(s)");
		appendEndOfLine(sb);
		return sb.toString();
	}

	private String buildRegExp(String filterPattern) {
		StringBuilder buf = new StringBuilder();
		// "*" --> ".*"
		// "." --> "\."
		// "$" --> "\$"
		byte[] bytes = filterPattern.getBytes();
		for (byte b : bytes) {
			if (b == '*') {
				buf.append(".*");
			} else if (b == '.' || b == '$' || b == '^' || b == '[' || b == ']') {
				buf.append('\\');
				buf.append((char) b);
			} else {
				buf.append((char) b);
			}
		}
		return buf.toString();
	}
}
