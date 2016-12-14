package crawl;

import java.util.List;

/**
 * Created by tcf24 on 2016/11/27.
 */
public class Site {

    private String name;
    private String domain;
    private int retryTimes;
    private int timeOut;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getTimeOut() {
        return timeOut;
    }
}
