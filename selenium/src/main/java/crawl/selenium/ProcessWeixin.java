package crawl.selenium;

import crawl.Processor.Processor;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ProcessWeixin implements Runnable,Processor {

    public String name;
    public Gson gson = new Gson();
    public BlockingQueue<String> taskQueue = null;

    public static final Log LOG = LogFactory.getLog(ProcessWeixin.class);

    public ProcessWeixin(String name, BlockingQueue<String> taskQueue) {
        this.name = name;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        String task = "";
        try {
            while (true){
                while (taskQueue.size() > 0) {
                    LOG.info("task queue size is  ----> " + taskQueue.size());
                    task = taskQueue.take();
                    process(task);
                }
                LOG.debug(name + "  task queue size <= 0 , sleep 10 sec..");
                Thread.sleep(10 * 1000);
            }
        } catch(Exception e){
            try {
                LOG.info("task connect fail. url is : " + task);
                taskQueue.put(task);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void process(String url){
        // 加载功能直接调用 Jsoup.connect 接口
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("exception...");
            e.printStackTrace();
        }
        Elements items = doc.select("div#page-content");

        items.forEach(p -> {
            Map<String,Object> m = new HashMap<String,Object>();

            String title = p.select("h2#activity-name").text();
            String content = p.select("div#js_content").text();
            String author = p.select("span.rich_media_meta").text();
            String posttime = p.select("em#post-date").text();
            String viewcnt = p.select("span#sg_readNum3").text();
            String upcnt = p.select("span#sg_likeNum3").text();

            LOG.info("title is ----> " + title);
            LOG.info("content is ----> " + content);
            LOG.info("author is ----> " + author);
            LOG.info("posttime is ----> " + posttime);
            LOG.info("viewcnt is ----> " + viewcnt);
            LOG.info("upcnt is ----> " + upcnt);

            m.put("title",title);
            m.put("content",content);
            m.put("author",author);
            m.put("posttime",posttime);
            m.put("viewcnt",viewcnt);
            m.put("upcnt",upcnt);

            LOG.info("get json result ---->" + gson.toJson(m));
            WorkCache.result.add(gson.toJson(m));
        });
    }

    public static void main(String[] args) throws IOException {
        String url = "http://mp.weixin.qq.com/s?src=3&timestamp=1481793231&ver=1&signature=2x8j32zrMQpScC20E*fEgQ-Lhu9fn684BZ*wlqPCxT5TIMl6vzcJwRWQZIUNTblZNAaCn1djb9nEvFjHRHreC73ZjFY7EoEE934FLdqJka5AV4YEKM-jSuZQ29qDvTr5znGtXTMX6bJZIINRBmC5w7nhIr8DAr5jiI0u1iQiC8g=";
        ProcessWeixin weixin = new ProcessWeixin("weixin",new LinkedBlockingDeque<>());

        weixin.process(url);

//        Document doc = Jsoup.connect(url).get();
//        if (doc == null) System.out.println("null");
//        System.out.println(doc.toString());

    }

}
