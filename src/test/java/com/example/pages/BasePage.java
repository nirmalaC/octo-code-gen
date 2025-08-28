package com.example.pages;

import com.example.support.FrameworkException;
import com.example.support.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

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
            waitForClickable(locator).click();
        } catch (TimeoutException | NoSuchElementException | ElementClickInterceptedException e) {
            throw new FrameworkException("Failed to click on element: " + locator, e);
        }
    }

    @Step("Typing into element: {locator} value: {text}")
    protected void type(By locator, String text) {
        try {
            WebElement el = waitForVisible(locator);
            el.clear();
            el.sendKeys(text);
        } catch (TimeoutException | NoSuchElementException e) {
            throw new FrameworkException("Failed to type into element: " + locator, e);
        }
    }

    @Step("Get text from element: {locator}")
    protected String getText(By locator) {
        try {
            return waitForVisible(locator).getText();
        } catch (TimeoutException | NoSuchElementException e) {
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
}
