import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {
    public static void main(String[] args) throws IOException {
        final String url = "https://www.bbc.com/";
        NewsData globalData = scrapeGlobalData(url);

        System.out.println("Scraped Data:");
        System.out.println(globalData);

        Document document = Jsoup.connect(url).get();
        System.out.println("Title from full document: " + document.title());
    }

    private static NewsData scrapeGlobalData(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        NewsData data = new NewsData();
        data.setTitle(doc.title());

        // Collect all headings
        List<String> headings = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Elements hs = doc.select("h" + i);
            for (Element h : hs) {
                headings.add("H" + i + ": " + h.text());
            }
        }
        data.setHeadings(headings);

        // Collect all links
        List<String> links = new ArrayList<>();
        Elements anchorTags = doc.select("a[href]");
        for (Element link : anchorTags) {
            links.add(link.attr("abs:href")); // absolute URL
        }
        data.setLinks(links);

        return data;
    }

    static class NewsData {
        private String title;
        private List<String> headings;
        private List<String> links;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getHeadings() {
            return headings;
        }

        public void setHeadings(List<String> headings) {
            this.headings = headings;
        }

        public List<String> getLinks() {
            return links;
        }

        public void setLinks(List<String> links) {
            this.links = links;
        }

        @Override
        public String toString() {
            return "Title: " + title + "\n\nHeadings:\n" + String.join("\n", headings)
                    + "\n\nLinks:\n" + String.join("\n", links);
        }
    }
}
