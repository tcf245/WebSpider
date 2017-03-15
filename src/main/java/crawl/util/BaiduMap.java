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

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 7000; i++) {
            String url = "http://api.map.baidu.com/geocoder/v2/?callback=renderOption&output=json&address=%E6%B3%B0%E5%B7%9E%E9%87%91%E5%BA%A7&city=%E6%B3%B0%E5%B7%9E%E5%B8%82&ak=UBwgKsGBRGOouiMumvwRD6aMrCnfojVF";
            String content = HttpClientUtils.getHtml(url,null);
            System.out.println(i + " -> " + content);
        }
    }
}
