import entities.Coupling.DocumentWrapper;
import entities.Coupling.JSoupWrapper;
import entities.Coupling.Loader;
import entities.Heading;
import entities.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.CrawlParameters;

public class PageTest {

    static Heading[] headings;
    static String[] links;
    static String testUrl = "https://javascript.info/hello-world";

    @BeforeAll
    public static void initJSoupDocumentWrapperArrays() {
        // do it once, so I don't have to write this for every headings or links test !

        Loader jSoupWrapper = new JSoupWrapper();
        DocumentWrapper documentWrapper = null;
        try {
            documentWrapper = jSoupWrapper.loadDocumentWrapper(testUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        headings = documentWrapper.getHeadings();
        links = documentWrapper.getLinks();
    }

    @Test
    void testMarkdownWrappers() {
        String value = "value";
        // test link-wrapper
        Assertions.assertEquals("<a></a>", Page.wrapInATag(""));
        Assertions.assertEquals("<a>" + value + "</a>", Page.wrapInATag(value));

        // test br - append
        Assertions.assertEquals("<br>", Page.prependBrTag(""));
        Assertions.assertEquals("<br>" + value, Page.prependBrTag(value));
    }

    @Test
    void testMarkdownHeadingConversion() {
        String headingValue = "heading";
        // test valid conversion
        for (int i = 1; i <= 6; i++) {
            Heading heading = new Heading(headingValue, "h" + i);
            Assertions.assertEquals("#".repeat(i) + " " + headingValue, Page.convertToMarkdownHeading(heading));
        }
    }


    @Test
    void testGetHeadingsSizeFromJSoupWrapper() {
        Assertions.assertEquals(11, headings.length);
    }
    @Test
    void testGetHeadingsFirstHeadingValueFromJsoupWrapper(){
        Assertions.assertEquals("Hello, world!", headings[0].value);
    }
    @Test
    void testGetHeadingsFirstHeadingTagFromJsoupWrapper(){
        Assertions.assertEquals("h1", headings[0].tagName);
    }


    @Test
    void testGetLinksSizeFromJSoupWrapper() {
        Assertions.assertEquals(62, links.length);
    }

    @Test
    void testGetLinksLinkBrokenFromJSoupWrapper() {
        Assertions.assertTrue(CrawlParameters.isLinkBroken(links[14]));
    }

    @Test
    void testGetLinksFirstLinkFromJSoupWrapper() {
        Assertions.assertEquals("https://ar.javascript.info/hello-world", links[0]);
    }
}
