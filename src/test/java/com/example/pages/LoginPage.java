package com.example.pages;

import com.example.support.ConfigReader;
import com.example.utils.CredentialManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
// Inheritance example as it extends basePage
// Benefits: Reuses common methods (click(), type(), getText()) and inherits driver, wait, and timeout.
public class LoginPage extends BasePage {

    // Encapsulation : Private fields and controlled access only accessible internally within the class
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private final String baseUrl = ConfigReader.get("baseUrl");
    private final By username = By.id("id_auth-username");
    private final By password = By.id("id_auth-password");
    private final By submitBtn = By.id("submit-button");
    private final By logoutBtn = By.xpath("//*[@id='submit']");
    private final By octopluBtn = By.xpath("//span[normalize-space()='Octoplus']");
    private final By exploreRewards = By.xpath("//a[contains(@href,'/octoplus/partner/offers') and .//span[normalize-space(text())='Explore rewards']]");
    private final By rewardsText = By.xpath("//div[@id='barcode-wrapper']//h2[text()='Reward activated!']");
    private final By activateOffers = By.xpath("//button[.//span[normalize-space(text())='Activate offer']]");
    private final String claimedRewardsTemplate = "//div[@data-testid='offer-card'][.//h3[contains(normalize-space(), '%s')]]//a[.//span[normalize-space()='Claimed reward']]";
    private final String RevealOfferTemplate = "//div[@data-testid='offer-card'][.//h3[contains(normalize-space(), '%s')]]//a[.//span[normalize-space()='Reveal offer']]";


    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        String url = baseUrl + "/login";
        logger.info("Opening login page: {}", url);
        driver.get(url);
        logger.info("Login page opened successfully");
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
        logger.info("Attempting to login with user: {}", user);
        String username = CredentialManager.getUsername(user);
        String password = CredentialManager.getPassword(user);

        enterUsername(username);
        enterPassword(password);
        clickSubmitButton();
        logger.info("Login completed for user: {}", user);
    }

    public void clickOctoPlusButton() {
        click(octopluBtn);
    }


    public boolean rewardsPageIsDisplayed() {
        return driver.getCurrentUrl().contains("/octoplus");
    }

    public void clickOnExploreRewards(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(exploreRewards));
        element.click();
    }

    public void assertPageText(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(rewardsText)
        );

        String element = heading.getText();
        Assert.assertEquals(element, "Reward activated!");
    }

    public void clickOnOfferCardToRevealCode(String offerText){
        logger.info("Attempting to click on offer card for: {}", offerText);
        // First, find the offer card link to check its status
        By offerCard = By.xpath("//div[@data-testid='offer-card'][.//h3[contains(normalize-space(text()), '" + offerText + "')]]//a[.//span]");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(offerCard));
        
        // Get the text from the span inside the link - find span with either "Claimed reward" or "Reveal offer"
        WebElement spanElement = element.findElement(By.xpath(".//span[normalize-space(text())='Claimed reward' or normalize-space(text())='Reveal offer']"));
        String spanText = spanElement.getText().trim();
        logger.debug("Found offer card status: {}", spanText);

        if ("Claimed reward".equals(spanText)){
            logger.info("Offer is already claimed, clicking on claimed reward link");
            String xpath = String.format(claimedRewardsTemplate, offerText);
            By claimedReward = By.xpath(xpath);
            click(claimedReward);
            logger.info("Clicked on Claimed Rewards Successfully");
        } else if ("Reveal offer".equals(spanText)){
            logger.info("Offer needs to be revealed, clicking on reveal offer");
            String xpath1 = String.format(RevealOfferTemplate, offerText);
            By revealOffers = By.xpath(xpath1);
            click(revealOffers);
            logger.info("Clicking on activate offer button");
            click(activateOffers);
            assertPageText();
            logger.info("Clicked on Reveal Offers Successfully");
        }
    }
}


