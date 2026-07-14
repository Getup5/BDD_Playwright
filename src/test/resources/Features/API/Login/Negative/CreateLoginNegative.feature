@Login @API @Negative

Feature: Create Login Negative Scenario with Login API

  @CreateLoginNegative_001 @Negative
  Scenario: User Login by passing Invalid value in Username Field
    Given i have "LoginEndpoint" api request with template "CreateLogin" and following details
      | username | abc       |
      | password | Test@1234 |
    When i POST "CreateLoginEndpoint" api request
    Then the http status code should be "401"
    And the following response details should be present
      | locator | value                        |
      | success | false                        |
      | message | Invalid username or password |
