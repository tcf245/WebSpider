package crawl.selenium;

import com.google.gson.Gson;
import crawl.util.HttpClientUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BFD_303 on 2016/12/30.
 */
public class CtripSpiderTest {
        public static Gson gson = new Gson();
        public static AtomicInteger count = new AtomicInteger(0);

    static{
        //加载驱动
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");
    }

    public static void main(String[] args){
        CtripSpiderTest instance =  new CtripSpiderTest();
        String[] urls = {"http://hotels.ctrip.com/hotel/479549.html#ctm_ref=ctr_hp_sb_lst"
        ,"http://hotels.ctrip.com/hotel/469193.html#ctm_ref=ctr_hp_sb_lst"
        ,"http://hotels.ctrip.com/hotel/433881.html#ctm_ref=ctr_hp_sb_lst"
        ,"http://hotels.ctrip.com/hotel/667517.html#ctm_ref=ctr_hp_sb_lst"
        ,"http://hotels.ctrip.com/hotel/777007.html#ctm_ref=ctr_hp_sb_lst"};

        instance.getCtripDiscountInfo(urls[4]);


//        for (int i = 0; i < urls.length; i++) {
//            String url = urls[i];
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                        instance.getCtripDiscountInfo(url);
//
//                }
//            }).start();
//        }

    }

    public void getCtripDiscountInfo(String url){
        Map<String,Object> data = new HashMap<>();

        // 创建了一个 Chrome driver 的实例
        // 注意，其余的代码依赖于接口而非实例
        WebDriver driver = new ChromeDriver();

        driver.get(url);

        data.put("url",url);
        WebElement name = driver.findElement(By.cssSelector("h2.cn_n"));
        if (name != null){
            data.put("name",name.getText());
        }

//        String[] dateD = {"2017-02-17","2017-02-18","2017-02-19","2017-02-20","2017-02-21"};
        String[] dateD = {"2017-02-21"};
        for (String time:dateD){
            data.put("date",time);
            if (!time.equals("2017-02-17")){
                WebElement date = driver.findElement(By.id("cc_txtCheckIn"));
                if (date != null ){
//                    date.click();
                    date.clear();
                    date.sendKeys(time);
                    driver.findElement(By.id("changeBtn")).click();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
                getPrice(data, driver,time);
        }




//        //获取页面源码,提取参数信息
//        String html = driver.getPageSource();
//        Map<String,String> params = getDiscountParams(html);
//
//        Set<Cookie> cookies = driver.manage().getCookies();
//        Map<String,String> headers = new HashMap<>();
//
//        StringBuffer cookie = new StringBuffer();
//        cookies.forEach(c -> {
//            cookie.append(c.getName() + "=" + c.getValue());
//            cookie.append(";");
//        });
//        cookie.deleteCharAt(cookie.length()-1);
//        headers.put("Cookie",cookie.toString());
//        driver.quit();
//
//        try {
//            String content = "";
//            getHeaders(new File("etc/ctrip/request.txt"),headers);
//            //优惠信息
//            url = "http://taocan.ctrip.com/SH/Ajax/DPSearch/AjaxDPGetRoomAndScenicSpot.aspx";
//            content = HttpClientUtils.httpPost(url,"utf-8",headers,params,null);
////            System.out.println(Thread.currentThread().getName() + " DiscountInformation----> " +  content);
//
//            //更多景点
//            params.put("PageSize",15+"");
//            params.put("PageIndex",1+"");
//            params.put("sorttype","S");
//            params.put("sortdirection","D");
//            url = "http://taocan.ctrip.com/SH/Ajax/DPSearch/AjaxMoreScenicSpot.aspx";
//            content = HttpClientUtils.httpPost(url,"utf-8",headers,params,null);
////            System.out.println(Thread.currentThread().getName() + " ScenicSpotList----> " +  content);
//
//
//            Map<String,Object> scenicData = gson.fromJson(content,Map.class);
//            if (!(boolean)scenicData.get("success")){
////                System.out.println("get data failed... -> " + content);
//                return;
//            }
//
//            List<Map<String,Object>> scenicList = (List<Map<String, Object>>) scenicData.get("data");
//            System.out.println("scenicList size is : " + scenicList.size());
//            for (Map<String, Object> m : scenicList) {
//                int id = 0;
//                try{
//                    id = (int) m.get("ScenicSpotId");
//                }catch(ClassCastException e){
//                    id = (int) ((double)m.get("ScenicSpotId"));
////                    System.out.println("get id : " + id);
//                }
//
//                //单个景点详情
//                params.remove("cityId");
//                params.remove("HotelId");
//                params.remove("Latitude");
//                params.remove("Longitude");
//                params.remove("PageSize");
//                params.remove("PageIndex");
//                params.remove("sorttype");
//                params.remove("sortdirection");
//
//                params.put("ScenicSpotId",id+"");
//                url = "http://taocan.ctrip.com/SH/Ajax/DPSearch/AjaxMoreTicket.aspx";
//                content = HttpClientUtils.httpPost(url,"utf-8",headers,params,null);
////                System.out.println(Thread.currentThread().getName() + " ScenicSpotId----> " +  content);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void getPrice(Map<String, Object> data, WebDriver driver,String time) {
        data.put("date",time);

        //获取房型价格等信息
        List<WebElement> roomList = driver.findElements(By.cssSelector("tr[brid] td.room_type"));
        List<WebElement> priceList = driver.findElements(By.cssSelector("tr[brid]"));

        List<Map<String,Object>> items = new ArrayList<>();
        for (WebElement e:roomList){
            String id = e.getAttribute("id").toString();
            for (WebElement web:priceList){
//                System.out.printf("web.attr is %s and id is %s",web.getAttribute("brid"),id);
                try{
                if(web.getAttribute("brid").equals(id)){
                    Map<String,Object> m = new HashMap<>();

                        m.put("roomType", e.findElement(By.cssSelector("a.room_unfold")).getText());
                        m.put("roomPrice", web.findElement(By.cssSelector("td span.base_price")).getText());
                        m.put("ratepolicy", web.getText());


                    items.add(m);
                }
                }catch (Exception e1){
                continue;
            }
            }
        }

        data.put("items",items);
        System.out.println(gson.toJson(data));
    }

    public Map<String,String> getDiscountParams(String html){
        Map<String,String> params = new HashMap<String,String>();
        params.put("CheckInDate","checkIn:'([\\d-]+)'");
        params.put("CheckOutDate","checkOut:'([\\d-]+)'");
        params.put("cityId","cityId:'([\\d]+)'");
        params.put("HotelId","hotelId : ([\\d]+)");
        params.put("Latitude","hotellat: '([\\d\\.]+)'");
        params.put("Longitude","hotellon: '([\\d\\.]+)'");

        for (String key : params.keySet()){
            String reg = params.get(key);
            Matcher m = Pattern.compile(reg).matcher(html);
            if (m.find()){
                params.put(key,m.group(1));
            }
        }
//        System.out.println(new Gson().toJson(params));
        return params;
    }

    public static void getHeaders(File file, Map<String,String> map) throws IOException {
        List<String> lines = FileUtils.readLines(file,"utf-8");
        lines.forEach(l -> {
            try{
                String[] strs = l.split(":");
                String content = "";
                for (int i=1;i<strs.length;i++){
                    content = content + strs[i];
                    if (i < strs.length - 1){
                        content = content + ":";
                    }
                }
                map.put(strs[0].trim(),content.trim());
            }catch (IndexOutOfBoundsException e){
//                System.out.println(l);
                e.printStackTrace();
            }
        });
//        System.out.println(new Gson().toJson(map));
    }

    public void tsbPageHandler(WebDriver driver){
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
        driver.close();
    }

    public void closeTab(WebDriver driver,String handles){
        if (handles == null) return;
        driver.switchTo().window(handles).close();
    }
}
