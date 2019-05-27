package web.page_objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import web.DriverBase;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LoginPage {

    private final RemoteWebDriver driver = DriverBase.getDriver();

    public Query userName= new Query(By.id("username"),driver);
    public Query userPassword = new Query(By.id("password"), driver);
    public Query loginBtn = new Query(By.xpath("/html/body/app-root/app-layout/div[1]/div/div/app-login/div/div/div[2]/form/button/span"), driver);

    public LoginPage() throws Exception {

    }

    public  LoginPage insertUsername (String username){
        userName.findWebElement().clear();
        userName.findWebElement().sendKeys(username);
        return this;
    }

    public  LoginPage insertPassword (String password){
        userPassword.findWebElement().clear();
        userPassword.findWebElement().sendKeys(password);
        return this;
    }

    public  LoginPage loginBtn (){
        loginBtn.findWebElement().click();
        return  this;

    }

    public static WebElement userNameField(WebDriver driver)
    {
        return driver.findElement(By.id("username"));
    }

    public static WebElement submitBtn(WebDriver driver)
    {
        return driver.findElement(By.xpath("/html/body/app-root/app-layout/div[1]/div/div/app-login/div/div/div[2]/form/button"));
    }

    public void clickUserNameField (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement userNameField = userNameField(driver);
                userNameField.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. userNameField");
                Thread.sleep(2000);
            }
        }
    }

    public void clickSubmitBtn (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement submitBtn = submitBtn(driver);
                submitBtn.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. clickSubmitBtn");
                Thread.sleep(2000);
            }
        }
    }

/*

    public HomePage submitSearch() {
        googleSearch.findWebElement().submit();

        return this;
    }

 */




}

