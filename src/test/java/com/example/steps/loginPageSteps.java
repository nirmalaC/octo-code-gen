package com.example.steps;

import com.example.pages.LoginPage;
import com.example.support.DriverFactory;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class loginPageSteps {

    private static final Logger logger = LoggerFactory.getLogger(loginPageSteps.class);
    private LoginPage loginPage;

    @Given("I am on the DemoQA login page")
    public void i_am_on_loginPage_page() {
        logger.info("Navigating to DemoQA login page");
        loginPage = new LoginPage(DriverFactory.getDriver()).open();
        assertTrue("Not on loginPage page", loginPage.isAt());
        logger.info("Successfully navigated to login page");
    }

    @When("I login with username {string}")
    public void i_loginPage_with_username_and_password(String user) {
        logger.info("Executing login step with username: {}", user);
        loginPage.login(user);
        logger.info("Login step completed for username: {}", user);
    }

    @Then("I should land on the profile page")
    public void i_should_land_on_profile_page() {
        assertTrue("Not on profile page", loginPage.isAt());
    }


    @When("I log out")
    public void i_log_out() {
        loginPage.logout();
    }


    @And("I click on octoplus button")
    public void iClickOnOctoplusButton() {
        loginPage.clickOctoPlusButton();
    }

    @And("I am on the Rewards page")
    public void iAmOnTheRewardsPage() {
        assertTrue("Not on octopus rewards page", loginPage.rewardsPageIsDisplayed());
    }

    @And("I click on Explore Rewards")
    public void
    iClickOnRewardsCard() {
       loginPage.clickOnExploreRewards();
    }

    @And("I click on claimed rewards button")
    public void iClickOnClaimedRewardsButton() {
       loginPage.clickOnRewardsCard();
    }

    @Then("I should see the reward activated text")
    public void iShouldSeeTheRewardActivatedText() {
        loginPage.assertPageText();
    }

    @And("I click on offers card to reveal offer for if exists {string}")
    public void iClickOnOffersCardToRevealOffer(String offerText) {
        logger.info("Executing step: Click on offers card to reveal offer for: {}", offerText);
        loginPage.clickOnOfferCardToRevealCode(offerText);
        logger.info("Step completed: Click on offers card for: {}", offerText);
    }
}
