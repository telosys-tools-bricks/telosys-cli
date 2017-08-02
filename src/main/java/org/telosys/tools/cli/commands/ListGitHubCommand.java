package org.telosys.tools.cli.commands;

import java.util.List;

import jline.console.ConsoleReader;

import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.cli.Command;
import org.telosys.tools.cli.Environment;
import org.telosys.tools.cli.commons.GitHubBundlesUtil;
import org.telosys.tools.commons.TelosysToolsException;

public class ListGitHubCommand extends Command {
	
	/**
	 * Constructor
	 * @param out
	 */
	public ListGitHubCommand(ConsoleReader consoleReader, Environment environment) {
		super(consoleReader, environment);
	}
	
	@Override
	public String getName() {
		return "lgh";
	}

	@Override
	public String getShortDescription() {
		return "List GitHub" ;
	}
	
	@Override
	public String getDescription() {
		return "List the content of the GitHub store";
	}
	
	@Override
	public String getUsage() {
		return "lgh [name-part1 name-part2 ...]";
	}
	
	@Override
	public String execute(String[] args) {
		if ( checkHomeDirectoryDefined() ) {
			if ( checkGitHubStoreDefined() ) {
				return listContent(getCurrentGitHubStore(), args);
			}
		}
		return null ;
	}
	
	private String listContent(String githubStoreName, String[] args) {
		
		List<String> bundles = getBundles(githubStoreName, args) ;
		return printBundles(githubStoreName, bundles);
	}
	
	private List<String> getBundles(String githubStoreName, String[] args) {
		List<String> criteria = GitHubBundlesUtil.buildCriteria(args);
		TelosysProject telosysProject = getTelosysProject();
		try {
			return GitHubBundlesUtil.getBundles(telosysProject, githubStoreName, criteria);
		} catch (TelosysToolsException e) {
			printError(e);
			return null ;
		}
	}
	
	/**
	 * Prints the given bundles
	 * @param githubStore
	 * @param bundles
	 * @return
	 */
	private String printBundles(String githubStore, List<String> bundles) {
		StringBuffer sb = new StringBuffer();
		if ( bundles != null && bundles.size() > 0 ) {
			appendLine(sb, "Bundles found in GitHub store '" + githubStore + "' : ");
			for ( String s : bundles ) {
				appendLine(sb, " . " + s);
			}
		}
		else {
			appendLine(sb, "No bundle found in GitHub store '" + githubStore + "'.");
		}
		return sb.toString();
	}

	
}
