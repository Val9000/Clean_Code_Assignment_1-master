package entities;

import entities.Coupling.DocumentWrapper;
import entities.Coupling.JSoupWrapper;
import entities.Coupling.Loader;
import org.jsoup.nodes.Document;
import utils.CrawlParameters;

import java.io.IOException;
import java.util.List;


public class Page {
    public final String url;
    private Heading[] headings;
    private Page[] subpages;
    DocumentWrapper documentWrapper;
    private Boolean isUrlBroken;

    Loader jSoupWrapper = new JSoupWrapper();

    public Page(String url) {
        this.url = url;
        this.isUrlBroken = false;
    }

    public static String getCrawlResultsMerged(List<String> crawlResults) {
        StringBuilder sb = new StringBuilder();
        for (String crawlResult : crawlResults) {
            sb.append("######## NEW CRAWL RESULT ########\n");
            sb.append(crawlResult);
            sb.append("######## END CRAWL RESULT ########\n\n\n");
        }
        return sb.toString();
    }


    /**
     * gets all headings for this page, as well as for further subpages
     *
     * @param level to which we want to crawl for further subpages
     * @throws IOException
     */
    public void crawl(int level) throws IOException {
        // crawl for this page

        try {
            documentWrapper = jSoupWrapper.loadDocumentWrapper(url);
        } catch (Exception e) {
            // we know that the loader, cant connect and load the url => therefore a broken link
            isUrlBroken = true;
            //e.printStackTrace();
        }

        if (!isUrlBroken) {
            headings = documentWrapper.getHeadings();
            subpages = getSubpages(documentWrapper.getLinks());

            // if we have reached the last level of recursion -> stop recursion
            if (level <= 0) return;

            // crawl subpages
            for (Page subpage : subpages)
                if (!subpage.isUrlBroken) subpage.crawl(level - 1);
        }
    }


    private Page[] getSubpages(String[] links) {
        Page[] subpages = new Page[links.length];

        for (int i = 0; i < links.length; i++)
            subpages[i] = new Page(links[i]);

        return subpages;
    }


    /**
     * checks if link of this page is broken
     *
     * @return {boolean}
     */
    public boolean isLinkBroken() {
        return CrawlParameters.isLinkBroken(url);
    }

    public boolean isLinkBroken(String link) {
        return CrawlParameters.isLinkBroken(link);
    }


    private String convertToMarkdown(int level) {
        StringBuilder stringBuilder = new StringBuilder();

        // print headings
        for (Heading heading : headings) stringBuilder.append(convertToMarkdownHeading(heading) + "\n");

        // print subpages
        for (Page subpage : subpages) {
            // print subpage heading
            if (subpage.isUrlBroken)
                stringBuilder.append(prependBrTag("broken link" + wrapInATag(subpage.url) + "\n"));
            else stringBuilder.append(prependBrTag("--> link to:" + wrapInATag(subpage.url) + "\n"));

            // as long as we are not at the end of recursion, print subpages too
            if (level > 0 && !(subpage.isUrlBroken)) stringBuilder.append(subpage.convertToMarkdown(level - 1));
        }
        return stringBuilder.toString();

    }

    public String convertToMarkdown(CrawlParameters args) {
        StringBuilder stringBuilder = new StringBuilder();
        // prepend metadata
        stringBuilder.append("input: " + wrapInATag(args.url) + "\n");
        stringBuilder.append(prependBrTag("depth: ") + args.depth + "\n");
        stringBuilder.append(prependBrTag("source language: ") + args.language + "\n");
        stringBuilder.append(prependBrTag("target language: ") + args.language + "\n");
        stringBuilder.append(prependBrTag("summary:" + "\n"));

        // add all contents of this and further subpages
        stringBuilder.append(convertToMarkdown(args.depth));
        return stringBuilder.toString();
    }

    public static String wrapInATag(String link) {
        return "<a>" + link + "</a>";
    }

    public static String prependBrTag(String text) {
        return "<br>" + text;
    }

    public static String convertToMarkdownHeading(Heading heading) {
        String[] markDownHeadingLevels = new String[]{"#", "##", "###", "####", "#####", "######"};
        int headingLevel = Character.getNumericValue(heading.tagName.charAt(1));
        return markDownHeadingLevels[headingLevel - 1] + " " + heading.value;
    }
}


