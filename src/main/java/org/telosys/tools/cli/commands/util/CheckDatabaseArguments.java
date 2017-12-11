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
package org.telosys.tools.cli.commands.util;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.api.MetaDataOptions;
import org.telosys.tools.api.MetaDataOptionsImpl;
import org.telosys.tools.commons.StrUtil;

public class CheckDatabaseArguments  {

	private Integer databaseId = null ;
	
	private MetaDataOptionsImpl options = new MetaDataOptionsImpl();
	
	private List<String> errors = new LinkedList<>();
	
	public CheckDatabaseArguments(String[] args) {
		for ( String arg : args ) {
			if ( arg.length() >= 2 && arg.charAt(0) == '-' ) {
				switch ( arg.substring(1) ) {
				case "i" :
					// -i : info
					options.setInfo(true);
					break;
					
				case "s" :
				case "sch" :
					// -s -sch : schemas
					options.setSchemas(true);
					break;

				case "ca" :
				case "cat" :
					// -ca -cat : catalogs
					options.setCatalogs(true);
					break;

				case "t" :
					// -t : tables
					options.setTables(true);
					break;
					
				case "c" :
				case "co" :
				case "col" :
					// -c -co -col : columns
					options.setColumns(true);
					break;
					
				case "f" :
				case "fk" :
					// -f -fk : columns
					options.setForeignKeys(true);
					break;
					
				case "p" :
				case "pk" :
					// -p -pk : columns
					options.setPrimaryKeys(true);
					break;
					
		        default:
		        	// -?: unknown arg
		        	errors.add("Invalid argument '" + arg+ "'");
				}
			}
			else {
				// supposed to be the database id
				int id = StrUtil.getInt(arg, -1);
				if ( id >= 0 ) {
					databaseId = id ;
				}
			}
		}
	}
	
	public MetaDataOptions getOptions() {
		return options;
	}

	public Integer getDatabaseId() {
		return databaseId;
	}

	public boolean hasErrors() {
		return ! errors.isEmpty() ;
	}

	public List<String> getErrors() {
		return errors;
	}
	
	
}
