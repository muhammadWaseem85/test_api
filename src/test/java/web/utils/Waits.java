package web.utils;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Waits {

    public static WebElement waitVisibilityOfElement(WebDriverWait wait, WebElement element) {
        try {
            element = wait.until(ExpectedConditions.visibilityOf(element));
            return element;
        } catch (Exception e) {
            System.out.println("The element is not visible. " + element.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean waitVisibilityOfTwoElements(WebDriverWait wait, WebElement element1, WebElement element2) {
        try {

            return wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(element1),
                    ExpectedConditions.visibilityOf(element2)
            ));
        } catch (Exception e) {
            System.out.println("The elements are not visible. " + element1.toString() + "     " + element2.toString());
            //e.printStackTrace();
            return false;
        }
    }

    public static WebElement waitPresenceOfElement(WebDriverWait wait, By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            System.out.println("The element is not presence using the locator: " + locator.toString());
            return null;
        }
    }

    public static List<WebElement> waitPresenceOfAllElements(WebDriverWait wait, By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            System.out.println("The elements are not presence using the locator: " + locator.toString());
            return new ArrayList<>();
        }
    }

    public static List<WebElement> waitMoreThanANumberOfElements(WebDriverWait wait, By locator, int numberOfElements) {
        try {
            return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, numberOfElements));
        } catch (Exception e) {
            System.out.println("The elements are not more than: " + numberOfElements + " using the locator: " + locator.toString());
            return new ArrayList<>();
        }
    }

    public static boolean waitUntilNumberOfElementsIs(WebDriverWait wait, By locator, int numberOfElements) {
        try {
            return (wait.until(ExpectedConditions.numberOfElementsToBe(locator, numberOfElements)).size() == numberOfElements);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean waitUntilNumberOfElementsIsMoreThan(WebDriverWait wait, By locator, int numberOfElements) {
        try {
            return (wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, numberOfElements)).size() > numberOfElements);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean waitUntilElementsAreInvisible(WebDriverWait wait, List<WebElement> elements) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
        } catch (TimeoutException e) {
            // Returns true because the element is not present in DOM. The
            // previous block checks if the element is present but is invisible.
            return true;
        } catch (Exception e) {
            System.out.println("The elements are visible. " + elements.toString());
//            e.printStackTrace();
            return false;
        }
    }

    public static boolean waitUntilElementIsInvisible(WebDriverWait wait, By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            System.out.println("The element is still visible. " + locator.toString());
            return false;
        }
    }

    public static Boolean waitUntilPopUpWindowsIsOpen(WebDriverWait wait, WebDriver driver, int expectedNumberOfWindows) {
        try {
            return wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
        } catch (Exception e) {
            System.out.println("The number of Windows expected isn't " + expectedNumberOfWindows + ", WIndows open is: " + driver.getWindowHandles().size());
//            e.printStackTrace();
            return false;
        }
    }

    public static Boolean waitUntilUrlContains(WebDriverWait wait, String text) {
        try {
            return wait.until((WebDriver driver) -> driver.getCurrentUrl().toLowerCase().contains(text.toLowerCase()));
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean waitUntilUrlDoesNotContains(WebDriverWait wait, String text) {
        try {
            return wait.until(ExpectedConditions.not(ExpectedConditions.urlContains(text)));
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean waitUntilTextIsEqual(WebDriverWait wait, String expectedText, WebElement element) {
        try {
            return wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    try {
                        String elementText = element.getText().trim();
                        return Boolean.valueOf(elementText.equalsIgnoreCase(expectedText));
                    } catch (StaleElementReferenceException var3) {
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("The text is not equal");
            return false;
        }
    }

    public static WebElement waitForElementToBeClickable(WebDriverWait wait, WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean waitUntilElementTextChange(WebDriverWait wait, By locator, String currentText) {
        try {
            return wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(locator, currentText)));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean waitUntilElementsTextChange(WebDriverWait wait, By locator1, By locator2, By locator3, String text1, String text2, String text3) {
        try {
            return wait.until(ExpectedConditions.or(
                    ExpectedConditions.not(ExpectedConditions.textToBe(locator1, text1)),
                    ExpectedConditions.not(ExpectedConditions.textToBe(locator2, text2)),
                    ExpectedConditions.not(ExpectedConditions.textToBe(locator3, text3))));
        } catch (Exception e) {
            return false;
        }
    }

    public static List<WebElement> waitVisibilityOfElements(WebDriverWait wait, List<WebElement> elements) {
        try {
            elements = wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            return elements;
        } catch (Exception e) {
            System.out.println("The elements are not visible. " + elements.toString());
            //e.printStackTrace();
            return null;
        }
    }

    public static boolean numberOfWindowsToBe(WebDriverWait wait, final int numberOfWindows) {
        try {
            return wait.until( webDriver -> webDriver.getWindowHandles().size() == numberOfWindows);

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean waitForAttributeToChange(WebDriverWait wait, WebElement element, String attribute, String oldValue) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return wait.until((WebDriver driver) -> {
                try {
                    String attributeValue = element.getAttribute(attribute);
                    return !(attribute.toLowerCase().equals(oldValue.toLowerCase()));
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean waitForAttributeToContains(WebDriverWait wait, WebElement element, String attribute, String value) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return wait.until((WebDriver driver) -> {
                try {
                    String attributeValue = element.getAttribute(attribute);
                    return attribute.toLowerCase().contains(value.toLowerCase());
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean waitForAttributeToNotBeEmpty(WebDriverWait wait, WebElement element, String attribute) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return wait.until((WebDriver driver) -> {
                String attributeValue;
                try {
                    attributeValue = element.getAttribute(attribute);
                    if(attributeValue.length() > 0)
                        return true;
                    else
                        return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean waitForElementTextToNotBeEmpty(WebDriverWait wait, WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return wait.until((WebDriver driver) -> {
                String textValue = "";
                try {
                    textValue = element.getText();
                    if(textValue.length() > 0)
                        return true;
                    else
                        return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static WebElement fluentWaitWithElement(final WebDriver driver, final WebElement locator, final int timeoutSeconds) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .withMessage("Timeout occured!")
                .ignoring(NoSuchElementException.class);
        return wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                return ((WebElement) driver).findElement((By) locator);
            }
        });

    }

    public static WebElement xfluentWaitWithBy(final WebDriver driver, final By locator, final int timeoutSeconds) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .withMessage("Timeout occured!")
                .ignoring(NoSuchElementException.class);
        return wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                return driver.findElement(locator);
            }
        });

    }


}
