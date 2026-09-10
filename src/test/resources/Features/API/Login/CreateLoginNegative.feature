@Login @API @Negative

Feature: Login Negative Scenario with Login API
  As a Amex Vat user I would like to create login with invalid credentials so that I can validate the negative scenario.

  @CreateLoginNegative_001
  Scenario: Verify login is rejected when user passes wrong username in Login API
    Given i have "CreateVatLoginEndpoint" api request with template "CreateLogin" and following details
      | username | abc      |
      | password | Test@123 |
    When i POST "CreateVatLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator         | value                                       |
      | code            | -1                                          |
      | message         | Invalid credentials or user does not exist. |
      | token           | null                                        |
      | tokenExpireTime | null                                        |

  @CreateLoginNegative_002
  Scenario: Verify login is rejected when user passes blank username in Login API
    Given i have "CreateVatLoginEndpoint" api request with template "CreateLogin" and following details
      | username | Blank    |
      | password | Test@123 |
    When i POST "CreateVatLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator         | value                                       |
      | code            | -1                                          |
      | message         | Invalid credentials or user does not exist. |
      | token           | null                                        |
      | tokenExpireTime | null                                        |

  @CreateLoginNegative_003
  Scenario: Verify login is rejected when user passes null username in Login API
    Given i have "CreateVatLoginEndpoint" api request with template "CreateLogin" and following details
      | username | null     |
      | password | Test@123 |
    When i POST "CreateVatLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator         | value                                       |
      | code            | -1                                          |
      | message         | Invalid credentials or user does not exist. |
      | token           | null                                        |
      | tokenExpireTime | null                                        |

  @CreateLoginNegative_004
  Scenario: Verify login is rejected when user passes wrong password in Login API
    Given i have "CreateVatLoginEndpoint" api request with template "CreateLogin" and following details
      | username | testuser |
      | password | abc      |
    When i POST "CreateVatLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator         | value                                       |
      | code            | -1                                          |
      | message         | Invalid credentials or user does not exist. |
      | token           | null                                        |
      | tokenExpireTime | null                                        |

  @CreateLoginNegative_002
  Scenario: Verify login is rejected when user passes blank password in Login API
    Given i have "CreateVatLoginEndpoint" api request with template "CreateLogin" and following details
      | username | testuser |
      | password | Blank    |
    When i POST "CreateVatLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator         | value                                       |
      | code            | -1                                          |
      | message         | Invalid credentials or user does not exist. |
      | token           | null                                        |
      | tokenExpireTime | null                                        |

  @CreateLoginNegative_003
  Scenario: Verify login is rejected when user passes null password in Login API`
    Given i have "CreateVatLoginEndpoint" api request with template "CreateLogin" and following details
      | username | null     |
      | password | Test@123 |
    When i POST "CreateVatLoginEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator         | value                                       |
      | code            | -1                                          |
      | message         | Invalid credentials or user does not exist. |
      | token           | null                                        |
      | tokenExpireTime | null                                        |