// Simple Geb biolerplate script.  Run this with the `groovy` command.
// (The first time you run it, it will appear to hang while Grapes downloads dependencies.)

@Grapes([
    @Grab("org.gebish:geb-core:0.9.3"),
    @Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.41.0"),
    @Grab("org.seleniumhq.selenium:selenium-support:2.41.0")
])
import geb.Browser

Browser.drive {
    System.out.println("Going to Google");
    go "http://google.com/ncr"
 
    // make sure we actually got to the page
    assert title == "Google"
 
    // enter wikipedia into the search field
    $("input", name: "q").value("wikipedia")
 
    // wait for the change to results page to happen
    // (google updates the page dynamically without a new request)
    System.out.println("Waiting for search");
    waitFor { title.endsWith("Google Search") }
 
    // is the first link to wikipedia?
    def firstLink = $("li.g", 0).find("a.l")
    assert firstLink.text() == "Wikipedia"
 
    // click the link 
    firstLink.click()
 
    // wait for Google's javascript to redirect to Wikipedia
    System.out.println("Waiting for Wikipedia");
    waitFor { title == "Wikipedia" }
    System.out.println("At Wikipedia");
}
