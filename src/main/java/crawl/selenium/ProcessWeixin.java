package crawl.selenium;

import crawl.spider.process.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static crawl.spider.WorkCache.gson;
import static crawl.spider.WorkCache.result;

/**
 * Created by BFD_303 on 2016/11/30.
 */
public class ProcessWeixin implements Processor,Runnable {

    public String name;
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
        } catch(SocketTimeoutException e){
            try {
                LOG.info("task connect fail. url is : " + task);
                taskQueue.put(task);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(String task) throws IOException, SocketTimeoutException {
        // 加载功能直接调用 Jsoup.connect 接口
        Document doc = Jsoup.connect(task).get();
        Elements page = doc.select("div#page-content");
        Map<String,Object> m = new HashMap<String,Object>();

        page.forEach(p -> {
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


            m.clear();
            m.put("title",title);
            m.put("content",content);
            m.put("author",author);
            m.put("posttime",posttime);
            m.put("viewcnt",viewcnt);
            m.put("upcnt",upcnt);

            LOG.info("get json result ---->" + gson.toJson(m));
            result.add(gson.toJson(m));
        });

        }

    public static void main(String[] args) {
        String url = "http://mp.weixin.qq.com/s?src=3&timestamp=1480494103&ver=1&signature=2x8j32zrMQpScC20E*fEgQ-Lhu9fn684BZ*wlqPCxT5TIMl6vzcJwRWQZIUNTblZNAaCn1djb9nEvFjHRHreC73ZjFY7EoEE934FLdqJka5AV4YEKM-jSuZQ29qDvTr5bqlbYx7WOVwmGtdTjeVn2U3BrwgAIA0ZEfiqJ0gnoXg=";
        ProcessWeixin weixin = new ProcessWeixin("weixin",new LinkedBlockingDeque<>());

        try {
            weixin.process(url);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
