package crawl.spider.process;

import crawl.spider.Page;
import crawl.spider.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BFD_303 on 2016/12/19.
 */
public class ProcessorHuaweiList implements Processor{
private static final Log LOG = LogFactory.getLog(ProcessorHuaweiList.class);

    @Override
    public void process(Page page) {
        String html = page.getHtml();
        Document doc = Jsoup.parse(html);

        String next = doc.select("a.next-page").attr("href");
        if (next != null){
            page.addTargetRequest(new Request(next, Request.Type.LIST,page.getRequest().getSite()));
        }

        Elements tables = doc.select("table.newlist");
        Map<String, Object> m = new HashMap<String, Object>();
        List<Map<String,Object>> list = new ArrayList<>();

        //去掉第一行
        tables.remove(0);
        for (Element e : tables) {
            String position = e.select("td.zwmc").text();
            String feedback = e.select("td.fk_lv").text();
            String company = e.select("td.gsmc").text();
            String salary = e.select("td.zwyx").text();
            String area = e.select("td.gzdd").text();
            String post_time = e.select("td.gxsj").text();

            LOG.info("position  ->  " + position);
            LOG.info("feedback  ->  " + feedback);
            LOG.info("company   ->  " + company);
            LOG.info("salary    ->  " + salary);
            LOG.info("area  ->  " + area);
            LOG.info("post_time ->  " + post_time);

            m.put("position", position);
            m.put("feedback", feedback);
            m.put("company", company);
            m.put("salary", salary);
            m.put("area", area);
            m.put("post_time", post_time);
            list.add(m);
        }

        page.putField("data",list);
    }

    @Override
    public Request.Type getType() {
        return Request.Type.LIST;
    }
}
