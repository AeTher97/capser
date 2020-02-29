package org.rdc.capser.config;

public class Config {

    public static double getInactivityTime() {
        return 432000000;
    }

    public static int getPlacementMatchesNumber() {
        return 5;
    }

    public static float getBuildNumber() {
        return Float.parseFloat("2.0");
    }

    public static boolean cloud() {
        return true;
    }
}
