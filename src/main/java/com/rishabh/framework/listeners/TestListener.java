package com.rishabh.framework.listeners;

import com.rishabh.framework.factory.DriverFactory;
import com.rishabh.framework.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        InMemoryLogAppender.clear();
        log.info(">>> Starting: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("<<< PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("<<< FAILED: {} - {}", result.getMethod().getMethodName(), result.getThrowable());

        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String path = ScreenshotUtil.capture(driver, result.getMethod().getMethodName());
            log.info("Screenshot saved to {}", path);

            // attach straight into the Allure report too, not just disk
            byte[] screenshot = ScreenshotUtil.captureAsBytes(driver);
            Allure.addAttachment(result.getMethod().getMethodName() + "-failure",
                    new ByteArrayInputStream(screenshot));
        }

        // whatever this test logged (page navigations, waits, retries) shows up right
        // next to the screenshot in the report - no more digging through execution.log
        Allure.addAttachment(result.getMethod().getMethodName() + "-execution-log", "text/plain",
                InMemoryLogAppender.getLogsForCurrentTest(), ".log");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("<<< SKIPPED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Suite finished: {} passed, {} failed, {} skipped",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }
}
