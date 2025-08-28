package com.example.steps;

import com.example.pages.LoginPage;
import com.example.support.DriverFactory;
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

    @When("I login with username {string} and password {string}")
    public void i_loginPage_with_username_and_password(String user, String pass) {
        loginPage.login(user, pass);
    }

    @Then("I should land on the profile page")
    public void i_should_land_on_profile_page() {
        assertTrue("Not on profile page", loginPage.isAt());
    }

    @Then("I should see my username on profile")
    public void i_should_see_my_username_on_profile() {
        assertEquals("Username mismatch", "Config.username()", loginPage.displayedUsername());
    }

    @Then("I should see login error {string}")
    public void i_should_see_loginPage_error(String expected) {
        assertEquals(expected, loginPage.error());
    }

    @When("I log out")
    public void i_log_out() {
        loginPage.logout();
    }

    @Then("I should be back on the login page")
    public void i_should_be_back_on_loginPage_page() {
        assertTrue(loginPage.isAt());
    }

}
