package crawl.excel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Arrays;

/**
 * Created by tcf24 on 2016/11/25.
 */
public class ExcelMain {
    private static final Log LOG = LogFactory.getLog(ExcelMain.class);

    public static void main(String[] args) {
        listFile(new File("target"));
    }

    private static void listFile(File file) {
        if (file.isDirectory()){
            LOG.info("  Directory  ----> " + file.getName());
            File[] files = file.listFiles();
            Arrays.stream(files).forEach(f -> listFile(f));
        }else{
            LOG.info("  File ----> " + file.getName());
            Thread t = new Thread(new Json2Excel(file.getName(),file));
            t.start();
        }
    }

}
