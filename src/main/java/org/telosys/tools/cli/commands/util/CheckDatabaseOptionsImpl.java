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

import org.telosys.tools.api.MetaDataOptions;

public class CheckDatabaseOptionsImpl implements MetaDataOptions {

	private boolean info    = false ;
	
	private boolean catalogs = false ;
	private boolean schemas  = false ;

	private boolean tables  = false ;
	private boolean columns = false ;
	private boolean primaryKeys = false ;
	private boolean foreignKeys = false ;
	
	@Override
	public boolean hasOptions() {
		return info || catalogs || schemas || tables || columns || primaryKeys || foreignKeys ;
	}
	
	@Override
	public boolean isInfo() {
		return info;
	}
	public void setInfo(boolean info) {
		this.info = info;
	}
	
	@Override
	public boolean isTables() {
		return tables;
	}
	public void setTables(boolean tables) {
		this.tables = tables;
	}
	
	@Override
	public boolean isColumns() {
		return columns;
	}
	public void setColumns(boolean columns) {
		this.columns = columns;
	}

	@Override
	public boolean isSchemas() {
		return this.schemas;
	}
	public void setSchemas(boolean schemas) {
		this.schemas = schemas;
	}

	@Override
	public boolean isCatalogs() {
		return this.catalogs;
	}
	public void setCatalogs(boolean catalogs) {
		this.catalogs = catalogs;
	}
	
	@Override
	public boolean isPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(boolean primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	@Override
	public boolean isForeignKeys() {
		return foreignKeys;
	}
	public void setForeignKeys(boolean foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
	
}
