Feature: Role-based login flow

  Scenario: User opens UI and performs login flow
    Given user opens ui application url
    When user enters "admin" into role "textbox" named "Username"
    And user clicks role "button" named "Login"
