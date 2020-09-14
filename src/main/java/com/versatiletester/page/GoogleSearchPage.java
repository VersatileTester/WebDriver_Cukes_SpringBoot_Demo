package com.versatiletester.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class GoogleSearchPage extends PageBase {
    private static final Logger log = Logger.getLogger(GoogleResultsPage.class);

    @FindBy(xpath = "//input[@title='Search']")
    private WebElement searchBox;
    @FindBy(xpath = "//span[@aria-label='Clear']")
    private WebElement clearIcon;

    @FindBy(xpath = "//input[@value='Google Search']")
    private WebElement googleSearchButton;
    @FindBy(xpath = "//input[contains(@value,'m Feeling Lucky')]")
    private WebElement feelingLuckyButton;

    /* The only IFrame on the page is the Cookie IFrame, which has no other useful identifiable attributes */
    @FindBy(xpath = "//iframe")
    private WebElement cookieIframe;
    @FindBy(xpath = "//span[normalize-space(text()) ='I agree']")
    private WebElement cookieConfirmationButton;

    public void googleSearchForTerm(String searchTerm){
        bypassPrompts();
        searchBox.sendKeys(searchTerm);
        driverUtils.clickPageTransitionElement(googleSearchButton);
    }

    /**
     * Technically the resulting page from a 'Lucky Search' is not the GoogleResults page, however as the actual page
     * hit is dynamic and depends upon the search criteria at any given time, the GoogleResultsPage is returned in order
     * to assert against it's visibility.
     *
     * @param searchTerm
     * @return
     */
    public void luckySearchForTerm(String searchTerm){
        bypassPrompts();
        searchBox.sendKeys(searchTerm);
        driverUtils.clickPageTransitionElement(feelingLuckyButton);
    }

    private void bypassPrompts(){
        if(driverUtils.elementExists(cookieIframe)){
            driver.switchTo().frame(cookieIframe);
            cookieConfirmationButton.click();
            driver.switchTo().defaultContent();
        }
    }

    @Override
    public boolean isVisible() {
        if( googleSearchButton.isDisplayed() && googleSearchButton.isEnabled()){
            return true;
        } else {
            return false;
        }
    }
}
