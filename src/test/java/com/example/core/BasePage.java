package com.example.core;

import com.example.config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

// BasePage is abstract and defines common page behavior
//Purpose: Hides implementation details and provides a common interface for page objects.
public abstract class BasePage {

    // Encapsulation : Private fields and controlled access only subclasses can access them
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final Duration timeout;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.timeout = Duration.ofSeconds(Long.parseLong(ConfigReader.get("timeout")));
        this.wait = new WebDriverWait(driver, timeout);
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Clicking on element: {locator}")
    protected void click(By locator) {
        try {
            logger.debug("Attempting to click on element: {}", locator);
            waitForClickable(locator).click();
            logger.info("Successfully clicked on element: {}", locator);
        } catch (TimeoutException | NoSuchElementException | ElementClickInterceptedException e) {
            logger.error("Failed to click on element: {}", locator, e);
            throw new FrameworkException("Failed to click on element: " + locator, e);
        }
    }

    @Step("Typing into element: {locator} value: {text}")
    protected void type(By locator, String text) {
        try {
            logger.debug("Typing text into element: {} with value: {}", locator, text);
            WebElement el = waitForVisible(locator);
            el.clear();
            el.sendKeys(text);
            logger.info("Successfully typed text into element: {}", locator);
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("Failed to type into element: {}", locator, e);
            throw new FrameworkException("Failed to type into element: " + locator, e);
        }
    }

    @Step("Get text from element: {locator}")
    protected String getText(By locator) {
        try {
            logger.debug("Getting text from element: {}", locator);
            String text = waitForVisible(locator).getText();
            logger.debug("Retrieved text from element {}: {}", locator, text);
            return text;
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("Failed to get text from element: {}", locator, e);
            throw new FrameworkException("Failed to get text from element: " + locator, e);
        }
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected String textOf(By locator) {
        return visible(locator).getText();
    }

    protected boolean urlContains(String part) {
        return wait.until(ExpectedConditions.urlContains(part));
    }

    protected void scrollIntoView(By locator) {
        WebElement element = waitForVisible(locator);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
}
