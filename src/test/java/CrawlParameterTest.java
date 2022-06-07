import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.CrawlParameters;

import java.net.MalformedURLException;

public class CrawlParameterTest {
    @Test
    void testUrlIsNull() {
        Assertions.assertTrue(CrawlParameters.isLinkBroken(null));
    }

    @Test
    void testUrlIsInvalid() {
        Assertions.assertTrue(CrawlParameters.isLinkBroken("invalid"));
    }

    @Test
    void testUrlProtocolNotGiven() {
        // no protocol => link should be broken
        Assertions.assertTrue(CrawlParameters.isLinkBroken("www.google.at"));
    }

    @Test
    void testUrlValid() {
        Assertions.assertFalse(CrawlParameters.isLinkBroken("https://www.google.at"));
    }

    @Test
    void testDepthIsNotNumericString() {
        Assertions.assertFalse(CrawlParameters.isPositiveNumeric(null));
    }

    @Test
    void testDepthIsNonNumericString() {
        Assertions.assertFalse(CrawlParameters.isPositiveNumeric("s"));
    }

    @Test
    void testDepthIsNumericNegative() {
        Assertions.assertFalse(CrawlParameters.isPositiveNumeric("-1"));

    }

    @Test
    void testDepthIsValid() {
        Assertions.assertTrue(CrawlParameters.isPositiveNumeric("4"));

    }

    @Test
    void testLanguageIsNull() {
        Assertions.assertFalse(CrawlParameters.isValidLanguage(null));
    }

    @Test
    void testLanguageIsInvalidString() {
        Assertions.assertFalse(CrawlParameters.isValidLanguage("invalid"));
    }

    @Test
    void testLanguageIsValid() {
        Assertions.assertTrue(CrawlParameters.isValidLanguage("en"));
    }

    @Test
    void testConvertWithValidArguments() {
        Assertions.assertDoesNotThrow(() -> CrawlParameters.convert(new String[]{"https://google.com", "1", "en"}));
    }

    @Test
    void testConvertWithInvalidUrlArgument() {
        Assertions.assertThrows(MalformedURLException.class, () -> CrawlParameters.convert(new String[]{"invalid", "1", "en"}));
    }

    @Test
    void testConvertWithInvalidDepthArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> CrawlParameters.convert(new String[]{"https://google.com", "a", "en"}));
    }

    @Test
    void testConvertWithOneArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> CrawlParameters.convert(new String[]{"https://google.com"}));
    }

    @Test
    void testConvertWithTwoArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> CrawlParameters.convert(new String[]{"https://google.com", "1"}));
    }

}
