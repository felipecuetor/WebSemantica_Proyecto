package stage1.crawler.ceur;

import java.io.BufferedWriter;
import java.io.FileWriter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Ceur_Crawler_Controller {
	public static void main(String[] args) {
		try {
			String columns = "title,authors,date,context,external_reference,url";
			String path = "../data/ceur_output.csv";
			String str = columns + "\n";
			BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
			writer.append(str);
			writer.close();

			/*
			 * crawlStorageFolder is a folder where intermediate crawl data is stored.
			 */
			String crawlStorageFolder = "../data/Ceur_Crawler_Storage";

			/*
			 * numberOfCrawlers shows the number of concurrent threads that should be
			 * initiated for crawling.
			 */
			int numberOfCrawlers = 1;

			CrawlConfig config = new CrawlConfig();

			config.setCrawlStorageFolder(crawlStorageFolder);

			/*
			 * Be polite: Make sure that we don't send more than 1 request per second (1000
			 * milliseconds between requests).
			 */
			config.setPolitenessDelay(2000);
			config.setMaxDownloadSize(1000000000);

			/*
			 * You can set the maximum crawl depth here. The default value is -1 for
			 * unlimited depth
			 */
			config.setMaxDepthOfCrawling(5);

			/*
			 * You can set the maximum number of pages to crawl. The default value is -1 for
			 * unlimited number of pages
			 */
			config.setMaxPagesToFetch(-1);

			/**
			 * Do you want crawler4j to crawl also binary data ? example: the contents of
			 * pdf, or the metadata of images etc
			 */
			config.setIncludeBinaryContentInCrawling(false);

			/*
			 * Do you need to set a proxy? If so, you can use:
			 * config.setProxyHost("proxyserver.example.com"); config.setProxyPort(8080);
			 *
			 * If your proxy also needs authentication: config.setProxyUsername(username);
			 * config.getProxyPassword(password);
			 */

			/*
			 * This config parameter can be used to set your crawl to be resumable (meaning
			 * that you can resume the crawl from a previously interrupted/crashed crawl).
			 * Note: if you enable resuming feature and want to start a fresh crawl, you
			 * need to delete the contents of rootFolder manually.
			 */
			config.setResumableCrawling(false);
			

			/*
			 * Set this to true if you want crawling to stop whenever an unexpected error
			 * occurs. You'll probably want this set to true when you first start testing
			 * your crawler, and then set to false once you're ready to let the crawler run
			 * for a long time.
			 */
			//config.setHaltOnError(true);

			/*
			 * Instantiate the controller for this crawl.
			 */
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
			CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

			/*
			 * For each crawl, you need to add some seed urls. These are the first URLs that
			 * are fetched and then the crawler starts following links which are found in
			 * these pages
			 */
			controller.addSeed("http://ceur-ws.org/");
//			controller.addSeed("https://www.ics.uci.edu/~lopes/");
//			controller.addSeed("https://www.ics.uci.edu/~welling/");

			/*
			 * Start the crawl. This is a blocking operation, meaning that your code will
			 * reach the line after this only when crawling is finished.
			 */
			controller.start(Ceur_Crawler.class, numberOfCrawlers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
