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
package org.telosys.tools.cli.commands;

import org.telosys.tools.api.TelosysApiVersion;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.Version;
import org.telosys.tools.commons.github.GitHubClient;
import org.telosys.tools.commons.http.HttpClient;
import org.telosys.tools.dsl.model.DslModelVersion;
import org.telosys.tools.generator.GeneratorVersion;
import org.telosys.tools.generic.model.GenericModelVersion;

import jline.console.ConsoleReader;

public class VerCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public VerCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "ver";
	}

	@Override
	public String getShortDescription() {
		return "Versions" ;
	}
	
	@Override
	public String getDescription() {
		return "Versions information";
	}
	
	@Override
	public String getUsage() {
		return "ver";
	}
			
	@Override
	public String execute(String[] args) {
		
		StringBuilder sb = new StringBuilder();
		
		appendLine(sb, "Components versions : "  );
		appendLine(sb, "Telosys CLI   : " + Version.TELOSYS_TOOLS_CLI_VERSION  );
		appendLine(sb, "Telosys API   : " + TelosysApiVersion.VERSION  );
		appendLine(sb, "Generator     : " + GeneratorVersion.GENERATOR_VERSION  );
		appendLine(sb, "Generic model : " + GenericModelVersion.VERSION  );
		appendLine(sb, "DSL model     : " + DslModelVersion.VERSION  );
//		appendLine(sb, "DB model      : " + DbModelVersion.VERSION  );
		appendLine(sb, "GitHub client : " + GitHubClient.VERSION ) ;
		appendLine(sb, "HTTP client   : " + HttpClient.VERSION ) ;
		
		return sb.toString();
	}

}
