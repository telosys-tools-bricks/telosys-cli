package org.telosys.tools.cli.commands;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CancelCommandException;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.bundles.TargetDefinition;
import org.telosys.tools.commons.bundles.TargetsDefinitions;
import org.telosys.tools.generator.GeneratorException;
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
	
	/**
	 * @param argEntityNames argument for entities ( eg '*', 'Car', 'Car,Driver', etc )
	 * @param argTemplateNames argument for templates ( eg '*', 'CacheFilter_java.vm', etc )
	 * @return
	 * @throws TelosysToolsException
	 * @throws GeneratorException
	 */
	private GenerationTaskResult generate(String argEntityNames, String argTemplateNames) throws TelosysToolsException, GeneratorException {
		
		TelosysProject telosysProject = getTelosysProject();
		// Loads the model for the current model name
		Model model = loadCurrentModel();
		List<String> entityNames = buildEntityNames(argEntityNames, model);
		
		//List<String> templateNames = buildTemplateNames(argTemplateNames);

		String bundleName = getCurrentBundle() ;
		List<TargetDefinition> targetDefinitions = buildTargetsList(argTemplateNames);
		
		print("Entities (model="+model.getName()+") : ");
		printList(entityNames);
		print("Templates (bundle="+bundleName+") : ");
		//printList(templateNames);
		printTargetDefinition(targetDefinitions);

		if ( confirm("Do you want to launch the generation") ) {
			print("Generation in progress...");
			
			
			//return generate(model, entityNames, templateNames);
			
			boolean flagResources = false ; 
			return telosysProject.launchGeneration(model, entityNames, bundleName, targetDefinitions, flagResources);			
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
	
	private List<TargetDefinition> buildTargetsList(String arg) throws TelosysToolsException {
		
		TargetsDefinitions targetsDefinitions = getTelosysProject().getTargetDefinitions(getCurrentBundle());
		List<TargetDefinition> allTemplatesDefinitions = targetsDefinitions.getTemplatesTargets();
		
		if ( "*".equals(arg) ) {
			// All targets
			return allTemplatesDefinitions ;
		}
		else {
			if ( arg.contains(",") ) {
				// Many template patterns (eg 'template1,template2,template3')
				Map<String,TargetDefinition> map = new Hashtable<String,TargetDefinition>();
				String[] array = StrUtil.split(arg, ',' );
				for ( String s : array ) {
					String templateName = s.trim();
					if ( templateName.length() > 0 ) {
						List<TargetDefinition> list = getTargetDefinitionsForTemplatePattern(templateName);
						for ( TargetDefinition td : list ) {
							map.put(td.getTemplate(), td);
						}
					}
				}
				// Convert map values to list
				List<TargetDefinition> list = new LinkedList<TargetDefinition>();
				for ( TargetDefinition td : map.values() ) {
					list.add(td);
				}
				return list;
			}
			else {
				// Only 1 template pattern 
				return getTargetDefinitionsForTemplatePattern(arg);
			}
		}
	}
	
	private List<TargetDefinition> getTargetDefinitionsForTemplatePattern(String templateName) {
		// TODO
		return new LinkedList<TargetDefinition>();
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

	private void printTargetDefinition( List<TargetDefinition> targetDefinitions ) {
		for ( TargetDefinition td : targetDefinitions ) {
			//print(" . " + td.getTemplate() + " --> " + td.getFile() );
			print(" . [" + getTargetType(td) + "] " + td.getTemplate() );
		}
	}
	private String getTargetType(TargetDefinition td) {
		if ( td.isOnce() ) {
			return "1" ;
		}
		else if ( td.isResource() ) {
			return "R" ;
		}
		else {
			return "*" ;
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
