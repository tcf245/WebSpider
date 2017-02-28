package crawl.util;

import crawl.spider.WorkCache;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by BFD_303 on 2017/1/24.
 */
public class BaiduMap {
    public static Set<String> aks = new HashSet<String>();

    public static String getLocation(String address,String city){
        Map<String,Object> data = new HashMap<String,Object>();
        String json = "";
        String ak = "";

        while (true){
            try {
                ak = getAK();
                StringBuffer uri = new StringBuffer("http://api.map.baidu.com/geocoder/v2/?callback=renderOption&output=json");
                uri.append("&address=" + URLEncoder.encode(address));
                uri.append("&city=" + URLEncoder.encode(city));
                uri.append("&ak=" + ak);
                json = HttpClientUtils.getHtml(uri.toString(),null);

            if (json.endsWith(")")){
                json = json.substring(json.indexOf("(")+1,json.indexOf(")"));
            }
            System.out.println(json);
            data = WorkCache.gson.fromJson(json,Map.class);
            double status = (double) data.get("status");
            //ak次数超过当天请求限制
            if (status == 302){
                aks.add(ak);
                ak = getAK();
                continue;
            }else if (status == 0){
                Map<String,Object> result = (Map<String, Object>) data.get("result");
                Map<String,Object> location = (Map<String, Object>) result.get("location");
                double lng = (double) location.get("lng");
                double lat = (double) location.get("lat");
                return lng + "," + lat;
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
            return 0 + "," + 0;
        }
    }

    /**
     * 获取AK，消重
     * @return
     */
    private static String getAK() throws IOException {
//        List<String> aks = FileUtils.readLines(new File("../etc/baidu-map.properties"),"utf-8");
        for (String s : aks){
            if (!aks.contains(s))
                return s;
        }
        return "g41LfmKNt41p7a0WvDoYEW5awKXlxGI0";
    }
}
