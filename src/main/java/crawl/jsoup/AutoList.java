package crawl.jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * Created by BFD_303 on 2017/2/13.
 */
public class AutoList {

    public void parse(String html){
        Element body = Jsoup.parse(html,"", Parser.htmlParser()).body();

        Elements links = body.select("a[href]");
        System.out.println(links.size());
        links.forEach(l -> {
            String link = l.attr("href").trim();
            System.out.println(link);
        });
    }

    public static void main(String[] args) throws IOException {
        String html = FileUtils.readFileToString(new File("etc/list.html"),"utf-8");
        AutoList autoList = new AutoList();

        autoList.parse(html);
    }
}
