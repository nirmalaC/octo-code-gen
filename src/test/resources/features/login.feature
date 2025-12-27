@smoke @login
Feature: code generation in Octopus Energy Website

  Background:
    Given I am on the DemoQA login page

  @positive @LoginSuccess
  Scenario Outline: Successful login with configured credentials
    When I login with username "<username>"
    And I log out
    Then I should be back on the login page

    Examples:
      | username       |
      | SELENIUM_OCTO1 |


  @positive @offers
  Scenario Outline: Check Offers
    When I login with username "<username>"
    And I click on octoplus button

    Examples:
      | username       |
      | SELENIUM_OCTO1 |