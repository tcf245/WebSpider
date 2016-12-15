package crawl.spider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BFD_303 on 2016/12/14.
 */
public class Request {

    private String url;
    private String method;
    private Type type;
    private Map<String,String> params = new HashMap<>();
    private Map<String, Object> extras = new HashMap<>();
    private int retryTimes = 0;

    //TODO  构造
    private Site site;

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public Type getType() {
        return type;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Request putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<String, Object>();
        }
        extras.put(key, value);
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Request(String url,Type type,Site site) {
        this.url = url;
        this.type = type;
        this.site = site;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Site getSite() {
        return site;
    }

    public enum Type{
        LIST,INFO,COMMENT;
    }
}
