package com.versatiletester.util.driver;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Test Utility Class - used to contain all RemoteWebDriver related utility methods.
 */
@SuppressWarnings("WeakerAccess")
public class DriverUtils {
    private static final Logger logger = Logger.getLogger(DriverUtils.class);

    private DriverFactory driverFactory;

    public DriverUtils(DriverFactory driverFactory){
        this.driverFactory = driverFactory;
    }

    /**
     * Generic utility method which clicks an element based on a partial match of it's text.
     *
     * @param xpathElement - Used to specify an element's type more uniquely
     * @param text - The search text of the element
     */
    public void clickByText(String xpathElement, String text){
        WebElement element = driverFactory.getInstance().findElement(
                By.xpath("//" + xpathElement + "[text()[contains(normalize-space(.),'" + text + "')]]"));
        scrollToElement(element);
        clickPageTransitionElement(element);
    }
    /** Overload method for clickByText which clicks any(the first) matching element */
    public void clickByText(String text){ clickByText("*",text); }

    /**
     * Generic utility method which enters text into an element based on it's value attribute. Useful for Radio buttons.
     *
     * @param xpathElement - Used to specify an element's type more uniquely
     * @param value - The 'value' css attribute of the element
     */
    public void clickByValue(String xpathElement, String value){
        WebElement element = driverFactory.getInstance().findElement(
                By.xpath("//" + xpathElement + "[@value='" + value + "']"));
        scrollToElement(element);
        clickPageTransitionElement(element);
    }

    public void clickPageTransitionElement(WebElement element) {
        element.click();
        waitUntilPageFullyLoaded();
    }

    public boolean elementExists(WebElement element){
        waitUntilPageFullyLoaded();
        // Set a small element timeout because it is a try catch.
        driverFactory.getInstance().manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        try{
            element.isEnabled();
            driverFactory.resetDriverTimeouts();
            return true;
        } catch(NoSuchElementException e){
            driverFactory.resetDriverTimeouts();
            return false;
        }
    }

    public byte[] getScreenshotBytes(){
        return ((TakesScreenshot)driverFactory.getInstance()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Generic utility method which finds any element with the specified search text, returning the element found.
     *
     * @param searchString - The text used to search for within the current page (ignoring irregular formatting).
     * @return WebElement - The element found on the current page, or null if the element is not found.
     */
    public boolean isStringVisible(String xpathElement, String searchString){
        waitUntilPageFullyLoaded();
        // Set a small element timeout because it is a try catch.
        driverFactory.getInstance().manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        try{
            driverFactory.getInstance().findElement(By.xpath(xpathElement + "[text()[contains(normalize-space(.),'" + searchString + "')]]"));
            driverFactory.resetDriverTimeouts();
            return true;
        } catch(NoSuchElementException e){
            driverFactory.resetDriverTimeouts();
            return false;
        }
    }
    /** Overload method for isStringVisible which matches any element regardless of identifier */
    public boolean isStringVisible(String searchString){
        return isStringVisible("//*", searchString);
    }

    /** Will scroll to the element provided, meeting the prerequisite of visibility before interaction. */
    public void scrollToElement(WebElement element){
        ((JavascriptExecutor) driverFactory.getInstance()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void takeScreenshot(String filePrefix){
        try {
            File screenshotFile = ((TakesScreenshot)driverFactory.getInstance()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File("./target/reports/" + filePrefix + "." +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("'T'yyyyMMddHHmmssS")) + "." + driverFactory.getInstance().getSessionId() + ".png"));
        } catch (IOException ex) { logger.error("Attempt to take screenshot failed, cause was: " + ex.getCause()); }
    }

    public void waitUntilPageFullyLoaded(){
        //Wait required to ensure DOM cache location is reset (page has begun transitioning to the next page + DOM is null)
        try { Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
        new WebDriverWait(driverFactory.getInstance(), 60).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        if((Boolean)((JavascriptExecutor) driverFactory.getInstance()).executeScript("return window.jQuery != undefined")){
            new WebDriverWait(driverFactory.getInstance(), 10).until(
                    webDriver -> (Boolean)((JavascriptExecutor) webDriver).executeScript("return jQuery.active === 0"));
        }
        //Wait required to ensure full loading of UI elements as it is not encompassed in the above DOM/JS waits
        try { Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
