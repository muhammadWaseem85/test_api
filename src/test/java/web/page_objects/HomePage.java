package web.page_objects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    private static WebElement element;


    public static WebElement homeElement(WebDriver driver){
        element = driver.findElements(By.className("head__nav-department-dropdown")).get(0);
        return element;
    }

    public static WebElement profileImg(WebDriver driver){
                                               //*[@id="masthead"]/div[1]/div/div[3]/div/a[1]
        return  driver.findElement(By.xpath("//*[@id=\"masthead\"]/div[1]/div/div[3]/div/a[2]"));

    }

    public static WebElement homeIcon(WebDriver driver){
        element = driver.findElement(By.xpath("//*[@id=\"masthead\"]/div[2]/div/div/div[1]/div/ul/li/i[1]"));
        return element;
    }

    public static WebElement humanResource(WebDriver driver){
        element = driver.findElement(By.linkText("Human Resources"));
        return element;
    }

    public static WebElement sales(WebDriver driver){
        element = driver.findElement(By.linkText("Sales"));
        return element;
    }

    public static WebElement marketing(WebDriver driver){
        element = driver.findElement(By.linkText("Marketing"));
        return element;
    }


    public static WebElement dropBtn(WebDriver driver){
        element = driver.findElement(By.xpath("(//*[@id=\"dropzoneWordpressForm\"]/div[2]/p)[1]"));
        return element;
    }

    public static WebElement staffDirectory(WebDriver driver){
        element = driver.findElement(By.linkText("Staff Directory"));
        return element;
    }

    public static WebElement searchField(WebDriver driver){
        element = driver.findElement(By.xpath("//*[@id='searchform']/div/input"));
        return element;
    }

    public static WebElement searchPageHeading(WebDriver driver){
        return driver.findElement(By.xpath("//*[@id=\"main\"]/header/h1"));

    }

    public static WebElement noSearchResultFound(WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@id=\"main\"]/div/div/p"));
    }

    public static WebElement addFileField (WebDriver driver)
    {
        return driver.findElement(By.xpath("(//*[@id=\"dropzoneWordpressForm\"]/div[2]/p)[1]"));
    }

    public static WebElement contextMenuDocumentExplorer (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@id=\"download-table-2\"]/div[3]/div[3]"));
    }

    public static WebElement contextMenuFeaturedDocument (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@id=\"download-table-1\"]/div[3]/div[3]"));
    }

    public static WebElement renameImageDocumentExplorer (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@id=\"download-table-2\"]/div[3]/ul/li[3]"));
    }

    public static WebElement renameFieldDocumentExplorer (WebDriver driver)
    {
        return driver.findElement(By.className("rename-file"));
    }

    public static WebElement deleteImageDocumentExplorer (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@id=\"download-table-2\"]/div[3]/ul/li[2]"));
    }

    public static WebElement deleteFeaturedDocument (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@id=\"download-table-1\"]/div[3]/ul/li[2]"));
    }

    public static WebElement readMoreBtn (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@class='news__link']/a"));
    }

    public static WebElement postedByText (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@class='news-single']/p"));
    }

    public static WebElement rootFolder (WebDriver driver)
    {
        return driver.findElement(By.xpath("//*[@id=\"download-table-2\"]/div[1]/span"));
    }

    public static WebElement renameImg (WebDriver driver)
    {
        return driver.findElement(By.className("rename-file"));
    }


    /**
 *
 *
 * Functions
 *
 */

    //*[@id="masthead"]/div[1]/div/div[3]/div/a[1]
    public static void clickProfileImage (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement profileImage = profileImg(driver);
                profileImage.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. profileImg(driver)");
                Thread.sleep(1000);
            }
        }
    }


    public static void clickPostedByText (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement text = postedByText(driver);
                text.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. postedByText(driver)");
                Thread.sleep(1000);
            }
        }
    }


    public static void clickReadMoreBtn (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement btn = readMoreBtn(driver);
                btn.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. readMoreBtn(driver)");
                Thread.sleep(1000);
            }
        }
    }




    public static void clickAddFileField (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement field = addFileField(driver);
                field.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. addFileField(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickContextMenuDocumentExplorer (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement menu = contextMenuDocumentExplorer(driver);
                menu.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. contextMenuDocumentExplorer(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickRenameImageDocumentExplorer (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement element = renameImageDocumentExplorer(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. renameImageDocumentExplorer(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickDeleteImageDocumentExplorer (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement element = deleteImageDocumentExplorer(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. deleteImageDocumentExplorer(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickHumanResourceTab (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement tab = humanResource(driver);
                tab.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. homeElement(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void focusHomeElement (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement element = homeElement(driver);
                element.sendKeys("");
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. homeElement(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickHomeElement (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement element = homeElement(driver);
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. homeElement(driver)");
                Thread.sleep(1000);
            }
        }
    }
    public static void clickSearchField (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement field = searchField(driver);//homeElement(driver);
                field.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. click searchField(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickHomeIcon (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement icon = homeIcon(driver);
                icon.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. click searchField(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickSearchPageHeading (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement element = searchPageHeading(driver);//
                element.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. searchPageHeading(driver)");
                Thread.sleep(1000);
            }
        }
    }
    public static void clearSearchField (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement field = searchField(driver);//homeElement(driver);
                field.clear();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. clear searchField(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void submitSearch (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement field = searchField(driver);//homeElement(driver);
                field.submit();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. homeElement(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickContextMenuFeaturedDocument (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement menu = contextMenuFeaturedDocument(driver);
                menu.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. contextMenuFeaturedDocument(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickDeleteFeaturedDocument (WebDriver driver) throws InterruptedException {
        for(int i=0;i<5;i++)
        {
            try{
                WebElement deleteOption = deleteFeaturedDocument(driver);
                deleteOption.click();
                break;
            }
            catch(Exception e)
            {
                System.out.println(">>> Element not found. deleteFeaturedDocument(driver)");
                Thread.sleep(1000);
            }
        }
    }

    public static void insertRename (WebDriver driver, String text) throws InterruptedException {
        renameFieldDocumentExplorer(driver).clear();
        Thread.sleep(500);
        renameFieldDocumentExplorer(driver).sendKeys(text);
        Thread.sleep(750);

    }


    public static void insertSearch (WebDriver driver, String text) throws InterruptedException {
        searchField(driver).clear();
        Thread.sleep(500);
        searchField(driver).sendKeys(text);
        Thread.sleep(750);

    }





    /*

    public HomePage submitSearch() {
        googleSearch.findWebElement().submit();

        return this;
    }

 */




}

