Geb Template
=============

A template project for the Geb Selenium wrapper using Gradle. This is not a project for when you want to use Geb to test your existing code; this is a project
for when the goal is to write Geb that will execute.

Basic Usage
=============

1. Fork this repo.
1. Customize GebConfig.groovy to reflect your reality
1. Add your script (what would be the contents of the `Browser.drive { ... }` closure) as a `.geb` file somewhere under `./src/test/geb`.
1. Run `gradlew run`
1. Profit!
