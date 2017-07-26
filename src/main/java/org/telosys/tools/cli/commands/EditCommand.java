package org.telosys.tools.cli.commands;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;

public class EditCommand extends Command {

	/**
	 * Constructor
	 * @param out
	 */
	public EditCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}


	@Override
	public String getName() {
		return "e";
	}

	@Override
	public String getShortDescription() {
		return "Edit" ;
	}

	@Override
	public String getDescription() {
		return "Open an external editor ( 'e [file-name]' ) ";
	}
	
	@Override
	public String execute(String[] args) {
		if ( args.length > 1 ) {
			return edit(args[1]);
		}
		else {
			return edit("");
		}
	}

	private String edit(String shortFileName) {
		if (!StrUtil.nullOrVoid(shortFileName)) {
			String fileToBeEdited = FileUtil.buildFilePath(getCurrentDirectory(), shortFileName);
			return launchEditor(fileToBeEdited);
		} else {
			return launchEditor("");
		}
	}

//	private String launchEditor(Environment environment, String fileFullPath) {
//		String editorCommand = environment.getEditorCommand();
//		String fullCommand = editorCommand + " " + fileFullPath;
//		try {
//			Runtime.getRuntime().exec(fullCommand);
//			if (fileFullPath != null) {
//				return "Editor launched (" + fileFullPath + ")";
//			} else {
//				return "Editor launched (no file name).";
//			}
//		} catch (IOException e) {
//			return "ERROR : IOException : " + e.getMessage();
//		}
//	}

//	private String getTelosysToolsCfgFullPath(Environment environment) {
//		return getFileFullPath(environment, Const.TELOSYS_TOOLS_CFG, Const.TELOSYS_TOOLS_FOLDER);
//	}
//
//	private String getDatabasesDbCfgFullPath(Environment environment) {
//		// TODO : use cfg for subfolder
//		return getFileFullPath(environment, Const.DATABASES_DBCFG, Const.TELOSYS_TOOLS_FOLDER);
//	}

//	private String getFileFullPath(Environment environment, String fileName, String subDirectory) {
//
////		// Try to get the file name in the HOME directory
////		String fileFullPath = FileUtil.buildFilePath(environment.getHomeDirectory(), fileName);
////		File file = new File(fileFullPath);
////		if (file.exists()) {
////			return fileFullPath;
////		}
//
//		// Try to get the file in the 'home' directory
//		String shortPath = fileName; // e.g. 'telosys-tools.cfg' or 'databases.dbcfg'
//		if (subDirectory != null) {
//			shortPath = FileUtil.buildFilePath(subDirectory, fileName); // e.g.
//																		// 'TelosysTools/databases.dbcfg'
//		}
//		String fileFullPath = FileUtil.buildFilePath(environment.getHomeDirectory(), shortPath);
//		File file = new File(fileFullPath);
//		if (file.exists()) {
//			return fileFullPath;
//		}
//
//		// Not found
//		return null;
//	}
}
