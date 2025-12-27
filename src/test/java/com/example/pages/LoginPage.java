package com.example.pages;

import com.example.support.ConfigReader;
import com.example.utils.CredentialManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    private final String baseUrl = ConfigReader.get("baseUrl");
    private final By username = By.id("id_auth-username");
    private final By password = By.id("id_auth-password");
    private final By submitBtn = By.id("submit-button");
    private final By logoutBtn = By.xpath("//*[@id='submit']");
    private final By octopluBtn = By.xpath("//span[normalize-space()='Octoplus']");
    private final By rewardsCard = By.xpath("//div[@data-testid='offer-card']//a[.//span[normalize-space()='Claimed reward']]");
    private final By exploreRewards = By.xpath("//a/span[text()='Explore rewards']");
    private final By rewardsText = By.xpath("//div[@id='barcode-wrapper']//h2[text()='Reward activated!']");



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


    public boolean rewardsPageIsDisplayed() {
        return driver.getCurrentUrl().contains("/octoplus");
    }

    public void clickOnRewardsCard(){
        click(rewardsCard);
    }

    public void clickOnExploreRewards(){
        click(exploreRewards);
    }

    public void assertPageText(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(rewardsText)
        );

        String element = heading.getText();
        Assert.assertEquals(element, "Reward activated!");
    }
}

