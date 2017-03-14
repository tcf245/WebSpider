package crawl.util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.util.Set;

/**
 * Created by BFD_303 on 2017/3/2.
 */
public class SeleniumUtils {
    private static WebDriver driver = new PhantomJSDriver();

    static{
        //加载驱动
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");
    }

    public static String getCookie(String url){
        StringBuffer cookie = new StringBuffer();

        driver.get(url);
        Set<Cookie> cookies =  driver.manage().getCookies();
        cookies.forEach(c -> {
            cookie.append(c.getName() + "=" + c.getValue());
            cookie.append(";");
        });
        cookie.deleteCharAt(cookie.length()-1);
//        driver.close();
        return cookie.toString();
    }

}
