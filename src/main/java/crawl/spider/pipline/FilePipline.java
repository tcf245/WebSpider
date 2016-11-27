package crawl.spider.pipline;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

import static crawl.spider.WorkCache.result;

/**
 * Created by tcf24 on 2016/11/24.
 */
public class FilePipline implements Pipline,Runnable{
    public String name;
    public File target;
    public static final Log LOG = LogFactory.getLog(FilePipline.class);

    public FilePipline(String name,String target) {
        this.name = name;
        this.target = new File(target);
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

                FileUtils.writeLines(target,result,true);
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
