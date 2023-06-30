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
package org.telosys.tools.batch.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.telosys.tools.generator.task.GenerationTaskResult;

public class BatchGenResult {

	private final Set<String> models;
	private final Set<String> bundles;
	private final Map<String,GenerationTaskResult> results;
	private int numberOfFilesGenerated;
	private int numberOfGenerationErrors;
	private int numberOfResourcesCopied;

	public BatchGenResult() {
		super();
		this.models  = new HashSet<>();
		this.bundles = new HashSet<>();
		this.results = new HashMap<>();
	}

	public void updateCurrentModel(String modelName) {
		models.add(modelName); // Add if not already present
	}
	public void updateCurrentBundle(String bundleName) {
		bundles.add(bundleName); // Add if not already present
	}
	
	public void update(String modelName, String bundleName, GenerationTaskResult generationTaskResult) {
		String key = "(" + modelName + ") " + bundleName;
		this.results.put(key, generationTaskResult);
		this.numberOfFilesGenerated += generationTaskResult.getNumberOfFilesGenerated();
		this.numberOfGenerationErrors += generationTaskResult.getNumberOfGenerationErrors();
		this.numberOfResourcesCopied += generationTaskResult.getNumberOfResourcesCopied();
	}
	
	public List<String> getBundlesStatus() {
		List<String> list = new LinkedList<>();
		SortedSet<String> keys = new TreeSet<>(results.keySet());
		for (String key : keys) {
			GenerationTaskResult r = results.get(key);
			StringBuilder sb = new StringBuilder();
			if ( r.getNumberOfGenerationErrors() > 0 ) {
				sb.append("(!) ");
			} else {
				sb.append("    ");
			}
			sb.append(key).append(" : ");
			sb.append(r.getNumberOfFilesGenerated()).append(" files generated, ");
			sb.append(r.getNumberOfGenerationErrors()).append(" error(s)");
			list.add(sb.toString());
		}
		return list;
	}
	
	public int getNumberOfModelsUsed() {
		return this.models.size();
	}

	public int getNumberOfBundlesUsed() {
		return this.bundles.size();
	}

	public int getNumberOfFilesGenerated() {
		return numberOfFilesGenerated;
	}

	public int getNumberOfGenerationErrors() {
		return numberOfGenerationErrors;
	}

	public int getNumberOfResourcesCopied() {
		return numberOfResourcesCopied;
	}

}
