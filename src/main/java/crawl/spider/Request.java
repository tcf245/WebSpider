package crawl.spider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BFD_303 on 2016/12/14.
 */
public class Request {

    private String url;
    private String method;
    private Map<String,String> params = new HashMap<>();
    private Map<String, Object> extras = new HashMap<>();

    //TODO  构造
    private Site site;

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
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

    public Request(String url) {
        this.url = url;
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
}
