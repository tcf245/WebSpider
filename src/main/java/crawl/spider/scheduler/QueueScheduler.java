package crawl.spider.scheduler;

import crawl.spider.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by BFD_303 on 2016/12/14.
 */
public class QueueScheduler implements Scheduler{
    private static final Log LOG = LogFactory.getLog(QueueScheduler.class);

    private DuplicateSet dupSet = new DuplicateSet();
    private BlockingQueue<Request> queue = new LinkedBlockingDeque();

    public QueueScheduler() {}

    @Override
    public void push(Request request) {
        queue.add(request);
    }

    @Override
    public Request poll() throws InterruptedException {
        Request request = queue.take();
        if (!dupSet.idDuplied(request.getUrl())){
            LOG.info("success to send task , url is : " + request.getUrl());
            return request;
        }
        LOG.info("request has been duplicate . url is : " + request.getUrl());
        return poll();
    }

    public int size(){
        return queue.size();
    }
}
