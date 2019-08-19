Feature: American Express
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 15 digits starting with 34 or 37.

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "AMEX"/"341111111111111"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid American Express
	Given the card number is "341111111111111"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "AMEX"

Scenario: American Express number too short
	Given the card number is "34111111111113"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "AMEX"

Scenario: American Express number too long
	Given the card number is "3411111111111110"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "AMEX"
