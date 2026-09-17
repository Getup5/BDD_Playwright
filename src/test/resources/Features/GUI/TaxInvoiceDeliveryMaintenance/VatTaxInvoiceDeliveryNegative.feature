@UI @VAT @VAT_TaxInvoiceDeliveryMaintenance @Negative

Feature: Verify that the VAT user is not able to create New Case in TAX Invoice Delivery Maintenance in the VAT portal,and validate the Negative Cases.

  @VAT_TaxInvoiceDeliveryMaintenance_001
  Scenario: Verify VAT user is able to create New Case without clicking Current Delivery Details in TAX Invoice Delivery
    Given Verify the VAT user is able to access the URL
    When  Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance
    Then  Verify the VAT user is able to submit without Clicking Current Delivery Details fields in the create new case form

  @VAT_TaxInvoiceDeliveryMaintenance_002
  Scenario: Verify VAT user is able to create New Case without clicking Primary Email Linkage Details in TAX Invoice Delivery
    Given Verify the VAT user is able to access the URL
    When  Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance
    Then  Verify the VAT user is able to submit without Clicking Primary Email Linkage Details fields in the create new case form

  @VAT_TaxInvoiceDeliveryMaintenance_003
  Scenario: Verify VAT user is able to create New Case without clicking Secondary Email Linkage Details in  TAX Invoice Delivery
    Given Verify the VAT user is able to access the URL
    When  Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance
    Then  Verify the VAT user is able to submit without Clicking Secondary Email Linkage Details fields in the create new case form

  @VAT_TaxInvoiceDeliveryMaintenance_004
  Scenario: Verify VAT user is able to create New Case without Uploading Files in TAX Invoice Delivery in TAX Invoice Delivery
    Given Verify the VAT user is able to access the URL
    When  Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance
    Then  Verify the VAT user is able to submit without Uploading Files in the create new case form
