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
  		classpath 'org.cufy:jamplate-gradle-plugin:0.0.4'
  	}
  }
  ```