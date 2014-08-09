Geb Template
=============

A template project for the [Geb](http://gebish.org) Selenium wrapper using Gradle.
This is not a project for when you want to use Geb to test your existing code; this is a project
for when the goal is to write Geb that will execute.

Basic Usage
=============

1. Install [Firefox](http://www.mozilla.org/en-US/firefox/new/)
1. Fork this repo
1. Customize GebConfig.groovy to reflect your reality
1. Add your script (what would be the contents of the `Browser.drive { ... }` closure) as a `.geb` file somewhere under `./src/run/geb`.
1. Run `gradlew run`
1. Profit!

Wait, What?
=============

If that made no sense to you, check out the following resources:

* [Geb's Excellent Manual](http://www.gebish.org/manual/current/all.html)
* [Gradle's Excellent Manual](http://www.gradle.org/docs/current/userguide/userguide_single.html)
* [Spock's "Spock Basics" Wiki Page](http://code.google.com/p/spock/wiki/SpockBasics)
* [Spock's Aspiring Manual](http://docs.spockframework.org/en/latest/)
* `./src/run/geb/sample/bobby_fischer_page.geb`
