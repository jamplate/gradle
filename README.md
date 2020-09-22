# Jamplate Gradle Plugin
 The gradle plugin for the [Jamplate Processor](https://github.com/cufyorg/jamplate-processor).

## How to apply using jitpack (for `0.0.x` versions)
  Add this to your `build.gradle`. [jitpack page](https://jitpack.io/#org.cufy/jamplate-gradle-plugin)
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
  		classpath 'org.cufy:jamplate-gradle-plugin:TAG'
  	}
  }
  ```

## How to apply using gradle (for `0.x.0` versions)
   Add this to your `build.gradle`. [more instructions](https://plugins.gradle.org/plugin/org.cufy.jamplate)
   ```groovy
   plugins {
        //Replace 'TAG' with the desired version
        id 'org.cufy.jamplate' version 'TAG'
   }
   ```

## How to use
  - Apply the plugin.
  - Create a new directory in your project's `src/main` or `src/test` directory.
  - Create your `package` directory like you would for any `java` source code.
  - Create a new file in that desired `package` with `.jamplate` extension.
  - Start coding in `jamplate`.
  - Keep in mind that the output of the processed file will be compiled as a `java` source-code.
  - Build your project using an `IDE`, or by typing `gradle build` or `gradlew build` in the terminal.

## Intellij Tips
  - If `Intellij` did not recognize the output files and gives you an error
    - Exit `Intellij`
    - Delete `.idea` folder in your project
    - Open `Intellij` again
  - If `Intellij` do not recognize `.jamplate` extension
    - Follow these instructions [Intellij.md](https://github.com/cufyorg/jamplate-processor/blob/master/Intellij.md)
