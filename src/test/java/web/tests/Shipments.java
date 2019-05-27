package web.tests;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import web.DriverBase;
import web.config.ConfigFileReader;
import web.page_objects.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import web.page_objects.ShipmentsPage;

public class Shipments extends DriverBase {
    ConfigFileReader configFileReader;

    private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
        return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
    }



    @Test
    public void verifyLoginAttemptWithCredentials() throws Exception {
        WebDriver driver = getDriver();
        driver.manage().window().maximize();


        configFileReader= new ConfigFileReader();
        driver.get(configFileReader.getApplicationUrl());
        LoginPage login = new LoginPage();
        ShipmentsPage shipment = new ShipmentsPage();

        login.clickUserNameField(driver);
        login.insertUsername(configFileReader.getAdminUsername());
        login.insertPassword(configFileReader.getAdminPassword());
        login.clickSubmitBtn(driver);
        shipment.clickHomeTab(driver);
        //driver.findElement(By.xpath("/html/body/app-root/app-layout/div[1]/div/div/app-login/div/div/div[2]/form/button")).click();

        //shipment.clickHomeTab(driver);
        shipment.clickShipmentsTab(driver);

        //Thread.sleep(10000);
        /*shipment.clickShipmentsDataTab(driver);
        shipment.clickSearchField(driver);
        shipment.searchField(driver).clear();
        shipment.searchField(driver).sendKeys("ZHtst114");
        shipment.clickShipmentSearchBtn(driver);
        Thread.sleep(5000);

         */

    }



    @Test (dependsOnMethods={"verifyLoginAttemptWithCredentials"}, alwaysRun=true)
    public void EndUserWithValidUserID() throws Exception {
        WebDriver driver = getDriver();
        configFileReader= new ConfigFileReader();
        driver.get("http://prepilot-webtool.s3-website.eu-central-1.amazonaws.com/end-users");
        LoginPage login = new LoginPage();
        ShipmentsPage shipment = new ShipmentsPage();
        shipment.clickTitle(driver);
        shipment.clickFilter(driver);
        shipment.clickOption(driver);
        shipment.clickField(driver);
        shipment.field(driver).sendKeys("076241ad-5e63-4d70-b339-d71f9fb44c99");
        shipment.clickSearchBtn(driver);
        Thread.sleep(2000);
        shipment.clickRow(driver);
        String element = shipment.row(driver).getText();
        System.out.println(element);
        Assert.assertEquals(element, "076241ad-5e63-4d70-b339-d71f9fb44c99");

    }


    @Test (dependsOnMethods={"EndUserWithValidUserID"}, alwaysRun=true)
    public void EndUserWithInvalid() throws Exception {
        WebDriver driver = getDriver();
        configFileReader= new ConfigFileReader();
        //driver.get("http://prepilot-webtool.s3-website.eu-central-1.amazonaws.com/end-users");
        LoginPage login = new LoginPage();
        ShipmentsPage shipment = new ShipmentsPage();
        shipment.clickClearBtn(driver);
        shipment.clickTitle(driver);
        shipment.clickFilter(driver);
        shipment.clickOption(driver);
        shipment.clickField(driver);
        shipment.field(driver).sendKeys("XXXXXXXXXX");
        shipment.clickSearchBtn(driver);
        Thread.sleep(2000);
        shipment.clickResult(driver);
        String element = shipment.result(driver).getText();
        System.out.println(element + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
        Assert.assertEquals(element, "Record not found.");

    }

}