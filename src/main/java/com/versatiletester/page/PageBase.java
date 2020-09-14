package com.versatiletester.page;

import com.versatiletester.config.SpringContext;
import com.versatiletester.util.driver.DriverFactory;
import com.versatiletester.util.driver.DriverUtils;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/** Any beans used inside an abstract constructor must be passed from the sub-classes, not autowired.
 *  This is why the contructor is a placeholder - The @PostConstruct gets called once post-instantiation, so can
 *  act as a one-time Constructor, however as it is managed by Spring you CANNOT instantiate pages using the usual
 *  " = new Page();" means, otherwise no objects will be autowired/accessible.
 *
 *  This also means that you cannot implement method chaining to simplify test execution.
 */
@Component
public abstract class PageBase{
    protected RemoteWebDriver driver;

    @Autowired
    protected DriverUtils driverUtils;
    @Autowired
    protected SpringContext springContext;
    @Autowired
    protected DriverFactory driverFactory;

    public PageBase(){}

    @PostConstruct
    private void pageSetupPostConstructor(){
        this.driver = driverFactory.getInstance();
        PageFactory.initElements(driver, this);
    }

    @SuppressWarnings("unused")
    public abstract boolean isVisible();
}
