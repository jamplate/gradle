# jamplate-gradle-plugin
 The gradle plugin for the jamplate pre-processor.

## How to apply
  Add this to your `build.gradle`
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