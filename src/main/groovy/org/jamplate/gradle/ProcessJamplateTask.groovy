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
import org.jamplate.diagnostic.Message
import org.jamplate.impl.Jamplate
import org.jamplate.impl.Meta
import org.jamplate.impl.model.EnvironmentImpl
import org.jamplate.impl.model.FileDocument
import org.jamplate.model.Compilation
import org.jamplate.model.Document
import org.jamplate.model.Environment
import org.jamplate.model.Memory

import java.util.function.Function

@SuppressWarnings('GrMethodMayBeStatic')
class ProcessJamplateTask extends DefaultTask {
	@InputDirectory
	File input
	@OutputDirectory
	File output

	protected Map<String, Object> defaultMemory
	protected Function<Compilation, Memory> memorySupplier

	@TaskAction
	void processJamplate() throws IOException {
		Objects.requireNonNull(input, "input")
		Objects.requireNonNull(output, "output")

		Document[] documents = FileDocument.hierarchy(input)

		Environment environment = new EnvironmentImpl()

		environment.meta[Meta.MEMORY] = defaultMemory
		environment.meta[Meta.PROJECT] = input
		environment.meta[Meta.OUTPUT] = output

		boolean compiled = Jamplate.compile(environment, documents)

		if (!compiled) {
			System.err.println("Compilation Error\n")
			Message cause = environment.diagnostic.first()
			environment.diagnostic.flush()
			throw cause.exception
		}

		Compilation[] jamplates = environment
				.compilationSet()
				.stream()
				.filter({ compilation -> compilation.rootTree.document().toString().endsWith(".jamplate") })
				.toArray({ len -> new Compilation[len] })

		boolean executed = this.memorySupplier == null ?
						   Jamplate.execute(environment, jamplates) :
						   Jamplate.execute(environment, this.memorySupplier, jamplates)

		if (!executed) {
			System.err.println("Runtime Error\n")
			Message cause = environment.diagnostic.first()
			environment.diagnostic.flush()
			throw cause.exception
		}

		environment.diagnostic.flush()
	}
}
