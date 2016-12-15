package crawl.spider;

import java.util.*;

/**
 * Created by tcf24 on 2016/11/27.
 */
public class Site {

    private String name = "";
    private String domain;
    private int retryTimes = 3;
    private int timeOut = 20000;
    private int Intervals = 0;

    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;
    private String charset;
    private Map<String,String> headers;
    private List<Request> startRequests = new ArrayList<>();
    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

    public static Site me(String name){
        return new Site(name);
    }

    public Site(String name){
        this.name = name;
    }

    public int getIntervals() {
        return Intervals;
    }

    public Site setIntervals(int intervals) {
        Intervals = intervals;
        return this;
    }

    public List<Request> getStartRequests() {
        return startRequests;
    }

    public void setStartRequests(List<Request> startRequests) {
        this.startRequests = startRequests;
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

    static {
        DEFAULT_STATUS_CODE_SET.add(200);
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

    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
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
