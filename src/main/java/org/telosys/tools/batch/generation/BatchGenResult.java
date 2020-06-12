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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.generator.task.GenerationTaskResult;

public class BatchGenResult {

	private int numberOfFilesGenerated;

	private int numberOfGenerationErrors;

	private int numberOfResourcesCopied;

	private int numberOfBundlesUsed;

	private final Map<String,GenerationTaskResult> results;

	public BatchGenResult() {
		super();
		this.results = new HashMap<>();
	}

	public void update(String bundleName, GenerationTaskResult generationTaskResult) {
		this.numberOfBundlesUsed++;
		this.results.put(bundleName, generationTaskResult);
		this.numberOfFilesGenerated += generationTaskResult.getNumberOfFilesGenerated();
		this.numberOfGenerationErrors += generationTaskResult.getNumberOfGenerationErrors();
		this.numberOfResourcesCopied += generationTaskResult.getNumberOfResourcesCopied();
	}
	
	public List<String> getBundlesStatus() {
		List<String> list = new LinkedList<>();
		for ( Map.Entry<String, GenerationTaskResult> e : results.entrySet() ) {
			GenerationTaskResult r = e.getValue();
			StringBuilder sb = new StringBuilder();
			if ( r.getNumberOfGenerationErrors() > 0 ) {
				sb.append("(!) ");
			} else {
				sb.append("    ");
			}
			sb.append(e.getKey()).append(" : ");
			sb.append(r.getNumberOfFilesGenerated()).append(" files generated, ");
			sb.append(r.getNumberOfGenerationErrors()).append(" error(s)");
			list.add(sb.toString());
		}
		return list;
	}
	
	public int getNumberOfBundlesUsed() {
		return numberOfBundlesUsed;
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
