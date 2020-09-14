package com.versatiletester.page;

import com.versatiletester.util.driver.DriverFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.versatiletester.config.SpringContext.GOOGLE_URL_PROPERTY_NAME;

@Component
@Scope("cucumber-glue")
@SuppressWarnings("unused")
public class GoogleResultsPage extends PageBase {
    private static final Logger log = Logger.getLogger(GoogleResultsPage.class);

    @FindAll({ @FindBy(xpath = "//h3/..") })
    private List<WebElement> searchResultList;

    /**
     * Index starts at 1 for the first search result link.
     * @param linkPosition
     */
    public void clickSearchResult(int linkPosition){
        driverUtils.clickPageTransitionElement(searchResultList.get(linkPosition - 1));
    }

    public WebElement getFirstResultContainingText(String text){
        for(WebElement element : searchResultList){
            if(element.getAttribute("innerText").contains(text)){
                return element;
            }
        }
        return null;
    }

    @Override
    public boolean isVisible() {
        if( driver.getCurrentUrl().contains(springContext.getProperty(GOOGLE_URL_PROPERTY_NAME)) &&
            !searchResultList.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
