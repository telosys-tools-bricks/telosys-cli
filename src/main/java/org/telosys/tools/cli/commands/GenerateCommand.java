package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.CriteriaUtil;
import org.telosys.tools.cli.commons.EntityUtil;
import org.telosys.tools.cli.commons.TargetUtil;
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
			// Check arguments :
			// 1 : gen -r
			// 2 : gen * * 
			// 3 : gen * * -r 
			if ( checkArguments(args, 1, 2, 3 ) ) {
				generate(args);
				return null;
			}
//			else {
//				return invalidUsage("Usage : " + getUsage() );
//			}
		}
		return null ;
	}

	/**
	 * Generation entry point
	 * @param args all the arguments as provided by the command line (0 to N)
	 */
	private void generate(String[] args)  {
		GenerationTaskResult result = null ;
		try {
			if ( args.length == 3 ) {
				// gen * * 
				result = generate(args[1], args[2], false);
			}
			else if ( args.length == 4 ) {
				// gen * * -r 
				if ( checkResourcesOption(args[3]) ) {
					result = generate(args[1], args[2], true);
				}
			}
			else if ( args.length == 2 ) {
				// gen -r 
				if ( checkResourcesOption(args[1]) ) {
					// TODO : result = generateResources();
				}
			}
			
			if ( result != null ) {
				printResult(result);
			}
		} catch (TelosysToolsException e) {
			printError(e);
		} catch (GeneratorException e) {
			printError(e);
		}
	}
	
	private boolean checkResourcesOption(String option) {
		if ( "-r".equals(option) ) {
			return true ;
		}
		else {
			print("Invalid argument '" + option + "' ( '-r' expected ) ");
			return false ;
		}
	}
	
	/**
	 * @param argEntityNames argument for entities ( eg '*', 'Car', 'Car,Driver', etc )
	 * @param argTemplateNames argument for templates ( eg '*', 'CacheFilter_java.vm', '_java,_xml', etc )
	 * @param flagResources resources generation flag : true = generate resources
	 * @return 
	 * @throws TelosysToolsException
	 * @throws GeneratorException
	 */
	private GenerationTaskResult generate(String argEntityNames, String argTemplateNames, 
			boolean flagResources) throws TelosysToolsException, GeneratorException {
		
		TelosysProject telosysProject = getTelosysProject();
		// Loads the model for the current model name
		Model model = loadCurrentModel();
		//List<String> entityNames = buildEntityNames(argEntityNames, model);
		List<Entity> entities = buildEntitiesList(argEntityNames, model);
		
		String bundleName = getCurrentBundle() ;
		List<TargetDefinition> targetDefinitions = buildTargetsList(argTemplateNames);
		
		print("Entities ( model = '"+getCurrentModel()+"' ) : ");
		print ( EntityUtil.buildListAsString(entities) );
		List<String> entityNames = EntityUtil.toEntityNames(entities);

		print("Templates ( bundle = '"+bundleName+"' ) : ");
		print ( TargetUtil.buildListAsString(targetDefinitions) );

		print("Copy resources : " + ( flagResources ? "yes" : "no" ));

		if ( entityNames.size() == 0 || targetDefinitions.size() == 0 ) {
			print("No entity or no templates => nothing to generate ");
			return null ;
		}
		else {
			if ( confirm("Do you want to launch the generation") ) {
				print("Generation in progress...");
				return telosysProject.launchGeneration(model, entityNames, bundleName, targetDefinitions, flagResources);			
			}
			else {
				print("Generation canceled.");
				return null ;
			}
		}
	}

	/**
	 * Builds a list of entities using the given argument ( eg : '*', 'Car', 'Car,Dog', 'Dog,Driver,Car' )
	 * @param arg
	 * @param model
	 * @return
	 */
//	private List<String> buildEntityNames(String arg, Model model) throws TelosysToolsException {
	private List<Entity> buildEntitiesList(String arg, Model model) throws TelosysToolsException {
//		List<String> list = new LinkedList<String>();
//		if ( "*".equals(arg) ) {
//			// All entities 
//			for ( Entity entity : model.getEntities() ) {
//				list.add(entity.getClassName());
//			}
//		}
//		else if ( arg.contains(",") ) {
//			// Many entity names : eg 'Car,Driver,Dog'
//			String[] array = StrUtil.split(arg, ',' );
//			for ( String s : array ) {
//				String entityName = s.trim();
//				if ( entityName.length() > 0 ) {
//					checkEntityExists(entityName, model);
//					list.add(entityName);
//				}
//			}
//		}
//		else {
//			// Only 1 entity name
//			checkEntityExists(arg, model);
//			list.add(arg);
//		}
//		return list ;
		
		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(arg) ;
		return EntityUtil.filter(model.getEntities(), criteria);
	}
	
//	private void checkEntityExists(String entityName, Model model) {
//		if ( model.getEntityByClassName(entityName) == null ) {
//			throw new CancelCommandException("Unknown entity '" + entityName + "'");
//		}
//	}
	
	/**
	 * Returns a list of TargetDefinitions for the given argument <br>
	 * 
	 * @param arg can be '*' or a single 'pattern' or a list of 'patterns' ( eg '*' or 'record' or 'record,resource' )
	 * @return
	 * @throws TelosysToolsException
	 */
	private List<TargetDefinition> buildTargetsList(String arg) throws TelosysToolsException {

		TargetsDefinitions targetDefinitions = getCurrentTargetsDefinitions();
		List<String> criteria = CriteriaUtil.buildCriteriaFromArg(arg) ;
		return TargetUtil.filter(targetDefinitions.getTemplatesTargets(), criteria);
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
