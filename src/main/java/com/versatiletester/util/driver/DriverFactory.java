package com.versatiletester.util.driver;

import com.versatiletester.config.MavenProfiles;
import com.versatiletester.config.SpringContext;
import com.google.common.base.Preconditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    private static Logger log = Logger.getLogger(DriverFactory.class);

    private static final String CHROME_DRIVER_VERSION = "2.43";
    private static final String GECKO_DRIVER_VERSION = "0.23.0";

    private RemoteWebDriver driver;
    private DriverCapabilityManager driverCapabilityManager;

    @Autowired
    protected SpringContext springContext;

    public DriverFactory(DriverCapabilityManager driverCapabilityManager){
        this.driverCapabilityManager = driverCapabilityManager;
    }

    public RemoteWebDriver getInstance() {
        if(driver != null){
            try{
                driver.getCurrentUrl();
                return this.driver;
            } catch(NoSuchSessionException e){
                log.info("NoSuchSessionException (Most likely from calling close() on Driver - Spring does this). " +
                        "Calling Quit() on driver and resetting instance.");
                driver.quit();
                return instantiateDriver();
            }
        } else{
            return instantiateDriver();
        }
    }
    private RemoteWebDriver instantiateDriver(){
        switch(MavenProfiles.getMatch(springContext.getProperty(SpringContext.MAVEN_PROFILE_PROPERTY_NAME))){
            case LOCAL:{
                log.info("'Local' Maven profile activated, instantiating driver");
                this.driver = instantiateLocalDriver();
                break;
            }
            case GRID:{
                log.info("'Grid' Maven profile activated, instantiating driver");
                Preconditions.checkArgument(springContext.getProperty(SpringContext.GRID_URL_PROPERTY_NAME) != null,
                        "In order to use the selenium grid maven profile, pass in the grid url as " +
                                "'-Dgrid.url=http://127.0.0.1:4444/hub/wb' or set it in the grid config file.");
                this.driver = instantiateRemoteDriver(
                        springContext.getProperty(SpringContext.GRID_URL_PROPERTY_NAME),
                        driverCapabilityManager.generateDefaultChromeCapabilities(
                                springContext.getProperty(SpringContext.PROJECT_NAME_PROPERTY_NAME),
                                springContext.getProperty(SpringContext.BUILD_NUM_PROPERTY_NAME)));
                break;
            }
            case BROWSERSTACK:{
                log.info("'Browserstack' Maven profile activated, instantiating driver");

                GDSBrowsers gdsBrowser = GDSBrowsers.getMatch(springContext.getProperty(SpringContext.BROWSER_PROPERTY_NAME));

                Preconditions.checkArgument(gdsBrowser != null, "Unknown browser defined: " +
                        springContext.getProperty(SpringContext.BROWSER_PROPERTY_NAME));

                // Null values from mobile devices handled in the 'generateBrowserStackCapabilities' method.
                this.driver = instantiateRemoteDriver(
                        springContext.getProperty(SpringContext.BSTACK_URL_PROPERTY_NAME),
                        driverCapabilityManager.generateBrowserStackCapabilities(
                                gdsBrowser,
                                springContext.getProperty(SpringContext.PROJECT_NAME_PROPERTY_NAME),
                                springContext.getProperty(SpringContext.BUILD_NUM_PROPERTY_NAME),
                                springContext.getProperty(SpringContext.LOCAL_ID_PROPERTY_NAME),
                                Boolean.parseBoolean(springContext.getProperty(
                                        SpringContext.BSTACK_LOCAL_BOOL_PROPERTY_NAME))));
                break;
            }
        }
        resetDriverTimeouts();
        setupBrowserWindow();
        return this.driver;
    }

    private RemoteWebDriver instantiateLocalDriver(){
        switch(LocalBrowsers.getMatchingBrowser(springContext.getProperty(SpringContext.BROWSER_PROPERTY_NAME))){
            case FIREFOX:{
                WebDriverManager.firefoxdriver().setup();
                return getFirefoxDriver();
            }
            /* TODO: Comment back in and implement methods if the browsers are required
            case IE:{
                WebDriverManager.iedriver().setup();
                return getIEDriver();
            }
            case EDGE:{
                WebDriverManager.edgedriver().setup();
                return getEdgeDriver();
            }*/
            default:{} //Generate a chrome instance by default.
            case CHROME:{
                return getChromeDriver();
            }
        }
    }

    private RemoteWebDriver instantiateRemoteDriver(String url, Capabilities caps) {
        try {
            return new RemoteWebDriver(new URL(url),caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static FirefoxDriver getFirefoxDriver() {
        WebDriverManager.firefoxdriver().version("0.26").setup();
        return new FirefoxDriver(new FirefoxOptions());
    }
    private ChromeDriver getChromeDriver() {
        WebDriverManager.chromedriver().version("85").setup();
        return new ChromeDriver();
    }

    public void resetDriverTimeouts(){
        /* The driver will wait x seconds for every page to load */
        driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
        /* The driver will wait x seconds for every element to be visible
         Increased the timeout to 10 seconds as IE needs more than 5 */
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    private void setupBrowserWindow(){
        MavenProfiles profile = MavenProfiles.getMatch(springContext.getProperty(SpringContext.MAVEN_PROFILE_PROPERTY_NAME));

        switch(profile){
            case BROWSERSTACK:{
                GDSBrowsers browser = GDSBrowsers.getMatch(springContext.getProperty(SpringContext.BROWSER_PROPERTY_NAME));

                //Maximize doesn't work on mobile operating systems
                if( browser != GDSBrowsers.OSX_CHROME &&
                        browser != GDSBrowsers.SAMSUNG_CHROME &&
                        browser != GDSBrowsers.IPAD_SAFARI )
                {
                    driver.manage().window().maximize();
                }

                //For OSX Chrome, you need to fullscreen the browser instead of maximising
                if(browser == GDSBrowsers.OSX_CHROME){
                    driver.manage().window().fullscreen();
                }

                break;
            }
            case GRID:{ } //Grid/Local profiles are the same logic
            case LOCAL:{
                if( SystemUtils.IS_OS_MAC &&
                        LocalBrowsers.getMatchingBrowser(springContext.getProperty(SpringContext.BROWSER_PROPERTY_NAME))
                        == LocalBrowsers.CHROME) {
                    driver.manage().window().fullscreen();
                } else {
                    driver.manage().window().maximize();
                }
                break;
            }
        }
    }
}
