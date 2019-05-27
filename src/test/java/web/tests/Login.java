package web.tests;

import web.DriverBase;
import web.config.ConfigFileReader;
import web.page_objects.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;

public class Login extends DriverBase {
    ConfigFileReader configFileReader;

    private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
        return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
    }

    @Test
    public void verifyLoginAttemptWithoutCredentials() throws Exception {

        WebDriver driver = getDriver();
        configFileReader= new ConfigFileReader();
        driver.get(configFileReader.getApplicationUrl());
        driver.manage().window().maximize();
        LoginPage loginPage = new LoginPage();
        loginPage.loginBtn();
        Thread.sleep(2000);

    }

    @Test (dependsOnMethods={"verifyLoginAttemptWithoutCredentials"}, alwaysRun=true)
    public void verifyLoginAttemptWithUsernameOnly() throws Exception {

        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("flasdkfjlskfla");
        loginPage.loginBtn();
        Thread.sleep(2000);




    }

    @Test (dependsOnMethods={"verifyLoginAttemptWithUsernameOnly"}, alwaysRun=true)
    public void verifyLoginAttemptWithPasswordOnly() throws Exception {

        LoginPage loginPage = new LoginPage();
        loginPage.insertPassword("abd");
        loginPage.loginBtn();
        Thread.sleep(2000);

    }

    @Test (dependsOnMethods={"verifyLoginAttemptWithPasswordOnly"}, alwaysRun=true)
    public void verifyLoginAttemptWithIncorrectCredentials() throws Exception {

        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername("12345");
        loginPage.insertPassword("123456788");
        loginPage.loginBtn();
        Thread.sleep(2000);

    }

    @Test (dependsOnMethods={"verifyLoginAttemptWithIncorrectCredentials"}, alwaysRun=true)
    public void forgetPasswordLink() throws Exception {
        WebDriver driver = getDriver();
        //driver.findElement(By.linkText("Lost your password?"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1200)");
        driver.findElement(By.xpath("//*[@id=\"nav\"]/a")).click();
        Thread.sleep(2000);


    }

    @Test (dependsOnMethods={"forgetPasswordLink"}, alwaysRun=true)
    public void verifyResetMandatoryField() throws Exception {
        WebDriver driver = getDriver();
        driver.findElement(By.id("wp-submit")).click();
        Thread.sleep(2000);


    }

    @Test (dependsOnMethods={"verifyResetMandatoryField"}, alwaysRun=true)
    public void verifyPassword() throws Exception {
        WebDriver driver = getDriver();
        driver.findElement(By.id("user_login")).sendKeys("wasim.mmm@dplit.com");
        Thread.sleep(2000);
        driver.findElement(By.id("wp-submit")).click();
        Thread.sleep(2000);


    }

    @Test (dependsOnMethods={"verifyPassword"}, alwaysRun=true)
    public void verifyLoginAttemptWithCredentials() throws Exception {
        WebDriver driver = getDriver();
        configFileReader= new ConfigFileReader();
        driver.get(configFileReader.getApplicationUrl());
        LoginPage loginPage = new LoginPage();
        loginPage.insertUsername(configFileReader.getAdminUsername());
        loginPage.insertPassword(configFileReader.getAdminPassword());
        loginPage.loginBtn();




    }




}