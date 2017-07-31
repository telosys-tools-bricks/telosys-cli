package org.telosys.tools.cli.commands;

import java.io.File;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.generic.model.Model;

public class CheckModelCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public CheckModelCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "cm";
	}

	@Override
	public String getShortDescription() {
		return "Check Model" ;
	}

	@Override
	public String getDescription() {
		return "Check the current/given model ( 'cm [model-name]' )";
	}
	
	@Override
	public String execute(String[] args) {
		
		if ( checkHomeDirectoryDefined() ) {
			if ( args.length > 1 ) {
				// cm model-name
				return checkModel(args[1]);
			}
			else {
				// cm => current model if defined
				if ( checkModelDefined() ) {
					return checkModel(getCurrentModel());
				}				
			}			
		}
		return null;
	}
	
//	private String getModelFileName(TelosysProject telosysProject, String modelName) throws TelosysToolsException {
//
//		String fileName = null ;
//		int n = 0 ;
//		List<File> models = telosysProject.getModels(); // All the models files in the current project
//		if ( modelName.contains(".") ) {
//			// Suffix is in the name 
//			for ( File file : models ) {
//				if ( file.getName().equals(modelName) ) {
//					fileName = file.getName() ;
//					n++;				
//				}
//			}
//		}
//		else {
//			// No suffix in the name => try to add all the suffixes 
//			for ( File file : models ) {
//				if ( file.getName().equals(modelName + ApiUtil.MODEL_SUFFIX) ) {
//					fileName = file.getName() ;
//					n++;				
//				}
//				if ( file.getName().equals(modelName + ApiUtil.DBMODEL_SUFFIX) ) {
//					fileName = file.getName() ;
//					n++;				
//				}
//				if ( file.getName().equals(modelName + ApiUtil.DBREP_SUFFIX) ) {
//					fileName = file.getName() ;
//					n++;				
//				}
//			}
//		}
//		if ( n == 0 ) {
//			return null ; // Not found
//		}
//		else if ( n == 1 ) {
//			return fileName ; // Found 1 matching file
//		}
//		else {
//			throw new TelosysToolsException("Ambiguous model name (" + n + " files found)");
//		}
//	}
	
	private String checkModel(String modelName) {
		
		// 1) try to get the file 
		File modelFile = getModelFile(modelName); 
		if ( modelFile != null ) {
			// 2) try to load the model 
			Model model = loadModel(modelFile);
			if ( model != null ) {
				int n = model.getEntities() != null ? model.getEntities().size() : 0 ; 
				print( "Model OK (file '" + modelFile.getName() + "' : " + n + " entities)" );
			}
		}
		return null ;

		
//		// 1) try to get the file name
//		String modelFileName;
//		try {
//			modelFileName = getModelFileName(telosysProject, modelName);
//		} catch (TelosysToolsException e1) {
//			printError(e1);
//			return null;
//		}
//
//		// 2) try to load the model from the file name
//		if ( modelFileName != null ) {
//			// try to load the model from the basic file name ( e.g. : 'books.dbrep' or 'books.model' )
//			try {
//				Model model = telosysProject.loadModel(modelFileName);
//				//telosysProject.loadModel(modelFileName);
//				int n = model.getEntities() != null ? model.getEntities().size() : 0 ; 
//				return "Model OK, file '" + modelFileName + "' loaded successfully : " + n + " entities.";
//			} catch (TelosysToolsException e2) {
//				if ( e2 instanceof TelosysModelException ) {
//					printError("Invalid model !");
//					// Print parsing errors
//					TelosysModelException tme = (TelosysModelException) e2 ;
//					Map<String,String> parsingErrors = tme.getParsingErrors();
//					if ( parsingErrors != null ) {
//						print( parsingErrors.size() + " parsing error(s)" );
//						for ( Map.Entry<String,String> entry : parsingErrors.entrySet() ) {
//							print( "'" + entry.getKey() + "' : " + entry.getValue() );
//						}					
//					}
//				}
//				else {
//					printError(e2);					
//				}
//			}
//		}
//		else {
//			return "Model '" + modelName + "' not found." ;
//		}
//		return null ;
	}
}
