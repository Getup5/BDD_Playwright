@UI
Feature: Role-based login flow

  Scenario: User opens UI and performs login flow
    Given user opens ui application url
    When Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance
    And Verify the VAT user is able to fill all the required fields in the create new case form
    Then Verify the VAT user is able to view the confirmation message after submitting the create new case