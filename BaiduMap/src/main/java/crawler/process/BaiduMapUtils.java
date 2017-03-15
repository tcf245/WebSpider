package crawler.process;


import crawler.util.EmptyUtil;
import crawler.util.HttpClientUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by BFD_303 on 2017/1/24.
 */
public class BaiduMapUtils {
    private final static Log LOG = LogFactory.getLog(BaiduMapUtils.class);

    private static Set<String> akSet = new HashSet<String>();

    /**
     *  ak 放入冷却集合
     * @param ak
     */
    public static void abandonAK(String ak){
        if (EmptyUtil.isEmpty(ak))
            return;
        akSet.add(ak);
    }

    /**
     * 每天凌晨清除 ak 冷却库
     */
    public static void clear(){
        akSet.clear();
    }

    /**
     * 获取AK，消重
     * @return
     */
    public static String getAK() throws IOException {
        List<String> aks = new ArrayList<>();
        aks.add("hsLA6AgbQdaaMVuGodrhlgdQGYnfQNWN");
        aks.add("33GXju2K3GwGI47eqWrYf1kTTR0qdGiw");

        for (String s : aks){
            if (!akSet.contains(s))
                return s;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String content = new String(HttpClientUtils.httpGet("http://api.map.baidu.com/place/v2/search?query=%E9%85%92%E5%BA%97&page_size=20&page_num=0&scope=2&region=%E5%8C%97%E4%BA%AC&output=json&ak=hsLA6AgbQdaaMVuGodrhlgdQGYnfQNWN",null));
        System.out.println(content);
    }

}
