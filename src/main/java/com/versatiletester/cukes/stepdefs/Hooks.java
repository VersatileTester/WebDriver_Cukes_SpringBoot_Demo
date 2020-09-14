package com.versatiletester.cukes.stepdefs;

import com.versatiletester.cukes.TestBase;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks extends TestBase {
    private static final Logger log = Logger.getLogger(Hooks.class);

    @Before
    public void testSetup(Scenario scenario){
        scenarioContext.setScenario(scenario);
    }

    @After
    public void teardown(Scenario scenario){
        log.info("Scenario '" + scenario.getName() + "' ending now.");

        if(scenario.isFailed()){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            log.error("Scenario failed: " + scenario.getName() + " at " + dateFormat.format(date));
            log.error("Failing URL: " + driver.getCurrentUrl());
            log.error("Failing Title: " + driver.getTitle());

            scenario.write("Scenario failed: " + scenario.getName() + " at " + dateFormat.format(date));
            scenario.write("Failing URL: " + driver.getCurrentUrl());
            scenario.write("Failing Title: " + driver.getTitle());

            try{
                scenario.embed(driverUtils.getScreenshotBytes(),"image/png");
                scenario.embed(driver.getPageSource().getBytes(Charsets.UTF_8),"text/html");
            } catch (Exception e) {
                log.error("Failed to embed screenshot, attempting to save externally");
                driverUtils.takeScreenshot("failed_state");
            }
        } else {
            log.info("Scenario passed: " + scenario.getName());
        }
        // No need to call Quit()/Close() - Spring handles this for us by using the 'cucumber-glue' scope
        driver.quit();
    }
}
