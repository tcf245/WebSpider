package crawl.excel;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by BFD_303 on 2016/12/23.
 */
public class Json2Csv {

    private static Gson gson = new Gson();

    public static void json2csv(File jsonFile, File targetFile) {
        Set<String> results = new HashSet<>();
        String line = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.contains("web-test.huawei")) continue;

                    Map<String, Object> data = gson.fromJson(line, Map.class);
                    List<Map<String, Object>> replys = (List<Map<String, Object>>) data.get("replys");

                    String cate = "";
                    try {
                        List<String> cates = (List<String>) data.get("cate");
                        if (cate == null) continue;
                        cate = cates.stream()
                                .map(String::toString).distinct()
                                .collect(Collectors.joining(">"));
                    } catch (ClassCastException e) {
                        cate = (String) data.get("cate");
                        if (cate == null) continue;
                    }

                    List<String> items = new ArrayList<>();

                    if (replys == null) continue;
                    for (Map<String, Object> m : replys) {
                        items.add(convert((String) data.get("url")));
                        items.add(convert(cate));
                        items.add(convert((String) data.get("title")));
                        items.add(convert((String) m.get("replydate")));
                        items.add(convert((String) m.get("replycontent")));
                        items.add(convert((String) m.get("replyusername")));
                        items.add(convert((String) m.get("replyfloor")));

                        String result = items.stream().map(String::toString)
                                .distinct()
                                .collect(Collectors.joining(","));

                        results.add(result);
                        System.out.println("result line is : " + result);
                        items.clear();
                    }
                    System.out.println("result set has lines count is : " + results.size());
                    if (results.size() >= 10000) {
                        FileUtils.writeLines(targetFile, "gbk",results, true);
                        results.clear();
                        System.out.println(results.size() + "  result set has been flush to disk .. now result will be clear .");
                    }

                } catch (NullPointerException e) {
                    System.out.println("exception line is : " + line);
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("exception line is : " + line);
                    e.printStackTrace();
                }
            }

            FileUtils.writeLines(targetFile, "gbk",results, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convert(String data) {
        if (data == null) return "";
        return data.replace(",", "，");
    }

    public static void main(String[] args) {

        File jsonFile = new File("E:\\xinsheng\\xinsheng.txt");
        File targetFile = new File("E:\\xinsheng\\xinsheng.csv");

        json2csv(jsonFile, targetFile);

    }

}
