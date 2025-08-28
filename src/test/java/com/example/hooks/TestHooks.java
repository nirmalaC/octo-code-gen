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

import java.io.ByteArrayInputStream;

public class TestHooks {

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        String browser = ConfigReader.get("browser");
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));
        DriverFactory.initDriver(browser, headless);
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(screenshot));
            }
            DriverFactory.quitDriver();
        }
    }
}
