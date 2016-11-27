package crawl.spider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

import static crawl.spider.WorkCache.result;

/**
 * Created by tcf24 on 2016/11/24.
 */
public class Pipline implements Runnable{
    public String name;
    public static final Log LOG = LogFactory.getLog(Pipline.class);

    public Pipline(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (true){
            try {
                if (result.size() <= 0){
                    LOG.info("result size is 0 wait 60 second ");
                    Thread.sleep(60 * 1000);
                    continue;
                }

                FileUtils.writeLines(new File("target/datasave.txt"),result,true);
                LOG.info(result.size() + " results has save to disk..  waiting 60 second ..");
                result.clear();

                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
