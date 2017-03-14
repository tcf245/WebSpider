package crawler.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by tcf24 on 2017/3/14.
 */
public class CrawlerMain {
    private final static Log LOG = LogFactory.getLog(CrawlerMain.class);

    private BlockingQueue<String> tasks = new LinkedBlockingQueue<String>();
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    private List<String> results = new CopyOnWriteArrayList<String>();

    public BlockingQueue<String> getTasks() {
        return tasks;
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public List<String> getResults() {
        return results;
    }

    public void start(){
        PropertyConfigurator.configureAndWatch(this.getClass().getClassLoader().getResource("log4j.properties").getFile());



    }

}
