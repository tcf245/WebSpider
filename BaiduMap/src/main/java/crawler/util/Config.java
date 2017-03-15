package crawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static Map<String, Properties> configs = new HashMap<String, Properties>();
    private static final String defaultFile = "config";

    static {
        try {
            String targetPath =  "conf/";
            System.out.println(targetPath);
            InputStream stream = new FileInputStream(new File(targetPath + "config.properties"));

            Properties p = new Properties();
            p.load(stream);
            String[] resources = p.getProperty("resources").split(";");
            for (String paths : resources) {
                InputStream is = new FileInputStream(targetPath +paths);
                String key = paths.substring(paths.lastIndexOf("/") + 1, paths.lastIndexOf("."));
                Properties pro = new Properties();
                pro.load(is);
                configs.put(key, pro);
            }
            configs.put("config", p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getConfigStr(String key, String defaultStr) {
        Properties p = configs.get(defaultFile);
        if (p.containsKey(key)) {
            return p.getProperty(key);
        }
        return defaultStr;

    }

    public static String getConfigStr(String key) {
        Properties p = configs.get(defaultFile);
        if (p.containsKey(key)) {
            return p.getProperty(key);
        }
        return null;

    }

    public static int getConfigInt(String key, int defaultint) {
        String result = getConfigStr(key, defaultint + "");
        return Integer.valueOf(result);
    }

    public static int getConfigInt(String key) {
        String result = getConfigStr(key);
        if (result == null) {
            return 0;
        }
        return Integer.valueOf(result);
    }

    public static String getConfigStr(String fileName, String key, String defaultStr) {
        if (configs.containsKey(fileName)) {
            Properties p = configs.get(fileName);
            if (p.containsKey(key)) {
                return p.getProperty(key);
            }
        }
        return defaultStr;

    }

    public static Integer getConfigInt(String fileName, String key, String defaultStr) {
        String result = getConfigStr(fileName, key, defaultStr);
        return Integer.valueOf(result);
    }

    public static void main(String[] args) {
        System.out.println(Config.getConfigStr("db", "redisHost", null));
    }
}