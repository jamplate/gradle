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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPlugin

import javax.inject.Inject

/**
 * The {@code jamplate} gradle plugin.
 *
 * @since 0.0.1 ~2020.09.15
 */
class JamplatePlugin implements Plugin<Project> {
	/**
	 * The {@link ObjectFactory} passed when constructing this class.
	 *
	 * @since 0.0.1 ~2020.09.15
	 */
	final ObjectFactory factory

	@Inject
	JamplatePlugin(ObjectFactory factory) {
		this.factory = factory
	}

	@Override
	void apply(Project project) {
		project.plugins.apply JavaPlugin

		project.extensions.create 'jamplate', JamplateExtension, project, factory
	}
}
