package crawl.util;

import crawl.spider.WorkCache;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by BFD_303 on 2017/1/24.
 */
public class BaiduMap {
    public static Set<String> aks = new HashSet<String>();

    public static String getLocation(String address,String city){
        String ak = getAK();
        Map<String,Object> data = new HashMap<String,Object>();
        String json = "";

        StringBuffer uri = new StringBuffer("http://api.map.baidu.com/geocoder/v2/?callback=renderOption&output=json");
        uri.append("&address=" + URLEncoder.encode(address));
        uri.append("&city=" + URLEncoder.encode(city));
        uri.append("&ak=" + ak);

        for (;;){
            try {
                json = HttpClientUtils.getHtml(uri.toString(),null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (json.endsWith(")")){
                json = json.substring(json.indexOf("("),json.indexOf(")"));
            }

            data = WorkCache.gson.fromJson(json,Map.class);
            int status = (int) data.get("status");
            //ak次数超过当天请求限制
            if (status == 302){
                aks.add(ak);
                ak = getAK();
            }else if (status == 0){
                Map<String,Object> result = (Map<String, Object>) data.get("result");
                Map<String,Object> location = (Map<String, Object>) result.get("location");
                double lng = (double) location.get("lng");
                double lat = (double) location.get("lat");
                return lng + "," + lat;
            }
            return null;
        }
    }

    /**
     * 获取AK，消重
     * @return
     */
    public static String getAK(){
        List<String> aks = new ArrayList<>();
        for (String s : aks){
            if (!aks.contains(s))
                return s;
        }
        return "g41LfmKNt41p7a0WvDoYEW5awKXlxGI0";
    }

    public static void main(String[] args) {
        for (int i = 0; i< 100; i++){
            String json = getLocation("潘家园武圣路武圣东里54号楼","北京");
            System.out.println(i + "   " + json);
        }
    }
}
