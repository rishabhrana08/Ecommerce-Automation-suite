package com.rishabh.framework.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * UI tests on a public site occasionally fail for reasons that have nothing to do
 * with our code - a slow ad script, a one-off network blip. Retry twice before
 * we actually call it a failure.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRIES = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            return true;
        }
        return false;
    }
}
