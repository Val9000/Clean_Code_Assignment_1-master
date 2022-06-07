package entities;

/**
 * Represents a heading on a {Page} object
 */
public class Heading {
    public final String value;
    public final String tagName;

    public Heading(String value, String tagName) {
        this.value = value;
        this.tagName = tagName;
    }
}
