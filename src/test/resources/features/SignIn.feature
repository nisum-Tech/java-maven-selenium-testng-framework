Feature: User Sign In

  Scenario: Successful Sign In
    Given user is on the login page
    When user enters valid username and password
    And clicks on the login button
    Then user is redirected to the home page
