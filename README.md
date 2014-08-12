Geb Template
=============

This is the template project for the [Geb](http://gebish.org) Selenium wrapper using Gradle.
This is not a project for when you want to use Geb to test your existing code; this is a project
for when the goal is to write Geb that will execute against some external application.

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
* `./src/run/geb/sample/bobby_fischer_page_via_dsl.geb`

Advanced Usage
==============

Adding Pages
--------------

You can add a Geb [Page](http://www.gebish.org/pages) by adding them into `./src/main/groovy`. You can test your page class using
[GebSpec](http://www.gebish.org/manual/current/testing.html#spock_junit__testng) by putting the test into `./src/test/groovy`. There is a sample page
at `./src/main/groovy/sample/WikipediaEnHomePage.groovy`, and a test for the sample page at `./src/test/groovy/sample/WikipediaEnHomePageTest.groovy`.

Adding to the DSL
-------------------

You can add methods to the DSL by building out `./src/main/groovy/GebRunnerDsl.groovy`. By default, it delegates to the running `geb.Browser` instance. You
can add more fields for it to delegate to (it ships with an example `WikipediaDsl` as a delegate), or you can add the methods direclty onto that class.

Adding Imports
---------------

If you want to add default imports for the scripts, you have to edit `GebRunner`. You will need to add an `ImportCustomizer` to the `CompilerConfiguration`.
For an example, see: http://mrhaki.blogspot.com/2011/06/groovy-goodness-add-imports.html
