package crawler;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Arxiv_Crawler extends WebCrawler {
	/**
	 * You should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {

		String href = url.getURL().toLowerCase();
		String current_page = referringPage.getWebURL().getURL();
		if (!href.startsWith("https://arxiv.org")) {
			return false;
		}

		if (href.endsWith("/recent") | href.endsWith("/current") | href.endsWith("/new") | href.endsWith("/recent/")
				| href.endsWith("/current/") | href.endsWith("/new/") | href.endsWith("/contact")
				| href.endsWith("/contact/") | href.endsWith("/form/") | href.endsWith("/form")
				| href.endsWith("/robots") | href.endsWith("/robots/") | href.contains("help") | href.contains("/css/")
				| href.contains("/bibex/")) {
			return false;
		}

		if (current_page.equals("https://arxiv.org/")) {
			if (href.startsWith("https://arxiv.org/archive/eess")) {
				return true;
			} else {
				return false;
			}
		}
		if (current_page.startsWith("https://arxiv.org/archive/")) {
			if (href.startsWith("https://arxiv.org/year/")) {
				return true;
			}
		}
		if (current_page.startsWith("https://arxiv.org/year/")) {
			if (href.startsWith("https://arxiv.org/list/")) {
				if (href.contains("?")) {
					return false;
				}
				return true;
			}
		}
		if (current_page.startsWith("https://arxiv.org/list/")) {
			HtmlParseData htmlParseData = (HtmlParseData) referringPage.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			ArrayList links_array = new ArrayList();
			links_array.add(links.toArray());
			if (html.contains(">all</a>")) {
				if (href.contains("skip=")) {
					return false;
				}
				if (href.startsWith("https://arxiv.org/abs/")) {
					return false;
				}
				return true;
			} else {
				if (href.startsWith("https://arxiv.org/abs/")) {
					return true;
				}
				return false;
			}
		}

		return false;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		Map<String, String> metaDataMap = htmlParseData.getMetaTags();
		String pureHTML = htmlParseData.getHtml();
		Document doc = Jsoup.parse(pureHTML);

		if (url.startsWith("https://arxiv.org/abs/")) {
			try {
				String title = doc.select("h1.title").first().text().substring(6);
				Elements authors_list = doc.select("div.authors").select("a");
				Iterator authors_iter = authors_list.iterator();
				String authors = "";
				while (authors_iter.hasNext()) {
					Element current_author = (Element) authors_iter.next();
					String current_author_string = current_author.text();
					authors += current_author_string + ";";
				}
				authors = authors.substring(0, authors.length() - 1);
				String date = metaDataMap.get("citation_date");
				String context = doc.select("td.subjects").first().text();
				String external_reference="";
				String doi = "";
				try {
					Element doi_element = doc.select("td.msc_classes").append("").first();
					doi = doi_element.text();
					external_reference += doi +";";
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Element ext_link_elem = doc.select("td.arxivid").append("").first();
					String ext_link = ext_link_elem.text();
					external_reference += ext_link;
					if (external_reference.equals(";")) {
						external_reference = "";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				String path = "../data/arxiv_output.csv";
				String str = title + "," + authors + "," + date + "," + context + "," + external_reference + "\n";
				BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
				writer.append(str);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
