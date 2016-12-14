package crawl.spider;

import com.google.gson.Gson;
import crawl.spider.process.Processor;
import crawl.spider.scheduler.QueueScheduler;
import crawl.spider.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tcf24 on 2016/12/14.
 */
public class Spider {

    private Gson gson = new Gson();
    private Site site;
    private List<String> result = new ArrayList<>();
    private Scheduler scheduler = new QueueScheduler();
    private List<Processor> processors;
    private int threadNum;

    private ExecutorService executorService = Executors.newCachedThreadPool();


    public Spider(Processor processors) {
//        this.Processor = pageProcessor;
//        this.site = pageProcessor.getSite();
//        this.startRequests = pageProcessor.getSite().getStartRequests();
    }

    public void thread(int num){
        this.threadNum = num;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public void create(Processor... processor){
        Arrays.stream(processor).forEach(p -> processors.add(p));
    }

}
