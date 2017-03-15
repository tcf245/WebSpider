package crawler.process;

import com.google.gson.Gson;
import crawler.util.HttpClientUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tcf24 on 2017/3/14.
 */
public class Worker extends Thread{
    private final static Log LOG = LogFactory.getLog(Worker.class);
    private Gson gson = new Gson();
    private BlockingQueue<String> queue;


    public Worker(String name,BlockingQueue<String> queue){
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()){
            try {
                LOG.info("tasks size is : " + queue.size());
                if (queue.size() <= 0){
                    this.interrupt();
                }

                String url = queue.take();
                String ak = this.getName();
                url = url.replace("#ak#",ak);
//                for (;;){
//                    ak = BaiduMapUtils.getAK();
//                    if (ak == null){
//                        //ak 配额达到上限，线程休眠
//                        Thread.sleep(60 * 60 * 1000);
//                    }else{
//                        url = url.replace("#ak#",ak);
//                        break;
//                    }
//                }

                String content = new String(HttpClientUtils.httpGet(url,null));
                Map<String,Object> data =  gson.fromJson(content,Map.class);
                double status = (double) data.get("status");
                //错误处理
                if (status != 0){
//                    BaiduMapUtils.abandonAK(ak);
                    Thread .sleep(60 * 1000);
                    queue.put(url);
                    continue;
                }

                // 第一页的解析进行任务的扩散
                if (url.contains("page_num=0&")){
                    double total = (double) data.get("total");
                    double pages = Math.floor(total / 20);
                    if (pages > 0){
                        for (int i = 1; i <= pages; i++){
                            String u = url;
                            u = url.replace("page_num=0&","page_num="+ i +"&");
                            LOG.info("get more task url is : " + u);
                            queue.put(u);
                        }
                    }
                }

                List<Map<String,Object>> results = (List<Map<String, Object>>) data.get("results");
                for (Map<String,Object> map : results) {
                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put("name",map.get("name"));
                    m.put("location",map.get("location"));
                    m.put("address",map.get("address"));

                    double detail = (double) map.get("detail");
                    //包含详细信息
                    if (detail == 1){
                        Map<String,Object> details = (Map<String, Object>) map.get("detail_info");
                        m.put("tag",details.get("tag"));
                        m.put("type",details.get("type"));
                        m.put("price",details.get("price"));
                        m.put("shop_hours",details.get("shop_hours"));
                        m.put("overall_rating",details.get("overall_rating"));
                        m.put("facility_rating",details.get("facility_rating"));
                        m.put("comment_num",details.get("comment_num"));
                        m.put("favorite_num",details.get("favorite_num"));
                        m.put("checkin_num",details.get("checkin_num"));
                    }
                    LOG.info("get result data is : " + gson.toJson(m));
                    CrawlerMain.result.add(gson.toJson(m));
                }

            } catch(NullPointerException e){
                continue;
            }catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
