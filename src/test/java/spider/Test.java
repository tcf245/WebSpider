package spider;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by BFD_303 on 2016/12/13.
 */
public class Test {

    public static void main(String[] args) {
        Gson gson = new Gson();

        try {
            String json = FileUtils.readFileToString(new File("etc/json.json"),"utf-8");
            Map<String,Object> map = gson.fromJson(json,Map.class);

            List<String> keys = (List<String>) map.get("keys");
            List<Map<String,Object>> showdata = (List<Map<String, Object>>) map.get("showdata");

            showdata.forEach( m -> {
                keys.forEach( k -> {
                    try{
                        m.get(k).toString();
                    }catch(NullPointerException e){
                        System.out.println(k);
                        System.out.println(gson.toJson(m));
                    }
                });
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}