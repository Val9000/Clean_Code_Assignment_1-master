package entities.Coupling;

import entities.Heading;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JSoupWrapper implements Loader {
    Document document;

    @Override
    public DocumentWrapper loadDocumentWrapper(String url) throws Exception {
        loadDocument(url);

        if(document == null){
            throw new Exception("not a valid url from loader " + url);
        }
        return new DocumentWrapper(getHeadings(), getLinks());
    }

    private boolean loadDocument(String url) {
        try {
            document = Jsoup.connect(url).get();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Heading[] getHeadings() {
        Elements allHeadingTags = document.select("h1, h2, h3, h4, h5, h6");

        Heading[] headings = new Heading[allHeadingTags.size()];
        int index = 0;
        for (Element headingTag : allHeadingTags)
            headings[index++] = new Heading(headingTag.html(), headingTag.tagName());

        return headings;
    }

    private String[] getLinks() {
        Elements allLinkTags = document.select("a");
        String[] links = new String[allLinkTags.size()];

        int index = 0;
        for (Element linkTag : allLinkTags)
            links[index++] = linkTag.attr("href");

        return links;
    }
}
