import geb.Browser

class GebRunnerDsl {

  GebRunnerDsl(Browser browser) {
    this.browser = browser
  }

  @Delegate WikipediaDsl wikipediaDsl = new WikipediaDsl(browser)
  @Delegate Browser browser

}
