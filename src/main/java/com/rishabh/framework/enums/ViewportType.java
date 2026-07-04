package com.rishabh.framework.enums;

/**
 * Rough breakpoints, not pulled from any official device list - just common
 * resolutions we care about for the responsive smoke checks.
 */
public enum ViewportType {

    DESKTOP(1920, 1080),
    LAPTOP(1366, 768),
    TABLET(768, 1024),
    MOBILE(375, 667);

    private final int width;
    private final int height;

    ViewportType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
