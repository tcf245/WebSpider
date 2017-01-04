package crawl.selenium;

import com.google.gson.Gson;
import crawl.util.HttpClientUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BFD_303 on 2016/12/30.
 */
public class CtripSpiderTest {

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
                        "http://hotels.ctrip.com/hotel/691682.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_7"};
//        instance.getCtripDiscountInfo(urls[0]);

        for (int i = 0; i < 5; i++) {
            String url = urls[i];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        instance.getCtripDiscountInfo(url);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }

    public void getCtripDiscountInfo(String url){
        // 创建了一个 Chrome driver 的实例
        // 注意，其余的代码依赖于接口而非实例
        WebDriver driver = new PhantomJSDriver();

        driver.get(url);

        //获取页面源码,提取参数信息
        String html = driver.getPageSource();
        Map<String,String> params = getDiscountParams(html);

//        try{
//            WebElement element = driver.findElement(By.cssSelector("ul#hotel_tabs_DP_TAB li.fn-shx-dp a"));
//            if (element != null){
//                Actions action = new Actions(driver);
//                action.moveToElement(element).click().perform();
//            }
//        }catch(NoSuchElementException e){
//            System.out.println("no discount information ..");
//            driver.quit();
//            return;
//        }

        Set<Cookie> cookies = driver.manage().getCookies();
        Map<String,String> headers = new HashMap<>();

        StringBuffer cookie = new StringBuffer();
        cookies.forEach(c -> {
            cookie.append(c.getName() + "=" + c.getValue());
            cookie.append(";");
        });
        cookie.deleteCharAt(cookie.length()-1);
        headers.put("Cookie",cookie.toString());

        try {
            getHeaders(new File("etc/ctrip/request.txt"),headers);

            String content = "";
            url = "http://taocan.ctrip.com/SH/Ajax/DPSearch/AjaxDPGetRoomAndScenicSpot.aspx";
            content = HttpClientUtils.httpPost(url,"utf-8",headers,params,null);

            System.out.println(Thread.currentThread().getName() + " ----> " +  content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        driver.quit();
    }

    public Map<String,String> getDiscountParams(String html){
        Map<String,String> params = new HashMap<String,String>();
        params.put("CheckInDate","checkIn:'([\\d-]+)'");
        params.put("checkOutDate","checkOut:'([\\d-]+)'");
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
}
