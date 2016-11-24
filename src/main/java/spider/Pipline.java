package spider;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by tcf24 on 2016/11/24.
 */
public class Pipline implements Runnable{
    public String name;
    public List<String> list ;

    public Pipline(String name, List<String> list) {
        this.name = name;
        this.list = list;
    }

    @Override
    public void run() {
        while (true){
            try {
                if (list.size() <= 0){
                    Thread.sleep(600 * 000);
                    continue;
                }

                FileUtils.writeLines(new File("target/datasave.txt"),list,true);
                list.clear();


                Thread.sleep(60 * 000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
