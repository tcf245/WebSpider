package crawl.excel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static crawl.spider.WorkCache.gson;


/**
 * Created by tcf24 on 2016/11/25.
 */
public class Json2Excel implements Runnable{
    private static final Log LOG = LogFactory.getLog(Json2Excel.class);

    public String name ;
    public File file ;
    private Workbook wb ;
    private Sheet sheet ;

    public Json2Excel(String name, File file) {
        this.name = name;
        this.file = file;
        wb = new SXSSFWorkbook();
        sheet = wb.createSheet();
    }

    public void run() {
        try {
            List<String> lines = FileUtils.readLines(file,"utf-8");
            LOG.info(file.getName() + " get lines number is -> " + lines.size());

            lines.forEach(s -> insertRow(s));
            FileOutputStream fos = new FileOutputStream(file.getParent() + "/" + getFileName(file) + ".xlsx");
            wb.write(fos);
            fos.close();
            LOG.info(getFileName(file) + ".xlsx has flush to disk . ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertRow(String s) {
        Map<String,Object> m = gson.fromJson(s,Map.class);
        Row row ;
        int i = 0;

        //如果是第一行，添加表头
        if (sheet.getLastRowNum() == 0){
            row = sheet.createRow(sheet.getLastRowNum());
            for (String k : m.keySet()) {
                Cell c = row.createCell(i++);
                c.setCellValue(k);
            }
//            m.keySet().forEach(k -> {
//                int i = 0;
//                Cell c = row.createCell(i++);
//                c.setCellValue(k);
//            });
        }
        row = sheet.createRow(sheet.getLastRowNum() + 1);

        i = 0;
        for (String k : m.keySet()) {
            Cell c = row.createCell(i++);
            c.setCellValue(String.valueOf(m.get(k)));
        }
    }

    public String getFileName(File file){
        return file.getName().split("\\.")[0];
    }

}
