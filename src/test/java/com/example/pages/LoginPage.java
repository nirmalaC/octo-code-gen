package com.example.pages;

import com.example.support.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final String baseUrl = ConfigReader.get("baseUrl");
    private final By username = By.id("userName");
    private final By password = By.id("password");
    private final By flash = By.id("flash");
    private final By loginBtn = By.xpath("//*[@id='login']");
    private final By errorMsg = By.id("name");
    private final By newUser  = By.id("newUser");
    private final By logoutBtn     = By.xpath("//*[@id='submit']");
    private final By usernameValue = By.id("userName-value");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(baseUrl + "/login");
        return this;
    }

    public boolean isAt() {
        return driver.getCurrentUrl().contains("/login");
    }

    public void enterUsername(String user) {
        type(username, user);
    }

    public void enterPassword(String pass) {
        type(password, pass);
    }

    public void clickLogin() {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(loginBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void clickNewUser() {
        click(newUser);
    }

    public String displayedUsername() {
        return textOf(usernameValue);
    }

    public void logout() {
        click(logoutBtn);
    }


    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickLogin();
    }
    public String error() {
        return textOf(errorMsg);
    }

}
