package crawl.spider.scheduler;

import crawl.spider.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by BFD_303 on 2016/12/14.
 */
public class QueueScheduler implements Scheduler{
    private static final Log LOG = LogFactory.getLog(QueueScheduler.class);

    private DuplicateSet dupSet = new DuplicateSet();
    private BlockingDeque<Request> queue = new LinkedBlockingDeque();

    public QueueScheduler() {}

    /**
     * 发任务
     * @param request
     */
    @Override
    public void push(Request request) {
        if (request.getType().equals(Request.Type.LIST)){
            queue.addFirst(request);
        }else{
            queue.add(request);
        }
    }

    /**
     * 取任务
     * @return
     * @throws InterruptedException
     */
    @Override
    public Request poll() throws InterruptedException {
        Request request = queue.take();
        while (dupSet.idDuplied(request.getUrl())){
            LOG.info("request has been duplicate . url is : " + request.getUrl());
            request = queue.take();
        }
        LOG.info("success to send task , url is : " + request.getUrl());
        return request;
    }

    @Override
    public void addDup(Request request){
        dupSet.addToDupSet(request.getUrl());
    }

    public int size(){
        return queue.size();
    }

    public BlockingQueue<Request> getQueue() {
        return queue;
    }

}
