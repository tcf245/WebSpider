package site.dianping;

import com.bfd.crawler.kafka7.KfkProducer;
import com.google.gson.Gson;
import crawl.spider.SpiderMain;
import crawl.spider.WorkCache;
import crawl.util.HttpClientUtils;
import crawl.util.SeleniumUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对列表页保存的数据，请求详情页数据，获得经纬度和营业时间等
 * 得到的数据与列表页数据结合
 *
 * Created by BFD_303 on 2017/3/1.
 */
public class DianpingMain {
    private static final Log LOG = LogFactory.getLog(DianpingMain.class);
    private static BlockingQueue<String> tasks = new LinkedBlockingQueue<>();
    private static List<String> result = new CopyOnWriteArrayList<>();
    private static Gson gson = new Gson();
    private static Map<String,String> headers = new HashMap();

    static{
        headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Encoding","gzip, deflate, sdch");
        headers.put("Accept-Language","en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4");
        headers.put("Cache-Control","max-age=0");
        headers.put("Connection","keep-alive");
        headers.put("Host","www.dianping.com");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
    }

//    private static List<String> result = new ArrayList<String>();

    public void loadTask() throws IOException {
        List<String> lines = FileUtils.readLines(new File("etc/Dianping/listdata.txt"),"utf-8");
//        List<String> lines = FileUtils.readLines(new File("etc/Dianping/listdata.txt"),"utf-8");

        try{
            for (String l : lines){
                List<Map<String,Object>> items = gson.fromJson(l,List.class);
                for (Map<String,Object> item : items){
                    tasks.put(gson.toJson(item));
                }
            }
            System.out.println("tasks size is : " + tasks.size());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从详情页中解析字段，作为补充
     * @param html
     * @param item
     */
    public void parse(String html,Map<String,Object> item){
        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        String city = body.select("a.city").text();
        String time = "";
        String lat = "";
        String lng = "";

        Elements bread;
        List<String> cate = new ArrayList<>();

        try{
            time = body.select("p.info span.item").text();
            bread = body.select("div.breadcrumb a[href]");
            for (Element e : bread){
                cate.add(e.text());
            }
            if (cate.size() == 0){
                bread = body.select("div.crumb span");
                for (Element e : bread){
                    cate.add(e.text());
                }
            }
            if (cate.size() == 0){
                bread = body.select("div.crumb span a");
                for (Element e : bread){
                    cate.add(e.text());
                }
            }

        }catch(NullPointerException e){
            e.printStackTrace();
        }

        lat = getField("lat",html);
        lng = getField("lng",html);

        item.put("lat",lat);
        item.put("lng",lng);
        item.put("city",city);
        item.put("time",time);
        item.put("cate",cate);

        System.out.println("parse data : " + gson.toJson(item));
    }

    /**
     * 正则匹配
     * @param field
     * @param html
     * @return
     */
    public String getField(String field,String html){
        String reg = field + "[\" ]?:([^,})]+)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()){
            String str = matcher.group(1);
            if (str != null)
                return str.replace("\"","");
        }
        return "";
    }

    /**
     * 保存线程
     */
    public void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        KfkProducer.getInstance().send("dianping",result);
                        FileUtils.writeLines(new File("E:\\Dianping\\result.txt"),result,true);
                        System.out.println("result has flush to disk , count is : " + result.size());
                        result.clear();
                        Thread.sleep(120 * 1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 解析线程启动
     */
    public void parse(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            String json = tasks.take();
                            System.out.println("tasks size is : " + tasks.size());
                            Map<String,Object> item = gson.fromJson(json,Map.class);
                            String url = (String) item.get("url");
                            String html = HttpClientUtils.getHtml(url,headers);
                            parse(html,item);
                            result.add(gson.toJson(item));

                            Thread.sleep(100);
                        } catch (RuntimeException e) {
                            synchronized (DianpingMain.this){
                                String cookie = SeleniumUtils.getCookie("http://www.dianping.com");
                                System.out.println("update cookie is : " + cookie);
                                headers.put("cookie",cookie);
                            }
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }
    
    public static void main(String[] args) {
        String logPath = DianpingMain.class.getClassLoader().getResource("log4j.properties").getFile();
        PropertyConfigurator.configureAndWatch(logPath);
        DianpingMain dianping = new DianpingMain();

        try {
            dianping.loadTask();
            dianping.parse();
            dianping.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
