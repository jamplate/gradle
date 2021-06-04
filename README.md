# Jamplate Gradle Plugin [![](https://jitpack.io/v/org.jamplate/gradle.svg)](https://jitpack.io/#org.jamplate/gradle)

The gradle plugin for [Jamplate](https://github.com/jamplate/processor).

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

```gradle
jamplate {
    //here you can put all the default mappigns you want
    memory 'Text', 'Value'
    memory 'Object', '{"Key": "Value"}'
    memory 'Array', '["item1", "item2"]'
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
