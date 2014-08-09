class WikipediaEnHomePage extends geb.Page {

  static url = "http://en.wikipedia.org/wiki/Main_Page"
  static at = {
    title == "Wikipedia, the free encyclopedia"
  }
  static content = {
    topBanner { $("#mp-topbanner") }
    searchForm { $("form#searchform") }
    searchInput { searchForm.find("input", name:"search") }
    searchSubmit { searchForm.find("input", name:"go") }
  }

}
