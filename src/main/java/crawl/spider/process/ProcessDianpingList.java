package crawl.spider.process;

import crawl.spider.Page;
import crawl.spider.Request;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * Created by BFD_303 on 2017/1/24.
 */
public class ProcessDianpingList implements Processor{
    @Override
    public void process(Page page) {
        String html = page.getHtml();
        Document doc = Jsoup.parse(html);

        Elements elements = doc.select("div#shop-all-list ul li");
        for (Element e : elements) {
            String href = e.select("div.txt div.tit a").attr("href");
            System.out.println("get link " + href);
        }

            String next = doc.select("a.next").attr("href");
            System.out.println("next " + next);
    }

    @Override
    public Request.Type getType() {
        return Request.Type.LIST;
    }

    public static void main(String[] args) throws IOException {
        String html = FileUtils.readFileToString(new File("etc/chanpin.txt"),"utf-8");
        Page page = new Page();
        page.setHtml(html);

        Processor process = new ProcessDianpingList();
        process.process(page);
    }

}
