package web.utils;

import web.DriverBase;
import web.config.ConfigFileReader;
import web.page_objects.HomePage;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.time.Duration;

import org.openqa.selenium.By;


import static java.util.concurrent.TimeUnit.SECONDS;

public class Utils extends DriverBase {

    public static WebDriver driver;
    ConfigFileReader configFileReader;


   // Switch to first tab
   public static  void switchToFirstTab (WebDriver driver){
       ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
       System.out.println("No. of tabs: >>>>>>>>>>>>>>>>" + tabs.size());
       driver.switchTo().window(tabs.get(0));

   }

   //Switch to second tab
    public static  void switchToSecondTab (WebDriver driver){
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        System.out.println("No. of tabs: >>>>>>>>>>>>>>>>" + tabs.size());
        if (tabs.size() >1 ){
            driver.switchTo().window(tabs.get(1));
            System.out.println("User is in second tab");
        } else {
            driver.switchTo().window(tabs.get(0));
            System.out.println("User is in default tab");
        }

    }


    // Check for browser
    public static  void  checkForBrowser () throws InterruptedException {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        if (browserName.contains("firefox")){
            System.out.println("Browser Detected" + browserName);
            Thread.sleep(3000);

        }else {
            System.out.println("Browser Detected" + browserName);
        }
    }

    // Switch to third tab
    public static  void switchToThirdTab (WebDriver driver){
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        System.out.println("No. of tabs: >>>>>>>>>>>>>>>>" + tabs.size());
        driver.switchTo().window(tabs.get(2));
    }

    // Switch to fourth tab
    public static  void switchToFourthTab (WebDriver driver){
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        System.out.println("No. of tabs: >>>>>>>>>>>>>>>>" + tabs.size());
        driver.switchTo().window(tabs.get(3));


    }

    // Switch to fifth tab
    public static  void switchToFifthTab (WebDriver driver){
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        System.out.println("No. of tabs: >>>>>>>>>>>>>>>>" + tabs.size());
        driver.switchTo().window(tabs.get(4));
    }




    //Login Function for Staging
    public void logoutFunction(WebDriver driver) throws Exception {
        HomePage home = new HomePage();
        configFileReader= new ConfigFileReader();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        driver.get(configFileReader.getApplicationUrl());
        driver.findElement(By.xpath("//*[@id=\"masthead\"]/div[1]/div/div[3]/div/i")).click();
        driver.findElement(By.linkText("Logout")).click();

    }















    //Pause / Wait function
    public static WebDriverWait Wait(WebDriver driver, int timeOut)
    {

        return new WebDriverWait(driver, timeOut);

    }


    //Javascript scroll down function
    public static void scrollDownLow(WebDriver driver)
    {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,500)", "");
    }



    //Javascript scroll down function
    public static void scrollDown(WebDriver driver)
    {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,1000)", "");
    }


    //Javascript scroll down function
    public static void scrollDownMore(WebDriver driver)
    {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,2000)", "");
    }



    // wait for Ajax request function
    // Utils.waitForAjax(driver, "Ajax_call");
    //
    public static void waitForAja(WebDriver driver, String action) {
        driver.manage().timeouts().setScriptTimeout(10, SECONDS);
        ((JavascriptExecutor) driver).executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                        "var xhr = new XMLHttpRequest();" +
                        "xhr.open('POST', '/" + action + "', true);" +
                        "xhr.onreadystatechange = function() {" +
                        "  if (xhr.readyState == 4) {" +
                        "    callback(xhr.responseText);" +
                        "  }" +
                        "};" +
                        "xhr.send();");
    }





    public static void fWait(final String elementId,final String value){
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(100))
                .pollingEvery(Duration.ofSeconds(3))
                //.ignoring(NoSuchElementException.class)
                .until(new ExpectedCondition<Boolean>(){
                 public Boolean apply(    WebDriver wd){
                     WebElement element=wd.findElement(By.xpath(elementId));
                     System.out.println(element + "_________________________________________");
                     String txt = element.getText();
                     System.out.println(txt + "___________________________________________");
                     return element.getText().contains(value);
                 }
             });





    }



















}
