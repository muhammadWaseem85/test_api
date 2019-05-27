package android_app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



public class Login {

    public static AppiumDriver driver;

    @BeforeTest
    public void Setup() throws MalformedURLException,InterruptedException {

        DesiredCapabilities cap = new DesiredCapabilities();


        //File file = new File ("E:\\app\\Komoot.apk");
        File file = new File ("C:\\Appium-Maven\\courier-app-2.0.9.apk");
        //File file = new File ("/Users/wasim/Documents/debug/app-debug.apk");
        cap.setCapability("app", file.getAbsolutePath());
        cap.setCapability("newCommandTimeout", "9300");
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "7.1");
        cap.setCapability("deviceName","APPOA83");
        //cap.setCapability("autoGrantPermissions","true");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);


    }


    //@AfterTest
    public void zTerminatek() {
        driver.quit();

    }



    //Test: Add a product in a cart
    @Test
    public void verifyProductIsAdded() throws InterruptedException {

        Thread.sleep(5000);
        driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.Button[2]")).click();
        //driver.findElementByAccessibilityId("permission_allow_button").click();
        Thread.sleep(5000);
        Thread.sleep(1000);
        driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]/android.view.View/android.widget.EditText")).sendKeys("ALPHA");
        Thread.sleep(1000);
        driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[4]/android.view.View/android.widget.EditText")).sendKeys("123456");
        Thread.sleep(1000);
        driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[6]/android.widget.Button")).click();
        Thread.sleep(3000);


    }












}

