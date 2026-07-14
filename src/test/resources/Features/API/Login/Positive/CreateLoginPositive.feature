@Login @API @Positive

Feature: Create Login Positive Scenario with Login API

  @CreateLogin @Sanity
  Scenario: Create Login
    Given i have "CreateLoginEndpoint" api request with template "CreateLogin" and following details
      | username | Test |
      | password | Test@1234  |
    When i POST "CreateLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator           | value              |
      | success           | true               |
      | data.accessToken  | #should be present |
      | data.refreshToken | #should be present |
      | data.refreshToken | #should be present |