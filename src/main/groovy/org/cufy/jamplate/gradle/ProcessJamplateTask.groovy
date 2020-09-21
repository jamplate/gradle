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
package org.cufy.jamplate.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jamplate.Jamplate

@SuppressWarnings('GrMethodMayBeStatic')
class ProcessJamplateTask extends DefaultTask {
	@InputDirectory
	File input
	@OutputDirectory
	File output

	@TaskAction
	void processJamplate() throws IOException {
		this.process(input, output)
	}

	protected void process(File input, File output) {
		Objects.requireNonNull(input, "input")
		Objects.requireNonNull(output, "output")

		if (input.isDirectory())
			processTree(input, output)
		else if (input.exists())
			processFile(input, output)
	}

	protected void processTree(File inputTree, File outputTree) {
		Objects.requireNonNull(inputTree, "inputTree")
		Objects.requireNonNull(outputTree, "outputTree")

		for (File input : inputTree.listFiles()) {
			File output = new File(outputTree, input.name.replaceAll('[.]jamplate$', '.java'))

			System.out.println(output)

			process(input, output)
		}
	}

	protected void processFile(File input, File output) {
		Objects.requireNonNull(input, "input")
		Objects.requireNonNull(output, "output")

		jamplate(input, output)
	}

	protected static void jamplate(File input, File output) {
		Objects.requireNonNull(input, "input")
		Objects.requireNonNull(output, "output")

		output.parentFile.mkdirs()
		Jamplate.process(input, output, Collections.emptyMap())
	}

//	protected static void copy(File input, File output) {
//		Objects.requireNonNull(input, "input")
//		Objects.requireNonNull(output, "output")
//		output.parentFile.mkdirs()
//
//		FileInputStream inputStream
//		FileOutputStream outputStream
//		try {
//			inputStream = new FileInputStream(input)
//			outputStream = new FileOutputStream(output)
//			byte[] buffer = new byte[1024]
//
//			while (true) {
//				int length = inputStream.read(buffer)
//
//				if (length == -1)
//					break
//
//				outputStream.write(buffer, 0, length)
//			}
//		} finally {
//			if (inputStream != null)
//				inputStream.close()
//			if (outputStream != null)
//				outputStream.close()
//		}
//	}
}
