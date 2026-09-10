@Login @API @Positive

Feature: Create Login Positive Scenario with Login API

  @CreateLogin @Sanity
  Scenario: User Passes valid Username and Password in the Login API
    Given i have "CreateVatLoginEndpoint" api request with template "CreateLogin" and following details
      | username | testuser |
      | password | Test@123 |
    When i POST "CreateVatLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator         | value              |
      | code            | 0                  |
      | token           | #should be present |
      | message         | Valid user         |
      | tokenExpireTime | #should be present |