class WikipediaEnHomePageSpec extends geb.spock.GebSpec {

  Class page = WikipediaEnHomePage

  def "can navigate there"() {
    when:
    to page

    then:
    at page
  }

  def "can get top banner text"() {
    when:
    to page
    System.out.println(topBanner.text())

    then:
    topBanner
    topBanner.text()
  }

  def "can find the search form"() {
    when:
    to page

    then:
    searchForm
  }

  def "can put something into the search input"() {
    when:
    to page

    then:
    searchInput
    searchInput() << "Foo"
  }

  def "can click the search button"() {
    when:
    to page

    then:
    searchSubmit
    searchSubmit.click()
  }

}
