package crawl.spider.process;

import crawl.spider.Page;
import crawl.spider.Request;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

/**
 * Created by BFD_303 on 2017/2/8.
 */
public class ProcessDianpingInfo implements Processor{
    @Override
    public void process(Page page) {
        String html = page.getHtml();
        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        String city = body.select("a.city").text();
        String name = body.select("h1.shop-name").text();
        String address = body.select("div.expand-info span.item").text();
        String avgPriceTitle = body.select("span#avgPriceTitle").text();
        String reviewCount = body.select("span#reviewCount").text();
        String star = body.select("span#power-star").attr("title");
        String time = body.select("p.info span.item").text();

        System.out.println("city " + city);
        System.out.println("name " + name);
        System.out.println("address " + address);
        System.out.println("avgPriceTitle " + avgPriceTitle);
        System.out.println("reviewCount " + reviewCount);
        System.out.println("star " + star);
        System.out.println("time " + time);

    }

    @Override
    public Request.Type getType() {
        return Request.Type.INFO;
    }

    public static void main(String[] args) throws IOException {
        ProcessDianpingInfo process = new ProcessDianpingInfo();
        String html = FileUtils.readFileToString(new File("etc/chanpin.txt"),"utf-8");
        Page page = new Page();
        page.setHtml(html);

        process.process(page);
    }
}
