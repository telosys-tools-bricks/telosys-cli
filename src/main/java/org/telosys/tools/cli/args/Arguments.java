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
package org.telosys.tools.cli.args;

import java.util.Hashtable;
import java.util.Map;

public class Arguments {
	
	public static final String H_ARG  = "-h" ;
	public static final String I_ARG  = "-i" ;
	public static final String O_ARG  = "-o" ;

	private final Map<String,Argument> args = new Hashtable<>();

	public Arguments() {
		super();
	}

	public void put(Argument arg) {
		args.put(arg.getName(), arg);
	}
	
	public String[] getNames() {
		return args.keySet().toArray(new String[0]);
	}

	public Argument getArgument(String name) {
		return args.get(name);
	}

}
