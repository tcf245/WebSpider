package crawl.spider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BFD_303 on 2016/12/14.
 */
public class Page {

    private String url;
    private Request request;
    private String Html;
    private Map<String,Object> fields = new HashMap<String,Object>();
    private List<Request> targetRequests;
    private int statusCode;

    public Page() {
    }

    public Object getField(String key){
        if (fields.isEmpty()) return null;
        return fields.get(key);
    }

    public void putField(String key,Object object){
        fields.put(key,object);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
        putField("request",request);
    }

    public String getHtml() {
        return Html;
    }

    public void setHtml(String html) {
        Html = html;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public List<Request> getTargetRequests() {
        return targetRequests;
    }

    public void setTargetRequests(List<Request> targetRequests) {
        this.targetRequests = targetRequests;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode(){return statusCode;}
}
