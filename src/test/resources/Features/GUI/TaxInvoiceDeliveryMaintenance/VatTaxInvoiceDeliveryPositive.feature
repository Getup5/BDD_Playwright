@UI @VAT @VAT_TaxInvoiceDeliveryMaintenance

Feature: Verify that the VAT user can successfully create New Case in TAX Invoice Delivery Maintenance in the BTA portal,and validate their functionality.

  @VAT_TaxInvoiceDeliveryMaintenance_001 @Sanity
  Scenario: Verify that VAT user is able to create New Case in TAX Invoice Delivery Maintenance
    Given Verify the VAT user is able to access the URL
    When Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance
    And  Verify the VAT user is able to fill all the required fields in the create new case form
#    And  Verify the VAT user is able to submit the create new case form
#    Then Verify the VAT user is able to view the confirmation message after submitting the create new case



