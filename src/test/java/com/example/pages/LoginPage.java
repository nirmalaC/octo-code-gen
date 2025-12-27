package com.example.pages;

import com.example.support.ConfigReader;
import com.example.utils.CredentialManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final String baseUrl = ConfigReader.get("baseUrl");
    private final By username = By.id("id_auth-username");
    private final By password = By.id("id_auth-password");
    private final By submitBtn = By.id("submit-button");
    private final By logoutBtn     = By.xpath("//*[@id='submit']");
    private final By octopluBtn = By.xpath("//span[normalize-space()='Octoplus']");


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

    public void clickSubmitButton() {
        click(submitBtn);
    }

    public void logout() {
        click(logoutBtn);
    }

    public void login(String user) {
        String username = CredentialManager.getUsername(user);
        String password = CredentialManager.getPassword(user);

        enterUsername(username);
        enterPassword(password);
        clickSubmitButton();
    }

    public void clickOctoPlusButton() {
        click(octopluBtn);
    }


}
