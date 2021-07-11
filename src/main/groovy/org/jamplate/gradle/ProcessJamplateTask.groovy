/*
 *	Copyright 2020-2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.jamplate.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jamplate.impl.document.FileDocument
import org.jamplate.model.Document
import org.jamplate.unit.Unit

import java.util.stream.Collectors

@SuppressWarnings('GrMethodMayBeStatic')
class ProcessJamplateTask extends DefaultTask {
	@InputDirectory
	File input
	@OutputDirectory
	File output

	protected Unit unit

	@TaskAction
	void processJamplate() throws IOException {
		Objects.requireNonNull(input, "input")
		Objects.requireNonNull(output, "output")

		Document[] documents = FileDocument.hierarchy(input)

		Unit unit = this.unit

		unit.initialize(documents) &&
		unit.parse(documents) &&
		unit.analyze(documents) &&
		unit.compile(documents) &&
		unit.execute(
				documents.toList()
						 .stream()
						 .filter({
							 d -> d.toString().endsWith(".jamplate")
						 })
						 .collect(Collectors.toList())
		)
		unit.diagnostic()
	}
}
