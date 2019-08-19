Feature: Maestro
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numubers are 16-19 digits starting with 5018, 5020, 5038, 5893, 6759, or 6761

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "MAESTRO"/"5018000000000009"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid Maestro
	Given the card number is "5018000000000009"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "MAESTRO"

Scenario: Maestro number too short
	Given the card number is "501800000000007"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "MAESTRO"

Scenario: Maestro number too long
	Given the card number is "50180000000000000009"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "MAESTRO"
