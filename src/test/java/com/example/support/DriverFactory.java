package com.example.support;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static void initDriver(String browser, boolean headless) {
        logger.info("Initializing WebDriver - Browser: {}, Headless: {}", browser, headless);
        WebDriver driver;
        // Interface-based: WebDriver interface with multiple implementations
        switch (browser.toLowerCase()) {
            case "firefox":
                logger.debug("Setting up Firefox driver");
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ff = new FirefoxOptions();
                if (headless) ff.addArguments("-headless");
                driver = new FirefoxDriver(ff);
                logger.info("Firefox driver initialized successfully");
                break;
            case "edge":
                logger.debug("Setting up Edge driver");
                WebDriverManager.edgedriver().setup();
                EdgeOptions edge = new EdgeOptions();
                if (headless) edge.addArguments("--headless=new");
                driver = new EdgeDriver(edge);
                logger.info("Edge driver initialized successfully");
                break;
            case "chrome":
            default:
                logger.debug("Setting up Chrome driver");
                WebDriverManager.chromedriver().setup();
                ChromeOptions co = new ChromeOptions();
                if (headless) co.addArguments("--headless=new");
                co.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(co);
                logger.info("Chrome driver initialized successfully");
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().window().maximize();
        tlDriver.set(driver);
        logger.info("WebDriver setup completed and stored in ThreadLocal");
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            logger.info("Quitting WebDriver");
            driver.quit();
            tlDriver.remove();
            logger.info("WebDriver quit successfully and removed from ThreadLocal");
        } else {
            logger.warn("Attempted to quit driver but no driver instance found");
        }
    }
}
