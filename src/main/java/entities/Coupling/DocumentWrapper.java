package entities.Coupling;

import entities.Heading;

public class DocumentWrapper {
    Heading[] headings;
    String[] links;

    public DocumentWrapper(Heading[] headings, String[] links) {
        this.headings = headings;
        this.links = links;
    }

    public Heading[] getHeadings() {
        return headings;
    }

    public String[] getLinks() {
        return links;
    }
}