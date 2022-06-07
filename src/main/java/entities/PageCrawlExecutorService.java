package entities;

import utils.CrawlParameters;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PageCrawlExecutorService implements Closeable {
    private ExecutorService taskExecutor;
    private List<PageCrawlTask> pageCrawlTasks;
    private List<Future<String>> taskFutures;
    private List<CrawlParameters> crawlParameters;

    public PageCrawlExecutorService(int numOfThreads) {
        taskExecutor = Executors.newFixedThreadPool(numOfThreads);
    }

    public String crawl(List<CrawlParameters> crawlParameters) {
        this.crawlParameters = crawlParameters;

        pageCrawlTasks = createPageCrawlTasks();

        taskFutures = executePageCrawlTasks();

        return getCrawlResultsMerged();
    }

    private String getCrawlResultsMerged() {
        List<String> crawlResults = getCrawlResults();
        return Page.getCrawlResultsMerged(crawlResults);
    }

    private List<PageCrawlTask> createPageCrawlTasks() {
        List<PageCrawlTask> tasks = new ArrayList<>();
        for (CrawlParameters params : crawlParameters)
            tasks.add(new PageCrawlTask(params));
        return tasks;
    }


    /**
     * Creates a list of future objects. Each future contains a crawl-result string.
     * These future object allows us, to asynchronously wait till a PageCrawlTask finishes,
     * then we can call future.get() to get the crawl-result.
     *
     * When submitting a task, it is getting added to the ExecutorService internal queue.
     * The internal Thread Pool handles, which Thread gets which task.
     */
    private List<Future<String>> executePageCrawlTasks() {
        List<Future<String>> taskFutures = new ArrayList<>();
        for (PageCrawlTask task : pageCrawlTasks) {
            taskFutures.add(taskExecutor.submit(task));
        }
        return taskFutures;
    }

    /**
     * Through future.get(i) we are asynchronously waiting for the PageCrawlTask crawl-result.
     * In case of an InterruptedException or ExecutionException during multithreaded execution :
     * added an appropriate message so we know at which task we got the exception, to not stop the whole collection
     * of crawl results.
     */
    private List<String> getCrawlResults() {
        List<String> crawlResults = new ArrayList<>();

        /*
         since taskFutures.size == crawlParameters.size are equal,
         we then can find out at which index the async Task didn't run properly.
         */
        for (int i = 0; i < taskFutures.size(); i++) {
            try {
                String result = taskFutures.get(i).get();
                crawlResults.add(result);
            } catch (InterruptedException | ExecutionException e) {
                crawlResults.add("Exception happened during multithreaded execution for Crawl Page : " + crawlParameters.get(i).url);
                continue;
            }
        }

        return crawlResults;
    }

    public void shutDown() {
        // other things could come here...that's why a separate method
        taskExecutor.shutdown();
    }

    @Override
    public void close() throws IOException {
        shutDown();
    }
}
