package crawl.spider;

import crawl.spider.downloader.HttpClientDownloader;
import crawl.spider.pipeline.FilePipeline;
import crawl.spider.process.ProcessDianpingList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import java.net.URLEncoder;

import static crawl.spider.WorkCache.*;

/**
 * 智联招聘
 * Created by tcf24 on 2016/11/24.
 */
public class SpiderMain {
    private static final Log LOG = LogFactory.getLog(SpiderMain.class);

    private static void swap(int a, int b){
        int temp = a;
        a = b;
        b = temp;
        System.out.printf("a = %d and b = %d",a,b);
    }
    public static void main(String[] args) {
        String logPath = SpiderMain.class.getClassLoader().getResource("log4j.properties").getFile();
        PropertyConfigurator.configureAndWatch(logPath);

        int a = 1;
        int b = 2;
        swap(a,b);
        System.out.printf("a = %d and b = %d",a,b);
        //
//        try {
//            //加载任务
//            getTask();
//
//            //启动10 个工作线程
//            for (int i = 0; i < 10; i++) {
////                Thread t = new Thread(new ProcessDianpingList());
////                t.start();
//            }
//
//            //启动保存线程
//            Thread t = new Thread(new FilePipeline(WorkCache.LIST_RESULT,"etc/dianping-listdata.txt"));
//            t.start();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public static void getTask() throws InterruptedException {
        String url = "http://sou.zhaopin.com/jobs/searchresult.ashx?in=121100&jl=@key@&p=@index@";

        url = url.replace("@key@",URLEncoder.encode("广州+深圳+珠海+佛山+东莞+中山+江门+肇庆+惠州+清远+云浮+阳江+河源+汕尾"));

        for (int i = 1;i<= 492 ; i++){
            url = url.replace("@index@",i+"");
            TASK_QUEUE.put(url);
        }
        LOG.info("get task number is : " + TASK_QUEUE.size());
    }

}
