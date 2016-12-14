package crawl.spider.downloader;

import crawl.spider.Page;
import crawl.spider.Request;

/**
 * Created by BFD_303 on 2016/12/14.
 */
public interface Downloader {
    Page download(Request request);
}
