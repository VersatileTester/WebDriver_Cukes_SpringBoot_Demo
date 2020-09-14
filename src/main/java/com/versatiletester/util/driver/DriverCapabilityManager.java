package com.versatiletester.util.driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverCapabilityManager {

    public DesiredCapabilities generateBrowserStackCapabilities(GDSBrowsers gdsBrowser,
                                                                String projectName,
                                                                String buildNumber,
                                                                String localIdentifier,
                                                                boolean local) {
        DesiredCapabilities caps = new DesiredCapabilities();

        if(!(gdsBrowser.getOs().equalsIgnoreCase("windows")||gdsBrowser.getOs().equalsIgnoreCase("OS X"))){
            //Mobile Specific
            caps.setCapability("os_version", gdsBrowser.getOsVersion());
            caps.setCapability("device", gdsBrowser.getOs());
            caps.setCapability("real_mobile", "true");
        } else{
            //Desktop Specific
            caps.setCapability("os", gdsBrowser.getOs());
            caps.setCapability("os_version", gdsBrowser.getOsVersion());

            caps.setCapability("browserstack.selenium_version", "3.14.0");

            caps.setCapability("browser", gdsBrowser.getBrowser());
            caps.setCapability("browser_version", gdsBrowser.getBrowserVersion());
        }

        //Local Browserstack capability settings
        caps.setCapability("project", projectName);
        caps.setCapability("build", buildNumber);
        caps.setCapability("browserstack.localIdentifier", localIdentifier);

        caps.setCapability("browserstack.local", local);
        caps.setCapability("browserstack.timezone", "Europe/London");

        return caps;
    }

    public DesiredCapabilities generateDefaultChromeCapabilities(String projectName, String buildNumber){
        DesiredCapabilities caps = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();

        //caps.setBrowserName("chrome");

        if(projectName != null){
            caps.setCapability("project", projectName);
        }
        if(buildNumber != null) {
            caps.setCapability("build", buildNumber);
        }

        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.setExperimentalOption("useAutomationExtension", false);


        caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return caps;
    }
}