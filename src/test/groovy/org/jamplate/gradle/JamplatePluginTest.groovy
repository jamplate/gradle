package org.jamplate.gradle

import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JamplatePluginTest {
	@Test
	void test() {
		Assert.assertTrue(true)

		x() &&
		y() &&
		z()
		n()
	}

	boolean n() {
		println "N"
		return true
	}

	boolean x() {
		println "X"
		return false
	}

	boolean y() {
		println "Y"
		return true
	}

	boolean z() {
		println "Z"
		return false
	}
}
