package com.versatiletester.config;

public enum Environments {
    SANDPIT("sandpit"),
    TEST("test"),
    PRE_PROD("preprod");

    private final String propertyValue;

    Environments(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return this.propertyValue;
    }

    public static Environments getMatch(String text) {
        for (Environments platform : Environments.values()) {
            if (platform.toString().equalsIgnoreCase(text.toLowerCase())) {
                return platform;
            }
        }
        throw new RuntimeException("Target Environment '" + text + "' unsupported.");
    }
}