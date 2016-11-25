package spider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import java.net.URLEncoder;

import static spider.WorkCache.result;
import static spider.WorkCache.taskQueue;

/**
 * Created by tcf24 on 2016/11/24.
 */
public class SpiderMain {
    private static final Log LOG = LogFactory.getLog(SpiderMain.class);

    public static void main(String[] args) {
        String logPath = SpiderMain.class.getClassLoader().getResource("log4j.properties").getFile();
        PropertyConfigurator.configureAndWatch(logPath);

        try {
            //加载任务
            getTask();

            //启动10 个工作线程
            for (int i = 0; i < 10; i++) {
                Thread t = new Thread(new WorkThread("worker-" + i,taskQueue));
                t.start();
            }

            //启动保存线程
            Thread t = new Thread(new Pipline("pipline"));
            t.start();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void getTask() throws InterruptedException {

        String url = "http://sou.zhaopin.com/jobs/searchresult.ashx?in=121100&jl=@key@&p=@index@";

        url = url.replace("@key@",URLEncoder.encode("广州+深圳+珠海+佛山+东莞+中山+江门+肇庆+惠州+清远+云浮+阳江+河源+汕尾"));

        for (int i = 1;i<= 492 ; i++){
            url = url.replace("@index@",i+"");
            taskQueue.put(url);
        }
        LOG.info("get task number is : " + taskQueue.size());
    }

}
