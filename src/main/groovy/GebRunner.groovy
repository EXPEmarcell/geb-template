package com.webonise.geb

import groovy.io.FileType
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import geb.Browser
import geb.Configuration
import geb.ConfigurationLoader
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
  File path = new File("src/run/geb").absoluteFile

  Object fileNameFilter = ~/.*\.geb/

  /**
  * Overload the {@code classLoader} setter to support generic Java classloaders
  */
  void setClassLoader(ClassLoader classLoader) {
    assert classLoader : "Attempted to set classloader to null"
    this.classLoader = new GroovyClassLoader(classLoader)
  }

  /**
  * Overload the {@code path} setter to support strings
  */
  void setPath(String pathString) {
    path = new File(pathString).absoluteFile
  }

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
    Configuration config = configuration

    Browser b = new Browser(config)
    GebRunnerDsl dsl = new GebRunnerDsl(b)

    try {
      path.traverse(
        type: FileType.FILES,
        nameFilter: fileNameFilter,
        preDir: { System.out.println("$it => Enter") },
        preDirRoot: true,
        postDir: { System.out.println("$it => Exit") },
        postDirRoot: true
      ) { File file ->
        System.out.println(file.absolutePath)

        DelegatingScript script = (DelegatingScript)sh.parse(file)

        try {
          b.clearCookiesQuietly()
          script.setDelegate(dsl)
          script.run()
          System.out.println(file.absolutePath  + " => SUCCESS!")
        } catch(Exception e) {
          System.out.println(file.absolutePath  + " => FAILURE!")
          System.out.println("<<<")
          e.printStackTrace(System.out)
          System.out.println(">>>")
        }
      }
    } finally {
      b.quit()
    }
  }

  static void main(String[] args) {
    String env = "development"
    if(args && args.length > 1 && args[0]) env = args[0]

    GebRunner runner = new GebRunner()
    runner.environment = env
    runner.run()
  }

}
