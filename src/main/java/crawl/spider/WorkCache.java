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
    public static BlockingQueue<String> taskQueue = new LinkedBlockingDeque();

    public static List<String> result = new CopyOnWriteArrayList<>();

}
