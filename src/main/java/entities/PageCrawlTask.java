package entities;

import utils.CrawlParameters;

import java.util.concurrent.Callable;

/**
 * A object that implements callable, so when the task finishes,
 * we get the crawl result.
 */
public class PageCrawlTask implements Callable {
    CrawlParameters crawlParameters;

    public PageCrawlTask(CrawlParameters crawlParameters) {
        this.crawlParameters = crawlParameters;
    }

    @Override
    public String call() throws Exception {
        Page rootPage = new Page(crawlParameters.url);
        rootPage.crawl(crawlParameters.depth);

        return rootPage.convertToMarkdown(crawlParameters);
    }
}
