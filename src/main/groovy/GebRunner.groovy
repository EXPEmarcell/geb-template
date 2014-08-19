package com.webonise.geb

import java.util.regex.Pattern

import groovy.io.FileType
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import geb.Browser
import geb.Configuration
import geb.ConfigurationLoader
import geb.Page
import geb.PageChangeListener
import groovy.util.DelegatingScript
import org.codehaus.groovy.control.CompilerConfiguration

/**
* Class that runs a bunch of geb scripts.
*/
class GebRunner {

  /**
  * The classloader to use for loading the configuration, browser, etc.
  */
  GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)

  /**
  * The environment to pass to the configuration
  */
  String environment = "development"

  /**
  * The path to the configuration files and the scripts
  */
  File path = new File("src/run/geb")

  /**
  * Filter for Geb files
  */
  Pattern fileNameFilter = ~/.*\.geb/

  /**
  * The requested file to execute
  */
  String requestedFileName = null

  /**
  * Provides the URL of the Geb Configuration
  */
  URL getGebConfigURL() {
    return new File(path, "GebConfig.groovy").toURL()
  }

  /**
  * Construct the Geb configuration.
  */
  Configuration getConfiguration() {
    ConfigurationLoader loader = new ConfigurationLoader(environment, System.properties, classLoader)
    return loader.getConf(gebConfigURL, classLoader)
  }

  /**
  * Construct a Groovy shell
  */
  GroovyShell getGroovyShell() {
    CompilerConfiguration cc = new CompilerConfiguration()
    cc.scriptBaseClass = DelegatingScript.name
    cc.defaultScriptExtension = "geb"
    cc.optimizationOptions = ["all": false, "int":false]
    cc.tolerance = 0
    cc.verbose = true

    GroovyShell shell = new GroovyShell(classLoader, new Binding(), cc)
    return shell
  }

  /**
  * Constructs a Groovy shell and Geb configuration, and then executes the Geb test on each file in {@code path}.
  */
  void run() {
    GroovyShell sh = groovyShell

    Map<File,DelegatingScript> scripts = [:]

    Browser b = null
    try {
      path.traverse(
        type: FileType.FILES,
        nameFilter: fileNameFilter,
        preDir: { System.out.println("$it => Parse Enter") },
        preDirRoot: true,
        postDir: { System.out.println("$it => Parse Exit") },
        postDirRoot: true
      ) { File file ->
        System.out.println("$file => Parsing script")
        scripts[file] = sh.parse(file)
        System.out.println("$file => Parsed script")
      }

      Configuration config = configuration
      b = new Browser(config)
      b.registerPageChangeListener({ Browser browser, Page oldPage, Page newPage ->
        if(!oldPage) {
          System.out.println("\tStarted browser")
        } else {
          System.out.println("\tBrowsed from $oldPage to $newPage")
        }
      } as PageChangeListener)
      GebRunnerDsl dsl = new GebRunnerDsl(b)

      scripts.each { File file, DelegatingScript script ->
        try {
          System.out.println("$file => Setup script")
          (b.availableWindows as List).tail()?.each { String window ->
            b.withWindow(window) {
              b.close()
            }
          }
          b.withWindow(b.availableWindows[0]) {
            b.driver.manage().window().maximize()
            b.clearCookiesQuietly()
            if(requestedFileName && !file.absolutePath.toLowerCase().contains(requestedFileName.toLowerCase())) {
              System.out.println("$file => Not requested -- looking for: $requestedFileName")
            } else {
              System.out.println("$file => Running script")
              script.setDelegate(dsl)
              script.run()
              System.out.println("$file => SUCCESS!")
            }
          }
        } catch(Exception e) {
          System.out.println("$file => FAILURE!")
          System.out.println("<<<")
          e.printStackTrace(System.out)
          System.out.println(">>>")
        }
      }
    } finally {
      b?.quit()
    }
  }

  static void main(String[] args) {
    GebRunner runner = new GebRunner()
    runner.environment = System.properties["env"] ?: runner.environment
    runner.path = new File(System.properties["dir"] ?: runner.path.absolutePath)
    runner.requestedFileName = System.properties["file"] ?: null
    runner.run()
  }

}
