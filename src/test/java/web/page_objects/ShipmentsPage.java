package web.page_objects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShipmentsPage {
    private static WebElement element;


    public static WebElement homeTab(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"outer-most-wrapper\"]/div[1]/app-sidebar/div[2]/div[1]/ul/li[1]/a/span"));
    }

    public static WebElement shipmentsTab(WebDriver driver)
    {
        //*[@id="outer-most-wrapper"]/div[1]/app-sidebar/div[2]/div[1]/ul/li[2]/a/span
        return  driver.findElement(By.xpath("//*[@id=\"outer-most-wrapper\"]/div[1]/app-sidebar/div[2]/div[1]/ul/li[2]/a/span"));
    }

    public static WebElement shipmentsDataTab(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"outer-most-wrapper\"]/div[1]/app-sidebar/div[2]/div[1]/ul/li[2]/ul/li[1]/a/span"));
    }

    public static WebElement searchField(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-shipment/div/div/div[1]/div/form/div/div[2]/fieldset/div/input"));
    }

    public static WebElement shipmentSearchBtn(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-shipment/div/div/div[1]/div/form/div/div[3]/fieldset/button[1]"));
    }

    public static WebElement profileImg(WebDriver driver){

        return  driver.findElement(By.xpath("//*[@id=\"masthead\"]/div[1]/div/div[3]/div/a[2]"));

    }

    public static WebElement homeIcon(WebDriver driver){
        element = driver.findElement(By.xpath("//*[@id=\"masthead\"]/div[2]/div/div/div[1]/div/ul/li/i[1]"));
        return element;
    }

    /**
     *
     * Functions
     *
     */


    public void clickHomeTab (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement homeTab = homeTab(driver);
                homeTab.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. homeTab");
                Thread.sleep(2000);
            }
        }
    }

    public void clickShipmentsTab (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement shipmentTab = shipmentsTab(driver);//Tab(driver);
                shipmentTab.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. shipmentTab");
                Thread.sleep(3000);
            }
        }
    }


    public void clickShipmentsDataTab (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement shipmentDataTab = shipmentsDataTab(driver);//Tab(driver);
                shipmentDataTab.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  shipmentDataTab");
                Thread.sleep(2000);
            }
        }
    }

    public void clickSearchField (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{

                WebElement searchField = searchField(driver);
                searchField.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.   searchField.click()");
                Thread.sleep(2000);
            }
        }
    }

    public void clickShipmentSearchBtn (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement shipmentSearchBtn = shipmentSearchBtn(driver);
                shipmentSearchBtn.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.   shipmentSearchBtn.click();");
                Thread.sleep(2000);
            }
        }
    }



    /**
     *
     *
     *
     *
     *
     * */



    public static WebElement title(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/app-page-header/div/div[1]/div/span"));
    }

    public void clickTitle (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = title(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  title(driver)");
                Thread.sleep(2000);
            }
        }
    }



    public static WebElement filter(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/div/div/div[1]/div/form/div/div[1]/fieldset/select"));
    }

    public void clickFilter (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = filter(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  filter(driver)");
                Thread.sleep(2000);
            }
        }
    }

    public static WebElement option(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/div/div/div[1]/div/form/div/div[1]/fieldset/select/option[3]"));
    }

    public void clickOption (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = option(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  filter(driver)");
                Thread.sleep(2000);
            }
        }
    }

    public static WebElement field(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/div[1]/div/div[1]/div/form/div/div[2]/fieldset/div/input"));
    }

    public void clickField (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = field(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  filter(driver)");
                Thread.sleep(2000);
            }
        }
    }


    public static WebElement searchBtn(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/div[1]/div/div[1]/div/form/div/div[3]/fieldset/button[1]"));
    }

    public void clickSearchBtn (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = searchBtn(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  filter(driver)");
                Thread.sleep(2000);
            }
        }
    }

    public static WebElement clearBtn(WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/div[1]/div/div[1]/div/form/div/div[3]/fieldset/button[2]"));
    }

    public void clickClearBtn (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = clearBtn(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  filter(driver)");
                Thread.sleep(2000);
            }
        }
    }


    public static WebElement row (WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/div[1]/div/div[2]/div/app-basic-data-table/form/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/a"));
    }

    public void clickRow (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = row(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  filter(driver)");
                Thread.sleep(2000);
            }
        }
    }

    public static WebElement result (WebDriver driver)
    {
        return  driver.findElement(By.xpath("//*[@id=\"main-content-wraper\"]/div/div/app-end-users/div/div/div[2]/div/app-basic-data-table/form/ngx-datatable/div/datatable-body/datatable-selection/div"));
    }

    public void clickResult (WebDriver driver) throws InterruptedException {
        for(int i=0;i<10;i++)
        {
            try{
                WebElement element = result(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found.  filter(driver)");
                Thread.sleep(2000);
            }
        }
    }

}

