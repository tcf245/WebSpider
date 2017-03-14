package crawler.process;

import com.google.gson.Gson;
import crawler.util.HttpClientUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
                String url = queue.take();
                String ak = BaiduMapUtils.getAK();
                if (ak == null){
                    //ak 配额达到上限，线程休眠，等待唤醒
                }

                String content = String.valueOf(HttpClientUtils.httpGet(url,null));
                Map<String,Object> data =  gson.fromJson(content,Map.class);
                //todo tada 数据处理 保存

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
