@smoke @login
Feature: code generation in Octopus Energy Website

  Background:
    Given I am on the DemoQA login page

  @positive @offersCard
  Scenario Outline: Check Available Rewards
    When I login with username "<username>"
    And I click on octoplus button
    And I am on the Rewards page
    And I click on Explore Rewards
    And I click on offers card to reveal offer for if exists "<offerText>"

    Examples:
      | username       | offerText                                        |
      | SELENIUM_OCTO1 | A hot or cold drink on us - any size, every week |