package com.versatiletester.cukes.runner;

import com.google.common.base.Preconditions;
import com.versatiletester.config.SpringContext;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json:target/reports/json/cucumber.json"},
        features = {"src/main/resources/features"},
        glue = {"com/versatiletester"},
        tags = {"@All"})
public class RunCukesTest {
    private static final Logger log = Logger.getLogger(RunCukesTest.class);

    /**
     * This class runs before all tests, and will halt test execution (including all parallel threads/forks) until
     * it is complete. Use for any initial environment set up required before test suite run.
     */
    @BeforeClass
    public static void setupSuite(){
        Preconditions.checkArgument(System.getProperty(SpringContext.ENVIRONMENT_PROPERTY_NAME) != null);
    }
}
