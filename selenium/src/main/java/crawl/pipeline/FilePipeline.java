package crawl.pipeline;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tcf24 on 2016/11/24.
 */
public class FilePipeline implements Pipeline,Runnable{

    public File target;
    public Collection<?> collection;
    public static final Log LOG = LogFactory.getLog(FilePipeline.class);

    public FilePipeline(Collection<?> collection, String targetPath) {
        this.collection = collection;
        this.target = new File(targetPath);
    }

    @Override
    public void run() {
        save();
    }

    public void save() {
        while (true){
            try {
                if (collection.size() <= 0){
                    LOG.info("result size is 0 wait 60 second ");
                    Thread.sleep(60 * 1000);
                    continue;
                }

                synchronized (collection){
                    if (collection instanceof BlockingQueue){
                        List<String> list = new ArrayList<>();
                        Gson gson = new Gson();
                        collection.forEach(e -> list.add(gson.toJson(e)));
                        FileUtils.writeLines(target,list,false);
                        continue;
                    }
                    FileUtils.writeLines(target,collection,true);
                    LOG.info(collection.size() + " results has save to disk..  waiting 60 second ..");
                    collection.clear();
                }

                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
