package crawler.process;


import crawler.util.EmptyUtil;
import crawler.util.Namespace;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by BFD_303 on 2017/1/24.
 */
public class BaiduMapUtils {
    private final static Log LOG = LogFactory.getLog(BaiduMapUtils.class);

    private static Set<String> akSet = new HashSet<String>();
    private static List<String> aks;
    static{
        try {
            aks = FileUtils.readLines(new File(Namespace.CONFIG_PATH),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        for (String s : aks){
            if (!aks.contains(s))
                return s;
        }
        return "33GXju2K3GwGI47eqWrYf1kTTR0qdGiw";
    }
}
