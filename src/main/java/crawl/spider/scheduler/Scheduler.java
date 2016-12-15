package crawl.spider.scheduler;

import crawl.spider.Request;

/**
 * Created by tcf24 on 2016/11/27.
 */
public interface Scheduler {
    int size();
    void addDup(Request request);
    void push(Request request);
    Request poll() throws InterruptedException;
}
