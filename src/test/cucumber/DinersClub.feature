Feature: Diners Club
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 14 digits starting with 300 or 36

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "DINERS"/"30000000000004"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid Diners Club
	Given the card number is "30000000000004"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "DINERS"

Scenario: Diners Club number too short
	Given the card number is "3000000000007"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "DINERS"

Scenario: Diners Club number too long
	Given the card number is "300000000000007"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "DINERS"
