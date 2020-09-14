package com.versatiletester.config;

import com.versatiletester.util.driver.DriverCapabilityManager;
import com.versatiletester.util.driver.DriverFactory;
import com.versatiletester.util.driver.DriverUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

/** '@PostConstruct' and '@PreDestroy' run in the setup and teardown of each thread which is reused by tests,
 * so cannot be used as 'BeforeSuite'/'AfterSuite' functionality. */
@ComponentScan("com/versatiletester")
@PropertySource("classpath:config/application.properties")
public class SpringContext {
    private static final Logger log = Logger.getLogger(SpringContext.class);

    public static final String PROJECT_NAME_PROPERTY_NAME = "project.name";
    public static final String MAVEN_PROFILE_PROPERTY_NAME = "maven.profile";
    public static final String LOCAL_ID_PROPERTY_NAME = "local.id";
    public static final String BUILD_NUM_PROPERTY_NAME = "build";
    public static final String BROWSER_PROPERTY_NAME = "browser";
    public static final String ENVIRONMENT_PROPERTY_NAME = "environment";
    public static final String GRID_URL_PROPERTY_NAME = "grid.url";
    public static final String BSTACK_URL_PROPERTY_NAME = "browserstack.url";
    public static final String BSTACK_LOCAL_BOOL_PROPERTY_NAME = "browserstack.local";
    public static final String RESOURCES_DIR_PROPERTY_NAME = "resources.dir";

    public static final String GOOGLE_URL_PROPERTY_NAME = "google.base.url";

    @Autowired
    public Environment env;

    public String getProperty(String propertyName) {
        if(env.getProperty(propertyName) == null){
            log.error("Could not find property with reference: '" + propertyName + "', returning empty string");
            return "";
        } else {
            return env.getProperty(propertyName);
        }
    }

    @Bean
    public DriverCapabilityManager getDriverCapabilityManager(){
        return new DriverCapabilityManager();
    }

    @Bean
    public DriverFactory getDriverFactory(DriverCapabilityManager driverCapabilityManager){
        return new DriverFactory(driverCapabilityManager);
    }

    @Bean
    public DriverUtils getDriverUtils(DriverFactory driverFactory){
        return new DriverUtils(driverFactory);
    }

    @Bean(destroyMethod="")
    @Scope("cucumber-glue") // cucumber-glue scope defined to create one instance of Driver per Scenario.
    public RemoteWebDriver getDriver(DriverFactory driverFactory) {
        return driverFactory.getInstance();
    }
}
