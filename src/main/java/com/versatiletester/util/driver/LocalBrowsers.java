package com.versatiletester.util.driver;

import com.versatiletester.config.MavenProfiles;

public enum LocalBrowsers {
    FIREFOX,
    CHROME;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static LocalBrowsers getMatchingBrowser(String browser){
        try{
            return LocalBrowsers.valueOf(browser.toUpperCase());
        } catch(IllegalArgumentException e){
            throw new RuntimeException("Local Browser '" + browser + "' unsupported.");
        }
    }
}
