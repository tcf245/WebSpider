package crawl.spider;

import com.google.gson.Gson;
import crawl.spider.downloader.Downloader;
import crawl.spider.downloader.HttpClientDownloader;
import crawl.spider.pipeline.FilePipeline;
import crawl.spider.pipeline.Pipeline;
import crawl.spider.process.Processor;
import crawl.spider.scheduler.QueueScheduler;
import crawl.spider.scheduler.Scheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tcf24 on 2016/12/14.
 */
public class Spider {
    private static final Log LOG = LogFactory.getLog(Spider.class);

    private Site site;
    private Gson gson = new Gson();
    private Scheduler scheduler;
    private Downloader downloader;

    private List<Pipeline> pipelines = new ArrayList<>();
    private List<Processor> processors = new ArrayList<>();
    private List<Request> startRequests = new ArrayList<>();
    private List<String> result = new ArrayList<>();

    private boolean isDup = true;
    private int threadNum = 5;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public Spider(Site site,Processor... processors) {
        create(processors);
        this.site = site;
        this.startRequests = site.getStartRequests();
    }

    public void run(){
        init();
        LOG.info("Spider " + site.getName() + " started ..");

        pipelines.forEach(p -> executorService.submit((Runnable) p));


        if (threadNum <= 0){
            LOG.error("thread num should be more than 0 , it will be define as 1");
            threadNum = 1;
        }

        for (int i = 0;i < threadNum;i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    LOG.info(Thread.currentThread().getName() + "  started.. ");
                    while (true){
                        try {
                            Request req = scheduler.poll();
                            if (req == null) continue;
                            LOG.info("get request url is : " + req.getUrl() + " and task queue size is : " + scheduler.size());
                            processRequest(req);
                            if (isDup) scheduler.addDup(req);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (site.getIntervals() != 0){
                            try {
                                Thread.currentThread().sleep(site.getIntervals());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
        executorService.shutdown();
    }

    public void init(){
        if (downloader == null) {
            this.downloader = new HttpClientDownloader();
        }
        if (scheduler == null){
            this.scheduler = new QueueScheduler();
        }
        if (pipelines.size() == 0) {
            pipelines.add(new FilePipeline(result,WorkCache.path + site.getName() + "/datasave.txt"));
            if (scheduler instanceof QueueScheduler){
                BlockingQueue queue = ((QueueScheduler) scheduler).getQueue();
                pipelines.add(new FilePipeline(((QueueScheduler) scheduler).getQueue(),WorkCache.path + site.getName() + "/tasksave.txt"));
            }
        }
        if (startRequests != null) {
            for (Request request : startRequests) {
                scheduler.push(request);
            }
            startRequests.clear();
        }
    }

    public Spider addStartRequest(String url,Request.Type type){
        startRequests.add(new Request(url,type,site));
        return this;
    }

    protected void processRequest(Request request) {
        Page page = downloader.download(request);
        if (page == null) {
            throw new RuntimeException("unaccpetable response status");
        }
        if (!site.getAcceptStatCode().contains(request.getExtra("statusCode"))) {
            scheduler.push(request);
            request.setRetryTimes(request.getRetryTimes() + 1);
            return;
        }

        getProcessor(request).process(page);

        if (page.getFields() != null)
            result.add(gson.toJson(page.getFields()));
        if (page.getTargetRequests() != null)
            page.getTargetRequests().forEach(r -> scheduler.push(r));

    }

    public Processor getProcessor(Request request){
        //TODO choose processor by pagetype
        for (Processor p : processors){
            if(p.getType() == request.getType()) return p;
        }
        return null;
    }

    public List<Request> getStartRequests() {
        return startRequests;
    }

    public void setStartRequests(List<Request> startRequests) {
        this.startRequests = startRequests;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public Spider setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public boolean isDup() {
        return isDup;
    }

    public Spider setDup(boolean dup) {
        isDup = dup;
        return this;
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
