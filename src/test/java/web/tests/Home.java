package web.tests;

import web.DriverBase;
import web.config.ConfigFileReader;
import web.page_objects.HomePage;
import web.page_objects.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Home extends DriverBase {
    ConfigFileReader configFileReader;

    private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
        return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
    }

    @Test
    public void verifyUserNavigatesToHomePage() throws Exception {
        WebDriver driver = getDriver();
        LoginPage loginPage = new LoginPage();
        HomePage home = new HomePage();
        configFileReader= new ConfigFileReader();
        driver.get(configFileReader.getApplicationUrl());
        driver.manage().window().maximize();
        loginPage.insertUsername(configFileReader.getAdminUsername());
        loginPage.insertPassword(configFileReader.getAdminPassword());
        loginPage.loginBtn();
        home.clickHomeElement(driver);
        String confirmHomeLocator = home.homeElement(driver).getText();
        System.out.println(confirmHomeLocator);
        Assert.assertTrue(confirmHomeLocator.contains("Home"));
    }

    @Test (dependsOnMethods={"verifyUserNavigatesToHomePage"}, alwaysRun=true)
    public void verifySearchFeature() throws Exception {
        WebDriver driver = getDriver();
        configFileReader= new ConfigFileReader();
        HomePage home = new HomePage();
        home.clickSearchField(driver);
        home.insertSearch(driver, "wasim");
        home.submitSearch(driver);
        home.clickSearchPageHeading(driver);
    }

    @Test (dependsOnMethods={"verifySearchFeature"}, alwaysRun=true)
    public void verifySearchFeatureIfNoResult() throws Exception {
        WebDriver driver = getDriver();
        configFileReader= new ConfigFileReader();
        HomePage home = new HomePage();
        driver.get(configFileReader.getApplicationUrl());
        home.clickSearchField(driver);
        home.insertSearch(driver, "fkjsldkf&**&##*pp");
        home.submitSearch(driver);
        home.clickSearchPageHeading(driver);
        String msg = home.noSearchResultFound(driver).getText();
        System.out.println(msg);
        Assert.assertTrue(msg.contains("No related"));
    }

    @Test (dependsOnMethods={"verifySearchFeatureIfNoResult"}, alwaysRun=true)
    public void verifyAnnouncementSlider() throws Exception {
        WebDriver driver = getDriver();
        configFileReader= new ConfigFileReader();
        HomePage home = new HomePage();
        driver.get(configFileReader.getApplicationUrl());
        WebElement image = driver.findElements(By.className("owl-dot")).get(0);
        image.click();
        Thread.sleep(1000);
        WebElement image2 = driver.findElements(By.className("owl-dot")).get(1);
        image2.click();
        Thread.sleep(1000);
        WebElement image3 = driver.findElements(By.className("owl-dot")).get(2);
        image3.click();
        Thread.sleep(1000);
        WebElement image4 = driver.findElements(By.className("owl-dot")).get(3);
        image4.click();
        Thread.sleep(1000);
        WebElement image5 = driver.findElements(By.className("owl-dot")).get(4);
        image5.click();
    }

}