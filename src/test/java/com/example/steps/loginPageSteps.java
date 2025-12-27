package com.example.steps;

import com.example.pages.LoginPage;
import com.example.support.DriverFactory;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class loginPageSteps {

    private LoginPage loginPage;

    @Given("I am on the DemoQA login page")
    public void i_am_on_loginPage_page() {
        loginPage = new LoginPage(DriverFactory.getDriver()).open();
        assertTrue("Not on loginPage page", loginPage.isAt());
    }

    @When("I login with username {string}")
    public void i_loginPage_with_username_and_password(String user) {
        loginPage.login(user);
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
}
