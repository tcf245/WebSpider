package crawl.spider.pipeline;

import crawl.util.KfkProducerUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Created by BFD_303 on 2017/2/6.
 */
public class KfkPipeline implements Pipeline,Runnable {

    public String topic;
    public List<String> result;
    public KfkProducerUtil producer = KfkProducerUtil.getInstance();
    public static final Log LOG = LogFactory.getLog(KfkPipeline.class);

    public KfkPipeline(List<String> result, String topic) {
        this.result = result;
        this.topic = topic;
    }

    @Override
    public void run() {
        save();
    }

    public void save() {
        while (true){
            try {
                if (result.size() <= 0){
                    LOG.info("result size is 0 wait 60 second ");
                    Thread.sleep(60 * 1000);
                    continue;
                }
                synchronized (result){
                    producer.send(topic,result);
                    LOG.info(result.size() + " result has send to kfk..  waiting 60 second ..");
                    result.clear();
                }

                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
