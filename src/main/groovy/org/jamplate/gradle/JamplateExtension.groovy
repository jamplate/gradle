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
package org.jamplate.gradle

import org.jamplate.model.Compilation
import org.jamplate.model.Memory
import org.jamplate.model.Value
import org.json.JSONArray
import org.json.JSONObject

import java.util.function.Function

class JamplateExtension {
	Map<String, Object> defaultMemory = new HashMap<>()
	Function<Compilation, Memory> memorySupplier

	void memory(String address, String value) {
		defaultMemory.put(address, value)
	}

	void memory(String address, Number number) {
		defaultMemory.put(address, number)
	}

	void memory(String address, Closure value) {
		defaultMemory.put address, (Value) {
			memory ->
				Object v = value(memory)

				if (v instanceof Map)
					return new JSONObject((Map) v).toString()
				if (v instanceof Collection)
					return new JSONArray((Collection) v).toString()
				if (v instanceof String)
					return v

				return String.valueOf(v)
		}
	}

	void memory(String address, List value) {
		defaultMemory.put(address, value)
	}

	void memory(String address, Map value) {
		defaultMemory.put(address, new JSONObject(value))
	}

	void memory(Map map) {
		defaultMemory.putAll(map)
	}

	void replaceMemorySupplier(Function<Compilation, Memory> memorySupplier) {
		this.memorySupplier = memorySupplier
	}
}
