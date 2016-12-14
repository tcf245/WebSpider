package crawl.spider;

import java.util.Map;
import java.util.Set;

/**
 * Created by tcf24 on 2016/11/27.
 */
public class Site {

    private String name;
    private String domain;
    private int retryTimes;
    private Set<Integer> acceptStatCode;
    private int timeOut;
    private String charset;
    private Map<String,String> headers;
    private int cycleRetryTimes;

    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    public void setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

    public void setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
    }

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
