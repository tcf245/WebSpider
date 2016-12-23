package crawl.excel;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by BFD_303 on 2016/12/23.
 */
public class Json2Csv {

    private static Gson gson = new Gson();

    public static void json2csv(File jsonFile,File targetFile){
        List<String> results = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            String line = "";
            while ((line = reader.readLine()) != null){
                Map<String,Object> data = gson.fromJson(line,Map.class);
                List<Map<String,Object>> replys = (List<Map<String, Object>>) data.get("replys");
                List<String> cates = (List<String>) data.get("cate");
                String cate = cates.stream()
                        .map(String::toString).distinct()
                        .collect(Collectors.joining(">"));

                String result = convert((String) data.get("url")) + "," + convert(cate) + convert((String) data.get("title")) + ",";
                replys.forEach( m -> {
                    m.get("authorname");
                });



            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convert(String data){
        return data.replace(",","，");
    }

    public static void main(String[] args) {

    }

}
