/*
 *	Copyright 2020 Cufy
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
import org.jamplate.impl.Jamplate
import org.jamplate.impl.Meta
import org.jamplate.impl.model.EnvironmentImpl
import org.jamplate.impl.model.FileDocument
import org.jamplate.model.Compilation
import org.jamplate.model.Document
import org.jamplate.model.Environment

@SuppressWarnings('GrMethodMayBeStatic')
class ProcessJamplateTask extends DefaultTask {
	@InputDirectory
	File input
	@OutputDirectory
	File output

	@TaskAction
	void processJamplate() throws IOException {
		jamplate(input, output)
	}

	protected static void jamplate(File input, File output) {
		Objects.requireNonNull(input, "input")
		Objects.requireNonNull(output, "output")

		Document[] documents = FileDocument.hierarchy(input)

		Environment environment = new EnvironmentImpl()

		environment.meta[Meta.PROJECT] = input
		environment.meta[Meta.OUTPUT] = output

		boolean compiled = Jamplate.compile(environment, documents)

		if (!compiled) {
			System.err.println("Compilation Error\n")
			environment.diagnostic.flush(true)
			return
		}

		Compilation[] jamplates = environment
				.compilationSet()
				.stream()
				.filter(compilation -> compilation.rootTree.document().toString().endsWith(".jamplate"))
				.toArray(Compilation[]::new)

		boolean executed = Jamplate.execute(environment, jamplates)

		if (!executed) {
			System.err.println("Runtime Error\n")
			environment.diagnostic.flush(true)
			return
		}

		environment.diagnostic.flush()
	}
}
