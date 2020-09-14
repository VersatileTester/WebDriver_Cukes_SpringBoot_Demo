package com.versatiletester.cukes.stepdefs;

import com.versatiletester.cukes.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.Logger;

import static com.versatiletester.config.ScenarioContext.DEFAULT_SEARCH_TERM;
import static com.versatiletester.config.SpringContext.GOOGLE_URL_PROPERTY_NAME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GoogleStepDefs extends TestBase {
    private static final Logger log = Logger.getLogger(GoogleStepDefs.class);

    @Given("^I navigate to the Google home page$")
    public void i_navigate_to_the_Google_home_page(){
        driver.get(springContext.getProperty(GOOGLE_URL_PROPERTY_NAME));
    }

    @When("^I google search the term (.*)$")
    public void i_google_search_the_term_X(String searchTerm) {
        googleSearchPage.googleSearchForTerm(searchTerm);
    }

    @Then("^I should see at least one result containing the term (.*)$")
    public void i_should_see_at_least_one_result_containing_the_term_X(String searchTerm) {
        assertNotNull("Could not find any results matching the search term " + searchTerm,
                googleResultsPage.getFirstResultContainingText(searchTerm));
    }

    @When("^When I lucky search any valid word$")
    public void when_I_lucky_search_any_valid_word() {
        googleSearchPage.luckySearchForTerm(DEFAULT_SEARCH_TERM);
    }

    @Then("^I should not be taken to the google results page$")
    public void i_should_not_be_taken_to_the_google_results_page() {
        assertFalse(googleResultsPage.isVisible());
    }
}