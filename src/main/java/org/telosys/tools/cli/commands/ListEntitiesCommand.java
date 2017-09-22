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

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.cli.CommandWithModel;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.CriteriaUtil;
import org.telosys.tools.cli.commons.EntityUtil;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

public class ListEntitiesCommand extends CommandWithModel {

	/**
	 * Constructor
	 * @param out
	 */
	public ListEntitiesCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "le";
	}

	@Override
	public String getShortDescription() {
		return "List Entities" ;
	}

	@Override
	public String getDescription() {
		return "List the entities for the current model";
	}
	
	@Override
	public String getUsage() {
		return "le [*|pattern|pattern1,pattern2,...]";
	}
	
	@Override
	public String execute(String[] args) {
		
//		if ( checkModelDefined() ) {
//			if ( args.length > 1 ) {
//				// le model-name
//				return listEntities(args[1]);
//			}
//			else {
//				// le => current model if defined
//				if ( checkModelDefined() ) {
//					return listEntities(getCurrentModel());
//				}				
//			}			
//		}
//		return null;
		if ( checkArguments(args, 0, 1) && checkHomeDirectoryDefined() && checkModelDefined() ) {
			return listEntities(args);
		}
		return null ;		
	}
	
//	private String listEntities(String modelName) {
//
//		// 1) try to get the file 
//		File modelFile = getModelFile(modelName); 
//		if ( modelFile != null ) {
//			// 2) try to load the model 
//			Model model = loadModel(modelFile);
//			if ( model != null ) {
//				printEntities(modelFile, model.getEntities());
//			}
//		}
//		return null ;
//	}
		
//	private String listEntities(String arg) {
//		Model model = loadCurrentModel();
//		if ( model != null ) {
//			printEntities(getCurrentModel(), model.getEntities());
//		}
//		return null ;
//	}
	
//	private void printEntities(File modelFile, List<Entity> entities) {
//		if ( entities != null && entities.size() > 0 ) {
//			print("Model '" + modelFile.getName() + "' contains " + entities.size() + " entities : " ) ;
//			for ( Entity e : entities ) {
//				print(" . " + e.getClassName() + " ( " + e.getFullName() + " )" ) ;
//			}
//		}
//		else {
//			print("Model '" + modelFile.getName() + "' is void (no entity)" ) ;
//		}
//	}

//	private void printEntities(String modelName, List<Entity> entities) {
//		if ( entities != null && entities.size() > 0 ) {
//			print("Model '" + modelName + "' contains " + entities.size() + " entities : " ) ;
//			for ( Entity e : entities ) {
//				print(" . " + e.getClassName() + " ( " + e.getFullName() + " )" ) ;
//			}
//		}
//		else {
//			print("Model '" + modelName + "' is void (no entity)" ) ;
//		}
//	}

	private String listEntities(String args[]) {
		Model model = loadCurrentModel();
		if ( model != null ) {
			List<Entity> entities = model.getEntities();
			List<String> criteria = CriteriaUtil.buildCriteriaFromArg(args.length > 1 ? args[1] : null) ;
			
			//List<TargetDefinition> selectedTargets = TargetUtil.filter(targetDefinitions.getTemplatesTargets(), criteria);
			List<Entity> selectedEntities = EntityUtil.filter(entities, criteria);

			print ( EntityUtil.buildListAsString(selectedEntities) );
			return null ;
		}
		return null ;
	}
}
