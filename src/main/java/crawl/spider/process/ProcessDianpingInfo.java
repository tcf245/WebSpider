package crawl.spider.process;

import crawl.spider.Page;
import crawl.spider.Request;
import crawl.spider.WorkCache;
import crawl.util.BaiduMap;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BFD_303 on 2017/2/8.
 */
public class ProcessDianpingInfo implements Processor{
    @Override
    public void process(Page page) {
        String html = page.getHtml();
        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        String city = body.select("a.city").text();
        String name = "";
        String address = "";
        String avgPrice = "";
        String reviewCount = "";
        String star = "";
        String time = "";

        Elements bread;
        List<String> cate = new ArrayList<>();

        try{
            if (!"".equals(address = body.select("div.expand-info span.item").text().trim()) && !"".equals(name = body.select("h1.shop-name").text().replace("添加分店",""))){
                avgPrice = body.select("span#avgPriceTitle").text().replace("人均：","");
                reviewCount = body.select("span#reviewCount").text().replace("条评论","");
                star = body.select("span.mid-rank-stars").attr("title");
                time = body.select("p.info span.item").text();
                bread = body.select("div.breadcrumb a[href]");
                for (Element e : bread){
                    cate.add(e.text());
                }
            }
            else if (!"".equals(name = body.select("h1.shop-name").text())
                    && !"".equals(address = body.select("div#base-info p.shop-address").text())){
                reviewCount = getField("reviewCount",html);
                bread = body.select("div.crumb span");
                for (Element e : bread){
                    cate.add(e.text());
                }
            }else if(!"".equals(name = body.select("div.hotel-title h1").text())
                    && !"".equals(address = body.select("div.hotel-address-box span.hotel-address").text().replace("地址：",""))){
                reviewCount = getField("reviewCount",html);
                bread = body.select("div.crumb span a");
                for (Element e : bread){
                    cate.add(e.text());
                }
            }else{
                name = getField("fullName",html);
                name = getField("fullName",html);
                name = getField("fullName",html);
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

//        System.out.println("city:" + city);
//        System.out.println("name:" + name);
//        System.out.println("address:" + address);
//        System.out.println("avgPrice:" + avgPrice);
//        System.out.println("reviewCount:" + reviewCount);
//        System.out.println("star:" + star);
//        System.out.println("time:" + time);

        String lat = getField("lat",html);
        String lng = getField("lng",html);

        page.putField("city",city);
        page.putField("name",name);
        page.putField("address",address);
        page.putField("avgPrice",avgPrice);
        page.putField("reviewCount",reviewCount);
        page.putField("star",star);
        page.putField("time",time);
        page.putField("cate",cate);

        System.out.println(WorkCache.gson.toJson(page.getFields()));
    }

    public String getField(String field,String html){
        String reg = field + "[\" ]?:([^,})]+)";
        System.out.println(reg);
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()){
            String str = matcher.group(1);
            if (str != null)
                return str.replace("\"","");
        }
        return "";
    }

    @Override
    public Request.Type getType() {
        return Request.Type.INFO;
    }

    public static void main(String[] args) throws IOException {
        ProcessDianpingInfo process = new ProcessDianpingInfo();
        String html = FileUtils.readFileToString(new File("etc/chanpin.txt"),"utf-8");
        Page page = new Page();
        page.setHtml(html);

        process.process(page);
        process.getField("lng","");
    }
}
