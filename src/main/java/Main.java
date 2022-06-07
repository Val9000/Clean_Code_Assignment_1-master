import entities.PageCrawlExecutorService;
import utils.CrawlParameters;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            int numOfThreads = 4;
            List<CrawlParameters> params = CrawlParameters.convert(args);

            try (PageCrawlExecutorService service = new PageCrawlExecutorService(numOfThreads)) {
                String result = service.crawl(params);

                String path = "Berisa_Valon_Assginment_2_report.md";
                Path filePath = Path.of(path);
                try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
                    fileWriter.write(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



/*

 */