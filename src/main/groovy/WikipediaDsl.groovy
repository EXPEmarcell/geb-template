import geb.Browser
import groovy.transform.Canonical

@Canonical
class WikipediaDsl {

  @Delegate Browser browser

  void searchWikipediaEn(String topic) {
    to WikipediaEnHomePage
    searchInput << topic
    searchSubmit.click()
  }

}
