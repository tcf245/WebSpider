package crawl.spider.process;

import crawl.spider.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BFD_303 on 2017/1/24.
 */
public class ProcessDianpingList implements Processor{
    private static final Log LOG = LogFactory.getLog(ProcessDianpingList.class);

    @Override
    public void process(Page page) {
        String html = page.getHtml();
        Document doc = Jsoup.parse(html);
        List<Map<String,Object>> itemList = new ArrayList();
        Elements elements = doc.select("div#shop-all-list ul li[class]");
        String city = doc.body().select("a.city").text();

        LOG.info("list page parse is start. get items count is : " + elements.size());
        try{
            for (Element e : elements) {
                Map<String,Object> item = new HashMap();
                String href = "http://www.dianping.com" + e.select("div.txt div.tit a").attr("href");
                page.addTargetRequest(new Request(href, Request.Type.INFO,page.getRequest().getSite()));

                Element div = e.select("div.txt").first();
                String name = div.select("div.tit a h4").text();
                String star = div.select("div.comment span.sml-rank-stars").attr("title");
                String reviewCount = div.select("div.comment a.review-num b").text();
                String avgPrice = div.select("div.comment a.mean-price b").text();
                String type = div.select("div.tag-addr a span.tag").text();
                String[] strs = type.split(" ");
                String location = "";
                if (strs.length == 2){
                    type = strs[0];
                    location = strs[1];
                }
                String addr = div.select("div.tag-addr span.addr").text();

                item.put("city",city);
                item.put("name",name);
                item.put("star",star);
                item.put("reviewCount",reviewCount);
                item.put("avgPrice",avgPrice);
                item.put("type",type);
                item.put("location",location);
                item.put("addr",addr);
                item.put("url",href);

                LOG.info("get item data is : " + WorkCache.gson.toJson(item));
                itemList.add(item);
            }
            WorkCache.LIST_RESULT.add(WorkCache.gson.toJson(itemList));
            LOG.info("a task has been parsed ,result has been saved to list,list size is : " + WorkCache.LIST_RESULT.size());
        }catch(Exception e){
            e.printStackTrace();
        }


        String next = doc.select("a.next").attr("href");
        System.out.println("next " + next);
        page.addTargetRequest(new Request(next, Request.Type.LIST,page.getRequest().getSite()));
    }

    @Override
    public Request.Type getType() {
        return Request.Type.LIST;
    }

    public static void main(String[] args) throws IOException {
        Site site = Site.me("Dianping").setDomain("http://www.dianping.com").setIntervals(100);
        Spider spider = new Spider(site,new ProcessDianpingList(),new ProcessDianpingInfo())
                .setDup(true)
                .setThreadNum(10);
        loadStartTask(spider);
        spider.run();

    }

    public static void loadStartTask(Spider spider){
        String url = "http://www.dianping.com/search/keyword/@index@/0_@keyword@";
        String[] keywords = {"必胜客","酒吧","麦当劳","肯德基","咖啡店","星巴克","KTV","酒店","写字楼","商场","居民区","学校"};
        for (int i = 1; i < 100; i++) {
            for (String s : keywords){
                url = url.replace("@index@",i+"").replace("@keyword@", URLEncoder.encode(s));
                spider.addStartRequest(url, Request.Type.LIST);
            }
        }

    }

}
