# Jamplate Gradle Plugin [![](https://jitpack.io/v/org.jamplate/gradle.svg)](https://jitpack.io/#org.jamplate/gradle)

The gradle plugin for [Jamplate](https://github.com/jamplate/processor). For more
information please visit: [jamplate.org](https://jamplate.org)

## How to apply using jitpack

Add this to your `build.gradle`.

```groovy
apply plugin: 'java'
apply plugin: 'jamplate'

buildscript() {
	repositories {
		maven {
			url 'https://jitpack.io'
		}
	}

	dependencies {
		//replace 'TAG' with the desired version
		classpath 'org.jamplate:gradle:TAG'
	}
}
```

### How to use

- Apply the plugin.
- Create a new directory named `jamplate` in your project's `src/main` or `src/test`
  directory.
- Create your `package` directory like you would for any `java` source code.
- Create a new file in that desired `package` with `.jamplate` extension.
- Start coding in `jamplate`.
- Keep in mind that the output of the processed file will be compiled as a `java`
  source-code.
- Build your project using an `IDE`, or by typing `gradle build` or `gradlew build` in the
  terminal.

### Configure the Default memory

```groovy
//utility imports
import static org.jamplate.glucose.internal.util.Values.*
import static org.jamplate.impl.unit.Action.*
import static org.jamplate.util.Specs.*

jamplate {
	//with the method `unit`, you create a new task for the module you give it to it
	//the method will return the created unit object for that module  
	unit('main').spec.add(listener({ event ->
		if (event.action == PRE_EXEC) {
			//here you can put all the default mappings you want
			event.memory.set 'Text', text('Value')
			event.memory.set 'Object', object(["Key": "Value"])
			event.memory.set 'Array', array(["item1", "item2"])
			event.memory.set 'Dynamic', { memory -> 'Dynamic Variable' }
			event.memory.set "Random", { '' + (long) (Math.random() * (1L << 60)) }
			event.memory.set "Document", { '' + it.frame.instruction?.tree?.document() }
		}
	}))
    //with this method, the unit output will be treated as generated java source
    java('main')
    //with this method, the unit output will be treated as generated text
    source('main')
}
```

### Intellij Tips

- If `Intellij` did not recognize the output files and gives you an error
    - Exit `Intellij`
    - Delete `.idea` folder in your project
    - Open `Intellij` again
- If `Intellij` do not recognize `.jamplate` extension
    - Follow these instructions
      [Intellij.md](https://github.com/jamplate/processor/blob/master/Intellij.md)
