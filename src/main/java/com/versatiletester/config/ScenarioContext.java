package com.versatiletester.config;

import com.versatiletester.cukes.TestBase;
import io.cucumber.core.api.Scenario;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * A config class used to share variables/dynamic states across the cucumber classes.
 */
@Component
@Scope("cucumber-glue")
public class ScenarioContext {
    private static final Logger log = Logger.getLogger(ScenarioContext.class);

    public static final String DEFAULT_SEARCH_TERM = "default";

    private HashMap<String,String> sharedScope;
    private String debugString;
    private Scenario scenario;

    public String getValue(String key){
        return this.sharedScope.get(key);
    }

    public void addValue(String key, String value){
        this.sharedScope.put(key,value);
        this.debugString += "; " + key + ":" + value;
    }

    public Scenario getScenario() { return scenario; }

    public void setScenario(Scenario scenario) { this.scenario = scenario; }

    public ScenarioContext(){
        sharedScope = new HashMap<>();
        debugString = "";
    }
}
