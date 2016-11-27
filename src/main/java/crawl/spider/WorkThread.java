package crawl.spider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static crawl.spider.WorkCache.gson;
import static crawl.spider.WorkCache.result;

/**
 * Created by tcf24 on 2016/11/24.
 */
public class WorkThread implements Runnable {
    public String name;
    public BlockingQueue<String> taskQueue = null;
    public static final Log LOG = LogFactory.getLog(WorkThread.class);

    public WorkThread(String name, BlockingQueue taskQueue) {
        this.name = name;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        String task = "";

        try {
            while (taskQueue.size() > 0) {
                LOG.info("task queue size is  ----> " + taskQueue.size());
                task = taskQueue.take();
                Document doc = Jsoup.connect(task).get();
                Elements tables = doc.select("table.newlist");

                //去掉第一行
                tables.remove(0);
                for (Element e : tables) {
                    String position = e.select("td.zwmc").text();
                    String feedback = e.select("td.fk_lv").text();
                    String company = e.select("td.gsmc").text();
                    String salary = e.select("td.zwyx").text();
                    String area = e.select("td.gzdd").text();
                    String post_time = e.select("td.gxsj").text();

                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put("position",position);
                    m.put("feedback",feedback);
                    m.put("company",company);
                    m.put("salary",salary);
                    m.put("area",area);
                    m.put("post_time",post_time);

                    LOG.info("get json result ---->" + gson.toJson(m));

                    result.add(gson.toJson(m));
                }

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

}
