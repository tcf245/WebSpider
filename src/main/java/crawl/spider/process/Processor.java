package crawl.spider.process;

import crawl.spider.Page;
import crawl.spider.Request;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by tcf24 on 2016/11/27.
 */
public interface Processor {
    void process(Page Page);
    Request.Type getType();
}
