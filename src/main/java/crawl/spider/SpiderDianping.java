package crawl.spider;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by BFD_303 on 2017/3/1.
 */
public class SpiderDianping {
    private static final Log LOG = LogFactory.getLog(SpiderDianping.class);
    public static Gson gson = new Gson();
    public static final String path = "etc/";

    public static BlockingQueue<String> TASK_QUEUE = new LinkedBlockingDeque();
    public static List<String> INFO_RESULT = new CopyOnWriteArrayList<>();
    public static List<String> LIST_RESULT = new CopyOnWriteArrayList<>();
}
