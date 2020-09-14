package com.versatiletester.cukes;

import com.versatiletester.config.ScenarioContext;
import com.versatiletester.config.SpringContext;
import com.versatiletester.page.GoogleResultsPage;
import com.versatiletester.page.GoogleSearchPage;
import com.versatiletester.util.driver.DriverFactory;
import com.versatiletester.util.driver.DriverUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TestBase {
    private static final Logger log = Logger.getLogger(TestBase.class);

    //UTILS FOR DRIVER, TEST, SPRING
    @Autowired
    protected SpringContext springContext;
    @Autowired
    protected ScenarioContext scenarioContext;
    @Autowired
    protected DriverUtils driverUtils;
    @Autowired
    protected DriverFactory driverFactory;
    @Autowired
    protected RemoteWebDriver driver;

    //Pages
    @Autowired
    protected GoogleSearchPage googleSearchPage;
    @Autowired
    protected GoogleResultsPage googleResultsPage;
}
