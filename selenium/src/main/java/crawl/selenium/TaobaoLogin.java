package crawl.selenium;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.Set;

/**
 * Created by BFD_303 on 2017/1/6.
 */
public class TaobaoLogin {
    private static final Log LOG = LogFactory.getLog(TaobaoLogin.class);

    public void login(String username,String passwd){
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.taobao.com/");

        driver.findElement(By.className("h")).click();

        WebElement quickLogin = driver.findElement(By.id("J_Quick2Static"));
        Actions actions = new Actions(driver);
        actions.moveToElement(quickLogin).click();


        driver.findElement(By.id("TPL_username_1")).sendKeys(username);
        driver.findElement(By.id("TPL_password_1")).sendKeys(passwd);
        driver.findElement(By.id("J_SubmitStatic")).click();

        Set<Cookie> cookies = driver.manage().getCookies();
        cookies.forEach( c -> System.out.println(c.toString()));

        driver.quit();
    }
}
