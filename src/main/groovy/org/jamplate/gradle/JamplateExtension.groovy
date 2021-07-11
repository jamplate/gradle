/*
 *	Copyright 2021 Cufy
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
//file:noinspection GrMethodMayBeStatic
//file:noinspection GrMethodMayBeStatic
package org.jamplate.gradle

import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.jamplate.diagnostic.Diagnostic
import org.jamplate.diagnostic.Message
import org.jamplate.glucose.internal.memory.Address
import org.jamplate.glucose.spec.GlucoseSpec
import org.jamplate.impl.unit.Action
import org.jamplate.impl.unit.UnitImpl
import org.jamplate.unit.Unit

import static org.jamplate.glucose.internal.util.Values.text
import static org.jamplate.util.Specs.listener

class JamplateExtension {
	Map<String, Unit> units = new HashMap<>()

	Project project

	/**
	 * The factory passed by the plugin.
	 *
	 * @since 0.3.0 ~2021.07.11
	 */
	ObjectFactory factory

	JamplateExtension(Project project, ObjectFactory factory) {
		Objects.requireNonNull(project, "project")
		Objects.requireNonNull(factory, "factory")
		this.project = project
		this.factory = factory
	}

	Unit unit(
			String module,
			taskName = defaultTaskName(module),
			input = defaultInputFile(module),
			output = defaultOutputFile(module)
	) {
		return units.computeIfAbsent(module, {
			Unit unit = new UnitImpl(new GlucoseSpec())

			unit.spec.add listener({ event ->
				if (event.action == Action.PRE_EXEC) {
					event.memory.set Address.PROJECT, text(input)
					event.memory.set Address.OUTPUT, text(output)
				}
			})
			unit.spec.add(listener({ event ->
				if (event.action == Action.DIAGNOSTIC) {
					Diagnostic diagnostic = event.diagnostic

					if (!diagnostic.empty) {
						Message message = diagnostic.first()
						Throwable exception = message.exception

						diagnostic.flush(true)

						if (exception != null)
							throw exception
					}
				}
			}))

			//create the jamplate task
			project.tasks.create(taskName, ProcessJamplateTask) {
				it.input = input
				it.output = output
				it.unit = unit
			}

			return unit
		})
	}

	void java(
			String module,
			taskName = defaultTaskName(module),
			input = defaultInputFile(module),
			output = defaultOutputFile(module)
	) {
		SourceSet sourceSet = project.convention.getPlugin(JavaPluginConvention).sourceSets.getByName(module)

		//create new jamplates source set
		SourceDirectorySet jamSourceSet =
				factory
						.sourceDirectorySet(
								"${module}.jamplate",
								"Jamplate ${module} extends Java ${module}"
						)
						.srcDir input

		//register the source set
		sourceSet.allSource.source jamSourceSet

		//register the source set as a java source
		sourceSet.allJava.source jamSourceSet

		//register the folder containing the generated code as java source folder
		sourceSet.java.srcDir output

		//make the java compile task run after the jamplate task
		project.tasks.named(sourceSet.compileJavaTaskName) {
			it.dependsOn taskName
		}
	}

	void source(
			String module,
			taskName = defaultTaskName(module),
			input = defaultInputFile(module),
			output = defaultOutputFile(module)
	) {
		//create new jamplates source set
		factory
				.sourceDirectorySet(
						"${module}.jamplate",
						"Jamplate ${module}"
				)
				.srcDir input

		project.tasks.named('build') { it.dependsOn(taskName) }
	}

	protected String defaultTaskName(String module) {
		return "process${module == "main" ? "" : camelCase(module)}Jamplate"
	}

	protected File defaultInputFile(String module) {
		return new File(project.projectDir, "src/$module/jamplate")
	}

	protected File defaultOutputFile(String module) {
		return new File(project.buildDir, "jamplate/$module")
	}

	/**
	 * Return the given {@code string} formatted as {@code camelCase}.
	 *
	 * @param string the string to be formatted as {@code camelCase}.
	 * @return the given {@code string} formatted as {@code camelCase}.
	 * @throws NullPointerException if the given {@code string} is null.
	 * @since 0.0.1 ~2020.09.15
	 */
	protected static String camelCase(String string) {
		Objects.requireNonNull(string, "string")
		return string.length() <= 1 ?
			   string.toUpperCase() :
			   string.substring(0, 1)
					 .toUpperCase() +
			   string.substring(1, string.length())
					 .toLowerCase()
	}
}
