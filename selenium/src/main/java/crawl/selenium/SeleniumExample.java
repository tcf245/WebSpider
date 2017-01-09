package crawl.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.stream.Collectors;

public class SeleniumExample {

    static{
        //加载驱动
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");
    }

    public static void main(String[] args) throws InterruptedException {

        // 创建了一个 Chrome driver 的实例
        // 注意，其余的代码依赖于接口而非实例
        WebDriver driver = new ChromeDriver();

        // 使用它访问 Google
        driver.get("http://www.google.com");
        // 同样的事情也可以通过以下代码完成
        // driver.navigate().to("http://www.google.com");

        // 找到搜索输入框
        WebElement element = driver.findElement(By.name("q"));

        // 输入要查找的词
        element.sendKeys("Cheese!");

        // 提交表单
        element.submit();

        // 检查页面标题
        System.out.println("Page title is: " + driver.getTitle());

        // Google 搜索结果由 JavaScript 动态渲染
        // 等待页面加载完毕，超时时间设为10秒
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });

        //应该能看到: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());

        //打印源码
        System.out.println("source code is: " + driver.getPageSource());

        //获取cookie并打印
        Set<Cookie> cookies = driver.manage().getCookies();
        cookies.forEach(c -> System.out.println(c.getName() + " ---> " + c.getValue()));

        //3秒后关闭浏览器
        Thread.sleep(3000);
        driver.quit();
    }
}
