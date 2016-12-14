package crawl.spider.process;

import crawl.spider.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by tcf24 on 2016/12/14.
 */
public class ProcessorZhilianList implements Processor {
    private static final Log LOG = LogFactory.getLog(ProcessorZhilianList.class);

    @Override
    public void process(Page page) {
        String html = page.getHtml();
        Document doc = Jsoup.parse(html);


    }
}
