package crawl.selenium;

/**
 * Created by BFD_303 on 2017/1/6.
 */
public class WebDriverTest {
    static{
        //加载驱动
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");
    }

    public static void main(String[] args) {
        TaobaoLogin taobao = new TaobaoLogin();

        taobao.login("username","passwd");
    }

}
