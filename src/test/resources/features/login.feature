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
    And I am on the Rewards page
    And I click on Explore Rewards
    And I click on claimed rewards button
    Then I should see the reward activated text

    Examples:
      | username       |
      | SELENIUM_OCTO1 |

  @positive @offersCard
  Scenario Outline: Check Available Rewards
    When I login with username "<username>"
    And I click on octoplus button
    And I am on the Rewards page
    And I click on Explore Rewards
    And I click on offers card to reveal offer for if exists "<offerText>"

    Examples:
      | username       | offerText                         |
      | SELENIUM_OCTO1 | 2 Super Saver seats for £8 at Vue |