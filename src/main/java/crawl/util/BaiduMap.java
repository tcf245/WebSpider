package crawl.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by BFD_303 on 2017/1/24.
 */
public class BaiduMap {

    public String getLocation(String address,String city){
        StringBuffer uri = new StringBuffer("http://api.map.baidu.com/geocoder/v2/?callback=renderOption&output=json");
        uri.append("&address=" + URLEncoder.encode(address));
        uri.append("&city=" + URLEncoder.encode(city));
        uri.append("&ak=" + getAK());

        return null;
    }

    public String getAK(){

        return null;
    }
}
