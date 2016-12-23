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

        try {
            List<String> lines = FileUtils.readLines(new File("etc/test.txt"),"utf-8");
            lines.forEach(s -> {
                s = s.replace("    ","\t").replace("   ","\t").replace("  ","\t").replace(" ","\t");
               String[] strs = s.trim().split("\t");
                String sql = "insert into bbslist_000 (cid,tname,keyword,pagetype,url,status,createtime) values(\"souhu_qiche\",\""+strs[0]+"\",\""+strs[1]+"\",\"bbspostlist\",\""+strs[2]+"\",1,now());";
                System.out.println(sql);

            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}