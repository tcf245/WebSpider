package crawl.selenium;

import com.google.gson.Gson;
import crawl.util.HttpClientUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
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
        String[] urls = {"http://hotels.ctrip.com/hotel/534568.html#ctm_ref=hod_hp_hot_dl_n_1_3",
                        "http://hotels.ctrip.com/hotel/457112.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_22",
                        "http://hotels.ctrip.com/hotel/2570579.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_23",
                        "http://hotels.ctrip.com/hotel/5293044.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_24",
                        "http://hotels.ctrip.com/hotel/691682.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_7",
                        "http://hotels.ctrip.com/hotel/431639.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_4"};

//        instance.getCtripDiscountInfo("http://hotels.ctrip.com/hotel/431639.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_4");

        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        instance.getCtripDiscountInfo(url);
                        System.out.println("count is ----> " + count.incrementAndGet());
                    }
                }
            }).start();
        }

    }

    public void getCtripDiscountInfo(String url){
        // 创建了一个 Chrome driver 的实例
        // 注意，其余的代码依赖于接口而非实例
        WebDriver driver = new ChromeDriver();

        driver.get(url);

        //获取页面源码,提取参数信息
        String html = driver.getPageSource();
        Map<String,String> params = getDiscountParams(html);

        Set<Cookie> cookies = driver.manage().getCookies();
        Map<String,String> headers = new HashMap<>();

        StringBuffer cookie = new StringBuffer();
        cookies.forEach(c -> {
            cookie.append(c.getName() + "=" + c.getValue());
            cookie.append(";");
        });
        cookie.deleteCharAt(cookie.length()-1);
        headers.put("Cookie",cookie.toString());
        driver.quit();

        try {
            String content = "";
            getHeaders(new File("etc/ctrip/request.txt"),headers);
            //优惠信息
            url = "http://taocan.ctrip.com/SH/Ajax/DPSearch/AjaxDPGetRoomAndScenicSpot.aspx";
            content = HttpClientUtils.httpPost(url,"utf-8",headers,params,null);
            System.out.println(Thread.currentThread().getName() + " DiscountInformation----> " +  content);

            //更多景点
            params.put("PageSize",15+"");
            params.put("PageIndex",1+"");
            params.put("sorttype","S");
            params.put("sortdirection","D");
            url = "http://taocan.ctrip.com/SH/Ajax/DPSearch/AjaxMoreScenicSpot.aspx";
            content = HttpClientUtils.httpPost(url,"utf-8",headers,params,null);
            System.out.println(Thread.currentThread().getName() + " ScenicSpotList----> " +  content);


            Map<String,Object> scenicData = gson.fromJson(content,Map.class);
            if (!(boolean)scenicData.get("success")){
                System.out.println("get data failed... -> " + content);
                return;
            }

            List<Map<String,Object>> scenicList = (List<Map<String, Object>>) scenicData.get("data");
            System.out.println("scenicList size is : " + scenicList.size());
            for (Map<String, Object> m : scenicList) {
                int id = 0;
                try{
                    id = (int) m.get("ScenicSpotId");
                }catch(ClassCastException e){
                    id = (int) ((double)m.get("ScenicSpotId"));
                    System.out.println("get id : " + id);
                }

                //单个景点详情
                params.remove("cityId");
                params.remove("HotelId");
                params.remove("Latitude");
                params.remove("Longitude");
                params.remove("PageSize");
                params.remove("PageIndex");
                params.remove("sorttype");
                params.remove("sortdirection");

                params.put("ScenicSpotId",id+"");
                url = "http://taocan.ctrip.com/SH/Ajax/DPSearch/AjaxMoreTicket.aspx";
                content = HttpClientUtils.httpPost(url,"utf-8",headers,params,null);
                System.out.println(Thread.currentThread().getName() + " ScenicSpotId----> " +  content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        System.out.println(new Gson().toJson(params));
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
                System.out.println(l);
                e.printStackTrace();
            }
        });
        System.out.println(new Gson().toJson(map));
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
