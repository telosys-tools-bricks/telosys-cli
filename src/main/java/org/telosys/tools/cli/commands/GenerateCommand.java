package org.telosys.tools.cli.commands;

import java.util.LinkedList;
import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CancelCommandException;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generator.target.TargetsDefinitions;
import org.telosys.tools.generator.task.ErrorReport;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

/**
 * Generates targets using the given entities and the given templates 
 * 
 * @author Laurent GUERIN
 *
 */
public class GenerateCommand extends CommandWithModel {
	
	/**
	 * Constructor
	 * @param out
	 */
	public GenerateCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "gen";
	}

	@Override
	public String getShortDescription() {
		return "Generate" ;
	}

	@Override
	public String getDescription() {
		return "Generates the given targets for the given entities";
	}
	
	@Override
	public String getUsage() {
		return "gen *|entity-name *|template-name";
	}

	@Override
	public String execute(String[] args) {
		if ( checkModelDefined() && checkBundleDefined() ) {
			if ( args.length == 3 ) {
				generate(args);
				return null;
			} 
			else {
				return invalidUsage("Usage : " + getUsage() );
			}
		}
		return null ;
	}

	private void generate(String[] args)  {
		GenerationTaskResult result ;
		try {
			result = generate(args[1], args[2]);
			if ( result != null ) {
				printResult(result);
			}
		} catch (TelosysToolsException e) {
			printError(e);
		} catch (GeneratorException e) {
			printError(e);
		}
	}
	
	private GenerationTaskResult generate(String argEntityNames, String argTemplateNames) throws TelosysToolsException, GeneratorException {
		
		// Loads the model for the current model name
		Model model = loadCurrentModel();
		List<String> entityNames = buildEntityNames(argEntityNames, model);
		
		List<String> templateNames = buildTemplateNames(argTemplateNames);

		print("Entities : ");
		printList(entityNames);
		print("Templates : ");
		printList(templateNames);

		if ( confirm("Do you want to launch the generation") ) {
			print("Generation in progress...");
			return generate(model, entityNames, templateNames);
		}
		else {
			print("Generation canceled.");
			return null ;
		}
	}

	/**
	 * Builds a list of entities using the given argument ( eg : '*', 'Car', 'Car,Dog', 'Dog,Driver,Car' )
	 * @param arg
	 * @param model
	 * @return
	 */
	private List<String> buildEntityNames(String arg, Model model) throws TelosysToolsException {
		List<String> list = new LinkedList<String>();
		if ( "*".equals(arg) ) {
			// All entities 
			for ( Entity entity : model.getEntities() ) {
				list.add(entity.getClassName());
			}
		}
		else if ( arg.contains(",") ) {
			// Many entity names : eg 'Car,Driver,Dog'
			String[] array = StrUtil.split(arg, ',' );
			for ( String s : array ) {
				String entityName = s.trim();
				if ( entityName.length() > 0 ) {
					checkEntityExists(entityName, model);
					list.add(entityName);
				}
			}
		}
		else {
			// Only 1 entity name
			checkEntityExists(arg, model);
			list.add(arg);
		}
		return list ;
	}
	
	private void checkEntityExists(String entityName, Model model) {
		if ( model.getEntityByClassName(entityName) == null ) {
			throw new CancelCommandException("Unknown entity '" + entityName + "'");
		}
	}
	
	private List<String> buildTemplateNames(String arg) throws TelosysToolsException {
		List<String> list = new LinkedList<String>();
		if ( "*".equals(arg) ) {
			// All  
			return getTelosysProject().getTemplates(getCurrentBundle());
		}
		else if ( arg.contains(",") ) {
			// Many entity names : eg 'template1,template2,template3'
			String[] array = StrUtil.split(arg, ',' );
			for ( String s : array ) {
				String templateName = s.trim();
				if ( templateName.length() > 0 ) {
					list.add(s.trim());
				}
			}
		}
		else {
			// Only 1 entity name
			list.add(arg);
		}
		return list ;
	}
	
	private List<TargetDefinition> buildTargets(String bundleName, List<String> templatesNames) throws TelosysToolsException {
		TelosysProject telosysProject = getTelosysProject();
		TargetsDefinitions targetDefinitions = telosysProject.getTargetDefinitions(bundleName);
		List<TargetDefinition> allTemplates = targetDefinitions.getTemplatesTargets();
		List<TargetDefinition> selectedTemplates = new LinkedList<TargetDefinition>();
		for ( String templateName : templatesNames ) {
			// search template name in target definitions
			for ( TargetDefinition targetDef : allTemplates ) {
				if ( targetDef.getFile().equals(templateName) ) {
					// Found
					selectedTemplates.add( targetDef );
				}
			}
		}		
		return selectedTemplates ;
	}
	
	private GenerationTaskResult generate(Model model, List<String> entityNames, List<String> templateNames) throws TelosysToolsException, GeneratorException {
		TelosysProject telosysProject = getTelosysProject();
		
		String bundleName = getCurrentBundle() ;
		List<TargetDefinition> targetsList = buildTargets(bundleName, templateNames);
		boolean flagResources = false ; 
		
		print("targetsList : " + targetsList.size() );
		
		return telosysProject.launchGeneration(model, entityNames, bundleName, targetsList, flagResources);
	}

	private void printList( List<String> list ) {
		for ( String s : list ) {
			print(" . " + s);
		}
	}
	
	private void printResult( GenerationTaskResult result ) {
		print("Generation completed.");
		print(" " + result.getNumberOfFilesGenerated() + " file(s) generated");
		print(" " + result.getNumberOfResourcesCopied() + " resource(s) copied");
		print(" " + result.getNumberOfGenerationErrors() + " error(s)");
		List<ErrorReport> errors = result.getErrors() ;
		if ( errors != null && errors.size() > 0 ) {
			int i = 0 ;
			for ( ErrorReport err : errors ) {
				i++ ;
				print ( " - Error #" + i ) ;
				print ( "   Type : " + err.getErrorType() ) ;
				print ( "   Message : " + err.getMessage() ) ;
				Throwable ex = err.getException();
				if ( ex != null ) {
					print ( "   Exception : " + ex.getClass().getSimpleName() + " : " + ex.getMessage() ) ;
					Throwable cause = ex.getCause() ;
					while ( cause != null ) {
						print ( "    Cause : " + cause.getClass().getSimpleName() + " : " + cause.getMessage() ) ;
						cause = cause.getCause();
					}
				}
			}
		}
	}
	
}
