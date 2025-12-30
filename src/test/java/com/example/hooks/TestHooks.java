package com.example.hooks;

import com.example.support.DriverFactory;
import com.example.support.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class TestHooks {

    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);

    @Before
    public void setUp(Scenario scenario) {
        logger.info("Setting up test scenario: {}", scenario.getName());
        String browser = ConfigReader.get("browser");
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));
        logger.debug("Test configuration - Browser: {}, Headless: {}", browser, headless);
        DriverFactory.initDriver(browser, headless);
        logger.info("Test setup completed for scenario: {}", scenario.getName());
    }


    @After
    public void tearDown(Scenario scenario) {
        logger.info("Tearing down test scenario: {}", scenario.getName());
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            if (scenario.isFailed()) {
                logger.error("Scenario failed: {}. Capturing screenshot", scenario.getName());
                // Captures a screenshot of the current browser state and stores it as a byte array
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
                logger.info("Screenshot captured and attached to Allure report");
            } else {
                logger.info("Scenario passed: {}", scenario.getName());
            }
            DriverFactory.quitDriver();
        }
        logger.info("Test teardown completed for scenario: {}", scenario.getName());
    }
}
