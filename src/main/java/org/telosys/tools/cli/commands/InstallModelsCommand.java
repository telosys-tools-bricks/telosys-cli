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

import java.util.List;

import org.telosys.tools.api.InstallationType;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Color;
import org.telosys.tools.cli.CommandLevel2;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.commons.Filter;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;

import jline.console.ConsoleReader;

/**
 * Install Model(s) Examples : im * -> install all bundles from GitHub im car
 * --> install all the bundles containing "java" or "rest"
 * 
 * @author Laurent GUERIN
 *
 */
public class InstallModelsCommand extends CommandLevel2 {

	/**
	 * Constructor
	 * 
	 * @param out
	 */
	public InstallModelsCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}

	@Override
	public String getName() {
		return "im";
	}

	@Override
	public String getShortDescription() {
		return "Install Models";
	}

	@Override
	public String getDescription() {
		return "Install models from the depot ";
	}

	@Override
	public String getUsage() {
		return "im [name-part1 name-part2 ...]";
	}

	@Override
	public String execute(String[] args) {

		if (args.length > 1) {
			// ib aaa bbb
			if (checkHomeDirectoryDefined() && checkGitHubStoreDefined()) {
				install(args);
			}
			return null;
		} else {
			// ib
			return "Invalid usage";
		}
	}

	private void install(String[] args) {
		// SUGGESTION: if already exists : prompt "overwrite ? [y/n] : "
//		TelosysProject telosysProject = getTelosysProject();

		String depotName = getCurrentGitHubStore();

//		DepotResponse depotResponse;
		try {
			DepotResponse depotResponse = getTelosysProject().getModelsAvailableInDepot(depotName); 
			if (isDepotResponseOK(depotResponse)) {
				List<String> criteria = buildCriteriaFromArgs(args);
				filterAndInstallSearchResult("model", depotName, depotResponse, criteria);
			}
		} catch (TelosysToolsException e) {
			printError(e);
		}
//		// Filter bundles names if args
//		List<String> allBundles = depotResponse.getElementNames();
//		List<String> bundles = Filter.filter(allBundles, buildCriteriaFromArgs(args));
//
//		// Install bundles
//		if (bundles != null && !bundles.isEmpty()) {
//			print("Installing " + bundles.size() + " bundle(s) from repository... ");
//			for (String bundleName : bundles) {
//				try {
//					if (telosysProject.downloadAndInstallBundle(githubStoreName, bundleName)) {
//						print(" . '" + bundleName + "' : installed. ");
//					} else {
//						print(" . '" + bundleName + "' : not installed (already exists). ");
//					}
//				} catch (TelosysToolsException e) {
//					print(" . '" + bundleName + "' : ERROR : " + e.getMessage());
//				}
//			}
//		} else {
//			print("No bundle found in repository.");
//		}
	}

	protected void filterAndInstallSearchResult(String elementTypeName, String depotName, DepotResponse depotResponse, List<String> criteria) {
		// Filter elements (models or bundles) with criteria if any
		List<String> elements = Filter.filter(depotResponse.getElementNames(), criteria);
		if ( ! elements.isEmpty()) {
			print("Installing " + elements.size() + " " + elementTypeName + "(s) from depot... ");
			for (String element : elements) {
				try {
					if ( getTelosysProject().downloadAndInstall(depotName, element, InstallationType.MODEL) ) {
						print(" . '" + element + "' : installed. ");
					} else {
						print(" . '" + element + "' : not installed (already exists). ");
					}
				} catch (TelosysToolsException e) {
					print(" . '" + element + "' : ERROR : " + e.getMessage());
				}
			}
		} else {
			print("No " + elementTypeName + " found in depot.");
		}
	}

	protected boolean isDepotResponseOK(DepotResponse depotResponse) {
		if (depotResponse.getHttpStatusCode() == 200) {
			return true; // OK
		} else {
			if (depotResponse.getHttpStatusCode() == 403) {
				String msg = "API request refused : http status '" + depotResponse.getHttpStatusCode() + "'";
				print(Color.colorize(msg, Color.RED_BRIGHT));
				DepotRateLimit rateLimit = depotResponse.getRateLimit();
				print("API rate limit status : ");
				print(" . remaining : " + rateLimit.getRemaining());
				print(" . limit     : " + rateLimit.getLimit());
				print(" . reset     : " + rateLimit.getReset());
			} else {
				String msg = "Unexpected http status '" + depotResponse.getHttpStatusCode() + "'";
				print(Color.colorize(msg, Color.RED_BRIGHT));
				print("Depot: " + depotResponse.getDepotName());
				print("URL: " + depotResponse.getDepotURL());
			}
			return false; // NOT OK
		}
	}
}
