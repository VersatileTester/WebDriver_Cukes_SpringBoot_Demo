package com.versatiletester.config;

public enum MavenProfiles {
    LOCAL("local"),
    BROWSERSTACK("bstack"),
    GRID("grid");

    private String description;

    MavenProfiles(String browser) {
        this.description = browser;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public static MavenProfiles getMatch(String text) {
        for (MavenProfiles profile : MavenProfiles.values()) {
            if (profile.toString().equalsIgnoreCase(text)) {
                return profile;
            }
        }
        throw new RuntimeException("Maven Profile '" + text + "' unsupported.");
    }
}
