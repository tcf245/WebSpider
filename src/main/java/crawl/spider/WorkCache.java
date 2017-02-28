package crawl.spider;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by tcf24 on 2016/11/24.
 */
public class WorkCache {
    public static Gson gson = new Gson();
    public static final String path = "etc/";

    public static BlockingQueue<String> TASK_QUEUE = new LinkedBlockingDeque();
    public static List<String> INFO_RESULT = new CopyOnWriteArrayList<>();
    public static List<String> LIST_RESULT = new CopyOnWriteArrayList<>();

}
