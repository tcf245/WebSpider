package crawl.spider.process;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by tcf24 on 2016/11/27.
 */
public interface Processor {
    void process(String task) throws IOException,SocketTimeoutException;
}
