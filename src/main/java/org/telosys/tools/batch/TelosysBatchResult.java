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
package org.telosys.tools.batch;

import org.telosys.tools.generator.task.GenerationTaskResult;

public class TelosysBatchResult {

	private int numberOfFilesGenerated;

	private int numberOfGenerationErrors;

	private int numberOfResourcesCopied;

	private int numberOfBundlesUsed;

	public TelosysBatchResult() {
		super();
	}

	public void update(String bundleName, GenerationTaskResult generationTaskResult) {
		this.numberOfBundlesUsed++;
		this.numberOfFilesGenerated += generationTaskResult.getNumberOfFilesGenerated();
		this.numberOfGenerationErrors += generationTaskResult.getNumberOfGenerationErrors();
		this.numberOfResourcesCopied += generationTaskResult.getNumberOfResourcesCopied();
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
