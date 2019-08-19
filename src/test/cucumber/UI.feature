@UI
Feature: UI tests
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "VISA"/"4111111111111111"/"0399"/$1000/false

Scenario: UI calls web service
	Given I am using Firefox
	And I open the payment page on "http://localhost:5551"
	And I enter "AABBCCD" as the account key
	And I enter "4111111111111111" as the number
	And I enter "0399" as the expiry
	And I enter $123.45 as the amount
	When I click the pay button
	Then the status should be "APPROVED"
	And the message should be "Approved"
	And the type should be "VISA"
	And the funds should be "PAID"
	
	Given I enter $-1.23 as the amount
	When I click the pay button
	Then the status should be "INVALID_AMOUNT_FORMAT"
	And the type should be ""
