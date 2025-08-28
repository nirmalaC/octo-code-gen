@smoke @login
Feature: Login to DemoQA Book Store

  Background:
    Given I am on the DemoQA login page

  @positive @LoginSuccess
  Scenario Outline: Successful login with configured credentials
    When I login with username "<username>" and password "<password>"
    And I log out
    Then I should be back on the login page

    Examples:
      | username | password  |
      | Nimu0123 | Nimu0123! |


  @negative @LoginFailure
  Scenario Outline: Invalid login shows an error
    When I login with username "<username>" and password "<password>"
    Then I should see login error "Invalid username or password!"

    Examples:
      | username  | password     |
      | wrongUser | wrongPass    |
      | wrongUser | Password123! |